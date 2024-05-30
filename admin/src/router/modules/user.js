import Layout from '@/layout/index.vue'

export default {
	path: '/user',
	component: Layout,
	alwaysShow: true,
	meta: {
		title: '用户管理',
		icon: 'icon-user'
	},
	children: [
		{
			path: '/user/list',
			meta: {
				title: '用户列表'
			},
			component: () => import('@/views/user/list/index.vue')
		}
	]
}
