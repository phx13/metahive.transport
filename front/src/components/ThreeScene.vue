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
import {TWEEN} from "three/examples/jsm/libs/tween.module.min";
import sceneHelper from "@/assets/js/scene-helper";

let scene, camera, renderer, light, pointLight, gltfLoader, controls;

export default {
	name: "ThreeScene",
	methods: {
		//初始化three.js相关内容
		init() {
			//初始化场景
			scene = sceneHelper.createScene();
			
			//天空盒
			const materialArray = sceneHelper.loadSkyboxMaterial("../../skybox/", "Daylight Box", ".bmp");
			const skybox = sceneHelper.createSkybox(materialArray, 5000, 5000, 5000);
			skybox.position.set(0, 2500, 0);
			scene.add(skybox);
			
			//初始化相机
			const containerWidth = this.$refs.containerScene.offsetWidth;
			const containerHeight = this.$refs.containerScene.offsetHeight;
			camera = new THREE.PerspectiveCamera(100, containerWidth / containerHeight, 10, 15000)
			camera.position.set(0, 700, 2000);
			camera.lookAt(new THREE.Vector3(0, 0, 0));
			
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
			gltfLoader = new GLTFLoader();
			
			request.get("/api/server/coach/all").then(res => {
				res.forEach((item) => {
					gltfLoader.load('../../model/cistern__eight-axle_oil_tank/scene.gltf', function (train) {
						train.scene.name = item.id;
						train.scene.userData.lon = parseFloat(item.currentCoord.split(",")[0]);
						train.scene.userData.lat = parseFloat(item.currentCoord.split(",")[1]);
						train.scene.userData.toLon = parseFloat(item.toCoord.split(",")[0]);
						train.scene.userData.toLat = parseFloat(item.toCoord.split(",")[1]);
						
						train.scene.position.set((train.scene.userData.lon - 116.52616976895676) * 100000, 0, -(train.scene.userData.lat - 39.991732847489686) * 100000);
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
			
			// const track = new THREE.RingGeometry(1700, 1680, 50);
			// const trackMaterial = new THREE.MeshBasicMaterial({color: 0x001111, side: THREE.DoubleSide});
			// const trackMesh = new THREE.Mesh(track, trackMaterial);
			// // track.rotation.x = Math.PI / 2;
			// track.rotateX(Math.PI / 2);
			// scene.add(trackMesh);
			//
			// const ground = new THREE.CircleGeometry(1650, 128);
			// const groundMaterial = new THREE.MeshBasicMaterial({color: 0xADFF2F, side: THREE.DoubleSide});
			// const groundMash = new THREE.Mesh(ground, groundMaterial);
			// // ground.rotation.x = Math.PI / 2;
			// ground.rotateX(Math.PI / 2);
			// scene.add(groundMash);
			
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
			
			
			//坐标系工具
			// const axesHelper = new THREE.AxesHelper(500);
			// scene.add(axesHelper);
			
			//初始化控制器
			controls = new OrbitControls(camera, renderer.domElement);
			controls.listenToKeyEvents(this.$refs.containerScene);
			controls.enableDamping = true; // an animation loop is required when either damping or auto-rotation are enabled
			controls.dampingFactor = 0.05;
			controls.screenSpacePanning = false;
			controls.minDistance = 100;
			controls.maxDistance = 3000;
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
				TWEEN.update();
				renderer.render(scene, camera);
			}
			
			animate();
		},
		createPanel() {
			const gui = new GUI({container: this.$refs.containerScene, title: "三维场景控制器"});
			
			
			//车厢列表
			const coachList = gui.addFolder("终端列表");
			const coaches = {};
			request.get("/api/server/coach/all").then(res => {
				res.forEach((item) => {
					const key = item.id;
					const value = function () {
						emitter.emit("receiveCoachId", key);
						let currentCoach = scene.getObjectByName(key);
						new TWEEN.Tween(currentCoach.position)
						.to({
							x: (currentCoach.userData.toLon - 116.52616976895676) * 100000,
							y: 0,
							z: -(currentCoach.userData.toLat - 39.991732847489686) * 100000
						}, 15000)
						.start();
						
						animateCamera(camera.position,
							{
								x: currentCoach.position.x + 100,
								y: 100,
								z: currentCoach.position.z + 100
							}, controls.target, currentCoach.position);
						
					}
					
					coaches[key] = value;
					coachList.add(coaches, key).name("终端 " + key);
				})
			})
			
			// current1 相机当前的位置
			// target1 相机的目标位置
			// current2 当前的controls的target
			// target2 新的controls的target
			function animateCamera(current1, target1, current2, target2) {
				var tween = new TWEEN.Tween({
					x1: current1.x, // 相机当前位置x
					y1: current1.y, // 相机当前位置y
					z1: current1.z, // 相机当前位置z
					x2: current2.x, // 控制当前的中心点x
					y2: current2.y, // 控制当前的中心点y
					z2: current2.z  // 控制当前的中心点z
				});
				tween.to({
					x1: target1.x, // 新的相机位置x
					y1: target1.y, // 新的相机位置y
					z1: target1.z, // 新的相机位置z
					x2: target2.x, // 新的控制中心点位置x
					y2: target2.y, // 新的控制中心点位置x
					z2: target2.z  // 新的控制中心点位置x
				}, 1000);
				tween.onUpdate(function (object) {
					camera.position.x = object.x1;
					camera.position.y = object.y1;
					camera.position.z = object.z1;
					controls.target.x = object.x2;
					controls.target.y = object.y2;
					controls.target.z = object.z2;
					controls.update();
				})
				tween.onComplete(function () {
					controls.enabled = true;
				})
				tween.easing(TWEEN.Easing.Cubic.InOut);
				tween.start();
			}
			
			//控制列表
			const controlList = gui.addFolder("控制列表");
			const controlObjs = {
				getCameraPosition: function () {
					alert(camera.position.x + "\n" + camera.position.y + "\n" + camera.position.z);
				}
			}
			controlList.add(controlObjs, "getCameraPosition").name("获取相机位置");
			
			// 编辑列表
			const editList = gui.addFolder("编辑列表");
			const editObjs = {
				addCube: function () {
					const geometry = new THREE.BoxGeometry(editObjs.width, editObjs.height, editObjs.depth);
					const material = new THREE.MeshBasicMaterial({color: colorFormats.string});
					const cube = new THREE.Mesh(geometry, material);
					cube.position.set(editObjs.positionX, editObjs.positionY, editObjs.positionZ);
					scene.add(cube);
				},
				width: 100,
				height: 100,
				depth: 100,
				positionX: 0,
				positionY: 0,
				positionZ: 0,
			}
			// Create color pickers for multiple color formats
			const colorFormats = {
				string: '#ffffff',
				int: 0xffffff,
				object: {r: 1, g: 1, b: 1},
				array: [1, 1, 1]
			};
			editList.add(editObjs, "addCube").name("添加一个楼房");
			editList.add(editObjs, 'width').name("宽");   // Number Field
			editList.add(editObjs, 'height').name("高");   // Number Field
			editList.add(editObjs, 'depth').name("长");   // Number Field
			editList.add(editObjs, 'positionX').name("x轴");   // Number Field
			editList.add(editObjs, 'positionY').name("y轴");   // Number Field
			editList.add(editObjs, 'positionZ').name("z轴");   // Number Field
			editList.addColor(colorFormats, 'string').name("颜色");
			
			
			// 场景操作
			const sceneList = gui.addFolder("场景操作");
			const sceneObjs = {
				saveScene: function () {
					let data = JSON.stringify(scene.toJSON());
					const a = document.createElement("a");
					a.href = URL.createObjectURL(new Blob([JSON.stringify(scene.toJSON(), null, 2)], {
						type: "text/plain"
					}));
					a.setAttribute("download", "scene.txt");
					document.body.appendChild(a);
					a.click();
					document.body.removeChild(a);
				},
				loadScene: function () {
					const input = document.createElement("input");
					input.setAttribute("type", "file");
					document.body.appendChild(input);
					input.click();
					
					// 监听change事件:
					input.addEventListener('change', function () {
						// 检查文件是否选择:
						if (!input.value) {
							return;
						}
						// 获取File引用:
						var file = input.files[0];
						
						// 读取文件:
						var reader = new FileReader();
						reader.onload = function (e) {
							let data = this.result; // 'data:image/jpeg;base64,/9j/4AAQSk...(base64编码)...'
							// console.log(data)
							
							if (data) {
								
								// 将 sceneJson 字符串转换为 json 对象
								let loadedScene = JSON.parse(data);
								let loader = new THREE.ObjectLoader();
								
								scene = loader.parse(loadedScene);
								
							}
						};
						// 以DataURL的形式读取文件:
						reader.readAsText(file);

// 						const loader = new THREE.FileLoader();
//
// //加载一个文本文件，并把结果输出到控制台上
// 						loader.load(
// 							// resource URL
// 							file.name,
//							
// 							// onLoad回调
// 							function (data) {
// 								// output the text to the console
// 								console.log(data);
// 								let loadedScene = JSON.parse(data);
// 								let loader = new THREE.ObjectLoader();
//								
// 								scene = loader.parse(loadedScene);
//								
// 							},
//							
// 							// onProgress回调
// 							function (xhr) {
// 								console.log((xhr.loaded / xhr.total * 100) + '% loaded');
// 							},
//							
// 							// onError回调
// 							function (err) {
// 								console.error('An error happened');
// 							}
// 						);
						document.body.removeChild(input);
					});
					
					
					// if (json) {
					// 	var loadedGeometry = JSON.parse(json);
					// 	var loader = new THREE.ObjectLoader();
					// 	scene = loader.parse(loadedGeometry);
					// }
				},
				clearScene: function () {
					scene = new THREE.Scene();
				}
			}
			sceneList.add(sceneObjs, "saveScene").name("保存场景");
			sceneList.add(sceneObjs, "loadScene").name("加载场景");
			sceneList.add(sceneObjs, "clearScene").name("清空场景");
			
			gui.domElement.style.position = "absolute";
			gui.domElement.style.top = "5.5em";
			gui.domElement.style.left = "30.5em";
		},
		createGlobalController: function () {
			let globalController = sceneHelper.createGlobalController(this.$refs.containerScene, "三维场景控制器", "5.5em", "30.5em");
			sceneHelper.addSkyboxController(scene, globalController);
		}
	},
	mounted() {
		this.init();
		// this.createPanel();
		this.createGlobalController();
	}
}
</script>

<style scoped>
#containerScene {
    width: 100%;
    height: 900px;
}
</style>