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
			:isBatchAction="type == 2 || type == -2 ? true : false"
			:batchDel="false"
		>
			<template #cover="{ row }">
				<el-image :src="row.cover" fit="cover" style="width: 74px" />
			</template>
			<template #price="{ row }">
				<span>{{ row.price }}元</span>
			</template>
			<template #payStatus="{ row }">
				<span>{{ payStatusDict[row.payStatus] }}</span>
			</template>
			<template #orderStatus="{ row }">
				<span>{{ orderStatusDict[row.orderStatus] }}</span>
			</template>
			<template #name="{ row }">
				<span>{{ row.addressVo.name }}</span>
			</template>
			<template #phone="{ row }">
				<span>{{ row.addressVo.phone }}</span>
			</template>
			<template #city="{ row }">
				<span>{{ row.addressVo.pcdStr }}{{ row.addressVo.address }}</span>
			</template>

			<template #action="{ row }">
				<el-button type="success" link v-if="type == 2" @click="onSendClick(row)">发货</el-button>
				<el-button type="success" link v-if="type == -2" @click="onRefundClick(row)">同意退款</el-button>
				<el-button type="success" link v-if="type == 14" @click="onReplyClick(row)">回复评价</el-button>
				<el-button type="primary" link @click="onDetailsClick(row)">详情</el-button>
			</template>
			<template #batchAction>
				<el-dropdown-item v-if="type == 2" @click="onBatchSendClick" :disabled="state.list.length == 0"
					>批量发货</el-dropdown-item
				>
				<el-dropdown-item v-if="type == -2" @click="onBatchRefundClick" :disabled="state.list.length == 0"
					>批量同意退款</el-dropdown-item
				>
			</template>
		</DTable>

		<Details ref="detailsRef" />
		<Reply ref="replyRef" @success="queryList" />
	</div>
</template>

<script setup>
import { Search, Plus, Refresh } from '@element-plus/icons-vue'
import useMessage from '@/hooks/useMessage'
import useConfirm from '@/hooks/useConfirm'
import Details from './components/Details.vue'
import Reply from './components/Reply.vue'
import { list, send, refund } from '@/api/order.api.js'

const { success, warning } = useMessage()

const tableRef = ref(null)
const formRef = ref(null)
const replyRef = ref(null)

const props = defineProps({
	type: {
		type: Number,
		default: () => {
			return 1
		}
	}
})

const detailsRef = ref(null)
const state = reactive({
	loading: false,
	list: [],
	total: 0,
	searchForm: {
		current: 1,
		page: 10,
		name: '',
		type: props.type
	}
})

const columns = ref([])

if (props.type == 14) {
	columns.value = [
		{ label: '序号', width: 80, fixed: true, index: true },
		{ label: '订单号', prop: 'orderNo', minWidth: 160, fixed: 'left', showOverflowTooltip: true },
		{ label: '金额', prop: 'price', slot: 'price', width: 120 },
		{ label: '数量', prop: 'num', width: 120 },
		{ label: '评论内容', prop: 'content', width: 180, showOverflowTooltip: true },
		{ label: '评论时间', prop: 'commentTime', minWidth: 180, showOverflowTooltip: true },
		{ label: '姓名', prop: 'name', slot: 'name', minWidth: 120 },
		{ label: '联系电话', prop: 'phone', slot: 'phone', width: 120 },
		{ label: '收货地址', prop: 'city', slot: 'address', minWidth: 240, showOverflowTooltip: true },
		{ label: '下单时间', prop: 'createTime', minWidth: 180 },
		{ label: '操作', action: true, width: 140, fixed: 'right' }
	]
} else {
	columns.value = [
		{ label: '序号', width: 80, fixed: true, index: true },
		{ label: '订单号', prop: 'orderNo', minWidth: 160, fixed: 'left', showOverflowTooltip: true },
		{ label: '金额', prop: 'price', slot: 'price', width: 120 },
		{ label: '数量', prop: 'num', width: 120 },
		{ label: '姓名', prop: 'name', slot: 'name', minWidth: 120 },
		{ label: '联系电话', prop: 'phone', slot: 'phone', width: 120 },
		{ label: '收货地址', prop: 'city', slot: 'address', minWidth: 240, showOverflowTooltip: true },
		{ label: '下单时间', prop: 'createTime', minWidth: 180 },
		{ label: '操作', action: true, width: 140, fixed: 'right' }
	]
}

const payStatusDict = {
	'-1': '支付失败',
	0: '未支付',
	1: '支付成功'
}
const orderStatusDict = {
	'-1': '手动关闭',
	'-2': '超时关闭',
	0: '待支付',
	1: '已支付',
	2: '交易成功'
}

onMounted(() => {
	queryList()
})

const onReplyClick = (row) => {
	replyRef.value.open(row)
}

const onSearchClick = async () => {
	state.searchForm.current = 1
	await queryList()
}

const onRefreshClick = async () => {
	await formRef.value.resetFields()
	await onSearchClick()
}

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

// 发货
const onSendClick = async (item) => {
	try {
		state.loading = true
		await send({ ids: item.id })
		success('发货成功')
		await queryList()
	} catch (error) {
		console.log(error)
	} finally {
		state.loading = false
	}
}

// 批量发货
const onBatchSendClick = async () => {
	const val = tableRef.value.getSelectValue()
	if (!val) return warning('请先选择数据')
	try {
		await useConfirm('确定要批量发货吗?')
		state.loading = true
		await send({ ids: val.toString() })
		success('批量发货成功')
		tableRef.value.updateCheckAllStatus()
		await queryList()
	} catch (error) {
		console.log(error)
	} finally {
		state.loading = false
	}
}

// 同意退款
const onRefundClick = async (row) => {
	try {
		await useConfirm('确定要同意退款吗?')
		state.loading = true
		await refund({ orderNoList: [row.orderNo] })
		success('同意退款成功')
		queryList()
	} catch (error) {
		console.log(error)
	} finally {
		state.loading = false
	}
}

// 批量同意发货
const onBatchRefundClick = async () => {
	const val = tableRef.value.getSelectValue()
	if (!val) return warning('请先选择数据')
	try {
		let ids = []
		state.list.map((item) => {
			val.map((v) => {
				if (item.id == v) {
					ids.push(item.orderNo)
				}
			})
		})
		await useConfirm('确定要同意退款吗?')
		state.loading = true
		await refund({ orderNoList: ids })
		tableRef.value.updateCheckAllStatus()
		success('同意退款成功')
		queryList()
	} catch (error) {
		console.log(error)
	} finally {
		state.loading = false
	}
}

const onDetailsClick = (row) => {
	detailsRef.value.open(row)
}
</script>

<style lang="scss" scoped></style>
