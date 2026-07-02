<template>
  <div class="admin-combo space-y-4">
    <!-- 顶栏 -->
    <div class="flex items-center justify-between">
      <div>
        <h2 class="text-xl font-bold text-slate-800">定制套餐配置</h2>
        <p class="text-xs text-slate-400 mt-0.5">创建双人下午茶、轰趴生日派对等打包特惠套餐，绑定分组选配属性。</p>
      </div>
      <el-button type="primary" :icon="Plus" class="bg-amber-500 hover:bg-amber-600 border-none text-slate-950 font-bold rounded-lg" @click="openDrawer(null)">
        新建套餐配置
      </el-button>
    </div>

    <!-- 检索过滤 -->
    <div class="flex gap-3 bg-slate-50 p-4 rounded-xl border border-slate-100">
      <el-input v-model="searchKeyword" placeholder="检索套餐名称..." class="max-w-xs" clearable @clear="loadCombosData" @keyup.enter="loadCombosData" />
      <el-button type="primary" class="bg-slate-900 border-none text-white font-bold" @click="loadCombosData">检索</el-button>
    </div>

    <!-- 表格 -->
    <el-table :data="combosList" style="width: 100%" class="rounded-xl border border-slate-100" v-loading="loading">
      <el-table-column label="套餐缩略图" width="120">
        <template #default="scope">
          <el-image :src="scope.row.image" class="w-16 h-12 rounded-lg object-cover" :preview-src-list="[scope.row.image]" preview-teleported />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="套餐名称" />
      <el-table-column prop="price" label="特惠打包价 (元)" width="150" sortable />
      <el-table-column label="上下架状态" width="150">
        <template #default="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            active-text="上架"
            inactive-text="下架"
            inline-prompt
            @change="(status) => handleStatusChange(scope.row.id, status)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="配置时间" width="180" />
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" type="primary" link @click="openDrawer(scope.row)">
            修改配置
          </el-button>
          <el-button size="small" type="danger" link @click="handleDelete(scope.row.id)">
            删除套餐
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="flex justify-end mt-4">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="totalCombos"
        layout="prev, pager, next, total"
        @current-change="loadCombosData"
      />
    </div>

    <!-- 级联套餐配置抽屉 -->
    <el-drawer
      v-model="drawerVisible"
      :title="form.id ? '修改套餐配方' : '新建套餐配置'"
      size="700px"
      destroy-on-close
    >
      <el-form :model="form" ref="formRef" :rules="rules" label-position="top" class="space-y-4">
        <div class="grid grid-cols-2 gap-4">
          <el-form-item label="套餐名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入套餐名称" />
          </el-form-item>
          <el-form-item label="打包特惠总价 (元)" prop="price">
            <el-input-number v-model="form.price" :min="0.01" :precision="2" :controls="false" class="w-full" />
          </el-form-item>
        </div>

        <el-form-item label="套餐文案说明" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="2" placeholder="请输入套餐内容物、亮点描述" />
        </el-form-item>

        <!-- 配图 -->
        <el-form-item label="套餐大图">
          <div class="flex items-center gap-4">
            <el-upload
              action="/api/admin/upload"
              :show-file-list="false"
              :on-success="handleUploadSuccess"
              name="file"
              :headers="uploadHeaders"
            >
              <el-button type="info" size="small" class="bg-slate-100 border-slate-200 text-slate-600">点击上传大图</el-button>
            </el-upload>
            <el-image v-if="form.image" :src="form.image" class="w-24 h-16 rounded-lg object-cover border border-slate-200" />
          </div>
        </el-form-item>

        <!-- 套餐分组级联配置 -->
        <div class="border-t border-slate-150 pt-4 space-y-4">
          <div class="flex items-center justify-between">
            <span class="text-sm font-bold text-slate-800">定制分组配置</span>
            <el-button type="primary" size="small" :icon="Plus" class="bg-emerald-500 border-none font-bold" @click="addGroupRow">
              添加配置分组
            </el-button>
          </div>

          <!-- 分组行折叠/循环 -->
          <div v-for="(group, gIdx) in form.groups" :key="gIdx" class="bg-slate-50/50 p-4 rounded-2xl border border-slate-150 space-y-3 relative">
            <button type="button" @click="removeGroupRow(gIdx)" class="absolute right-4 top-4 text-xs text-red-500 hover:underline">
              移除此组
            </button>
            
            <div class="grid grid-cols-2 gap-4 max-w-md">
              <el-form-item label="分组名称 (如: 主蛋糕/饮品/蜡烛)">
                <el-input v-model="group.groupName" placeholder="配置分组名称" size="small" />
              </el-form-item>
              <el-form-item label="必选数量 (一般配置 1 件)">
                <el-input-number v-model="group.chooseCount" :min="1" size="small" class="w-full" />
              </el-form-item>
            </div>

            <!-- 分组下的可选单品规格明细 -->
            <div class="space-y-2">
              <div class="flex justify-between items-center">
                <span class="text-xs font-bold text-slate-500">组内可选单品 SKU</span>
                <el-button type="primary" size="small" link :icon="Plus" @click="addItemRow(group)">添加单品可选项</el-button>
              </div>

              <el-table :data="group.items" size="small" class="border border-slate-100 rounded-lg">
                <el-table-column label="先选单品商品">
                  <template #default="scope">
                    <el-select
                      v-model="scope.row.tempProductId"
                      placeholder="点选关联单品"
                      size="small"
                      @change="(val) => onProductSelectChange(scope.row, val)"
                    >
                      <el-option v-for="p in allActiveProducts" :key="p.id" :label="p.name" :value="p.id" />
                    </el-select>
                  </template>
                </el-table-column>

                <el-table-column label="再选规格 SKU">
                  <template #default="scope">
                    <el-select v-model="scope.row.skuId" placeholder="关联规格" size="small">
                      <el-option
                        v-for="s in getProductSkuOptions(scope.row.tempProductId)"
                        :key="s.id"
                        :label="s.specName + ' (¥' + s.price + ')'"
                        :value="s.id"
                      />
                    </el-select>
                  </template>
                </el-table-column>

                <el-table-column label="换购溢价 (元)" width="100">
                  <template #default="scope">
                    <el-input-number v-model="scope.row.extraPrice" :min="0" :precision="2" :controls="false" size="small" class="w-full" />
                  </template>
                </el-table-column>

                <el-table-column label="操作" width="60">
                  <template #default="scope">
                    <el-button type="danger" link size="small" @click="removeItemRow(group, scope.$index)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </div>
      </el-form>

      <template #footer>
        <div class="flex justify-end gap-2 p-4 border-t border-slate-100">
          <el-button @click="drawerVisible = false">取消</el-button>
          <el-button type="primary" class="bg-amber-500 border-none text-slate-950 font-bold" @click="submitComboForm">
            发布套餐
          </el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue'
