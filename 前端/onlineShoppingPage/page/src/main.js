import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import router from './router'
import App from './App.vue'
import { ElLoading } from 'element-plus'

import { useUserStore } from '@/store/userStore'  // ✅ 导入在 use 之后没关系

const app = createApp(App)
const pinia = createPinia()  // ✅ 你漏掉了这一行

app.use(pinia)
app.use(ElementPlus)
app.use(router)

const userStore = useUserStore()  // ✅ 确保在 app.use(pinia) 之后调用
userStore.checkLogin()

app.mount('#app')

