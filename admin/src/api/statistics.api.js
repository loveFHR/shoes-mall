import request from '@/utils/request'

export const order = () => {
	return request({
		url: '/statistic/order',
		method: 'GET'
	})
}

export const type = () => {
	return request({
		url: '/statistic/type',
		method: 'GET'
	})
}
export const product = () => {
	return request({
		url: '/statistic/product',
		method: 'GET'
	})
}
