<template>
	<div class="page">
		<NavBar title="修改密保信息" />
		<div class="content">
			<van-form @submit="onSubmit">
				<van-cell-group inset>
					<van-field
						required
						disabled
						v-model="state.form.email"
						name="email"
						label="邮箱地址"
						placeholder="邮箱地址"
						:rules="[
							{ required: true, message: '请填写邮箱地址' },
							{ validator: validateEmail, message: '请输入正确的邮箱地址' }
						]"
					/>
					<van-field
						required
						v-model="state.form.code"
						name="code"
						label="验证码"
						placeholder="验证码"
						:rules="[{ required: true, message: '请填写验证码' }]"
					>
						<template #button>
							<van-button size="small" type="primary" @click="onSendCodeClick">{{ state.codeText }}</van-button>
						</template>
					</van-field>
					<van-field
						required
						v-model="state.form.securityQuestion"
						name="securityQuestion"
						label="密保问题"
						placeholder="密保问题"
						:rules="[{ required: true, message: '请填写密保问题' }]"
					/>
					<van-field
						required
						v-model="state.form.securityAnswer"
						name="securityAnswer"
						label="密保答案"
						placeholder="密保答案"
						:rules="[{ required: true, message: '请填写密保答案' }]"
					/>
				</van-cell-group>
				<div style="margin: 16px">
					<van-button round block type="primary" native-type="submit"> 提交 </van-button>
				</div>
			</van-form>
		</div>
	</div>
</template>

<script setup>
import { sendCode, resetSecurity } from '@/api/user.api.js'
import useToast from '@/hooks/useToast'
import useLoading from '@/hooks/useLoading'
import useUserStore from '@/store/modules/user.js'

const router = useRouter()
const { toast } = useToast()
const { loading, closeLoading } = useLoading()
const useUser = useUserStore()
const { userInfo } = storeToRefs(useUser)

const timer = ref(null)
const num = ref(60)
const state = reactive({
	codeText: '发送验证码',
	form: {
		token: '',
		email: userInfo.value.email
	}
})

const onSubmit = async () => {
	console.log(state.form)
	try {
		loading()
		await resetSecurity(state.form)
		toast('修改成功')
		router.go(-1)
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

const validateEmail = (val) => /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/.test(val)

const onSendCodeClick = async () => {
	if (timer.value) return
	try {
		loading()
		const { data } = await sendCode({ email: state.form.email })
		startTimer()
		state.form.token = data
		state.codeText = '60s后重试'
		toast('验证码发送成功')
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

const startTimer = () => {
	timer.value = setInterval(() => {
		if (num.value == 0) {
			stopTimer()
		} else {
			num.value--
			state.codeText = `${num.value}s后重试`
		}
	}, 1000)
}

const stopTimer = () => {
	clearInterval(timer.value)
	timer.value = null
	state.codeText = '发送验证码'
	num.value = 60
}
</script>

<style lang="scss" scoped>
.content {
	.van-form {
		padding: 20px 0;
		background-color: #fff;
	}
}
</style>
