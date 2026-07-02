import { defineStore } from 'pinia'
import { login as loginApi, getUserInfo } from '@/api/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('bakery_token') || '',
    userInfo: JSON.parse(localStorage.getItem('bakery_user_info') || '{}'),
    showLoginModal: false,
    loginModalTab: 'login',
    redirectAfterLogin: ''
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
    role: (state) => state.userInfo.role // 0-顾客，1-管理员，2-骑手
  },
  actions: {
    // 用户登录
    async login(loginForm) {
      try {
        const res = await loginApi(loginForm)
        const token = res.data
        this.token = token
        localStorage.setItem('bakery_token', token)
        
        // 登录成功后拉取用户信息
        await this.getInfo()
        return this.userInfo
      } catch (error) {
        return Promise.reject(error)
      }
    },
    
    // 获取用户信息
    async getInfo() {
      try {
        const res = await getUserInfo()
        const info = res.data
        this.userInfo = info
        localStorage.setItem('bakery_user_info', JSON.stringify(info))
        return info
      } catch (error) {
        this.logout()
        return Promise.reject(error)
      }
    },
    
    // 用户登出
    logout() {
      this.token = ''
      this.userInfo = {}
      localStorage.removeItem('bakery_token')
      localStorage.removeItem('bakery_user_info')
    }
  }
})
