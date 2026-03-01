package com.dorm.manage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 学生注册请求
 *
 * @author dorm-manage
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "学号不能为空")
    @Size(min = 4, max = 32, message = "学号长度4-32位")
    private String studentId;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度6-20位")
    private String password;

    @NotBlank(message = "请再次输入密码")
    private String confirmPassword;

    @NotBlank(message = "验证码不能为空")
    private String captcha;

    private String captchaKey;

    /** 姓名（选填） */
    @Size(max = 32)
    private String realName;

    /** 联系电话（选填） */
    @Size(max = 20)
    private String phone;
}
