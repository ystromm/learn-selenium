package com.github.ystromm.learn_selenium.backend_impl;

import com.github.ystromm.learn_selenium.backend_api.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = TodoController.API_TODO)
public class TodoController {
    public static final String API_TODO = "/api/todo";
    public static final String ID = "/{id}";
    private final TodoRepository todoRepository;

    @Autowired
    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Todo post(@RequestBody Todo todo) {
        return todoRepository.create(todo);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Todo> getAll() {
        return todoRepository.getAll();
    }

    @RequestMapping(value = ID, method = RequestMethod.GET)
    public Todo get(@PathVariable int id) {
        return todoRepository.get(id).orElseThrow(() -> new NotFoundexception());
    }

    @RequestMapping(value = ID, method = RequestMethod.PUT)
    public Todo put(@PathVariable int id, @RequestBody Todo todo) {
        return todoRepository.update(todo).orElseThrow(() -> new NotFoundexception());
    }

    @RequestMapping(value = ID, method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) {
        todoRepository.remove(id).orElseThrow(() -> new NotFoundexception());
    }
}
