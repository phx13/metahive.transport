<template>
<!--    <video id="videoContainer" controls autoplay src="https://www.youtube.com/embed/FkqLBkBvd3U"/>-->
    <iframe id="videoContainer" src="https://www.youtube.com/embed/FkqLBkBvd3U" title="车厢实时视频" allowfullscreen></iframe>
</template>

<script>
export default {
    name: "VideoPlayer"
}
</script>

<style scoped>
#videoContainer{
    width: 100%;
    height: 300px;
}
</style>