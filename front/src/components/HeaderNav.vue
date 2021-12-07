<template>
	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<div class="container-fluid">
			<a class="navbar-brand" href="/">在途运输管理平台</a>
			<button aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"
					class="navbar-toggler"
					data-bs-target="#navbarSupportedContent" data-bs-toggle="collapse" type="button">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div id="navbarSupportedContent" class="collapse navbar-collapse">
				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
					<li class="nav-item">
						<a :class="home.class" href="/">首页</a>
					</li>
					<li class="nav-item">
						<router-link :class="terminalManagement.class" to="/terminal_management">终端管理</router-link>
					</li>
					<li class="nav-item">
						<router-link :class="transportScene.class" to="/transport_scene">三维场景</router-link>
					</li>
					<template v-if="isLogin">
						<li class="nav-item dropdown">
							<a :class="accountManagement.class" aria-expanded="false"
							   class="nav-link dropdown-toggle {'active':home.isCurrent}"
							   data-bs-toggle="dropdown" href="#"
							   role="button">
								{{ username }}
							</a>
							<ul aria-labelledby="navbarDropdown" class="dropdown-menu">
								<li>
									<router-link class="dropdown-item" to="/account_management">个人中心</router-link>
								</li>
								<li>
									<hr class="dropdown-divider">
								</li>
								<li>
									<router-link class="dropdown-item" to="/login" @click="logout()">注销</router-link>
								</li>
							</ul>
						</li>
					</template>
					<template v-else>
						<li class="nav-item">
							<router-link :class="terminalManagement.class" to="/login">{{ username }}</router-link>
						</li>
					</template>
				</ul>
			</div>
			<a class="navbar-brand"> {{ dateYear }} {{ dateWeek }} {{ dateDay }}</a>
		</div>
	</nav>
</template>

<script>
import storage from "@/assets/js/storage";

export default {
	name: "HeaderNav",
	data() {
		return {
			isLogin: false,
			username: "",
			home: {"class": "nav-link active"},
			terminalManagement: {"class": "nav-link active"},
			accountManagement: {"class": "nav-link dropdown-toggle active"},
			transportScene: {"class": "nav-link active"},
			dateDay: null,
			dateYear: null,
			dateWeek: null,
			weekday: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
			timer: null
			
		}
	},
	//在这里调用ajax请求方法
	created() {
		this.getUser();
		this.getUrl();
	},
	methods: {
		getUser: function () {
			let username = storage.getItem("username");
			if (username !== false) {
				this.username = username;
				this.isLogin = true;
			} else {
				this.username = "请登录";
				this.isLogin = false;
			}
		},
		getUrl: function () {
			let currentUrl = this.$route.path;
			this.terminalManagement.class = "nav-link";
			this.transportScene.class = "nav-link";
			this.accountManagement.class = "nav-link dropdown-toggle";
			this.home.class = "nav-link";
			if (currentUrl.endsWith("terminal_management")) {
				this.terminalManagement.class = "nav-link active";
			} else if (currentUrl.endsWith("transport_scene")) {
				this.transportScene.class = "nav-link active";
			} else if (currentUrl.endsWith("account_management")) {
				this.accountManagement.class = "nav-link dropdown-toggle active";
			} else {
				this.home.class = "nav-link active";
			}
		},
		logout: function () {
			storage.clear();
		}
	},
	mounted() {
		this.timer = setInterval(() => {
			const date = this.$dayjs(new Date())
			this.dateDay = date.format('HH:mm:ss');
			this.dateYear = date.format('YYYY-MM-DD');
			this.dateWeek = date.format(this.weekday[date.day()]);
			
		}, 1000)
	},
	beforeDestroy() {
		if (this.timer) {
			clearInterval(this.timer)
		}
	}
}
</script>

<style scoped>

</style>