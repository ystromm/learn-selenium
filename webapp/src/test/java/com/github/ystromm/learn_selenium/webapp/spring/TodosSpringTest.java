package com.github.ystromm.learn_selenium.webapp.spring;

import com.github.ystromm.learn_selenium.backend_api.Todo;
import com.github.ystromm.learn_selenium.backend_impl.BackendMain;
import com.github.ystromm.learn_selenium.backend_impl.TodoRepository;
import com.github.ystromm.learn_selenium.webapp.PropertyNames;
import com.github.ystromm.learn_selenium.webapp.WebappMain;
import com.github.ystromm.learn_selenium.webapp.pages.TodosPage;
import com.github.ystromm.learn_selenium.webapp.webdriver.Bys;
import com.github.ystromm.learn_selenium.webapp.webdriver.Firefox;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.github.ystromm.learn_selenium.webapp.webdriver.Screenshot.screenshot;
import static com.github.ystromm.learn_selenium.webapp.webdriver.WebElementMatchers.isDisplayed;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Duration.ONE_SECOND;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebappMain.class, BackendMain.class}, webEnvironment = RANDOM_PORT)
public class TodosSpringTest {
    @Rule
    public TestName testName = new TestName();
    @LocalServerPort
    private int localServerPort;
    private WebDriver webDriver;
    private TodosPage todosPage;
    @Autowired
    private TodoRepository todoRepository;

    @Before
    public void setUpWebDriver() throws IOException {
        // System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
        webDriver = Firefox.firefoxDriver();
        todosPage = new TodosPage(webDriver, "http://localhost:" + localServerPort);
    }

    @Before
    public void setUpProperties() {
        System.setProperty(PropertyNames.BACKEND_SERVER_PORT, Integer.toString(localServerPort));
        System.setProperty(PropertyNames.TODOS_URL, "http://localhost:" + localServerPort);
    }

    @Before
    public void setUpTodoRepository() {
        // the mock gets re-used between test cases, kind of nasty
        reset(todoRepository);
    }

    @After
    public void tearDownWebDriver() throws IOException {
        screenshot(webDriver, getClass().getSimpleName() + "_" + testName.getMethodName());
        webDriver.quit();
    }

    @Test
    public void open_should_get_todos() {
        when(todoRepository.getAll()).thenReturn(emptyList());
        todosPage.open();
        verify(todoRepository).getAll();
    }

    @Test
    public void open_should_not_show_error() {
        when(todoRepository.getAll()).thenReturn(emptyList());
        todosPage.open();
        assertThat(todosPage.getError(), not(isDisplayed()));
    }

    @Ignore("work in progress")
    @Test
    public void open_should_show_no_todos() {
        when(todoRepository.getAll()).thenReturn(emptyList());
        todosPage.open();
        await().atLeast(ONE_SECOND).until(() ->
                assertThat(todosPage.getTodoItems(), empty()));
    }

    @Test
    public void open_should_show_todos() {
        when(todoRepository.getAll()).thenReturn(singletonList(todo()));
        todosPage.open();
        await().atMost(ONE_SECOND).until(() ->
                assertThat(todosPage.getTodoItems(), hasSize(1)));
    }

    @Test
    public void open_should_show_error() {
        when(todoRepository.getAll()).thenThrow(new IllegalStateException("Testing internal error!"));
        todosPage.open();
        assertThat(todosPage.getError(), isDisplayed());
    }

    @Ignore("work in progress")
    @Test
    public void click_checkbox_should_update() {
        when(todoRepository.getAll()).thenReturn(singletonList(todo()));
        when(todoRepository.update(Mockito.any(Todo.class))).thenReturn(Optional.of(checkedTodo()));
        todosPage.open();
        await().atMost(ONE_SECOND).until(() ->
                assertThat(todosPage.getTodoItems(), hasSize(1)));
        todosPage.clickFirstCheckbox();
        await().atMost(ONE_SECOND).until(() ->
                verify(todoRepository).update(checkedTodo()));
    }

    private static Todo todo() {
        return todo(false);
    }

    private static Todo checkedTodo() {
        return todo(true);
    }

    private static Todo todo(boolean done) {
        return Todo.builder().id(1).text("To do!").done(done).build();
    }

    @Ignore("work in progress")
    @Test
    public void check_done_should_not_add_items() {
        addTodoItem();
        final List<WebElement> todos_items_item_done_checkbox_before = webDriver.findElements(Bys.byTestId("todos_items_item_done_checkbox"));
        todos_items_item_done_checkbox_before.get(0).click();
        await().atMost(ONE_SECOND).until(() -> {
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
