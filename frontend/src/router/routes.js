import AdminLayout from '@/layouts/AdminLayout.vue'
import CustomerLayout from '@/layouts/CustomerLayout.vue'
import RiderLayout from '@/layouts/RiderLayout.vue'

const routes = [
  {
    path: '/',
    redirect: '/customer/home'
  },

  // 顾客移动端（C端）路由
  {
    path: '/customer',
    component: CustomerLayout,
    redirect: '/customer/home',
    // 移除全局的 requiresAuth，单独在子路由配置
    children: [
      {
        path: 'home',
        name: 'CustomerHome',
        component: () => import('@/views/customer/Home.vue'),
        meta: { title: '烘焙商城大厅' }
      },
      {
        path: 'product/:id',
        name: 'CustomerProductDetail',
        component: () => import('@/views/customer/ProductDetail.vue'),
        meta: { title: '商品详情与评价' }
      },
      {
        path: 'cart',
        name: 'CustomerCart',
        component: () => import('@/views/customer/Cart.vue'),
        meta: { title: '购物车结算', requiresAuth: true, role: 0 }
      },
      {
        path: 'orders',
        name: 'CustomerOrders',
        component: () => import('@/views/customer/OrderList.vue'),
        meta: { title: '我的订单跟踪', requiresAuth: true, role: 0 }
      },
      {
        path: 'order/:orderNo',
        name: 'CustomerOrderDetail',
        component: () => import('@/views/customer/OrderDetail.vue'),
        meta: { title: '订单详情', requiresAuth: true, role: 0 }
      },
      {
        path: 'profile',
        name: 'CustomerProfile',
        component: () => import('@/views/customer/Profile.vue'),
        meta: { title: '个人钱包中心', requiresAuth: true, role: 0 }
      }
    ]
  },
  // 骑手移动端路由
  {
    path: '/rider',
    component: RiderLayout,
    redirect: '/rider/lobby',
    meta: { requiresAuth: true, role: 2 },
    children: [
      {
        path: 'lobby',
        name: 'RiderLobby',
        component: () => import('@/views/rider/DeliveryLobby.vue'),
        meta: { title: '骑手抢单大厅' }
      },
      {
        path: 'active',
        name: 'RiderActiveOrder',
        component: () => import('@/views/rider/ActiveOrder.vue'),
        meta: { title: '执行中订单' }
      },
      {
        path: 'earnings',
        name: 'RiderEarnings',
        component: () => import('@/views/rider/Earnings.vue'),
        meta: { title: '收益记录' }
      },
      {
        path: 'profile',
        name: 'RiderProfile',
        component: () => import('@/views/rider/RiderProfile.vue'),
        meta: { title: '我的' }
      }
    ]
  },
  // 管理员PC端（B端）路由
  {
    path: '/admin',
    component: AdminLayout,
    redirect: '/admin/dashboard',
    meta: { requiresAuth: true, role: 1 },
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '控制大屏分析' }
      },
      {
        path: 'category',
        name: 'AdminCategory',
        component: () => import('@/views/admin/Category.vue'),
        meta: { title: '商品分类管辖' }
      },
      {
        path: 'product',
        name: 'AdminProduct',
        component: () => import('@/views/admin/ProductForm.vue'),
        meta: { title: '规格商品级联保存' }
      },
      {
        path: 'combo',
        name: 'AdminCombo',
        component: () => import('@/views/admin/ComboForm.vue'),
        meta: { title: '定制套餐配置' }
      },
      {
        path: 'orders',
        name: 'AdminOrders',
        component: () => import('@/views/admin/AdminOrders.vue'),
        meta: { title: '全站订单调控' }
      }
    ]
  },
  // 404 回退重定向
  {
    path: '/:pathMatch(.*)*',
    redirect: '/customer/home'
  }
]

export default routes
