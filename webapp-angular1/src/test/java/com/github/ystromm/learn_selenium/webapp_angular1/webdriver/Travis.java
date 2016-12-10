package com.github.ystromm.learn_selenium.webapp_angular1.webdriver;

public final class Travis {

    static boolean onTravis() {
        return "true".equals(System.getenv().get("TRAVIS"));
    }

    // hidden
    Travis() {
        // empty
    }
}
