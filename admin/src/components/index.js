// 注册全局组件
import { defineAsyncComponent } from 'vue'
import DTable from './DTable/index.vue'

const SvgIcon = defineAsyncComponent(() => import('@/components/SvgIcon/index.vue'))
const Pagination = defineAsyncComponent(() => import('@/components/Pagination/index.vue'))
const DialogPopup = defineAsyncComponent(() => import('@/components/DialogPopup/index.vue'))

export default {
	install(app) {
		app.component('SvgIcon', SvgIcon)
		app.component('Pagination', Pagination)
		app.component('DTable', DTable)
		app.component('DialogPopup', DialogPopup)
	}
}
