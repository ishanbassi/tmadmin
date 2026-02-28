package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.enumeration.TrademarkTokenType;
import com.bassi.tmapp.service.dto.PartialTokenPhoneticDto;
import com.bassi.tmapp.service.dto.PartialTrademarkTokenDto;
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
        String clientTmNormalizedName,
        String publishedTmNormalizedName,
        List<PartialTrademarkTokenDto> clientTrademarkTokens,
        List<PartialTrademarkTokenDto> savedTT,
        List<PartialTokenPhoneticDto> clientPhonetics,
        List<PartialTokenPhoneticDto> savedTP
    ) {
        double coreScore = computeCoreTokenScore(clientTrademarkTokens, savedTT, clientPhonetics, savedTP);
        if (coreScore == 0.0) {
            return 0.0; // HARD STOP: no CORE phonetic match
        }

        double phraseScore = computePhraseSimilarity(normalize(clientTmNormalizedName), normalize(publishedTmNormalizedName));

        double overlapScore = computeTokenOverlap(clientTrademarkTokens, savedTT);

        double positionScore = computePositionScore(clientTrademarkTokens, savedTT);

        return round(
            coreScore * CORE_MATCH_WEIGHT +
            phraseScore * PHRASE_SIM_WEIGHT +
            overlapScore * TOKEN_OVERLAP_WEIGHT +
            positionScore * POSITION_WEIGHT
        );
    }

    private double computeCoreTokenScore(
        List<PartialTrademarkTokenDto> clientTrademarkTokens,
        List<PartialTrademarkTokenDto> savedTT,
        List<PartialTokenPhoneticDto> clientPhonetics,
        List<PartialTokenPhoneticDto> savedTP
    ) {
        List<PartialTrademarkTokenDto> clientCoreTokens = clientTrademarkTokens
            .stream()
            .filter(t -> t.getTokenType() == TrademarkTokenType.CORE)
            .toList();

        if (clientCoreTokens.isEmpty()) {
            return 0.0;
        }

        Set<String> publishedPhonetics = savedTT
            .stream()
            .flatMap(t -> savedTP.stream())
            .map(PartialTokenPhoneticDto::getPhoneticCode)
            .collect(Collectors.toSet());

        long matched = clientCoreTokens
            .stream()
            .filter(ct -> clientPhonetics.stream().map(PartialTokenPhoneticDto::getPhoneticCode).anyMatch(publishedPhonetics::contains))
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

    private double computeTokenOverlap(List<PartialTrademarkTokenDto> clientTrademarkTokens, List<PartialTrademarkTokenDto> savedTT) {
        Set<String> tokensA = clientTrademarkTokens.stream().map(PartialTrademarkTokenDto::getTokenText).collect(Collectors.toSet());

        Set<String> tokensB = savedTT.stream().map(PartialTrademarkTokenDto::getTokenText).collect(Collectors.toSet());

        if (tokensA.isEmpty() || tokensB.isEmpty()) {
            return 0.0;
        }

        Set<String> intersection = new HashSet<>(tokensA);
        intersection.retainAll(tokensB);

        Set<String> union = new HashSet<>(tokensA);
        union.addAll(tokensB);

        return (double) intersection.size() / union.size();
    }

    private double computePositionScore(List<PartialTrademarkTokenDto> clientTrademarkTokens, List<PartialTrademarkTokenDto> savedTT) {
        double score = 0.0;

        for (PartialTrademarkTokenDto ct : clientTrademarkTokens) {
            for (PartialTrademarkTokenDto pt : savedTT) {
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
