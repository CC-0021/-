package com.dorm.manage.interceptor;

import com.dorm.manage.common.ResultCode;
import com.dorm.manage.util.JwtUtil;
import com.dorm.manage.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * Token 鉴权拦截器 - 验证 JWT 有效性及 Redis 中的 token
 *
 * @author dorm-manage
 */
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            writeError(response, ResultCode.UNAUTHORIZED, "未授权，请先登录");
            return false;
        }
        String token = auth.substring(7);
        try {
            Claims claims = jwtUtil.parseToken(token);
            String username = jwtUtil.getUsername(claims);
            String stored = redisUtil.getToken(username);
            if (stored == null || !stored.equals(token)) {
                writeError(response, ResultCode.UNAUTHORIZED, "登录已过期，请重新登录");
                return false;
            }
            Integer userType = jwtUtil.getUserType(claims);
            request.setAttribute("userId", jwtUtil.getUserId(claims));
            request.setAttribute("username", username);
            request.setAttribute("userType", userType);
            // 管理端接口仅允许管理员访问
            if (request.getRequestURI().contains("/admin/") && (userType == null || userType != 2)) {
                writeError(response, ResultCode.FORBIDDEN, "权限不足，仅管理员可操作");
                return false;
            }
            return true;
        } catch (ExpiredJwtException e) {
            writeError(response, ResultCode.UNAUTHORIZED, "登录已过期，请重新登录");
            return false;
        } catch (JwtException e) {
            writeError(response, ResultCode.UNAUTHORIZED, "无效的token");
            return false;
        }
    }

    private void writeError(HttpServletResponse response, int code, String msg) throws Exception {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> body = new HashMap<>();
        body.put("code", code);
        body.put("msg", msg);
        body.put("data", null);
        body.put("timestamp", System.currentTimeMillis());
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
