package com.github.ystromm.learn_selenium.webapp_elm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

import static com.github.ystromm.learn_selenium.webapp_elm.PropertyNames.TODOS_URL;

@Controller
public class IndexController {
    @GetMapping("/")
    String index(Map<String, Object> model) {
        model.put(TODOS_URL, System.getProperty(TODOS_URL));
        return "index";
    }
}
