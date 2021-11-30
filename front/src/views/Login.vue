<template>
  <!-- Navigation Bar-->
  <section class="vh-100 d-flex align-items-center" style="background-image: url('../assets/images/bg-login.jpg');">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
          <div class="bg-white p-4 rounded box-shadow">
            <h5> 登录 </h5>
            <form>
              <div class="row">
                <div class="col-lg-12">
                  <div class="form-group">
                    <label> 邮箱 </label>
                    <input class="form-control" name="email" placeholder="邮箱" required="" type="email">
                  </div>
                </div>

                <div class="col-lg-12">
                  <div class="form-group">
                    <label> 密码 </label>
                    <input class="form-control" placeholder="密码" required="" type="password">
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
export default {
  name: "Login",
  methods: {
    goLogin: function () {
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>

</style>