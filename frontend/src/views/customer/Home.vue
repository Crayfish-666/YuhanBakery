<template>
  <div class="home-view font-serif text-bakery-burgundy relative min-h-screen overflow-hidden bg-gradient-to-tr from-[#FAF6F0] via-[#FAF8F5] to-[#FFFDFB]">
    
    <!-- Decorative background elements -->
    <!-- Blurry ambient glows -->
    <div class="absolute top-[8%] right-[-12%] w-[450px] h-[450px] rounded-full bg-[#f2cc8f]/15 blur-[120px] pointer-events-none animate-pulse-slow z-0"></div>
    <div class="absolute bottom-[20%] left-[-15%] w-[600px] h-[600px] rounded-full bg-[#e07a5f]/8 blur-[160px] pointer-events-none animate-pulse-slow z-0"></div>
    <div class="absolute top-[45%] left-[30%] w-[350px] h-[350px] rounded-full bg-[#81b29a]/5 blur-[100px] pointer-events-none animate-pulse-slow z-0"></div>

    <!-- Organic Plant Shadow overlay (cast from top-right) -->
    <div class="absolute top-0 right-0 w-[450px] h-[450px] opacity-[0.04] pointer-events-none blur-[1px] z-0">
      <svg viewBox="0 0 100 100" class="w-full h-full text-bakery-burgundy fill-current">
        <path d="M100,0 C90,15 78,25 65,30 C50,35.5 35,32 15,25 C25,35 42,45 58,42 C72,39 88,25 100,0 Z M85,8 C76,18 64,22 52,21 C62,28 75,22 85,8 Z M70,15 C61,24 49,26 38,24 C48,31 60,26 70,15 Z" />
      </svg>
    </div>

    <!-- Sparkling Stars -->
    <svg class="absolute top-[12%] left-[8%] w-5 h-5 text-bakery-primary/30 animate-star pointer-events-none z-0" viewBox="0 0 24 24" fill="currentColor">
      <path d="M12 2l2.4 7.2 7.2 2.4-7.2 2.4-2.4 7.2-2.4-7.2-7.2-2.4 7.2-2.4z"/>
    </svg>
    <svg class="absolute top-[35%] right-[42%] w-4 h-4 text-bakery-primary/45 animate-star delay-300 pointer-events-none z-0" viewBox="0 0 24 24" fill="currentColor">
      <path d="M12 2l2.4 7.2 7.2 2.4-7.2 2.4-2.4 7.2-2.4-7.2-7.2-2.4 7.2-2.4z"/>
    </svg>
    <svg class="absolute bottom-[25%] left-[12%] w-5 h-5 text-bakery-primary/25 animate-star delay-500 pointer-events-none z-0" viewBox="0 0 24 24" fill="currentColor">
      <path d="M12 2l2.4 7.2 7.2 2.4-7.2 2.4-2.4 7.2-2.4-7.2-7.2-2.4 7.2-2.4z"/>
    </svg>

    <!-- Hero Banner Section -->
    <section class="relative w-full max-w-7xl mx-auto px-6 pt-16 pb-28 flex flex-col md:flex-row items-center justify-between min-h-[550px] z-10">
      <div class="md:w-1/2 space-y-8 animate-fade-in-up">
        <p class="text-[10px] tracking-[0.3em] uppercase text-bakery-brown/70 font-sans">Summer Menu 2026</p>
        <h1 class="text-5xl md:text-6xl font-bold leading-tight">
          把庆祝，切成一块<br />
          <span class="glowing-title block mt-2">会发光的蛋糕。</span>
        </h1>
        <p class="text-sm text-bakery-brown/80 max-w-md leading-relaxed font-sans">
          为生日、茶歇与节日送礼建立一套稳定、优雅、可预约的甜点体验。
        </p>

        <!-- Brand Promises Icon Group -->
        <div class="flex flex-wrap gap-x-6 gap-y-3 pt-2 pb-2 font-sans text-xs text-bakery-brown font-medium">
          <div class="flex items-center gap-2 hover:text-bakery-primary transition-colors duration-300 cursor-pointer">
            <span class="w-5.5 h-5.5 rounded-full bg-bakery-primary/10 text-bakery-primary flex items-center justify-center">
              <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364-6.364l-.707.707M6.343 17.657l-.707.707m0-12.728l.707.707m12.728 12.728l.707.707M12 8a4 4 0 100 8 4 4 0 000-8z"></path></svg>
            </span>
            <span>当日新鲜现做</span>
          </div>
          <div class="flex items-center gap-2 hover:text-bakery-primary transition-colors duration-300 cursor-pointer">
            <span class="w-5.5 h-5.5 rounded-full bg-bakery-primary/10 text-bakery-primary flex items-center justify-center">
              <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"></path></svg>
            </span>
            <span>恒温冷链直达</span>
          </div>
          <div class="flex items-center gap-2 hover:text-bakery-primary transition-colors duration-300 cursor-pointer">
            <span class="w-5.5 h-5.5 rounded-full bg-bakery-primary/10 text-bakery-primary flex items-center justify-center">
              <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z"></path></svg>
            </span>
            <span>心意手写贺卡</span>
          </div>
        </div>

        <div class="flex items-center gap-4 pt-2 font-sans">
          <button @click="scrollToCatalogue" class="group relative overflow-hidden px-8 py-3 border-2 border-bakery-burgundy bg-transparent text-bakery-burgundy rounded-full font-bold transition-colors duration-300 hover:text-bakery-cream flex items-center gap-2 cursor-pointer">
            <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-bakery-burgundy transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
            <span class="relative z-10 flex items-center gap-2 transition-colors duration-50 group-hover:text-bakery-cream">
              立即选购 <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 5l7 7m0 0l-7 7m7-7H3"></path></svg>
            </span>
          </button>
          <button @click="handleClaimNewStoreCoupon" class="group relative overflow-hidden px-8 py-3 border-2 border-[#e07a5f] bg-[#e07a5f]/10 text-[#e07a5f] rounded-full font-bold transition-colors duration-300 hover:bg-[#e07a5f] hover:text-white flex items-center gap-2 cursor-pointer shadow-sm">
            <span class="relative z-10 flex items-center gap-2 transition-colors duration-50">
              🎁 领满50减25券
            </span>
          </button>
        </div>
      </div>
      
      <!-- Hero Image Card with depth & boundary overflow -->
      <div class="relative md:w-1/2 flex justify-end mt-12 md:mt-0 animate-fade-in-up delay-200">
        <!-- Golden Halo Background behind cake -->
        <div class="absolute -inset-10 m-auto w-[110%] h-[110%] rounded-full bg-[#f2cc8f]/20 blur-[50px] pointer-events-none -z-10 animate-pulse-slow"></div>
        
        <!-- Parallax Container -->
        <div 
          class="relative w-full max-w-[420px] aspect-[4/5] rounded-[2rem] border border-bakery-brown/10 bg-white/40 p-3 shadow-[0_24px_80px_rgba(90,42,36,0.10)] overflow-hidden cursor-pointer"
          style="perspective: 1200px"
          @mousemove="handleHeroMouseMove"
          @mouseleave="handleHeroMouseLeave"
        >
          <div
            :style="heroParallaxStyle"
            class="w-full h-full transition-transform duration-200 ease-out"
          >
            <img 
              src="https://images.unsplash.com/photo-1464349095431-e9a21285b5f3?w=1200&h=1300&fit=crop&auto=format" 
              alt="粉色奶油生日蛋糕与蜡烛" 
              class="w-full h-full rounded-[1.5rem] object-cover scale-[1.05]" 
            />
          </div>
        </div>

        <!-- Floating Price Card with glassmorphism & refraction -->
        <div class="absolute -bottom-6 -left-6 md:-left-12 glass-panel p-6 rounded-2xl shadow-[0_20px_50px_rgba(74,46,43,0.18)] border border-white/40 max-w-xs font-sans hover:scale-105 hover:shadow-[0_25px_60px_rgba(74,46,43,0.25)] transition-all duration-500 z-20">
          <div class="flex justify-between items-start mb-2 gap-4">
            <div>
              <p class="text-[10px] text-amber-700 font-bold tracking-wider">今日主厨推荐</p>
              <h3 class="font-serif text-lg font-bold text-bakery-burgundy">玫瑰浆果皇冠</h3>
            </div>
            <span class="text-xl font-extrabold text-bakery-burgundy">¥268</span>
          </div>
          <p class="text-[10px] text-bakery-brown mb-4">6 英寸 · 4-6 人食 · 今日 18:30 前可送</p>
          <button @click="scrollToCatalogue" class="group relative overflow-hidden w-full py-2.5 border border-bakery-burgundy bg-transparent text-bakery-burgundy rounded-full text-xs font-bold transition-colors duration-300 hover:text-white cursor-pointer">
            <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-bakery-burgundy transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
            <span class="relative z-10 transition-colors duration-50 group-hover:text-white">立即预订</span>
          </button>
        </div>
      </div>
    </section>

    <!-- Catalogue Section -->
    <section id="catalogue" class="py-24 border-t border-bakery-brown/5 relative z-10">
      <!-- Faint decorative background light behind catalog -->
      <div class="absolute top-[20%] left-[-10%] w-[400px] h-[400px] rounded-full bg-[#f2cc8f]/8 blur-[100px] pointer-events-none -z-10"></div>

      <div class="max-w-7xl mx-auto px-6">
        <div class="flex flex-col md:flex-row justify-between items-end mb-12 animate-fade-in-up">
          <div>
            <p class="text-[10px] tracking-[0.2em] uppercase text-bakery-brown/70 font-sans mb-2">Catalogue</p>
            <h2 class="text-3xl font-bold text-bakery-burgundy">今天想要哪一种甜？</h2>
          </div>
          
          <!-- Categories Cards Group (Replacing simple text pills) -->
          <div class="flex gap-4 overflow-x-auto pb-4 pt-2 scrollbar-none snap-x font-sans w-full md:w-auto mt-6 md:mt-0">
            <!-- ALL category card -->
            <div 
              @click="selectCategory(null)" 
              :class="selectedCategoryId === null ? 'bg-bakery-burgundy text-bakery-cream border-bakery-burgundy shadow-lg shadow-bakery-burgundy/20 -translate-y-1' : 'bg-white/60 hover:bg-white text-bakery-brown border-white/50 hover:-translate-y-1 hover:shadow-md'"
              class="flex-shrink-0 w-24 sm:w-28 h-28 sm:h-32 rounded-2xl border flex flex-col items-center justify-center cursor-pointer transition-all duration-300 snap-start group"
            >
              <div :class="selectedCategoryId === null ? 'bg-white/10 text-white' : 'bg-bakery-cream text-bakery-burgundy'" class="w-12 h-12 rounded-full flex items-center justify-center text-xl mb-2 transition-colors duration-300 group-hover:scale-110">
                🍰
              </div>
              <span class="text-xs font-bold tracking-wide">全部</span>
            </div>
            
            <!-- Dynamic categories cards -->
            <div 
              v-for="cat in categories" 
              :key="cat.id" 
              @click="selectCategory(cat.id)" 
              :class="selectedCategoryId === cat.id ? 'bg-bakery-burgundy text-bakery-cream border-bakery-burgundy shadow-lg shadow-bakery-burgundy/20 -translate-y-1' : 'bg-white/60 hover:bg-white text-bakery-brown border-white/50 hover:-translate-y-1 hover:shadow-md'"
              class="flex-shrink-0 w-24 sm:w-28 h-28 sm:h-32 rounded-2xl border flex flex-col items-center justify-center cursor-pointer transition-all duration-300 snap-start group"
            >
              <div :class="selectedCategoryId === cat.id ? 'bg-white/10 text-white' : 'bg-bakery-cream text-bakery-burgundy'" class="w-12 h-12 rounded-full flex items-center justify-center text-xl mb-2 transition-colors duration-300 group-hover:scale-110">
                {{ getCategoryIcon(cat.name) }}
              </div>
              <span class="text-xs font-bold tracking-wide">{{ cat.name }}</span>
            </div>
          </div>
        </div>

        <!-- Product Grid -->
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
          <div 
            v-for="(prod, idx) in products" 
            :key="prod.id" 
            @click="$router.push(`/customer/product/${prod.id}`)"
            class="group bg-white/70 backdrop-blur-sm rounded-[1.5rem] p-4 shadow-sm hover:shadow-[0_22px_45px_rgba(74,46,43,0.12)] border border-white/50 hover:border-bakery-primary/20 hover:-translate-y-2 transition-all duration-500 animate-fade-in-up cursor-pointer"
            :style="{ animationDelay: (idx * 60) + 'ms' }"
          >
            <div class="relative aspect-square rounded-xl overflow-hidden bg-slate-50/50 mb-4 shadow-sm z-0">
              <!-- Hover Glow Effect -->
              <div 
                class="absolute inset-0 scale-125 opacity-0 blur-3xl transition-opacity duration-500 group-hover:opacity-100 -z-10" 
                :style="{ backgroundColor: getProductGlowColor(prod.name) }"
              ></div>
              <img :src="prod.image" class="relative z-10 w-full h-full object-cover group-hover:scale-[1.05] transition-transform duration-500 ease-out" alt="product" />
              <!-- Tags -->
              <div class="absolute top-3 left-3 flex flex-col gap-1 z-20">
                <span class="px-2.5 py-0.5 bg-[#e07a5f] text-white text-[9px] font-bold rounded-full font-sans shadow-sm tracking-wider">NEW</span>
              </div>
              <button class="absolute top-3 right-3 p-2 bg-white/80 backdrop-blur rounded-full text-bakery-brown hover:text-red-500 hover:scale-110 active:scale-95 transition-all shadow-sm cursor-pointer z-20">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"></path></svg>
              </button>
            </div>
            
            <div class="space-y-1.5 px-1">
              <h3 class="font-bold text-base leading-tight truncate text-bakery-burgundy">{{ prod.name }}</h3>
              <p class="text-[10px] text-bakery-brown font-sans truncate leading-relaxed">{{ prod.description }}</p>
            </div>
            
            <div class="flex items-center justify-between mt-4 px-1 font-sans">
              <span class="text-xl font-bold text-bakery-burgundy">¥{{ getMinPrice(prod.skus) }}</span>
              <button @click.stop="openSkuSelector(prod)" class="group/btn relative overflow-hidden px-4 py-1.5 border border-bakery-burgundy text-bakery-burgundy rounded-full text-xs font-bold transition-colors duration-300 hover:text-white active:scale-95 flex items-center gap-1 cursor-pointer">
                <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-bakery-burgundy transition-transform duration-300 ease-out group-hover/btn:translate-x-0"></span>
                <span class="relative z-10 flex items-center gap-1 transition-colors duration-50 group-hover/btn:text-white">
                  <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path></svg>
                  加入
                </span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Services Section -->
    <section class="max-w-7xl mx-auto px-6 py-24 flex flex-col lg:flex-row gap-12 items-center relative z-10">
      <div class="lg:w-1/3 space-y-4 animate-fade-in-up">
        <p class="text-[10px] tracking-[0.2em] uppercase text-bakery-brown/70 font-sans mb-2">Service Standards</p>
        <h2 class="text-3xl md:text-4xl font-bold leading-tight text-bakery-burgundy">
          同城速配、生日乔迁，<br/>都可以更体面地送甜。
        </h2>
        <p class="text-sm text-bakery-brown leading-relaxed font-sans">
          我们定制专用的冷链及包装标准，支持企业订购与贺卡按需搭配，适合每一次重要的团聚与庆祝。
        </p>
      </div>
      
      <div class="lg:w-2/3 bg-white/60 backdrop-blur-md p-6 md:p-10 rounded-[2rem] border border-white/50 shadow-sm flex flex-col gap-6 font-sans animate-fade-in-up delay-200">
        <div class="flex gap-5 items-start p-4 hover:bg-white/80 rounded-xl transition-all duration-300 group cursor-pointer">
          <div class="w-12 h-12 rounded-full bg-bakery-cream flex flex-shrink-0 items-center justify-center text-bakery-burgundy group-hover:scale-110 transition-transform duration-300">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path></svg>
          </div>
          <div>
            <h4 class="font-bold text-sm mb-1 text-bakery-burgundy">同城冷链</h4>
            <p class="text-xs text-bakery-brown">全程 2-6°C 保温箱配送，签收前实时追踪。</p>
          </div>
        </div>
        
        <div class="flex gap-5 items-start p-4 hover:bg-white/80 rounded-xl transition-all duration-300 group cursor-pointer">
          <div class="w-12 h-12 rounded-full bg-bakery-cream flex flex-shrink-0 items-center justify-center text-bakery-burgundy group-hover:scale-110 transition-transform duration-300">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
          </div>
          <div>
            <h4 class="font-bold text-sm mb-1 text-bakery-burgundy">准时保鲜</h4>
            <p class="text-xs text-bakery-brown">可预约 30 分钟配送窗口，迟到自动补偿。</p>
          </div>
        </div>
        
        <div class="flex gap-5 items-start p-4 hover:bg-white/80 rounded-xl transition-all duration-300 group cursor-pointer">
          <div class="w-12 h-12 rounded-full bg-bakery-cream flex flex-shrink-0 items-center justify-center text-bakery-burgundy group-hover:scale-110 transition-transform duration-300">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z"></path></svg>
          </div>
          <div>
            <h4 class="font-bold text-sm mb-1 text-bakery-burgundy">现做定制</h4>
            <p class="text-xs text-bakery-brown">祝福牌、蜡烛、餐具与企业卡片按单配置。</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Simple SKU Selection Modal overlay (Replacing Vant ActionSheet) -->
    <div v-if="showSkuPopup" class="fixed inset-0 z-[100] flex items-center justify-center font-sans">
      <div class="absolute inset-0 bg-black/40 backdrop-blur-sm" @click="showSkuPopup = false"></div>
      <div class="relative bg-white w-full max-w-md mx-4 rounded-2xl shadow-2xl p-6 overflow-hidden animate-fade-in-up">
        <button @click="showSkuPopup = false" class="absolute top-4 right-4 p-2 text-slate-400 hover:text-slate-800 transition">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
        </button>
        
        <div v-if="selectedProduct" class="space-y-6">
          <div class="flex gap-4 border-b border-slate-100 pb-4">
            <img :src="selectedProduct.image" class="w-24 h-24 object-cover rounded-xl shadow-sm bg-slate-50" />
            <div class="flex flex-col justify-center">
              <h3 class="font-bold text-lg text-slate-800">{{ selectedProduct.name }}</h3>
              <p class="text-2xl font-black text-amber-700 mt-1">¥{{ currentSkuPrice }}</p>
            </div>
          </div>
          
          <div>
            <p class="text-sm font-bold text-slate-700 mb-3">选择规格</p>
            <div class="flex flex-wrap gap-2">
              <button
                v-for="sku in selectedProduct.skus"
                :key="sku.id"
                @click="selectSku(sku)"
                :class="activeSkuId === sku.id ? 'bg-amber-50 border-amber-600 text-amber-800 font-bold' : 'bg-white border-slate-200 text-slate-600 hover:border-amber-300'"
                class="px-4 py-2 text-xs rounded-xl border transition-all duration-200"
              >
                {{ sku.specName }}
              </button>
            </div>
          </div>

          <button @click="handleAddToCart" class="group relative overflow-hidden w-full py-3.5 border-2 border-bakery-burgundy bg-transparent text-bakery-burgundy font-bold rounded-xl transition-colors duration-300 hover:text-white flex items-center justify-center gap-2 cursor-pointer">
            <span class="absolute inset-0 z-0 h-full w-full -translate-x-full bg-bakery-burgundy transition-transform duration-300 ease-out group-hover:translate-x-0"></span>
            <span class="relative z-10 transition-colors duration-50 group-hover:text-white">加入购物袋</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { listCategories, listProducts, listCombos, claimCoupon } from '@/api/mall'
