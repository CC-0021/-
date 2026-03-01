package com.dorm.manage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求（学生端：学号+密码+验证码）
 *
 * @author dorm-manage
 */
@Data
public class LoginRequest {

    @NotBlank(message = "学号不能为空")
    private String studentId;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String captcha;

    /** 验证码key（前端从获取验证码接口拿到） */
    private String captchaKey;
}
