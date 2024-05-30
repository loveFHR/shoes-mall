<template>
	<div class="page address-add">
		<NavBar title="新增地址" />

		<van-address-edit
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
import { add } from '@/api/address.api.js'

const router = useRouter()
const { loading, closeLoading } = useLoading()

const onSave = async (value) => {
	try {
		const obj = {
			phone: value.tel,
			address: value.addressDetail,
			name: value.name,
			province: value.province,
			city: value.city,
			district: value.county,
			isDefault: value.isDefault ? 1 : 0
		}
		loading('保存中...')
		await add(obj)
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
