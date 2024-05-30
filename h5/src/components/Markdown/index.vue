<template>
	<div class="editor">
		<VueMarkdownEditor
			placeholder="请输入内容..."
			v-model="htmlText"
			:height="height"
			:mode="mode"
			:disabled-menus="[]"
		/>
	</div>
</template>

<script setup>
import VueMarkdownEditor from '@kangc/v-md-editor'
import '@kangc/v-md-editor/lib/style/base-editor.css'
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js'
import '@kangc/v-md-editor/lib/theme/style/vuepress.css'

import Prism from 'prismjs'
import 'prismjs/components/prism-json'

VueMarkdownEditor.use(vuepressTheme, {
	Prism
})

defineProps({
	height: {
		type: String,
		default: () => {
			return '400px'
		}
	},
	mode: {
		type: String,
		default: () => {
			return 'editable'
		}
	}
})

const htmlText = defineModel({ type: String })
//左侧工具栏
const leftBar = ref('undo redo clear h bold italic strikethrough quote ul ol table hr link image code')
</script>

<style scoped lang="scss">
.editor {
	width: 100%;
	height: 100%;
}
.v-md-editor {
	box-shadow: none;
	padding: 0;
}
</style>
<style lang="scss">
.vuepress-markdown-body {
	padding: 0 !important;
	img {
		margin-top: -10px !important;
	}
}
</style>
