package com.online_shopping_project.onlineshopping.Util;
import com.online_shopping_project.onlineshopping.config.SecurityConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//JWT 认证过滤器 在每次请求时自动验证 Token 是否有效。
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    //每次 HTTP 请求都会经过此方法
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        // 从 HTTP 请求头中获取 Authorization 字段
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {  //调用 JwtUtil 工具类，使用密钥 secret 解析并验证 Token
                // 如果签名正确且未过期，parse() 会返回解析后的 Jws<Claims> 对象
                String runtimeSecret = JwtUtil.getRuntimeSecret();
                Jws<Claims> jws = JwtUtil.parse(runtimeSecret, token);
                Claims claims = jws.getBody();
                //判断是否为后台管理token
                String type = claims.get("type", String.class);
                if ("admin".equals(type)) {
                    // 后台管理员 token
                    String adminId = claims.get("adminId").toString();
                    String username = claims.getSubject(); // 登录账号
                    String nickname = claims.get("nickname", String.class);
                    // 注入 request 用于业务层获取
                    request.setAttribute("adminId", adminId);
                    request.setAttribute("username", username);
                    request.setAttribute("nickname", nickname);
                    request.setAttribute("roles", claims.get("roles"));
                    // Spring Security 认证对象
                    AbstractAuthenticationToken authentication =
                            new AbstractAuthenticationToken(AuthorityUtils.NO_AUTHORITIES) {
                                @Override public Object getCredentials() { return token; }
                                @Override public Object getPrincipal() { return "ADMIN:" + nickname; }
                            };

                    authentication.setDetails(adminId);
                    authentication.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else{
                    // 提取用户信息
                    String userId = claims.get("uid", String.class);
                    String username = claims.get("uname", String.class);
                    // 将用户信息注入到 request，供 AuthContext 使用
                    request.setAttribute("userId", userId);
                    request.setAttribute("email", claims.getSubject()); // subject = 登录时传入的 email
                    // 将用户信息注入到 Spring Security 上下文 创建 Spring Security 的认证对象（Authentication）
                    AbstractAuthenticationToken authentication =
                            new AbstractAuthenticationToken(AuthorityUtils.NO_AUTHORITIES) {
                                @Override public Object getCredentials() { return token; } //凭证
                                @Override public Object getPrincipal() { return username; } //主体
                            };
                    // 将用户ID附加到认证对象的附加信息中
                    authentication.setDetails(userId);
                    // 设置认证状态为“已认证”
                    authentication.setAuthenticated(true);
                    // 将认证对象保存到 SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception ignore) {
                // Token 验证失败：返回 401，提示前端重新登录
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":401,\"message\":\"token invalid or expired\"}");
                return;
            }
        }
        //继续执行过滤器链，将请求传递给下一个过滤器或控制器。
        chain.doFilter(request, response);
    }
}


