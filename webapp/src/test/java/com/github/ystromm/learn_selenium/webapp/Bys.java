package com.github.ystromm.learn_selenium.webapp;

import org.openqa.selenium.By;

public final class Bys {

    public static By byTestId(String value) {
        return By.cssSelector("*[data-test-id='" + value + "']");
    }

    // hidden
    private Bys() {
        // empty
    }
}
