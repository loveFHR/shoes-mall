import { createApp } from 'vue'
import 'normalize.css/normalize.css'
import '@/styles/index.scss'
import router from './router'
import pinia from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import directive from '@/directive/index.js'
import GlobalComponents from '@/components/index'
import App from './App.vue'
import 'virtual:svg-icons-register'
import './permission.js'

const app = createApp(App)

directive(app)
app.use(GlobalComponents)
app.use(router)
app.use(pinia)
app.use(ElementPlus)
app.mount('#app')
