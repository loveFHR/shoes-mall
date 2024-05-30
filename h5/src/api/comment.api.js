import request from '@/utils/request.js'

export const add = (data) => {
	return request({
		url: `/comment/do`,
		method: 'POST',
		data
	})
}

export const del = (id) => {
	return request({
		url: `/comment/delete/${id}`,
		method: 'POST'
	})
}
