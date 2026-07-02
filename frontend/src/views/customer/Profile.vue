<template>
  <div class="profile-view px-4 py-2 space-y-4">
    <!-- 个人中心顶部名片 -->
    <div class="glass-panel p-5 rounded-3xl border border-slate-100 flex items-center justify-between shadow-sm">
      <div class="flex items-center gap-3">
        <div class="w-12 h-12 rounded-full bg-gradient-to-tr from-amber-500 to-orange-500 flex items-center justify-center text-white font-black text-xl shadow">
          {{ username.charAt(0).toUpperCase() }}
        </div>
        <div>
          <h2 class="font-bold text-slate-800 text-sm">{{ username }}</h2>
          <p class="text-[10px] text-slate-400 mt-0.5">会员身份：顾客 (C端)</p>
        </div>
      </div>
      <button @click="handleLogout" class="text-xs text-slate-400 hover:text-red-500 flex items-center gap-1">
        <van-icon name="log-out" />
        <span>退出</span>
      </button>
    </div>

    <!-- 钱包中心卡片 -->
    <div class="bg-gradient-to-br from-slate-900 to-slate-850 p-5 rounded-3xl shadow-xl text-white relative overflow-hidden">
      <!-- 饰图 -->
      <div class="absolute w-24 h-24 rounded-full bg-amber-500/10 -bottom-8 -right-8"></div>
      <div class="space-y-4">
        <div class="flex justify-between items-center">
          <span class="text-xs text-slate-400 font-semibold tracking-wider">我的烘焙钱包 (余额)</span>
          <button
            @click="showRechargePopup = true"
            class="group relative overflow-hidden px-3.5 py-1.5 border border-amber-500 bg-transparent text-amber-500 font-bold text-xs rounded-xl transition-colors duration-300 hover:text-slate-950 cursor-pointer"
          >
            <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-amber-500 transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
            <span class="relative z-10 transition-colors duration-50 group-hover:text-slate-950">账户充值</span>
          </button>
        </div>
        <div class="space-y-1">
          <h1 class="text-3xl font-black">¥ {{ balance }}</h1>
          <p class="text-[10px] text-slate-500">累计充值：¥{{ totalRecharge }}</p>
        </div>
      </div>
    </div>

    <!-- 快捷功能分区 Tab -->
    <van-tabs v-model:active="activeTab" color="#e07a5f" class="bg-transparent">
      <!-- 地址簿管理 -->
      <van-tab title="收货地址">
        <div class="space-y-3 mt-3">
          <div class="flex justify-between items-center">
            <span class="text-xs font-bold text-slate-500">我的收货地址</span>
            <button
              @click="openAddressForm(null)"
              class="text-xs text-amber-600 font-bold flex items-center gap-0.5"
            >
              <van-icon name="plus" />
              <span>新增地址</span>
            </button>
          </div>

          <!-- 地址列表 -->
          <div v-if="addresses.length === 0" class="text-center py-10 text-xs text-slate-400 bg-white rounded-2xl border border-slate-100">
            暂无收货地址，点击右上方新增
          </div>
          <div v-else class="space-y-2.5">
            <div
              v-for="addr in addresses"
              :key="addr.id"
              class="glass-panel p-3.5 rounded-2xl border border-slate-100 flex justify-between items-start shadow-sm"
            >
              <div class="space-y-1.5 flex-grow pr-4">
                <div class="flex items-center gap-2">
                  <span class="font-bold text-slate-800 text-xs">{{ addr.contactName }}</span>
                  <span class="text-slate-500 text-[10px]">{{ addr.contactPhone }}</span>
                  <span
                    v-if="addr.isDefault === 1"
                    class="text-[8px] bg-amber-500/10 text-amber-600 px-1.5 py-0.5 rounded-full font-black"
                  >
                    默认
                  </span>
                </div>
                <p class="text-[10px] text-slate-400 leading-relaxed">{{ addr.addressDetail }}</p>
                <button
                  v-if="addr.isDefault === 0"
                  @click="handleSetDefault(addr.id)"
                  class="text-[9px] text-slate-400 hover:text-amber-600 underline block"
                >
                  设为默认
                </button>
              </div>
              <div class="flex gap-2">
                <button @click="openAddressForm(addr)" class="text-slate-300 hover:text-amber-500 text-xs">
                  <van-icon name="edit" />
                </button>
                <button @click="handleDeleteAddress(addr.id)" class="text-slate-300 hover:text-red-500 text-xs">
                  <van-icon name="delete" />
                </button>
              </div>
            </div>
          </div>
        </div>
      </van-tab>

      <!-- 资金流水日志 -->
      <van-tab title="账目明细">
        <div class="space-y-3 mt-3">
          <span class="text-xs font-bold text-slate-500 block">资金变动流水日志</span>
          <div v-if="balanceLogs.length === 0" class="text-center py-10 text-xs text-slate-400 bg-white rounded-2xl border border-slate-100">
            暂无账目明细记录
          </div>
          <div v-else class="space-y-2.5">
            <div
              v-for="log in balanceLogs"
              :key="log.id"
              class="glass-panel p-3 rounded-2xl border border-slate-100 flex justify-between items-center shadow-sm text-xs"
            >
              <div>
                <p class="font-bold text-slate-700">{{ getLogTypeText(log.type) }}</p>
                <p class="text-[9px] text-slate-400 mt-0.5">{{ log.createTime }}</p>
                <p v-if="log.orderNo" class="text-[9px] text-slate-400">单号: {{ log.orderNo }}</p>
              </div>
              <span :class="log.amount > 0 ? 'text-emerald-500 font-bold' : 'text-slate-800 font-bold'">
                {{ log.amount > 0 ? '+' : '' }}{{ log.amount }}
              </span>
            </div>
          </div>
        </div>
      </van-tab>
    </van-tabs>

    <!-- 充值弹窗 -->
    <van-action-sheet v-model:show="showRechargePopup" title="钱包充值" class="rounded-t-3xl">
      <div class="p-5 space-y-5">
        <div class="grid grid-cols-3 gap-2">
          <button
            v-for="amt in [50, 100, 200, 500, 1000]"
            :key="amt"
            @click="rechargeAmount = amt"
            :class="rechargeAmount === amt ? 'bg-amber-500 text-slate-950 border-amber-500 font-bold' : 'bg-slate-100 text-slate-600 border-transparent'"
            class="py-3 border text-xs rounded-xl transition text-center font-medium"
          >
            充 ¥{{ amt }}
          </button>
        </div>
        <div class="space-y-1">
          <span class="text-xs font-bold text-slate-500">自定义充值金额 (元)</span>
          <input
            v-model="rechargeAmount"
            type="number"
            placeholder="自定义输入面额"
            class="w-full bg-slate-100 border border-slate-100 rounded-xl px-4 py-3 text-xs focus:outline-none focus:border-amber-500"
          />
        </div>
        <button
          @click="submitRecharge"
          :disabled="recharging"
          class="group relative overflow-hidden w-full border-2 border-bakery-burgundy bg-transparent text-bakery-burgundy font-bold py-3.5 rounded-xl transition-colors duration-300 hover:text-white text-xs mt-2 cursor-pointer"
        >
          <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-bakery-burgundy transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
          <span v-if="recharging" class="relative z-10 transition-colors duration-50 group-hover:text-white">安全充值处理中...</span>
          <span v-else class="relative z-10 transition-colors duration-50 group-hover:text-white">立即充值</span>
        </button>
      </div>
    </van-action-sheet>

    <!-- 地址表单弹窗 -->
    <van-action-sheet v-model:show="showAddressForm" :title="addressForm.id ? '编辑地址' : '新增地址'" class="rounded-t-3xl">
      <div class="p-5 space-y-4">
        <div>
          <label class="block text-slate-500 text-xs font-bold mb-1 ml-1">联系人姓名</label>
          <input
            v-model="addressForm.contactName"
            type="text"
            placeholder="请输入联系人姓名"
            class="w-full bg-slate-50 border border-slate-100 rounded-xl px-4 py-2.5 text-xs focus:outline-none"
          />
        </div>
        <div>
          <label class="block text-slate-500 text-xs font-bold mb-1 ml-1">联系电话</label>
          <input
            v-model="addressForm.contactPhone"
            type="tel"
            placeholder="请输入手机号"
            class="w-full bg-slate-50 border border-slate-100 rounded-xl px-4 py-2.5 text-xs focus:outline-none"
          />
        </div>
        <div>
          <label class="block text-slate-500 text-xs font-bold mb-1 ml-1">收货详细地址</label>
          <input
            v-model="addressForm.addressDetail"
            type="text"
            placeholder="请输入省市区及详细门牌号"
            class="w-full bg-slate-50 border border-slate-100 rounded-xl px-4 py-2.5 text-xs focus:outline-none"
          />
        </div>
        <div class="flex items-center justify-between py-2">
          <span class="text-xs font-bold text-slate-500">设为默认收货地址</span>
          <van-switch v-model="addressForm.isDefault" active-color="#e07a5f" :active-value="1" :inactive-value="0" />
        </div>
        <button
          @click="submitAddress"
          class="group relative overflow-hidden w-full border-2 border-bakery-burgundy bg-transparent text-bakery-burgundy font-bold py-3.5 rounded-xl transition-colors duration-300 hover:text-white text-xs mt-3 cursor-pointer"
        >
          <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-bakery-burgundy transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
          <span class="relative z-10 transition-colors duration-50 group-hover:text-white">保存并使用</span>
        </button>
      </div>
    </van-action-sheet>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/modules/user'
