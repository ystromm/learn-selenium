package com.github.ystromm.learn_selenium.webapp_angular1;

import com.github.ystromm.learn_selenium.backend_impl.TodoApiConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(TodoApiConfiguration.class)
public class WebappMain {
    public static void main(String[] args) {
        SpringApplication.run(WebappMain.class, args);
    }
}
