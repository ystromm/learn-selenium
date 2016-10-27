package com.github.ystromm.learn_selenium.webapp;

import com.github.ystromm.learn_selenium.backend_impl.BackendMain;
import org.awaitility.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static com.github.ystromm.learn_selenium.webapp.Bys.byTestId;
import static com.github.ystromm.learn_selenium.webapp.Screenshot.screenshot;
import static com.github.ystromm.learn_selenium.webapp.WebElementMatchers.withText;
import static com.github.ystromm.learn_selenium.webapp.WebElementMatchers.withValue;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyString;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebappMain.class, BackendMain.class}, webEnvironment = RANDOM_PORT)
public class AddTodoTest {
    @Rule
    public TestName testName = new TestName();
    @LocalServerPort
    private int localServerPort;
    private WebDriver webDriver;

    @Before
    public void openBrowser() throws IOException {
        // System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
        webDriver = Firefox.firefoxDriver();
        webDriver.get("http://localhost:" + localServerPort);
    }

    @After
    public void tearDownWebDriver() throws IOException {
        screenshot(webDriver, testName);
        webDriver.quit();
    }

    @Test
    public void add_should_clear_text() {
        addTodoItem("To do!");
        await().atMost(Duration.ONE_SECOND).until(() ->
                assertThat(webDriver.findElement(byTestId("todos_add_input")), withValue(isEmptyString())));
    }

    @Test
    public void add_should_add_an_item() {
        final List<WebElement> todos_items_item_before = webDriver.findElements(byTestId("todos_items_item"));
        addTodoItem("To do!");
        await().atMost(Duration.ONE_SECOND).until(() ->
                assertThat(webDriver.findElements(byTestId("todos_items_item")), hasSize(todos_items_item_before.size() + 1)));
    }

    @Test
    public void add_should_add_an_item_with_the_given_text() {
        final String text = "To do item " + new Random().nextInt();
        addTodoItem(text);
        await().atMost(Duration.ONE_SECOND).until(() ->
                assertThat(webDriver.findElements(byTestId("todos_items_item_text")), hasItem(withText(equalTo(text)))));
    }

    private void addTodoItem(String text) {
        final WebElement todos_add_input = webDriver.findElement(byTestId("todos_add_input"));
        final WebElement todos_add_button = webDriver.findElement(byTestId("todos_add_button"));
        todos_add_input.sendKeys(text);
        todos_add_button.click();
    }
}
