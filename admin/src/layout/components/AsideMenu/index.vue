<template>
	<el-aside class="aside" :width="isCollapse ? '64px' : '180px'">
		<el-scrollbar height="calc(100vh - 64px)">
			<el-menu
				router
				unique-opened
				:collapse-transition="false"
				:collapse="isCollapse"
				:default-active="defaultActive"
				background-color="#1D2129"
				text-color="#ffffff"
			>
				<menu-item :routes="constantRouterMap" />
			</el-menu>
		</el-scrollbar>
	</el-aside>
</template>

<script setup>
import { ref, onBeforeMount, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { storeToRefs } from 'pinia'
// import {useUserStore} from "@/store/modules/user";
import { useCollapseStore } from '@/store/modules/collapse'
import MenuItem from './MenuItem.vue'
import { constantRouterMap } from '@/router'

const router = useRouter()
const route = useRoute()
// const userStore = useUserStore();
const collapseStore = useCollapseStore()
// const {getMenuList} = storeToRefs(userStore);
const { isCollapse } = storeToRefs(collapseStore)

const defaultActive = ref('/dashboard')

onBeforeMount(() => {
	const activeMenu = router.currentRoute.value.meta?.activeMenu
	if (activeMenu) {
		defaultActive.value = activeMenu
		return
	}
	defaultActive.value = router.currentRoute.value.path
})

watch(
	() => route.path,
	(val) => {
		if (route?.meta?.activeMenu) {
			defaultActive.value = route?.meta?.activeMenu
		} else {
			defaultActive.value = val
		}
	}
)
</script>

<style lang="scss" scoped>
.logo {
	width: 100%;
	height: 64px;
	display: flex;
	align-items: center;

	.logo-img {
		width: 24px;
		height: 24px;
		margin-left: 20px;
	}

	h1 {
		font-size: 16px;
		line-height: 20px;
		font-weight: 400;
		color: var(--el-color-primary);
		margin-left: 10px;
	}
}
</style>
