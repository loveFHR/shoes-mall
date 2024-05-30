import Cookies from 'js-cookie'
import router from '@/router'

const __TOKEN = 'token'

// 设置token
export const setToken = (value) => {
	Cookies.set(__TOKEN, value)
}

// 获取token
export const getToken = () => {
	return Cookies.get(__TOKEN)
}

// 删除token
export const removeToken = () => {
	Cookies.remove(__TOKEN)
}

export const setItem = (key, value) => {
	Cookies.set(key, value)
}

export const getItem = (key) => {
	return Cookies.get(key)
}

export const isLogin = () => {
	return new Promise((resolve, reject) => {
		if (!getToken()) {
			router.push({
				path: '/login'
			})
			reject()
		} else {
			resolve()
		}
	})
}
