import request from '@/utils/request.js'

export const list = () => {
	return request({
		url: `/ad/page/1/1000`,
		method: 'POST'
	})
}
