<template>
	<div class="comment">
		<div class="comment-item" v-for="item in list" :key="item.id">
			<div class="user">
				<img :src="item.userAvatar" alt="" />
				<span>{{ item.username }}</span>
			</div>
			<div>{{ item.content }}</div>

			<div class="more" v-for="v in item.childCommentList" :key="v.id">
				<div class="more-item admin" v-if="v.isAdmin == 1">商家回复: {{ v.content }}</div>
			</div>
		</div>
	</div>
</template>

<script setup>
defineProps({
	list: {
		type: Array,
		default: () => {
			return []
		}
	}
})
</script>

<style lang="scss" scoped>
.comment-item {
	margin-bottom: 20px;
	.user {
		display: flex;
		align-items: center;
		margin-bottom: 10px;
		img {
			width: 60px;
			height: 60px;
			border-radius: 100%;
			object-fit: cover;
			margin-right: 10px;
		}
		span {
			font-size: 28px;
			color: #1f1f1f;
		}
	}
	.more {
		height: 100px;
		margin-top: 20px;
		margin-left: 20px;
		box-sizing: border-box;
		font-size: 28px;
		.more-item {
			padding: 20px;
			box-sizing: border-box;
			margin-bottom: 20px;
		}
		.admin {
			background-color: #efefef;
			border-radius: 8px;
		}
	}
}
</style>
