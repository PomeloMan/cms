<template>
  <div class="main-header">
    <div class="left-wrapper">
      <a-icon
        class="trigger"
        :type="collapsed ? 'menu-unfold' : 'menu-fold'"
        @click="collapse"
      />
    </div>
    <div class="right-wrapper">
      <a-dropdown class="" :trigger="['click']">
        <li class="ant-menu-item ant-dropdown-link" href="#">
          语言 <a-icon type="down" />
        </li>
        <a-menu slot="overlay">
          <a-menu-item key="0" @click="i18n('zh')">中文</a-menu-item>
          <a-menu-item key="1" @click="i18n('en')">英文</a-menu-item>
        </a-menu>
      </a-dropdown>
    </div>
  </div>
</template>

<script>
import { Menu, Dropdown } from 'ant-design-vue'
import i18n from '@/constants/i18n'

export default {
  name: 'main-header',
  components: {
    AMenu: Menu,
    AMenuItem: Menu.Item,
    ADropdown: Dropdown
  },
  data() {
    return {
      locales: i18n.locales
    }
  },
  computed: {
    collapsed: {
      get() {
        return this.$store.state.common.collapsed
      },
      set(val) {
        this.$store.dispatch('common/updateCollapsed', val)
      }
    }
  },
  methods: {
    collapse() {
      this.collapsed = !this.collapsed
    },
    i18n(locale) {
      this.$i18n.locale = locale
    }
  },
  created() {
    // debugger
  }
}
</script>

<style lang="scss" scoped>
.main-header {
  display: flex;
  align-items: center;
  justify-content: space-between;

  .right-wrapper {
    padding: 0 24px;
  }
}
.trigger {
  font-size: 18px;
  line-height: 64px;
  padding: 0 24px;
  cursor: pointer;
  transition: color 0.3s;

  &:hover {
    color: #1890ff;
  }
}
</style>
