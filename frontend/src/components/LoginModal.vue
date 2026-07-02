<template>
  <Transition name="modal-fade">
    <div 
      v-if="showModal" 
      class="fixed inset-0 z-[9999] flex items-center justify-center bg-slate-900/60 backdrop-blur-xs p-4 overflow-y-auto"
      @click.self="closeModal"
    >
      <!-- Main Card Container -->
      <div 
        class="relative w-full max-w-5xl bg-white rounded-3xl shadow-2xl overflow-hidden min-h-[650px] flex flex-col md:flex-row transition-all duration-300 transform scale-100"
      >
        <!-- Close Button (Absolute) -->
        <button 
          @click="closeModal" 
          class="absolute top-4 right-4 z-50 p-2 text-slate-400 hover:text-slate-700 bg-slate-100/50 hover:bg-slate-100 rounded-full transition-all cursor-pointer border border-transparent hover:border-slate-200"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>

        <!-- Left Pane: Dynamic and Decorative (Blue Theme) -->
        <div class="relative hidden md:flex w-1/2 bg-[#1e293b] text-white flex-col justify-between p-12 overflow-hidden select-none">
          <!-- Morphing SVG Shape 1 -->
          <div 
            class="absolute top-[-10%] left-[-20%] w-72 h-72 bg-blue-500/15 rounded-[3rem] blur-xl transition-all duration-700 ease-in-out"
            :class="isLogin ? 'translate-x-0 rotate-12' : '-translate-x-12 -rotate-[15deg] scale-110'"
          ></div>
          
          <!-- Morphing SVG Shape 2 -->
          <div 
            class="absolute bottom-[-15%] right-[-15%] w-80 h-80 border-4 border-blue-500/15 rounded-full transition-all duration-700 ease-in-out"
            :class="isLogin ? 'translate-x-0 translate-y-0 rotate-45' : 'translate-x-12 -translate-y-8 rotate-90 scale-90'"
          ></div>

          <!-- Morphing SVG Shape 3 -->
          <div 
            class="absolute top-[40%] right-[5%] w-24 h-24 bg-gradient-to-tr from-blue-500/15 to-sky-500/0 rounded-full blur-md transition-all duration-700 ease-in-out"
            :class="isLogin ? 'translate-y-0 rotate-0 scale-100' : '-translate-y-20 rotate-180 scale-125'"
          ></div>

          <!-- Dynamic Text Transition Block -->
          <Transition name="fade-slide" mode="out-in">
            <div :key="isLogin ? 'login-text' : 'register-text'" class="flex flex-col justify-between h-full relative z-10">
              <!-- Top Header & Welcome -->
              <div class="space-y-4">
                <div class="w-12 h-12 rounded-2xl bg-blue-600 flex items-center justify-center text-white font-black text-2xl shadow-lg shadow-blue-500/20">
                  宇
                </div>
                <h2 class="text-3xl font-extrabold tracking-tight text-white mt-12">
                  {{ isLogin ? 'Welcome Back!' : 'Start & Grow With Us' }}
                </h2>
                <p class="text-xs text-blue-100/70 leading-relaxed font-sans mt-2 max-w-sm">
                  {{ isLogin ? '登录宇晗烘焙 Mall 账户。安全登入以访问您的专属特权、钱包余额与订单数据。' : '免费注册成为宇晗烘焙会员！即刻解锁同城冷链配送、快捷订单管理及更多专属会员生日折扣优惠。' }}
                </p>
              </div>
              
              <!-- Bottom Quote -->
              <div class="border-t border-white/10 pt-6">
                <p class="text-xs italic text-blue-50/80 leading-relaxed font-sans">
                  "{{ isLogin ? 'If you double the number of experiments you do per year you\'re going to double your inventiveness.' : 'People don\'t buy what you do; they buy why you do it.' }}"
                </p>
                <p class="text-[10px] uppercase tracking-widest text-blue-400 font-bold mt-2.5">
                  — {{ isLogin ? 'Jeff Bezos' : 'Simon Sinek' }}
                </p>
              </div>
            </div>
          </Transition>
        </div>

        <!-- Right Pane: Forms and Toggle (responsive Safe Area margins for iOS/Android) -->
        <div class="w-full md:w-1/2 px-6 py-12 md:p-12 flex flex-col justify-center bg-white relative overflow-hidden pb-safe">
          <!-- Mobile Header (Visible only on mobile screen widths) -->
          <div class="flex md:hidden items-center gap-3 mb-8">
            <div class="w-8 h-8 rounded-lg bg-blue-600 flex items-center justify-center text-white font-bold text-sm">
              宇
            </div>
            <div>
              <h2 class="text-sm font-bold text-slate-800">宇晗烘焙</h2>
              <p class="text-[8px] text-slate-400 font-medium">YUHAN BAKERY</p>
            </div>
          </div>

          <!-- Forms relative container block -->
          <div class="relative w-full min-h-[460px] overflow-hidden">
            <Transition :name="formTransition">
              <!-- LOGIN FORM -->
              <form 
                v-if="isLogin" 
                key="login" 
                @submit.prevent="handleLogin"
                class="absolute inset-0 flex flex-col justify-center space-y-4 w-full transition-all duration-500 ease-out"
              >
                <div>
                  <h3 class="text-xl font-bold text-slate-800">账号登录</h3>
                  <p class="text-[11px] text-slate-400 mt-1">请输入您的系统账号及安全密码完成登录</p>
                </div>

                <!-- Input Username -->
                <div class="space-y-1">
                  <label class="block text-slate-500 text-[10px] font-bold uppercase ml-1">用户名</label>
                  <input 
                    v-model="loginForm.username" 
                    type="text" 
                    required 
                    placeholder="请输入您的用户名" 
                    class="w-full text-xs px-4 py-3 bg-slate-50 text-slate-800 border border-slate-100 rounded-xl focus:outline-none focus:border-blue-600 focus:bg-white transition-all"
                  />
                </div>

                <!-- Input Password -->
                <div class="space-y-1">
                  <label class="block text-slate-500 text-[10px] font-bold uppercase ml-1">登录密码</label>
                  <input 
                    v-model="loginForm.password" 
                    type="password" 
                    required 
                    placeholder="请输入密码" 
                    class="w-full text-xs px-4 py-3 bg-slate-50 text-slate-800 border border-slate-100 rounded-xl focus:outline-none focus:border-blue-600 focus:bg-white transition-all"
                  />
                </div>

                <!-- Action Button -->
                <button 
                  type="submit" 
                  :disabled="loading"
                  class="group relative overflow-hidden w-full py-3 bg-blue-600 text-white font-bold rounded-xl transition-all duration-300 hover:scale-[1.005] hover:shadow-md cursor-pointer flex items-center justify-center text-xs"
                >
                  <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-blue-700 transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
                  <span class="relative z-10 text-white transition-colors duration-200 group-hover:text-white">
                    {{ loading ? '正在验证身份...' : '立即登录' }}
                  </span>
                </button>

                <!-- Social Logins Divider -->
                <div class="relative flex items-center justify-center my-2 text-[10px] text-slate-400">
                  <div class="absolute w-full h-px bg-slate-100"></div>
                  <span class="relative bg-white px-3 font-medium">第三方快捷登入</span>
                </div>

                <!-- Social Login Buttons -->
                <div class="grid grid-cols-2 gap-3">
                  <button type="button" @click="handleSocialLogin('Google')" class="flex items-center justify-center gap-2 py-2 border border-slate-100 hover:border-slate-200 rounded-xl transition-all hover:bg-slate-50 cursor-pointer text-xs text-slate-600 font-medium">
                    <svg viewBox="0 0 24 24" class="w-4 h-4"><path fill="#EA4335" d="M12 5.04c1.66 0 3.2.57 4.38 1.69l3.27-3.27C17.68 1.54 14.98 1 12 1 7.35 1 3.37 3.65 1.4 7.56l3.92 3.04C6.26 7.55 8.91 5.04 12 5.04z"/><path fill="#4285F4" d="M23.49 12.27c0-.81-.07-1.59-.2-2.36H12v4.51h6.46c-.29 1.48-1.14 2.73-2.4 3.58l3.76 2.91c2.2-2.03 3.67-5.01 3.67-8.64z"/><path fill="#FBBC05" d="M5.32 14.88c-.23-.69-.36-1.42-.36-2.18s.13-1.49.36-2.18L1.4 7.48C.51 9.24 0 11.21 0 13.3s.51 4.06 1.4 5.82l3.92-3.24z"/><path fill="#34A853" d="M12 23c3.24 0 5.97-1.07 7.96-2.91l-3.76-2.91c-1.1.74-2.5 1.18-4.2 1.18-3.09 0-5.74-2.51-6.68-5.56L1.4 16.04C3.37 19.95 7.35 23 12 23z"/></svg>
                    <span>Google</span>
                  </button>
                  <button type="button" @click="handleSocialLogin('Apple')" class="flex items-center justify-center gap-2 py-2 border border-slate-100 hover:border-slate-200 rounded-xl transition-all hover:bg-slate-50 cursor-pointer text-xs text-slate-600 font-medium">
                    <svg viewBox="0 0 16 16" class="w-4 h-4 fill-slate-800"><path d="M11.182.008C11.148-.03 9.923.023 8.857 1.18c-1.066 1.156-.902 2.482-.878 2.516s1.52.087 2.475-1.258.762-2.391.728-2.43m3.314 11.733c-.048-.096-2.325-1.234-2.113-3.422s1.675-2.789 1.698-2.854-.597-.79-1.254-1.157a3.7 3.7 0 0 0-1.563-.434c-.108-.003-.483-.095-1.254.116-.508.139-1.653.589-1.968.607-.316.018-1.256-.522-2.267-.665-.647-.125-1.333.131-1.824.328-.49.196-1.422.754-2.074 2.237-.652 1.482-.311 3.83-.067 4.56s.625 1.924 1.273 2.796c.576.984 1.34 1.667 1.659 1.899s1.219.386 1.843.067c.502-.308 1.408-.485 1.766-.472.357.013 1.061.154 1.782.539.571.197 1.111.115 1.652-.105.541-.221 1.324-1.059 2.238-2.758q.52-1.185.473-1.282"/></svg>
                    <span>Apple ID</span>
                  </button>
                </div>

                <!-- Form Switcher Links -->
                <p class="text-[11px] text-slate-500 text-center pt-2">
                  还没有系统账号？
                  <button type="button" @click="isLogin = false" class="text-blue-600 hover:underline font-bold focus:outline-none cursor-pointer bg-transparent border-none">
                    立即注册
                  </button>
                </p>
              </form>

              <!-- REGISTER FORM -->
              <form 
                v-else 
                key="register" 
                @submit.prevent="handleRegister"
                class="absolute inset-0 flex flex-col justify-center space-y-3.5 w-full transition-all duration-500 ease-out"
              >
                <div>
                  <h3 class="text-xl font-bold text-slate-800">免费注册</h3>
                  <p class="text-[11px] text-slate-400 mt-0.5">请填写以下表单快速创建系统账号</p>
                </div>

                <!-- Input Username -->
                <div class="space-y-1">
                  <label class="block text-slate-500 text-[10px] font-bold uppercase ml-1">用户名</label>
                  <input 
                    v-model="registerForm.username" 
                    type="text" 
                    required 
                    placeholder="设置您的系统唯一用户名" 
                    class="w-full text-xs px-4 py-2.5 bg-slate-50 text-slate-800 border border-slate-100 rounded-xl focus:outline-none focus:border-blue-600 focus:bg-white transition-all"
                  />
                </div>

                <!-- Input Phone -->
                <div class="space-y-1">
                  <label class="block text-slate-500 text-[10px] font-bold uppercase ml-1">手机号码</label>
                  <input 
                    v-model="registerForm.phone" 
                    type="tel" 
                    required 
                    placeholder="请输入11位手机号码" 
                    class="w-full text-xs px-4 py-2.5 bg-slate-50 text-slate-800 border border-slate-100 rounded-xl focus:outline-none focus:border-blue-600 focus:bg-white transition-all"
                  />
                </div>

                <!-- Input Password -->
                <div class="space-y-1">
                  <label class="block text-slate-500 text-[10px] font-bold uppercase ml-1">登录密码</label>
                  <input 
                    v-model="registerForm.password" 
                    type="password" 
                    required 
                    placeholder="设置6位以上的密码" 
                    class="w-full text-xs px-4 py-2.5 bg-slate-50 text-slate-800 border border-slate-100 rounded-xl focus:outline-none focus:border-blue-600 focus:bg-white transition-all"
                  />
                </div>

                <!-- Role Selection Checkboxes -->
                <div class="space-y-1.5">
                  <label class="block text-slate-500 text-[10px] font-bold uppercase ml-1">选择登录角色</label>
                  <div class="grid grid-cols-2 gap-2">
                    <button
                      v-for="role in roles"
                      :key="role.value"
                      type="button"
                      @click="registerForm.role = role.value"
                      :class="registerForm.role === role.value ? 'bg-blue-50 border-blue-600 text-blue-800 font-bold' : 'bg-slate-50 border-slate-100 text-slate-500 hover:border-slate-200'"
                      class="border py-2 text-[10px] rounded-xl text-center font-medium transition cursor-pointer"
                    >
                      {{ role.label }}
                    </button>
                  </div>
                </div>

                <!-- Action Button -->
                <button 
                  type="submit" 
                  :disabled="loading"
                  class="group relative overflow-hidden w-full py-3 bg-blue-600 text-white font-bold rounded-xl transition-all duration-300 hover:scale-[1.005] hover:shadow-md cursor-pointer flex items-center justify-center text-xs"
                >
                  <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-blue-700 transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
                  <span class="relative z-10 text-white transition-colors duration-200 group-hover:text-white">
                    {{ loading ? '正在创建账户...' : '提交注册并登录' }}
                  </span>
                </button>

                <!-- Form Switcher Link -->
                <p class="text-[11px] text-slate-500 text-center pt-1">
                  已经有账户？
                  <button type="button" @click="isLogin = true" class="text-blue-600 hover:underline font-bold focus:outline-none cursor-pointer bg-transparent border-none">
                    立即登录
                  </button>
                </p>
              </form>
            </Transition>
          </div>
        </div>
      </div>
    </div>
  </Transition>
