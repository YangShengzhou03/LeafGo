# LeafGO（职达）

<div align="center">

![LeafGO Logo](frontend/src/assets/images/logo.png)

**连接人才与机遇，开启职业新篇章**

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Vue](https://img.shields.io/badge/Vue-3.4.0-brightgreen.svg)](https://vuejs.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)

[在线演示](#) | [文档](#) | [API文档](API_DOCUMENTATION.md) | [设计文档](DESIGN.md)

</div>

---

## 项目简介

**LeafGO（职达）** 是一个现代化的智能招聘平台，致力于为求职者和企业提供高效、便捷的招聘求职服务。平台采用前后端分离架构，提供完整的招聘求职解决方案。

### 核心定位

- **求职者**：智能职位推荐、在线简历管理、实时沟通
- **招聘者**：高效人才筛选、职位发布管理、企业品牌展示
- **平台管理**：数据统计分析、内容审核管理、用户行为分析

### 项目特色

- **现代化UI设计** - 提供极致用户体验
- **高性能架构** - 前后端分离，支持高并发访问
- **多端支持** - Web端支持
- **智能匹配** - 职位搜索与筛选
- **实时通讯** - 在线聊天、消息推送
- **安全可靠** - JWT认证、数据加密、权限控制

---

## 功能特性

### 求职者端功能

#### 职位搜索与发现
- **智能搜索**：关键词搜索、地图搜索、语音搜索
- **多维度筛选**：职位类型、薪资范围、工作经验、学历要求、公司行业
- **热门推荐**：基于用户画像的个性化职位推荐
- **职位收藏**：收藏感兴趣的职位，随时查看

#### 简历管理
- **在线简历**：可视化简历编辑器
- **多份简历**：支持创建多份简历应对不同职位
- **简历模板**：提供多种专业简历模板
- **简历导出**：支持PDF、Word格式导出
- **隐私设置**：简历公开/隐藏设置

#### 投递管理
- **一键投递**：快速投递简历
- **投递记录**：实时跟踪投递状态
- **状态提醒**：查看、面试、录用等状态通知
- **投递分析**：投递数据统计与分析

#### 即时通讯
- **在线聊天**：与HR实时沟通
- **消息提醒**：新消息实时推送
- **聊天记录**：历史消息查看
- **文件传输**：支持发送简历、图片等文件

#### 企业浏览
- **企业详情**：公司介绍、规模、融资情况
- **在招职位**：查看企业所有在招职位
- **企业认证**：识别认证企业，保障求职安全
- **公司评价**：查看员工评价和面试经验

### 招聘者端功能

#### 职位管理
- **发布职位**：快速发布职位信息
- **职位编辑**：随时修改职位详情
- **上下线管理**：灵活控制职位展示状态
- **批量操作**：批量发布、编辑、下线职位
- **职位模板**：常用职位模板快速发布

#### 候选人管理
- **简历接收**：实时接收求职者简历
- **简历筛选**：智能筛选匹配候选人
- **状态管理**：待查看、已查看、面试、录用、拒绝
- **人才库**：建立企业人才库
- **批量处理**：批量查看、批量操作

#### 企业展示
- **企业主页**：完善企业信息展示
- **品牌宣传**：上传公司Logo、宣传图片
- **企业认证**：完成企业认证提升可信度
- **数据统计**：职位浏览、投递数据分析

#### 数据分析
- **招聘数据**：职位发布、简历接收统计
- **效果分析**：招聘渠道效果分析
- **趋势分析**：招聘趋势可视化
- **报表导出**：数据报表导出功能

### 管理后台功能

#### 用户管理
- **用户列表**：查看所有注册用户
- **用户审核**：审核用户资质
- **状态管理**：启用/禁用用户账号
- **用户统计**：用户增长趋势分析

#### 企业管理
- **企业列表**：查看所有入驻企业
- **企业审核**：审核企业认证申请
- **企业分类**：按行业、规模分类管理
- **违规处理**：处理企业违规行为

#### 职位管理
- **职位审核**：审核发布的职位信息
- **违规处理**：处理违规职位
- **职位统计**：职位发布数据分析
- **热门职位**：推荐优质职位

#### 数据统计
- **平台数据**：用户、企业、职位、投递统计
- **趋势分析**：平台发展趋势分析
- **数据可视化**：图表展示关键指标
- **报表生成**：定期生成统计报表

---

## 技术栈

### 前端技术栈

- **框架**：Vue 3.4 + TypeScript 5.3
- **UI组件**：Element Plus 2.5
- **状态管理**：Pinia 2.1
- **路由**：Vue Router 4.2
- **HTTP客户端**：Axios 1.6
- **构建工具**：Vite 5.0
- **样式**：SCSS
- **工具库**：Day.js、NProgress

### 后端技术栈

- **语言**：Java 17
- **框架**：Spring Boot 3.2 + Spring Security + JWT
- **ORM**：Spring Data JPA
- **数据库**：MariaDB 10.11
- **缓存**：Redis 7
- **API文档**：Swagger/OpenAPI 3.0

### DevOps

- **容器化**：Docker + Docker Compose

---

## 项目结构

```
leaf-go/
├── backend/                    # 后端服务
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/leafgo/
│   │   │   │   ├── config/         # 配置类
│   │   │   │   ├── controller/     # 控制器
│   │   │   │   ├── service/        # 业务逻辑
│   │   │   │   ├── repository/     # 数据访问
│   │   │   │   ├── entity/         # 实体类
│   │   │   │   ├── dto/            # 数据传输对象
│   │   │   │   ├── security/       # 安全配置
│   │   │   │   ├── exception/      # 异常处理
│   │   │   │   └── util/           # 工具类
│   │   │   └── resources/          # 资源文件
│   │   └── test/                   # 测试代码
│   └── pom.xml                     # Maven配置
│
├── frontend/                   # 前端项目（Vue 3）
│   ├── src/
│   │   ├── api/                    # API接口
│   │   ├── assets/                 # 静态资源
│   │   ├── components/             # 公共组件
│   │   ├── composables/            # 组合式函数
│   │   ├── layouts/                # 布局组件
│   │   ├── router/                 # 路由配置
│   │   ├── store/                  # 状态管理
│   │   ├── types/                  # TypeScript类型
│   │   ├── utils/                  # 工具函数
│   │   └── views/                  # 页面组件
│   │       ├── seeker/             # 求职者端
│   │       ├── employer/           # 招聘者端
│   │       └── admin/              # 管理后台
│   ├── public/                     # 公共资源
│   ├── package.json                # 依赖配置
│   └── vite.config.ts              # Vite配置
│
├── data.sql                    # 数据库初始化脚本
├── docker-compose.yml          # Docker编排配置
├── API_DOCUMENTATION.md        # API文档
├── DESIGN.md                   # 设计文档
├── LICENSE                     # 许可证
└── README.md                   # 项目说明
```

---

## 快速开始

### 环境要求

- **Node.js** >= 18.0
- **Java** >= 17
- **Maven** >= 3.8
- **MariaDB** >= 10.11
- **Redis** >= 7.0
- **Docker** >= 20.0（可选）

### 安装步骤

#### 1. 克隆项目

```bash
git clone https://github.com/yourusername/leaf-go.git
cd leaf-go
```

#### 2. 数据库初始化

```bash
# 创建数据库
mysql -u root -p < data.sql

# 或使用Docker
docker-compose up -d mariadb
```

#### 3. 启动后端服务

```bash
cd backend

# 配置数据库连接
# 编辑 src/main/resources/application.yml

# 编译打包
mvn clean package

# 运行
java -jar target/leafgo-backend-1.0.0.jar
```

#### 4. 启动前端服务

```bash
cd frontend

# 安装依赖
npm install

# 开发模式运行
npm run dev

# 生产环境构建
npm run build
```

#### 5. 使用Docker Compose（推荐）

```bash
# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 停止服务
docker-compose down
```

### 访问地址

- **前端Web**：http://localhost:8080
- **后端API**：http://localhost:8081
- **API文档**：http://localhost:8081/swagger-ui.html
- **管理后台**：http://localhost:8080/admin

### 默认账号

#### 求职者账号
- 邮箱：`zhangsan@example.com`
- 密码：`password123`

#### 招聘者账号
- 邮箱：`wangwu@example.com`
- 密码：`password123`

#### 管理员账号
- 邮箱：`admin@example.com`
- 密码：`admin123`

---

## 功能模块

### 用户认证模块

- 用户注册（求职者/招聘者）
- 用户登录（邮箱/手机号）
- 密码找回
- 第三方登录（微信/QQ）
- JWT Token认证
- 权限控制

### 职位模块

- 职位发布
- 职位编辑
- 职位搜索
- 职位推荐
- 职位收藏
- 职位统计

### 简历模块

- 简历创建
- 简历编辑
- 简历预览
- 简历导出
- 多简历管理

### 投递模块

- 简历投递
- 投递记录
- 状态跟踪
- 投递统计

### 消息模块

- 即时通讯
- 消息推送
- 聊天记录
- 文件传输

### 企业模块

- 企业入驻
- 企业认证
- 企业展示
- 企业管理

---

## API文档

详细的API文档请查看 [API_DOCUMENTATION.md](API_DOCUMENTATION.md)

### 主要接口

#### 认证接口
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户登出

#### 职位接口
- `GET /api/jobs` - 获取职位列表
- `GET /api/jobs/{id}` - 获取职位详情
- `POST /api/jobs` - 发布职位（招聘者）
- `PUT /api/jobs/{id}` - 更新职位
- `DELETE /api/jobs/{id}` - 删除职位
- `GET /api/jobs/search` - 搜索职位
- `GET /api/jobs/my` - 获取我的职位

#### 用户接口
- `GET /api/users/{id}` - 获取用户信息
- `PUT /api/users/{id}` - 更新用户信息

#### 简历接口
- `GET /api/resumes/{userId}` - 获取简历
- `PUT /api/resumes/{userId}` - 更新简历

#### 投递接口
- `POST /api/applications` - 投递简历
- `GET /api/applications` - 获取投递记录
- `PUT /api/applications/{id}/status` - 更新投递状态

#### 消息接口
- `GET /api/messages/conversations` - 获取会话列表
- `POST /api/messages` - 发送消息

#### 企业接口
- `GET /api/companies` - 获取企业列表
- `GET /api/companies/{id}` - 获取企业详情
- `GET /api/companies/{id}/jobs` - 获取企业职位

#### 轮播图接口
- `GET /api/banners` - 获取轮播图列表

---

## 界面预览

### 求职者端

#### 首页
![求职者首页](docs/images/seeker-home.png)
- 职位搜索栏
- 热门职位分类
- 精选职位推荐
- 热门企业展示

#### 职位列表
![职位列表](docs/images/job-list.png)
- 多条件筛选
- 职位卡片展示
- 分页加载

#### 职位详情
![职位详情](docs/images/job-detail.png)
- 职位信息
- 公司介绍
- 一键投递
- 收藏功能

### 招聘者端

#### 管理首页
![招聘者首页](docs/images/employer-home.png)
- 数据统计
- 最新投递
- 快捷操作

#### 职位管理
![职位管理](docs/images/employer-jobs.png)
- 职位列表
- 状态管理
- 编辑删除

### 管理后台

#### 数据统计
![管理后台](docs/images/admin-home.png)
- 平台数据
- 趋势分析
- 图表展示

---

## 数据库设计

详细的数据库设计请查看 [DESIGN.md](DESIGN.md)

### 核心表结构

- `users` - 用户表
- `companies` - 企业表
- `jobs` - 职位表
- `resumes` - 简历表
- `applications` - 投递记录表
- `messages` - 消息表
- `conversations` - 会话表
- `banners` - 轮播图表

---

## 部署说明

### Docker部署

```bash
# 构建镜像
docker-compose build

# 启动服务
docker-compose up -d

# 查看日志
docker-compose logs -f
```

### 生产环境部署

#### 后端部署

```bash
# 打包
mvn clean package -Dmaven.test.skip=true

# 运行
nohup java -jar target/leafgo-backend-1.0.0.jar \
  --spring.profiles.active=prod \
  > app.log 2>&1 &
```

#### 前端部署

```bash
# 构建
npm run build

# 使用Nginx部署
cp -r dist/* /usr/share/nginx/html/
```

#### Nginx配置

```nginx
server {
    listen 80;
    server_name yourdomain.com;

    location / {
        root /usr/share/nginx/html;
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://localhost:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

---

## 开发指南

### 代码规范

- **Java**：遵循阿里巴巴Java开发手册
- **Vue**：遵循Vue官方风格指南
- **TypeScript**：使用ESLint + Prettier
- **Git提交**：遵循Conventional Commits规范

### 分支管理

- `master` - 生产环境分支
- `develop` - 开发环境分支
- `feature/*` - 功能开发分支
- `hotfix/*` - 紧急修复分支

### 开发流程

1. 从`develop`分支创建`feature`分支
2. 完成功能开发和测试
3. 提交Pull Request
4. 代码审查通过后合并
5. 定期发布到生产环境

---

## 贡献指南

我们欢迎所有形式的贡献，包括但不限于：

- 提交Bug报告
- 提交功能建议
- 提交代码改进
- 完善文档

### 提交Issue

在提交Issue之前，请确保：

1. 搜索已有的Issue，避免重复
2. 使用清晰的标题描述问题
3. 提供详细的复现步骤
4. 附上相关的日志或截图

### 提交Pull Request

1. Fork本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交Pull Request

---

## 许可证

本项目采用 [MIT License](LICENSE) 开源协议

---

## 联系方式

- **项目主页**：https://github.com/yourusername/leaf-go
- **问题反馈**：https://github.com/yourusername/leaf-go/issues
- **邮箱**：support@leafgo.com

---

## 致谢

感谢以下开源项目的支持：

- [Vue.js](https://vuejs.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [React Native](https://reactnative.dev/)
- [Element Plus](https://element-plus.org/)
- [MariaDB](https://mariadb.org/)

---

<div align="center">

**如果这个项目对你有帮助，请给一个Star支持一下**

Made by LeafGO Team

</div>
