<template>
	<div class="page user-edit">
		<NavBar title="修改密码" />

		<div class="content">
			<div class="form-item">
				<div class="label"><span>*</span>密保答案:</div>
				<input type="text" v-model="state.form.securityAnswer" placeholder="请输入密保答案" />
			</div>
			<div class="form-item">
				<div class="label"><span>*</span>新密码:</div>
				<input type="password" v-model="state.form.resetPwd" placeholder="请输入新密码" />
			</div>
			<div class="form-item">
				<div class="label"><span>*</span>确定密码:</div>
				<input type="password" v-model="state.form.checkPwd" placeholder="请输入确定密码" />
			</div>
			<div class="btn" @click="onConfirmClick">修改</div>
		</div>
	</div>
</template>

<script setup>
import useLoading from '@/hooks/useLoading'
import useToast from '@/hooks/useToast'
import useUserStore from '@/store/modules/user'
import { editPassword } from '@/api/user.api.js'

const router = useRouter()
const useUser = useUserStore()
const { userInfo } = storeToRefs(useUser)
const { loading, closeLoading } = useLoading()
const { toast } = useToast()

const state = reactive({
	form: {
		securityAnswer: '',
		resetPwd: '',
		checkPwd: ''
	}
})

const onConfirmClick = async () => {
	try {
		await validateData()
		loading('加载中...')
		await editPassword(state.form)
		toast('修改成功')
		router.go(-1)
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

const validateData = () => {
	return new Promise((resolve, reject) => {
		if (!state.form.securityAnswer) {
			toast('请输入密保问题')
			return reject()
		}
		if (!state.form.resetPwd) {
			toast('请输入新密码')
			return reject()
		}
		if (!state.form.checkPwd) {
			toast('请再次输入密码')
			return reject()
		}
		if (state.form.checkPwd != state.form.resetPwd) {
			toast('两次密码不一致')
			return reject()
		}
		resolve()
	})
}
</script>

<style lang="scss" scoped>
.user-edit {
	background-color: #ffffff;
}
.form-item {
	width: 686px;
	margin: 0 auto;
	display: flex;
	align-items: center;
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
	width: 686px;
	margin: 0 auto;
	height: 100px;
	font-size: 30px;
	color: #ffffff;
	border-radius: 20px;
	margin-top: 50px;
	background-color: rgb(255, 98, 0);
	text-align: center;
	line-height: 100px;
}
.van-radio-group {
	display: flex;
	align-items: center;
	.van-radio {
		margin-right: 20px;
	}
}
</style>
