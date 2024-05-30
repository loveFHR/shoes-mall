<template>
	<div>
		<el-form :model="state.searchForm" ref="formRef">
			<el-row :gutter="10">
				<el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4">
					<el-form-item prop="companyName">
						<el-input
							v-model="state.searchForm.companyName"
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
					<el-button type="success" :icon="Plus" @click="onAddClick">新增链接</el-button>
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
			@batchDel="onBatchDelClick"
		>
			<template #url="{ row }">
				<el-link type="primary" :underline="false" :href="row.url" target="__black">{{ row.url }}</el-link>
			</template>
			<template #cover="{ row }">
				<el-image
					:src="row.cover"
					fit="cover"
					:preview-src-list="[row.cover]"
					preview-teleported
					style="width: 64px; height: 30px"
				/>
			</template>
			<template #action="{ row }">
				<el-button type="primary" link @click="onEditClick(row)">编辑</el-button>
				<el-button type="danger" link @click="onDelClick(row)">删除</el-button>
			</template>
		</DTable>

		<AddForm ref="addRef" @success="queryList" />
		<EditForm ref="editRef" @success="queryList" />
	</div>
</template>

<script setup>
import AddForm from './components/AddForm.vue'
import EditForm from './components/EditForm.vue'
import { Search, Plus, Refresh } from '@element-plus/icons-vue'
import useMessage from '@/hooks/useMessage'
import useConfirm from '@/hooks/useConfirm'
import { list, del, batchDel } from '@/api/link.api.js'

const { success } = useMessage()

const formRef = ref(null)
const addRef = ref(null)
const tableRef = ref(null)
const editRef = ref(null)
const state = reactive({
	list: [],
	total: 0,
	loading: false,
	searchForm: {
		companyName: null,
		current: 1,
		page: 10
	}
})

const columns = [
	{ label: '序号', index: true, width: 80 },
	{ label: '链接名称', prop: 'companyName', minWidth: 140 },
	{ label: '链接地址', prop: 'url', slot: 'url', minWidth: 200, showOverflowTooltip: true },
	{ label: '链接图片', prop: 'cover', slot: 'cover' },
	{ label: '创建时间', prop: 'createTime', minWidth: 200 },
	{ label: '修改时间', prop: 'updateTime', minWidth: 200 },
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

const onAddClick = () => {
	addRef.value.open()
}

const onEditClick = async (row) => {
	editRef.value.open(row)
}

const onDelClick = async (row) => {
	try {
		await useConfirm('确定要删除该合作链接吗?')
		state.loading = true
		await del(row.id)
		success('删除成功')
		queryList()
	} catch (error) {
		console.log(error)
	} finally {
		state.loading = false
	}
}

const onBatchDelClick = async (val) => {
	try {
		await useConfirm('确定要批量删除吗?')
		state.loading = true
		await batchDel({ ids: val.toString() })
		tableRef.value.updateCheckAllStatus()
		success('操作成功')
		queryList()
	} catch (error) {
		console.log(error)
	} finally {
		state.loading = false
	}
}
</script>

<style lang="scss" scoped></style>
