<template>
  <div class="order-detail-view max-w-xl mx-auto px-4 pt-4 pb-24 space-y-4">
    <!-- 头部导航 -->
    <div class="flex items-center gap-2 mt-1">
      <button @click="goBack" class="p-1 rounded-full hover:bg-slate-100 transition text-slate-600">
        <van-icon name="arrow-left" size="18" />
      </button>
      <h1 class="text-lg font-extrabold text-slate-800">订单详情</h1>
    </div>

    <!-- 加载中 -->
    <div v-if="loading" class="flex flex-col items-center justify-center py-20 space-y-3">
      <van-loading color="#e07a5f" size="24px">加载订单信息...</van-loading>
    </div>

    <!-- 订单不存在/失败 -->
    <div v-else-if="!order" class="flex flex-col items-center justify-center py-20 space-y-4">
      <van-icon name="warning-o" size="48" class="text-slate-300" />
      <p class="text-slate-500 font-bold text-sm">未能加载订单详情</p>
      <button @click="goBack" class="px-5 py-2 bg-slate-100 hover:bg-slate-200 text-slate-600 text-xs font-bold rounded-xl transition">
        返回列表
      </button>
    </div>

    <!-- 详情主内容 -->
    <div v-else class="space-y-4">
      <!-- 订单状态卡片 -->
      <div class="glass-panel p-5 rounded-2xl border border-slate-100 shadow-sm bg-gradient-to-br from-white to-slate-50/50 flex items-center justify-between">
        <div class="space-y-1">
          <p class="text-[10px] text-slate-400 font-medium">订单状态</p>
          <h2 :class="getStatusClass(order.status)" class="text-lg font-black flex items-center gap-1.5">
            <span class="inline-block w-2.5 h-2.5 rounded-full" :class="getStatusDotClass(order.status)"></span>
            {{ getStatusText(order.status) }}
          </h2>
          <p class="text-[10px] text-slate-400">下单时间: {{ formatTime(order.createTime) }}</p>
        </div>
        <van-icon :name="getStatusIcon(order.status)" size="40" class="opacity-80" :class="getStatusColorClass(order.status)" />
      </div>

      <!-- 配送信息卡片 -->
      <div class="glass-panel p-4 rounded-2xl border border-slate-100 shadow-sm space-y-3">
        <h3 class="text-xs font-bold text-slate-800 flex items-center gap-1.5 border-b border-slate-100 pb-2">
          <van-icon name="location-o" size="14" class="text-amber-600" />
          <span>服务与配送信息</span>
        </h3>
        <div class="text-xs space-y-2.5">
          <div class="flex justify-between">
            <span class="text-slate-400">服务方式</span>
            <span class="font-bold text-slate-700">{{ order.deliveryType === 1 ? '外卖配送' : '到店自提' }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-slate-400">联系电话</span>
            <span class="font-bold text-slate-700">{{ order.contactPhone }}</span>
          </div>
          <div v-if="order.deliveryType === 1" class="flex flex-col gap-1">
            <span class="text-slate-400">送达地址</span>
            <div class="p-2.5 bg-slate-50 rounded-xl border border-slate-100 text-slate-600 leading-relaxed">
              <p class="font-bold text-slate-700">{{ parsedAddress.contactName }} ({{ parsedAddress.contactPhone }})</p>
              <p class="mt-0.5 text-slate-500">{{ parsedAddress.addressDetail }}</p>
            </div>
          </div>
          <div v-else class="p-2.5 bg-slate-50 rounded-xl border border-slate-100 text-slate-600 space-y-0.5">
            <p class="font-bold text-slate-700">自提点：CrayfishBakery（总店）</p>
            <p>自提地址：杭州市西湖区西湖大道 88 号</p>
            <p class="text-[10px] text-slate-400">营业时间：周一至周日 09:00 - 21:30</p>
          </div>
          <div v-if="order.remark" class="flex justify-between border-t border-dashed border-slate-100 pt-2">
            <span class="text-slate-400 shrink-0">备注说明</span>
            <span class="text-slate-600 italic text-right pl-4">{{ order.remark }}</span>
          </div>
        </div>
      </div>

      <!-- 商品清单卡片 -->
      <div class="glass-panel p-4 rounded-2xl border border-slate-100 shadow-sm space-y-3">
        <h3 class="text-xs font-bold text-slate-800 flex items-center gap-1.5 border-b border-slate-100 pb-2">
          <van-icon name="shopping-cart-o" size="14" class="text-amber-600" />
          <span>商品清单</span>
        </h3>
        <div class="divide-y divide-slate-50">
          <div v-for="item in order.items" :key="item.id" 
               class="flex gap-3 py-3 first:pt-0 last:pb-0 cursor-pointer hover:bg-slate-50/50 transition px-1 rounded-xl"
               @click="router.push(`/customer/product/${item.productId}`)">
            <img :src="item.productImage" class="w-14 h-14 object-cover rounded-xl bg-slate-50 border border-slate-100" />
            <div class="flex-grow flex flex-col justify-between py-0.5">
              <div>
                <h4 class="text-xs font-bold text-slate-800 line-clamp-1">{{ item.productName }}</h4>
                <p class="text-[10px] text-slate-400 mt-0.5">{{ item.specName }}</p>
              </div>
              <div class="flex justify-between text-xs">
                <span class="text-amber-600 font-bold">¥{{ item.price }}</span>
                <span class="text-slate-400">x{{ item.quantity }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 金额明细及支付状态卡片 -->
      <div class="glass-panel p-4 rounded-2xl border border-slate-100 shadow-sm space-y-3">
        <h3 class="text-xs font-bold text-slate-800 flex items-center gap-1.5 border-b border-slate-100 pb-2">
          <van-icon name="gold-coin-o" size="14" class="text-amber-600" />
          <span>金额明细</span>
        </h3>
        <div class="text-xs space-y-2 pb-2 border-b border-slate-100">
          <div class="flex justify-between">
            <span class="text-slate-400">商品原价</span>
            <span class="text-slate-700">¥{{ originalPrice }}</span>
          </div>
          <div class="flex justify-between" v-if="discountAmount > 0">
            <span class="text-slate-400">优惠抵扣</span>
            <span class="text-emerald-600 font-medium">-¥{{ discountAmount }}</span>
          </div>
          <div class="flex justify-between text-sm pt-1">
            <span class="font-bold text-slate-800">实付总额</span>
            <span class="font-black text-amber-600">¥{{ order.totalAmount }}</span>
          </div>
        </div>
        <div class="text-[10px] text-slate-400 space-y-1 pt-1">
          <p>订单单号: {{ order.orderNo }}</p>
          <p v-if="order.createTime">下单时间: {{ formatTime(order.createTime) }}</p>
          <p v-if="order.payTime">支付时间: {{ formatTime(order.payTime) }}</p>
          <p v-if="order.payMethod">支付方式: {{ getPayMethodText(order.payMethod) }}</p>
        </div>
      </div>
    </div>

    <!-- 底部悬浮控制条 (仅待支付状态下展示) -->
    <div v-if="order && order.status === 0" class="fixed bottom-0 left-0 right-0 max-w-xl mx-auto p-4 bg-white/95 backdrop-blur-md border-t border-slate-100 shadow-lg flex items-center gap-3 z-50">
      <button
        @click="handleCancel"
        class="w-1/3 py-3 border border-slate-200 text-slate-500 font-bold rounded-xl text-xs bg-transparent transition hover:bg-slate-50 active:scale-95"
      >
        取消订单
      </button>
      <button
        @click="openPayDrawer"
        class="w-2/3 py-3 bg-bakery-burgundy text-white font-bold rounded-xl text-xs transition hover:bg-bakery-burgundy/90 active:scale-95 shadow-md shadow-bakery-burgundy/20"
      >
        立即支付 (¥{{ order.totalAmount }})
      </button>
    </div>

    <!-- 收银台支付抽屉 -->
    <van-action-sheet v-model:show="showPayDrawer" title="收银台" class="rounded-t-3xl">
      <div v-if="order" class="p-5 space-y-5">
        <div class="text-center space-y-1">
          <p class="text-xs text-slate-400">应付金额</p>
          <h2 class="text-3xl font-black text-slate-800">¥{{ order.totalAmount }}</h2>
          <p class="text-[10px] text-slate-400 mt-1">单号: {{ order.orderNo }}</p>
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
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { 
  Icon as VanIcon, Loading as VanLoading, ActionSheet as VanActionSheet, 
  showSuccessToast, showFailToast, showConfirmDialog 
} from 'vant'
import { getOrderDetailByNo, payWithWallet, mockWeChatPay, cancelOrder } from '@/api/order'
import { getUserInfo } from '@/api/user'
import 'vant/es/toast/style'

const route = useRoute()
const router = useRouter()
const orderNo = route.params.orderNo

const loading = ref(true)
const order = ref(null)
const showPayDrawer = ref(false)
const activePayMethod = ref(1) // 1-余额，2-微信
const userBalance = ref('0.00')
const paying = ref(false)

// 地址快照解析
const parsedAddress = computed(() => {
  if (!order.value || !order.value.addressSnapshot) return {}
  try {
    return JSON.parse(order.value.addressSnapshot)
  } catch (e) {
    // 兼容非 JSON 格式
    return { addressDetail: order.value.addressSnapshot }
  }
})

// 计算商品总原价
const originalPrice = computed(() => {
  if (!order.value || !order.value.items) return '0.00'
  const total = order.value.items.reduce((sum, item) => sum + parseFloat(item.price) * item.quantity, 0)
  return total.toFixed(2)
})

// 计算优惠金额
const discountAmount = computed(() => {
  if (!order.value) return 0
  const orig = parseFloat(originalPrice.value)
  const paid = parseFloat(order.value.totalAmount)
  const diff = orig - paid
  return diff > 0 ? diff.toFixed(2) : 0
})

const loadOrderDetail = async () => {
  loading.value = true
  try {
    const res = await getOrderDetailByNo(orderNo)
    if (res.code === 200) {
      order.value = res.data
    } else {
      showFailToast(res.message || '订单获取失败')
    }
  } catch (err) {
    console.error(err)
    showFailToast('获取订单明细失败')
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.push('/customer/orders')
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
    case 0: return 'text-amber-600'
    case 1: return 'text-sky-600'
    case 2: return 'text-blue-600'
    case 3: return 'text-emerald-600'
    case 4: return 'text-slate-400'
    default: return 'text-slate-400'
  }
}

const getStatusDotClass = (status) => {
  switch (status) {
    case 0: return 'bg-amber-500 animate-pulse'
    case 1: return 'bg-sky-500'
    case 2: return 'bg-blue-500'
    case 3: return 'bg-emerald-500'
    case 4: return 'bg-slate-400'
    default: return 'bg-slate-400'
  }
}

const getStatusColorClass = (status) => {
  switch (status) {
    case 0: return 'text-amber-500'
    case 1: return 'text-sky-500'
    case 2: return 'text-blue-500'
    case 3: return 'text-emerald-500'
    case 4: return 'text-slate-400'
    default: return 'text-slate-400'
  }
}

const getStatusIcon = (status) => {
  switch (status) {
    case 0: return 'todo-list-o'
    case 1: return 'clock-o'
    case 2: return 'logistics'
    case 3: return 'passed'
    case 4: return 'close'
    default: return 'info-o'
  }
}

const getPayMethodText = (method) => {
  switch (method) {
    case 1: return '钱包余额支付'
    case 2: return '微信支付'
    default: return '未知支付方式'
  }
}

const formatTime = (timeStr) => {
  if (!timeStr) return ''
  return timeStr.replace('T', ' ')
}

const openPayDrawer = async () => {
  activePayMethod.value = 1
  showPayDrawer.value = true

  // 获取可用余额
  try {
    const res = await getUserInfo()
    userBalance.value = res.data.balance
  } catch (err) {
    console.error(err)
  }
}

// 模拟支付结算
const handlePaySubmit = async () => {
  if (!order.value) return

  paying.value = true
  try {
    if (activePayMethod.value === 1) {
      // 钱包支付
      await payWithWallet(order.value.orderNo)
      showSuccessToast('余额扣减成功，支付成功')
    } else {
      // 微信支付 (本地沙箱)
      await mockWeChatPay(order.value.orderNo)
      showSuccessToast('模拟微信回调完成，支付成功')
    }
    showPayDrawer.value = false
    loadOrderDetail() // 支付成功后重新加载刷新订单状态
  } catch (err) {
    console.error(err)
  } finally {
    paying.value = false
  }
}

// 手动取消订单
const handleCancel = () => {
  showConfirmDialog({
    title: '确认取消',
    message: '您确定要取消该笔未支付订单吗？取消后会退回优惠券并归还商品库存。'
  }).then(async () => {
    try {
      await cancelOrder(orderNo)
      showSuccessToast('订单已成功取消')
      loadOrderDetail() // 刷新状态
    } catch (err) {
      console.error(err)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadOrderDetail()
})
</script>

<style scoped>
.order-detail-view {
  background-color: #fafbfc;
}
.glass-panel {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(12px);
}
</style>
