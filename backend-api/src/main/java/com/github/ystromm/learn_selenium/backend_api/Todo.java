package com.github.ystromm.learn_selenium.backend_api;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.NONE)
public class Todo {
    private int id;
    private String text = "";
    private boolean done = false;

    public TodoBuilder copy() {
        return Todo.builder().id(id).text(text).done(done);
    }
}
