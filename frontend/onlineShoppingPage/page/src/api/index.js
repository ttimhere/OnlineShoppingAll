import axios from 'axios'
import { useUserStore } from '@/store/userStore'
import { ElMessage } from 'element-plus'
// 创建 axios 实例
// baseURL 设置为 '/api'，实际由 Vite 代理转发到后端（避免跨域）
const service = axios.create({
    baseURL: '/api',
    timeout: 15000
})
// 请求拦截器 统一添加 Token、通用参数等
service.interceptors.request.use((config) => {
    const store = useUserStore()
    // 如果用户登录了，自动在请求头添加 Authorization
    if (store?.token) {
        config.headers.Authorization = `Bearer ${store.token}`
    }
    return config
})
// 响应拦截器
service.interceptors.response.use(
    (resp) => { //成功响应
        const r = resp.data
        if (r && r.code === 200) {
            return r.data !== undefined ? r.data : null
        }
        console.error('[API Error]', r)
        ElMessage.error(r?.message || '请求失败')
        return Promise.reject(r?.message || '请求失败')
    },
    (error) => {
        console.error('[Network Error]', error)
        ElMessage.error(error?.response?.data?.message || error.message)
        return Promise.reject(error?.response?.data?.message || error.message)
    }
)
export default service
