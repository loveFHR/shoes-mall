<template>
	<el-breadcrumb>
		<transition-group name="breadcrumb">
			<el-breadcrumb-item v-for="item in breadcrumbList" :key="item.path">
				{{ item.meta.title }}
			</el-breadcrumb-item>
		</transition-group>
	</el-breadcrumb>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const breadcrumbList = ref([])

onMounted(() => {
	getBreadcrumbList()
})

watch(
	() => route.path,
	() => {
		getBreadcrumbList()
	}
)

const getBreadcrumbList = () => {
	let matched = route.matched.filter((v) => v.meta && v.meta.title)
	const frist = matched[0]
	if (frist?.path !== '/dashboard') {
		matched = [{ path: '/dashboard', meta: { title: '首页' } }].concat(matched)
	}
	breadcrumbList.value = matched
}
</script>
