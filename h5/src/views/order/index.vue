<template>
	<div class="page order">
		<NavBar title="我的订单" />
		<div class="status">
			<div
				:class="['item', state.type == item.value ? 'active' : '']"
				v-for="item in statusList"
				:key="item.value"
				@click="onStatusClick(item)"
			>
				{{ item.label }}
			</div>
		</div>
		<div class="content">
			<Empty v-if="state.list.length == 0 && !isLoading" />
			<div
				class="order-item"
				v-for="item in state.list"
				:key="item.id"
				@click="router.push({ path: '/product/' + item.productId })"
			>
				<div class="time-status">
					<div>{{ item.createTime }}</div>
					<div v-if="item.orderStatus == 0" style="color: #f00">待付款</div>
					<div v-if="item.orderStatus == 2">待发货</div>
					<div v-if="item.orderStatus == 3">待收货</div>
					<div v-if="item.orderStatus == 4">待评价</div>
					<div v-if="item.orderStatus == 5">已完成</div>
					<div v-if="item.orderStatus == -3">退款订单</div>
					<div v-if="item.orderStatus == -2">退款中</div>
					<div v-if="item.orderStatus == -1">取消订单</div>
				</div>
				<div class="order-number">订单号: {{ item.orderNo }}</div>
				<div class="order-info">
					<img :src="item.cover" class="pic" />
					<div class="info">
						<div class="title">{{ item.productName }}</div>
						<div class="tag">
							<van-tag plain type="primary">{{ item.systemBaseSettingColorLabel }}</van-tag>
							<van-tag plain type="primary">{{ item.systemBaseSettingSizeLabel }}</van-tag>
						</div>
						<div class="price">
							¥{{ item.price }} <span>x{{ item.num }}</span>
						</div>
					</div>
				</div>
				<div class="action">
					<div class="btn" v-if="item.orderStatus == 4 || item.isComment == 0" @click.stop="onCommentClick(item)">
						点击评论
					</div>

					<div class="btn" v-if="item.isComment == 1" @click.stop="onCommentClick(item)">追加评论</div>

					<div class="btn" v-if="item.orderStatus == 0 || item.orderStatus == 2" @click.stop="onCloseOrderClick(item)">
						取消订单
					</div>

					<div class="btn" v-if="item.orderStatus == 0" @click.stop="onPayClick(item)">点击支付</div>

					<div class="btn" v-if="item.orderStatus == 3" @click.stop="onConfirmClick(item)">确认收货</div>

					<div class="btn" v-if="item.orderStatus == 3" @click.stop="onRefundClick(item)">申请退款</div>

					<div class="btn" @click.stop="onDelClick(item)" v-if="item.orderStatus == 4 || item.orderStatus == 5">
						删除订单
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup>
import { showConfirmDialog } from 'vant'
import useLoading from '@/hooks/useLoading'
import useToast from '@/hooks/useToast'
import { list, cancel, del, receive, applyRefund } from '@/api/order.api.js'
import { alipay } from '@/api/pay.api.js'

const route = useRoute()
const router = useRouter()
const { toast } = useToast()
const { loading, closeLoading, isLoading } = useLoading()

const statusList = [
	{ label: '全部', value: 13 },
	{ label: '待付款', value: 0 },
	{ label: '待发货', value: 2 },
	{ label: '待收货', value: 3 },
	{ label: '已完成', value: 5 },
	{ label: '待评价', value: 4 }
]
const state = reactive({
	list: [],
	type: 6,
	current: 1,
	page: 99999
})

onMounted(() => {
	state.type = route.query.type || 13
	queryList()
})

const queryList = async () => {
	try {
		loading('加载中...')
		const { data } = await list(state.current, state.page, {
			type: state.type,
			findMyOrder: 1
		})
		state.list = data
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

// 支付
const onPayClick = async (item) => {
	try {
		loading('加载中...')
		const { data } = await alipay({ out_trade_no: item.orderNo })
		const div = document.createElement('div') // 创建div
		div.innerHTML = data // 将返回的form 放入div
		document.body.appendChild(div)
		document.forms[0].submit()
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

// 取消订单
const onCloseOrderClick = async (item) => {
	try {
		await showConfirmDialog({
			title: '警告',
			message: '确定要取消该订单吗?'
		})
		loading('加载中...')
		await cancel({ ids: item.id })
		await toast('取消订单成功')
		await onSearch()
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

// 删除订单
const onDelClick = async (item) => {
	try {
		await showConfirmDialog({
			title: '警告',
			message: '确定要删除该订单吗?'
		})
		loading('加载中...')
		await del({ orderIdList: [item.id] })
		state.list = []
		state.current = 1
		await queryList()
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

// 确认收货
const onConfirmClick = async (item) => {
	try {
		await showConfirmDialog({
			title: '警告',
			message: '确定收货吗?'
		})
		loading('加载中...')
		await receive({ ids: item.id })
		toast('收货成功')
		onSearch()
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

// 申请退款
const onRefundClick = async (item) => {
	try {
		await showConfirmDialog({
			title: '警告',
			message: '确定要申请退款吗?'
		})
		loading('加载中...')
		await applyRefund({ ids: item.id })
		toast('申请成功')
		onSearch()
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

const onStatusClick = async (item) => {
	if (state.type == item.value) return
	state.type = item.value
	state.list = []
	state.current = 1
	await queryList()
}

// 评论
const onCommentClick = (item) => {
	console.log({ ...item })
	router.push({
		path: '/comment',
		query: {
			orderId: item.id, // 订单id
			productId: item.productId, // 商品id
			answerId: item.commentId, // 回复的id
			pid: item.commentId
		}
	})
}

const onSearch = async () => {
	state.list = []
	state.current = 1
	await queryList()
}
</script>

<style lang="scss" scoped>
.order {
	.status {
		width: 100%;
		height: 100px;
		background-color: #ffffff;
		display: flex;
		align-items: center;
		position: sticky;
		top: 0;
		z-index: 99;
		.item {
			flex: 1;
			text-align: center;
			font-size: 28px;
		}
		.active {
			color: #f00;
			font-weight: 500;
		}
	}
	.content {
		.order-item {
			width: 686px;
			background-color: #ffffff;
			padding: 30px;
			box-sizing: border-box;
			margin: 0 auto;
			margin-top: 20px;
			border-radius: 12px;
			.time-status {
				display: flex;
				align-items: center;
				justify-content: space-between;
				font-size: 28px;
			}
			.order-number {
				margin: 20px 0;
				font-size: 26px;
			}
			.order-info {
				display: flex;
				margin-top: 18px;
				.pic {
					width: 140px;
					height: 140px;
					margin-right: 14px;
					border-radius: 6px;
				}
				.info {
					flex: 1;
					min-width: 0;
					.title {
						flex: 1;
						font-size: 26px;
						white-space: nowrap;
						overflow: hidden;
						text-overflow: ellipsis;
						color: #1f1f1f;
					}
					.tag {
						margin-top: 12px;
						.van-tag {
							margin-right: 12px;
						}
					}
					.price {
						margin-top: 14px;
						font-size: 26px;
						span {
							color: #999999;
						}
					}
				}
			}
			.action {
				margin-top: 28px;
				display: flex;
				align-items: center;
				justify-content: flex-end;
				border-top: 1px dashed #efefef;
				padding-top: 20px;
				font-size: 26px;
				.btn {
					margin-left: 20px;
					border: 1px solid #efefef;
					padding: 12px 24px;
					border-radius: 8px;
				}
			}
		}
	}
}
</style>