</template>

<script setup>
import { ref, computed, reactive, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/modules/user'
import { register as registerApi } from '@/api/user'
import { showSuccessToast, showFailToast } from 'vant'
import 'vant/es/toast/style'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loading = ref(false)

const showModal = computed(() => userStore.showLoginModal)

// Sync local tab state with userStore global tab choice
const isLogin = computed({
  get: () => userStore.loginModalTab === 'login',
  set: (val) => {
    userStore.loginModalTab = val ? 'login' : 'register'
  }
})

// Dynamic transition class based on active tab state to trigger slide direction
const formTransition = computed(() => isLogin.value ? 'slide-right' : 'slide-left')

const roles = [
  { label: '顾客', value: 0 },
  { label: '配送骑手', value: 2 }
]

const loginForm = reactive({
  username: '',
  password: ''
})

const registerForm = reactive({
  username: '',
  phone: '',
  password: '',
  role: 0
})

const closeModal = () => {
  userStore.showLoginModal = false
  // Clean up trigger query if we are closing from a forced redirect on storefront
  if (route.query.triggerLogin) {
    const newQuery = { ...route.query }
    delete newQuery.triggerLogin
    delete newQuery.redirect
    router.replace({ path: route.path, query: newQuery })
  }
}

// Watch keyboard escape key
if (typeof window !== 'undefined') {
  window.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && userStore.showLoginModal) {
      closeModal()
    }
  })
}

