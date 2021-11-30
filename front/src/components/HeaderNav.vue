<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
      <a class="navbar-brand" href="/">在途运输管理平台</a>
      <button aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation" class="navbar-toggler"
              data-bs-target="#navbarSupportedContent" data-bs-toggle="collapse" type="button">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div id="navbarSupportedContent" class="collapse navbar-collapse">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <a :class="home.class" aria-current="page" href="/">首页</a>
          </li>
          <li class="nav-item">
            <router-link :class="terminalManagement.class" to="/terminal_management">终端管理</router-link>
          </li>
          <li class="nav-item dropdown">
            <a :class="accountManagement.class" aria-expanded="false" class="nav-link dropdown-toggle {'active':home.isCurrent}"
               data-bs-toggle="dropdown" href="#"
               role="button">
              当前用户
            </a>
            <ul aria-labelledby="navbarDropdown" class="dropdown-menu">
              <li>
                <router-link class="dropdown-item" to="/account_management">个人中心</router-link>
              </li>
              <li>
                <hr class="dropdown-divider">
              </li>
              <li>
                <router-link class="dropdown-item" to="/login">注销</router-link>
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import request from "@/assets/js/request";

export default {
  name: "HeaderNav",
  data() {
    return {
      user: "",
      home: {"class": "nav-link active"},
      terminalManagement: {"class": "nav-link active"},
      accountManagement: {"class": "nav-link dropdown-toggle active"},
    }
  },
  //在这里调用ajax请求方法
  created() {
    // this.getUser();
    this.getUrl();
  },
  mounted() {
    // this.getUrl();
  },
  methods: {
    getUser: function () {
      request.get("/api/server/user").then(res => {
        if (res) {
          this.user = res;
        }
      })
    },
    getUrl: function () {
      let currentUrl = this.$route.path;
      this.terminalManagement.class = "nav-link";
      this.accountManagement.class = "nav-link dropdown-toggle";
      this.home.class = "nav-link";
      if (currentUrl.endsWith("terminal_management")) {
        this.terminalManagement.class = "nav-link active";
      } else if (currentUrl.endsWith("account_management")) {
        this.accountManagement.class = "nav-link dropdown-toggle active";
      } else {
        this.home.class = "nav-link active";
      }
    }
  }
}
</script>

<style scoped>

</style>