import request from '@/utils/request.js'

export const list = (params) => {
	const { current, page, companyName } = params
	return request({
		url: `/ad/page/${current}/${page}`,
		method: 'POST',
		params: {
			companyName
		}
	})
}

export const add = (data) => {
	return request({
		url: `/ad/add`,
		method: 'POST',
		data
	})
}

export const edit = (data) => {
	return request({
		url: `/ad/update`,
		method: 'POST',
		data
	})
}

export const del = (id) => {
	return request({
		url: `/ad/delete/${id}`,
		method: 'POST'
	})
}

export const batchDel = (params) => {
	return request({
		url: `/ad/delete`,
		method: 'DELETE',
		params
	})
}
