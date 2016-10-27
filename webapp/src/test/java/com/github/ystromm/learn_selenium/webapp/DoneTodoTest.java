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

import static com.github.ystromm.learn_selenium.webapp.Screenshot.screenshot;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebappMain.class, BackendMain.class}, webEnvironment = RANDOM_PORT)
public class DoneTodoTest {
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
    public void check_done_should_toggle_checkbox() {
        addTodoItem();
        final List<WebElement> todos_items_item_done_checkbox_before = webDriver.findElements(Bys.byTestId("todos_items_item_done_checkbox"));
        final boolean checkedBefore = Boolean.parseBoolean(todos_items_item_done_checkbox_before.get(0).getAttribute("checked"));
        todos_items_item_done_checkbox_before.get(0).click();
        await().atMost(Duration.ONE_SECOND).until(() -> {
                    final List<WebElement> todos_items_item_done_checkbox_after = webDriver.findElements(Bys.byTestId("todos_items_item_done_checkbox"));
                    final boolean checkedAfter = Boolean.parseBoolean(todos_items_item_done_checkbox_after.get(0).getAttribute("checked"));
                    assertThat(checkedBefore, equalTo(!checkedAfter));
                }
        );
    }

    @Test
    public void check_done_should_not_add_items() {
        addTodoItem();
        final List<WebElement> todos_items_item_done_checkbox_before = webDriver.findElements(Bys.byTestId("todos_items_item_done_checkbox"));
        todos_items_item_done_checkbox_before.get(0).click();
        await().atMost(Duration.ONE_SECOND).until(() -> {
                    assertThat(webDriver.findElements(Bys.byTestId("todos_items_item_done_checkbox")), hasSize(todos_items_item_done_checkbox_before.size()));
                }
        );
    }

    private void addTodoItem() {
        final WebElement todos_add_input = webDriver.findElement(Bys.byTestId("todos_add_input"));
        final WebElement todos_add_button = webDriver.findElement(Bys.byTestId("todos_add_button"));
        todos_add_input.sendKeys("To do item");
        todos_add_button.click();
    }
}