import {
  Icon as VanIcon, Tabs as VanTabs, Tab as VanTab, ActionSheet as VanActionSheet,
  Switch as VanSwitch, showSuccessToast, showFailToast, showConfirmDialog
} from 'vant'
import { getUserInfo, topup, listAddresses, saveAddress, setDefaultAddress, deleteAddress, listBalanceLogs } from '@/api/user'
import 'vant/es/toast/style'

const router = useRouter()
const userStore = useUserStore()

const username = ref('Customer')
const balance = ref('0.00')
const totalRecharge = ref('0.00')
const activeTab = ref(0)

// 充值管理
const showRechargePopup = ref(false)
const rechargeAmount = ref(100)
const recharging = ref(false)

// 地址管理
const addresses = ref([])
const showAddressForm = ref(false)
const addressForm = reactive({
  id: null,
  contactName: '',
  contactPhone: '',
  addressDetail: '',
  isDefault: 0
})

// 资金变动流水
const balanceLogs = ref([])

// 加载核心用户信息
const loadUserInfo = async () => {
  try {
    const res = await getUserInfo()
    username.value = res.data.username
    balance.value = res.data.balance
    totalRecharge.value = res.data.totalRecharge
  } catch (err) {
    console.error(err)
  }
}

// 加载地址列表
const loadAddresses = async () => {
  try {
    const res = await listAddresses()
    addresses.value = res.data
  } catch (err) {
    console.error(err)
  }
}

