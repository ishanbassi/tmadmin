package com.bassi.tmapp.service.webScraping;

import java.time.Duration;
import java.util.Random;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class HumanClick {

    // Pause before clicking, then click, then pause after
    public static void click(WebElement element) {
        HumanDelay.betweenActions(); // pause before
        element.click();
        HumanDelay.afterClick(); // pause after
    }

    // Move mouse to element first, then click (most realistic)
    public static void moveAndClick(WebDriver driver, WebElement element) {
        Actions actions = new Actions(driver);
        HumanDelay.betweenActions();

        actions
            .moveToElement(element)
            .pause(Duration.ofMillis(300 + new Random().nextInt(400))) // hover briefly
            .click()
            .perform();

        HumanDelay.afterClick();
    }
}