import { addCart } from '@/api/mall'
import { useUserStore } from '@/store/modules/user'

const router = useRouter()
const userStore = useUserStore()
const categories = ref([])
const products = ref([])
const selectedCategoryId = ref(null)

// Parallax states for hero featured image
const rx = ref(0)
const ry = ref(0)
const tx = ref(0)
const ty = ref(0)

const heroParallaxStyle = computed(() => {
  return {
    transform: `rotateX(${rx.value}deg) rotateY(${ry.value}deg) translateX(${tx.value}px) translateY(${ty.value}px)`,
    transformStyle: 'preserve-3d'
  }
})

const handleHeroMouseMove = (e) => {
  const rect = e.currentTarget.getBoundingClientRect()
  const xPct = (e.clientX - rect.left) / rect.width - 0.5
  const yPct = (e.clientY - rect.top) / rect.height - 0.5

  rx.value = -(yPct * 8)
  ry.value = xPct * 8
  tx.value = xPct * 24
  ty.value = yPct * 24
}

const handleHeroMouseLeave = () => {
  rx.value = 0
  ry.value = 0
  tx.value = 0
  ty.value = 0
}

const getProductGlowColor = (name) => {
  if (!name) return 'rgba(242, 204, 143, 0.3)'
  if (name.includes('玫瑰') || name.includes('草莓') || name.includes('红') || name.includes('浆果')) return 'rgba(224, 122, 95, 0.45)'
  if (name.includes('抹茶') || name.includes('开心果') || name.includes('绿') || name.includes('巴斯克')) return 'rgba(129, 178, 154, 0.45)'
  if (name.includes('巧克力') || name.includes('黑巧') || name.includes('可可') || name.includes('黑客')) return 'rgba(74, 46, 43, 0.35)'
  if (name.includes('芒果') || name.includes('柠檬') || name.includes('黄') || name.includes('橙')) return 'rgba(242, 204, 143, 0.45)'
  return 'rgba(242, 204, 143, 0.35)'
}

