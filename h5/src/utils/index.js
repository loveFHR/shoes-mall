// 防抖
export function debounce(func, wait, immediate) {
	let timeout // 定义一个计时器变量，用于延迟执行函数
	return function (...args) {
		// 返回一个包装后的函数
		const context = this // 保存函数执行上下文对象
		const later = function () {
			// 定义延迟执行的函数
			timeout = null // 清空计时器变量
			if (!immediate) func.apply(context, args) // 若非立即执行，则调用待防抖函数
		}
		const callNow = immediate && !timeout // 是否立即调用函数的条件
		clearTimeout(timeout) // 清空计时器
		timeout = setTimeout(later, wait) // 创建新的计时器，延迟执行函数
		if (callNow) func.apply(context, args) // 如果满足立即调用条件，则立即执行函数
	}
}
