import Login from './views/Login.vue'
import NotFound from './views/404.vue'
import Home from './views/Home.vue'
import Main from './views/Main.vue'
import Table from './views/nav1/Table.vue'
import Form from './views/nav1/Form.vue'
import user from './views/nav1/user.vue'
import Page4 from './views/nav2/Page4.vue'
import Page5 from './views/nav2/Page5.vue'
import Page6 from './views/nav3/Page6.vue'
import echarts from './views/charts/echarts.vue'

//=============导入组件===========
import noPermission from './views/403.vue'
import Department from "./views/org/Department";
import Permission from "./views/auth/Permission";
import Role from "./views/auth/Role";
import Employee from "./views/org/Employee";
import Menu from "./views/auth/Menu";
import config from "./views/sys/config";
import dictionary from "./views/sys/dictionary";
import dictionaryItem from "./views/sys/dictionaryItem";
import ProductType from "./views/prod/ProductType";
import Product from "./views/prod/Product";
import BusinessActivity from "./views/mkt/BusinessActivity";
import Clue from "./views/mkt/Clue";
import Order from "./views/order/Order";
import Contract from "./views/order/Contract";
import Business from "./views/mkt/Business";
let routes = [
    {
        path: '/login',
        component: Login,
        name: '',
        hidden: true
    },
    {
        path: '/404',
        component: NotFound,
        name: '',
        hidden: true
    },
    {
        path: '/',
        component: Home,
        name: '首页',
        iconCls: 'fa fa-bar-chart',
        leaf: true, // 表明当前是一个叶子节点,它的意思就是:当前只有这一个节点,没有儿子
        children: [
            { path: '/echarts', component: echarts, name: '首页' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '用户信息',
        iconCls: 'el-icon-message',//图标样式class
        children: [
            { path: '/department', component: Department, name: '用户管理'},
            { path: '/employee', component: Employee, name: '用户地址' },
            { path: '/employee', component: Employee, name: '成长记录' },
            { path: '/employee', component: Employee, name: '用户详情' },
            { path: '/employee', component: Employee, name: '用户真实信息' },
            { path: '/employee', component: Employee, name: '员工管理' },

        ]
    },
    {
        path: '/',
        component: Home,
        name: '权限管理',
        iconCls: 'el-icon-message',//图标样式class
        children: [
            { path: '/permission', component: Permission, name: '权限列表'},
            { path: '/menu', component: Menu, name: '菜单管理' },
            { path: '/role', component: Role, name: '角色列表'},
        ]
    },

    {
        path: '/',
        component: Home,
        name: '产品管理',
        iconCls: 'el-icon-message',//图标样式class
        children: [
            { path: '/productType', component: ProductType, name: '产品类型' },
            { path: '/product', component: Product, name: '产品列表' },
            { path: '/form', component: Form, name: 'Form' },
            { path: '/user', component: user, name: '列表' },
        ]
    },
    {
        path: '/',
        component: Home,
        name: '营销模块',
        iconCls: 'fa fa-id-card-o',
        children: [
            { path: '/businessActivity', component: BusinessActivity, name: '市场活动' },
            { path: '/clue', component: Clue, name: '线索管理' },
            {path:'/business',component: Business,name:'商机'},
        ]
    },
    {
        path: '/',
        component: Home,
        name: '订单模块',
        iconCls: 'fa fa-id-card-o',
        children: [
            { path: '/order', component: Order, name: '订单管理' },
            { path: '/contract', component: Contract, name: '合同管理' }
        ]
    },


    {
        path: '*',
        hidden: true,
        redirect: { path: '/404' }
    },
    {
        path: '/',
        component: Home,
        name: '',
        hidden: true,
        children: [
            { path: '/403', component: noPermission, name: '' }
        ]
    }
];

export default routes;