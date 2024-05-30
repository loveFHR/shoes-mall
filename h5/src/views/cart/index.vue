<template>
	<div class="page cart">
		<Empty v-if="state.list.length == 0 && getToken() && !isLoading" />
		<div class="no-login" v-if="!getToken()">
			<div class="title">先去登录嘛</div>
			<div class="desc">别忘了买点什么东西犒劳一下自己哦</div>
			<van-button size="small" type="primary" @click="router.push({ path: '/login' })">去登录</van-button>
		</div>
		<div class="content" v-if="state.list.length != 0">
			<template v-for="(item, index) in state.list" :key="item.id">
				<div class="cart-item" @click="onDetailsClick(item)">
					<svg-icon
						:name="item.active ? 'icon-check-active' : 'icon-check'"
						:size="50"
						@click.stop="onCheckItemClick(index)"
					/>
					<div class="info">
						<img :src="item.cover" class="pic" />
						<div class="info-content">
							<div class="title">{{ item.title }}</div>
							<div class="tag">
								<van-tag plain type="primary">{{ item.systemBaseSettingColorLabel }}</van-tag>
								<van-tag plain type="primary">{{ item.systemBaseSettingSizeLabel }}</van-tag>
							</div>
							<div class="price">
								<span>¥{{ item.nowPrice }}</span>
								<div class="number">
									<span class="icon" @click.stop="onSubtractClick(index)">-</span>
									<span class="num">{{ item.num }}</span>
									<span class="icon" @click.stop="onAugmentClick(index)">+</span>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="del-btn" @click="onDelClick(item)">删除</div>
			</template>
		</div>

		<div class="footer" v-if="state.list.length != 0">
			<div class="left" @click="onAllSelectClick">
				<svg-icon :name="state.isAll ? 'icon-check-active' : 'icon-check'" :size="50" />
				<span style="margin-left: 4px">{{ state.isAll ? '取消全选' : '全选' }}</span>
			</div>
			<div class="right">
				<span
					>合计: <span style="color: rgb(255, 98, 0); font-size: 24px">{{ state.price }}</span></span
				>
				<div class="btn" @click="onBuyClick">结算</div>
			</div>
		</div>
	</div>
</template>

<script setup>
import { showConfirmDialog } from 'vant'
import { getToken } from '@/utils/auth.js'
import useLoading from '@/hooks/useLoading'
import useToast from '@/hooks/useToast'
import { list, editCart, del } from '@/api/cart.api.js'

const router = useRouter()
const { toast } = useToast()
const { loading, closeLoading, isLoading } = useLoading()

const state = reactive({
	price: 0,
	list: [],
	isAll: false
})

onMounted(() => {
	if (getToken()) {
		queryList()
	}
})

