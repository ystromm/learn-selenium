package com.github.ystromm.learn_selenium.webapp;

import com.google.common.io.Files;
import com.google.common.primitives.Booleans;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class XxxTest {
    private WebDriver webDriver;

    @Before
    public void openBrowser() {
        webDriver = new ChromeDriver();
        webDriver.get("www.google.se");
    }

    @Test
    public void xxx() {

        // todo get
        assertThat(webDriver.getTitle(), equalTo("Google"));
        final WebElement searchField = webDriver.findElement(By.name("q"));
        searchField.sendKeys("Drupal!");
        searchField.submit();
        final Boolean aBoolean = new WebDriverWait(webDriver, 10).<Boolean>until((ExpectedCondition<Boolean>) webDriver1 -> {
            return webDriver1.getTitle().toLowerCase().startsWith("drupal!");
        });
        assertThat(aBoolean, equalTo(true));
    }

    @After
    public void saveScreenshotAndCloseBrowser() throws IOException {
        ScreenshotHelper.saveScreenshot(webDriver, "screenshot.png");
        webDriver.quit();
    }

    private static final class ScreenshotHelper {

        public static void saveScreenshot(final WebDriver webDriver, final String screenshotFileName) throws IOException {
            final File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot, new File(screenshotFileName));
        }

        // hidden
        private ScreenshotHelper() {
            // empty
        }
    }
}
