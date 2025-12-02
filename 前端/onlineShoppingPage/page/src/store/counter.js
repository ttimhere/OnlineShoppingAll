import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: null
  }),
  actions: {
    setToken(t) {
      this.token = t
      if (t) localStorage.setItem('token', t)
      else localStorage.removeItem('token')
    },
    setUser(u) { this.user = u }
  }
})
