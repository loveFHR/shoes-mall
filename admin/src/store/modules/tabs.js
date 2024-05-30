import { defineStore } from 'pinia'
import { getRawRoute } from '@/utils'

export const useTabsStore = defineStore({
	id: 'app-tabs',
	state: () => {
		return {
			tabList: [
				{
					title: '首页',
					path: '/dashboard'
				}
			]
		}
	},
	actions: {
		// 添加
		async addTab(route) {
			const { path } = getRawRoute(route)

			// let updateIndex = -1
			const tabHasExits = this.tabList.some((tab) => {
				// updateIndex = index
				return tab.path === path
			})
			if (!tabHasExits) {
				this.tabList.push(route)
			}
		},
		// 删除选中的单个
		async onDel(route) {
			const index = this.tabList.findIndex((v) => v.path === route.path)
			if (index !== -1) {
				this.tabList.splice(index, 1)
			}
		},
		// 删除选中的其他所有（不包括固定的首页）
		onDelOtherTab(item) {
			this.tabList = this.tabList.filter((v) => v.path === item.path || v.path === '/dashboard')
		},
		onDelAllTab() {
			this.tabList = []
			this.tabList.push({
				title: '首页',
				path: '/dashboard'
			})
		}
	},
	getters: {
		getTabList() {
			return this.tabList
		}
	}
})

export default useTabsStore
