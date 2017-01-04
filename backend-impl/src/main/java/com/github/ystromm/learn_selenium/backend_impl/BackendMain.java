package com.github.ystromm.learn_selenium.backend_impl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class BackendMain {
    public static void main(String[] args) {
        SpringApplication.run(TodoApiConfiguration.class, args);
    }
}
