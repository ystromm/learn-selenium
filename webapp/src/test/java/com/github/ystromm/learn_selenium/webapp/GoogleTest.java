package com.github.ystromm.learn_selenium.webapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Ignore
public class GoogleTest {
    private WebDriver webDriver;

    @Before
    public void openBrowser() {
        System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
        webDriver = new ChromeDriver();
        webDriver.get("http://www.google.se");
    }

    @Test
    public void title_should_be_google() {
        assertThat(webDriver.getTitle(), equalTo("Google"));
    }

    @Test
    public void search_for_should_set_title() {
        final WebElement searchField = webDriver.findElement(By.name("q"));
        searchField.sendKeys("Drupal!");
        searchField.submit();
        // should have awaitility here
        assertThat(new WebDriverWait(webDriver, 10).<Boolean>until((ExpectedCondition<Boolean>) providedWebDriver -> {
            return providedWebDriver.getTitle().toLowerCase().startsWith("drupal!");
        }), equalTo(true));
    }

    @After
    public void saveScreenshotAndCloseBrowser() throws IOException {
        ScreenshotHelper.saveScreenshot(webDriver, "screenshot.png");
        webDriver.quit();
    }

}
