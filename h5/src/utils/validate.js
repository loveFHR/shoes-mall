const regEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/
const regPhone = /^1[3456789]\d{9}$/

export const validateEmail = (val) => {
	return new Promise((resolve, reject) => {
		if (!regEmail.test(val)) {
			reject(new Error('请输入正确的邮箱地址'))
		} else {
			resolve()
		}
	})
}

export const validatePhone = (val) => {
	return new Promise((resolve, reject) => {
		if (!val) {
			reject(new Error('手机号不能为空'))
		} else {
			if (!regPhone.test(val)) {
				reject(new Error('请输入正确的手机号'))
			} else {
				resolve()
			}
		}
	})
}
