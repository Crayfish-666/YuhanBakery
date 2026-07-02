<template>
  <div class="swipe-button-wrapper relative w-full h-[72px] bg-black border border-[#333333] overflow-hidden flex items-center">
    <!-- 背景层与流动动画 -->
    <div class="absolute inset-0 flex items-center justify-center overflow-hidden z-0">
      <div 
        class="text-[#333333] text-2xl font-black tracking-[1em] whitespace-nowrap opacity-50 flex"
        :class="{ 'animate-flow-arrows': !isCompleted, 'hidden': isCompleted }"
      >
        >>> >>> >>> >>> >>> >>>
      </div>
    </div>
    
    <!-- 背景文字 -->
    <div class="absolute w-full text-center text-lg font-bold text-white tracking-widest z-10 transition-opacity duration-300 pointer-events-none" :class="isCompleted ? 'opacity-0' : 'opacity-100'">
      {{ text }}
    </div>
    
    <!-- 滑过的进度条背景 -->
    <div class="absolute left-0 top-0 h-full bg-[#00FF66]/20 border-r border-[#00FF66] z-10 transition-all" :class="isCompleted ? 'duration-300' : ''" :style="{ width: thumbX + 36 + 'px' }"></div>
    
    <!-- 完成后的文字 -->
    <div class="absolute w-full text-center text-lg font-black text-[#00FF66] tracking-widest z-20 transition-opacity duration-300 pointer-events-none" :class="isCompleted ? 'opacity-100' : 'opacity-0'">
      {{ successText }}
    </div>

    <!-- 滑块 Thumb (机能风方块+防滑纹理) -->
    <div 
      ref="thumb"
      class="swipe-thumb absolute left-0 top-0 h-[72px] w-[72px] bg-[var(--rider-neon-green,#00FF66)] flex items-center justify-center z-30 cursor-pointer shadow-[0_0_15px_rgba(0,255,102,0.5)] border-r-2 border-white/30"
      :class="{ 'transition-transform duration-300': !isDragging, 'bg-[#1C1C1E] border-[#333333] shadow-none': isCompleted }"
      :style="{ transform: `translateX(${thumbX}px)` }"
      @touchstart.prevent="onTouchStart"
      @touchmove.prevent="onTouchMove"
      @touchend.prevent="onTouchEnd"
      @mousedown="onMouseDown"
    >
      <div v-if="!isCompleted" class="flex gap-1">
        <div class="w-1 h-8 bg-black/40 rounded-full"></div>
        <div class="w-1 h-8 bg-black/40 rounded-full"></div>
        <div class="w-1 h-8 bg-black/40 rounded-full"></div>
      </div>
      <van-icon v-else name="success" color="#00FF66" size="32" class="font-black" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  text: {
    type: String,
    default: '>> 滑动确认 >>'
  },
  successText: {
    type: String,
    default: '操作成功'
  }
})

const emit = defineEmits(['confirm'])

const thumb = ref(null)
const thumbX = ref(0)
const isDragging = ref(false)
const isCompleted = ref(false)
const maxDrag = ref(0)

let startX = 0
let currentX = 0

const initMaxDrag = () => {
  if (thumb.value && thumb.value.parentElement) {
    maxDrag.value = thumb.value.parentElement.offsetWidth - thumb.value.offsetWidth
  }
}

onMounted(() => {
  initMaxDrag()
  window.addEventListener('resize', initMaxDrag)
})

onUnmounted(() => {
  window.removeEventListener('resize', initMaxDrag)
})

const startDrag = (clientX) => {
  if (isCompleted.value) return
  isDragging.value = true
  startX = clientX - thumbX.value
}

const moveDrag = (clientX) => {
  if (!isDragging.value || isCompleted.value) return
  currentX = clientX - startX
  if (currentX < 0) currentX = 0
  if (currentX > maxDrag.value) currentX = maxDrag.value
  thumbX.value = currentX
}

const endDrag = () => {
  if (!isDragging.value || isCompleted.value) return
  isDragging.value = false
  if (thumbX.value >= maxDrag.value * 0.95) {
    thumbX.value = maxDrag.value
    isCompleted.value = true
    emit('confirm')
  } else {
    thumbX.value = 0
  }
}

const onTouchStart = (e) => startDrag(e.touches[0].clientX)
const onTouchMove = (e) => moveDrag(e.touches[0].clientX)
const onTouchEnd = () => endDrag()

const onMouseDown = (e) => {
  startDrag(e.clientX)
  document.addEventListener('mousemove', onMouseMove)
  document.addEventListener('mouseup', onMouseUp)
}
const onMouseMove = (e) => moveDrag(e.clientX)
const onMouseUp = () => {
  endDrag()
  document.removeEventListener('mousemove', onMouseMove)
  document.removeEventListener('mouseup', onMouseUp)
}

const reset = () => {
  isCompleted.value = false
  thumbX.value = 0
}
defineExpose({ reset })
</script>

<style scoped>
@keyframes flow-arrows {
  0% { transform: translateX(0); }
  100% { transform: translateX(20px); }
}
.animate-flow-arrows {
  animation: flow-arrows 1s linear infinite;
}
</style>
