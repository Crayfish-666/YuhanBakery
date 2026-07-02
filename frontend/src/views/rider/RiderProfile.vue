<template>
  <div class="rider-profile w-full min-h-[100vh] bg-rider-dark pb-24">
    <!-- Top Status Bar -->
    <div class="sticky top-0 z-40 bg-rider-dark/90 backdrop-blur-xl border-b border-[#333333] px-4 py-4 flex justify-between items-center">
      <h1 class="text-white text-2xl font-black tracking-wide">我的主页</h1>
    </div>

    <div class="px-4 mt-6 space-y-6">
      <!-- 骑手基本信息 Card -->
      <div class="bg-rider-surface rounded-[16px] p-6 flex items-center gap-5 border border-[#333333] relative overflow-hidden">
        <div class="w-20 h-20 rounded-full bg-[#333333] border-2 border-[#00FF66] flex items-center justify-center shrink-0 z-10 shadow-[0_0_15px_rgba(0,255,102,0.3)]">
          <van-icon name="user-circle-o" size="56" color="#FFFFFF" />
        </div>
        <div class="flex-grow z-10">
          <h2 class="text-white text-[24px] font-black">{{ userInfo?.username || '专属骑手' }}</h2>
          <p class="text-[#00FF66] text-sm mt-1 font-mono-num font-bold tracking-widest">NO. {{ userInfo?.id ? String(userInfo.id).padStart(6, '0') : '000000' }}</p>
          <p class="text-[#8E8E93] text-sm mt-1 font-mono-num">{{ userInfo?.phone || '138****0000' }}</p>
        </div>
        <!-- 装饰性背景光晕 -->
        <div class="absolute right-[-20%] top-[-50%] w-48 h-48 bg-[#00FF66] opacity-10 blur-3xl rounded-full pointer-events-none"></div>
      </div>

      <!-- 数据看板 Grid -->
      <div class="grid grid-cols-2 gap-4">
        <!-- 今日数据 -->
        <div class="bg-rider-surface rounded-[16px] p-5 border border-[#333333]">
          <p class="text-[#8E8E93] text-xs font-bold tracking-wider mb-2 flex items-center gap-1">
            <van-icon name="fire" color="#FF3B30" />
            今日单量 (单)
          </p>
          <h3 class="text-white text-[32px] font-mono-num font-black leading-none">{{ todayOrders }}</h3>
        </div>
        
        <!-- 准时率 -->
        <div class="bg-rider-surface rounded-[16px] p-5 border border-[#333333]">
          <p class="text-[#8E8E93] text-xs font-bold tracking-wider mb-2 flex items-center gap-1">
            <van-icon name="checked" color="#0A84FF" />
            今日准时率
          </p>
          <h3 class="text-[#00FF66] text-[32px] font-mono-num font-black leading-none">100%</h3>
        </div>

        <!-- 历史所得 -->
        <div class="bg-rider-surface rounded-[16px] p-5 border border-[#333333]">
          <p class="text-[#8E8E93] text-xs font-bold tracking-wider mb-2 flex items-center gap-1">
            <van-icon name="gold-coin" color="#0A84FF" />
            历史总所得 (元)
          </p>
          <h3 class="text-white text-[24px] font-mono-num font-black leading-none mt-1">{{ totalIncome }}</h3>
        </div>

        <!-- 历史订单 -->
        <div class="bg-rider-surface rounded-[16px] p-5 border border-[#333333]">
          <p class="text-[#8E8E93] text-xs font-bold tracking-wider mb-2 flex items-center gap-1">
            <van-icon name="orders-o" color="#FFFFFF" />
            历史总单量
          </p>
          <h3 class="text-white text-[24px] font-mono-num font-black leading-none mt-1">{{ totalHistoryOrders }}</h3>
        </div>
      </div>

      <!-- Actions -->
      <div class="pt-6 border-t border-[#333333]">
        <button 
          @click="onLogout"
          class="w-full h-[64px] bg-[#1C1C1E] border-2 border-[#FF3B30] text-[#FF3B30] text-[20px] font-black tracking-widest rounded-[16px] active:scale-[0.98] transition-transform flex items-center justify-center gap-2 shadow-[0_4px_15px_rgba(255,59,48,0.15)]"
        >
          <van-icon name="power-off" size="24" />
          下线并退出工作台
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Icon as VanIcon, showConfirmDialog } from 'vant'
import { useUserStore } from '@/store/modules/user'
import request from '@/api/request'

const router = useRouter()
const userStore = useUserStore()
const userInfo = ref(null)

// 模拟数据展示，未来可接真实接口
const todayOrders = ref(Math.floor(Math.random() * 10) + 1)
const totalIncome = ref('0.00')
const totalHistoryOrders = ref(Math.floor(Math.random() * 100) + 20)

const loadData = async () => {
  try {
    const res = await request.get('/user/info')
    userInfo.value = res.data
    // 假设用 balance 或充值额度充当历史所得展示
    totalIncome.value = Number(res.data.balance || 0).toFixed(2)
  } catch (err) {
    console.error(err)
  }
}

const onLogout = () => {
  showConfirmDialog({
    title: '退出工作台',
    message: '下线后系统将停止为您派单。确认退出？',
    className: 'dark-dialog'
  }).then(() => {
    // 置离线
    localStorage.removeItem('rider_online_status')
    userStore.logout()
    router.push('/customer/home')
  }).catch(() => {})
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
:deep(.van-dialog.dark-dialog) {
  background-color: #1C1C1E;
  color: #FFFFFF;
}
:deep(.van-dialog__header) {
  color: #FFFFFF;
  font-weight: 900;
}
:deep(.van-dialog__confirm) {
  color: #FF3B30 !important;
  font-weight: 900;
}
:deep(.van-dialog__cancel) {
  color: #8E8E93 !important;
}
</style>
