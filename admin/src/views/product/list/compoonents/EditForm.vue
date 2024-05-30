<template>
	<el-drawer
		v-model="state.drawer"
		:size="1000"
		title="新增商品"
		append-to-body
		:close-on-click-modal="false"
		:close-on-press-escape="false"
		@close="onCancelClick"
	>
		<el-form :model="state.form" ref="formRef" :rules label-width="90">
			<el-row :gutter="10">
				<el-col :span="8">
					<el-form-item label="商品名称:" prop="title">
						<el-input v-model="state.form.title" placeholder="请输入商品名称" clearable />
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="商品尺寸:" prop="systemBaseSettingSizeList">
						<el-select
							v-model="state.form.systemBaseSettingSizeList"
							multiple
							collapse-tags
							collapse-tags-tooltip
							placeholder="请选择商品尺寸"
							@change="onSearchClick"
						>
							<el-option v-for="item in sizeList" :key="item.id" :label="item.label" :value="item.id" />
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="商品颜色:" prop="systemBaseSettingColorList">
						<el-select
							v-model="state.form.systemBaseSettingColorList"
							multiple
							collapse-tags
							collapse-tags-tooltip
							placeholder="请选择商品颜色"
							@change="onSearchClick"
						>
							<el-option v-for="item in colorList" :key="item.id" :label="item.label" :value="item.id" />
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="商品类型:" prop="systemBaseSettingTypeId">
						<el-select
							v-model="state.form.systemBaseSettingTypeId"
							placeholder="请选择商品类型"
							@change="onSearchClick"
						>
							<el-option v-for="item in typeList" :key="item.id" :label="item.label" :value="item.id" />
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="商品品牌:" prop="systemBaseSettingBrandId">
						<el-select
							v-model="state.form.systemBaseSettingBrandId"
							placeholder="请选择商品品牌"
							@change="onSearchClick"
						>
							<el-option v-for="item in brandList" :key="item.id" :label="item.label" :value="item.id" />
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="商品库存:" prop="totalNum">
						<el-input v-model="state.form.totalNum" type="number" placeholder="请输入商品库存" :min="1" clearable />
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="商品原价:" prop="usedPrice">
						<el-input v-model="state.form.usedPrice" type="number" placeholder="请输入商品原价" :min="1" clearable />
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="商品现价:" prop="nowPrice">
						<el-input v-model="state.form.nowPrice" type="number" placeholder="请输入商品现价" :min="1" clearable />
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="上下架:" prop="isOn">
						<el-radio-group v-model="state.form.isOn">
							<el-radio label="上架" :value="1" />
							<el-radio label="下架" :value="0" />
						</el-radio-group>
					</el-form-item>
				</el-col>
				<el-col :span="24">
					<el-form-item label="商品图片:" prop="cover">
						<el-upload
							v-loading="coverLoading"
							class="cover-uploader"
							accept=".png,.jpg,.jpeg.avif"
							:show-file-list="false"
							:http-request="onUploadCoverClick"
						>
							<img v-if="state.form.cover" :src="state.form.cover" />
							<el-icon v-else><Plus /></el-icon>
						</el-upload>
					</el-form-item>
				</el-col>
				<el-col :span="24">
					<el-form-item label="商品主图:" prop="mainPicture">
						<div class="imgs">
							<template v-if="state.form.mainPicture">
								<div class="img-box" v-for="(item, index) in state.form.mainPicture.split(',')" :key="index">
									<el-image :src="item" fit="scale-down" />
									<el-button link @click="onDelImg(index)">
										<el-icon :size="20"><CircleClose /></el-icon>
									</el-button>
								</div>
							</template>
							<el-upload
								v-loading="mainImgLoading"
								class="cover-uploader"
								accept=".png,.jpg,.jpeg.avif"
								:show-file-list="false"
								:http-request="onUploadMainImgClick"
							>
								<el-icon><Plus /></el-icon>
							</el-upload>
						</div>
					</el-form-item>
				</el-col>
				<el-col :span="24">
					<el-form-item label="商品描述:" prop="desc">
						<Markdown height="800px" v-model="state.form.desc" />
					</el-form-item>
				</el-col>
			</el-row>
		</el-form>
		<template #footer>
			<el-button @click="onCancelClick">取消</el-button>
			<el-button type="primary" :loading="state.btnLoading" @click="onConfirmClck">确定</el-button>
		</template>
	</el-drawer>
</template>

