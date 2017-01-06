package com.github.ystromm.learn_selenium.webapp_elm.fluentlenium;

import com.github.ystromm.learn_selenium.backend_api.Todo;
import com.github.ystromm.learn_selenium.backend_impl.BackendMain;
import com.github.ystromm.learn_selenium.backend_impl.TodoRepository;
import com.github.ystromm.learn_selenium.webapp_elm.PropertyNames;
import com.github.ystromm.learn_selenium.webapp_elm.WebappMain;
import org.awaitility.Awaitility;
import org.awaitility.Duration;
import org.awaitility.core.ConditionFactory;
import org.fluentlenium.adapter.junit.FluentTest;
import org.fluentlenium.core.domain.FluentWebElement;
import org.fluentlenium.core.hook.wait.Wait;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Wait
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebappMain.class, BackendMain.class}, webEnvironment = RANDOM_PORT)
public class TodosTest extends FluentTest {
    @Rule
    public TestName testName = new TestName();
    @LocalServerPort
    private int localServerPort;
    @Autowired
    private TodoRepository todoRepository;

    @Before
    public void setUpProperties() {
        System.setProperty(PropertyNames.TODOS_URL, apiUrl());
    }

    @Before
    public void setUpTodoRepository() {
        // the mock gets re-used between test cases, kind of nasty
        reset(todoRepository);
    }

    @Test
    public void title_should_be_todos() {
        goTo(baseUrl());
        assertThat(window().title(), equalTo("Todos"));
    }

    @Test
    public void open_should_getAll() {
        goTo(baseUrl());
        $(with_data_test_id("todos_items_li"));
        verify(todoRepository, timeout(1000)).getAll();
    }

    @Test
    public void should_have_items_list() {
        goTo(baseUrl());
        assertThat($(with_data_test_id("todos_items_ul")), hasSize(1));
    }

    @Test
    public void should_have_item() {
        when(todoRepository.getAll()).thenReturn(singletonList(todo(1)));
        goTo(baseUrl());
        awaitAtMostOneSecond().until(() -> assertThat($(with_data_test_id("todos_items_li")), hasSize(1)));
    }

    @Test
    public void should_have_item_with_text() {
        final Todo todo = todo(1);
        when(todoRepository.getAll()).thenReturn(singletonList(todo));
        goTo(baseUrl());
        awaitAtMostOneSecond().until(() -> assertThat(el(with_data_test_id("todo_text_text_1")).value(), equalTo(todo.getText())));
    }

    @Ignore("Fails with reflection exceptin on write")
    @Test
    public void add_should_create() {
        final String text = "Todo " + System.currentTimeMillis();
        goTo(baseUrl());
        el(with_data_test_id("todo_add_text")).write(text);
        el(with_data_test_id("todo_add_button")).click();
        verify(todoRepository, timeout(1000)).create(Todo.builder().text(text).done(false).build());
    }

    @Test
    public void done_should_update() {
        final Todo todo = todo(1);
        when(todoRepository.getAll()).thenReturn(singletonList(todo));
        goTo(baseUrl());
        el(with_data_test_id("todo_done_checkbox_1")).click();
        verify(todoRepository, timeout(1000)).update(todo.copy().done(true).build());
    }

    @Test
    public void delete_should_remove() {
        when(todoRepository.getAll()).thenReturn(singletonList(todo(1)));
        goTo(baseUrl());
        el(with_data_test_id("todo_delete_button_1")).click();
        verify(todoRepository, timeout(1000)).remove(1);
    }

    private static ConditionFactory awaitAtMostOneSecond() {
        return Awaitility.await().atMost(Duration.ONE_SECOND);
    }

    private static Todo todo(int id) {
        return Todo.builder().text("Todo " + id).id(id).done(false).build();
    }


    private static String with_data_test_id(String value) {
        return "*[data-test-id='" + value + "']";
    }

    private String baseUrl() {
        return "http://localhost:" + localServerPort;
    }

    private String apiUrl() {
        return baseUrl() + "/api/todo";
    }

}
