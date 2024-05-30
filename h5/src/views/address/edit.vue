<template>
	<div class="page address-add">
		<NavBar title="修改地址" />

		<van-address-edit
			:address-info="info"
			ref="addressRef"
			v-model="abc"
			:area-list="areaList"
			show-set-default
			show-search-result
			:search-result="searchResult"
			:area-columns-placeholder="['请选择', '请选择', '请选择']"
			@save="onSave"
			@change-detail="onChangeDetail"
		/>
	</div>
</template>

<script setup>
import { areaList } from '@vant/area-data'
import useLoading from '@/hooks/useLoading'
import { edit, info as getInfo } from '@/api/address.api.js'

const route = useRoute()
const router = useRouter()
const { loading, closeLoading } = useLoading()

const info = ref(null)
onMounted(() => {
	queryDetails()
})

const queryDetails = async () => {
	try {
		loading('加载中...')
		const { data } = await getInfo(route.query.id)
		console.log(data)
		data.tel = data.phone
		data.county = data.district
		data.isDefault = data.isDefault == 1 ? true : false
		data.addressDetail = data.address
		info.value = data
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}

const onSave = async (value) => {
	try {
		const obj = {
			id: info.value.id,
			phone: value.tel,
			address: value.addressDetail,
			name: value.name,
			province: value.province,
			city: value.city,
			district: value.county,
			isDefault: value.isDefault ? 1 : 0
		}
		loading('保存中...')
		await edit(obj)
		router.go(-1)
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}
</script>

<style lang="scss" scoped>
:deep(.van-button) {
	border-radius: 14px !important;
	height: 100px;
	background: linear-gradient(116deg, #56a2fe 0%, #2972ff 100%);
}
</style>
