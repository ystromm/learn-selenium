package com.github.ystromm.learn_selenium.webapp_angular1.webdriver;

import com.google.common.io.Files;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public final class Screenshot {

    public static void screenshot(WebDriver webDriver, String testName) throws IOException {
        final String testReportDir = System.getProperty("testReportDir", ".");
        saveScreenshot(webDriver, testReportDir + "/" + testName + "_screenshot.png");
    }

    public static void saveScreenshot(final WebDriver webDriver, final String screenshotFileName) throws IOException {
        if (WebDriverUtil.isConnected(webDriver)) {
            final File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot, new File(screenshotFileName));
        }
    }

    // hidden
    private Screenshot() {
        // empty
    }
}
