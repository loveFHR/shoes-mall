export function getRawRoute(route) {
	if (!route) return route
	const { matched, ...opt } = route
	return {
		...opt,
		matched: matched
			? matched.map((item) => ({
					meta: item.meta,
					name: item.name,
					path: item.path
				}))
			: undefined
	}
}
