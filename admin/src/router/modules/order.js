import Layout from '@/layout/index.vue'

export default {
	path: '/order',
	component: Layout,
	alwaysShow: true,
	meta: {
		title: '订单管理',
		icon: 'icon-order'
	},
	children: [
		{
			path: '/order/waiting',
			meta: {
				title: '待发货订单'
			},
			component: () => import('@/views/order/waiting/index.vue')
		},
		{
			path: '/order/transport',
			meta: {
				title: '运输中订单'
			},
			component: () => import('@/views/order/transport/index.vue')
		},
		{
			path: '/order/success',
			meta: {
				title: '交易完成订单'
			},
			component: () => import('@/views/order/success/index.vue')
		},
		{
			path: '/order/apply-refund',
			meta: {
				title: '申请退款订单'
			},
			component: () => import('@/views/order/apply-refund/index.vue')
		},
		{
			path: '/order/return',
			meta: {
				title: '已退货订单'
			},
			component: () => import('@/views/order/return/index.vue')
		},
		{
			path: '/order/cancel',
			meta: {
				title: '已取消订单'
			},
			component: () => import('@/views/order/cancel/index.vue')
		},
		{
			path: '/order/message',
			meta: {
				title: '待回复评价订单'
			},
			component: () => import('@/views/order/message/index.vue')
		}
	]
}
