<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
  // computed: {
  //   key() {
  //     return this.$route.name !== undefined ? this.$route.name + +new Date() : this.$route + +new Date()
  //   }
  // }
}
</script>

<style>
.app-main {
  padding: 10px;
}

.common {
  margin-top: 40px;
}

.el-card {
  margin-bottom: 10px;
}
</style>
