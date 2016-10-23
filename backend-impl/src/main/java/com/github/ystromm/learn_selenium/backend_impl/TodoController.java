package com.github.ystromm.learn_selenium.backend_impl;

import com.github.ystromm.learn_selenium.backend_api.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class TodoController {
    public static final String API_TODO = "/api/todo";
    public static final String ID = "/{id}";
    private final TodoRepository todoRepository;

    @Autowired
    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @RequestMapping(value = API_TODO, method = RequestMethod.POST)
    public Todo post(@RequestBody Todo todo) {
        return todoRepository.create(todo);
    }

    @RequestMapping(value = API_TODO, method = RequestMethod.GET)
    public Collection<Todo> get() {
        return todoRepository.getAll();
    }

    @RequestMapping(value = API_TODO + ID, method = RequestMethod.GET)
    public Todo get(@PathVariable int id) {
        return todoRepository.get(id).orElseThrow(() -> new NotFoundexception());
    }

    @RequestMapping(value = API_TODO + ID, method = RequestMethod.PUT)
    public Todo put(@PathVariable int id, @RequestBody Todo todo) {
        return todoRepository.get(id).map(existingTodo -> {
            final Todo updatedTodo = existingTodo.copy().text(todo.getText()).build();
            todoRepository.update(updatedTodo);
            return updatedTodo;
        }).orElseThrow(() -> new NotFoundexception());
    }

    @RequestMapping(value = API_TODO + ID, method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) {
        todoRepository.get(id).map(todo -> {
            todoRepository.remove(id);
            return todo;
        }).orElseThrow(() -> new NotFoundexception());
    }
}
