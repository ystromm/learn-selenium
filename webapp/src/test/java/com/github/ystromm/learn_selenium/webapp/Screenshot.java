package com.github.ystromm.learn_selenium.webapp;

import com.google.common.io.Files;
import org.junit.rules.TestName;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

final class Screenshot {

    static void screenshot(WebDriver webDriver, TestName testName) throws IOException {
        final String testReportDir = System.getProperty("testReportDir", ".");
        saveScreenshot(webDriver, testReportDir + "/" + testName.getMethodName() + "_screenshot.png");
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
