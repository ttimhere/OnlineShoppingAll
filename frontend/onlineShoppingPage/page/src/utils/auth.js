//Token 处理工具
const TOKEN_KEY = 'os_token';

export function getToken() { //登录成功保存token
    return localStorage.getItem(TOKEN_KEY) || '';
}
export function setToken(token) { //页面刷新恢复登录状态
    localStorage.setItem(TOKEN_KEY, token);
}
export function removeToken() { //注销清除 token
    localStorage.removeItem(TOKEN_KEY);
}
