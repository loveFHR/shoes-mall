import request from '@/utils/request.js'

// 商品列表
export const list = (current, page, data) => {
	return request({
		url: `/product/query/${current}/${page}`,
		method: 'POST',
		data
	})
}

// 商品详情
export const info = (id) => {
	return request({
		url: `/product/detail/${id}`,
		method: 'GET'
	})
}

// 添加购物车
export const addCart = (data) => {
	return request({
		url: `/shoppingCart/add`,
		method: 'POST',
		data
	})
}
