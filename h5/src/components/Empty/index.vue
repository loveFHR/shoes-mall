<template>
	<div class="empty">
		<img src="@/assets/img-empty.png" alt="" />
		<div class="text">{{ text }}</div>
	</div>
</template>

<script setup>
defineProps({
	text: {
		type: String,
		default: () => {
			return '暂无内容'
		}
	}
})
</script>

<style lang="scss" scoped>
.empty {
	display: flex;
	flex-direction: column;
	align-items: center;
	margin-top: 100px;
	img {
		width: 300px;
	}
	.text {
		margin-top: 20px;
		font-size: 28px;
		color: #999999;
	}
}
</style>
