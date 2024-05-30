<template>
	<transition name="el-zoom-in-center">
		<div
			v-show="state.isShow"
			:key="Math.random()"
			aria-hidden="true"
			class="el-dropdown__popper el-popper is-light is-pure custom-contextmenu"
			role="tooltip"
			data-popper-placement="bottom"
			:style="`top: ${dropdowns.y + 5}px;left: ${dropdowns.x}px;`"
		>
			<ul class="el-dropdown-menu">
				<template v-for="(v, k) in state.dropdownList">
					<li
						v-if="!v.affix"
						:key="k"
						class="el-dropdown-menu__item"
						aria-disabled="false"
						tabindex="-1"
						@click="onCurrentContextmenuClick(v.contextMenuClickId)"
					>
						<SvgIcon :name="v.icon" />
						<span>{{ v.txt }}</span>
					</li>
				</template>
			</ul>
			<div class="el-popper__arrow" :style="{ left: `${state.arrowLeft}px` }" />
		</div>
	</transition>
</template>

<script setup>
import { reactive, computed, onMounted, onUnmounted, watch } from 'vue'

const props = defineProps({
	dropdown: {
		type: Object,
		default: () => {
			return {
				x: 0,
				y: 0
			}
		}
	}
})

const emit = defineEmits(['currentContextmenuClick'])

const state = reactive({
	isShow: false,
	dropdownList: [
		{
			contextMenuClickId: 0,
			txt: '刷新页面',
			affix: false,
			icon: 'tabs-icon-refresh'
		},
		{
			contextMenuClickId: 1,
			txt: '关闭当前',
			affix: false,
			icon: 'tabs-icon-close'
		},
		{
			contextMenuClickId: 2,
			txt: '关闭其它',
			affix: false,
			icon: 'tabs-icon-close2'
		},
		{
			contextMenuClickId: 3,
			txt: '全部关闭',
			affix: false,
			icon: 'tabs-icon-folder-delete'
		}
	],
	item: {},
	arrowLeft: 10
})
// 父级传过来的坐标 x,y 值
const dropdowns = computed(() => {
	// 117 为 `Dropdown 下拉菜单` 的宽度
	if (props.dropdown.x + 117 > document.documentElement.clientWidth) {
		return {
			x: document.documentElement.clientWidth - 117 - 5,
			y: props.dropdown.y
		}
	} else {
		return props.dropdown
	}
})
// 当前项菜单点击
const onCurrentContextmenuClick = (contextMenuClickId) => {
	emit('currentContextmenuClick', Object.assign({}, { contextMenuClickId }, state.item))
}
// 打开右键菜单：判断是否固定，固定则不显示关闭按钮
const openContextmenu = (item) => {
	state.item = item
	closeContextmenu()
	setTimeout(() => {
		state.isShow = true
	}, 10)
}
// 关闭右键菜单
const closeContextmenu = () => {
	state.isShow = false
}
// 监听页面监听进行右键菜单的关闭
onMounted(() => {
	document.body.addEventListener('click', closeContextmenu)
})
// 页面卸载时，移除右键菜单监听事件
onUnmounted(() => {
	document.body.removeEventListener('click', closeContextmenu)
})
// 监听下拉菜单位置
watch(
	() => props.dropdown,
	({ x }) => {
		if (x + 117 > document.documentElement.clientWidth) {
			state.arrowLeft = 117 - (document.documentElement.clientWidth - x)
		} else {
			state.arrowLeft = 10
		}
	},
	{
		deep: true
	}
)

// 暴露变量
defineExpose({
	openContextmenu
})
</script>

<style lang="scss" scoped>
.custom-contextmenu {
	transform-origin: center top;
	z-index: 2190;
	position: fixed;

	.el-dropdown-menu__item {
		font-size: 12px !important;
		white-space: nowrap;
		i {
			font-size: 12px !important;
		}
		span {
			margin-left: 6px;
		}
	}
}
</style>
