<template>
  <div class="admin-product space-y-4">
    <!-- 顶栏 -->
    <div class="flex items-center justify-between">
      <div>
        <h2 class="text-xl font-bold text-slate-800">商品及规格管辖</h2>
        <p class="text-xs text-slate-400 mt-0.5">管理商城的单品蛋糕、面包、饮料。可配置各商品的尺寸或口味等多规格属性。</p>
      </div>
      <el-button type="primary" :icon="Plus" class="bg-amber-500 hover:bg-amber-600 border-none text-slate-950 font-bold rounded-lg" @click="openDrawer(null)">
        发布新商品
      </el-button>
    </div>

    <!-- 搜寻过滤栏 -->
    <div class="flex gap-3 bg-slate-50 p-4 rounded-xl border border-slate-100">
      <el-input v-model="searchKeyword" placeholder="搜寻商品名称..." class="max-w-xs" clearable @clear="loadProductsData" @keyup.enter="loadProductsData" />
      <el-button type="primary" class="bg-slate-900 border-none text-white font-bold" @click="loadProductsData">检索</el-button>
    </div>

    <!-- 商品主表格 -->
    <el-table :data="productsList" style="width: 100%" class="rounded-xl border border-slate-100" v-loading="loading">
      <el-table-column label="缩略图" width="120">
        <template #default="scope">
          <el-image :src="scope.row.image" class="w-16 h-12 rounded-lg object-cover" :preview-src-list="[scope.row.image]" preview-teleported />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="商品名称" />
      <el-table-column label="分类" width="150">
        <template #default="scope">
          <span>{{ getCategoryName(scope.row.categoryId) }}</span>
        </template>
      </el-table-column>
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
      <el-table-column prop="createTime" label="发布时间" width="180" />
      <el-table-column label="管辖操作" width="180">
        <template #default="scope">
          <el-button size="small" type="primary" link @click="openDrawer(scope.row)">
            修改规格
          </el-button>
          <el-button size="small" type="danger" link @click="handleDelete(scope.row.id)">
            删除下架
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页条 -->
    <div class="flex justify-end mt-4">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="totalProducts"
        layout="prev, pager, next, total"
        @current-change="loadProductsData"
      />
    </div>

    <!-- 级联编辑/发布抽屉 -->
    <el-drawer
      v-model="drawerVisible"
      :title="form.id ? '修改商品规格' : '发布新商品'"
      size="650px"
      destroy-on-close
    >
      <el-form :model="form" ref="formRef" :rules="rules" label-position="top" class="space-y-4">
        <div class="grid grid-cols-2 gap-4">
          <el-form-item label="分类归属" prop="categoryId">
            <el-select v-model="form.categoryId" placeholder="请点选大分类" class="w-full">
              <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="商品名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入商品名，如红丝绒蛋糕" />
          </el-form-item>
        </div>

        <el-form-item label="商品描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="2" placeholder="请输入商品详情介绍" />
        </el-form-item>

        <!-- 图片上传 -->
        <el-form-item label="商品配图">
          <div class="flex items-center gap-4">
            <el-upload
              action="/api/admin/upload"
              :show-file-list="false"
              :on-success="handleUploadSuccess"
              :before-upload="beforeUpload"
              name="file"
              :headers="uploadHeaders"
            >
              <el-button type="info" size="small" class="bg-slate-100 border-slate-200 text-slate-600">点击上传图片</el-button>
            </el-upload>
            <el-image v-if="form.image" :src="form.image" class="w-24 h-16 rounded-lg object-cover border border-slate-200" />
          </div>
        </el-form-item>

        <!-- 级联规格 SKU 管理 -->
        <div class="border-t border-slate-150 pt-4 space-y-3">
          <div class="flex items-center justify-between">
            <span class="text-sm font-bold text-slate-800">配置多规格属性 (SKU 行数)</span>
            <el-button type="primary" size="small" :icon="Plus" class="bg-emerald-500 border-none font-bold" @click="addSkuRow">
              添加规格行
            </el-button>
          </div>

          <el-table :data="form.skus" style="width: 100%" size="small" class="border border-slate-100 rounded-lg">
            <el-table-column label="规格名称 (如: 8寸/巧克力味)" prop="specName">
              <template #default="scope">
                <el-input v-model="scope.row.specName" placeholder="例如: 8寸/双层" size="small" />
              </template>
            </el-table-column>
            <el-table-column label="价格 (元)" prop="price" width="110">
              <template #default="scope">
                <el-input-number v-model="scope.row.price" :min="0.01" :precision="2" :controls="false" class="w-full" size="small" />
              </template>
            </el-table-column>
            <el-table-column label="库存 (件)" prop="stock" width="100">
              <template #default="scope">
                <el-input-number v-model="scope.row.stock" :min="0" :precision="0" :controls="false" class="w-full" size="small" />
              </template>
            </el-table-column>
            <el-table-column label="启用" prop="status" width="70">
              <template #default="scope">
                <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="0" size="small" />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="70">
              <template #default="scope">
                <el-button type="danger" link size="small" @click="removeSkuRow(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-form>

      <template #footer>
        <div class="flex justify-end gap-2 p-4 border-t border-slate-100">
          <el-button @click="drawerVisible = false">取消</el-button>
          <el-button type="primary" class="bg-amber-500 border-none text-slate-950 font-bold" @click="submitProductForm">
            保存并发布
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
  ElDrawer, ElForm, ElFormItem, ElInput, ElSelect, ElOption, ElUpload,
  ElInputNumber, ElMessage, ElMessageBox
} from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { listProducts, saveProduct, deleteProduct, toggleProductStatus, listCategories, getProductDetail } from '@/api/admin'

