<template>
    <div id="containerScene" ref="containerScene" @click="containerOnClick"></div>
</template>

<script>
import * as THREE from "three";
import {GLTFLoader} from 'three/examples/jsm/loaders/GLTFLoader.js';
import {OrbitControls} from "three/examples/jsm/controls/OrbitControls";
import emitter from "@/assets/js/emitter";
import request from "@/assets/js/request";
import {TWEEN} from "three/examples/jsm/libs/tween.module.min";
import sceneHelper from "@/assets/js/scene-helper";

let scene, camera, renderer, light, pointLight, gltfLoader, controls, container, staticGroup, dynamicGroup, raycaster,
	mouse;

export default {
	name: "ThreeScene",
	data() {
		return {
			currentSelectedEntity: null
		}
	},
	methods: {
		/**
		 * 初始化场景
		 */
		initScene() {
			scene = sceneHelper.createScene();
			container = this.$refs.containerScene;
		},
		/**
		 * 初始化天空盒
		 */
		initSkybox() {
			const skybox = sceneHelper.createMash(sceneHelper.mashGeometries.box, "skybox");
			sceneHelper.updateSkyboxMaterial(skybox, sceneHelper.skyboxTypes.sun);
			sceneHelper.updateMashScale(skybox, 5000, 5000, 5000);
			sceneHelper.updateMashPosition(skybox, 0, 2500, 0);
			scene.add(skybox);
		},
		/**
		 * 初始化相机
		 */
		initCamera() {
			camera = new THREE.PerspectiveCamera(100, container.offsetWidth / container.offsetHeight, 10, 15000)
			camera.position.set(0, 700, 2000);
			camera.lookAt(new THREE.Vector3(0, 0, 0));
		},
		/**
		 * 初始化渲染器
		 */
		initRender() {
			renderer = new THREE.WebGLRenderer();
			renderer.setSize(container.offsetWidth, container.offsetHeight);
			renderer.setClearColor(0xdddddd, 1);
			this.$refs.containerScene.appendChild(renderer.domElement);
		},
		/**
		 * 初始化灯光
		 */
		initLight() {
			light = new THREE.AmbientLight(0xffffff, 0.5);
			scene.add(light);
			pointLight = new THREE.PointLight(0xffffff, 0.7, 1000);
			pointLight.position.set(0, 1000, 0);
			scene.add(pointLight);
		},
		/**
		 * 初始化控制器
		 */
		initOrbitControl() {
			controls = new OrbitControls(camera, renderer.domElement);
			controls.listenToKeyEvents(this.$refs.containerScene);
			controls.enableDamping = true; // an animation loop is required when either damping or auto-rotation are enabled
			controls.dampingFactor = 0.05;
			controls.screenSpacePanning = false;
			controls.minDistance = 100;
			controls.maxDistance = 3000;
			controls.maxPolarAngle = Math.PI / 2;
		},
		/**
		 * 初始化组
		 */
		initGroup() {
			staticGroup = new THREE.Group();
			dynamicGroup = new THREE.Group();
			scene.add(staticGroup);
			scene.add(dynamicGroup);
		},
		initRaycaster() {
			raycaster = new THREE.Raycaster();
			mouse = new THREE.Vector2();
		},
		containerOnClick(event) {
			let getBoundingClientRect = container.getBoundingClientRect();
			mouse.x = ((event.clientX - getBoundingClientRect.left) / container.offsetWidth) * 2 - 1;
			mouse.y = -((event.clientY - getBoundingClientRect.top) / container.offsetHeight) * 2 + 1;
		},
		/**
		 * 刷新方法
		 */
		updated() {
			window.onresize = function () {
				camera.aspect = container.offsetWidth / container.offsetHeight;
				camera.updateProjectionMatrix();
				renderer.setSize(container.offsetWidth, container.offsetHeight);
			};
			
			function animate() {
				requestAnimationFrame(animate);
				
				raycaster.setFromCamera(mouse, camera);
				const intersects = raycaster.intersectObjects(staticGroup.children);
				if (intersects.length > 0) {
					for (let i = 0; i < intersects.length; i++) {
						// emitter.emit("currentSelectedChanged", intersects[i].object);
					}
				}
				
				controls.update();
				TWEEN.update();
				renderer.render(scene, camera);
			}
			
			animate();
		},
		createController: function () {
			let globalController = sceneHelper.createGlobalController(this.$refs.containerScene, "三维场景控制器", "5.5em", "30.5em");
			let entityController = sceneHelper.createGlobalController(this.$refs.containerScene, "实体列表控制器", "5.5em", "121.5em");
			
			sceneHelper.addSkyboxController(scene, globalController);
			addStaticEntityController(globalController);
			addDynamicEntityController(globalController);
			addSceneController(globalController);
			
			/**
			 * 添加静态实体控制器
			 * @param parent 父控制器
			 */
			function addStaticEntityController(parent) {
				let currentSelected = null;
				
				/**
				 * 物体材质改变事件
				 */
				function mashMaterialOnChanged() {
					if (currentSelected) {
						sceneHelper.updateMashMaterial(currentSelected, staticEntityObjects.colorFormats.int);
					}
				}
				
				/**
				 * 物体大小改变事件
				 */
				function mashScaleOnChanged() {
					if (currentSelected) {
						sceneHelper.updateMashScale(currentSelected, staticEntityObjects.width, staticEntityObjects.height, staticEntityObjects.depth);
					}
				}
				
				/**
				 * 物体位置改变事件
				 */
				function mashPositionOnChanged() {
					if (currentSelected) {
						sceneHelper.updateMashPosition(currentSelected, staticEntityObjects.positionX, staticEntityObjects.positionY, staticEntityObjects.positionZ);
					}
				}
				
				/**
				 * 物体控制器功能
				 */
				const staticEntitiesList = entityController.addFolder("静态实体");
				const staticEntities = {};
				
				const staticEntityList = parent.addFolder("静态实体操作");
				const staticEntityObjects = {
					type: "立方体",
					name: "",
					width: 100,
					height: 100,
					depth: 100,
					positionX: 0,
					positionY: 0,
					positionZ: 0,
					colorFormats: {
						string: '#ffffff',
						int: 0xffffff,
						object: {r: 1, g: 1, b: 1},
						array: [1, 1, 1]
					},
					addEntity: function () {
						let mashType = null;
						switch (staticEntityObjects.type) {
							case "立方体":
								mashType = sceneHelper.mashGeometries.box;
								break;
							case "圆形":
								mashType = sceneHelper.mashGeometries.circle;
								break;
							case "圆锥":
								mashType = sceneHelper.mashGeometries.cone;
								break;
						}
						const mash = sceneHelper.createMash(mashType, staticEntityObjects.name);
						sceneHelper.updateMashMaterial(mash, staticEntityObjects.colorFormats.int);
						sceneHelper.updateMashScale(mash, staticEntityObjects.width, staticEntityObjects.height, staticEntityObjects.depth)
						sceneHelper.updateMashPosition(mash, staticEntityObjects.positionX, staticEntityObjects.positionY, staticEntityObjects.positionZ)
						staticGroup.add(mash);
						
						staticEntities[mash.name] = function () {
							currentSelected = mash;
							staticEntityObjects.name = currentSelected.name;
							staticEntityObjects.width = currentSelected.scale.x;
							staticEntityObjects.height = currentSelected.scale.y;
							staticEntityObjects.depth = currentSelected.scale.z;
							staticEntityObjects.positionX = currentSelected.position.x;
							staticEntityObjects.positionY = currentSelected.position.y;
							staticEntityObjects.positionZ = currentSelected.position.z;
							staticEntityObjects.colorFormats.int = currentSelected.material.color.getHex();
							
							sceneHelper.animateCamera(
								camera.position,
								{
									x: mash.position.x + 100,
									y: 100,
									z: mash.position.z + 100
								},
								controls.target,
								mash.position,
								camera,
								controls);
							
						};
						staticEntitiesList.add(staticEntities, mash.name).listen();
					},
					removeEntity: function () {
						if (currentSelected) {
							entityController.folders.forEach(function (folder) {
								if (folder._title === "静态实体") {
									folder.children.forEach(function (entity) {
										if (entity.property === currentSelected.name) {
											staticGroup.remove(currentSelected);
											entity.destroy();
										}
									})
								}
							})
						}
					},
					clearEntity: function () {
						entityController.folders.forEach(function (folder) {
							if (folder._title === "静态实体") {
								folder.children.forEach(function (entity) {
									let curEntity = staticGroup.getObjectByName(entity.property);
									staticGroup.remove(curEntity);
								})
								folder.destroy();
							}
						})
					},
					cancelSelected: function () {
						currentSelected = null;
					}
				}
				staticEntityList.add(staticEntityObjects, 'type', ["立方体", "圆形", "圆锥"]).name("物体形状").listen();
				staticEntityList.add(staticEntityObjects, 'name').name("物体名称").listen();
				staticEntityList.add(staticEntityObjects, 'width').name("宽").listen().onChange(mashScaleOnChanged);
				staticEntityList.add(staticEntityObjects, 'height').name("高").listen().onChange(mashScaleOnChanged);
				staticEntityList.add(staticEntityObjects, 'depth').name("长").listen().onChange(mashScaleOnChanged);
				staticEntityList.add(staticEntityObjects, 'positionX').name("x轴位置").listen().onChange(mashPositionOnChanged);
				staticEntityList.add(staticEntityObjects, 'positionY').name("y轴位置").listen().onChange(mashPositionOnChanged);
				staticEntityList.add(staticEntityObjects, 'positionZ').name("z轴位置").listen().onChange(mashPositionOnChanged);
				staticEntityList.addColor(staticEntityObjects.colorFormats, 'int').name("颜色").listen().onChange(mashMaterialOnChanged);
				staticEntityList.add(staticEntityObjects, 'addEntity').name("添加实体");
				staticEntityList.add(staticEntityObjects, 'removeEntity').name("删除实体");
				staticEntityList.add(staticEntityObjects, 'clearEntity').name("清空实体");
				staticEntityList.add(staticEntityObjects, 'cancelSelected').name("取消选中");
			}
			
			/**
			 * 添加动态实体控制器
			 * @param parent 父控制器
			 */
			function addDynamicEntityController(parent) {
				/**
				 * 动态实体控制器功能
				 */
				const dynamicEntityList = parent.addFolder("动态实体操作");
				const dynamicEntityObjs = {
					loadDynamicEntities: function () {
						dynamicEntityObjs.clearDynamicEntities();
						loadCoaches(
							"/api/server/coach/all",
							entityController,
							"终端列表",
							'../../model/cistern__eight-axle_oil_tank/scene.gltf');
					},
					clearDynamicEntities: function () {
						entityController.folders.forEach(function (folder) {
							if (folder._title === "终端列表") {
								folder.children.forEach(function (entity) {
									let curEntity = dynamicGroup.getObjectByName(entity.property);
									dynamicGroup.remove(curEntity);
								})
								folder.destroy();
							}
						})
					}
				};
				dynamicEntityList.add(dynamicEntityObjs, "loadDynamicEntities").name("加载终端列表");
				dynamicEntityList.add(dynamicEntityObjs, "clearDynamicEntities").name("清除终端列表");
			}
			
			/**
			 * 加载终端
			 * @param api 请求路径
			 * @param parent 父控制器
			 * @param listName 列表名称
			 * @param modelPath 模型路径
			 */
			function loadCoaches(api, parent, listName, modelPath) {
				request.get(api).then(res => {
					const coachList = parent.addFolder(listName);
					const coaches = {};
					gltfLoader = new GLTFLoader();
					res.forEach((item) => {
						const key = item.id;
						coaches[key] = function () {
							emitter.emit("receiveCoachId", key);
							let currentCoach = dynamicGroup.getObjectByName(key);
							new TWEEN.Tween(currentCoach.position)
							.to({
								x: (currentCoach.userData.toLon - 116.52616976895676) * 100000,
								y: 0,
								z: -(currentCoach.userData.toLat - 39.991732847489686) * 100000
							}, 15000)
							.start();
							
							sceneHelper.animateCamera(
								camera.position,
								{
									x: currentCoach.position.x + 100,
									y: 100,
									z: currentCoach.position.z + 100
								},
								controls.target,
								currentCoach.position,
								camera,
								controls);
							
						};
						coachList.add(coaches, key).name("终端 " + key);
						
						gltfLoader.load(modelPath, function (train) {
							train.scene.name = item.id;
							train.scene.userData.lon = parseFloat(item.currentCoord.split(",")[0]);
							train.scene.userData.lat = parseFloat(item.currentCoord.split(",")[1]);
							train.scene.userData.toLon = parseFloat(item.toCoord.split(",")[0]);
							train.scene.userData.toLat = parseFloat(item.toCoord.split(",")[1]);
							
							sceneHelper.updateMashScale(train.scene, 5, 5, 5);
							sceneHelper.updateMashPosition(train.scene,
								sceneHelper.lon2meter(train.scene.userData.lon, 116.52616976895676),
								0,
								-sceneHelper.lat2meter(train.scene.userData.lat, 39.991732847489686));
							dynamicGroup.add(train.scene);
						});
					})
				})
			}
			
			/**
			 * 添加场景控制器
			 * @param parent 父控制器
			 */
			function addSceneController(parent) {
				// 场景操作
				const sceneList = parent.addFolder("场景操作");
				const sceneObjs = {
					saveScene: function () {
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
								let data = this.result;
								if (data) {
									let loadedScene = JSON.parse(data);
									let loader = new THREE.ObjectLoader();
									scene = loader.parse(loadedScene);
								}
							};
							reader.readAsText(file);
							document.body.removeChild(input);
						});
					},
					clearScene: function () {
						scene = new THREE.Scene();
					}
				}
				sceneList.add(sceneObjs, "saveScene").name("保存场景");
				sceneList.add(sceneObjs, "loadScene").name("加载场景");
				sceneList.add(sceneObjs, "clearScene").name("清空场景");
			}
		}
	},
	mounted() {
		this.initScene();
		this.initSkybox();
		this.initCamera();
		this.initRender();
		this.initLight();
		this.initOrbitControl();
		this.initGroup();
		this.initRaycaster();
		this.updated();
		this.createController();
	}
}
</script>

<style scoped>
#containerScene {
    width: 100%;
    height: 900px;
}
</style>