const getCategoryIcon = (name) => {
  if (!name) return '🍰'
  if (name.includes('生日') || name.includes('庆') || name.includes('祝')) return '🎂'
  if (name.includes('巧克力') || name.includes('黑巧') || name.includes('可可')) return '🍫'
  if (name.includes('草莓') || name.includes('水果') || name.includes('浆果') || name.includes('莓')) return '🍓'
  if (name.includes('乳酪') || name.includes('芝士') || name.includes('干酪') || name.includes('奶酪')) return '🧀'
  if (name.includes('下午茶') || name.includes('点心') || name.includes('面包') || name.includes('西点') || name.includes('挞')) return '☕'
  if (name.includes('礼盒') || name.includes('手礼') || name.includes('伴手')) return '🎁'
  return '🍰'
}

const showSkuPopup = ref(false)
const selectedProduct = ref(null)
const activeSkuId = ref(null)
const currentSkuPrice = ref(0)
const currentSkuStock = ref(0)
const buyQuantity = ref(1)

const loadCategories = async () => {
  try {
    const res = await listCategories()
    categories.value = res.data
  } catch (err) {
    console.error(err)
  }
}

const loadProducts = async () => {
  try {
    const params = { pageNum: 1, pageSize: 50 }
    if (selectedCategoryId.value !== null) {
      params.categoryId = selectedCategoryId.value
    }
    const res = await listProducts(params)
    products.value = res.data.records
  } catch (err) {
    console.error(err)
  }
}

