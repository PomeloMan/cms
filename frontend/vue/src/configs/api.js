import Mock_API from './api_mock'

const API = {
  // User
  USER_PAGE: '/cms/user/page',
  USER_LIST: '/cms/user/list',
  USER_SAVE: '/cms/user/save',
  USER_DELETE: '/cms/user/delete',
  USER_UPLOAD: '/cms/user/upload',
  USER_DOWNLOAD: '/cms/user/download',
  USER_TEMPLATE: '/cms/template/UserTemplate.xlsx',

  // Role
  ROLE_PAGE: '/cms/role/page',
  ROLE_LIST: '/cms/role/list',
  ROLE_SAVE: '/cms/role/save',
  ROLE_DELETE: '/cms/role/delete',
  ROLE_UPLOAD: '/cms/role/upload',
  ROLE_DOWNLOAD: '/cms/role/download',
  ROLE_TEMPLATE: '/cms/template/RoleTemplate.xlsx',

  // Auth
  AUTH_PAGE: '/cms/auth/page',
  AUTH_LIST: '/cms/auth/list',
  AUTH_SAVE: '/cms/auth/save',
  AUTH_DELETE: '/cms/auth/delete',
  AUTH_UPLOAD: '/cms/auth/upload',
  AUTH_DOWNLOAD: '/cms/auth/download',
  AUTH_TEMPLATE: '/cms/template/AuthTemplate.xlsx',

  // Menu
  MENU_LIST: '/cms/menu/list',
  MENU_SAVE: '/cms/menu/save',
  MENU_DELETE: '/cms/menu/delete',
  MENU_UPLOAD: '/cms/menu/upload',
  MENU_DOWNLOAD: '/cms/menu/download',
  MENU_TEMPLATE: '/cms/template/MenuTemplate.xlsx'
}

let api
const mode = process.env.VUE_APP_CURRENT_MODE
if (mode === 'mock') {
  api = Mock_API
} else {
  api = API
}

export default api