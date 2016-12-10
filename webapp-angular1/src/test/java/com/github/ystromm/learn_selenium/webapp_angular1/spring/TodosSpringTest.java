package com.github.ystromm.learn_selenium.webapp_angular1.spring;

import com.github.ystromm.learn_selenium.backend_api.Todo;
import com.github.ystromm.learn_selenium.backend_impl.BackendMain;
import com.github.ystromm.learn_selenium.backend_impl.TodoRepository;
import com.github.ystromm.learn_selenium.webapp_angular1.PropertyNames;
import com.github.ystromm.learn_selenium.webapp_angular1.WebappMain;
import com.github.ystromm.learn_selenium.webapp_angular1.pages.TodosPage;
import com.github.ystromm.learn_selenium.webapp_angular1.webdriver.Firefox;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Optional;

import static com.github.ystromm.learn_selenium.webapp_angular1.spring.Todos.checkedTodo;
import static com.github.ystromm.learn_selenium.webapp_angular1.spring.Todos.todo;
import static com.github.ystromm.learn_selenium.webapp_angular1.webdriver.Screenshot.screenshot;
import static com.github.ystromm.learn_selenium.webapp_angular1.webdriver.WebElementMatchers.isDisplayed;
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
        await().atMost(ONE_SECOND).until(() ->
                verify(todoRepository).getAll()
        );
    }

    @Test
    public void open_should_not_show_error() {
        when(todoRepository.getAll()).thenReturn(emptyList());
        todosPage.open();
        assertThat(todosPage.getError(), not(isDisplayed()));
    }

    @Test
    public void open_should_show_no_todos() {
        when(todoRepository.getAll()).thenReturn(emptyList());
        todosPage.open();
        await().atMost(ONE_SECOND).until(() ->
                assertThat(todosPage.getTodoItems(), empty())
        );
    }

    @Test
    public void open_should_show_todos() {
        when(todoRepository.getAll()).thenReturn(singletonList(todo()));
        todosPage.open();
        await().atMost(ONE_SECOND).until(() ->
                assertThat(todosPage.getTodoItems(), hasSize(1))
        );
    }

    @Test
    public void open_should_show_error() {
        when(todoRepository.getAll()).thenThrow(new IllegalStateException("Testing internal error!"));
        todosPage.open();
        assertThat(todosPage.getError(), isDisplayed());
    }

    @Test
    public void add_should_create() {
        final Todo todo = Todo.builder().text("To do!").build();
        when(todoRepository.getAll()).thenReturn(emptyList());
        when(todoRepository.create(Mockito.any(Todo.class))).thenReturn(todo());
        todosPage.open();
        todosPage.addTodoItem(todo.getText());
        await().atMost(ONE_SECOND).until(() ->
                verify(todoRepository).create(todo)
        );
    }

    @Test
    public void add_should_getAll_twice() {
        final Todo todo = Todo.builder().text("To do!").build();
        when(todoRepository.getAll()).thenReturn(emptyList());
        when(todoRepository.create(Mockito.any(Todo.class))).thenReturn(todo());
        todosPage.open();
        todosPage.addTodoItem(todo.getText());
        await().atMost(ONE_SECOND).until(() ->
                verify(todoRepository, times(2)).getAll()
        );
    }

    @Ignore("Fails on purpose")
    @Test
    public void click_checkbox_should_update() {
        when(todoRepository.getAll()).thenReturn(singletonList(todo()));
        when(todoRepository.update(checkedTodo())).thenReturn(Optional.of(checkedTodo()));
        todosPage.open();
        await().atMost(ONE_SECOND).until(() ->
                assertThat(todosPage.getTodoItems(), hasSize(1)));
        todosPage.clickFirstCheckbox();
        verify(todoRepository).update(checkedTodo());
    }

    @Test
    public void click_checkbox_should_getAll_twice() {
        when(todoRepository.getAll()).thenReturn(singletonList(todo()));
        when(todoRepository.update(checkedTodo())).thenReturn(Optional.of(checkedTodo()));
        todosPage.open();
        await().atMost(ONE_SECOND).until(() ->
                assertThat(todosPage.getTodoItems(), hasSize(1)));
        todosPage.clickFirstCheckbox();
        verify(todoRepository, times(2)).getAll();
    }


    @Test
    public void click_delete_should_remove() {
        final Todo todo = todo();
        when(todoRepository.getAll()).thenReturn(singletonList(todo));
        when(todoRepository.remove(todo.getId())).thenReturn(Optional.of(todo));
        todosPage.open();
        await().atMost(ONE_SECOND).until(() ->
                assertThat(todosPage.getTodoItems(), hasSize(1)));
        todosPage.clickFirstDelete();
        verify(todoRepository).remove(todo.getId());
    }

    @Ignore("Fails on purpose")
    @Test
    public void click_delete_should_getAll_twice() {
        final Todo todo = todo();
        when(todoRepository.getAll()).thenReturn(singletonList(todo));
        when(todoRepository.remove(todo.getId())).thenReturn(Optional.of(todo));
        todosPage.open();
        await().atMost(ONE_SECOND).until(() ->
                assertThat(todosPage.getTodoItems(), hasSize(1)));
        todosPage.clickFirstDelete();
        verify(todoRepository, times(2)).getAll();
    }
}