// 登录请求处理
const handleLogin = async () => {
  loading.value = true
  try {
    const userInfo = await userStore.login(loginForm)
    showSuccessToast('登录成功')
    
    // Clear forms
    loginForm.username = ''
    loginForm.password = ''
    
    closeModal()
    
    // Check if there was an active redirect query
    const targetRedirect = userStore.redirectAfterLogin || route.query.redirect
    if (targetRedirect) {
      userStore.redirectAfterLogin = ''
      router.push(targetRedirect)
    } else {
      // Automatic split routing based on roles if logged in normally
      setTimeout(() => {
        if (userInfo.role === 1) {
          router.push('/admin/dashboard')
        } else if (userInfo.role === 2) {
          router.push('/rider/lobby')
        } else {
          // Customer reloads storefront to update headers and wallet balance
          window.location.reload()
        }
      }, 500)
    }
  } catch (err) {
    console.error(err)
    showFailToast(err.response?.data?.message || '登录验证失败，请重试')
  } finally {
    loading.value = false
  }
}

// 注册请求处理
const handleRegister = async () => {
  if (registerForm.phone.length !== 11) {
    return showFailToast('请输入有效的11位手机号码')
  }
  if (registerForm.password.length < 6) {
    return showFailToast('密码长度不能少于6位')
  }
  
  loading.value = true
  try {
    await registerApi(registerForm)
    showSuccessToast('账号注册成功！')
    
    // Auto login right after registration
    const userInfo = await userStore.login({
      username: registerForm.username,
      password: registerForm.password
    })
    
    // Reset forms
    registerForm.username = ''
    registerForm.phone = ''
    registerForm.password = ''
    registerForm.role = 0
    
    closeModal()
    
    const targetRedirect = userStore.redirectAfterLogin || route.query.redirect
    if (targetRedirect) {
      userStore.redirectAfterLogin = ''
      router.push(targetRedirect)
    } else {
      setTimeout(() => {
        if (userInfo.role === 1) {
          router.push('/admin/dashboard')
        } else if (userInfo.role === 2) {
          router.push('/rider/lobby')
        } else {
          window.location.reload()
        }
      }, 500)
    }
  } catch (err) {
    console.error(err)
    showFailToast(err.response?.data?.message || '创建账号失败，请重试')
  } finally {
    loading.value = false
  }
}

