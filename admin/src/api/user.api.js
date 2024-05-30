import request from '@/utils/request.js'

// 登录
export const login = (data) => {
	return request({
		url: '/user/login',
		method: 'POST',
		data
	})
}

// 获取登录者信息
export const info = () => {
	return request({
		url: '/user/get/login'
	})
}

export const list = (params) => {
	const { name, current, page } = params
	return request({
		url: `/user/query/${current}/${page}`,
		method: 'POST',
		params: {
			name
		}
	})
}

export const changeStatus = (params) => {
	return request({
		url: `/user/changeStatus`,
		method: 'POST',
		params
	})
}

export const resetPass = (userId) => {
	return request({
		url: `/user/reset/${userId}`,
		method: 'POST'
	})
}
