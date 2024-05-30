import { ElMessageBox } from 'element-plus'

export default function useConfirm(message) {
	return new Promise((resolve, reject) => {
		ElMessageBox.confirm(message, '警告', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning'
		})
			.then(() => {
				resolve('confirm')
			})
			.catch((err) => {
				reject('cancel')
			})
	})
}
