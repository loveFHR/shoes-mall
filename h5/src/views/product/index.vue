<template>
	<div class="page product">
		<NavBar title="商品详情" />

		<div class="content" v-if="Object.keys(info).length > 0">
			<Swipe :info />
			<div class="item">
				<div class="title">
					{{ info.title }}
				</div>
				<div class="price">
					<div>
						¥<span>{{ info.nowPrice }}</span>
					</div>
					<span class="old-price" v-if="info.nowPrice != info.usedPrice">¥{{ info.usedPrice }}</span>
				</div>
			</div>
			<div class="item">
				<div class="lable">颜色</div>
				<div class="item-content size">
					<div
						:class="['tag', state.colorIndex === index ? 'active' : '']"
						v-for="(item, index) in info.systemBaseSettingColorLabelList"
						:key="item"
						@click="onColorClick(index)"
					>
						{{ item }}
					</div>
				</div>
				<div class="lable mt40">鞋码</div>
				<div class="item-content size">
					<div
						:class="['tag', state.sizeIndex === index ? 'active' : '']"
						v-for="(item, index) in info.systemBaseSettingSizeLabelList"
						:key="item"
						@click="onSizeClick(index)"
					>
						{{ item }}
					</div>
				</div>
				<div class="lable mt40">
					<span>数量</span>
					<div class="number">
						<span class="icon" @click="onSubtractClick">-</span>
						<span class="num">{{ state.form.num }}</span>
						<span class="icon" @click="onAugmentClick">+</span>
					</div>
				</div>
			</div>

			<div class="item comment">
				<div class="lable">宝贝评价({{ info.commonList.length || 0 }})</div>
				<div class="item-content">
					<Comment :list="info.commonList" />
				</div>
			</div>

			<div class="item desc">
				<Markdown v-model="info.desc" mode="preview" height="auto" />
			</div>
		</div>
		<div class="footer" v-if="Object.keys(info).length > 0">
			<van-action-bar>
				<van-action-bar-icon icon="cart-o" text="购物车" @click="onCartClick" />
				<van-action-bar-button type="warning" text="加入购物车" @click="onAddCartClick" />
				<van-action-bar-button type="danger" text="立即购买" @click="onPurchaseClick" />
			</van-action-bar>
		</div>
	</div>
</template>

<script setup>
import Swipe from './components/Swipe.vue'
import Comment from './components/Comment.vue'
import Markdown from '@/components/Markdown/index.vue'
import useToast from '@/hooks/useToast'
import useLoading from '@/hooks/useLoading'
import { isLogin } from '@/utils/auth.js'
import { info as getInfo } from '@/api/product.api.js'
import { addCart } from '@/api/cart.api.js'

const route = useRoute()
const router = useRouter()
const { loading, closeLoading } = useLoading()
const { toast } = useToast()

const info = ref({})

const state = reactive({
	colorIndex: null,
	sizeIndex: null,
	form: {
		productId: null,
		systemBaseSettingColorId: null, // 颜色
		systemBaseSettingSizeId: null, // 尺码
		price: null,
		num: 1,
		addressId: null
	}
})

onMounted(() => {
	queryDetails()
})

