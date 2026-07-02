import axios from 'axios'
import { ElMessage } from 'element-plus'
import { showFailToast } from 'vant'
import 'element-plus/theme-chalk/el-message.css'
import 'vant/es/toast/style'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000,
})

// 根据路径自动适配 Vant 还是 Element Plus 进行消息通知
function showErrorMessage(message) {
  const isAddressAdmin = window.location.hash.includes('/admin') || window.location.pathname.includes('/admin');
  if (isAddressAdmin) {
    ElMessage.error(message || '请求执行失败')
  } else {
    showFailToast(message || '操作失败')
  }
}

// 1. 请求拦截器
request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('bakery_token')
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  (error) => {
    console.error('发送请求失败：', error)
    return Promise.reject(error)
  }
)

// 2. 响应拦截器
request.interceptors.response.use(
  (response) => {
    const res = response.data
    // 后端统一 Result 返回结构：code, message, data
    if (res.code === 200) {
      return res
    }

    // 401: Token 失效或暂未登录
    if (res.code === 401) {
      localStorage.removeItem('bakery_token')
      localStorage.removeItem('bakery_user_info')
      showErrorMessage('登录已失效，请重新登录')
      setTimeout(() => {
        // 重定向至登录页
        window.location.href = '/login'
      }, 1500)
      return Promise.reject(new Error(res.message || '未授权'))
    }

    // 403: 拒绝访问
    if (res.code === 403) {
      showErrorMessage('没有相关操作权限')
      return Promise.reject(new Error(res.message || '没有权限'))
    }

    // 其它非 200 的报错
    showErrorMessage(res.message)
    return Promise.reject(new Error(res.message || '业务异常'))
  },
  (error) => {
    console.error('响应报错信息：', error)
    let errorMsg = '网络连接异常，请稍后再试'
    if (error.response && error.response.data && error.response.data.message) {
      errorMsg = error.response.data.message
    }
    showErrorMessage(errorMsg)
    return Promise.reject(error)
  }
)

export default request