const onDelClick = async (item) => {
	try {
		await showConfirmDialog({
			title: '警告',
			message: '确定要删除该购物车数据吗?'
		})
		loading()
		await del(item.id)
		toast('删除成功')
		queryList()
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

const queryList = async () => {
	try {
		loading('加载中...')
		const { data } = await list()
		console.log(data)
		data.map((item) => {
			item.active = false
		})
		state.list = data
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

const onBuyClick = () => {
	const list = state.list.filter((v) => v.active)
	if (list.length == 0) return
	let arr = []
	list.map((item) => {
		arr.push({
			cover: item.cover,
			title: item.title,
			color: item.systemBaseSettingColorLabel,
			size: item.systemBaseSettingSizeLabel,
			colorId: item.systemBaseSettingColor,
			sizeId: item.systemBaseSettingSize,
			num: item.num,
			nowPrice: item.nowPrice,
			cartId: item.id,
			productId: item.productId
		})
	})
	localStorage.setItem('ProductInfo', JSON.stringify(arr))
	router.push({
		path: '/product/purchase'
	})
}

const onAllSelectClick = async () => {
	state.isAll = !state.isAll
	state.list.map((item) => {
		item.active = state.isAll
	})
	await countPrice()
}

const onCheckItemClick = (index) => {
	state.list[index].active = !state.list[index].active
	countPrice()
}

const countPrice = () => {
	const list = state.list.filter((v) => v.active)
	if (list.length == state.list.length) {
		state.isAll = true
	} else {
		state.isAll = false
	}
	if (list.length == 0) {
		state.price = 0
	} else {
		let allPrice = 0
		list.map((item) => {
			const price = item.num * item.nowPrice
			allPrice += price
		})
		state.price = allPrice
	}
}

const onSubtractClick = async (index) => {
	if (state.list[index].num == 1) {
		return toast('最少购买一件')
	}
	state.list[index].num--

	const data = state.list[index]
	const obj = {
		id: data.id,
		productId: data.productId,
		systemBaseSettingSize: data.systemBaseSettingSize,
		systemBaseSettingColor: data.systemBaseSettingColor,
		num: data.num
	}
	onEditCart(obj)
	await countPrice()
}

const onAugmentClick = async (index) => {
	if (state.list[index].num == state.list[index].restNum) {
		return toast(`最多购买${state.list[index].restNum}件`)
	}
	state.list[index].num++

	const data = state.list[index]
	const obj = {
		id: data.id,
		productId: data.productId,
		systemBaseSettingSize: data.systemBaseSettingSize,
		systemBaseSettingColor: data.systemBaseSettingColor,
		num: data.num
	}
	onEditCart(obj)
	await countPrice()
}

const onEditCart = async (obj) => {
	try {
		editCart(obj)
	} catch (error) {
		console.log(error)
	}
}

const onDetailsClick = (item) => {
	router.push({
		path: '/product/' + item.id
	})
}
</script>

<style lang="scss" scoped>
.cart {
	.no-login {
		display: flex;
		flex-direction: column;
		align-items: center;
		position: absolute;
		top: 50%;
		left: 50%;
		width: 100%;
		transform: translate(-50%, -50%);
		.title {
			font-size: 32px;
			font-weight: 500;
		}
		.desc {
			margin-top: 20px;
			color: #999;
			font-size: 26px;
			margin-bottom: 30px;
		}
		.van-button {
			width: 200px;
		}
	}
	.content {
		width: 100%;
		background-color: #ffffff;
		padding: 30px 0;
		padding-bottom: 120px;
		box-sizing: border-box;
		.cart-item {
			width: 686px;
			margin: 0 auto;
			display: flex;
			align-items: center;
			margin-bottom: 30px;
			.info {
				flex: 1;
				margin-left: 20px;
				display: flex;
				.pic {
					width: 200px;
					height: 200px;
					object-fit: cover;
					border-radius: 8px;
					margin-right: 20px;
				}
				.info-content {
					flex: 1;
					position: relative;
					.title {
						font-size: 26px;
						color: #1f1f1f;
						word-break: break-all;
					}
					.tag {
						margin-top: 6px;
					}
					.van-tag {
						margin-right: 10px;
						padding: 0 12px;
					}
					.price {
						display: flex;
						align-items: center;
						justify-content: space-between;
						position: absolute;
						bottom: 0;
						left: 0;
						right: 0;
						color: rgb(255, 80, 0);
						font-size: 28px;
						font-weight: 500;
						.number {
							display: flex;
							line-height: 40px;
							align-items: center;
							.icon {
								font-size: 40px;
								font-weight: 400;
							}
							.num {
								background-color: #efefef;
								padding: 0 24px;
								border-radius: 4px;
								margin: 0 20px;
								font-size: 24px;
							}
						}
					}
				}
			}
		}
	}
	.del-btn {
		text-align: right;
		margin-top: 20px;
		margin-right: 32px;
		font-size: 28px;
		color: #f00;
		margin-bottom: 30px;
	}
	.footer {
		position: fixed;
		bottom: 100px;
		left: 0;
		right: 0;
		display: flex;
		align-items: center;
		padding: 0 32px;
		box-sizing: border-box;
		justify-content: space-between;
		height: 100px;
		background-color: #ffffff;
		.left,
		.right {
			display: flex;
			align-items: center;
		}
		.btn {
			background: var(--van-action-bar-button-danger-color);
			border-radius: 20px;
			height: 60px;
			width: 120px;
			font-size: 26px;
			color: #fff;
			font-weight: 500;
			text-align: center;
			line-height: 60px;
		}
		span {
			margin-right: 20px;
		}
	}
}
</style>
