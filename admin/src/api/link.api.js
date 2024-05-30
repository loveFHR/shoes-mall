import request from '@/utils/request.js'

export const list = (params) => {
	const { current, page, companyName } = params
	return request({
		url: `/cooperation/page/${current}/${page}`,
		method: 'POST',
		params
	})
}

export const add = (data) => {
	return request({
		url: `/cooperation/add`,
		method: 'POST',
		data
	})
}

export const edit = (data) => {
	return request({
		url: `/cooperation/update`,
		method: 'POST',
		data
	})
}

export const del = (id) => {
	return request({
		url: `/cooperation/delete/${id}`,
		method: 'POST'
	})
}

export const batchDel = (params) => {
	return request({
		url: `/cooperation/delete`,
		method: 'DELETE',
		params
	})
}
