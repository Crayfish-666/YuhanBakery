<template>
  <div class="flex h-screen bg-slate-50/50 font-sans overflow-hidden">
    <!-- Sidebar for Desktop (fixed 260px wide) -->
    <aside 
      class="hidden lg:flex flex-col w-[260px] bg-white border-r border-slate-100 flex-shrink-0"
    >
      <!-- Logo Section -->
      <div class="h-16 flex items-center px-6 border-b border-slate-100 gap-3">
        <div class="w-9 h-9 rounded-xl bg-emerald-600 flex items-center justify-center text-white shadow-sm shadow-emerald-200">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2.5">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364-6.364l-.707.707M6.343 17.657l-.707.707m0-12.728l.707.707m12.728 12.728l.707.707M12 8a4 4 0 100 8 4 4 0 000-8z" />
          </svg>
        </div>
        <span class="font-bold text-slate-800 tracking-tight text-lg">宇晗烘焙 Admin</span>
      </div>
      
      <!-- Nav Links -->
      <nav class="flex-1 px-4 py-6 space-y-2 overflow-y-auto">
        <router-link
          v-for="item in menuItems"
          :key="item.path"
          :to="item.path"
          class="group relative flex items-center gap-3 px-4 py-3 rounded-xl overflow-hidden transition-all duration-300 w-full"
          :class="activePath === item.path ? 'bg-emerald-600 text-white shadow-md shadow-emerald-100 font-semibold' : 'text-slate-500 hover:text-white'"
        >
          <!-- Background slide-in layer for hover (only visible when not active) -->
          <span
            v-if="activePath !== item.path"
            class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-emerald-600 transition-transform duration-300 ease-out group-hover:translate-x-0"
          ></span>
          
          <!-- Content wrapper to align icons and labels above sliding background -->
          <div class="relative z-10 flex items-center gap-3 w-full">
            <span 
              class="w-5 h-5 flex items-center justify-center transition-colors duration-300"
              :class="activePath === item.path ? 'text-white' : 'text-slate-400 group-hover:text-white'"
              v-html="item.iconSvg"
            ></span>
            <span class="text-sm transition-colors duration-300">{{ item.name }}</span>
          </div>
        </router-link>
      </nav>

      <!-- Sidebar Footer -->
      <div class="p-4 border-t border-slate-100">
        <button
          @click="logout"
          class="group relative flex items-center gap-3 px-4 py-3 rounded-xl overflow-hidden transition-all duration-300 w-full text-left text-slate-500 hover:text-white cursor-pointer"
        >
          <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-rose-600 transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
          <div class="relative z-10 flex items-center gap-3 w-full">
            <span class="w-5 h-5 flex items-center justify-center text-slate-400 group-hover:text-white transition-colors duration-300">
              <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
              </svg>
            </span>
            <span class="text-sm font-medium transition-colors duration-300">退出登录</span>
          </div>
        </button>
      </div>
    </aside>

    <!-- Mobile Sidebar Backdrop & Drawer -->
    <div v-if="mobileMenuOpen" class="fixed inset-0 z-40 flex lg:hidden bg-slate-900/40 backdrop-blur-xs transition-opacity duration-300" @click="mobileMenuOpen = false">
      <aside 
        class="flex flex-col w-[260px] max-w-xs bg-white h-full shadow-2xl transition-transform duration-300 ease-in-out"
        :class="mobileMenuOpen ? 'translate-x-0' : '-translate-x-full'"
        @click.stop
      >
        <div class="h-16 flex items-center px-6 border-b border-slate-100 justify-between">
          <div class="flex items-center gap-3">
            <div class="w-9 h-9 rounded-xl bg-emerald-600 flex items-center justify-center text-white shadow-sm shadow-emerald-200">
              <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2.5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364-6.364l-.707.707M6.343 17.657l-.707.707m0-12.728l.707.707m12.728 12.728l.707.707M12 8a4 4 0 100 8 4 4 0 000-8z" />
              </svg>
            </div>
            <span class="font-bold text-slate-800 tracking-tight text-lg">宇晗烘焙 Admin</span>
          </div>
          <button @click="mobileMenuOpen = false" class="text-slate-400 hover:text-slate-600 focus:outline-none cursor-pointer">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <nav class="flex-1 px-4 py-6 space-y-2 overflow-y-auto">
          <router-link
            v-for="item in menuItems"
            :key="item.path"
            :to="item.path"
            @click="mobileMenuOpen = false"
            class="group relative flex items-center gap-3 px-4 py-3 rounded-xl overflow-hidden transition-all duration-300 w-full"
            :class="activePath === item.path ? 'bg-emerald-600 text-white shadow-md shadow-emerald-100 font-semibold' : 'text-slate-500 hover:text-white'"
          >
            <span
              v-if="activePath !== item.path"
              class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-emerald-600 transition-transform duration-300 ease-out group-hover:translate-x-0"
            ></span>
            <div class="relative z-10 flex items-center gap-3 w-full">
              <span 
                class="w-5 h-5 flex items-center justify-center transition-colors duration-300"
                :class="activePath === item.path ? 'text-white' : 'text-slate-400 group-hover:text-white'"
                v-html="item.iconSvg"
              ></span>
              <span class="text-sm transition-colors duration-300">{{ item.name }}</span>
            </div>
          </router-link>
        </nav>

        <div class="p-4 border-t border-slate-100">
          <button
            @click="logout"
            class="group relative flex items-center gap-3 px-4 py-3 rounded-xl overflow-hidden transition-all duration-300 w-full text-left text-slate-500 hover:text-white cursor-pointer"
          >
            <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-rose-600 transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
            <div class="relative z-10 flex items-center gap-3 w-full">
              <span class="w-5 h-5 flex items-center justify-center text-slate-400 group-hover:text-white transition-colors duration-300">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                </svg>
              </span>
              <span class="text-sm font-medium transition-colors duration-300">退出登录</span>
            </div>
          </button>
        </div>
      </aside>
    </div>

    <!-- Main Container Area -->
    <div class="flex-1 flex flex-col min-w-0 overflow-hidden">
      <!-- Top Header -->
      <header class="h-16 bg-white border-b border-slate-100 flex items-center justify-between px-6 flex-shrink-0">
        <!-- Left: Search Box or Mobile Toggle -->
        <div class="flex items-center gap-4 flex-1 max-w-md">
          <button 
            @click="mobileMenuOpen = true" 
            class="lg:hidden text-slate-500 hover:text-slate-800 focus:outline-none p-1.5 rounded-lg hover:bg-slate-50 cursor-pointer"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          </button>
          
          <!-- Return to Mall Button -->
          <button 
            @click="router.push('/customer/home')" 
            class="hidden md:flex items-center gap-2 px-4 py-2 rounded-xl bg-emerald-50 text-emerald-600 hover:bg-emerald-100 font-semibold text-sm transition-colors cursor-pointer"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
            </svg>
            <span>返回商城</span>
          </button>

          <div class="h-6 w-px bg-slate-100 hidden sm:block"></div>

          <!-- User Avatar Dropdown -->
          <el-dropdown trigger="click" @command="handleCommand">
            <div class="flex items-center gap-3 cursor-pointer outline-none group">
              <!-- Avatar with online dot status -->
              <div class="relative">
                <el-avatar :size="36" class="bg-emerald-100 text-emerald-800 font-bold border border-emerald-200">
                  {{ username.substring(0, 1).toUpperCase() }}
                </el-avatar>
                <span class="absolute bottom-0 right-0 w-3 h-3 bg-emerald-500 rounded-full ring-2 ring-white"></span>
              </div>
              <div class="hidden sm:flex flex-col text-left">
                <span class="text-sm font-semibold text-slate-700 group-hover:text-slate-900 transition-colors">{{ username }}</span>
                <span class="text-[10px] text-slate-400 font-medium">系统超级管理员</span>
              </div>
              <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4 text-slate-400 group-hover:text-slate-600 transition-colors" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M19 9l-7 7-7-7" />
              </svg>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <div class="px-4 py-2 border-b border-slate-100 max-w-[200px]">
                  <p class="text-xs font-semibold text-slate-800 truncate">{{ username }}</p>
                  <p class="text-[10px] text-slate-400 truncate">admin@yuhanbakery.com</p>
                </div>
                <el-dropdown-item command="profile">
                  <div class="flex items-center gap-2">
                    <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                    </svg>
                    <span>控制台首页</span>
                  </div>
                </el-dropdown-item>
                <el-dropdown-item command="logout">
                  <div class="flex items-center gap-2 text-rose-600">
                    <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                    </svg>
                    <span>退出登录</span>
                  </div>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <!-- Main Scrollable Main Content -->
      <main class="flex-1 overflow-y-auto bg-slate-50 p-6 md:p-8">
        <div class="max-w-7xl mx-auto">
          <!-- Page title with path details -->
          <div class="mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
            <div>
              <div class="flex items-center gap-2 text-xs text-slate-400 font-medium">
                <span>控制台</span>
                <span>/</span>
                <span class="text-slate-600">{{ pageTitle }}</span>
              </div>
              <h1 class="text-2xl font-bold text-slate-900 tracking-tight mt-1">{{ pageTitle }}</h1>
            </div>
            <!-- Custom header action buttons (if any) -->
            <div class="flex items-center gap-3">
              <button 
                @click="refreshStats"
                class="flex items-center gap-2 px-3.5 py-2 bg-white text-slate-600 rounded-xl border border-slate-100 hover:border-slate-200 shadow-3xs text-xs font-semibold hover:bg-slate-50 active:scale-95 transition-all cursor-pointer"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M4 4v5h.582m15.356 2A8.001 8.001 0 1121.21 7.89H18.24" />
                </svg>
                <span>刷新面板</span>
              </button>
            </div>
          </div>
          
          <!-- Inject child routes directly in layout content area -->
          <router-view />
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  ElDropdown, ElDropdownMenu, ElDropdownItem, ElAvatar, ElMessageBox
} from 'element-plus'
import { useUserStore } from '@/store/modules/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activePath = computed(() => {
  return route.path
})

