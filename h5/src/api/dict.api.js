import request from '@/utils/request.js'

// 添加字典 1-鞋码 2-颜色 3-品牌 4-鞋类型
export const add = (data) => {
	return request({
		url: '/systemBaseSetting/add',
		method: 'POST',
		data
	})
}

// 修改
export const edit = (data) => {
	return request({
		url: '/systemBaseSetting/update',
		method: 'POST',
		data
	})
}

// 删除
export const del = (id) => {
	return request({
		url: `/systemBaseSetting/${id}`,
		method: 'DELETE'
	})
}

// 批量删除
export const batchDel = (params) => {
	return request({
		url: `/systemBaseSetting/delete`,
		method: 'DELETE',
		params
	})
}

// 查询 1-鞋码 2-颜色 3-品牌 4-鞋类型
export const list = (params) => {
	return request({
		url: `/systemBaseSetting/${params.type}`,
		method: 'GET',
		params
	})
}

// 禁用启用

export const updateStatus = (params) => {
	return request({
		url: `/systemBaseSetting/ban`,
		method: 'POST',
		params
	})
}
