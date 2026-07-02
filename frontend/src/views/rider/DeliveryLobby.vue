<template>
  <div class="rider-lobby w-full min-h-[100vh] bg-rider-dark pb-6">
    <!-- Top Status Bar (Sticky) -->
    <div class="sticky top-0 z-40 bg-rider-dark/90 backdrop-blur-xl border-b border-[#333333] px-4 py-4 flex justify-between items-center">
      <h1 class="text-white text-2xl font-black tracking-wide">抢单大厅</h1>
      <div 
        class="flex items-center gap-2 px-4 py-2 rounded-full border cursor-pointer transition-colors duration-300"
        :class="isOnline ? 'bg-[#1C1C1E] border-[#00FF66]/30' : 'bg-[#1C1C1E] border-[#FF3B30]/30'"
        @click="toggleOnline"
      >
        <div class="w-3 h-3 rounded-full" :class="isOnline ? 'bg-[#00FF66] shadow-[0_0_8px_#00FF66]' : 'bg-[#FF3B30]'"></div>
        <span class="text-white font-bold text-sm">{{ isOnline ? '在线接单' : '离线休息' }}</span>
      </div>
    </div>

    <!-- Content Area (16px margins) -->
    <div class="px-4 mt-6">
      <!-- Offline State -->
      <div v-if="!isOnline" class="flex flex-col items-center justify-center mt-32 space-y-4">
        <div class="w-16 h-16 rounded-full bg-[#1C1C1E] border border-[#333333] flex items-center justify-center">
          <van-icon name="moon-o" size="32" color="#8E8E93" />
        </div>
        <h2 class="text-[#FFFFFF] text-xl font-bold">您已下线</h2>
        <p class="text-[#8E8E93] text-sm">点击右上角按钮上线，开始接取新订单</p>
      </div>

      <!-- Online Empty State -->
      <div v-else-if="availableOrders.length === 0" class="flex flex-col items-center justify-center mt-32 space-y-4">
        <van-loading type="spinner" color="#0A84FF" size="32px" />
        <h2 class="text-[#0A84FF] text-lg font-bold tracking-widest animate-pulse">正在扫描附近订单...</h2>
      </div>

      <!-- Order List -->
      <div v-else class="space-y-4">
        <div 
          v-for="order in availableOrders" 
          :key="order.id"
          class="bg-rider-surface rounded-[16px] p-4 flex flex-col gap-4 relative overflow-hidden"
        >
          <!-- 抢单面板的紧急倒计时模拟 (仅作视觉张力展示) -->
          <div class="absolute -right-4 -top-4 w-16 h-16 bg-[#FF3B30]/10 rounded-full flex items-center justify-center">
            <span class="text-[#FF3B30] font-mono-num font-bold text-xs mt-4 mr-4">新</span>
          </div>

          <!-- Header (Price & Distance) -->
          <div class="flex justify-between items-end">
            <div>
              <p class="text-[#8E8E93] text-xs font-bold tracking-wider mb-1">订单金额 / 预计跑腿费</p>
              <h3 class="text-white text-[24px] font-mono-num font-black leading-none flex items-baseline gap-2">
                ¥{{ order.totalAmount }}
                <span class="text-amber-500 text-sm">/ 赚 ¥{{ Math.max(5, (order.totalAmount * 0.1)).toFixed(2) }}</span>
              </h3>
            </div>
            <div class="text-right">
              <p class="text-[#8E8E93] text-xs font-bold tracking-wider mb-1">取送总路程</p>
              <h3 class="text-[#0A84FF] text-xl font-mono-num font-black">{{ ((order.id % 30) / 10 + 1.2).toFixed(1) }} KM</h3>
            </div>
          </div>

          <div class="h-[1px] w-full bg-[#333333]"></div>

          <!-- Route Info -->
          <div class="space-y-4">
            <!-- Pickup -->
            <div class="flex gap-3 items-start">
              <div class="w-6 h-6 rounded bg-[#1C1C1E] border border-[#333333] flex items-center justify-center shrink-0 mt-0.5">
                <span class="text-[#00FF66] text-xs font-black">取</span>
              </div>
              <div class="flex-grow">
                <p class="text-white text-[20px] font-semibold leading-tight">宇晗烘焙 (软件园总店)</p>
                <p class="text-[#8E8E93] text-sm mt-1">请在接单后15分钟内到店</p>
              </div>
            </div>

            <!-- Deliver -->
            <div class="flex gap-3 items-start">
              <div class="w-6 h-6 rounded bg-[#1C1C1E] border border-[#333333] flex items-center justify-center shrink-0 mt-0.5">
                <span class="text-[#0A84FF] text-xs font-black">送</span>
              </div>
              <div class="flex-grow">
                <p class="text-white text-[20px] font-semibold leading-tight">{{ getAddressDetail(order.addressSnapshot) }}</p>
                <p class="text-[#8E8E93] text-sm mt-1">{{ getAddressName(order.addressSnapshot) }}</p>
              </div>
            </div>
          </div>

          <!-- Action Button -->
          <button 
            @click="handleAcceptOrder(order.id)"
            class="w-full h-[56px] bg-[#00FF66] text-black text-lg font-black tracking-widest rounded-[12px] active:scale-[0.98] transition-transform flex items-center justify-center gap-2"
          >
            立即抢单
            <van-icon name="arrow" />
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { Icon as VanIcon, Loading as VanLoading, showSuccessToast } from 'vant'
import { listAvailableOrders, acceptOrder } from '@/api/rider'

const router = useRouter()
const isOnline = ref(false)
const availableOrders = ref([])
let pollInterval = null

const toggleOnline = () => {
  isOnline.value = !isOnline.value
  localStorage.setItem('rider_online_status', isOnline.value ? '1' : '0')
  if (isOnline.value) {
    loadAvailable()
    pollInterval = setInterval(loadAvailable, 10000)
  } else {
    availableOrders.value = []
    if (pollInterval) clearInterval(pollInterval)
  }
}

const getAddressName = (addrSnapshot) => {
  if (!addrSnapshot) return '未知联系人'
  try { return JSON.parse(addrSnapshot).contactName || '顾客' } catch (e) { return '顾客' }
}

const getAddressDetail = (addrSnapshot) => {
  if (!addrSnapshot) return '到店自取'
  try { return JSON.parse(addrSnapshot).addressDetail || '自取' } catch (e) { return addrSnapshot }
}

const loadAvailable = async () => {
  if (!isOnline.value) return
  try {
    const res = await listAvailableOrders({ pageNum: 1, pageSize: 50 })
    availableOrders.value = res.data.records
  } catch (err) { console.error(err) }
}

const handleAcceptOrder = async (orderId) => {
  try {
    await acceptOrder(orderId)
    showSuccessToast({ message: '抢单成功！', className: 'dark-toast' })
    setTimeout(() => {
      router.push('/rider/active')
    }, 500)
  } catch (err) {
    console.error(err)
  }
}

onMounted(() => {
  // 恢复状态
  const savedStatus = localStorage.getItem('rider_online_status')
  if (savedStatus === '1') {
    isOnline.value = true
    loadAvailable()
    pollInterval = setInterval(loadAvailable, 10000)
  }
})

onUnmounted(() => {
  if (pollInterval) clearInterval(pollInterval)
})
</script>

<style scoped>
:deep(.van-toast.dark-toast) {
  background-color: #1C1C1E;
  color: #FFFFFF;
  border: 1px solid #333333;
}
</style>
