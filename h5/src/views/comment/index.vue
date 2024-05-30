<template>
	<div class="page comment">
		<NavBar title="评论" />

		<div class="content">
			<van-cell-group>
				<van-field
					v-model="state.form.content"
					rows="6"
					autosize
					type="textarea"
					maxlength="2000"
					placeholder="请输入评论"
					show-word-limit
				/>
			</van-cell-group>
			<van-button type="primary" block @click="onConfirmClick">提交</van-button>
		</div>
	</div>
</template>

<script setup>
import { add } from '@/api/comment.api.js'
import useLoading from '@/hooks/useLoading'
import useToast from '@/hooks/useToast'

import { useRoute } from 'vue-router'
const { toast } = useToast()
const { loading, closeLoading } = useLoading()

const route = useRoute()
const router = useRouter()

const state = reactive({
	form: {
		orderId: route.query.orderId,
		productId: route.query.productId,
		answerId: route.query.answerId,
		pid: route.query.pid,
		content: ''
	}
})

const onConfirmClick = async () => {
	try {
		if (!state.form.content) {
			return toast('请输入评论内容')
		}
		loading('加载中...')
		await add(state.form)
		toast('评论成功')
		router.go(-1)
	} catch (error) {
		console.log(error)
	} finally {
		closeLoading()
	}
}
</script>

<style lang="scss" scoped>
.comment {
	.content {
		width: 686px;
		margin: 0 auto;
		margin-top: 20px;
		.van-button {
			margin-top: 60px;
		}
	}
}
</style>
