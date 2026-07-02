<template>
  <div class="rider-active w-full min-h-[100vh] bg-rider-dark pb-24">
    <!-- Top Status Bar -->
    <div class="sticky top-0 z-40 bg-rider-dark/90 backdrop-blur-xl border-b border-[#333333] px-4 py-4">
      <h1 class="text-white text-2xl font-black tracking-wide">执行中订单</h1>
    </div>

    <div class="px-4 mt-6">
      <!-- Empty State -->
      <div v-if="myOrders.length === 0" class="flex flex-col items-center justify-center mt-32 space-y-4">
        <div class="w-16 h-16 rounded-full bg-[#1C1C1E] border border-[#333333] flex items-center justify-center">
          <van-icon name="logistics" size="32" color="#8E8E93" />
        </div>
        <h2 class="text-[#FFFFFF] text-xl font-bold">暂无任务</h2>
        <p class="text-[#8E8E93] text-sm">前往“抢单”大厅接取订单吧</p>
      </div>

      <!-- Active Orders List -->
      <div class="space-y-6">
        <div 
          v-for="order in myOrders" 
          :key="order.id"
          class="bg-rider-surface rounded-[16px] p-4 flex flex-col relative overflow-hidden"
          :class="order.status === 2 ? 'border-[#0A84FF]' : ''"
        >
          <!-- Status Tag -->
          <div 
            class="absolute top-0 right-0 px-4 py-1 rounded-bl-xl font-bold text-xs"
            :class="order.status === 2 ? 'bg-[#0A84FF] text-white' : 'bg-[#333333] text-[#8E8E93]'"
          >
            {{ order.status === 2 ? '配送中' : '待取餐' }}
          </div>

          <p class="text-[#8E8E93] text-xs font-bold uppercase tracking-widest mt-1 mb-4">订单号 {{ order.orderNo }}</p>

          <div class="space-y-5">
            <!-- Pickup Info -->
            <div class="flex gap-3 items-start">
              <div class="w-8 h-8 rounded bg-[#1C1C1E] border border-[#333333] flex items-center justify-center shrink-0 mt-0.5">
                <span class="text-[var(--rider-neon-green)] text-base font-black">取</span>
              </div>
              <div class="flex-grow">
                <p class="text-white text-[24px] font-semibold leading-tight">宇晗烘焙 (总店)</p>
                <p class="text-[#8E8E93] text-[14px] mt-1">地址: 软件园一期中软国际楼下</p>
              </div>
              <a v-if="order.status === 1" href="tel:13800138000" class="w-10 h-10 rounded-full bg-[#00FF66]/20 text-[#00FF66] flex items-center justify-center shrink-0">
                <van-icon name="phone-circle" size="24" />
              </a>
            </div>

            <!-- Deliver Info -->
            <div class="flex gap-3 items-start" :class="order.status === 1 ? 'opacity-40 grayscale' : ''">
              <div class="w-8 h-8 rounded bg-[#1C1C1E] border border-[#333333] flex items-center justify-center shrink-0 mt-0.5">
                <span class="text-[#0A84FF] text-base font-black">送</span>
              </div>
              <div class="flex-grow">
                <p class="text-white text-[24px] font-semibold leading-tight">{{ getAddressDetail(order.addressSnapshot) }}</p>
                <p class="text-[#8E8E93] text-[14px] mt-1">{{ getAddressName(order.addressSnapshot) }} ({{ getAddressPhone(order.addressSnapshot) }})</p>
              </div>
              <a v-if="order.status === 2" :href="'tel:' + getAddressPhone(order.addressSnapshot)" class="w-10 h-10 rounded-full bg-[#0A84FF]/20 text-[#0A84FF] flex items-center justify-center shrink-0">
                <van-icon name="phone-circle" size="24" />
              </a>
            </div>
          </div>

          <!-- Action Slider -->
          <div class="mt-6">
            <SwipeButton 
              v-if="order.status === 1"
              text=">> 滑动确认已取餐 >>"
              successText="取餐成功"
              @confirm="handleConfirmPickup(order.id)" 
            />
            <SwipeButton 
              v-else-if="order.status === 2"
              text=">> 滑动确认已送达 >>"
              successText="送达成功"
              class="border-[#0A84FF] shadow-[0_0_15px_rgba(10,132,255,0.3)]"
              @confirm="handleConfirmDelivery(order.id)" 
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Icon as VanIcon, showSuccessToast } from 'vant'
import SwipeButton from '@/components/SwipeButton.vue'
import { listMyOrders, completeDelivery } from '@/api/rider'
import request from '@/api/request'

const myOrders = ref([])

const getAddressName = (addrSnapshot) => {
  if (!addrSnapshot) return '未知联系人'
  try { return JSON.parse(addrSnapshot).contactName || '顾客' } catch (e) { return '顾客' }
}

const getAddressPhone = (addrSnapshot) => {
  if (!addrSnapshot) return '无号码'
  try { return JSON.parse(addrSnapshot).contactPhone || '无号码' } catch (e) { return '无号码' }
}

const getAddressDetail = (addrSnapshot) => {
  if (!addrSnapshot) return '自提'
  try { return JSON.parse(addrSnapshot).addressDetail || '自取' } catch (e) { return addrSnapshot }
}

const loadOrders = async () => {
  try {
    const res = await listMyOrders({ pageNum: 1, pageSize: 50 })
    // 只展示待取餐(1)和配送中(2)
    myOrders.value = res.data.records.filter(o => o.status === 1 || o.status === 2)
  } catch (err) { console.error(err) }
}

const handleConfirmPickup = async (orderId) => {
  try {
    // 调用我们刚加的 pickup API
    await request.put('/rider/order/pickup', null, { params: { orderId } })
    showSuccessToast({ message: '取餐成功，请尽快送达！', className: 'dark-toast' })
    setTimeout(loadOrders, 800)
  } catch (err) {
    console.error(err)
    loadOrders()
  }
}

const handleConfirmDelivery = async (orderId) => {
  try {
    await completeDelivery(orderId)
    showSuccessToast({ message: '成功送达，这单辛苦了！', className: 'dark-toast' })
    setTimeout(loadOrders, 800)
  } catch (err) {
    console.error(err)
    loadOrders()
  }
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
:deep(.van-toast.dark-toast) {
  background-color: #1C1C1E;
  color: #FFFFFF;
  border: 1px solid #333333;
}
/* override swipe button thumb color for status 2 */
.border-\[\#0A84FF\] :deep(.swipe-thumb) {
  background-color: #0A84FF !important;
  box-shadow: 0 0 15px rgba(10,132,255,0.5) !important;
}
</style>
