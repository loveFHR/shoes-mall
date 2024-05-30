<template>
	<div class="login">
		<div class="form-box">
			<div class="title">澎湃鞋城后台管理系统</div>
			<el-form :model="state.form" ref="formRef" :rules @keyup.enter="onLoginClick">
				<el-form-item prop="email">
					<el-input v-model="state.form.email" placeholder="请输入账号" clearable />
				</el-form-item>
				<el-form-item prop="password">
					<el-input v-model="state.form.password" show-password type="password" placeholder="请输入密码" clearable />
				</el-form-item>
				<el-form-item>
					<el-button class="login-btn" type="primary" :loading @click="onLoginClick">{{ loginText }}</el-button>
				</el-form-item>
			</el-form>
		</div>
	</div>
</template>

<script setup>
import useMessage from '@/hooks/useMessage'
import useUserStore from '@/store/modules/user'

const router = useRouter()
const useUser = useUserStore()
const { success } = useMessage()

const formRef = ref(null)
const loading = ref(false)
const loginText = ref('登录')
const state = reactive({
	form: {
		email: '',
		password: '',
		admin: 1
	}
})

const rules = ref({
	email: [{ required: true, message: '请输入邮箱地址' }],
	password: [{ required: true, message: '请输入密码' }]
})

const onLoginClick = async () => {
	try {
		await formRef.value.validate()
		loading.value = true
		loginText.value = '登录中...'
		await useUser.onLogin(state.form)
		success('登录成功')
		useUser.onQueryUserInfo()
		router.push({
			path: '/'
		})
	} catch (error) {
		console.log(error)
	} finally {
		loading.value = false
		loginText.value = '登录'
	}
}
</script>

<style lang="scss" scoped>
.login {
	width: 100%;
	height: 100%;
	background-color: #efefef;
	.form-box {
		width: 400px;
		height: 300px;
		background-color: #ffffff;
		position: absolute;
		top: 40%;
		left: 50%;
		transform: translate(-50%, -50%);
		box-sizing: border-box;
		padding: 30px;
		border-radius: 6px;
		box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
		.title {
			text-align: center;
			font-size: 24px;
			font-weight: bold;
		}
		.el-form {
			margin-top: 60px;
			.login-btn {
				width: 100%;
				margin-top: 20px;
			}
		}
	}
}
</style>
