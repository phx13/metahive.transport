<template>
    <div id="coachDetailContainer">
        <ul class="list-group list-group-flush">
            <h5 class="text-white"> 车厢信息 </h5>
            <img :src="currentCoach.imagePath" style="height: 150px">
            <li class="list-group-item list-group-item-dark">车厢编号： {{ currentCoach.id }}</li>
            <li class="list-group-item list-group-item-dark">车厢司机： {{ currentCoach.user }}</li>
            <li class="list-group-item list-group-item-dark">车厢型号： {{ currentCoach.type }}</li>
            <li class="list-group-item list-group-item-dark">车厢坐标： {{ currentCoach.coordinate }}</li>
            <li class="list-group-item list-group-item-dark">车厢来自： {{ currentCoach.from }}</li>
            <li class="list-group-item list-group-item-dark">车厢去往： {{ currentCoach.to }}</li>
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
				imagePath: ""
			}
		}
	},
	mounted() {
		emitter.on("receiveCoachId", (info) => {
			request.get("/api/server/coach/" + info).then(res => {
				this.currentCoach.id = res.id;
				this.currentCoach.user = res.username;
				this.currentCoach.type = res.type;
				this.currentCoach.coordinate = res.currentCoord;
				this.currentCoach.from = res.fromName;
				this.currentCoach.to = res.toName;
				this.currentCoach.imagePath = res.imagePath;
			})
		})
	}
}
</script>

<style scoped>
#coachDetailContainer{
    width: 100%;
}
</style>