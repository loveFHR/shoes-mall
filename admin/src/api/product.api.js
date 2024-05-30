import request from '@/utils/request.js'

// 商品列表
export const list = (current, page, data) => {
	return request({
		url: `/product/query/${current}/${page}`,
		method: 'POST',
		data
	})
}

// 添加商品
export const add = (data) => {
	return request({
		url: `/product/add`,
		method: 'POST',
		data
	})
}

// 商品上下架
export const updateStatus = (params) => {
	return request({
		url: `/product/onDown`,
		method: 'POST',
		params
	})
}

// 商品详情
export const info = (id) => {
	return request({
		url: `/product/detail/${id}`,
		method: 'GET'
	})
}

// 修改商品
export const edit = (data) => {
	return request({
		url: `/product/update`,
		method: 'POST',
		data
	})
}

// 批量删除
export const batchDel = (params) => {
	return request({
		url: `/product/delete`,
		method: 'DELETE',
		params
	})
}