<script setup>
import { Plus, CircleClose } from '@element-plus/icons-vue'
import Markdown from '@/components/Markdown/index.vue'
import useMessage from '@/hooks/useMessage'
import { upload } from '@/api/upload.api.js'
import { info, edit } from '@/api/product.api.js'
import { nextTick } from 'vue'

defineProps({
	brandList: {
		type: Array,
		default: () => {
			return []
		}
	},
	typeList: {
		type: Array,
		default: () => {
			return []
		}
	},
	sizeList: {
		type: Array,
		default: () => {
			return []
		}
	},
	colorList: {
		type: Array,
		default: () => {
			return []
		}
	}
})

const { success } = useMessage()

const formRef = ref(null)
const coverLoading = ref(false)
const mainImgLoading = ref(false)
const state = reactive({
	drawer: false,
	btnLoading: false,
	form: {
		isOn: 0,
		systemBaseSettingColorList: [],
		systemBaseSettingSizeList: [],
		cover: '',
		mainPicture: ''
	}
})
const rules = ref({
	title: [{ required: true, message: '请输入商品名称' }],
	desc: [{ required: true, message: '请输入商品描述' }],
	totalNum: [{ required: true, message: '请输入商品库存' }],
	usedPrice: [{ required: true, message: '请输入商品原价' }],
	nowPrice: [{ required: true, message: '请输入商品现价' }],
	cover: [{ required: true, message: '请上传商品图片' }],
	mainPicture: [{ required: true, message: '请上传商品主图' }],
	isOn: [{ required: true, message: '请选上下架状态' }],
	systemBaseSettingBrandId: [{ required: true, message: '请输入商品品牌' }],
	systemBaseSettingTypeId: [{ required: true, message: '请输入商品类型' }],
	systemBaseSettingColorList: [{ required: true, message: '请输入商品颜色' }],
	systemBaseSettingSizeList: [{ required: true, message: '请输入商品尺寸' }]
})

const emit = defineEmits(['success'])

const onConfirmClck = async () => {
	try {
		await formRef.value.validate()
		state.btnLoading = true
		await edit(state.form)
		success('商品修改成功')
		onCancelClick()
		emit('success')
	} catch (error) {
		console.log(error)
	} finally {
		state.btnLoading = false
	}
}

// 上传封面图
const onUploadCoverClick = async ({ file }) => {
	try {
		state.form.cover = ''
		coverLoading.value = true
		const formData = new FormData()
		formData.append('file', file)
		const { data } = await upload(formData)
		state.form.cover = data
	} catch (error) {
		console.log(error)
	} finally {
		coverLoading.value = false
	}
}

// 上传主图
const onUploadMainImgClick = async ({ file }) => {
	try {
		mainImgLoading.value = true
		const formData = new FormData()
		formData.append('file', file)
		const { data } = await upload(formData)
		let arr = []
		if (state.form.mainPicture) {
			arr = state.form.mainPicture.split(',')
		}
		arr.push(data)
		state.form.mainPicture = arr.toString()
		console.log(state.form.mainPicture)
	} catch (error) {
		console.log(error)
	} finally {
		mainImgLoading.value = false
	}
}

const onDelImg = (index) => {
	let arr = state.form.mainPicture.split(',')
	arr.splice(index, 1)
	state.form.mainPicture = arr.toString()
}

const onCancelClick = () => {
	formRef.value.resetFields()
	state.drawer = false
}

const open = async (id) => {
	state.drawer = true
	await nextTick()
	try {
		const { data } = await info(id)
		console.log(data)
		state.form = data
	} catch (error) {
		console.log(error)
	}
}

defineExpose({
	open
})
</script>

<style lang="scss" scoped>
.cover-uploader {
	width: 160px;
	height: 160px;
	border: 1px dashed var(--el-border-color);
	border-radius: 6px;
	cursor: pointer;
	position: relative;
	overflow: hidden;
	transition: var(--el-transition-duration-fast);
	img {
		width: 160px;
		height: 160px;
		object-fit: scale-down;
	}
	.el-icon {
		width: 160px;
		height: 160px;
		color: #ccc;
		font-weight: 400;
		font-size: 32px;
	}
}
.imgs {
	width: 100%;
	display: grid;
	grid-gap: 10px;
	grid-template-columns: repeat(4, 160px);
	.img-box {
		width: 100%;
		height: 160px;
		position: relative;
		.el-image {
			width: 100%;
			height: 100%;
			border-radius: 6px;
			border: 1px dashed var(--el-border-color);
		}
		.el-icon {
			position: absolute;
			top: 0;
			right: 0;
			cursor: pointer;
			z-index: 99;
		}
	}
}
</style>
