<template>
	<div class="layout-tabsview">
		<el-scrollbar ref="scrollbarRef">
			<ul ref="tagsUlRef" class="layout-tabsview-ul">
				<li
					v-for="(item, index) in tabsStore.getTabList"
					:key="item.path"
					:ref="
						(el) => {
							if (el) tagsRefs[index] = el
						}
					"
					class="layout-tabsview-li"
					:class="{ 'is-active': isActive(item) }"
					@click="onTabClick(item, index)"
					@contextmenu.prevent="onContextmenu(item, $event)"
				>
					<span>{{ item.title }}</span>
					<div class="iconfont" v-if="item.path !== '/dashboard'">
						<SvgIcon class="icon" :size="18" name="tabs-icon-close" @click.stop="onRemoveTab(item)" />
					</div>
				</li>
			</ul>
		</el-scrollbar>
		<Contextmenu ref="contextmenuRef" :dropdown="state.dropdown" @currentContextmenuClick="onCurrentContextmenuClick" />
	</div>
</template>

<script setup>
import { ref, reactive, watch, onMounted, defineAsyncComponent, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useTabsStore } from '@/store/modules/tabs.js'

const Contextmenu = defineAsyncComponent(() => import('./contextmenu.vue'))

const route = useRoute()
const router = useRouter()
const tabsStore = useTabsStore()

const tagsRefs = ref([])
const scrollbarRef = ref()
const contextmenuRef = ref()
const tagsUlRef = ref()

const activeName = ref('/dashboard')

const state = reactive({
	dropdown: { x: '', y: '' },
	tagsRefsIndex: 0
})

onMounted(() => {
	activeName.value = route.path
	tabsStore.addTab({
		path: route.path,
		title: route.meta.title
	})
})

watch(
	() => route.path,
	(val) => {
		activeName.value = val
		tabsStore.addTab({
			path: route.path,
			title: route.meta.title
		})
		const index = tabsStore.getTabList.findIndex((v) => v.path === val)
		if (index !== -1) {
			state.tagsRefsIndex = tabsStore.getTabList.length
		}
		state.tagsRefsIndex = index
		tagsViewmoveToCurrentTag()
	}
)

// tab点击事件
const onTabClick = (item, index) => {
	state.tagsRefsIndex = index
	router.push(item.path)
	tagsViewmoveToCurrentTag()
}

// tab删除事件
const onRemoveTab = async (item) => {
	tabsStore.onDel(item)
	const length = tabsStore.getTabList.length - 1
	router.push(tabsStore.getTabList[length].path)
}
// 删除其他tab
const onCloseOtherTabs = async (item) => {
	await tabsStore.onDelOtherTab(item)
	if (item.path !== route.path) {
		router.push({ path: item.path })
	}
}
// 删除所有
const onDelAllTabs = async () => {
	await tabsStore.onDelAllTab()
	router.push({ path: '/' })
}

// 设置高量
const isActive = (item) => {
	if (item.path === route.path) {
		return true
	} else {
		return false
	}
}

// 右键点击时：传 x,y 坐标值到子组件中（props）
const onContextmenu = (v, e) => {
	const { clientX, clientY } = e
	state.dropdown.x = clientX
	state.dropdown.y = clientY
	contextmenuRef.value.openContextmenu(v)
}

// 当前项右键菜单点击
const onCurrentContextmenuClick = async (item) => {
	switch (item.contextMenuClickId) {
		case 0:
			// 刷新当前页
			console.log(router)
			router.go(0)
			break
		case 1:
			// 关闭当前
			if (item.path === '/dashboard') return
			onRemoveTab(item)
			break
		case 2:
			// 关闭其它
			onCloseOtherTabs(item)
			break
		case 3:
			// 关闭全部
			onDelAllTabs()
			break
	}
}
// tagsView 横向滚动
const tagsViewmoveToCurrentTag = () => {
	nextTick(() => {
		if (tagsRefs.value.length <= 0) return false
		// 当前 li 元素
		const liDom = tagsRefs.value[state.tagsRefsIndex]
		// 当前 li 元素下标
		const liIndex = state.tagsRefsIndex
		// 当前 ul 下 li 元素总长度
		const liLength = tagsRefs.value.length
		// 最前 li
		const liFirst = tagsRefs.value[0]
		// 最后 li
		const liLast = tagsRefs.value[tagsRefs.value.length - 1]
		// 当前滚动条的值
		const scrollRefs = scrollbarRef.value.$refs.wrapRef
		// 当前滚动条滚动宽度
		const scrollS = scrollRefs.scrollWidth
		// 当前滚动条偏移宽度
		const offsetW = scrollRefs.offsetWidth
		// 当前滚动条偏移距离
		const scrollL = scrollRefs.scrollLeft
		// 上一个 tags li dom
		const liPrevTag = tagsRefs.value[state.tagsRefsIndex - 1]
		// 下一个 tags li dom
		const liNextTag = tagsRefs.value[state.tagsRefsIndex + 1]
		// 上一个 tags li dom 的偏移距离
		let beforePrevL = 0
		// 下一个 tags li dom 的偏移距离
		let afterNextL = 0
		if (liDom === liFirst) {
			// 头部
			scrollRefs.scrollLeft = 0
		} else if (liDom === liLast) {
			// 尾部
			scrollRefs.scrollLeft = scrollS - offsetW
		} else {
			// 非头/尾部
			if (liIndex === 0) beforePrevL = liFirst.offsetLeft - 5
			else beforePrevL = liPrevTag?.offsetLeft - 5
			if (liIndex === liLength) afterNextL = liLast.offsetLeft + liLast.offsetWidth + 5
			else afterNextL = liNextTag.offsetLeft + liNextTag.offsetWidth + 5
			if (afterNextL > scrollL + offsetW) {
				scrollRefs.scrollLeft = afterNextL - offsetW
			} else if (beforePrevL < scrollL) {
				scrollRefs.scrollLeft = beforePrevL
			}
		}
		// 更新滚动条，防止不出现
		scrollbarRef.value.update()
	})
}
</script>

<style lang="scss" scoped>
.layout-tabsview {
	position: relative;
	border-top: 1px solid var(--el-border-color-lighter);
	border-bottom: 1px solid var(--el-border-color-lighter);
	background-color: var(--el-color-white);

	:deep(.el-scrollbar__wrap) {
		overflow-x: auto !important;
	}

	&-ul {
		height: 34px;
		display: flex;
		align-items: center;
		font-size: 12px;
		color: var(--el-text-color-regular);
		white-space: nowrap;
		padding: 0 15px;
	}

	&-li {
		height: 26px;
		line-height: 26px;
		display: flex;
		align-items: center;
		padding: 0 15px;
		margin-right: 5px;
		border-radius: 2px;
		position: relative;
		z-index: 0;
		cursor: pointer;
		justify-content: space-between;

		&:hover {
			background-color: var(--el-color-primary-light-9);
			color: var(--el-color-primary);
			border-color: var(--el-color-primary-light-5);
		}

		.iconfont {
			border-radius: 100%;
			position: relative;
			height: 14px;
			width: 14px;
			text-align: center;
			line-height: 14px;
			right: -5px;
			display: flex;
			align-items: center;
			justify-content: center;
			.icon {
				color: #ffffff !important;
			}
			&:hover {
				color: var(--el-color-white);
				background-color: var(--el-color-primary-light-3);
			}
		}
	}

	.is-active {
		color: var(--el-color-white);
		background: var(--el-color-primary);
		border-color: var(--el-color-primary);
		transition: border-color 3s ease;
	}
}
</style>