const pageTitle = computed(() => {
  return route.meta.title || '系统管理'
})

const username = computed(() => {
  return userStore.userInfo?.username || 'Admin'
})

const mobileMenuOpen = ref(false)
const searchInput = ref(null)

const menuItems = [
  {
    name: '看板图表大屏',
    path: '/admin/dashboard',
    iconSvg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="9" rx="1"/><rect x="14" y="3" width="7" height="5" rx="1"/><rect x="14" y="12" width="7" height="9" rx="1"/><rect x="3" y="16" width="7" height="5" rx="1"/></svg>`
  },
  {
    name: '商品分类管辖',
    path: '/admin/category',
    iconSvg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M7 7h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>`
  },
  {
    name: '商品与规格保存',
    path: '/admin/product',
    iconSvg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" /></svg>`
  },
  {
    name: '定制套餐配置',
    path: '/admin/combo',
    iconSvg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" /></svg>`
  },
  {
    name: '全站订单调控',
    path: '/admin/orders',
    iconSvg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" /></svg>`
  }
]

const focusSearch = () => {
  if (searchInput.value) {
    searchInput.value.focus()
  }
}

const refreshStats = () => {
  window.location.reload()
}

const logout = () => {
  ElMessageBox.confirm('您确认要安全退出管理员后台吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logout()
    router.push('/customer/home')
  }).catch(() => {})
}

const handleCommand = (command) => {
  if (command === 'logout') {
    logout()
  } else if (command === 'profile') {
    router.push('/admin/dashboard')
  }
}
</script>

<style scoped>
/* Standard overrides for Element Plus dropdown transitions if needed */
:deep(.el-avatar) {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
