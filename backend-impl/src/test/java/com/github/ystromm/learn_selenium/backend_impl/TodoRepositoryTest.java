package com.github.ystromm.learn_selenium.backend_impl;

import com.github.ystromm.learn_selenium.backend_api.Todo;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

public class TodoRepositoryTest {

    public static final String TO_DO = "To do!";
    public static final String UPDATED_TEXT = "Updated text!";
    private TodoRepository todoRepository;

    @Before
    public void setUpTodoRepository() {
        todoRepository = new TodoRepository();
        todoRepository.getAll().forEach(todo -> todoRepository.remove(todo.getId()));
    }

    @Test
    public void update_should_return_empty() {
        final int nextId = nextId();
        assertThat(todoRepository.update(Todo.builder().id(nextId).text("").done(false).build()), equalTo(Optional.empty()));
    }

    @Test
    public void update_should_return_todo() {
        final int nextId = nextId();
        final Todo createdTodo = todoRepository.create(Todo.builder().id(nextId).text("").done(false).build());
        assertThat(todoRepository.update(createdTodo.copy().text(UPDATED_TEXT).build()),
                equalTo(Optional.of(Todo.builder().id(nextId).text(UPDATED_TEXT).done(false).build())));
    }

    @Test
    public void create_should_return_todo_with_id_not_zero() {
        final Todo createdTodo = todoRepository.create(Todo.builder().text("").done(false).build());
        assertThat(createdTodo.getId(), not(equalTo(0)));
    }

    @Test
    public void getAll_should_return_todo() {
        final int nextId = nextId();
        todoRepository.create(Todo.builder().text(TO_DO).done(false).build());
        assertThat(todoRepository.getAll(), hasItem(Todo.builder().id(nextId).text(TO_DO).done(false).build()));
    }

    @Test
    public void delete_should_return_todo() {
        final int nextId = nextId();
        final Todo createdTodo = todoRepository.create(Todo.builder().text(TO_DO).done(false).build());
        todoRepository.remove(createdTodo.getId());
        assertThat(todoRepository.get(nextId), equalTo(Optional.empty()));
    }

    private int nextId() {
        return todoRepository.nextId.get() + 1;
    }
}