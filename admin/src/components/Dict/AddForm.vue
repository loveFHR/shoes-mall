<template>
	<DialogPopup
		:title="'新增' + title"
		v-model="state.dialog"
		:btnloading="state.btnloading"
		@cancel="close"
		@confirm="onConfirmClick"
	>
		<el-form :model="state.form" ref="formRef" :rules>
			<el-form-item label="名称:" prop="label">
				<el-input v-model="state.form.label" placeholder="请输入名称" clearable />
			</el-form-item>
		</el-form>
	</DialogPopup>
</template>

<script setup>
import useMessage from '@/hooks/useMessage'
import { add } from '@/api/dict.api.js'

const props = defineProps({
	title: {
		type: String,
		default: () => {
			return ''
		}
	},
	type: {
		type: Number,
		default: () => {
			return 1
		}
	}
})

const emit = defineEmits(['success'])
const { success } = useMessage()

const formRef = ref(null)
const state = reactive({
	dialog: false,
	btnloading: false,
	form: {
		type: props.type,
		label: ''
	}
})
const rules = ref({
	label: [{ required: true, message: '名称不能为空' }]
})

const onConfirmClick = async () => {
	try {
		await formRef.value.validate()
		state.btnloading = true
		await add(state.form)
		await success('新增成功')
		emit('success')
		close()
	} catch (error) {
		console.log(error)
	} finally {
		state.btnloading = false
	}
}

const open = () => {
	state.dialog = true
}

const close = async () => {
	await formRef.value.resetFields()
	state.dialog = false
}

defineExpose({
	open
})
</script>

<style lang="scss" scoped></style>
