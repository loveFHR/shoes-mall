import { defineStore } from 'pinia'
import Cookies from 'js-cookie'

export const useCollapseStore = defineStore({
	id: 'app-collapse',
	state: () => {
		return {
			isCollapse: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : false
		}
	},
	actions: {
		updateCollapse() {
			this.isCollapse = !this.isCollapse
			if (this.isCollapse) {
				Cookies.set('sidebarStatus', 1)
			} else {
				Cookies.set('sidebarStatus', 0)
			}
		}
	},
	getters: {
		getCollapse(state) {
			return state.isCollapse
		}
	}
})

export default useCollapseStore
