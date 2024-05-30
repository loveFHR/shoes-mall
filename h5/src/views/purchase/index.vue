<template>
	<div class="page purchase">
		<NavBar title="创建订单" />

		<div class="content">
			<div class="item address-item" @click="onAddressClick">
				<svg-icon name="icon-address" :size="42" />
				<div class="info">
					<div>
						<template v-if="Object.keys(address).length > 0">
							<div class="address">{{ address.pcdStr }}{{ address.address }}</div>
							<div class="name-phone">{{ address.name }} {{ address.phone }}</div>
						</template>
						<template v-else>请选择收货地址</template>
					</div>
					<div class="icon-arrow"><van-icon name="arrow" /></div>
				</div>
			</div>
			<div class="item product">
				<div class="product-item" v-for="item in list" :key="item.id">
					<img :src="item.cover" class="pic" />
					<div class="info">
						<div class="title">{{ item.title }}</div>
						<div class="tag">
							<van-tag plain type="primary">{{ item.color }}</van-tag>
							<van-tag plain type="primary">{{ item.size }}</van-tag>
						</div>
						<div class="price">
							¥{{ item.nowPrice }} <span>x{{ item.num }}</span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<van-submit-bar :price="allPrice" button-text="提交订单" @submit="onCreateOrderClick" />
	</div>
</template>

<script setup>
import { getDefault, info } from '@/api/address.api.js'
import { add } from '@/api/order.api.js'
import { alipay } from '@/api/pay.api.js'
import useLoading from '@/hooks/useLoading'
import useToast from '@/hooks/useToast'

const router = useRouter()
const { toast } = useToast()
const { loading, closeLoading } = useLoading()

const list = ref([])
const address = ref({})
const allPrice = ref(0)

onMounted(() => {
	getDefaultAddress()

	list.value = JSON.parse(localStorage.getItem('ProductInfo'))
	list.value.map((item) => {
		allPrice.value += item.num * item.nowPrice
	})
	allPrice.value *= 100
})

const getAddressInfo = async (id) => {
	try {
		const { data } = await info(id)
		address.value = data
	} catch (error) {
		console.log(error)
	}
}

const onCreateOrderClick = async () => {
	if (Object.keys(address.value) == 0) return toast('请选择收货地址')
	const arr = []
	list.value.map((item) => {
		arr.push({
			productId: item.productId,
			systemBaseSettingColorId: item.colorId,
			systemBaseSettingSizeId: item.sizeId,
			price: item.nowPrice * item.num,
			addressId: address.value.id,
			shoppingCartId: item.cartId || null
		})
	})
	try {
		loading('下单中...')
		const { data } = await add(arr)
		const res = await alipay({ out_trade_no: data })
		const div = document.createElement('div') // 创建div
		div.innerHTML = res.data // 将返回的form 放入div
		document.body.appendChild(div)
		document.forms[0].submit()
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

const getDefaultAddress = async () => {
	try {
		const { data } = await getDefault()
		address.value = data || {}
		const addressId = localStorage.getItem('addressId')
		if (!data && addressId) {
			getAddressInfo(addressId)
		}
	} catch (error) {
		console.log(error)
	}
}

const onAddressClick = () => {
	router.push({
		path: '/address/list'
	})
}
</script>

<style lang="scss" scoped>
.purchase {
	.content {
		padding-bottom: 120px;
	}
	.item {
		width: 686px;
		margin: 0 auto;
		border-radius: 8px;
		background-color: #ffffff;
		padding: 30px;
		box-sizing: border-box;
		margin-top: 20px;
	}
	.address-item {
		display: flex;
		align-items: flex-start;
		.info {
			flex: 1;
			display: flex;
			align-items: center;
			margin-left: 14px;
			justify-content: space-between;
			.address {
				flex: 1;
				font-size: 26px;
				font-weight: 500;
			}
			.icon-arrow {
				margin-left: 14px;
			}
			.name-phone {
				font-size: 26px;
				color: #999999;
				margin-top: 8px;
			}
		}
	}
	.product {
		.product-item {
			display: flex;
			padding-bottom: 20px;
			border-bottom: 1px dashed #efefef;
			margin-bottom: 30px;
			&:last-child {
				padding: 0;
				margin: 0;
				border: 0;
			}
			.pic {
				width: 130px;
				height: 130px;
				border-radius: 8px;
				margin-right: 20px;
			}
			.info {
				flex: 1;
				min-width: 0;
				.title {
					flex: 1;
					white-space: nowrap;
					overflow: hidden;
					text-overflow: ellipsis;
					font-size: 28px;
					color: #1f1f1f;
				}
				.tag {
					margin-top: 6px;
				}
				.van-tag {
					margin-right: 10px;
					padding: 0 12px;
				}
				.price {
					margin-top: 14px;
					font-size: 26px;
					color: rgb(255, 98, 0);
					span {
						margin-left: 14px;
						color: #999999;
					}
				}
			}
		}
		.price {
			text-align: right;
			font-size: 28px;
			font-weight: 500;
			span {
				color: rgb(255, 98, 0);
			}
		}
	}
}
</style>
