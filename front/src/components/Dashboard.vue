<template>
    <div :id="myChartId"
         :style="{ width: '100%', height: '300px' }"></div>
</template>
<script lang="js">

export default ({
    name: 'Dashboard',
    props: {chartId: String},
    data() {
        return {
            myChartId: this.chartId
        }
    },
    mounted() {
		//this.$root => app
		let myChart = this.$root.echarts.init(
			document.getElementById(this.myChartId)
		);
	
		const gaugeData = [
			{
				value: 20,
				name: '油量',
				title: {
					offsetCenter: ['0%', '-20%']
				},
				detail: {
					valueAnimation: true,
					offsetCenter: ['0%', '20%']
				}
			},
			// {
			// 	value: 40,
			// 	name: 'Good',
			// 	title: {
			// 		offsetCenter: ['0%', '0%']
			// 	},
			// 	detail: {
			// 		valueAnimation: true,
			// 		offsetCenter: ['0%', '10%']
			// 	}
			// },
			// {
			// 	value: 60,
			// 	name: 'Commonly',
			// 	title: {
			// 		offsetCenter: ['0%', '30%']
			// 	},
			// 	detail: {
			// 		valueAnimation: true,
			// 		offsetCenter: ['0%', '40%']
			// 	}
			// }
		];
	
		// 绘制图表
		myChart.setOption({
			series: [
				{
					type: 'gauge',
					startAngle: 90,
					endAngle: -270,
					pointer: {
						show: false
					},
					progress: {
						show: true,
						overlap: false,
						roundCap: true,
						clip: false,
						itemStyle: {
							borderWidth: 1,
							borderColor: '#464646'
						}
					},
					axisLine: {
						lineStyle: {
							width: 20
						}
					},
					splitLine: {
						show: false,
						distance: 0,
						length: 10
					},
					axisTick: {
						show: false
					},
					axisLabel: {
						show: false,
						distance: 50
					},
					data: gaugeData,
					title: {
						fontSize: 20
					},
					detail: {
						width: 80,
						height: 40,
						fontSize: 25,
						color: 'auto',
						borderColor: 'auto',
						borderRadius: 15,
						borderWidth: 1,
						formatter: '{value}%'
					}
				}
			]
		});
	
		setInterval(function () {
			gaugeData[0].value = +(Math.random() * 100).toFixed(2);
			// gaugeData[1].value = +(Math.random() * 100).toFixed(2);
			// gaugeData[2].value = +(Math.random() * 100).toFixed(2);
			myChart.setOption({
				series: [
					{
						data: gaugeData,
						pointer: {
							show: false
						}
					}
				]
			});
		}, 2000);
	},
});
</script>

<style lang="css" scoped>

</style>

