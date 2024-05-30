import request from '@/utils/request.js'

export const list = () => {
	return request({
		url: `/user/query`,
		method: 'POST'
	})
}

export const add = (data) => {
	return request({
		url: `/user/add`,
		method: 'POST',
		data
	})
}

export const edit = (data) => {
	return request({
		url: `/user/update`,
		method: 'POST',
		data
	})
}

export const del = (id) => {
	return request({
		url: `/user/delete/${id}`,
		method: 'POST'
	})
}

export const info = (id) => {
	return request({
		url: `/user/query/${id}`,
		method: 'GET'
	})
}

export const setDefault = (id) => {
	return request({
		url: `/user/update/${id}`,
		method: 'POST'
	})
}

export const getDefault = () => {
	return request({
		url: `/user/query/default`,
		method: 'POST'
	})
}
