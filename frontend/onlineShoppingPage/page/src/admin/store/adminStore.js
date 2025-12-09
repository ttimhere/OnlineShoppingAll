import { defineStore } from 'pinia'
import adminRequest from '../api/adminRequest'
import router from '../../router'
//管理员后台登录状态管理
export const useAdminStore = defineStore('adminStore', {
    state: () => ({ //state：存储管理员相关的状态
        token: localStorage.getItem('admin_token') || '', // 管理员登录 token
        admin: JSON.parse(localStorage.getItem('admin_info') || 'null'), // 管理员个人信息
        roles: JSON.parse(localStorage.getItem('admin_roles') || '[]'), // 管理员角色
        menus: JSON.parse(localStorage.getItem('admin_menus') || '[]')// 后台菜单权限
    }),

    actions: {
        setLoginInfo(data) { //管理员登录成功后，保存完整会话
            this.token = data.token
            this.admin = data.admin
            this.roles = data.roles
            this.menus = data.menus
            // 持久化保存到浏览器
            localStorage.setItem('admin_token', this.token)
            localStorage.setItem('admin_info', JSON.stringify(this.admin))
            localStorage.setItem('admin_roles', JSON.stringify(this.roles))
            localStorage.setItem('admin_menus', JSON.stringify(this.menus))
        },

        logout() { //退出后台登录
            // 清空内存中的状态
            this.token = ''
            this.admin = null
            this.roles = []
            this.menus = []
            // 清除持久化
            localStorage.removeItem('admin_token')
            localStorage.removeItem('admin_info')
            localStorage.removeItem('admin_roles')
            localStorage.removeItem('admin_menus')
        }
    }
})
