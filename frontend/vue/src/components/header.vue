<template>
  <div
    class="app-header"
    :style="{
      height: breadcrumbs && breadcrumbs.length > 0 ? '40px' : '24px'
    }"
  >
    <a-breadcrumb>
      <template v-for="item in breadcrumbs">
        <a-breadcrumb-item :key="item.name">
          <template v-if="!!item.url">
            <router-link
              v-if="item.url === '/'"
              :to="{ path: item.url, query: { t: Date.now() } }"
              >{{ $t(item.name) }}</router-link
            >
            <router-link v-else :to="item.url">{{ $t(item.name) }}</router-link>
          </template>
          <template v-else>
            {{ $t(item.name) }}
          </template>
        </a-breadcrumb-item>
      </template>
    </a-breadcrumb>

    <slot name="action-group"></slot>
    <template v-if="searchType === 'advanced'">
      <div>
        <a-button
          type="link"
          class="fs-13"
          @click="isAdvanceWrapperVisible = !isAdvanceWrapperVisible"
        >
          高级搜索
          <a-icon type="down" />
        </a-button>
      </div>

      <div
        class="advance-wrapper"
        :class="{ visible: isAdvanceWrapperVisible }"
      ></div>
    </template>
    <template v-if="searchType === 'simple'">
      <div>
        <a-input-search
          size="small"
          style="width: 200px"
          v-model="keyword"
          @search="onSearch"
        />
      </div>
    </template>
    <template v-else>
      <div><!-- none --></div>
    </template>
  </div>
</template>

<script>
import { Breadcrumb, Button, Input } from 'ant-design-vue'

export default {
  name: 'app-header',
  components: {
    ABreadcrumb: Breadcrumb,
    ABreadcrumbItem: Breadcrumb.Item,
    AButton: Button,
    AInputSearch: Input.Search
  },
  props: {
    breadcrumbs: {
      type: Array,
      default: function() {
        return []
      }
    },
    searchType: {
      type: String, // 'simple' | 'advanced' | 'none'
      default: function() {
        return 'none'
      }
    }
  },
  data() {
    return {
      isAdvanceWrapperVisible: false,
      keyword: undefined
    }
  },
  methods: {
    onSearch() {
      this.$emit('onSearch', this.keyword)
    },
    onAdvancedSearch() {}
  }
}
</script>

<style lang="scss" scoped>
.app-header {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;

  > *:nth-child(1),
  > *:nth-child(3) {
    flex: 1;
  }
  > *:nth-child(1) {
    text-align: left;
  }
  > *:nth-child(3) {
    text-align: right;
    margin-right: 4px;
  }
}
.advance-wrapper {
  position: absolute;
  top: 40px;
  width: 100%;
  height: 0;
  left: 0;
  padding: 0 16px;
  z-index: 1;
  background: white;
  overflow: hidden;
  transition: all 0.3s;

  &.visible {
    height: 200px;
    border-bottom: 1px solid #e8e8e8;
  }
}
</style>