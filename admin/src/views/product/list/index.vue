<template>
	<div>
		<el-form :model="state.searchForm" ref="formRef" @keyup.enter="onSearchClick">
			<el-row :gutter="10">
				<el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4">
					<el-form-item prop="name">
						<el-input
							v-model="state.searchForm.name"
							:prefix-icon="Search"
							placeholder="请输入要搜索的商品名称"
							clearable
							@clear="onSearchClick"
						/>
					</el-form-item>
				</el-col>
				<el-col :xs="24" :sm="12" :md="8" :lg="4" :xl="3">
					<el-form-item prop="brandIdList">
						<el-select
							v-model="state.searchForm.brandIdList"
							multiple
							collapse-tags
							collapse-tags-tooltip
							placeholder="请选择商品品牌"
							@change="onSearchClick"
						>
							<el-option v-for="item in state.brandList" :key="item.id" :label="item.label" :value="item.id" />
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :xs="24" :sm="12" :md="8" :lg="4" :xl="3">
					<el-form-item prop="typeIdList">
						<el-select
							v-model="state.searchForm.typeIdList"
							multiple
							collapse-tags
							collapse-tags-tooltip
							placeholder="请选择商品类型"
							@change="onSearchClick"
						>
							<el-option v-for="item in state.typeList" :key="item.id" :label="item.label" :value="item.id" />
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :xs="24" :sm="12" :md="8" :lg="4" :xl="3">
					<el-form-item prop="sizeIdList">
						<el-select
							v-model="state.searchForm.sizeIdList"
							multiple
							collapse-tags
							collapse-tags-tooltip
							placeholder="请选择商品尺寸"
							@change="onSearchClick"
						>
							<el-option v-for="item in state.sizeList" :key="item.id" :label="item.label" :value="item.id" />
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :xs="24" :sm="12" :md="8" :lg="4" :xl="3">
					<el-form-item prop="colorIdList">
						<el-select
							v-model="state.searchForm.colorIdList"
							multiple
							collapse-tags
							collapse-tags-tooltip
							placeholder="请选择商品颜色"
							@change="onSearchClick"
						>
							<el-option v-for="item in state.colorList" :key="item.id" :label="item.label" :value="item.id" />
						</el-select>
					</el-form-item>
				</el-col>
				<el-form-item class="ml5">
					<el-button type="primary" :icon="Search" @click="onSearchClick">搜索</el-button>
					<el-button type="" :icon="Refresh" @click="onRefreshClick">重置</el-button>
					<el-button type="success" :icon="Plus" @click="onAddClick">新增商品</el-button>
				</el-form-item>
			</el-row>
		</el-form>

		<DTable
			ref="tableRef"
			:columns
			:list="state.list"
			:total="state.total"
			v-loading="state.loading"
			v-model:page="state.page"
			v-model:size="state.pageSize"
			@onPagination="queryList"
			@batchDel="onBatchDelClick"
		>
			<template #cover="{ row }">
				<el-image :src="row.cover" style="width: 34px" :preview-src-list="[row.cover]" fit="cover" preview-teleported />
			</template>
			<template #nowPrice="{ row }">
				<span>{{ row.nowPrice }}元</span>
			</template>
			<template #usedPrice="{ row }">
				<span>{{ row.usedPrice }}元</span>
			</template>
			<template #isOn="{ row }">
				<el-switch
					:loading="row.loading"
					v-model="row.isOn"
					:active-value="1"
					:inactive-value="0"
					active-text="上架"
					inactive-text="下架"
					inline-prompt
					:before-change="() => onUpdateStatusChange(row)"
				/>
			</template>
			<template #action="{ row }">
				<el-button type="primary" link @click="onEditClick(row)">编辑</el-button>
				<el-button type="danger" link @click="onDelClick(row)">删除</el-button>
			</template>
			<template #batchAction>
				<el-dropdown-item @click="onBatchDeleteClick">批量禁/反禁用</el-dropdown-item>
			</template>
		</DTable>

		<AddForm
			ref="addRef"
			:brandList="state.brandList"
			:typeList="state.typeList"
			:sizeList="state.sizeList"
			:colorList="state.colorList"
			@success="queryList"
		/>
		<EditForm
			ref="editRef"
			:brandList="state.brandList"
			:typeList="state.typeList"
			:sizeList="state.sizeList"
			:colorList="state.colorList"
			@success="queryList"
		/>
	</div>
</template>

