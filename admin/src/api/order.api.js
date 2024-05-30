import request from '@/utils/request.js'

// 订单列表
export const list = (params) => {
	const { current, page, type, name } = params
	return request({
		url: `/order/query/${current}/${page}`,
		method: 'POST',
		params: {
			type,
			name,
			findMyOrder: 0
		}
	})
}

// 批量发货
export const send = (params) => {
	return request({
		url: `/order/send`,
		method: 'POST',
		params
	})
}

// 退款
export const refund = (data) => {
	return request({
		url: `/pay/refund`,
		method: 'POST',
		data
	})
}

export const getInfo = (id) => {
	return request({
		url: `order/orderDetail/${id}`,
		method: 'GET'
	})
}

export const comment = (data) => {
	return request({
		url: `/comment/do`,
		method: 'POST',
		data
	})
}
