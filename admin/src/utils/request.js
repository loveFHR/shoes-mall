import axios from 'axios'
import router from '@/router'
import { getToken, removeToken } from './auth.js'
import useMessage from '@/hooks/useMessage'

const errorMap = {
	400: '请求报文中存在语法错误',
	401: '登录失效，请重新登录',
	403: '请求被服务器拒绝了',
	404: '未找到资源',
	405: '不支持该Request的方法',
	500: '服务器遇到一个错误，使其无法为请求提供服务',
	501: '客户端发起的请求超出服务器的能力范围',
	502: '代理使用的服务器遇到了上游的无效响应',
	503: '服务器处于超负载或正在停机维护，无法处理请求',
	504: '网关超时，服务器作为网关或代理，但是没有及时从上游服务器收到请求',
	505: '服务器收到的请求使用了它不支持的HTTP协议版本'
}

const service = axios.create({
	baseURL: '/api',
	timeout: 15000
})

service.interceptors.request.use(
	(config) => {
		if (getToken()) {
			config.headers['token'] = getToken()
		}
		return config
	},
	(error) => {
		return Promise.reject(error)
	}
)

service.interceptors.response.use(
	async (response) => {
		const { error } = useMessage()
		const res = response.data
		if (res.code === 0) {
			return res
		} else {
			// if (res.status === 4006) {
			// 	removeToken()
			// 	router.replace('/login')
			// }
			error(res.message)
			return Promise.reject(new Error(res.message || 'Error'))
		}
	},
	async ({ response }) => {
		const { error } = useMessage()
		const res = response || {}
		if (!errorMap[res.status]) {
			res.message = '系统错误，请稍后重试'
		} else {
			res.message = `错误状态码：${res.status}，${errorMap[res.status] || '系统错误'}`
		}
		error(res.message)
		return Promise.reject(res)
	}
)

export default service
