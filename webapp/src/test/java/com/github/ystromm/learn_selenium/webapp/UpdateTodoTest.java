package com.github.ystromm.learn_selenium.webapp;

import com.github.ystromm.learn_selenium.backend_impl.BackendMain;
import com.google.common.base.Charsets;
import org.awaitility.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebappMain.class, BackendMain.class}, webEnvironment = RANDOM_PORT)
@DirtiesContext
public class UpdateTodoTest {
    @Rule
    public TestName testName = new TestName();
    @LocalServerPort
    private int localServerPort;
    private WebDriver webDriver;

    @Before
    public void openBrowser() throws IOException {
        System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
        final FirefoxBinary firefoxBinary = onTravis() ? new FirefoxBinary(new File(getFirefoxPath())): new FirefoxBinary();
        webDriver = new FirefoxDriver(firefoxBinary, new FirefoxProfile());
        webDriver.get("http://localhost:" + localServerPort);
    }

    private static boolean onTravis() {
        return "true".equals(System.getenv().get("TRAVIS"));
    }

    private static String getFirefoxPath() throws IOException {
        final ProcessBuilder processBuilder = new ProcessBuilder("which", "firefox");
        processBuilder.redirectErrorStream(true);
        final Process process = processBuilder.start();
        try (final InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), Charsets.UTF_8);
             final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            return bufferedReader.readLine();
        }
    }

    @After
    public void tearDownWebDriver() throws IOException {
        final String testReportDir = System.getProperty("testReportDir");
        ScreenshotHelper.saveScreenshot(webDriver, testReportDir + "/" + testName.getMethodName() + "_screenshot.png");
        webDriver.quit();
    }

    @Test
    public void title_should_be_todos() {
        assertThat(webDriver.getTitle(), equalTo("Todos"));
    }

    @Test
    public void heading_should_be_todos() {
        assertThat(webDriver.findElement(byTestId("todos_heading")).getText(), equalTo("Todos"));
    }

    @Test
    public void error_should_not_be_displayed() {
        assertThat(webDriver.findElement(byTestId("todos_error")).isDisplayed(), equalTo(false));
    }

    @Test
    public void add_should_clear_text() {
        final WebElement todos_add_input = webDriver.findElement(byTestId("todos_add_input"));
        final WebElement todos_add_button = webDriver.findElement(byTestId("todos_add_button"));
        todos_add_input.sendKeys("To do item");
        todos_add_button.click();
        await().atMost(Duration.ONE_SECOND).until(() -> assertThat(todos_add_input.getAttribute("value"), isEmptyString()));
    }


    private static By byTestId(String value) {
        return By.cssSelector("*[data-test-id='" + value + "']");
    }
}
