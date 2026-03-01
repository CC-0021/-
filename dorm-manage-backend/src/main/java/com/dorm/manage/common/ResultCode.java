package com.dorm.manage.common;

/**
 * 响应状态码常量
 *
 * @author dorm-manage
 */
public final class ResultCode {

    public static final int SUCCESS = 200;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int SERVER_ERROR = 500;

    /** 验证码错误 */
    public static final int CAPTCHA_ERROR = 4001;
    /** 账号不存在 */
    public static final int ACCOUNT_NOT_FOUND = 4002;
    /** 密码错误 */
    public static final int PASSWORD_ERROR = 4003;
    /** 账号被禁用 */
    public static final int ACCOUNT_DISABLED = 4004;

    private ResultCode() {
    }
}
