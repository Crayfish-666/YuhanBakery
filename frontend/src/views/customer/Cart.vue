<template>
  <div class="cart-view px-4 py-2 space-y-4">
    <!-- 头部清空动作 -->
    <div class="flex items-center justify-between mt-1">
      <h1 class="text-xl font-extrabold text-slate-800">购物车</h1>
      <button
        v-if="cartList.length > 0"
        @click="handleClearCart"
        class="text-xs text-slate-400 hover:text-amber-600 flex items-center gap-1"
      >
        <van-icon name="delete-o" />
        <span>清空</span>
      </button>
    </div>

    <!-- 购物车空列表占位 -->
    <div v-if="cartList.length === 0" class="flex flex-col items-center justify-center py-20 space-y-4">
      <div class="w-24 h-24 rounded-full bg-slate-200/50 flex items-center justify-center text-slate-400">
        <van-icon name="shopping-cart-o" size="48" />
      </div>
      <div class="text-center">
        <p class="text-slate-500 font-bold text-sm">购物车空空如也</p>
        <p class="text-slate-400 text-xs mt-1">快去挑几款好吃的蛋糕吧</p>
      </div>
      <button
        @click="router.push('/customer/home')"
        class="group relative overflow-hidden px-6 py-2.5 border-2 border-bakery-burgundy bg-transparent text-bakery-burgundy text-xs font-bold rounded-xl transition-colors duration-300 hover:text-white cursor-pointer"
      >
        <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-bakery-burgundy transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
        <span class="relative z-10 transition-colors duration-50 group-hover:text-white">去大厅选购</span>
      </button>
    </div>

    <!-- 购物车卡片清单 -->
    <div v-else class="space-y-3 pb-24">
      <div
        v-for="item in cartList"
        :key="item.skuId"
        class="glass-panel p-3 rounded-2xl flex gap-3 border border-slate-100 relative shadow-sm"
      >
        <!-- 图片 -->
        <img :src="item.image" class="w-20 h-20 object-cover rounded-xl bg-slate-100" />
        
        <!-- 核心属性 -->
        <div class="flex-grow flex flex-col justify-between py-0.5">
          <div>
            <div class="flex justify-between items-start">
              <h3 class="font-bold text-slate-800 text-sm line-clamp-1 pr-4">{{ item.name }}</h3>
              <button @click="handleDeleteItem(item.skuId)" class="text-slate-300 hover:text-red-500">
                <van-icon name="cross" size="14" />
              </button>
            </div>
            <p class="text-[10px] text-slate-400 mt-0.5">{{ item.specName }}</p>
          </div>
          
          <div class="flex items-center justify-between">
            <span class="text-sm font-black text-amber-600">¥{{ item.price }}</span>
            
            <!-- 库存警告或步进器 -->
            <div v-if="item.status === 0" class="text-[10px] text-red-500 font-semibold bg-red-50 px-2 py-0.5 rounded-full">
              失效下架
            </div>
            <div v-else-if="item.stock === 0" class="text-[10px] text-red-500 font-semibold bg-red-50 px-2 py-0.5 rounded-full">
              抢光了
            </div>
            <van-stepper
              v-else
              v-model="item.quantity"
              :min="1"
              :max="item.stock"
              theme="round"
              button-size="20"
              @change="(qty) => onQuantityChange(item.skuId, qty)"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- 底部固定结算条 -->
    <van-submit-bar
      v-if="cartList.length > 0"
      :price="totalPriceCent"
      button-text="提交订单"
      button-color="linear-gradient(to right, #e07a5f, #f2cc8f)"
      class="border-t border-slate-100 pb-16 bg-white/95 backdrop-blur-md"
      @submit="openCheckoutDrawer"
    />

    <!-- 确认下单配置抽屉 -->
    <van-action-sheet v-model:show="showCheckoutDrawer" title="确认下单信息" class="rounded-t-3xl">
      <div class="p-5 space-y-4">
        <!-- 配送类型切换 -->
        <div>
          <span class="text-xs font-bold text-slate-400 block mb-2">服务方式</span>
          <div class="grid grid-cols-2 gap-2 bg-slate-100 p-1 rounded-xl">
            <button
              @click="checkoutForm.deliveryType = 1"
              :class="checkoutForm.deliveryType === 1 ? 'bg-white text-slate-900 font-bold shadow-sm' : 'text-slate-500'"
              class="py-2 rounded-lg text-xs transition"
            >
              外卖配送
            </button>
            <button
              @click="checkoutForm.deliveryType = 2"
              :class="checkoutForm.deliveryType === 2 ? 'bg-white text-slate-900 font-bold shadow-sm' : 'text-slate-500'"
              class="py-2 rounded-lg text-xs transition"
            >
              到店自提
            </button>
          </div>
        </div>

        <!-- 配送地址簿选择 (外卖配送时必须) -->
        <div v-if="checkoutForm.deliveryType === 1">
          <span class="text-xs font-bold text-slate-400 block mb-2">送达收货地址</span>
          <div v-if="addresses.length === 0" class="text-center py-4 bg-amber-500/10 rounded-xl">
            <p class="text-xs text-amber-700 font-bold">暂无收货地址</p>
            <button @click="router.push('/customer/orders')" class="text-[10px] text-amber-600 underline mt-1 block mx-auto">
              去订单/个人中心配置地址
            </button>
          </div>
          <div v-else class="space-y-2 max-h-36 overflow-y-auto">
            <div
              v-for="addr in addresses"
              :key="addr.id"
              @click="selectAddress(addr)"
              :class="checkoutForm.selectedAddressId === addr.id ? 'border-amber-500 bg-amber-500/5' : 'border-slate-100 bg-slate-50'"
              class="border p-2.5 rounded-xl cursor-pointer text-xs flex justify-between items-center transition"
            >
              <div>
                <p class="font-bold text-slate-700">{{ addr.contactName }} - {{ addr.contactPhone }}</p>
                <p class="text-[10px] text-slate-400 line-clamp-1">{{ addr.addressDetail }}</p>
              </div>
              <van-icon v-if="checkoutForm.selectedAddressId === addr.id" name="success" color="#e07a5f" />
            </div>
          </div>
        </div>

        <!-- 自提提示 -->
        <div v-else class="p-3 bg-slate-100 rounded-xl text-xs text-slate-500 space-y-1">
          <p class="font-bold text-slate-700">自提点：CrayfishBakery（小龙虾烘焙工坊总店）</p>
          <p>地址：杭州市西湖区西湖大道 88 号</p>
          <p>营业时间：周一至周日 09:00 - 21:30</p>
        </div>

        <!-- 优惠券选择 -->
        <div>
          <span class="text-xs font-bold text-slate-400 block mb-2">优惠券抵扣</span>
          <div v-if="coupons.length === 0" class="text-xs text-slate-400 p-3 bg-slate-50 rounded-xl border border-slate-100/50">
            暂无可用优惠券
          </div>
          <div v-else>
            <div 
              @click="showCouponSheet = true" 
              class="border border-slate-100 bg-slate-50 p-3 rounded-xl cursor-pointer text-xs flex justify-between items-center transition hover:bg-slate-100/50"
            >
              <div class="flex items-center gap-2">
                <van-icon name="coupon-o" size="16" class="text-amber-600" />
                <span v-if="selectedCoupon" class="font-bold text-amber-600">
                  {{ selectedCoupon.name }} (-¥{{ calculatedDiscount }})
                </span>
                <span v-else class="text-slate-600 font-medium">请选择优惠券</span>
              </div>
              <div class="flex items-center gap-1 text-[10px] text-slate-400">
                <span>{{ eligibleCoupons.filter(c => c.isEligible).length }}张可用</span>
                <van-icon name="arrow" />
              </div>
            </div>
          </div>
        </div>

        <!-- 联系电话与备注 -->
        <div class="space-y-2">
          <span class="text-xs font-bold text-slate-400 block">补充联系信息</span>
          <input
            v-model="checkoutForm.contactPhone"
            type="tel"
            placeholder="请输入您的手机号"
            class="w-full bg-slate-50 border border-slate-100 rounded-xl px-4 py-2.5 text-xs focus:outline-none focus:border-amber-500 transition"
          />
          <input
            v-model="checkoutForm.remark"
            type="text"
            placeholder="备注：如少糖、多叉子等要求(选填)"
            class="w-full bg-slate-50 border border-slate-100 rounded-xl px-4 py-2.5 text-xs focus:outline-none focus:border-amber-500 transition"
          />
        </div>

        <!-- 提交支付按钮 -->
        <button
          @click="submitCartOrder"
          :disabled="submitting"
          class="group relative overflow-hidden w-full border-2 border-bakery-burgundy bg-transparent text-bakery-burgundy font-bold py-3.5 rounded-xl transition-colors duration-300 hover:text-white text-xs mt-4 cursor-pointer"
        >
          <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-bakery-burgundy transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
          <span v-if="submitting" class="relative z-10 transition-colors duration-50 group-hover:text-white">订单创建中...</span>
          <span v-else class="relative z-10 transition-colors duration-50 group-hover:text-white">立即提交订单并支付</span>
        </button>
      </div>
    </van-action-sheet>

    <!-- 优惠券选择面板 -->
    <van-action-sheet v-model:show="showCouponSheet" title="选择优惠券" class="rounded-t-3xl">
      <div class="p-5 space-y-3 max-h-[60vh] overflow-y-auto">
        <!-- 不使用优惠券 -->
        <div 
          @click="selectCoupon(null)" 
          :class="!selectedCouponId ? 'border-amber-500 bg-amber-50' : 'border-slate-100 bg-slate-50'"
          class="border p-3 rounded-xl cursor-pointer text-xs flex justify-between items-center transition"
        >
          <span class="font-bold text-slate-700">不使用优惠券</span>
          <van-icon v-if="!selectedCouponId" name="success" color="#e07a5f" />
        </div>

        <!-- 优惠券列表 -->
        <div 
          v-for="coupon in eligibleCoupons" 
          :key="coupon.id"
          @click="selectCoupon(coupon)"
          :class="[
            !coupon.isEligible ? 'opacity-50 cursor-not-allowed bg-slate-100 border-dashed border-slate-200' : '',
            selectedCouponId === coupon.id ? 'border-amber-500 bg-amber-500/5' : 'border-slate-100 bg-slate-50'
          ]"
          class="border p-3 rounded-xl cursor-pointer text-xs flex justify-between items-center transition relative overflow-hidden"
        >
          <div class="flex-grow flex items-center gap-3">
            <!-- 减免金额标识 -->
            <div class="flex flex-col items-center justify-center bg-amber-500/10 text-amber-700 font-black rounded-lg w-14 h-14 shrink-0">
              <span class="text-[9px] font-normal" v-if="coupon.type === 2">折扣</span>
              <span class="text-[9px] font-normal" v-else>￥</span>
              <span class="text-base font-black">
                {{ coupon.type === 2 ? (coupon.discountRate * 10).toFixed(1) : parseFloat(coupon.discountAmount) }}
              </span>
              <span class="text-[8px] font-normal" v-if="coupon.type === 2">折</span>
            </div>

            <!-- 详情 -->
            <div class="space-y-0.5">
              <p class="font-bold text-slate-800 text-xs flex items-center gap-1.5">
                {{ coupon.name }}
                <span v-if="!coupon.isEligible" class="text-[8px] font-semibold text-red-500 bg-red-50 px-1.5 py-0.5 rounded">未达门槛</span>
              </p>
              <p class="text-[9px] text-slate-400">
                {{ coupon.minAmount ? `满 ￥${parseFloat(coupon.minAmount)} 可用` : '无门槛' }}
              </p>
              <p v-if="coupon.endTime" class="text-[8px] text-slate-400/80">
                有效期至：{{ coupon.endTime.replace('T', ' ') }}
              </p>
            </div>
          </div>

          <!-- 选中成功标志 -->
          <van-icon v-if="selectedCouponId === coupon.id" name="success" color="#e07a5f" />
        </div>
      </div>
    </van-action-sheet>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, reactive } from 'vue'
