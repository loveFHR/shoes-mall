import { createRouter, createWebHistory } from 'vue-router'

import Layout from '@/layout/index.vue'
import Login from '@/views/login/index.vue'

import product from './modules/product.js'
import order from './modules/order.js'
import user from './modules/user.js'
import other from './modules/other.js'
import statistics from './modules/statistics.js'

export const constantRouterMap = [
	{
		path: '/login',
		hidden: true,
		component: Login,
		meta: {
			title: '登陆'
		}
	},
	{
		path: '/',
		redirect: '/dashboard',
		component: Layout,
		alwaysShow: false,
		children: [
			{
				path: '/dashboard',
				meta: {
					title: '首页',
					icon: 'icon-home',
					isHide: false
				},
				component: () => import('@/views/dashboard/index.vue')
			}
		]
	},
	product,
	order,
	user,
	statistics,
	other
]

export const notFoundAndNoPower = [
	{
		path: '/:pathMatch(.*)*',
		redirect: '/404'
	},
	{
		path: '/404',
		component: () => import('@/views/error/404.vue'),
		meta: {
			title: '页面找不到',
			isHide: true
		}
	}
]

export const router = createRouter({
	routes: [...constantRouterMap],
	history: createWebHistory()
})

export default router
