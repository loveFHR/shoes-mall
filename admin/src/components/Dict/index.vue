<template>
	<div>
		<el-form :model="state.searchForm" ref="formRef">
			<el-row :gutter="10">
				<el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4">
					<el-form-item prop="label">
						<el-input
							v-model="state.searchForm.label"
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
					<el-button type="success" :icon="Plus" @click="onAddClick">新增{{ dict[type] }}</el-button>
				</el-form-item>
			</el-row>
		</el-form>

		<DTable
			ref="tableRef"
			:list="state.list"
			:total="state.total"
			v-loading="state.loading"
			:columns="columns"
			v-model:page="state.searchForm.current"
			v-model:size="state.searchForm.page"
			@onPagination="queryList"
			@batchDel="onBatchDeleteClick"
		>
			<template #status="{ row }">
				<el-switch
					:loading="row.loading"
					v-model="row.ban"
					:active-value="0"
					:inactive-value="1"
					active-text="启用"
					inactive-text="禁用"
					inline-prompt
					:before-change="() => onUpdateStatusChange(row)"
				/>
			</template>
			<template #action="{ row }">
				<el-button type="primary" link @click="onEditClick(row)">编辑</el-button>
				<el-button type="danger" link @click="onDelClick(row)">删除</el-button>
			</template>
			<template #batchAction>
				<el-dropdown-item @click="onBatchUpdateStatusClick">批量禁/反禁用</el-dropdown-item>
			</template>
		</DTable>

		<AddForm ref="addRef" @success="queryList" :title="dict[type]" :type />
		<EditForm ref="editRef" @success="queryList" :title="dict[type]" :type />
	</div>
</template>

<script setup>
import { Search, Plus, Refresh } from '@element-plus/icons-vue'
import AddForm from './AddForm.vue'
import EditForm from './EditForm.vue'
import useConfirm from '@/hooks/useConfirm'
import useMessage from '@/hooks/useMessage'
import { list, del, updateStatus, batchDel } from '@/api/dict.api.js'

const props = defineProps({
	type: {
		type: Number,
		default: () => {
			return 1
		}
	}
})

const dict = {
	1: '尺寸',
	2: '颜色',
	3: '品牌',
	4: '类型'
}

const { success, warning } = useMessage()

const formRef = ref(null)
const tableRef = ref(null)
const addRef = ref(null)
const editRef = ref(null)
const state = reactive({
	list: [],
	loading: false,
	total: 0,
	searchForm: {
		type: props.type,
		page: 10,
		label: null,
		current: 1
	}
})

const columns = [
	{ label: '序号', index: true, width: 80 },
	{ label: '名称', prop: 'label' },
	{ label: '状态', prop: 'status', width: 120, slot: 'status' },
	{ label: '操作', action: true, width: 110 }
]

onMounted(() => {
	queryList()
})

const queryList = async () => {
	try {
		state.loading = true
		const { data, total } = await list(state.searchForm)
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

const onUpdateStatusChange = (row) => {
	row.loading = true
	return new Promise((resolve, reject) => {
		updateStatus({ ids: row.id })
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

const onEditClick = (row) => {
	editRef.value.open(row)
}

const onDelClick = async (row) => {
	try {
		await useConfirm('确定要删除吗?')
		state.loading = true
		await batchDel({ ids: row.id })
		await success('操作成功')
		await queryList()
	} catch (error) {
		console.log(error)
	} finally {
		state.loading = false
	}
}

const onBatchDeleteClick = async (val) => {
	if (!val) return warning('请选择要批量删除的数据')
	const ids = val.toString()
	try {
		await useConfirm('确定要批量删除吗?')
		state.loading = true
		await batchDel({ ids })
		tableRef.value.updateCheckAllStatus()
		success('批量删除成功')
		queryList()
	} catch (error) {
		console.log(error)
	} finally {
		state.loading = false
	}
}

const onBatchUpdateStatusClick = async () => {
	console.log()
	const val = tableRef.value.getSelectValue()
	if (!val) return warning('请选择要批量批量禁/反禁用的数据')
	const ids = val.toString()
	try {
		await useConfirm('确定要批量禁/反禁用吗?')
		state.loading = true
		await updateStatus({ ids })
		tableRef.value.updateCheckAllStatus()
		success('批量禁/反禁用成功')
		queryList()
	} catch (error) {
		console.log(error)
	} finally {
		state.loading = false
	}
}

const onAddClick = () => {
	addRef.value.open()
}
</script>

<style lang="scss" scoped></style>
