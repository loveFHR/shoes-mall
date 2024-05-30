<template>
	<van-nav-bar z-index="100" :title fixed placeholder safe-area-inset-top @click-left="onBackClick">
		<template #left v-if="arrowLeft">
			<van-icon name="arrow-left" color="rgba(0, 0, 0, 0.90)" size="20px" />
		</template>
	</van-nav-bar>
</template>

<script setup>
defineProps({
	title: {
		type: String,
		default: () => {
			return ''
		}
	},
	arrowLeft: {
		type: Boolean,
		default: () => {
			return true
		}
	}
})

const router = useRouter()

const onBackClick = () => {
	router.go(-1)
}
</script>
