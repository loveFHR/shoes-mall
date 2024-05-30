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

// 发送验证码
export const sendCode = (params) => {
	return request({
		url: '/user/verifyCode',
		method: 'POST',
		params
	})
}

// 注册
export const register = (data) => {
	return request({
		url: '/user/register',
		method: 'POST',
		data
	})
}

// 修改信息
export const edit = (data) => {
	return request({
		url: '/user/update/my',
		method: 'POST',
		data
	})
}

// 修改密码
export const editPassword = (data) => {
	return request({
		url: '/user/resetPwd',
		method: 'POST',
		data
	})
}

// 通过邮箱修改密保
export const resetSecurity = (data) => {
	return request({
		url: '/user/resetSecurity',
		method: 'POST',
		data
	})
}
