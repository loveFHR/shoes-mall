import request from '@/utils/request.js'

export const add = (data) => {
	return request({
		url: `/order/add`,
		method: 'POST',
		data
	})
}

export const list = (current, page, params) => {
	return request({
		url: `/order/query/${current}/${page}`,
		method: 'POST',
		params
	})
}

// 删除订单
export const del = (data) => {
	return request({
		url: `/order/delete`,
		method: 'DELETE',
		data
	})
}

// 批量收货
export const receive = (params) => {
	return request({
		url: `/order/receive`,
		method: 'POST',
		params
	})
}

// 批量取消订单
export const cancel = (params) => {
	return request({
		url: `/order/cancel`,
		method: 'POST',
		params
	})
}

// 批量申请退款
export const applyRefund = (params) => {
	return request({
		url: `/order/applyRefund`,
		method: 'POST',
		params
	})
}

export const orderNum = () => {
	return request({
		url: `order/myOrderNum`,
		method: 'GET'
	})
}
