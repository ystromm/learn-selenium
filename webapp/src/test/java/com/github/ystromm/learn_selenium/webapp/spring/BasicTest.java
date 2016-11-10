package com.github.ystromm.learn_selenium.webapp.spring;

import com.github.ystromm.learn_selenium.backend_impl.BackendMain;
import com.github.ystromm.learn_selenium.webapp.WebappMain;
import com.github.ystromm.learn_selenium.webapp.webdriver.Bys;
import com.github.ystromm.learn_selenium.webapp.webdriver.Firefox;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.github.ystromm.learn_selenium.webapp.webdriver.Screenshot.screenshot;
import static com.github.ystromm.learn_selenium.webapp.webdriver.WebElementMatchers.isDisplayed;
import static com.github.ystromm.learn_selenium.webapp.webdriver.WebElementMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebappMain.class, BackendMain.class}, webEnvironment = RANDOM_PORT)
public class BasicTest {
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
        screenshot(webDriver, testName.getMethodName());
        webDriver.quit();
    }

    @Test
    public void title_should_be_todos() {
        assertThat(webDriver.getTitle(), equalTo("Todos"));
    }

    @Test
    public void heading_should_be_todos() {
        assertThat(webDriver.findElement(Bys.byTestId("todos_heading")), withText(equalTo("Todos")));
    }

    @Ignore("work in progress")
    @Test
    public void error_should_not_be_displayed() {
        assertThat(webDriver.findElement(Bys.byTestId("todos_error")), not(isDisplayed()));
    }
}
