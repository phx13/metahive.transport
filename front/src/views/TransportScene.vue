<template>
    <header-nav/>
<!--	<div class="bg-overlay"></div>-->
    <div class="container-fluid" style="background-image: url('../../images/404.jpg')">
		
        <div class="row">
            <div class="col-2">
                <video-player/>
                <coach-detail ref="coachDetail"/>
            </div>
            <div class="col-8">
                <three-scene/>
            </div>
            <div class="col-2">
				<dashboard :chartId="chart1" :chartTitle="chart1Title" :chartColor="chart1Color"/>
				<dashboard :chartId="chart2" :chartTitle="chart2Title" :chartColor="chart2Color"/>
				<dashboard :chartId="chart3" :chartTitle="chart3Title" :chartColor="chart3Color"/>
            </div>
        </div>
    </div>
</template>

<script>
import ThreeScene from "@/components/ThreeScene";
import HeaderNav from "@/components/HeaderNav";
import Dashboard from "@/components/Dashboard";
import VideoPlayer from "@/components/VideoPlayer";
import CoachDetail from "@/components/CoachDetail";

export default {
    name: "TransportScene",
    data() {
        return {
			chart1: "chart1",
			chart1Title: "油量",
			chart1Color: "#5470c6",
			chart2: "chart2",
			chart2Title: "温度",
			chart2Color: "#91cc75",
			chart3: "chart3",
			chart3Title: "压力",
			chart3Color: "#fac858",
        }
    },
	components: {CoachDetail, VideoPlayer, Dashboard, HeaderNav, ThreeScene}
}
</script>

<style scoped>
</style>