import { useRouter } from 'vue-router'
import {
  Icon as VanIcon, Stepper as VanStepper, SubmitBar as VanSubmitBar,
  ActionSheet as VanActionSheet, showSuccessToast, showFailToast, showConfirmDialog
} from 'vant'
import { listCart, updateCartQuantity, deleteCartItem, clearCart, getMyCoupons } from '@/api/mall'
import { listAddresses } from '@/api/user'
import { submitOrder } from '@/api/order'
import 'vant/es/toast/style'

const router = useRouter()
const cartList = ref([])
const addresses = ref([])
const showCheckoutDrawer = ref(false)
const submitting = ref(false)

// 优惠券相关状态
const coupons = ref([])
const selectedCouponId = ref(null)
const showCouponSheet = ref(false)

const checkoutForm = reactive({
  deliveryType: 1, // 1-外卖，2-自提
  selectedAddressId: null,
  contactPhone: '',
  addressSnapshot: '',
  remark: ''
})

// 加载购物车数据
const loadCart = async () => {
  try {
    const res = await listCart()
    cartList.value = res.data
  } catch (err) {
    console.error(err)
  }
}

// 数量变更同步
const onQuantityChange = async (skuId, quantity) => {
  try {
    await updateCartQuantity(skuId, quantity)
    if (window.refreshCartBadge) {
      window.refreshCartBadge()
    }
  } catch (err) {
    console.error(err)
  }
}

