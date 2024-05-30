<template>
	<div class="page address-list">
		<NavBar title="我的地址" />

		<Empty v-if="state.list.length == 0" />
		<div class="content">
			<div class="item" v-for="item in state.list" :key="item.id" @click="onAddressClick(item)">
				<div class="name-phone">
					<span>{{ item.name }}</span>
					{{ item.phone }}
					<van-tag type="primary" v-if="item.isDefault">默认</van-tag>
				</div>
				<div class="address">{{ item.address }}</div>
				<div class="action">
					<div class="btn del-btn" @click.stop="onDelClick(item)">删除</div>
					<div class="btn edit-btn" @click.stop="onEditClick(item)">编辑</div>
					<div class="btn edit-btn" @click.stop="onSetDefaultAddressClick(item)" v-if="!item.isDefault">
						设置成默认地址
					</div>
				</div>
			</div>
		</div>

		<div class="add">
			<div class="btn" @click="onAddClick">添加地址</div>
		</div>
	</div>
</template>

<script setup>
import { showConfirmDialog } from 'vant'
import useLoading from '@/hooks/useLoading'
import { list, del, setDefault } from '@/api/address.api.js'

const router = useRouter()
const { loading, closeLoading } = useLoading()

const state = reactive({
	list: []
})

onMounted(() => {
	queryList()
})

const queryList = async () => {
	try {
		loading('加载中...')
		const { data } = await list()
		data.map((item) => {
			item.tel = item.phone
			item.address = item.pcdStr + item.address
			item.isDefault = item.isDefault == 1 ? true : false
		})
		state.list = data
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

const onAddressClick = (item) => {
	localStorage.setItem('addressId', item.id)
	router.go(-1)
}

const onDelClick = async (item) => {
	try {
		await showConfirmDialog({
			title: '警告',
			message: '确定要删除该地址吗?'
		})
		loading('删除中...')
		await del(item.id)
		queryList()
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

const onSetDefaultAddressClick = async (item) => {
	try {
		loading('设置中...')
		await setDefault(item.id)
		queryList()
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

const onAddClick = () => {
	router.push({
		path: '/address/add'
	})
}

const onEditClick = (item) => {
	router.push({
		path: '/address/edit',
		query: {
			id: item.id
		}
	})
}
</script>

<style lang="scss" scoped>
.address-list {
	.content {
		padding-bottom: 160px;
		.item {
			width: 686px;
			margin: 0 auto;
			padding: 30px;
			box-sizing: border-box;
			background-color: #ffffff;
			border-radius: 8px;
			margin-top: 20px;
			.name-phone {
				color: #999999;
				font-size: 26px;
				span {
					margin-right: 20px;
				}
			}
			.address {
				font-size: 30px;
				font-weight: 500;
				margin-top: 20px;
			}
			.action {
				display: flex;
				align-items: center;
				justify-content: flex-end;
				margin-top: 20px;
				.btn {
					margin-left: 30px;
				}
				.del-btn {
					color: #f00;
				}
				.edit-btn {
					color: #56a2fe;
				}
			}
		}
	}
	.add {
		position: fixed;
		bottom: 0;
		left: 0;
		padding: 20px 0;
		right: 0;
		background-color: #ffffff;
		.btn {
			width: 686px;
			margin: 0 auto;
			height: 100px;
			display: flex;
			align-items: center;
			justify-content: center;
			background: linear-gradient(116deg, #56a2fe 0%, #2972ff 100%);
			color: #ffffff;
			border-radius: 16px;
		}
	}
}
</style>
