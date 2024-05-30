<template>
	<div class="page"></div>
</template>

<script setup>
import useEcharts from '@/hooks/useEcharts'
import { type } from '@/api/statistics.api.js'

onMounted(async () => {
	await queryList()
})

const queryList = async () => {
	try {
		const { data } = await type()
		let arr = []
		data.map((item) => {
			arr.push({
				value: item.num,
				name: item.date
			})
		})
		const { setOptions } = useEcharts(document.querySelector('.page'))
		const option = {
			tooltip: {
				trigger: 'item',
				formatter: '{b}: ({d}%)'
			},
			toolbox: {
				feature: {
					restore: {},
					saveAsImage: {
						pixelRatio: 2
					}
				}
			},
			legend: {
				orient: 'vertical',
				left: 'left',
				formatter: (name) => {
					var dataIndex = 0
					var data = option.series[0].data
					for (var i = 0; i < data.length; i++) {
						if (data[i].name === name) {
							dataIndex = i
							break
						}
					}
					const val = (
						(data[dataIndex].value / option.series[0].data.reduce((acc, cur) => acc + cur.value, 0)) *
						100
					).toFixed(2)
					if (name.length > 10) {
						return name.substring(0, 8) + '...' + ` (${val}%)`
					} else {
						return name + val + ` (${val}%)`
					}
				}
			},
			series: [
				{
					type: 'pie',
					radius: '60%',
					center: ['70%', '40%'],
					data: arr,
					label: {
						normal: {
							show: true,
							formatter: function (params) {
								var { name, percent } = params
								if (name.length > 5) {
									name = name.substring(0, 10) + '...'
								}
								return name + ` (${percent}%)`
							}
						}
					}
				}
			]
		}
		setOptions(option)
	} catch (error) {
		console.log(error)
	}
}
</script>

<style lang="scss" scoped>
.page {
	height: 100%;
	.echartsRef {
		width: 100%;
		height: 100%;
		background-color: red !important;
	}
}
</style>
