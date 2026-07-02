<template>
  <div class="orders-view max-w-7xl mx-auto px-6 pt-6 space-y-4 pb-20">
    <h1 class="text-xl font-extrabold text-slate-800 mt-1">我的订单</h1>

    <!-- 订单无记录占位 -->
    <div v-if="orders.length === 0" class="flex flex-col items-center justify-center py-20 space-y-4">
      <div class="w-24 h-24 rounded-full bg-slate-200/50 flex items-center justify-center text-slate-400">
        <van-icon name="bill-o" size="48" />
      </div>
      <p class="text-slate-500 font-bold text-sm">暂无历史订单</p>
      <button
        @click="router.push('/customer/home')"
        class="group relative overflow-hidden px-6 py-2 border-2 border-bakery-burgundy bg-transparent text-bakery-burgundy text-xs font-bold rounded-xl transition-colors duration-300 hover:text-white cursor-pointer"
      >
        <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-bakery-burgundy transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
        <span class="relative z-10 transition-colors duration-50 group-hover:text-white">去逛逛商城</span>
      </button>
    </div>

    <!-- 订单列表展示 -->
    <div v-else class="space-y-4 pb-20">
      <div
        v-for="order in orders"
        :key="order.id"
        class="glass-panel p-4 rounded-2xl border border-slate-100 shadow-sm space-y-3"
      >
        <!-- 订单头部：单号 & 状态 -->
        <div class="flex items-center justify-between border-b border-slate-100 pb-2 text-xs">
          <span class="text-slate-400 font-medium">单号: {{ order.orderNo }}</span>
          <span :class="getStatusClass(order.status)" class="font-bold px-2 py-0.5 rounded-full">
            {{ getStatusText(order.status) }}
          </span>
        </div>

        <!-- 订单明细项 -->
        <div class="space-y-2">
          <div v-for="item in order.items" :key="item.id" 
               class="flex gap-3 cursor-pointer hover:bg-slate-50 transition p-1 rounded-xl"
               @click="$router.push(`/customer/product/${item.productId}`)">
            <img :src="item.productImage" class="w-12 h-12 object-cover rounded-lg bg-slate-50 border border-slate-100" />
            <div class="flex-grow flex flex-col justify-between py-0.5">
              <div>
                <h4 class="text-xs font-bold text-slate-800 line-clamp-1">{{ item.productName }}</h4>
                <p class="text-[10px] text-slate-400 mt-0.5">{{ item.specName }}</p>
              </div>
              <div class="flex justify-between text-[11px]">
                <span class="text-slate-500">¥{{ item.price }}</span>
                <span class="text-slate-400">x{{ item.quantity }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 订单底部：合计 & 操作按钮 -->
        <div class="flex items-center justify-between border-t border-slate-100 pt-3">
          <div class="text-xs text-slate-500">
            共 <span class="font-bold text-slate-700">{{ getItemsCount(order) }}</span> 件，
            合计 <span class="font-black text-amber-600 text-sm">¥{{ order.totalAmount }}</span>
          </div>

          <!-- 待支付操作 -->
          <div v-if="order.status === 0" class="flex gap-2">
            <button
              @click="handleCancel(order.orderNo)"
              class="group relative overflow-hidden px-3 py-1.5 border border-slate-300 bg-transparent text-slate-500 text-[11px] font-bold rounded-lg transition-colors duration-300 hover:text-white cursor-pointer"
            >
              <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-slate-400 transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
              <span class="relative z-10 transition-colors duration-50 group-hover:text-white">取消</span>
            </button>
            <button
              @click="openPayDrawer(order)"
              class="group relative overflow-hidden px-4 py-1.5 border border-bakery-burgundy bg-transparent text-bakery-burgundy text-[11px] font-bold rounded-lg transition-colors duration-300 hover:text-white cursor-pointer"
            >
              <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-bakery-burgundy transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
              <span class="relative z-10 transition-colors duration-50 group-hover:text-white">去支付</span>
            </button>
          </div>
          
          <!-- 配送中 -->
          <div v-else-if="order.status === 2" class="text-[10px] text-slate-400">
            骑手正快马加鞭送货中...
          </div>
          <!-- 已完成，去评价 -->
          <div v-else-if="order.status === 3" class="flex gap-2">
            <button
              @click="openReviewDrawer(order)"
              class="group relative overflow-hidden px-4 py-1.5 border border-amber-600 bg-transparent text-amber-600 text-[11px] font-bold rounded-lg transition-colors duration-300 hover:text-white cursor-pointer"
            >
              <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-amber-600 transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
              <span class="relative z-10 transition-colors duration-50 group-hover:text-white">去评价</span>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 评价抽屉 -->
    <van-action-sheet v-model:show="showReviewDrawer" title="商品评价" class="rounded-t-3xl">
      <div v-if="selectedOrder" class="p-5 space-y-5">
        <!-- 为了简单起见，这里假设对订单第一个商品进行评价 -->
        <div class="space-y-4">
          <p class="text-sm font-bold text-slate-700">请对订单商品打分</p>
          <div class="flex items-center gap-2">
            <span class="text-xs text-slate-500">满意度：</span>
            <div class="flex gap-1">
              <svg v-for="i in 5" :key="i" @click="reviewForm.rating = i" xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 cursor-pointer" :class="i <= reviewForm.rating ? 'text-amber-400' : 'text-slate-200'" viewBox="0 0 20 20" fill="currentColor">
                <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
              </svg>
            </div>
          </div>
          <textarea v-model="reviewForm.content" class="w-full border border-slate-200 rounded-xl p-3 text-sm focus:outline-none focus:border-amber-500" rows="3" placeholder="写点评价吧，这对其他顾客很有帮助哦..."></textarea>
        </div>
        
        <button
          @click="submitReview"
          class="group relative overflow-hidden w-full border-2 border-amber-500 bg-amber-500 text-white font-bold py-3.5 rounded-xl transition-colors duration-300 hover:text-white text-xs mt-3 cursor-pointer"
        >
          <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-amber-600 transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
          <span class="relative z-10 transition-colors duration-50 group-hover:text-white">提交评价</span>
        </button>
      </div>
    </van-action-sheet>

    <!-- 收银台支付抽屉 -->
    <van-action-sheet v-model:show="showPayDrawer" title="收银台" class="rounded-t-3xl">
      <div v-if="selectedOrder" class="p-5 space-y-5">
        <div class="text-center space-y-1">
          <p class="text-xs text-slate-400">应付金额</p>
          <h2 class="text-3xl font-black text-slate-800">¥{{ selectedOrder.totalAmount }}</h2>
          <p class="text-[10px] text-slate-400 mt-1">单号: {{ selectedOrder.orderNo }}</p>
        </div>

        <!-- 支付方式选项 -->
        <div class="space-y-2.5">
          <!-- 余额支付 -->
          <div
            @click="activePayMethod = 1"
            :class="activePayMethod === 1 ? 'border-amber-500 bg-amber-50/20' : 'border-slate-100 bg-slate-50'"
            class="border p-3.5 rounded-xl cursor-pointer flex items-center justify-between transition"
          >
            <div class="flex items-center gap-3">
              <van-icon name="gold-coin-o" size="24" color="#e07a5f" />
              <div>
                <p class="text-xs font-bold text-slate-700">钱包余额支付</p>
                <p class="text-[10px] text-slate-400 mt-0.5">可用余额：¥{{ userBalance }}</p>
              </div>
            </div>
            <van-icon v-if="activePayMethod === 1" name="success" color="#e07a5f" />
          </div>

          <!-- 模拟微信支付 -->
          <div
            @click="activePayMethod = 2"
            :class="activePayMethod === 2 ? 'border-amber-500 bg-amber-50/20' : 'border-slate-100 bg-slate-50'"
            class="border p-3.5 rounded-xl cursor-pointer flex items-center justify-between transition"
          >
            <div class="flex items-center gap-3">
              <van-icon name="wechat" size="24" color="#07c160" />
              <div>
                <p class="text-xs font-bold text-slate-700">微信支付 (本地沙箱模拟)</p>
                <p class="text-[10px] text-slate-400 mt-0.5">免证书，一键直接回调支付成功</p>
              </div>
            </div>
            <van-icon v-if="activePayMethod === 2" name="success" color="#e07a5f" />
          </div>
        </div>

        <!-- 立即支付按钮 -->
        <button
          @click="handlePaySubmit"
          :disabled="paying"
          class="group relative overflow-hidden w-full border-2 border-bakery-burgundy bg-transparent text-bakery-burgundy font-bold py-3.5 rounded-xl transition-colors duration-300 hover:text-white text-xs mt-3 cursor-pointer"
        >
          <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-bakery-burgundy transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
          <span v-if="paying" class="relative z-10 transition-colors duration-50 group-hover:text-white">进行安全结算中...</span>
          <span v-else class="relative z-10 transition-colors duration-50 group-hover:text-white">确认支付</span>
        </button>
      </div>
    </van-action-sheet>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Icon as VanIcon, ActionSheet as VanActionSheet, showSuccessToast, showFailToast, showConfirmDialog } from 'vant'
