<template>
  <div id="app">
    <router-view :key="$route.query.t"></router-view>
  </div>
</template>

<script>
export default {
  name: 'app',
}
</script>

<style scope>
#app {
  /* font-family: 'Avenir', Helvetica, Arial, sans-serif; */
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  height: 100%;
}
</style>
