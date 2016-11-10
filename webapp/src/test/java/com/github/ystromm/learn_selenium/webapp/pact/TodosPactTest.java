package com.github.ystromm.learn_selenium.webapp.pact;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ystromm.learn_selenium.backend_api.Todo;
import com.github.ystromm.learn_selenium.webapp.webdriver.Firefox;
import com.github.ystromm.learn_selenium.webapp.WebappMain;
import com.github.ystromm.learn_selenium.webapp.pages.TodosPage;
import org.apache.http.entity.ContentType;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.github.ystromm.learn_selenium.webapp.PropertyNames.BACKEND_SERVER_PORT;
import static com.github.ystromm.learn_selenium.webapp.PropertyNames.TODOS_URL;
import static com.github.ystromm.learn_selenium.webapp.webdriver.Screenshot.screenshot;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebappMain.class}, webEnvironment = RANDOM_PORT)
public class TodosPactTest {
    @Rule
    public PactProviderRule pactProviderRule = new PactProviderRule("test_provider", "localhost", 8080, this);
    @Rule
    public TestName testName = new TestName();
    @LocalServerPort
    private int localServerPort;
    private WebDriver webDriver;
    private TodosPage todosPage;

    @Before
    public void openBrowser() throws IOException {
        // System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
        webDriver = Firefox.firefoxDriver();
        webDriver.get("http://localhost:" + localServerPort);
        todosPage = new TodosPage(webDriver, "http://localhost:" + localServerPort);
    }

    @Before
    public void setUpBackend() {
        System.setProperty(BACKEND_SERVER_PORT, Integer.toString(pactProviderRule.getConfig().getPort()));
        System.setProperty(TODOS_URL, "http://localhost:" + Integer.toString(pactProviderRule.getConfig().getPort()));
    }

    @After
    public void tearDownWebDriver() throws IOException {
        screenshot(webDriver, getClass().getSimpleName() + "_" + testName.getMethodName());
        webDriver.quit();
    }

    @Test
    @PactVerification
    public void open_should_get_todos() {
        todosPage.open();
        // verify GET api/todo
    }


    @Ignore("work in progress")
    @Test
    @PactVerification
    public void open_should_show_todos() {
        todosPage.open();
        assertThat(todosPage.getTodoItems(), hasSize(1));
    }

    @Pact(consumer="test_consumer") // will default to the provider name from mockProvider
    public PactFragment createFragment(PactDslWithProvider builder) throws JsonProcessingException {
        final Todo todo = Todo.builder().id(1).text("To do!").done(false).build();
        final List<Todo> todos = Arrays.asList(todo);
        final String todosJson = new ObjectMapper().writer().writeValueAsString(todos);
        return builder
                .given("test state")
                .uponReceiving("ExampleJavaConsumerPactRuleTest test interaction")
                .path("/api/todo")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(todosJson, ContentType.APPLICATION_JSON)
                .toFragment();
    }

}