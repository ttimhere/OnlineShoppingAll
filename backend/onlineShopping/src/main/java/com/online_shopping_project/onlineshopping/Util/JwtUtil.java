package com.online_shopping_project.onlineshopping.Util;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
/**
 * JWT 工具类
 * 负责生成和解析 Token，实现无状态登录。
 */
public class JwtUtil {
    private static String runtimeSecret;

    // 启动时由 SecurityConfig 调用，设置本次运行的 secret
    public static void setRuntimeSecret(String secret) {
        runtimeSecret = secret;
    }
    // 给 AdminController / JwtAuthFilter 使用
    public static String getRuntimeSecret() {
        return runtimeSecret;
    }
    //生成 JWT Token
    public static String generateToken(String secret, long expireMinutes,
                                       String subject, Map<String, Object> claims) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        long now = System.currentTimeMillis();
        Date exp = new Date(now + expireMinutes * 60_000);

        JwtBuilder builder = Jwts.builder()
                .setSubject(subject) // 设置主题（用户邮箱）
                .addClaims(claims) // 添加自定义载荷
                .setIssuedAt(new Date(now)) // 签发时间
                .setExpiration(exp) // 过期时间
                .signWith(key, SignatureAlgorithm.HS256); // 签名算法
        return builder.compact();
    }
    //解析 JWT Token
    public static Jws<Claims> parse(String secret, String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}