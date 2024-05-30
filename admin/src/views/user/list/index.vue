<template>
	<div>
		<el-form :model="state.searchForm" ref="formRef">
			<el-row :gutter="10">
				<el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4">
					<el-form-item prop="name">
						<el-input
							v-model="state.searchForm.name"
							placeholder="请输入要搜索的内容"
							clearable
							:prefix-icon="Search"
							@clear="onSearchClick"
						/>
					</el-form-item>
				</el-col>
				<el-form-item class="ml5">
					<el-button type="primary" :icon="Search" @click="onSearchClick">搜索</el-button>
					<el-button type="" :icon="Refresh" @click="onRefreshClick">重置</el-button>
				</el-form-item>
			</el-row>
		</el-form>
		<DTable
			ref="tableRef"
			:columns
			:list="state.list"
			:total="state.total"
			v-loading="state.loading"
			v-model:page="state.searchForm.current"
			v-model:size="state.searchForm.page"
			@onPagination="queryList"
			:batchDel="false"
		>
			<template #avatar="{ row }">
				<el-avatar :src="row.avatar" :size="24" />
			</template>
			<template #gender="{ row }">
				<span v-if="row.gender == 0">女</span>
				<span v-if="row.gender == 1">男</span>
				<span v-if="row.gender == 2">未知</span>
			</template>
			<template #ban="{ row }">
				<el-switch
					:loading="row.loading"
					v-model="row.ban"
					:active-value="0"
					:inactive-value="1"
					active-text="正常"
					inactive-text="禁用"
					inline-prompt
					:before-change="() => onUpdateStatusChange(row)"
				/>
			</template>
			<template #action="{ row }">
				<el-button type="primary" link @click="onDetailsClick(row)">详情</el-button>
				<el-button type="danger" link @click="onResetPasswordClick(row)">重置密码</el-button>
			</template>
			<template #batchAction>
				<el-dropdown-item @click="onBatchDeleteClick">批量禁/反禁用</el-dropdown-item>
			</template>
		</DTable>

		<Details ref="detailsRef" />
	</div>
</template>

<script setup>
import { Search, Refresh } from '@element-plus/icons-vue'
import Details from './components/Details.vue'
import useConfirm from '@/hooks/useConfirm'
import useMessage from '@/hooks/useMessage'
import { list, changeStatus, resetPass } from '@/api/user.api.js'

const { success, warning } = useMessage()

const formRef = ref(null)
const tableRef = ref(null)
const detailsRef = ref(null)
const state = reactive({
	list: [],
	total: 0,
	loading: false,
	searchForm: {
		name: '',
		current: 1,
		page: 10
	}
})
const columns = [
	{ label: '序号', index: true, width: 80, fixed: 'left' },
	{ label: '账号', prop: 'email', showOverflowTooltip: true },
	{ label: '头像', prop: 'avatar', slot: 'avatar' },
	{ label: '姓名', prop: 'username' },
	{ label: '性别', prop: 'gender', slot: 'gender' },
	{ label: '注册时间', prop: 'createTime', showOverflowTooltip: true },
	{ label: '状态', prop: 'ban', slot: 'ban', width: 120 },
	{ label: '操作', action: true, width: 130, fixed: 'right' }
]

onMounted(() => {
	queryList()
})

const queryList = async () => {
	try {
		state.loading = true
		const { data, total } = await list(state.searchForm)
		console.log(data)
		state.list = data
		state.total = total
	} catch (error) {
		console.log(error)
	} finally {
		state.loading = false
	}
}

const onSearchClick = async () => {
	state.searchForm.current = 1
	await queryList()
}

const onRefreshClick = async () => {
	await formRef.value.resetFields()
	await onSearchClick()
}

const onDetailsClick = (row) => {
	detailsRef.value.open(row)
}

const onResetPasswordClick = async (row) => {
	try {
		await useConfirm('确定要重置该用户的登录密码吗?')
		state.loading = true
		const { data } = await resetPass(row.id)
		alert(`当前新密码为: ${data}`)
		success('操作成功')
		queryList()
	} catch (error) {
		console.log(error)
	} finally {
		state.loading = false
	}
}

const onUpdateStatusChange = (row) => {
	row.loading = true
	return new Promise((resolve, reject) => {
		changeStatus({ userIds: row.id })
			.then(() => {
				success('操作成功')
				queryList()
				resolve()
			})
			.catch(() => {
				reject()
			})
			.finally(() => {
				row.loading = false
			})
	})
}

const onBatchDeleteClick = async () => {
	const val = tableRef.value.getSelectValue()
	if (!val) return warning('请先选择数据')
	try {
		await useConfirm('确定要批量禁/反禁用用户吗?')
		state.loading = true
		await changeStatus({ userIds: val.toString() })
		success('操作成功')
		queryList()
		tableRef.value.updateCheckAllStatus()
	} catch (error) {
		console.log(error)
	} finally {
		state.loading = false
	}
}
</script>

<style lang="scss" scoped></style>
