package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.enumeration.TrademarkType;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SlugUtil {

    private SlugUtil() {}

    public static String generate(String name, Integer classNo, Long applicationNumber, TrademarkType markType) {
        String base = resolveBase(name, markType);
        return base + "-class-" + classNo + "-" + applicationNumber;
    }

    private static String resolveBase(String name, TrademarkType markType) {
        String normalizedName = normalize(name);

        if (!normalizedName.isEmpty()) {
            return normalizedName;
        }

        // fallback for device marks
        if (markType != null && markType == TrademarkType.IMAGEMARK) {
            return "device-mark";
        }

        return "trademark";
    }

    private static String normalize(String input) {
        if (input == null) return "";

        String slug = Normalizer.normalize(input, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "")
            .toLowerCase()
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("^-|-$", "");

        return slug;
    }

    private static final Pattern APP_NO_PATTERN = Pattern.compile("(\\d+)$");

    /**
     * Extract application number from slug. Example: nike-class-25-5348291 ->
     * 5348291
     */
    public static String extractApplicationNumber(String slug) {
        if (slug == null || slug.isBlank()) {
            return null;
        }

        Matcher matcher = APP_NO_PATTERN.matcher(slug);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
}
