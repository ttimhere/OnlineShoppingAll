package com.online_shopping_project.onlineshopping.config;

import com.online_shopping_project.onlineshopping.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * 全局异常处理类
 * 统一捕获 Controller 层抛出的异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //捕获所有异常类型
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        e.printStackTrace();
        return Result.error("服务器异常: " + e.getMessage());
    }
}

