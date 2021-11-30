<template>
  <!-- Navigation Bar-->
  <section class="vh-100 d-flex align-items-center" style="background-image: url('../assets/images/bg-login.jpg');">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
          <div class="bg-white p-4 rounded box-shadow">
            <h5> 注册 </h5>
            <form class="login-form">
              <div class="row">
                <div class="col-lg-12">
                  <div class="form-group">
                    <label class=""> 用户名 </label>
                    <input class="form-control" name="s" placeholder="用户名" required="" type="text">
                  </div>
                </div>
                <div class="col-lg-12">
                  <div class="form-group">
                    <label class=""> 邮箱 </label>
                    <input class="form-control" name="email" placeholder="邮箱" required="" type="email">
                  </div>
                </div>
                <div class="col-lg-12">
                  <div class="form-group">
                    <label class=""> 密码 </label>
                    <input class="form-control" placeholder="密码" required="" type="password">
                  </div>
                </div>
                <div class="col-lg-12">
                  <div class="form-group">
                    <label class=""> 确认密码 </label>
                    <input class="form-control" placeholder="确认密码" required="" type="password">
                  </div>
                </div>
                <div class="col-lg-12 mt-4 mb-3">
                  <button class="btn btn-custom w-100" @click="goRegister()"> 注册</button>
                </div>
                <div class="mx-auto">
                  <p class="mb-0 mt-2"><small class="text-dark mr-2"> 已有账户? </small>
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
export default {
  name: "Register",
  methods: {
    goRegister: function () {
      this.$router.push('/login');
    }
  }
}
</script>

<style scoped>

</style>