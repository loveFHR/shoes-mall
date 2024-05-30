<template>
	<DialogPopup v-model="state.dialog" title="订单详情" :width="840" @cancel="close" :footer="false">
		<el-form>
			<el-row :gutter="10">
				<el-col :span="24">
					<div class="title">用户信息</div>
				</el-col>
				<el-col :span="8">
					<el-form-item label="用户昵称:">{{ state.info.username }}</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="用户电话:">{{ state.info.phone }}</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="用户邮箱:">{{ state.info.email }}</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="用户姓名:">{{ state.info.name }}</el-form-item>
				</el-col>
				<el-col :span="16">
					<el-form-item label="用户地址:">{{ state.info.address }}</el-form-item>
				</el-col>
				<el-col :span="24">
					<div class="title">订单信息</div>
				</el-col>
				<el-col :span="8">
					<el-form-item label="订单编号:">{{ state.info.orderNo }}</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="下单时间:">{{ state.info.createTime }}</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="支付金额:">{{ state.info.totalPrice }}元</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="数量:">x{{ state.info.num }}</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="订单状态:">{{ orderStatusDict[state.info.orderStatus] }}</el-form-item>
				</el-col>
				<el-col :span="24">
					<div class="title">商品信息</div>
				</el-col>
				<el-col :span="24">
					<el-form-item label="商品图片">
						<el-image
							:src="state.info.cover"
							:preview-src-list="[state.info.cover]"
							preview-teleported
							style="width: 80px"
						/>
					</el-form-item>
				</el-col>
				<el-col :span="24">
					<el-form-item label="商品名称">{{ state.info.title }}</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="商品尺码">{{ state.info.sizeLabel }}</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="商品颜色">{{ state.info.colorLabel }}</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="商品价格">{{ state.info.nowPrice }}元</el-form-item>
				</el-col>
			</el-row>
		</el-form>
	</DialogPopup>
</template>

<script setup>
import { getInfo } from '@/api/order.api.js'
const orderStatusDict = {
	'-1': '手动关闭',
	'-2': '申请退款',
	'-3': '退货订单',
	0: '待支付',
	2: '待发货',
	3: '待收货',
	4: '待评价 ',
	5: '交易成功'
}

const state = reactive({
	dialog: false,
	info: {}
})

const open = async (val) => {
	state.dialog = true
	await nextTick()

	try {
		const { data } = await getInfo(val.id)
		console.log(data)
		state.info = data
	} catch (error) {
		console.log(error)
	}
}

const close = () => {
	state.dialog = false
}

defineExpose({
	open
})
</script>

<style lang="scss" scoped>
.title {
	font-weight: 500;
	font-size: 14px;
	color: #000;
	margin-bottom: 12px;
}
</style>