const selectCategory = (catId) => {
  selectedCategoryId.value = catId
  loadProducts()
}

const getMinPrice = (skus) => {
  if (!skus || skus.length === 0) return 0
  return Math.min(...skus.map(s => s.price))
}

const openSkuSelector = (product) => {
  selectedProduct.value = product
  buyQuantity.value = 1
  if (product.skus && product.skus.length > 0) {
    const firstSku = product.skus[0]
    activeSkuId.value = firstSku.id
    currentSkuPrice.value = firstSku.price
    currentSkuStock.value = firstSku.stock
  }
  showSkuPopup.value = true
}

const selectSku = (sku) => {
  activeSkuId.value = sku.id
  currentSkuPrice.value = sku.price
  currentSkuStock.value = sku.stock
}

const handleAddToCart = async () => {
  if (!activeSkuId.value) return
  // Check auth before cart op
  if (!userStore.isLoggedIn) {
    userStore.showLoginModal = true
    userStore.loginModalTab = 'login'
    return
  }
  
  try {
    await addCart(activeSkuId.value, buyQuantity.value)
    alert('已成功加入购物袋')
    showSkuPopup.value = false
    if (window.refreshCartBadge) {
      window.refreshCartBadge()
    }
  } catch (err) {
    console.error(err)
  }
}

const scrollToCatalogue = () => {
  document.getElementById('catalogue')?.scrollIntoView({ behavior: 'smooth' })
}

const handleClaimNewStoreCoupon = async () => {
  if (!userStore.isLoggedIn) {
    userStore.showLoginModal = true
    userStore.loginModalTab = 'login'
    return
  }
  try {
    await claimCoupon(2)
    alert('恭喜，新店开业满50减25优惠券领取成功！')
  } catch (err) {
    console.error(err)
    if (err.response && err.response.data && err.response.data.message) {
      alert(err.response.data.message)
    } else {
      alert('领取失败或您已领取过该优惠券')
    }
  }
}

onMounted(() => {
  loadCategories()
  loadProducts()
})
</script>

<style scoped>
/* Scoped styles can be placed here if needed */
</style>
