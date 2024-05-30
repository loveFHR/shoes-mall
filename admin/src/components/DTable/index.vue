<template>
	<div element-loading-text="疯狂加载中...">
		<el-table
			border
			:data="list"
			v-bind="$attrs"
			class="w100"
			:header-cell-style="$tabelHaederStyle"
			ref="tableRef"
			@selection-change="onTableSelectChange"
			row-key="id"
		>
			<el-table-column type="selection" width="55" reserve-selection />
			<!-- 序号 -->
			<template v-if="indexOption">
				<el-table-column
					v-if="indexOption"
					type="index"
					:label="indexOption.label"
					:width="indexOption.width"
					:align="indexOption.align"
					:min-width="indexOption.minWidth"
					:fixed="indexOption.fixed"
				>
					<template #default="{ $index }">
						{{ (currentPage - 1) * pageSize + 1 + $index }}
					</template>
				</el-table-column>
			</template>

			<!-- 数据 -->
			<template v-for="item in tableOption" :key="item.prop">
				<!-- 没有插槽的数据 -->
				<template v-if="!item.slot">
					<el-table-column
						:label="item.label"
						:prop="item.prop"
						:width="item.width"
						:min-width="item.minWidth"
						:show-overflow-tooltip="item.showOverflowTooltip"
						:fixed="item.fixed"
						:sortable="item.sortable"
					>
						<template #default="{ row }">
							{{ row[item.prop] ? row[item.prop] : '-' }}
						</template>
					</el-table-column>
				</template>
				<!-- 自定义数据 -->
				<template v-else>
					<el-table-column
						:label="item.label"
						:prop="item.prop"
						:width="item.width"
						:min-width="item.minWidth"
						:show-overflow-tooltip="item.showOverflowTooltip"
						:fixed="item.fixed"
						:sortable="item.sortable"
					>
						<template #default="scoped">
							<slot :name="item.prop" :scoped="scoped" :row="scoped.row" :index="scoped.$index"></slot>
						</template>
					</el-table-column>
				</template>
			</template>

			<!-- 操作 -->
			<template v-if="actionOption">
				<el-table-column
					:label="actionOption.label"
					:align="actionOption.align"
					:width="actionOption.width"
					:min-width="actionOption.minWidth"
					fixed="right"
				>
					<template #default="scoped">
						<slot name="action" :scoped="scoped" :row="scoped.row" :index="scoped.$index"></slot>
					</template>
				</el-table-column>
			</template>
		</el-table>
		<!-- 分页 -->
		<div class="pagination" v-if="pagination">
			<div class="flex-a">
				<template v-if="isBatchAction">
					<el-checkbox v-model="checkAll" :indeterminate="indeterminate" @change="onCheckboxChange"></el-checkbox>
					<el-dropdown class="flex-a ml5 pointer">
						<span class="el-dropdown-link flex-a">
							批量操作
							<el-icon class="el-icon--right">
								<arrow-down />
							</el-icon>
						</span>
						<template #dropdown>
							<el-dropdown-menu>
								<el-dropdown-item
									v-if="batchDel"
									:disabled="!selectValue || selectValue.length == 0"
									@click="onBatchDeleteClick"
									>批量删除</el-dropdown-item
								>
								<slot name="batchAction"></slot>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				</template>
			</div>
			<el-pagination
				small
				v-model:current-page="currentPage"
				v-model:page-size="pageSize"
				:page-sizes="pageSizes"
				layout="total, sizes, prev, pager, next, jumper"
				:total="total"
				@size-change="handleSizeChange"
				@current-change="handleCurrentChange"
			/>
		</div>
	</div>
</template>

<script setup>
import { ArrowDown } from '@element-plus/icons-vue'
const emit = defineEmits(['onPagination', 'update:page', 'update:size', 'batchDel'])

const props = defineProps({
	// 表格数据
	list: {
		type: Array,
		required: true,
		default: () => {
			return []
		}
	},
	// 表格配置项
	columns: {
		type: Array,
		required: true,
		default: () => {
			return []
		}
	},
	// 是否显示分页
	pagination: {
		type: Boolean,
		default: () => {
			return true
		}
	},
	// 当前页
	page: {
		type: Number,
		default: () => {
			return 1
		}
	},
	// 当前页显示条数
	size: {
		type: Number,
		default: () => {
			return 10
		}
	},
	// 当前页显示条数配置数组
	pageSizes: {
		type: Array,
		default: () => {
			return [5, 10, 15, 20, 30, 50, 80, 100]
		}
	},
	// 总条数
	total: {
		type: Number,
		default: () => {
			return 0
		}
	},
	isBatchAction: {
		type: Boolean,
		default: () => {
			return true
		}
	},
	batchDel: {
		type: Boolean,
		default: () => {
			return true
		}
	}
})

const currentPage = computed({
	get() {
		return props.page
	},
	set(val) {
		emit('update:page', val)
	}
})

const pageSize = computed({
	get() {
		return props.size
	},
	set(val) {
		emit('update:size', val)
	}
})

const $tabelHaederStyle = () => {
	return {
		'background-color': '#F2F3F5',
		color: '#333333',
		'font-size': '14px',
		'font-weight': '500'
	}
}

const tableRef = ref(null)
const checkAll = ref(false)
const indeterminate = ref(false)
const selectValue = ref(null)

watch(selectValue, (val) => {
	if (val.length == props.total) {
		checkAll.value = true
		indeterminate.value = null
	}
	if (val.length < props.total) {
		indeterminate.value = true
	}

	if (val.length == 0) {
		checkAll.value = false
		indeterminate.value = false
	}
})

const onBatchDeleteClick = () => {
	emit('batchDel', selectValue.value)
}

// 表格选中事件
const onTableSelectChange = (val) => {
	selectValue.value = val.map((v) => v.id)
}

// 批量操作
const onCheckboxChange = (val) => {
	tableRef.value.toggleAllSelection()
}

// 获取序号项
const indexOption = computed(() => props.columns.find((item) => item.index))

// 获取操作项
const actionOption = computed(() => props.columns.find((item) => item.action))

// 获取正常数据
const tableOption = computed(() => {
	return props.columns.filter((item) => !item.index).filter((item) => !item.action)
})

const handleSizeChange = (val) => {
	emit('onPagination')
}

const handleCurrentChange = (val) => {
	emit('onPagination')
}

const updateCheckAllStatus = (val = false) => {
	indeterminate.value = val
	tableRef.value.clearSelection()
}

const getSelectValue = () => {
	return selectValue.value
}

defineExpose({
	getSelectValue,
	updateCheckAllStatus
})
</script>

<style lang="scss" scoped>
.empty {
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 20px 0;
	img {
		width: 80px;
	}
}
.pagination {
	margin-top: 20px;
	display: flex;
	align-items: center;
	justify-content: space-between;
}
</style>
