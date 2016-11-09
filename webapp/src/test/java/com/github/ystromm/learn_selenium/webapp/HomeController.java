package com.github.ystromm.learn_selenium.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private Environment environment;

    @GetMapping("/")
    String index(Map<String, Object> model) {
        model.put("backend.server.port", System.getProperty("backend.server.port"));
        return "index";
    }
}
