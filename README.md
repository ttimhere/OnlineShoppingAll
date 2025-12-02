# 电子商务网站的设计与实现 (OnlineShopping)

**学号：** [202330452292]  
**姓名：** [张诗汀]

---

## 1. 项目概述

本项目设计并实现了一个基于 Java 的电子商务网站。系统分为前台顾客系统和后台管理系统。前台提供商品浏览、购物车、订单支付及个人中心功能；后台提供商品管理、订单管理、销售报表统计及客户行为日志审计功能。

### 1.1 开发环境

* **操作系统:** Windows
* **开发工具:** IntelliJ IDEA 2024.3.5、Visual Studio Code
* **后端框架:** Spring Boot 2.7.5, MyBatis
* **前端框架:** Vue 3, Pinia, Element Plus
* **数据库:** MySQL 
* **其他工具:** Maven, Docker, Nginx

## 2. 系统架构与代码结构

### 2.1 后端代码结构 (Spring Boot)

遵循分层解耦原则，主要模块划分如下：

```text
src/main/java/com/online_shopping_project/onlineshopping
├── common          // 通用工具类与统一响应
├── Util          // JWT工具类
├── config          // 配置类
├── controller      // 控制层 (API接口)
├── service         // 业务逻辑层接口
├── mapper          // MyBatis DAO层接口
└── entity          // 数据库实体类 & VO/DTO
```

### 2.2 数据库设计 (MySQL)

数据库名为 `online_shop`，主要包含以下数据表：

* **用户相关:** `user` (用户表), `user_address` (地址表)
* **商品相关:** `category` (类目表), `product` (商品表)
* **交易相关:** `cart_item` (购物车), `order` (订单主表), `order_item` (订单明细), `payment` (支付记录), `stock_flow` (库存流水)
* **系统/后台:** `admin_user` (管理员), `role`, `menu`, `admin_user_role`（管理员与角色关联表）,`role_menu`（角色与菜单权限关联表）

## 3. 功能模块与关键代码说明

### 3.1 用户模块 (Customer)
实现了用户的注册、登录、个人中心及地址管理。

* **后端文件:**
    * `UserController.java`: 提供注册、登录、获取个人信息、获取当前登录用户的订单列表、查看某一订单详情、用户确认收货接口。
    * `UserService.java`/`UserServiceImpl.java`: 实现用户注册、登录的业务逻辑和根据ID查询用户的功能。
    * `UserAddressController.java` / `UserAddressService.java` / `UserAddressServiceImpl.java`: 实现收货地址的增删改查及设为默认。
    * `UserMapper.java`/ `UserAddressMapper.java`: 用户数据访问和用户地址相关数据层操作。
    * `JwtAuthFilter.java`/ `JwtUtil.java`: 拦截请求解析 Token，实现无状态认证。
* **前端文件:**
    * `src/pages/Login.vue`: 用户登录/注册页面，表单校验。
    * `src/store/userStore.js`: 使用 Pinia 持久化存储用户信息和 Token。
    * `src/pages/Profile.vue` & `src/pages/ProfileAddress.vue`: 个人中心及地址管理页面。

### 3.2 商品展示与搜索模块
支持商品类目树展示、分页查询。

* **后端文件:**
    * `CategoryController.java`: 提供 `/api/category/tree` 接口，递归构建类目树。
    * `ProductController.java`: 提供商品查询相关接口。
    * `CategoryService.java`/ `CategoryServiceImpl.java`：实现类目树结构的构建逻辑。
    * `ProductService.java`/ `ProductServiceImpl.java`：实现商品展示、添加商品图片、获取商品详情的业务逻辑。
    * `CategoryMapper.java`：实现查询启用状态的类目信息及分类层级关系。
    * `ProductImageMapper.java`/ `ProductMapper.java`：与其对应xml文件实现与商品图片相关数据层操作，以及查询商品、后台管理商品相关数据层操作。
* **前端文件:**
    * `src/pages/Home.vue`: 首页，整合左侧分类栏与右侧商品展示。
    * `src/components/NavBar.vue`: 顶部导航栏组件。

