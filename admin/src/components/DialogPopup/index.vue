<template>
	<el-dialog
		v-bind="$attr"
		v-model="visible"
		:title
		:width
		:close-on-click-modal="false"
		:close-on-press-escape="false"
		class="dialog-popup"
		:before-close="onCancelClick"
		destroy-on-close
	>
		<slot name="default"> </slot>
		<template #footer v-if="footer">
			<div class="dialog-footer">
				<el-button @click="onCancelClick">{{ cancelText }}</el-button>
				<el-button type="primary" :loading="btnLoading" @click="onConfirmClick"> {{ confirmText }} </el-button>
			</div>
		</template>
	</el-dialog>
</template>

<script setup>
import { defineModel } from 'vue'

defineProps({
	title: {
		type: String,
		default: () => {
			return ''
		}
	},
	width: {
		type: String,
		default: () => {
			return 500
		}
	},
	btnLoading: {
		type: Boolean,
		default: () => {
			return false
		}
	},
	cancelText: {
		type: String,
		default: () => {
			return '取消'
		}
	},
	confirmText: {
		type: String,
		default: () => {
			return '确定'
		}
	},
	footer: {
		type: Boolean,
		default: () => {
			return true
		}
	}
})
const visible = defineModel({ type: Boolean })

const emit = defineEmits(['cancel', 'confirm'])

const onCancelClick = () => {
	emit('cancel')
}

const onConfirmClick = () => {
	emit('confirm')
}
</script>

<style lang="scss">
.dialog-popup {
	padding: 0;
	border-radius: 4px;
	.el-dialog__header {
		padding: 0;
		margin: 0;
		height: 56px;
		border-bottom: 1px solid #e5e6eb;
		.el-dialog__title {
			line-height: 56px;
			margin-left: 24px;
			font-size: 16px;
			color: #1d2129;
			font-weight: 600;
		}
	}
	.el-dialog__body {
		padding: 24px;
		box-sizing: border-box;
	}
	.dialog-footer {
		box-sizing: border-box;
		margin-right: 24px;
		padding-bottom: 24px;
	}
}
</style>
