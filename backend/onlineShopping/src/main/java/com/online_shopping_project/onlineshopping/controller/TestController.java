package com.online_shopping_project.onlineshopping.controller;

import com.online_shopping_project.onlineshopping.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
public class TestController {
    @GetMapping("/api/test")
    public Result<String> test() {
        log.info("后端运行成功！");
        return Result.success("后端运行成功！");
    }
}
