package com.github.ystromm.learn_selenium.webapp_angular1.spring;

import com.github.ystromm.learn_selenium.backend_impl.TodoRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class TodosTestConfiguration {

    @Primary
    @Bean
    public TodoRepository getTodoRepository() {
        return Mockito.mock(TodoRepository.class);
    }
}
