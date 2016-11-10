package com.github.ystromm.learn_selenium.webapp.spring;

import com.github.ystromm.learn_selenium.backend_api.Todo;

public final class Todos {

    static Todo todo() {
        return todo(false);
    }

    static Todo checkedTodo() {
        return todo(true);
    }

    private static Todo todo(boolean done) {
        return Todo.builder().id(1).text("To do!").done(done).build();
    }

    // hidden
    private Todos() {
        // empty
    }
}
