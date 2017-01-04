package com.github.ystromm.learn_selenium.webapp_elm;

import com.github.ystromm.learn_selenium.backend_impl.BackendMain;
import com.github.ystromm.learn_selenium.backend_impl.TodoApiConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
public class WebappMain {

    public static void main(String[] args) {
        SpringApplication.run(WebappMain.class,  args);
    }
}
