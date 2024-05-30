<template>
	<div>
		<div class="search">
			<van-field v-model="state.form.name" clearable placeholder="请输入要搜索的关键字">
				<template #button>
					<van-button size="small" @click="onSearch" type="primary">搜索</van-button>
				</template>
			</van-field>
		</div>
		<van-dropdown-menu>
			<van-dropdown-item title="品牌" @close="onSearch">
				<van-checkbox-group v-model="state.form.brandIdList">
					<van-cell-group>
						<van-cell
							v-for="(item, index) in brandList"
							clickable
							:key="item.value"
							:title="item.text"
							@click="onToggleBrand(index)"
						>
							<template #right-icon>
								<van-checkbox :name="item.value" :ref="(el) => (brandRef[index] = el)" @click.stop />
							</template>
						</van-cell>
					</van-cell-group>
				</van-checkbox-group>
			</van-dropdown-item>
			<van-dropdown-item title="类型" @close="onSearch">
				<van-checkbox-group v-model="state.form.typeIdList">
					<van-cell-group>
						<van-cell
							v-for="(item, index) in typeList"
							clickable
							:key="item.value"
							:title="item.text"
							@click="onToggleType(index)"
						>
							<template #right-icon>
								<van-checkbox :name="item.value" :ref="(el) => (typeRef[index] = el)" @click.stop />
							</template>
						</van-cell>
					</van-cell-group>
				</van-checkbox-group>
			</van-dropdown-item>
			<van-dropdown-item title="颜色" @close="onSearch">
				<van-checkbox-group v-model="state.form.colorIdList">
					<van-cell-group>
						<van-cell
							v-for="(item, index) in colorList"
							clickable
							:key="item.value"
							:title="item.text"
							@click="onToggleColor(index)"
						>
							<template #right-icon>
								<van-checkbox :name="item.value" :ref="(el) => (colorRef[index] = el)" @click.stop />
							</template>
						</van-cell>
					</van-cell-group>
				</van-checkbox-group>
			</van-dropdown-item>
			<van-dropdown-item title="尺寸" @close="onSearch">
				<van-checkbox-group v-model="state.form.sizeIdList">
					<van-cell-group>
						<van-cell
							v-for="(item, index) in sizeList"
							clickable
							:key="item.value"
							:title="item.text"
							@click="onToggleSize(index)"
						>
							<template #right-icon>
								<van-checkbox :name="item.value" :ref="(el) => (sizeRef[index] = el)" @click.stop />
							</template>
						</van-cell>
					</van-cell-group>
				</van-checkbox-group>
			</van-dropdown-item>
		</van-dropdown-menu>

		<Empty v-if="state.list.length == 0 && !isLoading" />
		<div class="product-box">
			<div class="item" v-for="item in state.list" :key="item.id" @click="onDetailsClick(item)">
				<img :src="item.cover" class="pic" />
				<div class="title">{{ item.title }}</div>
				<div class="price">
					<div>
						¥<span>{{ item.nowPrice }}</span>
					</div>
					<span class="old-price" v-if="item.nowPrice != item.usedPrice">¥{{ item.usedPrice }}</span>
				</div>
			</div>
		</div>

		<Footer v-if="!isLoading"></Footer>
	</div>
</template>

<script setup>
import Footer from './components/Footer.vue'
import useLoading from '@/hooks/useLoading'
import { list } from '@/api/product.api.js'
import { list as dictList } from '@/api/dict.api.js'

const router = useRouter()
const { loading, closeLoading, isLoading } = useLoading()

const brandRef = ref([])
const typeRef = ref([])
const colorRef = ref([])
const sizeRef = ref([])
const brandList = ref([])
const typeList = ref([])
const sizeList = ref([])
const colorList = ref([])

const state = reactive({
	list: [],
	current: 1,
	page: 99999,
	form: {
		name: '',
		brandIdList: [],
		typeIdList: [],
		colorIdList: [],
		sizeIdList: []
	}
})

onMounted(() => {
	queryDictList()
	queryProductList()
})

const queryDictList = async () => {
	try {
		// 1-鞋码 2-颜色 3-品牌 4-鞋类型
		const { data: size } = await dictList({ type: 1, current: 1, page: 10000 })
		const { data: color } = await dictList({ type: 2, current: 1, page: 10000 })
		const { data: brand } = await dictList({ type: 3, current: 1, page: 10000 })
		const { data: type } = await dictList({ type: 4, current: 1, page: 10000 })
		brand.map((item) => {
			brandList.value.push({
				text: item.label,
				value: item.id
			})
		})
		size.map((item) => {
			sizeList.value.push({
				text: item.label,
				value: item.id
			})
		})
		color.map((item) => {
			colorList.value.push({
				text: item.label,
				value: item.id
			})
		})
		type.map((item) => {
			typeList.value.push({
				text: item.label,
				value: item.id
			})
		})
		console.log(size)
	} catch (error) {
		console.log(error)
	}
}

const queryProductList = async () => {
	try {
		loading('加载中...')
		const { data } = await list(state.current, state.page, state.form)
		state.list = [...state.list, ...data]
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

const onSearch = async () => {
	state.current = 1
	state.list = []
	await queryProductList()
}

const onToggleBrand = (index) => {
	brandRef.value[index].toggle()
}
const onToggleType = (index) => {
	typeRef.value[index].toggle()
}
const onToggleColor = (index) => {
	colorRef.value[index].toggle()
}
const onToggleSize = (index) => {
	sizeRef.value[index].toggle()
}

const onDetailsClick = (item) => {
	router.push({
		path: `/product/${item.id}`
	})
}
</script>

<style>
.van-dropdown-menu__bar {
	box-shadow: none !important;
	border-top: 1px solid #efefef;
}
</style>
<style lang="scss" scoped>
.search {
	width: 100%;
	height: 100px;
	background-color: #ffffff;
}

.product-box {
	width: 686px;
	margin: 0 auto;
	margin-top: 30px;
	column-count: 2;
	column-gap: 30px;

	.item {
		border-radius: 12px;
		overflow: hidden;
		background-color: #ffffff;
		padding-bottom: 20px;
		break-inside: avoid;
		margin-bottom: 30px;

		.pic {
			width: 100%;
			height: auto;
		}

		.title {
			font-size: 26px;
			line-height: 38px;
			padding: 0 10px;
			box-sizing: border-box;
			margin-top: 12px;
			color: #333333;
		}

		.price {
			padding: 0 10px;
			box-sizing: border-box;
			color: rgb(255, 98, 0);
			margin-top: 20px;
			font-weight: bold;
			font-size: 26px;
			display: flex;
			align-items: center;

			span {
				margin-left: 6px;
				font-size: 32px;
			}

			.old-price {
				font-weight: 400;
				color: #999;
				font-size: 26px;
				text-decoration-line: line-through;
				margin-left: 10px;
			}
		}
	}
}
</style>
