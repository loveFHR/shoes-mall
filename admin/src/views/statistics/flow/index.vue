<template>
	<div class="page"></div>
</template>

<script setup>
import useEcharts from '@/hooks/useEcharts'
import { order } from '@/api/statistics.api.js'

onMounted(async () => {
	await queryList()
})

const queryList = async () => {
	try {
		const { data } = await order()
		const { setOptions } = useEcharts(document.querySelector('.page'))
		const option = {
			toolbox: {
				feature: {
					dataZoom: {
						yAxisIndex: false
					},
					restore: {},
					saveAsImage: {
						pixelRatio: 2
					}
				}
			},
			tooltip: {
				trigger: 'axis',
				formatter: (params) => {
					const { axisValue, data } = params[0]
					return `${axisValue} 订单数: ${data}`
				}
			},
			grid: {
				bottom: 90
			},
			dataZoom: [
				{
					type: 'inside'
				},
				{
					type: 'slider'
				}
			],
			xAxis: {
				type: 'category',
				boundaryGap: false,
				data: data.map((item) => item.date)
			},
			yAxis: {
				type: 'value'
			},
			series: [
				{
					type: 'line',
					smooth: true,
					areaStyle: {},
					data: data.map((item) => item.num)
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
