package com.bassi.tmapp.service.webScraping;

import java.util.Random;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public class HumanTyping {

    private static final Random random = new Random();

    // Types each character with a random delay, like a real person
    public static void type(WebElement element, String text) {
        element.click();
        HumanDelay.afterClick();

        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            HumanDelay.keystroke();

            // Occasionally pause mid-word (simulates thinking)
            if (random.nextInt(10) == 0) {
                HumanDelay.between(100, 200);
            }
        }
    }

    // Clears field slowly, then types
    public static void clearAndType(WebElement element, String text) {
        element.click();
        HumanDelay.afterClick();

        // Select all and delete (human-like clear)
        element.sendKeys(Keys.CONTROL + "a");
        HumanDelay.keystroke();
        element.sendKeys(Keys.DELETE);
        HumanDelay.between(100, 150);

        type(element, text);
    }
}
