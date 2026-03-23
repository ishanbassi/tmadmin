package com.bassi.tmapp.service.webScraping;

import java.util.Random;

public class HumanDelay {

    private static final Random random = new Random();

    // Short pause between keystrokes (50ms - 150ms)
    public static void keystroke() {
        sleep(50 + random.nextInt(100));
    }

    // Pause after clicking something (500ms - 1500ms)
    public static void afterClick() {
        sleep(500 + random.nextInt(1000));
    }

    // Pause before doing next action (1s - 3s)
    public static void betweenActions() {
        sleep(1000 + random.nextInt(2000));
    }

    // Longer pause like reading something (2s - 5s)
    public static void reading() {
        sleep(2000 + random.nextInt(3000));
    }

    // Custom range pause
    public static void between(int minMs, int maxMs) {
        sleep(minMs + random.nextInt(maxMs - minMs));
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
