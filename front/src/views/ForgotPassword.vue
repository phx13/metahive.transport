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
                    <input class="form-control mt-2" name="email" placeholder="邮箱" required=""
                           type="email">
                  </div>
                </div>
                <div class="col-lg-12">
                  <button class="btn btn-custom w-100" @click="sendCode()"> 发送验证码</button>
                </div>
                <div class="mx-auto mt-3">
                  <p class="mb-0"><small class="text-dark mr-2">我们将新密码发送至您的邮箱</small></p>
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
export default {
  name: "ForgotPassword",
  methods: {
    sendCode: function () {
      alert("验证码已发送");
      this.$router.push("/login");
    }
  }
}
</script>

<style scoped>

</style>