// 加载资金变动日志
const loadBalanceLogs = async () => {
  try {
    const res = await listBalanceLogs({ pageNum: 1, pageSize: 50 })
    balanceLogs.value = res.data.records
  } catch (err) {
    console.error(err)
  }
}

// 退出登录
const handleLogout = () => {
  showConfirmDialog({
    title: '温馨提示',
    message: '确认安全退出登录吗？'
  }).then(() => {
    userStore.logout()
    router.push('/customer/home')
  }).catch(() => {})
}

// 充值充值
const submitRecharge = async () => {
  if (rechargeAmount.value <= 0) {
    return showFailToast('金额必须大于零')
  }
  recharging.value = true
  try {
    await topup(rechargeAmount.value)
    showSuccessToast('充值成功')
    showRechargePopup.value = false
    loadUserInfo()
    loadBalanceLogs()
  } catch (err) {
    console.error(err)
  } finally {
    recharging.value = false
  }
}

// 打开新增/编辑地址面板
const openAddressForm = (addr) => {
  if (addr) {
    addressForm.id = addr.id
    addressForm.contactName = addr.contactName
    addressForm.contactPhone = addr.contactPhone
    addressForm.addressDetail = addr.addressDetail
    addressForm.isDefault = addr.isDefault
  } else {
    addressForm.id = null
    addressForm.contactName = ''
    addressForm.contactPhone = ''
    addressForm.addressDetail = ''
    addressForm.isDefault = 0
  }
  showAddressForm.value = true
}

const submitAddress = async () => {
  if (!addressForm.contactName.trim()) return showFailToast('姓名不能为空')
  if (!addressForm.contactPhone.trim()) return showFailToast('电话不能为空')
  if (!addressForm.addressDetail.trim()) return showFailToast('地址不能空')

  try {
    await saveAddress(addressForm)
    showSuccessToast('地址保存成功')
    showAddressForm.value = false
    loadAddresses()
  } catch (err) {
    console.error(err)
  }
}

const handleSetDefault = async (id) => {
  try {
    await setDefaultAddress(id)
    showSuccessToast('默认地址配置成功')
    loadAddresses()
  } catch (err) {
    console.error(err)
  }
}

const handleDeleteAddress = (id) => {
  showConfirmDialog({
    title: '温馨提示',
    message: '您确定要删除该收货地址吗？'
  }).then(async () => {
    try {
      await deleteAddress(id)
      showSuccessToast('删除成功')
      loadAddresses()
    } catch (err) {
      console.error(err)
    }
  }).catch(() => {})
}

const getLogTypeText = (type) => {
  switch (type) {
    case 1: return '钱包充值'
    case 2: return '商城购买支出'
    case 3: return '订单售后退款'
    default: return '账目调整'
  }
}

onMounted(() => {
  loadUserInfo()
  loadAddresses()
  loadBalanceLogs()
})
</script>
