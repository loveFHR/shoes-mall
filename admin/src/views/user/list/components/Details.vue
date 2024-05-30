<template>
	<DialogPopup v-model="state.dialog" title="用户详情" :width="800" :footer="false" @cancel="close">
		<el-form :model="state.form" ref="formRef">
			<el-row :gutter="10">
				<el-col :span="8">
					<el-form-item label="用户名:">{{ state.form.username }}</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="电话:">{{ state.form.phone }}</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="邮箱地址:">{{ state.form.email }}</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="性别:">{{ state.form.gender == 1 ? '女' : '男' }}</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="注册时间:">{{ state.form.createTime }}</el-form-item>
				</el-col>
				<el-col :span="24">
					<el-form-item label="状态:">{{ state.form.ban == 0 ? '正常' : '禁用' }}</el-form-item>
				</el-col>
			</el-row>
		</el-form>
	</DialogPopup>
</template>

<script setup>
const formRef = ref(null)
const state = reactive({
	dialog: false,
	form: {}
})

const open = (row) => {
	state.dialog = true
	state.form = row
}

const close = () => {
	state.dialog = false
}

defineExpose({
	open
})
</script>

<style lang="scss" scoped></style>
