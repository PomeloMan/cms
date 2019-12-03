<template>
  <div>
    <div class="logo" />
    <a-menu theme="dark" mode="inline" :defaultSelectedKeys="['1']">
      <template v-for="menu in menus">
        <a-menu-item
          :key="menu.id"
          v-if="!menu.children || menu.children.length === 0"
        >
          <router-link :to="menu.url">
            <a-icon :type="menu.icon" />
            <span>{{ menu.dname.value || menu.name }}</span>
          </router-link>
        </a-menu-item>
        <a-sub-menu
          :key="menu.id"
          v-if="menu.children && menu.children.length > 0"
        >
          <span slot="title">
            <a-icon :type="menu.icon" />
            <span>{{ menu.dname.value || menu.name }}</span>
          </span>
          <template v-for="child in menu.children">
            <a-menu-item :key="child.id">
              <router-link :to="child.url">{{ child.dname.value || child.name }}</router-link>
            </a-menu-item>
          </template>
        </a-sub-menu>
      </template>
    </a-menu>
  </div>
</template>

<script>
import API from '../configs/api'
import { Menu } from 'ant-design-vue'

export default {
  name: 'main-sider',
  components: {
    AMenu: Menu,
    AMenuItem: Menu.Item,
    ASubMenu: Menu.SubMenu
  },
  data() {
    return {
      menus: []
    }
  },
  created() {
    Promise.all([this.getMenus()]).then()
  },
  methods: {
    getMenus() {
      return this.$http.post(API.MENU_LIST).then(({ data }) => {
        this.menus = data
      })
    }
  }
}
</script>

<style scoped>
.logo {
  height: 32px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px;
}
</style>
