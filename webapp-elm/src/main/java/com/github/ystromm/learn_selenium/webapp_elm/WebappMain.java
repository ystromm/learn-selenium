package com.github.ystromm.learn_selenium.webapp_elm;

import com.github.ystromm.learn_selenium.backend_impl.BackendMain;
import com.github.ystromm.learn_selenium.backend_impl.TodoApiConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(TodoApiConfiguration.class)
public class WebappMain {

    public static void main(String[] args) {
        System.setProperty(PropertyNames.TODOS_URL, baseUrl());
        SpringApplication.run(WebappMain.class,  args);
    }

    private static String baseUrl() {
        return "http://localhost:8081/api/todo";
    }
}
