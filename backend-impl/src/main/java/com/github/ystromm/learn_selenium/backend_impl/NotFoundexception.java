package com.github.ystromm.learn_selenium.backend_impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundexception extends RuntimeException {
}
