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
                <dashboard :chartId="chart4"/>
                <dashboard :chartId="chart5"/>
                <dashboard :chartId="chart6"/>
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
            chart4: "chart4",
            chart5: "chart5",
            chart6: "chart6"
        }
    },
	components: {CoachDetail, VideoPlayer, Dashboard, HeaderNav, ThreeScene}
}
</script>

<style scoped>
</style>