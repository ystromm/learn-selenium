package com.github.ystromm.learn_selenium.backend_impl;

import com.github.ystromm.learn_selenium.backend_api.Todo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;

import java.util.concurrent.ExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoControllerTest {

    public static final int ID = 1;
    public static final String FUHGETTABOUTIT = "Fuhgettaboutit.";
    public static final String REMEMBER_THIS = "Remember this!";
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TodoRepository todoRepository;


    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUpTodoRepository() {
        todoRepository.getAll().forEach(todo -> todoRepository.remove(todo.getId()));
    }

    @Test
    public void getAll_should_return_empty() {
        final ResponseEntity<Todo[]> responseEntity = testRestTemplate.getForEntity("/api/todo", Todo[].class);
        assertThat(responseEntity.getBody(), emptyArray());
    }

    @Test
    public void get_should_return_not_found() {
        final ResponseEntity<Todo> responseEntity = testRestTemplate.getForEntity("/api/todo/1", Todo.class);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void post_should_return_a_todo_with_id_not_0() {
        final ResponseEntity<Todo> responseEntity = testRestTemplate.postForEntity("/api/todo", todo(), Todo.class);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseEntity.getBody().copy(), not(equalTo(0)));
    }

    @Test
    public void post_should_return_a_todo() {
        final ResponseEntity<Todo> responseEntity = testRestTemplate.postForEntity("/api/todo", todo(), Todo.class);
        testRestTemplate.getForEntity("/api/todo/{id}", Todo.class, responseEntity.getBody().getId());
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseEntity.getBody().getText(), equalTo(REMEMBER_THIS));
    }


    @Test
    public void delete_should_return_not_found() {
        testRestTemplate.delete("/api/todo/{id}", ID);
    }

    @Test
    public void put_should_return_not_found() {
        testRestTemplate.put("/api/todo/{id}", Todo.builder().id(ID).text(FUHGETTABOUTIT).build(), ID);
    }

    @Test
    public void put_should_return_updated() {
        final ResponseEntity<Todo> postResponseEntity = testRestTemplate.postForEntity("/api/todo", todo(), Todo.class);
        final Todo todo = postResponseEntity.getBody();
        testRestTemplate.put("/api/todo/{id}", todo.copy().text(FUHGETTABOUTIT).build(), todo.getId());
        final ResponseEntity<Todo> getResponseEntity = testRestTemplate.getForEntity("/api/todo/{id}", Todo.class, todo.getId());
        assertThat(getResponseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(getResponseEntity.getBody().getText(), equalTo(FUHGETTABOUTIT));
    }

    private static Todo todo() {
        return Todo.builder().text(REMEMBER_THIS).build();
    }
}