package com.github.ystromm.learn_selenium.webapp.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

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

    public void addTodoItem(String text) {
        final WebElement todos_add_input = webDriver.findElement(byTestId("todos_add_input"));
        final WebElement todos_add_button = webDriver.findElement(byTestId("todos_add_button"));
        todos_add_input.sendKeys(text);
        todos_add_button.click();
    }

    public List<WebElement> getTodoItems() {
        return webDriver.findElements(byTestId("todos_items_item"));
    }
}
