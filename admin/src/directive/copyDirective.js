import useClipboard from 'vue-clipboard3'
import useMessage from '@/hooks/useMessage'

export default (app) => {
	app.directive('copy', {
		beforeMount(el, { value }) {
			const { toClipboard } = useClipboard()
			const { success } = useMessage()
			el.addEventListener('click', () => {
				if (value) {
					try {
						toClipboard(value, el)
						success('复制成功')
					} catch (error) {
						console.log(error)
					}
				}
			})
		}
	})
}
