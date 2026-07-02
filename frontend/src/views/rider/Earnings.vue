<template>
  <div class="rider-earnings w-full min-h-[100vh] bg-rider-dark pb-24">
    <!-- Top Status Bar -->
    <div class="sticky top-0 z-40 bg-rider-dark/90 backdrop-blur-xl border-b border-[#333333] px-4 py-4 flex justify-between items-center">
      <h1 class="text-white text-2xl font-black tracking-wide">收益与提现</h1>
      <button 
        @click="showWithdraw = true"
        class="bg-[#00FF66] text-black text-sm font-black px-4 py-1.5 rounded-full active:scale-95 transition-transform shadow-[0_0_10px_rgba(0,255,102,0.4)]"
      >
        申请提现
      </button>
    </div>

    <div class="px-4 mt-6 space-y-6">
      <!-- Balance Card -->
      <div class="bg-rider-surface rounded-[16px] p-6 relative overflow-hidden">
        <p class="text-[#8E8E93] text-sm font-bold tracking-wider mb-2">可提现余额 (元)</p>
        <h1 class="text-white text-[48px] font-mono-num font-black leading-none tracking-tight">
          {{ userInfo?.balance ? Number(userInfo.balance).toFixed(2) : '0.00' }}
        </h1>
        
        <div class="h-[1px] w-full bg-[#333333] my-5"></div>
        
        <div class="flex gap-6">
          <div>
            <p class="text-[#8E8E93] text-xs font-bold mb-1">今日跑单 (笔)</p>
            <p class="text-white text-xl font-mono-num font-bold">{{ todayOrders }}</p>
          </div>
          <div>
            <p class="text-[#8E8E93] text-xs font-bold mb-1">历史总充值</p>
            <p class="text-white text-xl font-mono-num font-bold">{{ userInfo?.totalRecharge ? Number(userInfo.totalRecharge).toFixed(2) : '0.00' }}</p>
          </div>
        </div>
      </div>

      <!-- Balance Logs -->
      <div class="space-y-4">
        <h3 class="text-[#8E8E93] text-xs font-bold tracking-widest uppercase">资金变动流水</h3>
        
        <div v-if="balanceLogs.length === 0" class="text-center py-10">
          <van-icon name="balance-list-o" size="48" color="#333333" />
          <p class="text-[#8E8E93] text-sm mt-3 font-bold">暂无资金流转记录</p>
        </div>

        <div class="space-y-3" v-else>
          <div 
            v-for="log in balanceLogs" :key="log.id"
            class="bg-rider-surface rounded-[12px] p-4 flex justify-between items-center"
          >
            <div class="flex items-center gap-3">
              <div 
                class="w-10 h-10 rounded-full flex items-center justify-center font-black"
                :class="getTypeColor(log.type, log.amount)"
              >
                {{ getTypeLabel(log.type) }}
              </div>
              <div>
                <p class="text-white text-[16px] font-bold">{{ getTypeName(log.type) }}</p>
                <p class="text-[#8E8E93] text-[12px] mt-0.5 font-mono-num">{{ log.createTime }}</p>
              </div>
            </div>
            <div class="text-right">
              <p 
                class="text-[20px] font-mono-num font-black"
                :class="log.amount > 0 ? 'text-[#00FF66]' : 'text-white'"
              >
                {{ log.amount > 0 ? '+' : '' }}{{ Number(log.amount).toFixed(2) }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 提现弹窗 -->
    <van-dialog 
      v-model:show="showWithdraw" 
      title="提现申请" 
      show-cancel-button 
      class="dark-dialog"
      confirm-button-text="确认提现"
      confirm-button-color="#00FF66"
      @confirm="handleWithdraw"
    >
      <div class="p-6">
        <p class="text-[#8E8E93] text-sm mb-4">输入提现金额 (元)，最高可提现 {{ userInfo?.balance || 0 }} 元</p>
        <input 
          v-model="withdrawAmount" 
          type="number" 
          placeholder="0.00"
          class="w-full bg-[#0A0A0A] text-white border border-[#333333] rounded-[8px] px-4 py-3 text-[24px] font-mono-num font-black outline-none focus:border-[#00FF66] transition-colors"
        />
      </div>
    </van-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Icon as VanIcon, Dialog as VanDialog, showSuccessToast, showFailToast } from 'vant'
import request from '@/api/request'

const userInfo = ref(null)
const balanceLogs = ref([])
const todayOrders = ref(0) // Mocked or calculated
const showWithdraw = ref(false)
const withdrawAmount = ref('')

const loadData = async () => {
  try {
    // 获取个人信息（包含余额）
    const userRes = await request.get('/user/info')
    userInfo.value = userRes.data

    // 获取资金流水
    const logRes = await request.get('/user/balance-log', { params: { pageNum: 1, pageSize: 50 } })
    balanceLogs.value = logRes.data.records
  } catch (err) {
    console.error(err)
  }
}

const getTypeColor = (type, amount) => {
  if (type === 1) return 'bg-[#0A84FF]/20 text-[#0A84FF]' // 充值
  if (type === 2) return 'bg-[#FF3B30]/20 text-[#FF3B30]' // 消费
  if (type === 3) return 'bg-[#0A84FF]/20 text-[#0A84FF]' // 退款
  if (type === 4) return 'bg-[#FF3B30]/20 text-[#FF3B30]' // 提现
  return 'bg-[#333333] text-white' // 其它，比如跑腿费入账如果是0? 我们目前没给骑手发跑腿费，只是模拟。
}

const getTypeLabel = (type) => {
  if (type === 1) return '充'
  if (type === 2) return '消'
  if (type === 3) return '退'
  if (type === 4) return '现'
  return '费'
}

const getTypeName = (type) => {
  if (type === 1) return '用户充值'
  if (type === 2) return '下单消费'
  if (type === 3) return '订单退款'
  if (type === 4) return '骑手提现出账'
  return '资金流水'
}

const handleWithdraw = async () => {
  if (!withdrawAmount.value || withdrawAmount.value <= 0) {
    showFailToast({ message: '请输入有效的提现金额', className: 'dark-toast' })
    return
  }
  if (withdrawAmount.value > (userInfo.value?.balance || 0)) {
    showFailToast({ message: '余额不足', className: 'dark-toast' })
    return
  }

  try {
    await request.post('/user/withdraw', null, { params: { amount: withdrawAmount.value } })
    showSuccessToast({ message: '提现成功，资金已打款！', className: 'dark-toast' })
    withdrawAmount.value = ''
    loadData() // 刷新余额和流水
  } catch (err) {
    console.error(err)
    showFailToast({ message: err.response?.data?.message || '提现失败', className: 'dark-toast' })
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
:deep(.van-toast.dark-toast) {
  background-color: #1C1C1E;
  color: #FFFFFF;
  border: 1px solid #333333;
}
:deep(.van-dialog.dark-dialog) {
  background-color: #1C1C1E;
  color: #FFFFFF;
}
:deep(.van-dialog__header) {
  color: #FFFFFF;
  font-weight: 900;
}
:deep(.van-dialog__confirm) {
  color: #00FF66 !important;
  font-weight: 900;
}
:deep(.van-dialog__cancel) {
  color: #8E8E93 !important;
}
</style>