import { listOrders, payWithWallet, mockWeChatPay, cancelOrder, submitReviewApi } from '@/api/order'
import { getUserInfo } from '@/api/user'
import 'vant/es/toast/style'

const router = useRouter()
const orders = ref([])
const showPayDrawer = ref(false)
const selectedOrder = ref(null)
const activePayMethod = ref(1) // 1-余额，2-微信
const userBalance = ref('0.00')
const paying = ref(false)

const showReviewDrawer = ref(false)
const reviewForm = ref({
  orderNo: '',
  productId: '',
  rating: 5,
  content: ''
})

const loadOrders = async () => {
  try {
    const res = await listOrders({ pageNum: 1, pageSize: 50 })
    orders.value = res.data.records
  } catch (err) {
    console.error(err)
  }
}

const getItemsCount = (order) => {
  if (!order.items) return 0
  return order.items.reduce((sum, item) => sum + item.quantity, 0)
}

const getStatusText = (status) => {
  switch (status) {
    case 0: return '未支付'
    case 1: return '待接单'
    case 2: return '配送中'
    case 3: return '已送达'
    case 4: return '已取消'
    default: return '未知'
  }
}

const getStatusClass = (status) => {
  switch (status) {
    case 0: return 'text-amber-500 bg-amber-50'
    case 1: return 'text-sky-500 bg-sky-50'
    case 2: return 'text-blue-500 bg-blue-50'
    case 3: return 'text-emerald-500 bg-emerald-50'
    case 4: return 'text-slate-400 bg-slate-50'
    default: return 'text-slate-400 bg-slate-50'
  }
}

