<template>
	<template v-for="item in routes" :key="item.path">
		<template v-if="item.children && item.children.length > 0">
			<el-sub-menu v-if="item.alwaysShow" :index="item.path">
				<template #title>
					<template v-if="item?.meta?.icon">
						<el-icon>
							<SvgIcon :name="item.meta.icon" />
						</el-icon>
					</template>
					<span>{{ item?.meta?.title }}</span>
				</template>
				<menu-item :routes="item.children" />
			</el-sub-menu>
			<menu-item v-else :routes="item.children" />
		</template>

		<template v-else>
			<el-menu-item v-if="!item.hidden" :index="item.path" @click="onMenuItemClick(item)">
				<template v-if="item?.meta?.icon">
					<el-icon>
						<SvgIcon :name="item.meta.icon" />
					</el-icon>
				</template>
				<span>{{ item?.meta?.title }}</span>
			</el-menu-item>
		</template>
	</template>
</template>

<script setup>
import { useTabsStore } from '@/store/modules/tabs.js'

defineProps({
	routes: {
		type: Array,
		default: () => {
			return []
		}
	}
})

const store = useTabsStore()

const onMenuItemClick = (item) => {
	const data = {
		title: item.meta.title,
		path: item.path
	}
	store.addTab(data)
}
</script>
