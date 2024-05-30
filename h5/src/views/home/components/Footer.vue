<template>
	<div class="home-footer">
		<div class="title">品质赢得顾客信赖</div>
		<div class="desc">
			十五年专心如一日，推动“货真价实、物关价廉、按需定制”网货的普及是我们的使命，通过缩减渠道成本、时间成本等综合购物成本，我们帮助更多的人享用网货，获得更高的生活品质;通过提供销售平台、营销、支付、技术等全套服务，我们帮助更多的企业开拓内销市场、建立品牌，实现产业升级。我们的出现将为整个网络购物市场打造一个透明、诚信、公正、公开的交易平台，进而影响人们的购物消费习惯，推动线下市场以及生产流通环节的透明、诚信，从而衍生出一个“开放、透明、分享、责任”的新商业文明。
		</div>
		<div class="ad">
			<img v-for="item in list" :key="item.id" :src="item.cover" alt="" />
		</div>
	</div>
</template>

<script setup>
import { list as getAdList } from '@/api/ad.api.js'

const list = ref([])

onMounted(() => {
	queryList()
})

const queryList = async () => {
	try {
		const { data } = await getAdList()
		console.log(data)
		list.value = data
	} catch (error) {
		console.log(error)
	}
}
</script>

<style lang="scss" scoped>
.home-footer {
	display: flex;
	flex-direction: column;
	align-items: center;
	text-align: center;
	margin-top: 100px;
	margin-bottom: 40px;
	.title {
		font-size: 32px;
		font-weight: 500;
		color: rgb(255, 98, 0);
	}
	.desc {
		padding: 0 32px;
		box-sizing: border-box;
		font-size: 26px;
		line-height: 42px;
		color: #999999;
		margin-top: 40px;
	}
	.ad {
		padding: 0 32px;
		box-sizing: border-box;
		margin-top: 40px;
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		img {
			width: 32%;
			height: 140px;
			margin-bottom: 16px;
			object-fit: cover;
		}
	}
}
</style>