### 3.3 购物车与订单模块
核心交易流程，包括加购、修改数量、结算、支付模拟及邮件通知。

* **后端文件:**
    * `CartController.java`/ `CartService.java` / `CartServiceImpl.java`: 处理购物车增删改查，并实现 `checkout` 逻辑（生成订单 -> 扣减库存 -> 记录支付 -> 发送邮件）。
    * `OrderService.java`/`OrderServiceImpl.java`: 实现订单查询与确认收货。
    * `CartItemMapper.java`: 与其对应xml文件实现购物车操作相关的数据层操作。
    * `ProductMapperExt.java`: 扩展接口，实现扣减库存。
    * `OrderMapper.java`: 实现记录订单以及查询订单，并处理订单状态流转。
    * `OrderItemMapper.java`: 插入订单明细以及查询订单详情的数据层操作。
    * `PaymentMapper.java`:查询与插入支付记录的数据层操作。
    * 
* **前端文件:**
    * `src/pages/Cart.vue`: 购物车主页，实现全选、计算总价、结算。
    * `src/components/CartItem.vue`: 单个购物车商品组件。
    * `src/pages/ProfileOrders.vue`/ `src/pages/ProfileOrderDetail.vue`: 用户订单历史查看与“确认收货”功能。

### 3.4 后台管理模块 (Admin)
供管理员（Super Admin）和客服（Service）使用。

#### 3.4.1 权限登录与商品管理
* **后端文件:**
    * `AdminController.java`: 后台登录和商品管理接口。
    * `AdminUserService.java`/`AdminUserServiceImpl.java`：实现管理员登陆与商品管理的业务逻辑。
    * `AdminUserMapper.java` / `RoleMapper.java`/ `MenuMapper.java`: 关联查询管理员角色与权限，用于后台权限校验。后台商品管理对应代码在ProductMapper.java中。
* **前端文件:**
    * `src/admin/pages/AdminLayout.vue`：后台整体布局页面。
    * `src/admin/pages/AdminLogin.vue`：管理员登陆页面实现。
    * `src/admin/pages/ProductManage.vue` / `ProductEdit.vue`: 商品列表展示与商品管理页面。

#### 3.4.2 订单管理
* **后端文件:**
    * `AdminOrderController.java`: 提供订单查询、发货、取消、更改地址接口。
    * `AdminOrderService.java`/`AdminOrderServiceImpl.java`：查询订单、发货、取消订单、修改订单地址的业务逻辑实现。
    * `AdminOrderMapper.java`/`AdminOrderItemMapper.java`：后台订单管理相关的数据层操作。
    * `AdminPaymentMapper.java`：根据订单号查询支付记录的数据层操作。
    * `AdminStockFlowMapper.java`: 订单取消时回滚库存并记录流水。
* **前端文件:**
    * `src/admin/pages/OrderManage.vue`/`src/admin/pages/OrderDetail.vue`: 订单列表展示和订单管理、订单详情页面。

#### 3.4.3 销售报表
* **后端文件:**
    * `AdminReportController.java`：提供销售报表查询相关接口。
    * `AdminReportService.java`/`AdminReportServiceImpl.java`：销售报表统计数据的业务逻辑实现。
    * `AdminReportMapper.java`：后台统计报表相关数据层操作。
* **前端文件:**
    * `src/admin/pages/SalesReport.vue`: 使用 ECharts 展示销售统计图表。

#### 3.4.4 客户与日志管理
* **后端文件:**
    * `AdminCustomerController.java`: 分页查询客户列表及行为日志接口。
    * `AdminCustomerService.java`/`AdminCustomerServiceImpl.java`：查询客户列表与用户行为日志的业务逻辑实现。
    * `UserEventService.java`/`UserEventServiceImpl.java`: 记录用户前台行为（浏览、下单等）。
    * `AdminCustomerMapper.java`/`AdminUserEventMapper.java`：查询后台客户列表与用户行为日志的数据层操作。
    * `UserEventWriteMapper.java`：记录用户行为的数据层操作。
* **前端文件:**
    * `src/admin/pages/UserEventLog.vue`/`src/admin/pages/UserEventLog.vue`: 展示用户列表与用户行为日志。