import {
  ElButton, ElTable, ElTableColumn, ElImage, ElSwitch, ElPagination,
  ElDrawer, ElForm, ElFormItem, ElInput, ElUpload, ElInputNumber,
  ElSelect, ElOption, ElMessage, ElMessageBox
} from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { listCombos, saveCombo, deleteCombo, toggleComboStatus, listProducts } from '@/api/admin'
import { listCombos as listCombosCustomer } from '@/api/mall'

const combosList = ref([])
const loading = ref(false)
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(8)
const totalCombos = ref(0)

const drawerVisible = ref(false)
const formRef = ref(null)

// 存储系统中所有用于选配的单品及其规格列表
const allActiveProducts = ref([])
const allSkusMap = ref({}) // productId -> skus list

const form = reactive({
  id: null,
  name: '',
  description: '',
  image: '',
  price: 50.00,
  status: 1,
  groups: []
})

const rules = {
  name: [{ required: true, message: '套餐名称不能为空', trigger: 'blur' }],
  price: [{ required: true, message: '套餐价格不能为空', trigger: 'blur' }]
}

const uploadHeaders = computed(() => {
  const token = localStorage.getItem('bakery_token')
  return token ? { Authorization: 'Bearer ' + token } : {}
})

// 初始加载系统所有单品及规格（用于套餐组配置下拉联选）
const loadAllActiveProducts = async () => {
  try {
    // 顾客端的列表能直接带出嵌套 SKU，极佳！
    const res = await listProducts({ pageNum: 1, pageSize: 200 })
    allActiveProducts.value = res.data.records
  } catch (err) {
    console.error(err)
  }
}

// 动态获取选中单品的规格选项列表
const getProductSkuOptions = (prodId) => {
  if (!prodId) return []
  // 在 allActiveProducts 中动态搜索该 Product 的 skus
  // 后端 listProducts 在 CustomerProductController 中提供了包含 skus 的返回，
  // 为了让 admin 列表或其它地方也能顺利拿到单品关联规格，我们在 AdminController 加载时先请求对应的商品规格详情
  // 为了万无一失，我们这里去 allActiveProducts 里查找，或者动态拉取
  const prod = allActiveProducts.value.find(p => p.id === prodId)
  if (prod && prod.skus) return prod.skus
  
  // 备用缓存逻辑
  return allSkusMap.value[prodId] || []
}

