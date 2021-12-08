import {GUI} from "three/examples/jsm/libs/lil-gui.module.min";
import * as THREE from "three";

let sceneHelper = (function () {

	/**
	 * 创建一个空的三维场景
	 * @returns {Scene} 空的场景
	 */
	function createScene() {
		return new THREE.Scene();
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
	 * 创建天空盒
	 * @param materialArray 材质数组
	 * @param width 天空盒的宽
	 * @param height 天空盒的高
	 * @param depth 天空盒的长
	 * @param positionX x轴位置
	 * @param positionY y轴位置
	 * @param positionZ z轴位置
	 * @returns {Mesh} 天空盒实体
	 */
	function createSkybox(materialArray, width, height, depth, positionX, positionY, positionZ) {
		const skyboxGeo = new THREE.BoxGeometry(1, 1, 1);
		const skybox = new THREE.Mesh(skyboxGeo, materialArray);
		skybox.name = "skybox";
		skybox.scale.set(width, height, depth);
		skybox.position.set(positionX, positionY, positionZ);
		return skybox;
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
		 * 天空盒类型
		 * @type {{sand: {basePath: string, imagesPrefix: string, imageType: string}, sun: {basePath: string, imagesPrefix: string, imageType: string}}}
		 */
		const typeDic = {
			"sun": {
				"basePath": "../../skybox/",
				"imagesPrefix": "Daylight Box",
				"imageType": ".bmp"
			},
			"sand": {
				"basePath": "../../skybox - 副本/",
				"imagesPrefix": "Daylight Box",
				"imageType": ".bmp"
			},
		}

		/**
		 * 更新天空盒材质
		 */
		function updateSkyboxMaterial() {
			let basePath = "";
			let imagesPrefix = "";
			let imageType = "";
			switch (skyboxObjects.type) {
				case '阳光风格':
					basePath = typeDic["sun"]["basePath"];
					imagesPrefix = typeDic["sun"]["imagesPrefix"];
					imageType = typeDic["sun"]["imageType"];
					break;
				case '沙漠风格':
					basePath = typeDic["sand"]["basePath"];
					imagesPrefix = typeDic["sand"]["imagesPrefix"];
					imageType = typeDic["sand"]["imageType"];
					break;
			}
			skybox.material = loadSkyboxMaterial(basePath, imagesPrefix, imageType);
		}

		/**
		 * 更新天空盒大小
		 */
		function updateSkyboxScale() {
			skybox.scale.set(skyboxObjects.width, skyboxObjects.height, skyboxObjects.depth);
		}

		/**
		 * 更新天空盒位置
		 */
		function updateSkyboxPosition() {
			skybox.position.set(skyboxObjects.positionX, skyboxObjects.positionY, skyboxObjects.positionZ);
		}

		/**
		 * 天空盒控制器功能
		 */
		const skyboxList = parent.addFolder("天空盒");
		const skyboxObjects = {
			type: "阳光风格",
			width: 5000,
			height: 5000,
			depth: 5000,
			positionX: 0,
			positionY: 2500,
			positionZ: 0
		}
		skyboxList.add(skyboxObjects, 'type', ["阳光风格", "沙漠风格", "阴雨风格"]).name("风格切换").onChange(updateSkyboxMaterial);
		skyboxList.add(skyboxObjects, 'width').name("宽").onChange(updateSkyboxScale);
		skyboxList.add(skyboxObjects, 'height').name("高").onChange(updateSkyboxScale);
		skyboxList.add(skyboxObjects, 'depth').name("长").onChange(updateSkyboxScale);
		skyboxList.add(skyboxObjects, 'positionX').name("x轴位置").onChange(updateSkyboxPosition);
		skyboxList.add(skyboxObjects, 'positionY').name("y轴位置").onChange(updateSkyboxPosition);
		skyboxList.add(skyboxObjects, 'positionZ').name("z轴位置").onChange(updateSkyboxPosition);
	}

	return {
		createScene,
		loadSkyboxMaterial,
		createSkybox,
		createGlobalController,
		addSkyboxController
	}
})();

export default sceneHelper;