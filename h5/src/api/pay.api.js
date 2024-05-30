import request from '@/utils/request.js'

export const alipay = (data) => {
	return request({
		url: `/pay/alipay`,
		method: 'POST',
		data
	})
}
