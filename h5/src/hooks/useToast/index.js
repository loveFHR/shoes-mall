import { showToast } from 'vant'
import 'vant/es/toast/style'

export default function () {
	function toast(message) {
		setTimeout(() => {
			showToast(message)
		}, 200)
	}

	return {
		toast
	}
}
