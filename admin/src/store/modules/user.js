import { defineStore } from 'pinia'
import { getToken, setToken } from '@/utils/auth.js'
import { login, info } from '@/api/user.api.js'

export const useUserStore = defineStore({
	id: 'app-user',
	state: () => {
		return {
			userInfo: {},
			token: getToken(),
			botTypeList: []
		}
	},
	actions: {
		onLogin(data) {
			return new Promise((resolve, reject) => {
				login(data)
					.then((res) => {
						setToken(res.data.token)
						resolve(res)
					})
					.catch((err) => {
						reject(err)
					})
			})
		},
		onQueryUserInfo() {
			return new Promise((resolve, reject) => {
				info()
					.then((res) => {
						this.userInfo = res.data
						resolve(res)
					})
					.catch((error) => {
						reject(error)
					})
			})
		}
	},
	persist: {
		enabled: true,
		strategies: [{ storage: localStorage, paths: ['userInfo', 'botTypeList'] }]
	}
})

export default useUserStore
