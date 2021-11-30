<template>
  <header-nav/>
  <section class="vh-100 d-flex align-items-center" style="background-image: url('../assets/images/bg-login.jpg');">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
          <div class="bg-white p-4 rounded box-shadow">
            <form>
              <div class="mb-3">
                <label class="form-label" for="exampleInputUser1">用户名</label>
                <input id="exampleInputUser1" class="form-control" type="text">
              </div>
              <div class="mb-3">
                <label class="form-label" for="exampleInputEmail1">邮箱</label>
                <input id="exampleInputEmail1" aria-describedby="emailHelp" class="form-control" type="email">
              </div>
              <div class="mb-3">
                <label class="form-label" for="exampleInputPassword1">密码</label>
                <input id="exampleInputPassword1" class="form-control" type="password">
              </div>
              <div class="mb-3">
                <label class="form-label" for="exampleInputPassword2">确认密码</label>
                <input id="exampleInputPassword2" class="form-control" type="password">
              </div>
              <button class="btn btn-custom w-100" type="submit">更新</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import HeaderNav from "@/components/HeaderNav";

export default {
  name: "AccountManagement",
  components: {HeaderNav}
}
</script>

<style scoped>

</style>