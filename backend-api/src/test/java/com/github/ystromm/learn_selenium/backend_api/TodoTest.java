package com.github.ystromm.learn_selenium.backend_api;


import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TodoTest {

    public static final String TEXT_VALUE = "textValue";

    @Test
    public void copy_should_copy_id() {
        assertThat(todo().copy().build().getId(), equalTo(1));
    }

    @Test
    public void copy_should_copy_text() {
        assertThat(todo().copy().build().getText(), equalTo(TEXT_VALUE));
    }

    @Test
    public void copy_should_copy_done() {
        assertThat(todo().copy().build().isDone(), equalTo(true));
    }

    private static Todo todo() {
        return Todo.builder().id(1).text(TEXT_VALUE).done(true).build();
    }
}