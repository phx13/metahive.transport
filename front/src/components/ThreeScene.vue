<template>
    <div id="containerScene" ref="containerScene"></div>
</template>

<script>
import * as THREE from "three";
import {GLTFLoader} from 'three/examples/jsm/loaders/GLTFLoader.js';
import {OrbitControls} from "three/examples/jsm/controls/OrbitControls";
import {GUI} from "three/examples/jsm/libs/lil-gui.module.min";
import emitter from "@/assets/js/emitter";
import request from "@/assets/js/request";
import {degToRad} from "three/src/math/MathUtils";

let scene, camera, renderer, light, pointLight, loader, controls;

export default {
    name: "ThreeScene",
    methods: {
        //初始化three.js相关内容
        init() {
            //初始化场景
            scene = new THREE.Scene();

            //初始化相机
            const containerWidth = this.$refs.containerScene.offsetWidth;
            const containerHeight = this.$refs.containerScene.offsetHeight;
            camera = new THREE.PerspectiveCamera(100, containerWidth / containerHeight, 100, 10000);
            // camera.position.set(2371.0970415723077, 2000, -1183.5378941679967);
            camera.position.set(0, 1500, 0);
            camera.lookAt(new THREE.Vector3(0, -1, 0));

            //初始化渲染器
            renderer = new THREE.WebGLRenderer();
            renderer.setSize(containerWidth, containerHeight);
            renderer.setClearColor(0xdddddd, 1);
            this.$refs.containerScene.appendChild(renderer.domElement);

            //初始化灯光
            light = new THREE.AmbientLight(0xffffff, 0.5);
            scene.add(light);
            pointLight = new THREE.PointLight(0xffffff, 0.7, 1000);
            // pointLight.position.set(2371.0970415723077, 1000, -1183.5378941679967);
            pointLight.position.set(0, 1000, 0);
            scene.add(pointLight);

            //初始化模型
            loader = new GLTFLoader();

            /**
             * 经纬度转xyz
             * @param longitude 经度
             * @param latitude 纬度
             * @param radius 半径
             */
            const lglt2xyz = function (longitude, latitude, radius) {
                var lg = degToRad(longitude), lt = degToRad(latitude);
                var y = radius * Math.sin(lt);
                var temp = radius * Math.cos(lt);
                var x = temp * Math.sin(lg);
                var z = temp * Math.cos(lg);
                return {x: x, y: y, z: z}
            }

            request.get("/api/server/coach/all").then(res => {
                res.forEach((item) => {
                    loader.load('../../model/cistern__eight-axle_oil_tank/scene.gltf', function (train) {
                        train.scene.name = item.id;
                        let lon = parseFloat(item.currentCoord.split(",")[0]);
                        let lat = parseFloat(item.currentCoord.split(",")[1]);
                        // let xyz = lglt2xyz(lon, lat, 3459);
                        // train.scene.position.set(xyz.x, 0, xyz.z);

                        train.scene.position.set((lon - 116.52616976895676) * 100000, 0, (lat - 39.991732847489686) * 100000);
                        console.log(train.scene.position);
                        // x: 2371.3343931617887, y: 0, z: -1182.842217987483
                        // x: 2371.6006096000683, y: 0, z: -1182.9572624025234
                        // x: 2370.951777816797, y: 0, z: -1183.0261865318964
                        train.scene.scale.set(5, 5, 5);
                        scene.add(train.scene);
                    });
                })
            })

            // loader.load('../../model/low-poly_circle_rug/scene.gltf', function (track) {
            //     track.scene.name = "track";
            //     // track.scene.rotateZ(Math.PI / 2);
            //     scene.add(track.scene);
            // });

            const track = new THREE.RingGeometry( 1600, 1580, 50 );
            const trackMaterial = new THREE.MeshBasicMaterial( { color: 0xffffff, side: THREE.DoubleSide } );
            const trackMesh = new THREE.Mesh( track, trackMaterial );
            track.rotateX(Math.PI / 2);
            scene.add( trackMesh );

            // loader.load('../../model/cistern__eight-axle_oil_tank/scene.gltf', function (train) {
            //     train.scene.name = "train";
            //     train.scene.translateZ(-70);
            //     scene.add(train.scene);
            // });

            // loader.load('../../model/railway_tracks/scene.gltf', function (track) {
            //     track.scene.scale.set(5, 5, 5);
            //     track.scene.rotateY(Math.PI / 2);
            //     for (let i = 0; i < 10; i++) {
            //         let forwardTrack = track.scene.clone();
            //         forwardTrack.translateX(10 * i);
            //         let backTrack = track.scene.clone();
            //         backTrack.translateX(-10 * i);
            //         scene.add(forwardTrack);
            //         scene.add(backTrack);
            //     }
            //     scene.add(track.scene);
            // });


            let pos = 0;

            function trainMove() {
                if (scene.getObjectByName('train')) {
                    let train = scene.getObjectByName('train');
                    if (pos < 1 && train.position.z < 80) {
                        train.translateZ(0.1);
                        pos += 0.1;
                    } else {
                        pos = 0;
                    }
                }
            }

            //坐标系工具
            const axesHelper = new THREE.AxesHelper(100);
            scene.add(axesHelper);

            //初始化控制器
            controls = new OrbitControls(camera, renderer.domElement);
            controls.listenToKeyEvents(this.$refs.containerScene);
            controls.enableDamping = true; // an animation loop is required when either damping or auto-rotation are enabled
            controls.dampingFactor = 0.05;
            controls.screenSpacePanning = false;
            controls.minDistance = 10;
            controls.maxDistance = 10000;
            controls.maxPolarAngle = Math.PI / 2;

            //初始化容器放缩事件
            window.onresize = function () {
                camera.aspect = containerWidth / containerHeight;
                camera.updateProjectionMatrix();
                renderer.setSize(containerWidth, containerHeight);
            };

            function animate() {
                requestAnimationFrame(animate);
                controls.update();
                // trainMove();
                renderer.render(scene, camera);
            }

            animate();
        },
        createPanel() {
            const gui = new GUI({container: this.$refs.containerScene, title: "三维场景控制器"});

            const coachList = gui.addFolder("车厢列表");

            const coaches = {};
            request.get("/api/server/coach/all").then(res => {
                res.forEach((item) => {
                    const key = item.id;
                    const value = function () {
                        emitter.emit("receiveCoachId", key);
                    }
                    coaches[key] = value;
                    coachList.add(coaches, key);
                })
            })
            gui.domElement.style.position = "absolute";
            gui.domElement.style.top = "5em";
        },

    },
    mounted() {
        this.init();
        this.createPanel();
    }
}
</script>

<style scoped>
#containerScene {
    width: 100%;
    height: 900px;
}
</style>