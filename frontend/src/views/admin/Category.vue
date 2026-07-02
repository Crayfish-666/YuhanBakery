<template>
  <div class="admin-category space-y-4">
    <!-- 标题及新增按钮 -->
    <div class="flex items-center justify-between">
      <div>
        <h2 class="text-xl font-bold text-slate-800">商品分类管理</h2>
        <p class="text-xs text-slate-400 mt-0.5">配置商品归属的大分类，调整在 C 端商城顶部的显示排序。</p>
      </div>
      <el-button type="primary" :icon="Plus" class="bg-amber-500 hover:bg-amber-600 border-none text-slate-950 font-bold rounded-lg" @click="openDialog(null)">
        新增分类
      </el-button>
    </div>

    <!-- 数据表格 -->
    <el-table :data="categories" style="width: 100%" class="rounded-xl border border-slate-100 mt-4" v-loading="loading">
      <el-table-column prop="id" label="分类ID" width="100" />
      <el-table-column prop="name" label="分类名称" />
      <el-table-column prop="sort" label="排序权重" width="120" sortable />
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column label="动作管辖" width="180">
        <template #default="scope">
          <el-button size="small" type="primary" link @click="openDialog(scope.row)">
            修改
          </el-button>
          <el-button size="small" type="danger" link @click="handleDelete(scope.row.id)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/修改对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="form.id ? '修改分类' : '新建分类'"
      width="400px"
      destroy-on-close
    >
      <el-form :model="form" ref="formRef" :rules="rules" label-position="top">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="排序值 (越小越靠前)" prop="sort">
          <el-input-number v-model="form.sort" :min="0" class="w-full" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer flex justify-end gap-2">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" class="bg-amber-500 text-slate-950 border-none font-bold" @click="submitForm">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import {
  ElButton, ElTable, ElTableColumn, ElDialog, ElForm, ElFormItem,
  ElInput, ElInputNumber, ElMessage, ElMessageBox
} from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { listCategories, saveCategory, deleteCategory } from '@/api/admin'
import 'element-plus/theme-chalk/el-message-box.css'
import 'element-plus/theme-chalk/el-message.css'

const categories = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  name: '',
  sort: 0
})

const rules = {
  name: [{ required: true, message: '分类名称不能为空', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await listCategories()
    categories.value = res.data
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

const openDialog = (row) => {
  if (row) {
    form.id = row.id
    form.name = row.name
    form.sort = row.sort
  } else {
    form.id = null
    form.name = ''
    form.sort = 0
  }
  dialogVisible.value = true
}

const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await saveCategory(form)
        ElMessage.success('保存分类成功')
        dialogVisible.value = false
        loadData()
      } catch (err) {
        console.error(err)
      }
    }
  })
}

const handleDelete = (id) => {
  ElMessageBox.confirm('您确定要彻底删除该商品分类吗？', '重要警示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCategory(id)
      ElMessage.success('分类已删除')
      loadData()
    } catch (err) {
      console.error(err)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadData()
})
</script>