<script setup>
import { Search, Refresh, Plus } from '@element-plus/icons-vue'
import AddForm from './compoonents/AddForm.vue'
import EditForm from './compoonents/EditForm.vue'
import useConfirm from '@/hooks/useConfirm'
import useMessage from '@/hooks/useMessage'
import { list as getDictList } from '@/api/dict.api.js'
import { list, updateStatus, batchDel } from '@/api/product.api.js'

const { success, warning } = useMessage()

const formRef = ref(null)
const addRef = ref(null)
const tableRef = ref(null)
const editRef = ref(null)
const state = reactive({
	brandList: [],
	typeList: [],
	sizeList: [],
	colorList: [],
	list: [],
	total: 0,
	loading: false,
	page: 1,
	pageSize: 10,
	searchForm: {
		name: null,
		brandIdList: [],
		typeIdList: [],
		sizeIdList: [],
		colorIdList: []
	}
})
const columns = [
	{ label: '序号', index: true, width: 80, fixed: 'left' },
	{ label: '商品名称', prop: 'title', showOverflowTooltip: true, minWidth: 180 },
	{ label: '商品图片', prop: 'cover', slot: 'cover', minWidth: 100 },
	{ label: '现价', prop: 'nowPrice', slot: 'nowPrice' },
	{ label: '原价', prop: 'usedPrice', slot: 'usedPrice' },
	{ label: '库存', prop: 'restNum' },
	{ label: '商品类型', prop: 'systemBaseSettingBrandLabel', minWidth: 120 },
	{ label: '商品品牌', prop: 'systemBaseSettingTypeLabel', minWidth: 120 },
	{ label: '颜色', prop: 'color', showOverflowTooltip: true, minWidth: 120 },
	{ label: '尺寸', prop: 'size', showOverflowTooltip: true, minWidth: 120 },
	{ label: '状态', prop: 'isOn', slot: 'isOn', width: 80 },
	{ label: '操作', action: true, width: 110, fixed: 'right' }
]

onMounted(() => {
	queryDictList()
	queryList()
})

const queryDictList = async () => {
	try {
		const { data: sizeList } = await getDictList({ type: 1, current: 1, page: 99999 })
		const { data: colorList } = await getDictList({ type: 2, current: 1, page: 99999 })
		const { data: brandList } = await getDictList({ type: 3, current: 1, page: 99999 })
		const { data: typeList } = await getDictList({ type: 4, current: 1, page: 99999 })
		state.sizeList = sizeList
		state.colorList = colorList
		state.brandList = brandList
		state.typeList = typeList
	} catch (error) {
		console.log(error)
	}
}

const queryList = async () => {
	try {
		state.loading = true
		const { data, total } = await list(state.page, state.pageSize, state.searchForm)
		data.map((item) => {
			item.color = item.systemBaseSettingColorLabelList.join(',')
			item.size = item.systemBaseSettingSizeLabelList.join(',')
		})
		state.list = data
		state.total = total
		console.log(data)
	} catch (error) {
		console.log(error)
	} finally {
		state.loading = false
	}
}

const onSearchClick = () => {
	state.page = 1
	queryList()
}

const onRefreshClick = async () => {
	await formRef.value.resetFields()
	await onSearchClick()
}

const onAddClick = () => {
	addRef.value.open()
}

const onEditClick = (row) => {
	editRef.value.open(row.id)
}

const onDelClick = async (row) => {
	try {
		await useConfirm('确定要删除该商品吗？')
		state.loading = true
		await batchDel({ ids: row.id })
		await success('删除成功')
		await queryList()
	} catch (error) {
		console.log(error)
	} finally {
		state.loading = false
	}
}

const onBatchDelClick = async (val) => {
	try {
		await useConfirm('确定要批量删除商品吗？')
		state.loading = true
		await batchDel({ ids: val.toString() })
		await success('批量删除成功')
		await queryList()
		tableRef.value.updateCheckAllStatus()
	} catch (error) {
		console.log(error)
	} finally {
		state.loading = false
	}
}

const onUpdateStatusChange = (row) => {
	row.loading = true
	return new Promise((reslove, reject) => {
		updateStatus({ ids: row.id })
			.then(() => {
				success('操作成功')
				queryList()
				reslove()
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
	try {
		const val = tableRef.value.getSelectValue()
		if (!val || Object.keys(val).length == 0) {
			return warning('请选择要操作的数据')
		}
		await useConfirm('确定要批量禁/反禁用吗?')
		state.loading = true
		await updateStatus({ ids: val.toString() })
		await success('操作成功')
		await queryList()
		tableRef.value.updateCheckAllStatus()
	} catch (error) {
		console.log(error)
	} finally {
		state.loading = false
	}
}
</script>

<style lang="scss" scoped></style>