// 移除购物车单品
const handleDeleteItem = async (skuId) => {
  showConfirmDialog({
    title: '温馨提示',
    message: '确认从购物车移除此规格商品吗？'
  }).then(async () => {
    try {
      await deleteCartItem(skuId)
      showSuccessToast('移除成功')
      loadCart()
      if (window.refreshCartBadge) {
        window.refreshCartBadge()
      }
    } catch (err) {
      console.error(err)
    }
  }).catch(() => {})
}

// 清空购物车
const handleClearCart = () => {
  showConfirmDialog({
    title: '重要确认',
    message: '您确定要清空购物车里的全部商品吗？'
  }).then(async () => {
    try {
      await clearCart()
      showSuccessToast('购物车已清空')
      loadCart()
      if (window.refreshCartBadge) {
        window.refreshCartBadge()
      }
    } catch (err) {
      console.error(err)
    }
  }).catch(() => {})
}

// 计算选中商品总分分值（未计算优惠券前的原价，Vant 接收分）
const originalPriceCent = computed(() => {
  const sum = cartList.value.reduce((total, item) => {
    if (item.status === 1 && item.stock > 0) {
      return total + parseFloat(item.price) * item.quantity
    }
    return total
  }, 0)
  return Math.round(sum * 100)
})

// 计算可用且符合门槛的优惠券
const eligibleCoupons = computed(() => {
  const originalAmount = originalPriceCent.value / 100
  return coupons.value.map(coupon => {
    const isEligible = !coupon.minAmount || originalAmount >= parseFloat(coupon.minAmount)
    return {
      ...coupon,
      isEligible
    }
  })
})

