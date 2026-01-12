package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.TokenPhonetic;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.TrademarkToken;
import com.bassi.tmapp.domain.enumeration.TrademarkTokenType;
import com.bassi.tmapp.repository.TokenPhoneticRepository;
import com.bassi.tmapp.repository.TrademarkTokenRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SimilarityScorerService {

    SimilarityScorerService() {}

    private static final double CORE_MATCH_WEIGHT = 0.50;
    private static final double PHRASE_SIM_WEIGHT = 0.30;
    private static final double TOKEN_OVERLAP_WEIGHT = 0.15;
    private static final double POSITION_WEIGHT = 0.05;

    public double computeFinalScore(
        Trademark client,
        Trademark published,
        List<TrademarkToken> clientTrademarkTokens,
        List<TrademarkToken> publishedTrademarkTokens,
        List<TokenPhonetic> clientPhonetics,
        List<TokenPhonetic> publishedPhonetics
    ) {
        double coreScore = computeCoreTokenScore(
            client,
            published,
            clientTrademarkTokens,
            publishedTrademarkTokens,
            clientPhonetics,
            publishedPhonetics
        );
        if (coreScore == 0.0) {
            return 0.0; // HARD STOP: no CORE phonetic match
        }

        double phraseScore = computePhraseSimilarity(normalize(client.getName()), normalize(published.getName()));

        double overlapScore = computeTokenOverlap(client, published, clientTrademarkTokens, publishedTrademarkTokens);

        double positionScore = computePositionScore(client, published, clientTrademarkTokens, publishedTrademarkTokens);

        return round(
            coreScore * CORE_MATCH_WEIGHT +
            phraseScore * PHRASE_SIM_WEIGHT +
            overlapScore * TOKEN_OVERLAP_WEIGHT +
            positionScore * POSITION_WEIGHT
        );
    }

    private double computeCoreTokenScore(
        Trademark client,
        Trademark published,
        List<TrademarkToken> clientTrademarkTokens,
        List<TrademarkToken> publishedTrademarkTokens,
        List<TokenPhonetic> clientPhonetics,
        List<TokenPhonetic> publishedTtPhonetics
    ) {
        List<TrademarkToken> clientCoreTokens = clientTrademarkTokens
            .stream()
            .filter(t -> t.getTokenType() == TrademarkTokenType.CORE)
            .toList();

        if (clientCoreTokens.isEmpty()) {
            return 0.0;
        }

        Set<String> publishedPhonetics = publishedTrademarkTokens
            .stream()
            .flatMap(t -> publishedTtPhonetics.stream())
            .map(TokenPhonetic::getPhoneticCode)
            .collect(Collectors.toSet());

        long matched = clientCoreTokens
            .stream()
            .filter(ct -> clientPhonetics.stream().map(TokenPhonetic::getPhoneticCode).anyMatch(publishedPhonetics::contains))
            .count();

        return (double) matched / clientCoreTokens.size();
    }

    private double computePhraseSimilarity(String a, String b) {
        int distance = levenshtein(a, b);
        int maxLen = Math.max(a.length(), b.length());

        if (maxLen == 0) return 1.0;

        return 1.0 - ((double) distance / maxLen);
    }

    private int levenshtein(String s1, String s2) {
        int[] prev = new int[s2.length() + 1];
        int[] curr = new int[s2.length() + 1];

        for (int j = 0; j <= s2.length(); j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            curr[0] = i;
            for (int j = 1; j <= s2.length(); j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
            }
            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }

        return prev[s2.length()];
    }

    private double computeTokenOverlap(
        Trademark a,
        Trademark b,
        List<TrademarkToken> clientTrademarkTokens,
        List<TrademarkToken> publishedTrademarkTokens
    ) {
        Set<String> tokensA = clientTrademarkTokens.stream().map(TrademarkToken::getTokenText).collect(Collectors.toSet());

        Set<String> tokensB = publishedTrademarkTokens.stream().map(TrademarkToken::getTokenText).collect(Collectors.toSet());

        if (tokensA.isEmpty() || tokensB.isEmpty()) {
            return 0.0;
        }

        Set<String> intersection = new HashSet<>(tokensA);
        intersection.retainAll(tokensB);

        Set<String> union = new HashSet<>(tokensA);
        union.addAll(tokensB);

        return (double) intersection.size() / union.size();
    }

    private double computePositionScore(
        Trademark client,
        Trademark published,
        List<TrademarkToken> clientTrademarkTokens,
        List<TrademarkToken> publishedTrademarkTokens
    ) {
        double score = 0.0;

        for (TrademarkToken ct : clientTrademarkTokens) {
            for (TrademarkToken pt : publishedTrademarkTokens) {
                if (ct.getTokenText().equals(pt.getTokenText())) {
                    if (ct.getPosition() == 1 && pt.getPosition() == 1) {
                        score += 1.0;
                    } else if (Math.abs(ct.getPosition() - pt.getPosition()) <= 1) {
                        score += 0.7;
                    } else {
                        score += 0.4;
                    }
                }
            }
        }

        return Math.min(score, 1.0);
    }

    private String normalize(String s) {
        return s.toLowerCase().replaceAll("[^a-z0-9 ]", "").replaceAll("\\s+", " ").trim();
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
