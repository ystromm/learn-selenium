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

import static com.github.ystromm.learn_selenium.webapp.Screenshot.screenshot;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
        System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
        webDriver = Firefox.firefoxDriver();
        webDriver.get("http://localhost:" + localServerPort);
    }

    @After
    public void tearDownWebDriver() throws IOException {
        screenshot(webDriver, testName);
        webDriver.quit();
    }

    @Test
    public void title_should_be_todos() {
        assertThat(webDriver.getTitle(), equalTo("Todos"));
    }

    @Test
    public void heading_should_be_todos() {
        assertThat(webDriver.findElement(Bys.byTestId("todos_heading")).getText(), equalTo("Todos"));
    }

    @Test
    public void error_should_not_be_displayed() {
        assertThat(webDriver.findElement(Bys.byTestId("todos_error")).isDisplayed(), equalTo(false));
    }

    @Test
    public void add_should_clear_text() {
        final WebElement todos_add_input = webDriver.findElement(Bys.byTestId("todos_add_input"));
        final WebElement todos_add_button = webDriver.findElement(Bys.byTestId("todos_add_button"));
        todos_add_input.sendKeys("To do item");
        todos_add_button.click();
        await().atMost(Duration.ONE_SECOND).until(() -> assertThat(todos_add_input.getAttribute("value"), isEmptyString()));
    }


}
