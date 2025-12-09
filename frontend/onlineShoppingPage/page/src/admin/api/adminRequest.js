import axios from 'axios'
import { useAdminStore } from '../store/adminStore'
const BASE = window.location.origin
const adminRequest = axios.create({
    baseURL: `${BASE}`,
    timeout: 5000
})
// 请求拦截器：自动加 Token
adminRequest.interceptors.request.use((config) => {
    const store = useAdminStore()
    if (store.token) {
        config.headers.Authorization = 'Bearer ' + store.token
    }
    return config
})
// 响应拦截器：遇到 401 自动退出并跳回登录
adminRequest.interceptors.response.use(
    (res) => res.data,
    (err) => {
        const resp = err.response
        // 后端返回 401 时，清除管理员登录状态
        if (resp && resp.status === 401) {
            const store = useAdminStore()
            store.logout()
            // 强制跳转到后台登录页
            window.location.href = '/admin/login'
            return
        }
        console.error('Admin API Error:', err)
        return Promise.reject(err)
    }
)
export default adminRequest
