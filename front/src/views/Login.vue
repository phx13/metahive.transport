<template>
    <div class="back-to-home rounded d-none d-sm-block">
        <router-link class="text-white rounded d-inline-block text-center" to="/"><i class="fa-solid fa-house"></i>
        </router-link>
    </div>

    <!-- Navigation Bar-->
    <section class="vh-100 d-flex align-items-center" style="background-image: url('../../images/bg-login.jpg');">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-6 col-lg-5">
                    <div class="bg-white p-4 rounded box-shadow">
                        <h5> 登录 </h5>
                        <form>
                            <div class="row">
                <div class="col-lg-12">
                    <div class="form-group">
                        <label> 用户名 </label>
                        <input id="usernameVal" class="form-control" placeholder="用户名" required="" type="email">
                    </div>
                </div>

                <div class="col-lg-12">
                  <div class="form-group">
                    <label> 密码 </label>
                      <input id="passwordVal" class="form-control" placeholder="密码" required="" type="password">
                  </div>
                </div>

                <div class="col-lg-12 mt-3">
                  <button class="btn btn-custom w-100" @click="goLogin()"> 登录</button>
                </div>
                <div class="mx-auto">
                  <p class="mb-0 mt-3">
                    <router-link class="text-dark font-weight-bold" to="/forgot_password"> 忘记密码？</router-link>
                  </p>
                </div>
                <div class="mx-auto">
                  <p class="mb-0 mt-3"><small class="text-dark mr-2"> 新用户？ </small>
                    <router-link class="text-dark font-weight-bold" to="/register"> 注册</router-link>
                  </p>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </section>
  <!-- HOME END-->
</template>

<script>
import request from "@/assets/js/request";
import $ from "jquery";
import storage from "@/assets/js/storage";

export default {
    name: "Login",
    methods: {
        goLogin: function () {
            let loginUser = {
                "name": $("#usernameVal").val(),
                "password": $("#passwordVal").val()
            }
            request.post("/api/server/user/login", loginUser).then(res => {
                alert(res["result"]);
                if (res["result"] === "登录成功") {
                    storage.setItem({value: res["tokenHead"] + res["token"], name: 'token', expires: 604800000});//存储token  过期时间是毫秒   我这里是一周
                    storage.setItem({value: res["username"], name: 'username', expires: 604800000});//存储token  过期时间是毫秒   我这里是一周
                    storage.setItem({value: res["id"], name: 'id', expires: 604800000});//存储token  过期时间是毫秒   我这里是一周
                    this.$router.push("/");
                } else {
                    this.$router.go(0);
                }
            })
        }
  }
}
</script>

<style scoped>

</style>