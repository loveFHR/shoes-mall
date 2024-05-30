<template>
	<div class="page login">
		<NavBar title="用户注册" />

		<div class="form">
			<div class="title">欢迎注册！</div>
			<div class="form-item">
				<div class="label">头像:</div>
				<van-uploader v-model="state.avatar" :max-count="1" :after-read="onUploadAvatar" />
			</div>
			<div class="form-item">
				<div class="label"><span>*</span>账号:</div>
				<input type="text" v-model="state.form.email" placeholder="请输入邮箱地址" />
			</div>
			<div class="form-item">
				<div class="label"><span>*</span>密码:</div>
				<input type="password" v-model="state.form.password" placeholder="请输入密码" />
			</div>
			<div class="form-item">
				<div class="label"><span>*</span>确认密码:</div>
				<input type="password" v-model="state.form.checkPassword" placeholder="请再次输入密码" />
			</div>
			<div class="form-item">
				<div class="label"><span>*</span>昵称:</div>
				<input type="text" v-model="state.form.username" placeholder="请输入昵称" />
			</div>
			<div class="form-item">
				<div class="label"><span>*</span>性别:</div>
				<van-radio-group v-model="state.form.gender" size="small" icon-size="16px">
					<van-radio :name="0">女</van-radio>
					<van-radio :name="1">男</van-radio>
					<van-radio :name="2">保密</van-radio>
				</van-radio-group>
			</div>
			<div class="form-item">
				<div class="label"><span>*</span>手机号:</div>
				<input type="text" v-model="state.form.phone" placeholder="请输入手机号" />
			</div>
			<div class="form-item">
				<div class="label"><span>*</span>验证码:</div>
				<input type="text" v-model="state.form.code" placeholder="请输入验证码" />
				<div class="code-btn" @click="onGetCodeClick">{{ codeTips }}</div>
			</div>
			<div class="form-item">
				<div class="label"><span>*</span>密保问题:</div>
				<input type="text" v-model="state.form.securityQuestion" placeholder="请输入密保问题" />
			</div>
			<div class="form-item">
				<div class="label"><span>*</span>密保答案:</div>
				<input type="text" v-model="state.form.securityAnswer" placeholder="请输入密保答案" />
			</div>
			<div style="margin-top: 30px">
				<van-checkbox v-model="checked" icon-size="19px"
					>已阅读并同意本站<span
						style="color: rgb(255, 98, 0)"
						@click.stop="router.push({ path: '/register/agreement' })"
						>《服务协议》</span
					></van-checkbox
				>
			</div>
			<div class="btn" @click="onRegisterClick">注册</div>
			<div class="action">已有账号? <span @click="router.go(-1)">去登录</span></div>
		</div>
	</div>
</template>

<script setup>
import useLoading from '@/hooks/useLoading'
import useToast from '@/hooks/useToast'
import { validateEmail } from '@/utils/validate.js'
import { sendCode } from '@/api/user.api.js'
import { upload } from '@/api/upload.api.js'
import useUserStore from '@/store/modules/user'

const router = useRouter()
const { loading, closeLoading } = useLoading()
const { toast } = useToast()
const useUser = useUserStore()

const codeTips = ref('获取验证码')
const num = ref(60)
const timer = ref(null)
const checked = ref(false)
const state = reactive({
	avatar: [],
	form: {
		email: '',
		password: '',
		checkPassword: '',
		token: '',
		gender: 2,
		avatar: ''
	}
})

const onRegisterClick = async () => {
	try {
		if (!checked.value) {
			return toast('请先阅读并同意本站协议')
		}
		await validateForm()
		await loading('注册中...')
		await useUser.onRegister(state.form)
		await toast('注册成功')
		router.go(-2)
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

const validateForm = () => {
	return new Promise((resolve, reject) => {
		if (!state.form.email) {
			toast('邮箱地址不能为空')
			return reject()
		}
		if (!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/.test(state.form.email)) {
			toast('请输入正确的邮箱地址')
			return reject()
		}
		if (!state.form.password) {
			toast('密码不能为空')
			return reject()
		}
		if (!state.form.checkPassword) {
			toast('确认密码不能为空')
			return reject()
		}
		if (state.form.checkPassword != state.form.password) {
			toast('两次密码不一致')
			return reject()
		}
		if (!state.form.username) {
			toast('昵称不能为空')
			return reject()
		}
		if (!state.form.phone) {
			toast('手机号不能为空')
			return reject()
		}
		if (!/^1[3456789]\d{9}$/.test(state.form.phone)) {
			toast('请输入正确的手机号')
			return reject()
		}
		if (!state.form.code) {
			toast('验证码不能为空')
			return reject()
		}
		if (!state.form.securityQuestion) {
			toast('密保问题不能为空')
			return reject()
		}
		if (!state.form.securityAnswer) {
			toast('密保答案不能为空')
			return reject()
		}
		resolve()
	})
}

const onUploadAvatar = async ({ file }) => {
	try {
		const formData = new FormData()
		formData.append('file', file)
		const { data } = await upload(formData)
		state.form.avatar = data
	} catch (error) {
		console.log(error)
	}
}

const onGetCodeClick = async () => {
	try {
		if (timer.value) return
		await validateEmail(state.form.email)
		loading('发送中...')
		const { data } = await sendCode({
			email: state.form.email,
			isRegister: 1
		})
		state.form.token = data
		codeTips.value = '60s后重试'
		onStartCountdown()
	} catch (error) {
		toast(error.message)
		console.log(error)
	} finally {
		closeLoading()
	}
}

const onStartCountdown = () => {
	timer.value = setInterval(() => {
		if (num.value == 0) {
			codeTips.value = '获取验证码'
			num.value = 60
			onStopCountdown()
			return
		}
		num.value--
		codeTips.value = `${num.value}s后重试`
	}, 1000)
}

const onStopCountdown = () => {
	clearInterval(timer.value)
	timer.value = null
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
			margin-bottom: 40px;
		}
		.form-item {
			display: flex;
			align-items: center;
			width: 100%;
			padding: 20px 0;
			border-bottom: 1px solid #efefef;
			.label {
				width: 134px;
				font-size: 28px;
				color: #999999;
				span {
					color: #f00;
				}
			}
			input {
				margin-left: 20px;
				flex: 1;
				font-size: 28px;
				color: #1f1f1f;
				padding: 0;
				border: 0 !important;
				background: transparent;
				&::placeholder {
					color: #999999;
				}
			}
			.code-btn {
				font-size: 26px;
				color: #409eff;
			}
		}
		.btn {
			width: 100%;
			height: 100px;
			font-size: 30px;
			color: #ffffff;
			border-radius: 20px;
			margin-top: 50px;
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
	.van-radio-group {
		display: flex;
		align-items: center;
		.van-radio {
			margin-right: 20px;
		}
	}
}
</style>
