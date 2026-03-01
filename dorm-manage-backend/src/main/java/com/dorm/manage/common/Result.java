package com.dorm.manage.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应格式
 *
 * @author dorm-manage
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    /**
     * 状态码：200成功 400参数错误 401未授权 403权限不足 500服务器错误
     */
    private int code;
    private String msg;
    private T data;
    private long timestamp;

    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "操作成功", data, System.currentTimeMillis());
    }

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> fail(int code, String msg) {
        return new Result<>(code, msg, null, System.currentTimeMillis());
    }
}
