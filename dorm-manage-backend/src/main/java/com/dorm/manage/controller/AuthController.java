package com.dorm.manage.controller;

import com.dorm.manage.common.Result;
import com.dorm.manage.dto.LoginRequest;
import com.dorm.manage.dto.LoginResponse;
import com.dorm.manage.dto.RegisterRequest;
import com.dorm.manage.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证接口 - 登录、验证码、登出
 *
 * @author dorm-manage
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 获取验证码
     */
    @GetMapping("/captcha")
    public Result<Map<String, String>> captcha() {
        return Result.ok(authService.getCaptcha());
    }

    /**
     * 学生/管理员登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String ip = getClientIp(httpRequest);
        LoginResponse resp = authService.login(request, ip);
        return Result.ok(resp);
    }

    /**
     * 学生注册（仅注册学生账号）
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request, HttpServletRequest httpRequest) {
        String ip = getClientIp(httpRequest);
        authService.register(request, ip);
        return Result.ok();
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public Result<Void> logout(@RequestAttribute String username) {
        authService.logout(username);
        return Result.ok();
    }

    private String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isEmpty()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
