<template>
  <!-- Navigation Bar-->
  <section class="vh-100 d-flex align-items-center" style="background-image: url('../assets/images/bg-login.jpg');">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-md-6 col-lg-4">
          <div class="bg-white p-4 rounded box-shadow">
            <h5> 找回密码 </h5>
            <form class="login-form">
              <div class="row">
                <div class="col-lg-12 mt-2">
                  <div class="form-group">
                    <p class="mb-0"> 请输入您的邮箱 </p>
                      <input id="email" class="form-control mt-2" placeholder="邮箱" required=""
                             type="email">
                  </div>
                </div>
                  <div class="col-lg-12">
                      <button class="btn btn-custom w-100" @click="sendCode()"> 发送</button>
                  </div>
                  <div class="mx-auto mt-3">
                      <p class="mb-0"><small class="text-dark mr-2">我们将新密码发送至您的邮箱</small></p>
                  </div>
                  <div class="mx-auto">
                      <p class="mb-0 mt-2"><small class="text-dark mr-2"> 无需重置？ </small>
                          <router-link class="text-dark font-weight-bold" to="/login"> 去登录</router-link>
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

export default {
    name: "ForgotPassword",
    methods: {
        sendCode: async function () {
            let data = {
                "email": $("#email").val()
            }
            // let cancel = { exec: null }
            // const result = request.post("/api/server/user/forgot_password", emailAddress, {
            //   // CancelToken takes a func that has the cancel function for it's param
            //   // we'll set it to a prop on our cancel object that we passed in. Then we'll
            //   // call this function where/when we need to cancel this request
            //   cancelToken: new CancelToken(c => (cancel.exec = c)),
            // }).then(res => {
            //   // alert(res);
            //   console.log(res);
            //   // this.$router.push("/login");
            // })
            //
            // setTimeout(() => {
            //   if (!result.ok) {
            //     // if the timeout fires, and our results didn't come back
            //     // then we'll call the cancel func set by CancelToken
            //     cancel.exec()
            //   }
            // }, 5000)

            const res = await request.post("/api/server/user/forgot_password", data);
            alert(res["result"]);

        }
    }
}
</script>

<style scoped>

</style>