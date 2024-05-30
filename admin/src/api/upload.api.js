import request from '@/utils/request.js'

export const upload = (data) => {
	return request({
		url: '/upload/one',
		method: 'POST',
		data
	})
}
