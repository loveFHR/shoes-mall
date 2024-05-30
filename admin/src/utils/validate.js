/*
 * @Author: Daixin
 * @Date: 2023-01-16 03:56:24
 * @Description: 判断是否为外部资源 true代表是外部资源
 * @param path 资源路径
 */
export function isExternal(path) {
	return /^(https?:|mailto:|tel:)/.test(path)
}