const queryDetails = async () => {
	try {
		loading('加载中...')
		const { data } = await getInfo(route.params.id)
		info.value = data
		state.form.productId = data.id
		state.form.price = data.nowPrice
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

const onPurchaseClick = async () => {
	try {
		await isLogin()
		await validateData()
		const obj = {
			productId: info.value.id,
			cover: info.value.cover,
			title: info.value.title,
			color: info.value.systemBaseSettingColorLabelList[state.colorIndex],
			size: info.value.systemBaseSettingSizeLabelList[state.sizeIndex],
			colorId: state.form.systemBaseSettingColorId,
			sizeId: state.form.systemBaseSettingSizeId,
			num: state.form.num,
			nowPrice: info.value.nowPrice
		}
		localStorage.setItem('ProductInfo', JSON.stringify([obj]))
		router.push({
			path: '/product/purchase'
		})
	} catch (error) {
		console.log(error)
	}
}

const onAddCartClick = async () => {
	try {
		await isLogin()
		await validateData()
		loading('加载中...')
		await addCart({
			productId: info.value.id,
			systemBaseSettingSize: state.form.systemBaseSettingSizeId,
			systemBaseSettingColor: state.form.systemBaseSettingColorId,
			num: state.form.num,
			price: info.value.nowPrice * state.form.num
		})
		toast('加入购物车成功')
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

const validateData = () => {
	return new Promise((resolve, reject) => {
		if (state.form.systemBaseSettingColorId == null) {
			toast('请选择颜色')
			reject()
			return
		}
		if (state.form.systemBaseSettingSizeId == null) {
			toast('请选择尺码')
			reject()
			return
		}
		resolve()
	})
}

const onCartClick = () => {
	router.replace({
		path: '/cart'
	})
}

const onColorClick = (index) => {
	if (state.colorIndex == index) {
		state.colorIndex = null
		state.systemBaseSettingColorId = null
	} else {
		state.colorIndex = index
		state.form.systemBaseSettingColorId = info.value.systemBaseSettingColorList[index]
	}
}

const onSizeClick = (index) => {
	if (state.sizeIndex == index) {
		state.sizeIndex = null
		state.systemBaseSettingSizeId = null
	} else {
		state.sizeIndex = index
		state.form.systemBaseSettingSizeId = info.value.systemBaseSettingSizeList[index]
	}
}

const onSubtractClick = () => {
	if (state.form.num == 1) {
		return toast('最少购买一件')
	}
	state.form.num--
}

const onAugmentClick = () => {
	if (state.form.num > info.value.restNum) {
		return toast(`最多购买${info.value.restNum}件`)
	}
	state.form.num++
}
</script>

<style lang="scss" scoped>
.product {
	.content {
		padding-bottom: 120px;
		.item {
			width: 686px;
			margin: 0 auto;
			background-color: #ffffff;
			border-radius: 10px;
			padding: 40px 30px;
			box-sizing: border-box;
			margin-top: 20px;
			.lable {
				font-size: 30px;
				color: #000;
				font-weight: 500;
				display: flex;
				align-items: center;
				justify-content: space-between;
			}
			.item-content {
				margin-top: 30px;
			}
		}
		.number {
			display: flex;
			line-height: 50px;
			align-items: center;
			.icon {
				font-size: 40px;
				font-weight: 400;
			}
			.num {
				height: 50px;
				background-color: #efefef;
				padding: 0 24px;
				border-radius: 4px;
				margin: 0 20px;
				font-size: 26px;
			}
		}
		.title {
			font-size: 30px;
			color: #1f1f1f;
			line-height: 50px;
			font-weight: 500;
		}
		.price {
			padding: 0 10px;
			box-sizing: border-box;
			color: rgb(255, 98, 0);
			margin-top: 20px;
			font-weight: bold;
			font-size: 28px;
			display: flex;
			align-items: center;
			span {
				margin-left: 6px;
				font-size: 32px;
			}
			.old-price {
				font-weight: 400;
				color: #999;
				font-size: 28px;
				text-decoration-line: line-through;
				margin-left: 10px;
			}
		}
		.size {
			display: flex;
			flex-wrap: wrap;
			.tag {
				font-size: 30px;
				height: 64px;
				line-height: 64px;
				border-radius: 8px;
				padding: 0 28px;
				margin-right: 10px;
				background-color: #f5f5f5;
				color: #000000;
				box-sizing: border-box;
				border: 1px solid #f5f5f5;
				margin-bottom: 20px;
			}
			.active {
				background-color: #ffffff;
				border: 1px solid rgb(255, 80, 0);
				color: rgb(255, 80, 0);
			}
		}
		.desc {
			padding: 0;
		}
	}
	.footer {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
	}
}
</style>
