package com.github.ystromm.learn_selenium.webapp_elm.fluentlenium;

import com.github.ystromm.learn_selenium.backend_api.Todo;
import com.github.ystromm.learn_selenium.backend_impl.BackendMain;
import com.github.ystromm.learn_selenium.backend_impl.TodoRepository;
import com.github.ystromm.learn_selenium.webapp_elm.PropertyNames;
import com.github.ystromm.learn_selenium.webapp_elm.WebappMain;
import org.fluentlenium.adapter.junit.FluentTest;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.fluentlenium.core.hook.wait.Wait;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.ystromm.learn_selenium.backend_api.Todo.todo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Wait
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
        System.setProperty(PropertyNames.TODOS_URL, baseUrl());
    }

    @Test
    public void title_should_be_todos() {
        goTo(baseUrl());
        assertThat(window().title(), equalTo("Todos"));
    }

    @Test
    public void should_have_items_list() {
        goTo(baseUrl());
        assertThat($(data_test_id("todos_items_ul")), hasSize(1));
    }

    @Test
    public void should_have_items_list_with_some_item() {
        todoRepository.create(todo("Don't forget"));
        goTo(baseUrl());
        assertThat($(data_test_id("todos_items_li")), hasSize(1));
    }

    private static String data_test_id(String value) {
        return "*[data-test-id='" + value + "']";
    }

    private String baseUrl() {
        return "http://localhost:" + localServerPort;
    }

}
