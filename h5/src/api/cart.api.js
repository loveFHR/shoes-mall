import request from '@/utils/request.js'

export const list = () => {
	return request({
		url: `/shoppingCart/query`,
		method: 'POST'
	})
}

export const addCart = (data) => {
	return request({
		url: `/shoppingCart/add`,
		method: 'POST',
		data
	})
}

export const editCart = (data) => {
	return request({
		url: `/shoppingCart/update`,
		method: 'POST',
		data
	})
}

export const del = (id) => {
	return request({
		url: `shoppingCart/delete/${id}`,
		method: 'DELETE'
	})
}
