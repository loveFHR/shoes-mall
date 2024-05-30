<template>
	<el-icon class="mr10 pointer icon-collapse" @click="onCollapseClick">
		<Fold v-if="!isCollapse" />
		<Expand v-else />
	</el-icon>
</template>

<script setup>
import { storeToRefs } from 'pinia'
import { useCollapseStore } from '@/store/modules/collapse'
import { Fold, Expand } from '@element-plus/icons-vue'

const collapseStore = useCollapseStore()
const { isCollapse } = storeToRefs(collapseStore)

const onCollapseClick = () => {
	collapseStore.updateCollapse()
}
</script>

<style lang="scss" scoped>
.icon-collapse {
	color: var(--fangmaster-bg-topBarColor);
}
</style>
