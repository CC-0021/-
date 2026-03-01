import { defineStore } from 'pinia'
import { login as loginApi, logout as logoutApi, getCaptcha } from '@/api/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userId: localStorage.getItem('userId') || '',
    username: localStorage.getItem('username') || '',
    realName: localStorage.getItem('realName') || '',
    userType: parseInt(localStorage.getItem('userType') || '1', 10),
    permissions: JSON.parse(localStorage.getItem('permissions') || '[]')
  }),

  getters: {
    isAdmin: (state) => state.userType === 2,
    displayName: (state) => state.realName || state.username || '用户'
  },

  actions: {
    setUser(payload) {
      if (payload.userId != null) this.userId = payload.userId
      if (payload.username != null) this.username = payload.username
      if (payload.realName != null) this.realName = payload.realName
      if (payload.userType != null) this.userType = payload.userType
      if (payload.permissions != null) this.permissions = payload.permissions
      localStorage.setItem('userId', this.userId)
      localStorage.setItem('username', this.username)
      localStorage.setItem('realName', this.realName)
      localStorage.setItem('userType', this.userType)
      localStorage.setItem('permissions', JSON.stringify(this.permissions))
    },
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token)
    },
    clearUser() {
      this.token = ''
      this.userId = ''
      this.username = ''
      this.realName = ''
      this.userType = 1
      this.permissions = []
      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      localStorage.removeItem('username')
      localStorage.removeItem('realName')
      localStorage.removeItem('userType')
      localStorage.removeItem('permissions')
    },
    async login({ studentId, password, captcha, captchaKey }) {
      const res = await loginApi({ studentId, password, captcha, captchaKey })
      this.setToken(res.data.token)
      this.setUser({
        userId: res.data.userId,
        username: res.data.username,
        realName: res.data.realName,
        userType: res.data.userType,
        permissions: res.data.permissions || []
      })
      return res
    },
    async logout() {
      try {
        await logoutApi()
      } catch (e) {}
      this.clearUser()
    },
    getCaptcha() {
      return getCaptcha()
    }
  }
})
