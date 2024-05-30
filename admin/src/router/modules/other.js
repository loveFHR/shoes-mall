import Layout from '@/layout/index.vue'

export default {
	path: '/other',
	component: Layout,
	alwaysShow: true,
	meta: {
		title: '企宣管理',
		icon: 'icon-ad'
	},
	children: [
		{
			path: '/other/link',
			meta: {
				title: '合作链接管理'
			},
			component: () => import('@/views/other/link/index.vue')
		},
		{
			path: '/other/ad',
			meta: {
				title: '广告管理'
			},
			component: () => import('@/views/other/ad/index.vue')
		}
	]
}