const handleSocialLogin = (platform) => {
  showSuccessToast(`${platform} 快捷登录成功 (演示数据)`)
  // Auto authenticate with mock customer session for demonstration
  loginForm.username = 'demo_user'
  loginForm.password = '123456'
  handleLogin()
}
</script>

<style scoped>
/* Modal Fade-in / Fade-out */
.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: opacity 0.3s ease;
}
.modal-fade-enter-from,
.modal-fade-leave-to {
  opacity: 0;
}

/* Left panel text fade-slide transitions */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.4s ease-in-out;
}
.fade-slide-enter-from {
  opacity: 0;
  transform: translateY(15px);
}
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-15px);
}

/* Forms slide transition triggers (overlapping absolute forms) */
.slide-left-enter-active,
.slide-left-leave-active,
.slide-right-enter-active,
.slide-right-leave-active {
  transition: all 0.5s cubic-bezier(0.25, 1, 0.5, 1);
}

.slide-left-enter-from {
  opacity: 0;
  transform: translateX(100%);
}
.slide-left-leave-to {
  opacity: 0;
  transform: translateX(-100%);
}

.slide-right-enter-from {
  opacity: 0;
  transform: translateX(-100%);
}
.slide-right-leave-to {
  opacity: 0;
  transform: translateX(100%);
}

/* iOS Safe Areas */
.pb-safe {
  padding-bottom: calc(12px + env(safe-area-inset-bottom, 0px));
}
</style>
