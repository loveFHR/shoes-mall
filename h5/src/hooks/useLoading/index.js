import { showLoadingToast, closeToast } from 'vant'
import 'vant/es/toast/style'

export default function () {
	const isLoading = ref(false)

	function loading(message) {
		isLoading.value = true
		showLoadingToast({
			message,
			loadingType: 'spinner',
			forbidClick: true,
			duration: 0
		})
	}

	function closeLoading() {
		isLoading.value = false
		closeToast()
	}

	return {
		loading,
		closeLoading,
		isLoading
	}
}
