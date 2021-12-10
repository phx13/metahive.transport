<template>
    <div :id="myChartId"
         :style="{ width: '100%', height: '300px' }"></div>
</template>
<script lang="js">

import emitter from "@/assets/js/emitter";
import request from "@/assets/js/request";

export default ({
	name: 'Dashboard',
	props: {
		chartId: String,
		chartTitle: String,
		chartColor: String
	},
	data() {
		return {
			myChartId: this.chartId,
			myChartTitle: this.chartTitle,
			myChartColor: this.chartColor
		}
	},
	mounted() {
		let myChart = this.$root.echarts.init(
			document.getElementById(this.myChartId)
		);
		
		const gaugeData = [
			{
				value: 20,
				name: this.myChartTitle,
				title: {
					offsetCenter: ['0%', '-30%'],
					color: "whitesmoke"
				},
				detail: {
					valueAnimation: true,
					offsetCenter: ['0%', '20%']
				}
			},
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
							borderColor: '#464646',
							color: this.myChartColor
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
						color: this.myChartColor,
						borderColor: this.myChartColor,
						borderRadius: 15,
						borderWidth: 1,
						formatter: '{value}%'
					}
				}
			]
		});
		
		emitter.on("receiveCoachId", (info) => {
			request.get("/api/server/coach/" + info).then(res => {
				switch (this.myChartTitle) {
					case "油量":
						gaugeData[0].value = parseInt(res.oil);
						break;
					case "温度":
						gaugeData[0].value = parseInt(res.temperature);
						break;
					case "压力":
						gaugeData[0].value = parseInt(res.pressure);
						break;
				}
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
			})
		})
	},
});
</script>

<style lang="css" scoped>

</style>

