package com.ginko.license.example.handler;

import com.ginko.license.checker.exception.CheckException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ginko
 * @date 1/9/20
 */
@RestControllerAdvice
public class CheckExceptionHandler {

    @ExceptionHandler(CheckException.class)
    @ResponseBody
    public Map<String, Object> validationErrorHandler(CheckException e) {
        Map<String, Object> res = new HashMap<>();
        res.put("result", "failed");
        res.put("reason", e.getMessage());
        return res;
    }
}
