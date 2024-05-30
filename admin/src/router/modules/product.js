import Layout from '@/layout/index.vue'

export default {
	path: '/product',
	component: Layout,
	alwaysShow: true,
	meta: {
		title: '商品管理',
		icon: 'icon-product'
	},
	children: [
		{
			path: '/product/list',
			meta: {
				title: '产品列表'
			},
			component: () => import('@/views/product/list/index.vue')
		},
		{
			path: '/product/brand',
			meta: {
				title: '品牌列表'
			},
			component: () => import('@/views/product/brand/index.vue')
		},
		{
			path: '/product/type',
			meta: {
				title: '类型列表'
			},
			component: () => import('@/views/product/type/index.vue')
		},
		{
			path: '/product/color',
			meta: {
				title: '颜色列表'
			},
			component: () => import('@/views/product/color/index.vue')
		},
		{
			path: '/product/size',
			meta: {
				title: '尺码列表'
			},
			component: () => import('@/views/product/size/index.vue')
		}
	]
}
