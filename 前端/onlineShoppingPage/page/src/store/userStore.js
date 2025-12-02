import { defineStore } from 'pinia'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { getProfile } from '@/api/user'
import { ElMessage } from 'element-plus'
import { useCartStore } from '@/store/cartStore'

export const useUserStore = defineStore('user', {
    state: () => ({
        token: getToken(),
        profile: null,
    }),
    getters: {
        isAuthed: (s) => !!s.token,
    },
    actions: {
        async setLogin(data) {
            this.token = data.token
            this.profile = data.user
            setToken(data.token)  // 存 localStorage

            const cartStore = useCartStore()
            await cartStore.loadCount()   // 登录后同步加载购物车数量
        },
        setToken(token) {
            this.token = token
            setToken(token)
        },
        clear() { //清空登录信息与购物车
            this.token = ''
            this.profile = null
            removeToken()

            const cartStore = useCartStore()
            cartStore.clear()
        },

        //启动时验证登录状态
        async checkLogin() {
            if (!this.token) return false
            try {
                const user = await getProfile()
                this.profile = user

                const cartStore = useCartStore()
                await cartStore.loadCount()

                return true
            } catch (e) {
                // token 无效或过期
                this.clear()
                ElMessage.warning('登录已过期，请重新登录')
                return false
            }
        },
    },
})
