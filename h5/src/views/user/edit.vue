<template>
	<div class="page user-edit">
		<NavBar title="修改个人信息" />

		<div class="content">
			<div class="form-item">
				<div class="label">头像:</div>
				<van-uploader v-model="state.avatar" :max-count="1" :after-read="onUploadAvatar" />
			</div>
			<div class="form-item">
				<div class="label"><span>*</span>昵称:</div>
				<input type="text" v-model="state.form.username" placeholder="请输入昵称" />
			</div>
			<div class="form-item">
				<div class="label"><span>*</span>手机号:</div>
				<input type="text" v-model="state.form.phone" placeholder="请输入手机号" />
			</div>
			<div class="form-item">
				<div class="label"><span>*</span>性别:</div>
				<van-radio-group v-model="state.form.gender" size="small" icon-size="16px">
					<van-radio :name="0">女</van-radio>
					<van-radio :name="1">男</van-radio>
					<van-radio :name="2">保密</van-radio>
				</van-radio-group>
			</div>

			<div class="btn" @click="onConfirmClick">保存</div>
		</div>
	</div>
</template>

<script setup>
import useLoading from '@/hooks/useLoading'
import useToast from '@/hooks/useToast'
import useUserStore from '@/store/modules/user'
import { upload } from '@/api/upload.api.js'
import { edit } from '@/api/user.api.js'

const router = useRouter()
const useUser = useUserStore()
const { userInfo } = storeToRefs(useUser)
const { loading, closeLoading } = useLoading()
const { toast } = useToast()

const state = reactive({
	form: {
		avatar: userInfo.value.avatar,
		gender: userInfo.value.gender,
		username: userInfo.value.username,
		phone: userInfo.value.phone
	},
	avatar: [{ url: userInfo.value.avatar }]
})

const onConfirmClick = async () => {
	try {
		loading('加载中...')
		await edit(state.form)
		await useUser.onGetUserInfo()
		toast('修改成功')
		router.go(-1)
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
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
