package com.github.ystromm.learn_selenium.webapp;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class AddTodoTest {
    private WebDriver webDriver;

    @Before
    public void openBrowser() {
        System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
        webDriver = new ChromeDriver();
        webDriver.get("http://localhost:8080");
    }
    @Test
    public void xxx() {

    }
}
