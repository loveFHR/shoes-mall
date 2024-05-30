import { createWebHistory, createRouter } from 'vue-router'

import Layout from '@/layout/index.vue'

const routes = [
	{
		path: '/',
		redirect: '/home'
	},
	{
		path: '/',
		name: 'Home',
		redirect: '/home',
		component: Layout,
		children: [
			{
				path: '/home',
				name: 'Home',
				component: () => import('@/views/home/index.vue'),
				meta: {
					title: '澎湃鞋城',
					isAuth: true
				}
			},
			{
				path: '/cart',
				name: 'Cart',
				component: () => import('@/views/cart/index.vue'),
				meta: {
					title: '购物车',
					isAuth: false
				}
			},
			{
				path: '/my',
				name: 'My',
				component: () => import('@/views/my/index.vue'),
				meta: {
					title: '个人中心',
					isAuth: true
				}
			}
		]
	},
	{
		path: '/login',
		meta: {
			name: 'Login',
			title: '用户登陆'
		},
		component: () => import('@/views/login/index.vue')
	},
	{
		path: '/register',
		meta: {
			name: 'Register',
			title: '用户注册'
		},
		component: () => import('@/views/register/index.vue')
	},
	{
		path: '/register/agreement',
		meta: {
			name: 'RegisterAgreement',
			title: '用户协议'
		},
		component: () => import('@/views/register/agreement.vue')
	},
	{
		path: '/product/:id',
		meta: {
			name: 'Product',
			title: '商品详情'
		},
		component: () => import('@/views/product/index.vue')
	},
	{
		path: '/product/purchase',
		meta: {
			name: 'purchase',
			title: '支付'
		},
		component: () => import('@/views/purchase/index.vue')
	},
	{
		path: '/address/list',
		meta: {
			name: 'AddressList',
			title: '我的地址'
		},
		component: () => import('@/views/address/index.vue')
	},
	{
		path: '/address/add',
		meta: {
			name: 'AddressAdd',
			title: '新增地址'
		},
		component: () => import('@/views/address/add.vue')
	},
	{
		path: '/address/edit',
		meta: {
			name: 'AddressEdit',
			title: '修改地址'
		},
		component: () => import('@/views/address/edit.vue')
	},
	{
		path: '/order/list',
		meta: {
			name: 'OrderList',
			title: '我的订单'
		},
		component: () => import('@/views/order/index.vue')
	},
	{
		path: '/user/edit',
		meta: {
			name: 'UserEdit',
			title: '个人中心'
		},
		component: () => import('@/views/user/edit.vue')
	},
	{
		path: '/user/edit-password',
		meta: {
			name: 'UserEditPassword',
			title: '修改密码'
		},
		component: () => import('@/views/user/edit-password.vue')
	},
	{
		path: '/user/edit-security',
		meta: {
			name: 'UserEditSecurity',
			title: '修改密保信息'
		},
		component: () => import('@/views/user/edit-security.vue')
	},
	{
		path: '/comment',
		meta: {
			name: 'Comment',
			title: '评论'
		},
		component: () => import('@/views/comment/index.vue')
	},
	{
		path: '/product/purchase/success',
		meta: {
			name: 'Success',
			title: '支付成功'
		},
		component: () => import('@/views/purchase/success.vue')
	}
]

const router = createRouter({
	routes: routes,
	history: createWebHistory()
})

export default router
