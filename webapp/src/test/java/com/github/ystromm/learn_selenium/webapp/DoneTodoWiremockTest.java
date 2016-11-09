package com.github.ystromm.learn_selenium.webapp;

import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.ystromm.learn_selenium.backend_api.Todo;
import com.github.ystromm.learn_selenium.webapp.pages.TodosPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.ystromm.learn_selenium.webapp.Bys.byTestId;
import static com.github.ystromm.learn_selenium.webapp.Screenshot.screenshot;
import static com.github.ystromm.learn_selenium.webapp.WebElementMatchers.isDisplayed;
import static com.github.ystromm.learn_selenium.webapp.WebElementMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebappMain.class}, webEnvironment = RANDOM_PORT)
public class DoneTodoWiremockTest {
    public static final String ERROR_MESSAGE = "Error message";
    public static final String API_TODO = "/api/todo";
    @Rule
    public TestName testName = new TestName();
    @LocalServerPort
    private int localServerPort;
    private WebDriver webDriver;
    @Rule
    public WireMockRule todoService = new WireMockRule();
    private TodosPage todosPage;


    @Before
    public void setUpWebDriver() throws IOException {
        // System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
        webDriver = Firefox.firefoxDriver();
        todosPage = new TodosPage(webDriver);
    }


    @Before
    public void setUpBackendServerPort() {
        System.setProperty(PropertyNames.BACKEND_SERVER_PORT, Integer.toString(todoService.port()));
    }

    @After
    public void tearDownWebDriver() throws IOException {
        screenshot(webDriver, testName);
        webDriver.quit();
    }

    @Test
    public void should_get_todos() {
        openTodosPage();
        verify(getRequestedFor(urlEqualTo(API_TODO)));
    }

    @Test
    public void should_show_no_todos() {
        stubForApiTodoWillReturnTodos();
        openTodosPage();
        assertThat(webDriver.findElements(byTestId("todos_items_item")), empty());
    }

    @Test
    public void should_show_one_todo() {
        final Todo todo = Todo.builder().id(1).text("Text").done(false).build();
        stubForApiTodoWillReturnTodos(todo);
        openTodosPage();
        assertThat(webDriver.findElements(byTestId("todos_items_item")), hasSize(1));
    }

    @Test
    public void should_show_error() {
        stubForApiTodoWillReturnError();
        openTodosPage();
        assertThat(todosPage.getError(), isDisplayed());
    }


    @Test
    public void should_show_error_status() {
        stubForApiTodoWillReturnError();
        openTodosPage();
        assertThat(todosPage.getErrorStatus(), withText("401"));
    }


    @Test
    public void should_show_error_message() {
        stubForApiTodoWillReturnError();
        openTodosPage();
        assertThat(todosPage.getErrorMessage(), withText(ERROR_MESSAGE));
    }

    private void stubForApiTodoWillReturnTodos(Todo... todos) {
        final String todosJson = Json.write(Arrays.asList(todos));
        todoService.stubFor(get(urlEqualTo(API_TODO)).willReturn(aResponse()
                .withBody(todosJson)
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
        ));
    }

    private void stubForApiTodoWillReturnError() {
        todoService.stubFor(get(urlEqualTo(API_TODO)).willReturn(aResponse()
                .withStatus(401)
                .withStatusMessage(ERROR_MESSAGE)));
    }

    private void openTodosPage() {
        webDriver.get("http://localhost:" + localServerPort);
    }
}
