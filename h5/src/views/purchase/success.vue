<template>
	<div class="page success">
		<NavBar title="支付成功" />
		<div class="content">
			<div class="title">支付成功</div>
			<van-button type="primary" size="small" @click="router.replace('/home')">返回首页</van-button>
		</div>
	</div>
</template>

<script setup>
const router = useRouter()

onMounted(() => {
	localStorage.removeItem('addressId')
	localStorage.removeItem('ProductInfo')
})
</script>

<style lang="scss" scoped>
.page {
	background-color: #ffffff;
	.content {
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		display: flex;
		flex-direction: column;
		align-items: center;
		.title {
			font-size: 48px;
			font-weight: 500;
		}
		.van-button {
			margin-top: 60px;
		}
	}
}
</style>
