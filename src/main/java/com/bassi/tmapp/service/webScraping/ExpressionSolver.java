package com.bassi.tmapp.service.webScraping;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionSolver {

    public static String solve(String expression) {
        expression = expression.trim().toLowerCase();

        // Pattern 1: Basic arithmetic - "2 + 7 = ?"
        if (expression.matches(".*\\d+\\s*[+\\-*]\\s*\\d+.*")) {
            return solveArithmetic(expression);
        }

        // Pattern 2: "Enter the first/second/third/last number in 3 3 1 8"
        if (expression.contains("enter the")) {
            return solvePositional(expression);
        }

        return "";
    }

    private static String solveArithmetic(String expr) {
        // Extract: number operator number
        Pattern p = Pattern.compile("(\\d+)\\s*([+\\-*])\\s*(\\d+)");
        Matcher m = p.matcher(expr);

        if (m.find()) {
            int a = Integer.parseInt(m.group(1));
            String op = m.group(2);
            int b = Integer.parseInt(m.group(3));

            return switch (op) {
                case "+" -> String.valueOf(a + b);
                case "-" -> String.valueOf(a - b);
                case "*" -> String.valueOf(a * b);
                default -> "";
            };
        }
        return "";
    }

    private static String solvePositional(String expr) {
        // Extract all numbers from the sequence
        // e.g. "enter the last number in 3 3 1 8"
        Pattern seqPattern = Pattern.compile("in\\s+([\\d\\s]+)");
        Matcher seqMatcher = seqPattern.matcher(expr);

        if (!seqMatcher.find()) return "";

        String[] parts = seqMatcher.group(1).trim().split("\\s+");
        List<String> numbers = Arrays.asList(parts);

        if (expr.contains("first")) return numbers.get(0);
        if (expr.contains("second")) return numbers.get(1);
        if (expr.contains("third")) return numbers.get(2);
        if (expr.contains("fourth")) return numbers.get(3);
        if (expr.contains("last")) return numbers.get(numbers.size() - 1);

        return "";
    }
}
