import SvgIcon from './SvgIcon/index.vue'
import NavBar from './NavBar/index.vue'
import Empty from './Empty/index.vue'

const components = {
	SvgIcon,
	NavBar,
	Empty
}

const install = function (Vue) {
	Object.keys(components).forEach((key) => {
		Vue.component(key, components[key])
	})
}

export default install
