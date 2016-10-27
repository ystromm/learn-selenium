package com.github.ystromm.learn_selenium.webapp;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.openqa.selenium.WebElement;

import static org.hamcrest.Matchers.equalTo;

public final class WebElementMatchers {

    public static Matcher<WebElement> withText(String text) {
        return withText(equalTo(text));
    }

    public static Matcher<WebElement> withText(Matcher<String> matcher) {
        return new FeatureMatcher<WebElement, String>(matcher, "text", "text") {
            @Override
            protected String featureValueOf(WebElement webElement) {
                return webElement.getText();
            }
        };
    }

    public static Matcher<WebElement> withValue(Matcher<String> matcher) {
        return new FeatureMatcher<WebElement, String>(matcher, "value", "value") {
            @Override
            protected String featureValueOf(WebElement webElement) {
                return webElement.getAttribute("value");
            }
        };
    }

    public static Matcher<WebElement> checked() {
        return checked(true);
    }

    public static Matcher<WebElement> unchecked() {
        return checked(false);
    }

    public static Matcher<WebElement> checked(boolean checked) {
        return new FeatureMatcher<WebElement, String>(equalTo(checked), "checked", "checked") {
            @Override
            protected String featureValueOf(WebElement webElement) {
                return webElement.getAttribute("checked");
            }
        };
    }

    public static Matcher<WebElement> displayed() {
        return displayed(true);
    }

    public static Matcher<WebElement> displayed(boolean isDisplayed) {
        return new FeatureMatcher<WebElement, Boolean>(equalTo(isDisplayed), "isDisplayed", "isDisplayed") {

            @Override
            protected Boolean featureValueOf(WebElement webElement) {
                return webElement.isDisplayed();
            }
        };
    }


    // hidden
    private WebElementMatchers() {
        // empty
    }
}
