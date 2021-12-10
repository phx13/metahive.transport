import {GUI} from "three/examples/jsm/libs/lil-gui.module.min";
import * as THREE from "three";
import {TWEEN} from "three/examples/jsm/libs/tween.module.min";

let sceneHelper = (function () {

	/**
	 * 创建一个空的三维场景
	 * @returns {Scene} 空的场景
	 */
	function createScene() {
		return new THREE.Scene();
	}

	/**
	 * 创建摄像机
	 * @param fov 摄像机视锥体垂直视野角度
	 * @param aspect 摄像机视锥体长宽比
	 * @param near 摄像机视锥体近端面
	 * @param far 摄像机视锥体远端面
	 * @returns {PerspectiveCamera} 摄像机实体
	 */
	function createCamera(fov, aspect, near, far) {
		return new THREE.PerspectiveCamera(fov, aspect, near, far);
	}

	/**
	 * 加载天空盒材质
	 * @param basePath 图片的根路径
	 * @param imagesPrefix 图片名称的前缀
	 * @param imageType 图片类型
	 * @returns {MeshBasicMaterial[]} 材质数组
	 */
	// skybox图片要统一imagePrefix+"Right", "Left", "Top", "Bottom", "Front", "Back"命名
	function loadSkyboxMaterial(basePath, imagesPrefix, imageType) {
		// 顺序为[right,left,up,down,front,back]
		const sides = ["Right", "Left", "Top", "Bottom", "Front", "Back"];
		const pathStings = sides.map(side => {
			return basePath + imagesPrefix + "_" + side + imageType;
		});

		return pathStings.map(image => {
			let texture = new THREE.TextureLoader().load(image);
			return new THREE.MeshBasicMaterial({map: texture, side: THREE.BackSide}); // <---
		});
	}

	/**
	 * 实体类型
	 * @type {{box: BoxGeometry, circle: CircleGeometry, cone: ConeGeometry}}
	 */
	const mashGeometries = {
		box: new THREE.BoxGeometry(1, 1, 1),
		circle: new THREE.CircleGeometry(5, 32),
		cone: new THREE.ConeGeometry(5, 20, 32)
	}

	/**
	 * 创建几何实体
	 * @param mashGeometry 几何体geometry
	 * @param name 实体名字
	 * @returns {Mesh} 实体
	 */
	function createMash(mashGeometry, name) {
		const mash = new THREE.Mesh(mashGeometry);
		mash.name = name;
		return mash;
	}

	/**
	 * 更新纯色物体材质
	 * @param mash 物体
	 * @param colorString 颜色
	 */
	function updateMashMaterial(mash, colorString) {
		mash.material = new THREE.MeshBasicMaterial({color: colorString});
	}

	/**
	 * 更新天空盒材质
	 * @param skybox 天空盒
	 * @param skyboxType 材质类型
	 */
	function updateSkyboxMaterial(skybox, skyboxType) {
		let basePath = skyboxType["basePath"];
		let imagesPrefix = skyboxType["imagesPrefix"];
		let imageType = skyboxType["imageType"];
		skybox.material = loadSkyboxMaterial(basePath, imagesPrefix, imageType);
	}

	/**
	 * 天空盒类型
	 * @type {{sand: {basePath: string, imagesPrefix: string, imageType: string}, sun: {basePath: string, imagesPrefix: string, imageType: string}}}
	 */
	const skyboxTypes = {
		sun: {
			"basePath": "../../skybox/",
			"imagesPrefix": "Daylight Box",
			"imageType": ".bmp"
		},
		sand: {
			"basePath": "../../skybox - 副本/",
			"imagesPrefix": "Daylight Box",
			"imageType": ".bmp"
		},
	}

	/**
	 * 更新物体大小
	 * @param mash 物体
	 * @param width 宽
	 * @param height 高
	 * @param depth 长
	 */
	function updateMashScale(mash, width, height, depth) {
		mash.scale.set(width, height, depth);
	}

	/**
	 * 更新物体位置
	 * @param mash 物体
	 * @param positionX x轴位置
	 * @param positionY y轴位置
	 * @param positionZ z轴位置
	 */
	function updateMashPosition(mash, positionX, positionY, positionZ) {
		mash.position.set(positionX, positionY, positionZ);
	}

	/**
	 * 创建全局控制器
	 * @param container 所属容器
	 * @param title 控制器标题
	 * @param top 上边距
	 * @param left 左边距
	 * @returns {g} 控制器实体
	 */
	function createGlobalController(container, title, top, left) {
		const globalController = new GUI({container: container, title: title});
		globalController.domElement.style.position = "absolute";
		globalController.domElement.style.top = top;
		globalController.domElement.style.left = left;
		return globalController;
	}

	/**
	 * 添加天空盒控制器
	 * @param scene 当前场景
	 * @param parent 父控制器
	 */
	function addSkyboxController(scene, parent) {
		/**
		 * 当前场景中的天空盒
		 */
		const skybox = scene.getObjectByName("skybox");

		/**
		 * 天空盒材质改变事件
		 */
		function skyboxMaterialOnChanged() {
			let skyboxType = {};
			switch (skyboxObjects.type) {
				case "阳光":
					skyboxType = skyboxTypes.sun
					break;
				case "沙漠":
					skyboxType = skyboxTypes.sand
					break;
			}
			updateSkyboxMaterial(skybox, skyboxType);
		}

		/**
		 * 天空盒大小改变事件
		 */
		function skyboxScaleOnChanged() {
			updateMashScale(skybox, skyboxObjects.width, skyboxObjects.height, skyboxObjects.depth);
		}

		/**
		 * 天空盒位置改变事件
		 */
		function skyboxPositionOnChanged() {
			updateMashPosition(skybox, skyboxObjects.positionX, skyboxObjects.positionY, skyboxObjects.positionZ);
		}

		/**
		 * 天空盒控制器功能
		 */
		const skyboxList = parent.addFolder("天空盒");
		const skyboxObjects = {
			type: "阳光",
			width: 5000,
			height: 5000,
			depth: 5000,
			positionX: 0,
			positionY: 2500,
			positionZ: 0
		}
		skyboxList.add(skyboxObjects, 'type', ["阳光", "沙漠", "阴雨"]).name("风格切换").onChange(skyboxMaterialOnChanged);
		skyboxList.add(skyboxObjects, 'width').name("宽").onChange(skyboxScaleOnChanged);
		skyboxList.add(skyboxObjects, 'height').name("高").onChange(skyboxScaleOnChanged);
		skyboxList.add(skyboxObjects, 'depth').name("长").onChange(skyboxScaleOnChanged);
		skyboxList.add(skyboxObjects, 'positionX').name("x轴位置").onChange(skyboxPositionOnChanged);
		skyboxList.add(skyboxObjects, 'positionY').name("y轴位置").onChange(skyboxPositionOnChanged);
		skyboxList.add(skyboxObjects, 'positionZ').name("z轴位置").onChange(skyboxPositionOnChanged);
	}




	/**
	 * 动态移动相机
	 * @param current1 相机当前位置
	 * @param target1 目标当前位置
	 * @param current2 相机结束位置
	 * @param target2 目标结束位置
	 * @param camera 相机实体
	 * @param controls 控制器实体
	 */
	function animateCamera(current1, target1, current2, target2, camera, controls) {
		const tween = new TWEEN.Tween({
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

	function lon2meter(lon, centerLon) {
		return (lon - centerLon) * 100000
	}


	function lat2meter(lat, centerLat) {
		return (lat - centerLat) * 100000
	}

	return {
		createScene,
		createMash,
		skyboxTypes,
		mashGeometries,
		updateSkyboxMaterial,
		updateMashMaterial,
		updateMashScale,
		updateMashPosition,
		createGlobalController,
		addSkyboxController,
		animateCamera,
		lon2meter,
		lat2meter
	}
})();

export default sceneHelper;