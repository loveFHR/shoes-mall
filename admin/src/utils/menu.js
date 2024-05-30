import { notFoundAndNoPower, router } from '@/router'

// 获取目录下的 .vue、.tsx 全部文件
const layouModules = import.meta.glob('../layout/*.{vue,tsx}')
const viewsModules = import.meta.glob('../views/**/*.{vue,tsx}')
const dynamicViewsModules = Object.assign({}, { ...layouModules }, { ...viewsModules })

// 路由 component 转换
export function backEndComponent(routes) {
	if (!routes) return
	return routes.map((item) => {
		if (item.component) item.component = dynamicImport(dynamicViewsModules, item.component)
		item.children && backEndComponent(item.children)
		return item
	})
}

// 路由 component 转换函数
export function dynamicImport(dynamicViewsModules, component) {
	const keys = Object.keys(dynamicViewsModules)
	const matchKeys = keys.filter((key) => {
		const k = key.replace(/..\/views|../, '')
		return k.startsWith(`${component}`) || k.startsWith(`/${component}`)
	})
	if (matchKeys?.length === 1) {
		const matchKey = matchKeys[0]
		return dynamicViewsModules[matchKey]
	}
	if (matchKeys?.length > 1) {
		return false
	}
}

// 添加动态路由
export async function setAddRoute(menuArrList) {
	await setFilterRouteEnd(menuArrList).forEach((route) => {
		router.addRoute(route)
	})
}

// 处理路由格式及添加捕获所有路由或 404 Not found 路由
export function setFilterRouteEnd(menuArrList) {
	return [...menuArrList, ...notFoundAndNoPower]
}
