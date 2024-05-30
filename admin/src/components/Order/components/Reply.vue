<template>
	<DialogPopup
		v-model="state.dialog"
		title="回复评价"
		:width="600"
		:btnLoading
		@cancel="close"
		@confirm="onConfirmClick"
	>
		<div class="msg">评价内容: {{ state.message }}</div>
		<el-form :model="state.form" ref="formRef" :rules>
			<el-form-item prop="content">
				<el-input type="textarea" v-model="state.form.content" :rows="6" placeholder="请输入内容..." />
			</el-form-item>
		</el-form>
	</DialogPopup>
</template>

<script setup>
import useMessage from '@/hooks/useMessage'
import { comment } from '@/api/order.api.js'

const emit = defineEmits(['success'])
const { success } = useMessage()

const formRef = ref(null)
const btnLoading = ref(false)
const state = reactive({
	dialog: false,
	message: '',
	form: {
		productId: null,
		orderId: null,
		pid: null,
		answerId: null,
		content: ''
	}
})
const rules = ref({
	content: [{ required: true, message: '请输入内容' }]
})

const onConfirmClick = async () => {
	try {
		await formRef.value.validate()
		btnLoading.value = true
		await comment(state.form)
		close()
		emit('success')
		success('回复成功')
	} catch (error) {
		console.log(error)
	} finally {
		btnLoading.value = false
	}
}

const open = (row) => {
	console.log({ ...row })
	state.dialog = true
	state.form.productId = row.productId
	state.form.orderId = row.id
	state.form.pid = row.commentId
	state.form.answerId = row.commentId
	state.message = row.content
}

const close = async () => {
	await formRef.value.resetFields()
	state.dialog = false
}

defineExpose({
	open
})
</script>

<style scoped>
.msg {
	margin-bottom: 20px;
	line-height: 24px;
}
</style>
