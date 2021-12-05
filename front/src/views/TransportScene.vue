<template>
    <header-nav/>
    <div class="container-fluid">
        <div class="row">
            <div class="col-2">
                <video-player/>
                <coach-detail ref="coachDetail"/>
            </div>
            <div class="col-8">
                <three-scene/>
            </div>
            <div class="col-2">
                <bar-chart :chartId="chart4"/>
                <bar-chart :chartId="chart5"/>
                <bar-chart :chartId="chart6"/>
            </div>
        </div>
    </div>
</template>

<script>
import ThreeScene from "@/components/ThreeScene";
import HeaderNav from "@/components/HeaderNav";
import BarChart from "@/components/BarChart";
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
    components: {CoachDetail, VideoPlayer, BarChart, HeaderNav, ThreeScene}
}
</script>

<style scoped>
</style>