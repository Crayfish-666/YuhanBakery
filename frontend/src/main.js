import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// 引入全局样式及 Tailwind CSS 注入
import './style.css'

// 引入 Element Plus (PC 管理端框架)
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 引入 Vant 4 (移动端框架)
import Vant from 'vant'
import 'vant/lib/index.css'

const app = createApp(App)

app.use(store)
app.use(router)
app.use(ElementPlus)
app.use(Vant)

app.mount('#app')
