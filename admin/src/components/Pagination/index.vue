<template>
	<div class="pagination-contaner" :class="[isBottom ? 'flex-bottom' : '']">
		<div class="pagination-count">
			<span class="mr16">第1-10条</span>
			<span>共 {{ total }} 条</span>
		</div>
		<el-pagination
			v-model:currentPage="currentPage"
			v-model:page-size="pageSize"
			:page-sizes="pageSizes"
			layout="sizes, prev, pager, next, jumper"
			:total="total"
			@size-change="handleSizeChange"
			@current-change="handleCurrentChange"
		/>
	</div>
</template>

<script setup name="pagination">
import { computed, watch, ref, onBeforeMount } from 'vue'

const proprs = defineProps({
	total: {
		type: Number,
		default: 0
	},
	page: {
		type: Number,
		default: 1
	},
	size: {
		type: Number,
		default: 10
	},
	pageSizes: {
		type: Array,
		// eslint-disable-next-line vue/require-valid-default-prop
		default: [10, 20, 30, 50, 100, 200, 300, 500]
	},
	isBottom: {
		type: Boolean,
		default: true
	}
})
const emit = defineEmits(['pagination', 'update:page', 'update:size'])

const currentPage = computed({
	get() {
		return proprs.page
	},
	set(val) {
		emit('update:page', val)
	}
})
const pageSize = computed({
	get() {
		return proprs.size
	},
	set(val) {
		emit('update:size', val)
	}
})

// 选择当前显示多少条数据
const handleSizeChange = (val) => {
	emit('pagination', { page: currentPage, size: val })
}
// 选择当前多少页
const handleCurrentChange = (val) => {
	emit('pagination', { page: val, size: pageSize })
}
</script>

<style lang="scss" scoped>
.pagination-contaner {
	width: 100%;
	height: 48px;
	line-height: 48px;
	display: flex;
	justify-content: space-between;
	box-shadow: 0px -2px 8px 1px rgba(0, 0, 0, 0.08);
	padding: 0 20px;

	.pagination-count {
		color: var(--fangmaster-bg-topBarColor);
		font-size: 14px;
		font-weight: 400;
	}
}
.flex-bottom {
	position: absolute;
	left: 0;
	right: 0;
	bottom: 0;
}
</style>
