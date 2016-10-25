package com.github.ystromm.learn_selenium.webapp;

import com.google.common.io.Files;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

final class ScreenshotHelper {

    public static void saveScreenshot(final WebDriver webDriver, final String screenshotFileName) throws IOException {
        final File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        Files.copy(screenshot, new File(screenshotFileName));
    }

    // hidden
    private ScreenshotHelper() {
        // empty
    }
}
