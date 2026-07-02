<template>
  <div class="fixed bottom-6 right-6 z-50 font-sans">
    <!-- 悬浮按钮 -->
    <div 
      v-if="!isOpen" 
      @click="toggleChat"
      class="group relative w-14 h-14 bg-amber-700 rounded-full flex items-center justify-center cursor-pointer shadow-2xl hover:scale-110 transition-transform duration-300 animate-bounce-slow"
    >
      <div class="absolute inset-0 bg-amber-700 rounded-full animate-ping opacity-20"></div>
      <svg class="w-7 h-7 text-white relative z-10" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z"></path>
      </svg>
    </div>

    <!-- 聊天窗口 -->
    <div 
      v-else 
      class="w-[350px] sm:w-[400px] h-[500px] max-h-[80vh] bg-white/90 backdrop-blur-xl border border-slate-200 rounded-2xl shadow-2xl flex flex-col overflow-hidden origin-bottom-right transition-all duration-300"
    >
      <!-- Header -->
      <div class="bg-amber-700 text-white px-5 py-4 flex items-center justify-between shadow-md relative z-10">
        <div class="flex items-center gap-3">
          <div class="w-8 h-8 bg-white/20 rounded-full flex items-center justify-center">
            <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 10h4.764a2 2 0 011.789 2.894l-3.5 7A2 2 0 0115.263 21h-4.017c-.163 0-.326-.02-.485-.06L7 20m7-10V5a2 2 0 00-2-2h-.095c-.5 0-.905.405-.905.905 0 .714-.211 1.412-.608 2.006L7 11v9m7-10h-2M7 20H5a2 2 0 01-2-2v-6a2 2 0 012-2h2.514"></path></svg>
          </div>
          <div>
            <h3 class="font-bold text-sm tracking-widest">宇晗智能助理</h3>
            <p class="text-[10px] text-amber-100 flex items-center gap-1">
              <span class="w-1.5 h-1.5 bg-green-400 rounded-full animate-pulse"></span>
              AI 在线为您解答
            </p>
          </div>
        </div>
        <button @click="toggleChat" class="hover:bg-white/20 p-1.5 rounded-full transition-colors">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
        </button>
      </div>

      <!-- Messages Area -->
      <div class="flex-grow p-4 overflow-y-auto space-y-4 bg-slate-50/50" ref="messagesContainer">
        <!-- Default Welcome -->
        <div class="flex gap-3">
          <div class="w-8 h-8 rounded-full bg-amber-700 shrink-0 flex items-center justify-center shadow-sm">
            <span class="text-white text-xs font-bold">AI</span>
          </div>
          <div class="bg-white border border-slate-100 text-slate-700 px-4 py-2.5 rounded-2xl rounded-tl-sm text-sm shadow-sm leading-relaxed max-w-[85%]">
            你好！我是宇晗烘焙的专属 AI 客服。你想了解本店的招牌软欧包、定制生日蛋糕，还是想知道怎么预定呢？🎂
          </div>
        </div>

        <!-- Chat History -->
        <div 
          v-for="(msg, index) in messages" 
          :key="index"
          class="flex gap-3"
          :class="msg.role === 'user' ? 'flex-row-reverse' : ''"
        >
          <!-- Avatar -->
          <div 
            class="w-8 h-8 rounded-full shrink-0 flex items-center justify-center shadow-sm"
            :class="msg.role === 'user' ? 'bg-slate-800' : 'bg-amber-700'"
          >
            <span class="text-white text-xs font-bold">{{ msg.role === 'user' ? 'Me' : 'AI' }}</span>
          </div>
          <!-- Message bubble -->
          <div 
            class="px-4 py-2.5 rounded-2xl text-sm shadow-sm leading-relaxed max-w-[85%] whitespace-pre-wrap break-words"
            :class="msg.role === 'user' 
              ? 'bg-slate-800 text-white rounded-tr-sm' 
              : 'bg-white border border-slate-100 text-slate-700 rounded-tl-sm'"
          >
            {{ msg.content }}
            <span v-if="msg.isStreaming" class="inline-block w-1 h-4 bg-amber-700 ml-1 animate-pulse align-middle"></span>
          </div>
        </div>
      </div>

      <!-- Input Area -->
      <div class="p-3 bg-white border-t border-slate-100">
        <div class="relative flex items-center">
          <input 
            v-model="inputMsg" 
            @keyup.enter="sendMessage"
            type="text" 
            placeholder="问问 AI 热门甜点..." 
            class="w-full bg-slate-100 border-none rounded-full py-3 pl-4 pr-12 text-sm text-slate-800 focus:outline-none focus:ring-2 focus:ring-amber-700/30 transition-all placeholder:text-slate-400"
            :disabled="isGenerating"
          />
          <button 
            @click="sendMessage"
            :disabled="!inputMsg.trim() || isGenerating"
            class="absolute right-1 w-9 h-9 bg-amber-700 rounded-full flex items-center justify-center text-white disabled:opacity-50 disabled:bg-slate-400 transition-colors cursor-pointer"
          >
            <svg class="w-4 h-4 ml-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"></path></svg>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'

const isOpen = ref(false)
const inputMsg = ref('')
const messages = ref([])
const isGenerating = ref(false)
const messagesContainer = ref(null)

const toggleChat = () => {
  isOpen.value = !isOpen.value
  if (isOpen.value) {
    scrollToBottom()
  }
}

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

const sendMessage = async () => {
  const text = inputMsg.value.trim()
  if (!text || isGenerating.value) return

  // 记录用户消息
  messages.value.push({ role: 'user', content: text })
  inputMsg.value = ''
  isGenerating.value = true
  scrollToBottom()

  // 准备 AI 消息插槽
  const aiMessage = { role: 'ai', content: '', isStreaming: true }
  messages.value.push(aiMessage)

  try {
    const response = await fetch('/api/customer/ai/chat', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ message: text })
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    const reader = response.body.getReader()
    const decoder = new TextDecoder('utf-8')
    let buffer = ''

    while (true) {
      const { done, value } = await reader.read()
      if (done) break

      buffer += decoder.decode(value, { stream: true })
      
      // 按 \n\n 切割事件块
      const parts = buffer.split('\n\n')
      // 最后一个可能不完整，保留到下一次
      buffer = parts.pop()

      for (const part of parts) {
        if (part.startsWith('data:')) {
          // 由于大模型返回的文本可能包含换行，后端 SseEmitter 会把每行都加上 data: 前缀
          // 所以我们需要将该数据块中每一行开头的 data: 都剔除掉
          const dataContent = part.replace(/^data:/gm, '')
          // 逐字累加显示
          aiMessage.content += dataContent
          scrollToBottom()
        }
      }
    }
  } catch (error) {
    console.error('AI Request Error:', error)
    aiMessage.content += '\n[网络异常，无法连接到 AI 引擎]'
  } finally {
    aiMessage.isStreaming = false
    isGenerating.value = false
    scrollToBottom()
  }
}
</script>

<style scoped>
.animate-bounce-slow {
  animation: float 3s ease-in-out infinite;
}
@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}
</style>
