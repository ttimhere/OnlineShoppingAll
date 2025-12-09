package com.online_shopping_project.onlineshopping.common;

import javax.servlet.http.HttpServletRequest;

/**
 * AuthContext 工具类
 * ---------------------
 * 统一封装从请求中获取当前登录用户信息的逻辑。
 * JwtAuthFilter 在解析 Token 后会向 request 中注入：
 *   - userId
 *   - email
 * 因此，这里直接从 request.getAttribute() 获取即可。
 */
public class AuthContext {

    /**
     * 获取当前登录用户ID
     */
    public static Long getUserId(HttpServletRequest request) {
        Object idObj = request.getAttribute("userId");
        if (idObj == null) {
            throw new RuntimeException("用户未登录或 Token 无效");
        }
        if (idObj instanceof Integer) {
            return ((Integer) idObj).longValue();
        } else if (idObj instanceof Long) {
            return (Long) idObj;
        } else {
            return Long.parseLong(idObj.toString());
        }
    }

    /**
     * 获取当前登录用户邮箱
     */
    public static String getEmail(HttpServletRequest request) {
        Object emailObj = request.getAttribute("email");
        return emailObj != null ? emailObj.toString() : null;
    }

    /**
     * 获取当前后台管理员ID
     */
    public static Long getAdminId(HttpServletRequest request) {
        Object idObj = request.getAttribute("adminId");
        if (idObj == null) {
            throw new RuntimeException("管理员未登录或 Token 无效");
        }
        return Long.parseLong(idObj.toString());
    }

    /**
     * 获取当前管理员角色列表
     */
    @SuppressWarnings("unchecked")
    public static java.util.List<String> getAdminRoles(HttpServletRequest request) {
        Object roles = request.getAttribute("roles");
        if (roles instanceof java.util.List<?>) {
            return (java.util.List<String>) roles;
        }
        return java.util.Collections.emptyList();
    }

}
