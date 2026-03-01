package com.dorm.manage.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类 - 生成与解析 token
 *
 * @author dorm-manage
 */
@Component
public class JwtUtil {

    @Value("${dorm.jwt.secret}")
    private String secret;

    @Value("${dorm.jwt.expire-hours:2}")
    private int expireHours;

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_USERNAME = "username";
    private static final String CLAIM_USER_TYPE = "userType";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 token，包含 userId、username、userType
     */
    public String generateToken(Long userId, String username, Integer userType) {
        Date now = new Date();
        Date expire = new Date(now.getTime() + (long) expireHours * 60 * 60 * 1000);
        return Jwts.builder()
                .claim(CLAIM_USER_ID, userId)
                .claim(CLAIM_USERNAME, username)
                .claim(CLAIM_USER_TYPE, userType)
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 解析 token 获取 Claims
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserId(Claims claims) {
        return claims.get(CLAIM_USER_ID, Long.class);
    }

    public String getUsername(Claims claims) {
        return claims.get(CLAIM_USERNAME, String.class);
    }

    public Integer getUserType(Claims claims) {
        return claims.get(CLAIM_USER_TYPE, Integer.class);
    }

    public boolean isExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
