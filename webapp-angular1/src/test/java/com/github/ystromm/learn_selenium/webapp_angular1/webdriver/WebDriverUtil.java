package com.github.ystromm.learn_selenium.webapp_angular1.webdriver;

import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

public final class WebDriverUtil {
    public static boolean isConnected(WebDriver webDriver) {
        try {
            webDriver.getTitle();
            return true;
        } catch (UnreachableBrowserException | SessionNotCreatedException e) {
            return false;
        }
    }

    // hidden
    private WebDriverUtil() {
        // empty
    }
}
