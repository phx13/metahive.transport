<template>
    <div id="videoContainer" style="background-image: url('../../images/video-bg.png')">
        <h5 class="text-white">车厢实时视频</h5>
        <iframe :src="videoPath" style="height:200px" allowfullscreen></iframe>
    </div>
</template>

<script>
import emitter from "@/assets/js/emitter";
import request from "@/assets/js/request";

export default {
    name: "VideoPlayer",
    data() {
        return {
            videoPath: ""
        }
    },
    mounted() {//在模板编译完成后执行
        // 启用监听
        emitter.on('*', (type, info) => {
            request.get("/api/server/coach/" + info).then(res => {
                this.videoPath = res.videoPath;
            })
        })
    }
}
</script>

<style scoped>
#videoContainer{
    width: 100%;
    height: 300px;
}
</style>