const selectedCoupon = computed(() => {
  return coupons.value.find(c => c.id === selectedCouponId.value)
})

const selectCoupon = (coupon) => {
  if (coupon && !coupon.isEligible) {
    showFailToast('未达到该优惠券的使用门槛')
    return
  }
  selectedCouponId.value = coupon ? coupon.id : null
  showCouponSheet.value = false
}

// 计算抵扣后的最终价格（分）
const totalPriceCent = computed(() => {
  const original = originalPriceCent.value
  if (!selectedCoupon.value) {
    return original
  }
  
  const coupon = selectedCoupon.value
  let finalCent = original
  const originalYuan = original / 100

  if (coupon.type === 1 || coupon.type === 3) { // 满减 或 无门槛
    const discountYuan = parseFloat(coupon.discountAmount)
    finalCent = Math.max(0, Math.round((originalYuan - discountYuan) * 100))
  } else if (coupon.type === 2) { // 折扣
    const rate = parseFloat(coupon.discountRate)
    finalCent = Math.max(0, Math.round(originalYuan * rate * 100))
  }
  
  return finalCent
})

// 计算抵扣金额（元）
const calculatedDiscount = computed(() => {
  if (!selectedCoupon.value) return '0.00'
  const original = originalPriceCent.value
  const final = totalPriceCent.value
  return ((original - final) / 100).toFixed(2)
})

