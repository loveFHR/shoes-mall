import { createApp } from 'vue'
import router from './router'
import pinia from './store'
import './styles/index.scss'
import 'vant/es/dialog/style'
import Component from './components'
import App from './App.vue'
import 'virtual:svg-icons-register'

const app = createApp(App)
app.use(router)
app.use(pinia)
app.use(Component)
app.mount('#app')
