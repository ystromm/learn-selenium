package com.github.ystromm.learn_selenium.webapp.pages;

import com.github.ystromm.learn_selenium.webapp.webdriver.Bys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.github.ystromm.learn_selenium.webapp.webdriver.Bys.byTestId;

public class TodosPage {
    private final WebDriver webDriver;
    private final String baseUrl;

    public TodosPage(WebDriver webDriver, String baseUrl) {
        this.webDriver = webDriver;
        this.baseUrl = baseUrl;
    }

    public void open() {
        webDriver.get(baseUrl);
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

    public void clickFirstCheckbox() {
        final List<WebElement> todos_items_item_done_checkboxes = webDriver.findElements(Bys.byTestId("todos_items_item_done_checkbox"));
        todos_items_item_done_checkboxes.get(0).click();

    }

    public void clickFirstDelete() {
        final List<WebElement> todos_items_item_delete_buttons = webDriver.findElements(Bys.byTestId("todos_items_item_delete_button"));
        todos_items_item_delete_buttons.get(0).click();
    }
}
