<template>
  <div>
    <app-header :breadcrumbs="breadcrumbs"></app-header>
    <div class="scroll-wrapper no-footer">
      <div class="content-wrapper"></div>
    </div>
  </div>
</template>

<script>
import BaseComponent from '@/components/base.component'
import AppHeader from '@/components/header'

export default {
  name: 'dashboard',
  extends: BaseComponent,
  components: {
    AppHeader: AppHeader
  },
  data() {
    return {}
  },
  created() {},
  mounted() {}
}
</script>

<style scoped>
</style>
