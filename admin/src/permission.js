import router from '@/router'
import NProgress from 'nprogress'
import { getToken } from '@/utils/auth'
import 'nprogress/nprogress.css'

// 白名单  能直接访问
const whiteList = ['/login']

router.beforeEach(async (to, from, next) => {
	if (to?.meta?.title) document.title = to?.meta?.title

	NProgress.start()
	if (getToken()) {
		if (to.path === '/login') {
			next({ path: '/' })
		} else {
			next()
		}
	} else {
		if (whiteList.indexOf(to.path) !== -1) {
			next()
		} else {
			next('/login')
			NProgress.done()
		}
	}
})

router.afterEach(() => {
	NProgress.done()
})