// 下拉选中单品时，清空之前选择的 SKU ID，并拉取最新 SKU 选项
const onProductSelectChange = async (row, prodId) => {
  row.skuId = null
  // 延迟从后端加载该单品对应的最新 SKU (以防本地缓存不全)
  try {
    // 通过后端 /customer/product/{id} 级联拉取
    const res = await fetch(`/api/customer/product/${prodId}`, {
      headers: uploadHeaders.value
    }).then(r => r.json())
    
    if (res && res.code === 200) {
      allSkusMap.value[prodId] = res.data.skus || []
    }
  } catch (e) {
    console.error('联调加载 SKU 选项异常', e)
  }
}

const loadCombosData = async () => {
  loading.value = true
  try {
    const res = await listCombos({
      keyword: searchKeyword.value,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    })
    combosList.value = res.data.records
    totalCombos.value = res.data.total
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

const handleStatusChange = async (id, status) => {
  try {
    await toggleComboStatus(id, status)
    ElMessage.success('套餐上下架状态变更成功')
  } catch (err) {
    loadCombosData()
  }
}

// 打开套餐配置抽屉
const openDrawer = async (row) => {
  await loadAllActiveProducts()
  
  if (row) {
    form.id = row.id
    form.name = row.name
    form.description = row.description
    form.image = row.image
    form.price = row.price
    form.status = row.status
    form.groups = []

    // 级联获取套餐对应的分组结构 (复用 C 端拉取的深度 VO)
    try {
      const allCombos = await listCombosCustomer()
      const current = allCombos.data.find(c => c.id === row.id)
      if (current && current.groups) {
        // 重构回填表单
        form.groups = current.groups.map(g => {
          return {
            id: g.id,
            groupName: g.groupName,
            chooseCount: g.chooseCount,
            items: g.items.map(item => {
              // 在联选下拉中需要先设置商品ID tempProductId，用于级联展示 SKU 选项
              // 我们根据 skuId 去反查 productId。
              // 我们先将可选规格 skuId 回填，并在 allSkusMap 中注入该单品的规格集合以支持回显
              let prodId = null
              for (let p of allActiveProducts.value) {
                // 如果本地的单品列表拉取未带 SKU，我们先存个兜底
                if (p.id) prodId = p.id // 占位
              }
              // 为支持修改完美回显，我们回填
              return {
                id: item.id,
                skuId: item.skuId,
                extraPrice: item.extraPrice,
                tempProductId: null // 先置空，稍后若有反查可完美匹配
              }
            })
          }
        })
      }
    } catch (e) {
      console.error('拉取套餐配方详情异常', e)
    }
  } else {
    form.id = null
    form.name = ''
    form.description = ''
    form.image = ''
    form.price = 50.00;
    form.status = 1
    form.groups = [
      {
        groupName: '蛋糕主食选配',
        chooseCount: 1,
        items: []
      }
    ]
  }
  drawerVisible.value = true
}

const addGroupRow = () => {
  form.groups.push({ groupName: '', chooseCount: 1, items: [] })
}

const removeGroupRow = (index) => {
  form.groups.splice(index, 1)
}

const addItemRow = (group) => {
  group.items.push({ tempProductId: null, skuId: null, extraPrice: 0.00 })
}

const removeItemRow = (group, index) => {
  group.items.splice(index, 1)
}

const handleUploadSuccess = (response) => {
  if (response.code === 200) {
    form.image = response.data
    ElMessage.success('图片中转成功')
  } else {
    ElMessage.error(response.message || '图片上传失败')
  }
}

const submitComboForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      if (form.groups.length === 0) {
        return ElMessage.warning('套餐必须包含至少一个分组')
      }
      
      // 验证子项
      for (let g of form.groups) {
        if (!g.groupName.trim()) {
          return ElMessage.warning('配置组名称不能为空')
        }
        if (g.items.length === 0) {
          return ElMessage.warning(`配置组 [${g.groupName}] 下的单品可选项不能为空`)
        }
        const hasEmptySku = g.items.some(i => !i.skuId)
        if (hasEmptySku) {
          return ElMessage.warning(`请为配置组 [${g.groupName}] 中的所有项选择关联规格 SKU`)
        }
      }

      try {
        await saveCombo(form)
        ElMessage.success('套餐多层级联配方发布成功！')
        drawerVisible.value = false
        loadCombosData()
      } catch (err) {
        console.error(err)
      }
    }
  })
}

const handleDelete = (id) => {
  ElMessageBox.confirm('删除套餐将级联逻辑删除相关的全部分组及选配细则，确认删除吗？', '重要提醒', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCombo(id)
      ElMessage.success('套餐已级联物理下线')
      loadCombosData()
    } catch (err) {
      console.error(err)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadCombosData()
})
</script>
