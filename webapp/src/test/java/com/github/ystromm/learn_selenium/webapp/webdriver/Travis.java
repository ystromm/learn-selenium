package com.github.ystromm.learn_selenium.webapp.webdriver;

public final class Travis {

    static boolean onTravis() {
        return "true".equals(System.getenv().get("TRAVIS"));
    }

    // hidden
    Travis() {
        // empty
    }
}
