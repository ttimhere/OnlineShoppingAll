import AdminLogin from '../pages/AdminLogin.vue'
import AdminLayout from '../pages/AdminLayout.vue'
import ProductManage from '../pages/ProductManage.vue'
import OrderManage from '../pages/OrderManage.vue'
import OrderDetail from '../pages/OrderDetail.vue'
import SalesReport from '../pages/SalesReport.vue'
import CustomerList from '../pages/CustomerList.vue'
import UserEventLog from '../pages/UserEventLog.vue'
const adminRoutes = [
    { path: '/admin/login', component: AdminLogin },//管理员登录路由
    {
        path: '/admin', //后台布局主路由
        component: AdminLayout,
        children: [ //各后台模块的子路由
            { path: 'product', component: ProductManage }, //商品管理
            { path: 'order', component: OrderManage }, //订单管理
            { //订单详情
                path: '/admin/order/:id',
                name: 'OrderDetail',
                component: () => import('../pages/OrderDetail.vue'),
                meta: { title: '订单详情' }
            },
            { //销售报表
                path: '/admin/report',
                name: 'SalesReport',
                component: () => import('../pages/SalesReport.vue'),
                meta: {
                    title: '销售报表'
                }
            },
            { path: 'customer', component: CustomerList }, //客户管理
            {
                path: '/admin/customer/events',
                name: 'UserEventLog',
                component: () => import('../pages/UserEventLog.vue'),
                meta: { hidden: true }  // 不在菜单显示
            }
        ]
    }
]
export default adminRoutes
