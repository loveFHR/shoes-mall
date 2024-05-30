import { useUserStore } from '@/store/modules/user.js'

/**
 * 用户按钮权限指令
 * @directive v-auth="xxx"
 */
export default (app) => {
	app.directive('auth', {
		mounted(el, binding) {
			const stores = useUserStore()
			if (!stores.btnList.some((v) => v === binding.value)) el.parentNode.removeChild(el)
		}
	})
}
