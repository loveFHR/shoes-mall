<template>
	<div class="page my">
		<div class="item user" @click="onUserClick">
			<img :src="userInfo.avatar || 'http://139.9.181.192:9000/shoes-mall/202405251716646715774.png'" class="avatar" />
			<div class="info">
				<div class="no-login" v-if="Object.keys(userInfo).length == 0">点击登录</div>
				<template v-else>
					<div class="nickname">{{ userInfo.username }}</div>
					<div class="phone">{{ userInfo.phone }}</div>
				</template>
			</div>
		</div>
		<div class="item">
			<div class="item-header">
				<div class="title">我的订单</div>
				<div class="right" @click="onGoPage('/order/list')">查看全部<van-icon name="arrow" /></div>
			</div>
			<div class="item-content">
				<div class="status" @click="onGoPage('/order/list', { type: 0 })">
					<svg-icon name="icon-dfk" :size="60" />
					<div class="text">待付款</div>
					<div class="badge" v-if="waitPay">{{ waitPay }}</div>
				</div>
				<div class="status" @click="onGoPage('/order/list', { type: 2 })">
					<svg-icon name="icon-dfh" :size="60" />
					<div class="text">待发货</div>
					<div class="badge" v-if="waitSend">{{ waitSend }}</div>
				</div>
				<div class="status" @click="onGoPage('/order/list', { type: 3 })">
					<svg-icon name="icon-dsh" :size="60" />
					<div class="text">待收货</div>
					<div class="badge" v-if="waitReceive">{{ waitReceive }}</div>
				</div>
				<div class="status" @click="onGoPage('/order/list', { type: 4 })">
					<svg-icon name="icon-pj" :size="60" />
					<div class="text">待评价</div>
					<div class="badge" v-if="waitComment">{{ waitComment }}</div>
				</div>
			</div>
		</div>

		<div class="list-box">
			<van-cell title="修改个人信息" is-link :clickable="false" @click="onGoPage('/user/edit')" />
			<van-cell title="修改密码" is-link :clickable="false" @click="onGoPage('/user/edit-password')" />
			<van-cell title="修改密保信息" is-link :clickable="false" @click="onGoPage('/user/edit-security')" />
			<van-cell title="地址管理" is-link :clickable="false" @click="onGoPage('/address/list')" />
		</div>

		<div class="btn" v-if="getToken()" @click="onLoginOutClick">退出登录</div>
	</div>
</template>

<script setup>
import { showConfirmDialog } from 'vant'
import useUserStore from '@/store/modules/user'
import { getToken, isLogin } from '@/utils/auth.js'
import { orderNum } from '@/api/order.api.js'

const router = useRouter()
const useUser = useUserStore()
const { userInfo } = storeToRefs(useUser)
const waitComment = ref(0)
const waitPay = ref(0)
const waitReceive = ref(0)
const waitSend = ref(0)

onMounted(() => {
	if (getToken()) {
		queryOrderNum()
	}
})

const queryOrderNum = async () => {
	try {
		const { data } = await orderNum()
		waitComment.value = data.waitComment || 0
		waitPay.value = data.waitPay || 0
		waitReceive.value = data.waitReceive || 0
		waitSend.value = data.waitSend || 0
	} catch (error) {
		console.log(error)
	}
}

const onGoPage = async (path, query) => {
	try {
		await isLogin()
		await router.push({
			path,
			query
		})
	} catch (error) {
		console.log(error)
	}
}

const onUserClick = () => {
	if (!getToken()) {
		router.push({
			path: '/login'
		})
	}
}

const onLoginOutClick = async () => {
	try {
		await showConfirmDialog({
			title: '警告',
			message: '确定要退出登录吗?'
		})
		await useUser.onLoginOut()
		router.replace('/home')
	} catch (error) {
		console.log(error)
	}
}
</script>

<style lang="scss" scoped>
.my {
	.no-login {
		font-size: 32px;
		font-weight: 500;
	}
	.item {
		width: 686px;
		margin: 0 auto;
		padding: 30px;
		box-sizing: border-box;
		margin-top: 30px;
		background-color: #ffffff;
		border-radius: 12px;
		.item-header {
			display: flex;
			align-items: center;
			justify-content: space-between;
			.title {
				font-size: 28px;
				font-weight: 500;
			}
			.right {
				font-size: 26px;
				color: #999999;
				font-weight: 400;
			}
		}
		.item-content {
			margin-top: 40px;
			display: flex;
			align-items: center;
			.status {
				flex: 1;
				display: flex;
				flex-direction: column;
				align-items: center;
				position: relative;
				.badge {
					width: 32px;
					height: 32px;
					font-size: 24px;
					text-align: center;
					line-height: 32px;
					color: #ffffff;
					position: absolute;
					top: -20px;
					right: 0;
					background-color: #f00;
					border-radius: 100%;
				}
				.text {
					font-size: 26px;
					margin-top: 20px;
					color: #666666;
				}
			}
		}
	}
	.user {
		display: flex;
		align-items: center;
		.avatar {
			width: 120px;
			height: 120px;
			border-radius: 100%;
			object-fit: cover;
			margin-right: 30px;
		}
		.nickname {
			font-size: 32px;
			font-weight: 500;
		}
		.phone {
			margin-top: 14px;
			font-size: 26px;
			color: #999999;
		}
	}
	.list-box {
		width: 686px;
		margin: 0 auto;
		padding: 20px 0;
		margin-top: 20px;
		border-radius: 12px;
		background-color: #ffffff;
		overflow: hidden;
	}
	.btn {
		width: 686px;
		height: 100px;
		border-radius: 16px;
		color: #ffffff;
		text-align: center;
		line-height: 100px;
		font-size: 28px;
		position: fixed;
		bottom: 140px;
		left: 32px;
		background: linear-gradient(116deg, #56a2fe 0%, #2972ff 100%);
	}
}
</style>
