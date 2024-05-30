<template>
	<van-swipe class="swipe" :autoplay="5000" indicator-color="white">
		<van-swipe-item v-for="item in info.mainPicture.split(',')" :key="item">
			<img :src="item" />
		</van-swipe-item>
	</van-swipe>
</template>

<script setup>
defineProps({
	info: {
		type: Object,
		default: () => {
			return {}
		}
	}
})
</script>

<style lang="scss" scoped>
.swipe {
	width: 100%;
	height: auto;
	img {
		width: 100%;
		height: 100%;
	}
}
</style>
