<template>
  <div class="admin-orders space-y-4">
    <!-- 标题 -->
    <div>
      <h2 class="text-xl font-bold text-slate-800">全站订单调控中心</h2>
      <p class="text-xs text-slate-400 mt-0.5">监控商城所有顾客提交的订单流水，查看外卖配送及自提进度。</p>
    </div>

    <!-- 搜索及筛选栏 -->
    <div class="flex flex-wrap gap-3 bg-slate-50 p-4 rounded-xl border border-slate-100 items-center">
      <el-input v-model="searchOrderNo" placeholder="请输入订单号精确搜索..." class="max-w-xs" clearable @clear="loadOrdersData" />
      
      <el-select v-model="filterStatus" placeholder="筛选订单状态" clearable class="w-44" @change="loadOrdersData" @clear="loadOrdersData">
        <el-option :value="0" label="0 - 未支付" />
        <el-option :value="1" label="1 - 已支付 (待接单)" />
        <el-option :value="2" label="2 - 配送中" />
        <el-option :value="3" label="3 - 已送达" />
        <el-option :value="4" label="4 - 已取消" />
      </el-select>

      <el-button type="primary" class="bg-slate-900 border-none text-white font-bold" @click="loadOrdersData">检索</el-button>
    </div>

    <!-- 数据表 -->
    <el-table :data="ordersList" style="width: 100%" class="rounded-xl border border-slate-100" v-loading="loading">
      <el-table-column prop="orderNo" label="订单号" width="180" />
      <el-table-column prop="totalAmount" label="支付总额 (元)" width="120" sortable />
      
      <el-table-column label="配送服务" width="120">
        <template #default="scope">
          <el-tag :type="scope.row.deliveryType === 1 ? 'warning' : 'success'" effect="light">
            {{ scope.row.deliveryType === 1 ? '外卖配送' : '到店自提' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="收货详情/快照">
        <template #default="scope">
          <span class="text-xs text-slate-600">{{ formatAddressSnapshot(scope.row.addressSnapshot) }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="contactPhone" label="联系电话" width="130" />
      
      <el-table-column label="订单状态" width="130">
        <template #default="scope">
          <el-tag :type="getStatusTagType(scope.row.status)" effect="dark">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="createTime" label="下单时间" width="180" />
      
      <el-table-column label="支付/关联项" width="150">
        <template #default="scope">
          <div class="text-[11px] text-slate-400 space-y-0.5">
            <p v-if="scope.row.payTime">已付: {{ scope.row.payTime.substring(5, 16) }}</p>
            <p v-if="scope.row.payMethod">方式: {{ scope.row.payMethod === 1 ? '钱包余额' : '微信支付' }}</p>
            <p v-if="scope.row.riderId">骑手ID: {{ scope.row.riderId }}</p>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="flex justify-end mt-4">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="totalOrders"
        layout="prev, pager, next, total"
        @current-change="loadOrdersData"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElTag, ElInput, ElSelect, ElOption, ElButton, ElPagination } from 'element-plus'
import { listAllOrders } from '@/api/admin'

const ordersList = ref([])
const loading = ref(false)
const searchOrderNo = ref('')
const filterStatus = ref(null)
const currentPage = ref(1)
const pageSize = ref(10)
const totalOrders = ref(0)

const loadOrdersData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize.value
    }
    if (searchOrderNo.value.trim() !== '') {
      params.orderNo = searchOrderNo.value.trim()
    }
    if (filterStatus.value !== null) {
      params.status = filterStatus.value
    }
    
    const res = await listAllOrders(params)
    ordersList.value = res.data.records
    totalOrders.value = res.data.total
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

// 解析地址快照 JSON
const formatAddressSnapshot = (snapshot) => {
  if (!snapshot) return '无需地址'
  if (snapshot === '自提自取' || snapshot === '自提单') return '到店自提'
  try {
    const addr = JSON.parse(snapshot)
    return `[${addr.contactName}] ${addr.addressDetail}`
  } catch (e) {
    return snapshot
  }
}

const getStatusText = (status) => {
  switch (status) {
    case 0: return '未支付'
    case 1: return '待接单 (已付)'
    case 2: return '配送中'
    case 3: return '已送达完成'
    case 4: return '已取消'
    default: return '未知'
  }
}

const getStatusTagType = (status) => {
  switch (status) {
    case 0: return 'info'
    case 1: return 'danger'
    case 2: return 'primary'
    case 3: return 'success'
    case 4: return 'warning'
    default: return 'info'
  }
}

onMounted(() => {
  loadOrdersData()
})
</script>
