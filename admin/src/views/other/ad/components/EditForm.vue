<template>
	<DialogPopup
		v-model="state.dialog"
		title="修改广告"
		:width="500"
		:btnLoading
		@cancel="close"
		@confirm="onConfirmClck"
	>
		<el-form :model="state.form" ref="formRef" :rules label-width="100">
			<el-form-item label="公司名称:" prop="companyName">
				<el-input v-model="state.form.companyName" placeholder="请输入广告名称" clearable />
			</el-form-item>
			<el-form-item label="广告费用:" prop="price">
				<el-input v-model="state.form.price" placeholder="请输入广告费用" clearable />
			</el-form-item>
			<el-form-item label="广告图片" prop="cover">
				<el-upload
					v-loading="coverLoading"
					class="cover-uploader"
					accept=".png,.jpg,.jpeg"
					:show-file-list="false"
					:http-request="onUploadCoverClick"
				>
					<img v-if="state.form.cover" :src="state.form.cover" />
					<el-icon v-else><Plus /></el-icon>
				</el-upload>
			</el-form-item>
			<el-form-item label="广告链接" prop="url">
				<el-input v-model="state.form.url" placeholder="请输入广告链接" clearable />
			</el-form-item>
			<el-form-item label="广告有效期" prop="expireTime">
				<el-date-picker
					v-model="state.form.expireTime"
					value-format="YYYY-MM-DD"
					type="date"
					:disabled-date="disabledDateFun"
					placeholder="请选择广告有效期"
					class="w100"
				/>
			</el-form-item>
		</el-form>
	</DialogPopup>
</template>

<script setup>
import { Plus } from '@element-plus/icons-vue'
import useMessage from '@/hooks/useMessage'
import { edit } from '@/api/ad.api.js'
import { upload } from '@/api/upload.api.js'
import { nextTick } from 'vue'

const emit = defineEmits(['success'])
const { success } = useMessage()

const formRef = ref(null)
const btnLoading = ref(false)
const coverLoading = ref(false)
const state = reactive({
	dialog: false,
	form: {
		companyName: null,
		price: null,
		cover: null,
		url: null,
		expireTime: null
	}
})
const rules = ref({
	companyName: [{ required: true, message: '请输入广告名称' }],
	price: [{ required: true, message: '请输入广告费用' }],
	cover: [{ required: true, message: '请上传广告图片' }],
	url: [{ required: true, message: '请输入广告链接' }],
	expireTime: [{ required: true, message: '请选择广告过期时间' }]
})

const onConfirmClck = async () => {
	try {
		await formRef.value.validate()
		btnLoading.value = true
		state.form.expireTime = state.form.expireTime + ' 23:59:59'
		await edit(state.form)
		success('修改成功')
		emit('success')
		close()
	} catch (error) {
		console.log(error)
	} finally {
		btnLoading.value = false
	}
}

const onUploadCoverClick = async ({ file }) => {
	try {
		state.form.cover = ''
		coverLoading.value = true
		const formData = new FormData()
		formData.append('file', file)
		const { data } = await upload(formData)
		state.form.cover = data
	} catch (error) {
		console.log(error)
	} finally {
		coverLoading.value = false
	}
}

const disabledDateFun = (time) => {
	return time.getTime() < Date.now()
}

const open = async (row) => {
	state.dialog = true
	await nextTick()
	const data = JSON.parse(JSON.stringify(row))
	state.form = data
}

const close = () => {
	formRef.value.resetFields()
	state.dialog = false
}

defineExpose({
	open
})
</script>

<style lang="scss" scoped>
.cover-uploader {
	width: 160px;
	height: 160px;
	border: 1px dashed var(--el-border-color);
	border-radius: 6px;
	cursor: pointer;
	position: relative;
	overflow: hidden;
	transition: var(--el-transition-duration-fast);
	img {
		width: 160px;
		height: 160px;
		object-fit: scale-down;
	}
	.el-icon {
		width: 160px;
		height: 160px;
		color: #ccc;
		font-weight: 400;
		font-size: 32px;
	}
}
</style>
