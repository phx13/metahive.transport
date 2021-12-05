<template>
    <div id="coachDetail">
        <ul class="list-group list-group-flush">
            <h5> 车厢信息 </h5>
            <li class="list-group-item">车厢编号： {{ currentCoach.id }}</li>
            <li class="list-group-item">车厢司机： {{ currentCoach.user }}</li>
            <li class="list-group-item">车厢型号： {{ currentCoach.type }}</li>
            <li class="list-group-item">车厢坐标： {{ currentCoach.coordinate }}</li>
            <li class="list-group-item">车厢来自： {{ currentCoach.from }}</li>
            <li class="list-group-item">车厢去往： {{ currentCoach.to }}</li>
            <li class="list-group-item">车厢油量： {{ currentCoach.oil }}</li>
            <li class="list-group-item">车厢温度： {{ currentCoach.temperature }}</li>
            <li class="list-group-item">车厢湿度： {{ currentCoach.humidity }}</li>
            <li class="list-group-item">车厢压力： {{ currentCoach.pressure }}</li>
        </ul>
    </div>
</template>

<script>

import emitter from "@/assets/js/emitter";
import request from "@/assets/js/request";

export default {
    name: "CoachDetail",
    data() {
        return {
            currentCoach: {
                id: "",
                user: "",
                type: "",
                coordinate: "",
                from: "",
                to: "",
                oil: "",
                temperature: "",
                humidity: "",
                pressure: ""
            }
        }
    },
    mounted() {//在模板编译完成后执行
        // 启用监听
        emitter.on('*', (type, info) => {
            console.log(type, info);
            request.get("/api/server/coach/"+info).then(res => {
                console.log(res);
                this.currentCoach.id = res.id;
                this.currentCoach.user = res.username;
                this.currentCoach.type = res.type;
                this.currentCoach.coordinate = res.currentCoord;
                this.currentCoach.from = res.fromName;
                this.currentCoach.to = res.toName;
                this.currentCoach.oil = res.oil;
                this.currentCoach.temperature = res.temperature;
                this.currentCoach.humidity = res.humidity;
                this.currentCoach.pressure = res.pressure;
            })
        })
    }
}
</script>

<style scoped>

</style>