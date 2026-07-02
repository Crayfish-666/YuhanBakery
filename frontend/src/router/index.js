import { createRouter, createWebHistory } from 'vue-router'
import routes from './routes'

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = to.meta.title + ' - YuhanBakery'
  }

  const token = localStorage.getItem('bakery_token')
  const userInfoStr = localStorage.getItem('bakery_user_info')
  const userInfo = userInfoStr ? JSON.parse(userInfoStr) : null

  // 1. 判断该页面是否需要登录凭证
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!token) {
      // 未登录直接重定向至主页并传递参数触发登录弹窗
      return next({ 
        path: '/customer/home', 
        query: { triggerLogin: 'true', redirect: to.fullPath } 
      })
    }

    // 2. 存在 token，验证路由角色权限限制
    const requiredRole = to.meta.role
    if (requiredRole !== undefined && userInfo) {
      // 仅当需要特权角色（Admin/Rider）且用户角色不匹配时，进行拦截
      if (requiredRole === 1 && userInfo.role !== 1) {
        return next({ path: '/customer/home' })
      }
      if (requiredRole === 2 && userInfo.role !== 2) {
        return next({ path: '/customer/home' })
      }
      // 如果 requiredRole === 0，则允许任何已登录用户（顾客/骑手/管理员）访问，无需拦截
    }
    
    // 角色无冲突，允许放行
    next()
  } else {
    next()
  }
})

export default router