// 开启下单抽屉并拉取地址、可用优惠券
const openCheckoutDrawer = async () => {
  // 检查是否有失效或零库存商品
  const hasInvalid = cartList.value.some(i => i.status === 0 || i.stock === 0)
  if (hasInvalid) {
    return showFailToast('购物车中含有失效或无货商品，请先清理')
  }

  showCheckoutDrawer.value = true
  
  // 并行拉取地址与优惠券
  try {
    const res = await listAddresses()
    addresses.value = res.data
    const def = addresses.value.find(a => a.isDefault === 1)
    if (def) {
      checkoutForm.selectedAddressId = def.id
      checkoutForm.contactPhone = def.contactPhone
      checkoutForm.addressSnapshot = JSON.stringify({
        contactName: def.contactName,
        contactPhone: def.contactPhone,
        addressDetail: def.addressDetail
      })
    }
  } catch (err) {
    console.error('加载地址失败', err)
  }

  try {
    const couponRes = await getMyCoupons()
    coupons.value = couponRes.data || []
    selectedCouponId.value = null // 默认重置
  } catch (err) {
    console.error('加载优惠券失败', err)
  }
}

const selectAddress = (addr) => {
  checkoutForm.selectedAddressId = addr.id
  checkoutForm.contactPhone = addr.contactPhone
  checkoutForm.addressSnapshot = JSON.stringify({
    contactName: addr.contactName,
    contactPhone: addr.contactPhone,
    addressDetail: addr.addressDetail
  })
}

// 提交订单并去往订单详情页
const submitCartOrder = async () => {
  if (checkoutForm.deliveryType === 1 && !checkoutForm.selectedAddressId) {
    return showFailToast('请点选一个送货地址')
  }
  if (!checkoutForm.contactPhone.trim()) {
    return showFailToast('请输入主要联系人电话')
  }

  submitting.value = true
  try {
    const submitDto = {
      deliveryType: checkoutForm.deliveryType,
      contactPhone: checkoutForm.contactPhone,
      addressSnapshot: checkoutForm.deliveryType === 1 ? checkoutForm.addressSnapshot : '自提自取',
      remark: checkoutForm.remark,
      couponId: selectedCouponId.value || null
    }
    const res = await submitOrder(submitDto)
    showSuccessToast('下单成功！')
    showCheckoutDrawer.value = false
    
    // 更新角标
    if (window.refreshCartBadge) {
      window.refreshCartBadge()
    }
    
    // 跳转到订单详情页面而非列表页
    const orderNo = res.data
    router.push(`/customer/order/${orderNo}`)
  } catch (err) {
    console.error(err)
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadCart()
})
</script>

<style scoped>
:deep(.van-submit-bar) {
  bottom: 50px !important;
}
</style>