// 打开收银台
const openPayDrawer = async (order) => {
  selectedOrder.value = order
  activePayMethod.value = 1
  showPayDrawer.value = true

  // 获取用户钱包可用余额
  try {
    const res = await getUserInfo()
    userBalance.value = res.data.balance
  } catch (err) {
    console.error(err)
  }
}

// 下银台结算
const handlePaySubmit = async () => {
  if (!selectedOrder.value) return

  paying.value = true
  try {
    if (activePayMethod.value === 1) {
      // 钱包支付
      await payWithWallet(selectedOrder.value.orderNo)
      showSuccessToast('余额扣减成功，支付成功')
    } else {
      // 微信支付 (本地沙箱)
      await mockWeChatPay(selectedOrder.value.orderNo)
      showSuccessToast('模拟微信回调完成，支付成功')
    }
    showPayDrawer.value = false
    loadOrders()
  } catch (err) {
    console.error(err)
  } finally {
    paying.value = false
  }
}

// 手动取消订单
const handleCancel = (orderNo) => {
  showConfirmDialog({
    title: '确认取消',
    message: '您确定要取消该笔未支付订单吗？取消后会归还预占的商品库存。'
  }).then(async () => {
    try {
      await cancelOrder(orderNo)
      showSuccessToast('订单已成功取消，库存已归还')
      loadOrders()
    } catch (err) {
      console.error(err)
    }
  }).catch(() => {})
}

// 打开评价弹窗
const openReviewDrawer = (order) => {
  selectedOrder.value = order
  // 默认评价订单中第一个商品
  const item = order.items && order.items.length > 0 ? order.items[0] : null
  if (!item) {
    showFailToast('订单无商品信息')
    return
  }
  reviewForm.value = {
    orderNo: order.orderNo,
    productId: item.productId,
    rating: 5,
    content: ''
  }
  showReviewDrawer.value = true
}

// 提交评价
const submitReview = async () => {
  if (!reviewForm.value.content.trim()) {
    showFailToast('请填写评价内容')
    return
  }
  try {
    await submitReviewApi(reviewForm.value)
    showSuccessToast('评价提交成功！')
    showReviewDrawer.value = false
    loadOrders()
  } catch (err) {
    console.error(err)
  }
}

onMounted(() => {
  loadOrders()
})
</script>
