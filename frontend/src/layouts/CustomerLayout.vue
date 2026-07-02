<template>
  <div class="min-h-screen bg-bakery-cream text-bakery-burgundy font-serif selection:bg-amber-200">
    <!-- Desktop/Tablet Header -->
    <header class="sticky top-0 z-50 bg-bakery-cream/90 backdrop-blur-md border-b border-bakery-brown/10">
      <div class="max-w-7xl mx-auto px-6 h-20 flex items-center justify-between">
        <!-- Left: Logo -->
        <div class="flex items-center gap-3 cursor-pointer" @click="$router.push('/customer/home')">
          <div class="w-10 h-10 rounded-xl bg-bakery-burgundy flex items-center justify-center text-bakery-cream font-bold text-lg">
            宇
          </div>
          <div>
            <h1 class="text-xl font-bold tracking-widest">宇晗烘焙</h1>
            <p class="text-[10px] tracking-widest uppercase text-bakery-brown">YUHAN BAKERY</p>
          </div>
        </div>

        <!-- Center: Nav Links (Desktop only) -->
        <nav class="hidden md:flex gap-10 text-sm font-medium tracking-wider text-bakery-brown">
          <router-link to="/customer/home" class="hover:text-amber-700 transition-colors">新品</router-link>
          <router-link to="/customer/home" class="hover:text-amber-700 transition-colors">生日专区</router-link>
          <router-link to="/customer/home" class="hover:text-amber-700 transition-colors">企业订购</router-link>
          <router-link to="/customer/home" class="hover:text-amber-700 transition-colors">配送范围</router-link>
        </nav>

        <!-- Right: Actions -->
        <div class="flex items-center gap-6">
          <button class="group relative overflow-hidden hidden md:flex items-center gap-2 px-4 py-1.5 rounded-full border border-bakery-brown/20 text-bakery-burgundy hover:text-white transition-colors duration-300 text-sm font-sans cursor-pointer">
            <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-bakery-burgundy transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
            <span class="relative z-10 flex items-center gap-2 transition-colors duration-50 group-hover:text-white">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path></svg>
              搜索口味
            </span>
          </button>
          
          <div class="relative cursor-pointer" @click="goToCart">
            <svg class="w-6 h-6 hover:text-amber-700 transition-colors" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z"></path></svg>
            <span v-if="cartCount > 0" class="absolute -top-2 -right-2 bg-amber-700 text-white text-[10px] font-sans font-bold px-1.5 py-0.5 rounded-full shadow-sm">{{ cartCount }}</span>
          </div>

          <!-- User Profile / Login -->
          <div class="relative group font-sans">
            <div @click="handleUserAction" class="cursor-pointer hover:text-amber-700 transition-colors flex items-center gap-2">
              <svg v-if="isLoggedIn" class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path></svg>
              <button v-else class="group relative overflow-hidden text-sm font-bold text-bakery-burgundy border border-bakery-burgundy px-4 py-1.5 rounded-full hover:text-white transition-colors duration-300 cursor-pointer">
                <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-bakery-burgundy transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
                <span class="relative z-10 transition-colors duration-50 group-hover:text-white">登录</span>
              </button>
            </div>
            
            <!-- User Dropdown Menu -->
            <div v-if="isLoggedIn" class="absolute right-0 top-full pt-3 w-48 hidden group-hover:block z-50">
              <div class="bg-white rounded-xl shadow-xl border border-slate-100 py-2 transition-all opacity-0 group-hover:opacity-100">
                <div class="px-4 py-2 border-b border-slate-50 mb-1">
                  <p class="text-xs text-slate-400">当前账号</p>
                  <p class="text-sm font-bold truncate text-slate-800">{{ username }}</p>
                </div>
                <router-link to="/customer/orders" class="block px-4 py-2 text-sm text-slate-600 hover:bg-slate-50 hover:text-amber-700">我的订单</router-link>
                <router-link to="/customer/profile" class="block px-4 py-2 text-sm text-slate-600 hover:bg-slate-50 hover:text-amber-700">钱包与设置</router-link>
                
                <!-- Conditional Entrances for Admin/Rider -->
                <div v-if="userRole === 1 || userRole === 2" class="border-t border-slate-100 my-1"></div>
                <router-link v-if="userRole === 1" to="/admin/dashboard" class="block px-4 py-2 text-sm font-bold text-amber-700 hover:bg-amber-50">🛠️ 管理员控制台</router-link>
                <router-link v-if="userRole === 2" to="/rider/lobby" class="block px-4 py-2 text-sm font-bold text-blue-600 hover:bg-blue-50">🛵 骑手抢单大厅</router-link>
                
                <div class="border-t border-slate-100 my-1"></div>
                <button @click="logout" class="w-full text-left px-4 py-2 text-sm text-red-500 hover:bg-red-50">退出登录</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="flex-grow">
      <router-view v-slot="{ Component }">
        <transition name="fade-slide" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <!-- Footer -->
    <footer class="bg-bakery-cream border-t border-bakery-brown/10 py-8 mt-12 font-sans">
      <div class="max-w-7xl mx-auto px-6 flex flex-col md:flex-row justify-between items-center gap-4">
        <p class="text-xs text-bakery-brown/60">© 2026 宇晗烘焙 · 北京/上海/杭州同城配送</p>
        <p class="text-xs text-bakery-brown/60 flex items-center gap-2">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"></path><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"></path></svg>
          查看配送范围 | 客服 09:00-22:00
        </p>
      </div>
    </footer>

    <!-- 全局 AI 助手悬浮窗 -->
    <FloatingAiChat />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import FloatingAiChat from '@/components/FloatingAiChat.vue'
import { listCart } from '@/api/mall'
import { useUserStore } from '@/store/modules/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const cartCount = ref(0)
const isLoggedIn = computed(() => userStore.isLoggedIn)
const username = computed(() => userStore.userInfo?.username || '')
const userRole = computed(() => userStore.userInfo?.role || 0)

const loadCartBadge = async () => {
  if (!isLoggedIn.value) {
    cartCount.value = 0
    return
  }
  try {
    const res = await listCart()
    if (res && res.data) {
      cartCount.value = res.data.reduce((total, item) => total + item.quantity, 0)
    }
  } catch (err) {
    console.error('获取购物车失败：', err)
  }
}

window.refreshCartBadge = loadCartBadge

const handleUserAction = () => {
  if (!isLoggedIn.value) {
    userStore.showLoginModal = true
    userStore.loginModalTab = 'login'
  }
}

const goToCart = () => {
  if (!isLoggedIn.value) {
    userStore.showLoginModal = true
    userStore.loginModalTab = 'login'
  } else {
    router.push('/customer/cart')
  }
}

const logout = () => {
  userStore.logout()
  cartCount.value = 0
  if (route.meta.requiresAuth) {
    router.push('/customer/home')
  }
}

// Watch token changes to fetch cart count
watch(() => userStore.token, (newToken) => {
  if (newToken) {
    loadCartBadge()
  } else {
    cartCount.value = 0
  }
}, { immediate: true })

// Watch route query to trigger modal automatically on redirect
watch(() => route.query.triggerLogin, (val) => {
  if (val === 'true') {
    userStore.showLoginModal = true
    userStore.loginModalTab = 'login'
    if (route.query.redirect) {
      userStore.redirectAfterLogin = route.query.redirect
    }
  }
}, { immediate: true })

onMounted(() => {
  if (isLoggedIn.value) {
    loadCartBadge()
  }
})
</script>

<style scoped>
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.3s ease-out;
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>
