import axios from 'axios'
import router from './router'

const http = axios.create({
  baseURL: process.env.VUE_APP_BASE_URL,
  timeout: 1000 * 30,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json; charset=utf-8',
    'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJwcmluY2lwYWwiOnsidXNlcm5hbWUiOiJhZG1pbmlzdHJhdG9yIiwiYXV0aG9yaXRpZXMiOlt7InJvbGUiOiJST0xFIn0seyJyb2xlIjoiQVVUSCJ9LHsicm9sZSI6Ik1FTlUifSx7InJvbGUiOiJVU0VSIn1dLCJ2ZXJzaW9uIjowLjAsInN0YXR1cyI6IlZhbGlkIn0sInN1YiI6ImFkbWluaXN0cmF0b3IiLCJleHAiOjE1NzU1Mjk1OTksImlhdCI6MTU3NDkyNDc5OX0.BTINSZSgIG0IlSqul31iWlyXa1O0nfrVo2tyVaXJZd1Mig6F03k8UGz4BpqKAjrMbLyWm_-oXWS0iRMT4mePcA'
  }
})

http.baseURL = process.env.VUE_APP_BASE_URL;

/**
 * 请求拦截
 */
http.interceptors.request.use(config => {
  // config.headers['token'] = Vue.cookie.get('token')
  let token = localStorage.getItem('oauth2AccessToken')

  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`
  }
  return config
}, error => {
  return Promise.reject(error)
})

/**
 * 响应拦截
 */
http.interceptors.response.use(response => {
  if (response.data && response.data.code === 401) {
    router.push({ name: 'login' })
  }
  return response
}, error => {
  return Promise.reject(error)
})

export default http