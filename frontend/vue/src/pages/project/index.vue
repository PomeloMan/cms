<template>
  <div>
    <app-header :breadcrumbs="breadcrumbs" :searchType="'simple'">
      <div slot="action-group">
        <a-button type="link">{{ $t('common.new') }}</a-button>
        <a-divider type="vertical" />
        <a-button type="link">Link</a-button>
        <a-divider type="vertical" />
      </div>
    </app-header>
    <div class="scroll-wrapper no-footer">
      <div class="content-wrapper">
        <div style="height:600px"></div>
      </div>
    </div>
  </div>
</template>

<script>
import BaseComponent from '@/components/base.component'
import AppHeader from '@/components/header'
import { Divider, Button } from 'ant-design-vue'

export default {
  extends: BaseComponent,
  components: {
    AppHeader: AppHeader,
    ADivider: Divider,
    AButton: Button
  }
}
</script>

<style lang="scss" scoped>
</style>