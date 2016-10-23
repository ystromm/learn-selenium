package com.github.ystromm.learn_selenium.backend_impl;

import com.github.ystromm.learn_selenium.backend_api.Todo;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TodoRepository {
    private final Map<Integer, Todo> todos = Maps.newHashMap();
    private final AtomicInteger nextId = new AtomicInteger();

    public Todo create(Todo todo) {
        final Todo newTodo = todo.copy().id(nextId.incrementAndGet()).build();
        todos.put(newTodo.getId(), newTodo);
        return newTodo;
    }

    public Optional<Todo> update(Todo todo) {
        return get(todo.getId()).map(existingTodo -> todos.put(todo.getId(), existingTodo.copy().text(todo.getText()).build()));
    }

    public Optional<Todo> get(int id) {
        return Optional.ofNullable(todos.get(id));
    }

    public Collection<Todo> getAll() {
        return ImmutableList.copyOf(todos.values());
    }

    public void remove(int id) {
        todos.remove(id);
    }
}