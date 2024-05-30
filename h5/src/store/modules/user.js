import { defineStore } from 'pinia'
import { setToken, removeToken } from '@/utils/auth.js'
import { login, register, info } from '@/api/user.api.js'

const userStore = defineStore('user', {
	state: () => {
		return {
			userInfo: {}
		}
	},
	actions: {
		onLogin(data) {
			return new Promise((resolve, reject) => {
				login(data)
					.then((res) => {
						setToken(res.data.token)
						this.userInfo = res.data
						resolve(res)
					})
					.catch((err) => {
						reject(err)
					})
			})
		},
		onGetUserInfo() {
			return new Promise((resolve, reject) => {
				info()
					.then((res) => {
						this.userInfo = res.data
						resolve(res)
					})
					.catch((err) => {
						reject(err)
					})
			})
		},
		onRegister(data) {
			return new Promise((resolve, reject) => {
				register(data)
					.then((res) => {
						setToken(res.data.token)
						this.userInfo = res.data
						resolve(res)
					})
					.catch((err) => {
						reject(err)
					})
			})
		},
		onLoginOut() {
			return new Promise((resolve) => {
				this.userInfo = {}
				localStorage.removeItem('ProductInfo')
				removeToken()
				resolve()
			})
		}
	},
	persist: {
		enabled: true,
		strategies: [{ storage: localStorage, paths: ['userInfo'] }]
	}
})

export default userStore
