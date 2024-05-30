<template>
	<div class="page">
		<NavBar :title="route.meta.title" :arrowLeft="false" />
		<router-view class="tabbar" />

		<van-tabbar fixed route safe-area-inset-bottom placeholder v-model="active">
			<van-tabbar-item v-for="item in tabbarList" :key="item.path" replace :to="item.path">
				<span>{{ item.name }}</span>
				<template #icon="props">
					<img :src="props.active ? item.iconActive : item.icon" />
				</template>
			</van-tabbar-item>
		</van-tabbar>
	</div>
</template>

<script setup>
import Bot from '@/assets/icons/tabbar/bot.png'
import BotActive from '@/assets/icons/tabbar/bot_active.png'
import Buy from '@/assets/icons/tabbar/buy.png'
import BuyActive from '@/assets/icons/tabbar/buy_active.png'
import My from '@/assets/icons/tabbar/my.png'
import MyActive from '@/assets/icons/tabbar/my_active.png'

const route = useRoute()

const active = ref('/home')
const tabbarList = [
	{
		name: '首页',
		icon: Bot,
		iconActive: BotActive,
		path: '/home'
	},
	{
		name: '购物车',
		icon: Buy,
		iconActive: BuyActive,
		path: '/cart'
	},
	{
		name: '我的',
		icon: My,
		iconActive: MyActive,
		path: '/my'
	}
]
</script>

<style lang="scss" scoped>
.tabbar {
	width: 100%;
	min-height: calc(100vh - 194px);
	background-color: rgba(243, 244, 244, 1);
	overflow-y: auto;
}
</style>
