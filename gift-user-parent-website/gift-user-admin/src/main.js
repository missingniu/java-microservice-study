import babelpolyfill from 'babel-polyfill'
import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
//import './assets/theme/theme-green/index.css'
import VueRouter from 'vue-router'
import store from './vuex/store'
import Vuex from 'vuex'
//import NProgress from 'nprogress'
//import 'nprogress/nprogress.css'
import routes from './routes'
// import Mock from './mock'
// Mock.bootstrap();
import 'font-awesome/css/font-awesome.min.css'
// 引入axios,因为后续我们需要通过axios调用后端接口
import axios from "axios"
import {Message} from "element-ui"
// 在main.js中给axios配置一个全局的统一的前缀
axios.defaults.baseURL = "http://127.0.0.1:8080/"
//axios.defaults.baseURL = "http://47.109.93.239:8080/"
//在main.js中
//Vue.prototype.$hostURL='http://localhost:8080'

Vue.prototype.$http = axios;  // 将axios交给vue,通过vue来进行使用
Vue.use(ElementUI)
Vue.use(VueRouter)
Vue.use(Vuex)

//NProgress.configure({ showSpinner: false });

const router = new VueRouter({
  routes
})

// router.beforeEach((to, from, next) => {
//   //NProgress.start();
// //NProgress.start();
//   if (to.path === '/login') {
//     sessionStorage.removeItem('user');
//   }
//   let user = JSON.parse(sessionStorage.getItem('user'));
//   if (!user && to.path !== '/login') {
//     next({ path: '/login' })
//   } else {
//     next()
//   }
// })

//router.afterEach(transition => {
//NProgress.done();
//});
// axios请求拦截器:添加请求拦截器，在请求头中加token
// 前端调用后端接口,都是通过axios发起请求的,所以在axios调用后端接口之前,都会先经过axios的前置拦截器
axios.interceptors.request.use(
    config => {
      // 从localStorage中获取token
      let token = localStorage.getItem("token");

      // 如果token有值,我们就放到请求头里面
      if (token) {
        config.headers.token = token;
      }
      return config
    },
    error => {
      return Promise.reject(error+'请登录后使用')
    })
// 响应拦截器:后端响应结果给前端时,都要先经过响应拦截器
axios.interceptors.response.use(function(response){
  //对返回的数据进行操作
  let result = response.data;  // response.data 就是后端返给我们的数据  {success:false,message:"noLogin"}


    // result=JSON.parse(result)
  if(!result.success && result.message == "noLogin"){ // 说明未登录,被拦截了,那么就要跳回到登陆页面
      // alert("请登录后再次访问!")
      response.data.message="Please login!!!"
    router.push({ path: '/login' });  // 跳转回登陆页面,让用户登陆
      Message({message:"Please login!!!",type:"error"})
    return Promise.resolve(response);
  }else if(!result.success && result.message == "noPermission"){
      router.push({path:'/403'});
      return response
  }else{
    // 如果登录过的话,就继续往下运行
    return response;
  }
},function(err){
  return Promise.reject(err)
})


new Vue({
  //el: '#app',
  //template: '<App/>',
  router,
  store,
  //components: { App }
  render: h => h(App)
}).$mount('#app')

