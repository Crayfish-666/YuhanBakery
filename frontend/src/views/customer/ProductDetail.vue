<template>
  <div class="max-w-7xl mx-auto px-6 pt-6 space-y-6 pb-24">
    <!-- Header Back Button -->
    <div class="flex items-center gap-4">
      <button @click="$router.back()" class="w-10 h-10 rounded-2xl bg-white flex items-center justify-center text-slate-400 hover:text-slate-800 hover:bg-slate-50 transition-colors shadow-sm">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7" />
        </svg>
      </button>
      <h2 class="text-xl font-bold text-slate-800">商品详情</h2>
    </div>

    <!-- Product Info Section -->
    <div class="bg-white rounded-3xl p-6 shadow-sm flex flex-col md:flex-row gap-8" v-if="product">
      <!-- Image -->
      <div class="md:w-1/2 rounded-2xl overflow-hidden bg-slate-50 aspect-square flex-shrink-0 relative">
        <img :src="product.image || '/img/default.jpg'" class="w-full h-full object-cover" />
      </div>
      
      <!-- Details & SKU -->
      <div class="md:w-1/2 flex flex-col justify-between">
        <div>
          <h1 class="text-3xl font-extrabold text-slate-800 mb-2">{{ product.name }}</h1>
          <p class="text-sm text-slate-500 mb-6 leading-relaxed">{{ product.description || '暂无商品介绍' }}</p>
          
          <div class="mb-6">
            <h3 class="text-sm font-bold text-slate-800 mb-3">选择规格</h3>
            <div class="flex flex-wrap gap-3">
              <button 
                v-for="sku in product.skus" 
                :key="sku.id"
                @click="selectedSku = sku"
                class="px-4 py-2.5 rounded-xl border text-sm font-medium transition-all"
                :class="selectedSku?.id === sku.id ? 'border-orange-500 bg-orange-50 text-orange-600 shadow-sm' : 'border-slate-200 text-slate-600 hover:border-slate-300'"
              >
                {{ sku.specName }} (¥{{ sku.price }})
              </button>
            </div>
            <div v-if="product.skus && product.skus.length === 0" class="text-xs text-rose-500 mt-2">暂无可用规格，无法购买</div>
          </div>
        </div>

        <div class="mt-8 pt-6 border-t border-slate-100 flex items-center justify-between">
          <div class="flex items-end gap-1">
            <span class="text-sm font-bold text-orange-600 mb-1">¥</span>
            <span class="text-3xl font-black text-orange-600 tracking-tight">{{ selectedSku?.price || '0.00' }}</span>
          </div>
          <button 
            @click="addToCart"
            :disabled="!selectedSku"
            class="px-8 py-3.5 rounded-2xl bg-orange-500 text-white font-bold hover:bg-orange-600 hover:shadow-lg hover:-translate-y-0.5 transition-all disabled:opacity-50 disabled:cursor-not-allowed"
          >
            加入购物车
          </button>
        </div>
      </div>
    </div>

    <!-- Reviews Section -->
    <div class="bg-white rounded-3xl p-6 shadow-sm">
      <h2 class="text-lg font-bold text-slate-800 mb-6 flex items-center gap-2">
        <span>用户评价</span>
        <span class="text-xs font-semibold px-2 py-0.5 bg-slate-100 text-slate-500 rounded-full">{{ reviews.length }}</span>
      </h2>
      
      <div v-if="reviews.length === 0" class="text-center py-10">
        <div class="w-16 h-16 bg-slate-50 rounded-full flex items-center justify-center mx-auto mb-3">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-8 h-8 text-slate-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
          </svg>
        </div>
        <p class="text-sm text-slate-400">暂无评价，购买后快来分享你的感受吧</p>
      </div>

      <div v-else class="space-y-6">
        <div v-for="review in reviews" :key="review.id" class="border-b border-slate-50 pb-6 last:border-0 last:pb-0">
          <div class="flex items-center justify-between mb-2">
            <div class="flex items-center gap-2">
              <div class="w-8 h-8 rounded-full bg-orange-100 text-orange-600 flex items-center justify-center font-bold text-xs">
                U
              </div>
              <span class="text-sm font-semibold text-slate-700">用户 {{ review.userId }}</span>
            </div>
            <span class="text-xs text-slate-400">{{ formatDate(review.createTime) }}</span>
          </div>
          
          <div class="flex items-center gap-1 mb-3">
            <svg v-for="i in 5" :key="i" xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" :class="i <= review.rating ? 'text-amber-400' : 'text-slate-200'" viewBox="0 0 20 20" fill="currentColor">
              <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
            </svg>
          </div>
          
          <p class="text-sm text-slate-600 leading-relaxed">{{ review.content }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProductDetail, getProductReviews, addCart } from '@/api/mall'
import { ElMessage } from 'element-plus'

const route = useRoute()
const product = ref(null)
const selectedSku = ref(null)
const reviews = ref([])

const loadData = async () => {
  const id = route.params.id
  if (!id) return
  
  try {
    const res = await getProductDetail(id)
    if (res.code === 200) {
      product.value = res.data
      if (res.data.skus && res.data.skus.length > 0) {
        selectedSku.value = res.data.skus[0]
      }
    }
    
    const reviewRes = await getProductReviews(id)
    if (reviewRes.code === 200) {
      reviews.value = reviewRes.data || []
    }
  } catch (err) {
    console.error('Failed to load product', err)
  }
}

const addToCart = async () => {
  if (!selectedSku.value) return
  try {
    const res = await addCart(selectedSku.value.id, 1)
    if (res.code === 200) {
      ElMessage.success('已加入购物车')
    } else {
      ElMessage.error(res.msg || '加入购物车失败')
    }
  } catch (err) {
    console.error('Add to cart failed', err)
    ElMessage.error('网络请求失败')
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return dateStr.replace('T', ' ').substring(0, 16)
}

onMounted(() => {
  loadData()
})
</script>
