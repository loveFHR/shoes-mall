import Layout from '@/layout/index.vue'

export default {
	path: '/statistics',
	component: Layout,
	alwaysShow: true,
	meta: {
		title: '查看报表',
		icon: 'icon-count'
	},
	children: [
		{
			path: '/statistics/performance',
			meta: {
				title: '业绩报表'
			},
			component: () => import('@/views/statistics/performance/index.vue')
		},
		{
			path: '/statistics/marketing',
			meta: {
				title: '营销报表'
			},
			component: () => import('@/views/statistics/marketing/index.vue')
		},
		{
			path: '/statistics/flow',
			meta: {
				title: '流量报表'
			},
			component: () => import('@/views/statistics/flow/index.vue')
		}
	]
}
