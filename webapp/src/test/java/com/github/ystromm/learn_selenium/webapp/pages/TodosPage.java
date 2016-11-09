package com.github.ystromm.learn_selenium.webapp.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.github.ystromm.learn_selenium.webapp.Bys.byTestId;

public class TodosPage {
    private final WebDriver webDriver;

    public TodosPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebElement getError() {
        return webDriver.findElement(byTestId("todos_error"));
    }

    public WebElement getErrorStatus() {
        return webDriver.findElement(byTestId("todos_error_status"));
    }

    public WebElement getErrorMessage() {
        return webDriver.findElement(byTestId("todos_error_message"));
    }
}
