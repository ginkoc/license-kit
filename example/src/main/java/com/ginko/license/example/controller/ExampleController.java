package com.ginko.license.example.controller;

import com.ginko.license.checker.annotations.CheckPoint;
import com.ginko.license.checker.predicates.DatePredicate;
import com.ginko.license.example.customp.MyPredicate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ginko
 * @date 9/6/19
 */
@Controller
public class ExampleController {

    @GetMapping(value = "/test")
    @CheckPoint(predicates = {DatePredicate.class, MyPredicate.class})
    @ResponseBody
    public Map<String, String> testingChecker() {
        Map<String, String> map = new HashMap<>();
        map.put("result", "all predicates pass");
        return map;
    }
}
