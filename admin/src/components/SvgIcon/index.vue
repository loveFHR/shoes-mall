<template>
	<svg :class="svgClass" aria-hidden="true" :style="style">
		<use :xlink:href="iconName" />
	</svg>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
	name: {
		type: String,
		default: () => {
			return ''
		}
	},
	className: {
		type: String,
		default: ''
	},
	color: {
		type: String,
		default: '#333333'
	},
	size: {
		type: Number,
		default: () => {
			return 32
		}
	}
})

const iconName = computed(() => {
	return `#icon-${props.name}`
})

const svgClass = computed(() => {
	if (props.className) {
		return `svg-icon ${props.className}`
	}
	return 'svg-icon'
})

const style = computed(() => {
	return `width: ${props.size / 2}px; height: ${props.size / 2}px;color: ${props.color}`
})
</script>

<style lang="scss" scoped>
.svg-icon {
	width: 1em;
	height: 1em;
	vertical-align: middle;
	outline: none;
}
</style>
