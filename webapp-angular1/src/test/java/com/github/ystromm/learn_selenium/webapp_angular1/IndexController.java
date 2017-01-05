package com.github.ystromm.learn_selenium.webapp_angular1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

import static com.github.ystromm.learn_selenium.webapp_angular1.PropertyNames.BACKEND_SERVER_PORT;
import static com.github.ystromm.learn_selenium.webapp_angular1.PropertyNames.TODOS_URL;

@Controller
public class IndexController {
    @GetMapping("/")
    String index(Map<String, Object> model) {
        model.put(BACKEND_SERVER_PORT, System.getProperty(BACKEND_SERVER_PORT));
        model.put(TODOS_URL, System.getProperty(TODOS_URL));
        return "index";
    }
}
