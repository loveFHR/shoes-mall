import { ElMessage } from 'element-plus'

export default function () {
	function success(message) {
		ElMessage({
			message,
			type: 'success',
			duration: 2000
		})
	}

	function warning(message) {
		ElMessage({
			message,
			type: 'warning',
			duration: 2000
		})
	}

	function error(message) {
		ElMessage({
			message,
			type: 'error',
			duration: 2000
		})
	}

	return {
		success,
		warning,
		error
	}
}
