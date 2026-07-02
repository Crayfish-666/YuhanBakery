# YuhanBakery (小龙虾烘焙坊)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.15-brightgreen.svg?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-blue.svg?style=flat-square&logo=openjdk)](https://oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-orange.svg?style=flat-square&logo=mysql)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg?style=flat-square&logo=redis)](https://redis.io/)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.0+-ff69b4.svg?style=flat-square&logo=rabbitmq)](https://www.rabbitmq.com/)
[![Vue 3](https://img.shields.io/badge/Vue-3.x-4fc08d.svg?style=flat-square&logo=vue.js)](https://vuejs.org/)
[![TailwindCSS](https://img.shields.io/badge/Tailwind-3.x-38bdf8.svg?style=flat-square&logo=tailwind-css)](https://tailwindcss.com/)
[![DeepSeek AI](https://img.shields.io/badge/DeepSeek-V3--Ark-12a5ff.svg?style=flat-square)](https://www.volcengine.com/)

YuhanBakery（小龙虾烘焙坊）是一个基于 **Spring Boot 3 + Java 21** 开发的现代化新零售 O2O 烘焙坊线上商城系统。它完整覆盖了 **“顾客线上选购下单、领券抵扣 -> 后台发货调度 -> 骑手抢单配送及结算提现”** 的核心业务闭环，并嵌入了基于 **DeepSeek 大模型** 的流式 AI 客服助理。

---

## 📖 目录

- [一、 项目背景与业务角色](#一-项目背景与业务角色)
- [二、 核心功能与架构亮点](#二-核心功能与架构亮点)
- [三、 系统技术栈](#三-系统技术栈)
- [四、 目录与结构说明](#四-目录与结构说明)
- [五、 本地快速启动](#五-本地快速启动)
- [六、 Docker 容器化部署](#六-docker-容器化部署)
- [七、 接口联调与 API 文档](#七-接口联调与-api-文档)
- [八、 核心技术方案设计](#八-核心技术方案设计)

---

## 一、 项目背景与业务角色

随着数字化转型的深化，线下烘焙门店亟需建立线上直销渠道以解决高峰排队、配送脱节、营销匮乏等痛点。YuhanBakery 打通了以下三个核心角色的协同运作：

```
                   ┌──────────────┐
                   │  顾客端 APP  │
                   └──────┬───────┘
                          │ 1. 下单支付 (领券抵扣/余额/微信支付)
                          ▼
 ┌──────────────┐  2. 订单调度  ┌──────────────┐
 │  后台管理端  │ ────────────► │  骑手端 APP  │
 └──────────────┘               └──────┬───────┘
                                       │ 3. 接单/取货/送达
                                       ▼
                                结算入账 & 提现
```

1. **顾客端 (Customer)**：提供流畅的移动端体验。支持商品分类浏览、自提/配送选择、优惠券智能抵扣、余额/模拟微信支付、订单实时状态跟踪、评价，以及火山引擎 DeepSeek 驱动的流式 AI 智能客服。
2. **管理端 (Admin)**：面向门店的运营中枢。负责分类和 SKU 规格的级联保存、套餐组合（Combo）上架、全站订单发货控制、营销优惠券配置发放、用户资产变更明细记录及可视化营业大盘。
3. **骑手端 (Rider)**：专为骑手打造的配送工具。采用极致个性的**暗黑机能风 (Neo-Brutalism)** 界面，适配户外高光可读性；配备**两段式防误触滑动按钮**控制接单流转；内置**真实配送费提现账户体系**，完成配送后运费直接入账。

---

## 二、 核心功能与架构亮点

*   **智能优惠券抵扣**：下单结算时，动态拉取可用优惠券，结合可用门槛（`minAmount`）与抵扣规则（满减、折扣、无门槛）计算最优方案。
*   **DeepSeek AI 客服助手**：集成了火山引擎大模型服务，采用 **SSE（Server-Sent Events）** 协议，实现流式的打字机回复交互，支持沉浸式导购和常见售后咨询。
*   **高并发秒杀库存扣减**：利用 **Redis + Lua 脚本** 实现商品 SKU 的原子库存预扣减，有效防超卖。
*   **订单超时自动取消**：基于 **RabbitMQ TTL 与死信队列 (DLX)** 实现未支付订单 15 分钟内自动取消，归还扣减的 Redis/MySQL 库存并原路退回优惠券。
*   **资金审计流水**：任何退款、钱包支付、充值、配送费入账和骑手提现均会写入 `user_balance_log` 进行强审计，确保账目清晰。

---

## 三、 系统技术栈

### 后端技术
*   **核心框架**：Spring Boot 3.5.15 & Spring MVC
*   **安全认证**：Spring Security + JWT (无状态分布式 Token 校验)
*   **持久层**：MyBatis-Plus 3.5.7 (结合逻辑删除及自动字段填充)
*   **基础数据库**：MySQL 8.x (Hikari 数据库连接池)
*   **缓存/并发**：Redis (Lettuce 客户端)
*   **消息队列**：RabbitMQ (手动 ACK 消息确认机制)
*   **三方服务**：阿里云 OSS (静态图片托管)、火山引擎大模型接口

### 前端技术
*   **开发框架**：Vue 3 (基于 Script Setup 及 Composition API)
*   **脚手架与构建**：Vite 5.x
*   **组件库**：Vant 4 (面向顾客和骑手的移动端)、Element Plus (面向管理员的 PC 端)
*   **样式库**：TailwindCSS (全局现代响应式布局)
*   **路由/状态**：Vue Router & Pinia
*   **网络通信**：Axios (结合统一全局响应/请求拦截器)

---

## 四、 目录与结构说明

### 1. 后端工程结构 (`/src`)
```text
src
├── main
│   ├── java/com/cyh/yuhanbakery
│   │   ├── common/         # 公共返回封装(Result/ResultCode)、全局异常捕捉、业务异常声明
│   │   ├── config/         # 各种基础设施配置(Redis, RabbitMQ, Swagger3, WebMvc等)
│   │   ├── controller/     # 接口暴露层 (Admin, Customer, Rider API)
│   │   ├── dto/            # 接口传参交互体 (SubmitOrderDTO, ProductSaveDTO 等)
│   │   ├── entity/         # MyBatis-Plus 数据库映射实体 (User, Orders, UserCoupon 等)
│   │   ├── mapper/         # MyBatis 映射器接口
│   │   ├── mq/             # 消息队列消费者监听器 (订单超时自动取消)
│   │   ├── scheduler/      # 定时任务 (兜底扫表取消未支付订单)
│   │   ├── security/       # Spring Security 过滤器链和 JWT 校验过滤器
│   │   ├── service/        # 核心业务逻辑层 (接口及其 impl 实现类)
│   │   └── utils/          # 各种工具类 (JwtUtils, OssUtils 等)
│   └── resources/
│       ├── db/             # 数据库结构与种子数据自动初始化脚本 (schema.sql / data.sql)
│       ├── lua/            # Redis 预扣库存 Lua 脚本
│       └── application.yaml# 主核心配置文件
```

### 2. 前端工程结构 (`/frontend`)
```text
frontend
├── src
│   ├── api/                # 全局 Axios 实例及各模块请求封装(request.js, mall.js, order.js)
│   ├── assets/             # 全局静态图片与样式
│   ├── components/         # 复用小组件 (如 AI 悬浮球组件 ChatBot.vue)
│   ├── layouts/            # 核心业务角色公共模版框架 (AdminLayout, CustomerLayout, RiderLayout)
│   ├── router/             # Vue Router 配置 (包含路由守卫及权限校验)
│   ├── stores/             # Pinia 用户认证和购物车状态管理
│   ├── views/              # 核心业务页面
│   │   ├── admin/          # 管理员页面：控制大盘、商品管理、套餐配置、订单调度
│   │   ├── customer/       # 顾客页面：首页(领券)、详情页、购物车、订单详情与支付、评价
│   │   ├── rider/          # 骑手页面：抢单大厅、执行中、收益记录、提现面板
│   │   └── Login.vue       # 统一登录与注册页面
│   └── App.vue             # 入口 Vue 组件
```

---

## 五、 本地快速启动

要手动运行本项目，请确保本地已配置好 **Java 21 (JDK 21+)**、**Node.js 18+**、**MySQL 8.x**、**Redis** 和 **RabbitMQ** 环境。

### 1. 数据库配置与自动载入
1. 本地启动 MySQL 实例，创建名为 `yuhan_bakery` 的数据库。
2. 后端采用 `spring.sql.init.mode: always` 选项。**当首次成功启动后端时，Spring Boot 会自动执行 `/src/main/resources/db/schema.sql` 和 `data.sql` 进行建表并注入种子数据。**

### 2. 修改后端配置
打开 `src/main/resources/application.yaml`，修改以下配置：
*   **MySQL 地址与凭证** (`spring.datasource`)
*   **Redis 地址及密码** (`spring.data.redis`)
*   **RabbitMQ 连接属性** (`spring.rabbitmq`)
*   **火山引擎 AI 大模型 Key 与模型 ID** (`volcengine.ark.api-key`, `model-id`)

### 3. 构建并运行后端
在项目根目录下，执行 Maven 构建及运行：
```bash
# 编译并打包项目
./mvnw.cmd clean compile

# 启动 Spring Boot 后端主应用 (监听 8080 端口)
./mvnw.cmd spring-boot:run
```

### 4. 构建并启动前端
进入 `frontend` 目录，安装依赖并启动 Vite 热更新服务器：
```bash
cd frontend

# 安装项目依赖
npm install

# 启动本地开发服务 (默认监听 5173 端口)
npm run dev
```
启动后在浏览器中打开 `http://localhost:5173` 即可进入系统。

---

## 六、 Docker 容器化部署

项目提供了一整套容器化部署方案，通过 `docker-compose.yml` 可一键拉起包括基础设施和后端应用在内的所有服务。

### 1. 修改 Docker 配置
确保 `docker-compose.yml` 内的环境变量（如 MySQL、Redis 等）匹配您的偏好。

### 2. 构建并启动
在项目根目录下直接运行：
```bash
# 后台构建镜像并一键拉起所有容器
docker-compose up -d --build
```
该命令会自动下载并启动：
*   **MySQL 8.0 容器** (映射 3306 端口)
*   **Redis 容器** (映射 6379 端口)
*   **RabbitMQ 容器** (映射 5672 端口，15672 为管理后台)
*   **Spring Boot 应用容器** (映射 8080 端口)

---

## 七、 接口联调与 API 文档

项目配置了 SpringDoc OpenAPI (Swagger 3) 规范。后端启动后，可在浏览器访问：

*   **OpenAPI Swagger 控制台**：`http://localhost:8080/api/swagger-ui.html`

可在此控制台调试各项接口，如添加购物车、优惠券领取、骑手流转等。

---

## 八、 核心技术方案设计

### 1. 并发库存扣减与 Redis 预扣
订单创建前为防超卖，后端引入 Lua 脚本确保读取和更新库存的原子性：
```lua
-- keys: bakery:sku:stock:{skuId}
-- args: quantity
local stock = tonumber(redis.call('get', KEYS[1]))
local num = tonumber(ARGV[1])
if not stock then
    return -2 -- 代表库存未初始化成功
end
if stock < num then
    return -1 -- 库存不足
else
    redis.call('decrby', KEYS[1], num)
    return 1 -- 扣减成功
end
```

### 2. 延迟消息队列取消订单
订单创建后，将订单号投递至带 TTL 的消息队列。若在 15 分钟内未发生支付成功回调，死信队列消费者会自动接管执行逻辑：
*   更新 `orders` 状态为 `4-已取消`。
*   归还预扣减的 SKU 数据库库存及 Redis 缓存库存。
*   恢复使用的 `user_coupon` 关联记录状态为 `0-未使用`。