const productsList = ref([])
const categories = ref([])
const loading = ref(false)
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(8)
const totalProducts = ref(0)

const drawerVisible = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  categoryId: null,
  name: '',
  description: '',
  image: '',
  status: 1,
  skus: []
})

const rules = {
  categoryId: [{ required: true, message: '必须归属分类', trigger: 'change' }],
  name: [{ required: true, message: '商品名称不能为空', trigger: 'blur' }]
}

// 上传请求头设置 Token
const uploadHeaders = computed(() => {
  const token = localStorage.getItem('bakery_token')
  return token ? { Authorization: 'Bearer ' + token } : {}
})

const loadCategoriesData = async () => {
  try {
    const res = await listCategories()
    categories.value = res.data
  } catch (err) {
    console.error(err)
  }
}

const getCategoryName = (catId) => {
  const cat = categories.value.find(c => c.id === catId)
  return cat ? cat.name : '未知分类'
}

const loadProductsData = async () => {
  loading.value = true
  try {
    const res = await listProducts({
      keyword: searchKeyword.value,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    })
    productsList.value = res.data.records
    totalProducts.value = res.data.total
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

// 快速下架变更状态
const handleStatusChange = async (id, status) => {
  try {
    await toggleProductStatus(id, status)
    ElMessage.success('上下架状态变更成功')
  } catch (err) {
    // 失败重置
    loadProductsData()
  }
}

// 打开规格编辑面板
const openDrawer = async (row) => {
  if (row) {
    form.id = row.id
    form.categoryId = row.categoryId
    form.name = row.name
    form.description = row.description
    form.image = row.image
    form.status = row.status
    form.skus = []
    
    // 从后端拉取该商品完整的 SKU 属性
    try {
      // 这里的 getProductDetail 是刚才在 CustomerProductController 导出的详情接口，包含嵌套 SKU 列表
      // 管理端可直接共用详情接口
      const detailRes = await getProductDetail(row.id)
      form.skus = detailRes.data.skus || []
    } catch (err) {
      console.error(err)
    }
  } else {
    form.id = null
    form.categoryId = null
    form.name = ''
    form.description = ''
    form.image = ''
    form.status = 1
    form.skus = [
      { specName: '标准件', price: 10.00, stock: 100, status: 1 }
    ]
  }
  drawerVisible.value = true
}

const addSkuRow = () => {
  form.skus.push({ specName: '', price: 10.00, stock: 100, status: 1 })
}

const removeSkuRow = (index) => {
  form.skus.splice(index, 1)
}

const beforeUpload = (file) => {
  const isJPGorPNG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/jpg'
  if (!isJPGorPNG) {
    ElMessage.error('上传图片只能是 JPG/JPEG/PNG 格式!')
  }
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!')
  }
  return isJPGorPNG && isLt2M
}

const handleUploadSuccess = (response) => {
  // 后端返回 Result 结构，url 为 data 字段
  if (response.code === 200) {
    form.image = response.data
    ElMessage.success('图片中转成功')
  } else {
    ElMessage.error(response.message || '图片上传失败')
  }
}

const submitProductForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      if (form.skus.length === 0) {
        return ElMessage.warning('商品规格列表不能为空，请至少添加一个规格')
      }
      // 检查 SKU 的规格名称是否填全
      const hasEmptyName = form.skus.some(s => !s.specName.trim())
      if (hasEmptyName) {
        return ElMessage.warning('规格名称不能为空，请输入规格值')
      }

      try {
        await saveProduct(form)
        ElMessage.success('级联商品规格发布成功')
        drawerVisible.value = false
        loadProductsData()
      } catch (err) {
        console.error(err)
      }
    }
  })
}

const handleDelete = (id) => {
  ElMessageBox.confirm('删除商品将一并逻辑下架该商品及其全部规格并清理 Redis 库存缓存，确认删除吗？', '高危操作警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteProduct(id)
      ElMessage.success('商品级联删除成功')
      loadProductsData()
    } catch (err) {
      console.error(err)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadCategoriesData()
  loadProductsData()
})
</script>
