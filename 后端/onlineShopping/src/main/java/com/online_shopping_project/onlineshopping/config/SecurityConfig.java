package com.online_shopping_project.onlineshopping.config;

import com.online_shopping_project.onlineshopping.Util.JwtAuthFilter;
import com.online_shopping_project.onlineshopping.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    @Value("${jwt.secret}")
    private String secret;

    //过滤器链配置
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                //h后台管理接口规则
                .antMatchers("/api/admin/login").permitAll()
                .antMatchers("/api/admin/**").authenticated()   // 后台接口必须是管理员 token
                //放行 Swagger 与静态资源
                .antMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/v3/api-docs.yaml",
                        "/uploads/**",
                        "/images/**",
                        "/product/**"
                ).permitAll()
                //放行商品展示、类目、上传接口
                .antMatchers(
                        "/api/category/**",
                        "/api/products/**",
                        "/api/upload/**"
                ).permitAll()
                //放行登录、注册接口
                .antMatchers(
                        "/api/user/login",
                        "/api/user/register",
                        "/api/test"
                ).permitAll()
                // 购物车接口必须登录后访问
                .antMatchers("/api/cart/**").authenticated()
                // 其他接口需要认证
                .anyRequest().authenticated();

        // 添加 JWT 过滤器
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //密码加密算法
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //AuthenticationManager 用于登录时认证
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @PostConstruct
    public void init() {
        String runtimeSecret = secret + ":" + System.currentTimeMillis();
        JwtUtil.setRuntimeSecret(runtimeSecret);
    }

}
