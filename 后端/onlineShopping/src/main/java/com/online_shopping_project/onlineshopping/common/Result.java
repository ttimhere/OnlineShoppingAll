package com.online_shopping_project.onlineshopping.common;
import lombok.Data;
@Data
public class Result<T> {
    private Integer code; // 状态码（200：成功；500：失败）
    private String message; // 提示信息
    private T data; // 返回数据内容（泛型）
    //有参构造和无参构造
    public Result() {}
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    //成功时调用 返回业务数据
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }
    //成功时调用 但是没有返回业务数据
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }
    //失败时调用：返回错误提示
    public static <T> Result<T> error(String message) {//只带消息
        return new Result<>(500, message, null);
    }
    public static <T> Result<T> error(Integer code, String message) {//自定义状态码 + 消息
        return new Result<>(code, message, null);
    }
}
