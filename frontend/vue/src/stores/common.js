export default {
  namespaced: true,
  state: {
    collapsed: false, // 侧边栏展开/收缩
    breadcrumbs: [] // 面包屑导航栏
  },
  mutations: {
    updateCollapsed(state, collapsed) {
      state.collapsed = collapsed
    },
    updateBreadcrumbs(state, breadcrumbs) {
      state.breadcrumbs = breadcrumbs
    }
  },
  actions: {
    updateCollapsed(context, collapsed) {
      context.commit('updateCollapsed', collapsed)
    },
    updateBreadcrumbs(context, breadcrumbs) {
      context.commit('updateBreadcrumbs', breadcrumbs)
    }
  }
}
