package com.github.ystromm.learn_selenium.webapp_angular1.wiremock;

import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.ystromm.learn_selenium.backend_api.Todo;
import com.github.ystromm.learn_selenium.webapp_angular1.webdriver.Firefox;
import com.github.ystromm.learn_selenium.webapp_angular1.PropertyNames;
import com.github.ystromm.learn_selenium.webapp_angular1.WebappMain;
import com.github.ystromm.learn_selenium.webapp_angular1.pages.TodosPage;
import org.awaitility.Duration;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.ystromm.learn_selenium.webapp_angular1.webdriver.Bys.byTestId;
import static com.github.ystromm.learn_selenium.webapp_angular1.webdriver.Screenshot.screenshot;
import static com.github.ystromm.learn_selenium.webapp_angular1.webdriver.WebElementMatchers.isDisplayed;
import static com.github.ystromm.learn_selenium.webapp_angular1.webdriver.WebElementMatchers.withText;
import static com.github.ystromm.learn_selenium.webapp_angular1.webdriver.WebElementMatchers.withValue;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebappMain.class}, webEnvironment = RANDOM_PORT)
public class TodosWiremockTest {
    private static final String ERROR_MESSAGE = "Error message";
    private static final String API_TODO = "/api/todo";
    @Rule
    public TestName testName = new TestName();
    @LocalServerPort
    private int localServerPort;
    private WebDriver webDriver;
    @Rule
    public WireMockRule todosService = new WireMockRule();
    private TodosPage todosPage;


    @Before
    public void setUpWebDriver() throws IOException {
        // System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
        webDriver = Firefox.firefoxDriver();
        todosPage = new TodosPage(webDriver, "http://localhost:" + localServerPort);
    }

    @Before
    public void setUpTodosService() {
        todosService.stubFor(options(urlEqualTo(API_TODO)).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Access-Control-Allow-Origin", "http://localhost:" + localServerPort)
                .withHeader("Access-Control-Allow-Methods", "GET, POST")
                .withHeader("Access-Control-Allow-Headers", "accept, content-type")
        ));
    }

    @Before
    public void setUpBackendServerPort() {
        System.setProperty(PropertyNames.BACKEND_SERVER_PORT, Integer.toString(todosService.port()));
        System.setProperty(PropertyNames.TODOS_URL, "http://localhost:" + Integer.toString(todosService.port()));
    }

    @After
    public void tearDownWebDriver() throws IOException {
        screenshot(webDriver, getClass().getSimpleName() + "_" + testName.getMethodName());
        webDriver.quit();
    }

    @Test
    public void open_should_get_apiTodos() {
        stubForApiTodoWillReturnTodos();
        todosPage.open();
        verify(getRequestedFor(urlEqualTo(API_TODO)));
    }

    @Test
    public void open_should_show_no_todos() {
        stubForApiTodoWillReturnTodos();
        todosPage.open();
        assertThat(todosPage.getTodoItems(), empty());
    }

    @Ignore("work in progress")
    @Test
    public void open_should_show_one_todo() {
        final Todo todo = Todo.builder().id(1).text("Text").done(false).build();
        stubForApiTodoWillReturnTodos(todo);
        todosPage.open();
        assertThat(todosPage.getTodoItems(), hasSize(1));
    }

    @Ignore("work in progress")
    @Test
    public void open_should_show_error() {
        stubForApiTodoWillReturnError();
        todosPage.open();
        assertThat(todosPage.getError(), isDisplayed());
    }

    @Ignore("work in progress")
    @Test
    public void add_should_clear_text() {
        stubForApiTodoWillReturnTodos();
        todosPage.open();
        todosPage.addTodoItem("To do!");
        await().atMost(Duration.ONE_SECOND).until(() ->
                assertThat(webDriver.findElement(byTestId("todos_add_input")), withValue(isEmptyString())));
    }

    @Ignore("work in progress")
    @Test
    public void add_should_post() {
        stubForApiTodoWillReturnTodos();
        todosPage.open();
        todosPage.addTodoItem("To do!");
        verify(postRequestedFor(urlEqualTo(API_TODO)));
    }


    @Ignore("work in progress")
    @Test
    public void add_should_add_an_item() {
        stubForApiTodoWillReturnTodos();
        todosPage.open();
        await().atMost(Duration.ONE_SECOND).until(() ->
                assertThat(todosPage.getTodoItems(), empty()));
        final Todo todo = Todo.builder().id(1).text("Some text").done(false).build();
        todosService.stubFor(post(urlEqualTo(API_TODO)).willReturn(aResponse()
                .withStatus(200)
                .withBody(Json.write(todo))
                .withHeader("Content-Type", "application/json")
        ));
        stubForApiTodoWillReturnTodos(todo);

        todosPage.addTodoItem("To do!");
        await().atMost(Duration.ONE_SECOND).until(() ->
                assertThat(todosPage.getTodoItems(), hasSize(1)));
    }

    @Ignore("work in progress")
    @Test
    public void add_should_add_an_item_with_the_given_text() {
        stubForApiTodoWillReturnTodos();
        todosPage.open();
        final String text = "To do item " + new Random().nextInt();
        final Todo todo = Todo.builder().id(1).text(text).done(false).build();
        todosService.stubFor(post(urlEqualTo(API_TODO)).willReturn(aResponse()
                .withStatus(200)
                .withBody(Json.write(todo))
                .withHeader("Content-Type", "application/json")
        ));
        stubForApiTodoWillReturnTodos(todo);
        todosPage.addTodoItem(text);
        await().atMost(Duration.ONE_SECOND).until(() ->
                assertThat(webDriver.findElements(byTestId("todos_items_item_text")), hasItem(withText(equalTo(text)))));
    }


    @Ignore("work in progress")
    @Test
    public void open_should_show_error_status() {
        stubForApiTodoWillReturnError();
        todosPage.open();
        assertThat(todosPage.getErrorStatus(), withText("401"));
    }


    @Ignore("work in progress")
    @Test
    public void open_should_show_error_message() {
        stubForApiTodoWillReturnError();
        todosPage.open();
        assertThat(todosPage.getErrorMessage(), withText(ERROR_MESSAGE));
    }

    private void stubForApiTodoWillReturnTodos(Todo... todos) {
        final String todosJson = Json.write(Arrays.asList(todos));
        todosService.stubFor(get(urlEqualTo(API_TODO)).willReturn(aResponse()
                .withBody(todosJson)
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
        ));
    }

    private void stubForApiTodoWillReturnError() {
        todosService.stubFor(get(urlEqualTo(API_TODO)).willReturn(aResponse()
                .withStatus(401)
                .withStatusMessage(ERROR_MESSAGE)));
    }

}
