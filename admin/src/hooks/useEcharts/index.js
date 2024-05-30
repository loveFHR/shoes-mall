import Echarts from '@/utils/echarts.js'

export default function (el) {
	const echart = Echarts.init(el)

	const setOptions = (options) => {
		echart.setOption(options)
	}
	const updateSize = () => {
		echart.resize()
	}

	window.addEventListener('resize', () => {
		echart.resize()
	})

	return {
		echart,
		setOptions,
		updateSize
	}
}
