<template>
	<div class="page login">
		<NavBar title="登录" arrowLeft />
		<div class="form">
			<div class="title">欢迎回来！</div>
			<div class="form-item">
				<div class="label">邮箱地址</div>
				<input type="text" v-model="state.form.email" placeholder="请输入邮箱地址" />
			</div>
			<div class="form-item mt40">
				<div class="label">密码</div>
				<input type="password" v-model="state.form.password" placeholder="请输入密码" />
			</div>
			<div class="btn" @click="onLoginClick">登录</div>
			<div class="action">还没有账号? <span @click="onRegisterClick">立即注册</span></div>
		</div>
	</div>
</template>

<script setup>
import useLoading from '@/hooks/useLoading'
import useToast from '@/hooks/useToast'
import useUserStore from '@/store/modules/user'

const router = useRouter()
const { loading, closeLoading } = useLoading()
const { toast } = useToast()
const useUser = useUserStore()

const state = reactive({
	form: {
		email: '',
		password: '',
		admin: 0
	}
})

const onLoginClick = async () => {
	if (!state.form.email) {
		return toast('请输入邮箱地址')
	}
	if (!state.form.password) {
		return toast('请输入密码')
	}
	try {
		loading('登录中...')
		await useUser.onLogin(state.form)
		toast('登录成功')
		setTimeout(() => {
			router.go(-1)
		}, 1500)
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

const onRegisterClick = () => {
	router.push({
		path: '/register'
	})
}
</script>

<style lang="scss" scoped>
.login {
	background-color: #ffffff;
	.form {
		width: 646px;
		margin: 0 auto;
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		.title {
			font-size: 54px;
			color: #333333;
			font-weight: 500;
			margin-bottom: 100px;
		}
		.form-item {
			width: 100%;
			background-color: #efefef;
			border-radius: 14px;
			padding: 20px;
			box-sizing: border-box;
			.label {
				margin-bottom: 18px;
				font-size: 28px;
				color: #999999;
			}
			input {
				width: 100%;
				font-size: 30px;
				color: #1f1f1f;
				padding: 0;
				border: 0 !important;
				background: transparent;
				&::placeholder {
					color: #999999;
				}
			}
		}
		.btn {
			width: 100%;
			height: 100px;
			font-size: 30px;
			color: #ffffff;
			border-radius: 20px;
			margin-top: 100px;
			background-color: rgb(255, 98, 0);
			text-align: center;
			line-height: 100px;
		}
		.action {
			margin-top: 20px;
			font-size: 26px;
			text-align: right;
			color: #999999;
			span {
				color: #409eff;
			}
		}
	}
}
</style>
