package com.dorm.manage.service;

import com.dorm.manage.common.ResultCode;
import com.dorm.manage.dto.LoginRequest;
import com.dorm.manage.dto.LoginResponse;
import com.dorm.manage.dto.RegisterRequest;
import com.dorm.manage.entity.User;
import com.dorm.manage.exception.BusinessException;
import com.dorm.manage.mapper.UserMapper;
import com.dorm.manage.util.JwtUtil;
import com.dorm.manage.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 认证服务 - 登录、验证码、限流
 *
 * @author dorm-manage
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${dorm.rate-limit.login-max-per-minute:20}")
    private int loginMaxPerMinute;

    /**
     * 学生登录：验证码 → 账号密码 → 生成JWT并存入Redis
     */
    public LoginResponse login(LoginRequest request, String clientIp) {
        if (!redisUtil.tryIncrementRateLimit("login:" + clientIp, loginMaxPerMinute)) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "请求过于频繁，请稍后再试");
        }
        String captcha = redisUtil.getCaptcha(request.getCaptchaKey());
        if (captcha == null || !captcha.equalsIgnoreCase(request.getCaptcha())) {
            throw new BusinessException(ResultCode.CAPTCHA_ERROR, "验证码错误");
        }
        User user = userMapper.selectByUsername(request.getStudentId());
        if (user == null) {
            throw new BusinessException(ResultCode.ACCOUNT_NOT_FOUND, "账号不存在");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException(ResultCode.ACCOUNT_DISABLED, "账号已被禁用");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR, "密码错误");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getUserType());
        redisUtil.setToken(user.getUsername(), token);
        java.util.List<String> permissions = user.getUserType() == 2
                ? userMapper.selectPermissionCodesByUserId(user.getId())
                : java.util.Collections.emptyList();
        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .userType(user.getUserType())
                .permissions(permissions != null ? permissions : java.util.Collections.emptyList())
                .build();
    }

    /**
     * 获取验证码，返回 key 与 base64 图片（此处简化为数字验证码，前端可自行绘制）
     */
    public Map<String, String> getCaptcha() {
        String key = UUID.randomUUID().toString().replace("-", "");
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
        redisUtil.setCaptcha(key, code);
        Map<String, String> map = new HashMap<>();
        map.put("captchaKey", key);
        map.put("captchaCode", code);
        return map;
    }

    public void logout(String username) {
        redisUtil.deleteToken(username);
    }

    /**
     * 学生注册：仅注册学生用户（user_type=1），密码 BCrypt 加密
     */
    public void register(RegisterRequest request, String clientIp) {
        if (!redisUtil.tryIncrementRateLimit("register:" + clientIp, loginMaxPerMinute)) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "请求过于频繁，请稍后再试");
        }
        String captcha = redisUtil.getCaptcha(request.getCaptchaKey());
        if (captcha == null || !captcha.equalsIgnoreCase(request.getCaptcha())) {
            throw new BusinessException(ResultCode.CAPTCHA_ERROR, "验证码错误");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "两次密码输入不一致");
        }
        User exist = userMapper.selectByUsername(request.getStudentId());
        if (exist != null) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "该学号已被注册");
        }
        User user = new User();
        user.setUsername(request.getStudentId());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setPhone(request.getPhone());
        user.setUserType(1);
        user.setStatus(1);
        userMapper.insert(user);
    }
}
