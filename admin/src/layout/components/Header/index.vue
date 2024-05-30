<template>
	<div class="layout-header">
		<div class="header-left">
			<Collapse />
			<Breadcrumb />
		</div>
		<div class="header-right">
			<div class="handle-icons">
				<Fullscreen />
			</div>
			<el-dropdown>
				<span class="el-dropdown-link user">
					<img class="user-avatar mr8" :src="userInfo.avatar" />
					<span class="user-name mr6">{{ userInfo.username }}</span>
					<el-icon :size="16"><CaretBottom /></el-icon>
				</span>
				<template #dropdown>
					<el-dropdown-menu>
						<el-dropdown-item @click="onLoginOutClick">退出登陆</el-dropdown-item>
					</el-dropdown-menu>
				</template>
			</el-dropdown>
		</div>
	</div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { storeToRefs } from 'pinia'
import { CaretBottom } from '@element-plus/icons-vue'
import Collapse from '../Collapse/index.vue'
import Breadcrumb from '../Breadcrumb/index.vue'
import Fullscreen from '../Fullscreen/index.vue'
import useConfirm from '@/hooks/useConfirm'
import { removeToken } from '@/utils/auth.js'
import useUserStore from '@/store/modules/user.js'

const router = useRouter()
const useUser = useUserStore()
const { userInfo } = storeToRefs(useUser)

const onLoginOutClick = async () => {
	try {
		await useConfirm('确定要退出登录吗?')
		await removeToken()
		localStorage.removeItem('botTypeList')
		localStorage.removeItem('userInfo')
		router.replace('/login')
	} catch (error) {
		console.log(error)
	}
}
</script>

<style lang="scss" scoped>
.layout-header {
	width: 100%;
	display: flex;
	align-items: center;
	justify-content: space-between;

	.header-left {
		margin-left: 12px;
		display: flex;
		align-items: center;
	}

	.header-right {
		height: 40px;
		margin-right: 26px;
		display: flex;
		align-items: center;

		.handle-icons {
			margin-right: 10px;
			display: flex;
			align-items: center;
		}

		.user {
			display: flex;
			align-items: center;
			cursor: pointer;

			.user-avatar {
				width: 28px;
				height: 28px;
				border-radius: 100%;
				object-fit: cover;
			}

			.user-name {
				font-size: 14px;
				font-weight: 400;
			}
		}
	}
}
</style>
