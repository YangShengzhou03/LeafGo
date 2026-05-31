# LeafGO（职达）

<div align="center">

[![GitHub Stars](https://img.shields.io/github/stars/YangShengzhou03/LeafGo?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/LeafGo)&nbsp;[![GitHub Forks](https://img.shields.io/github/forks/YangShengzhou03/LeafGo?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/LeafGo)&nbsp;[![GitHub Last Commit](https://img.shields.io/github/last-commit/YangShengzhou03/LeafGo?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/LeafGo)&nbsp;[![Vue.js](https://img.shields.io/badge/Vue.js-3.4.0-42b883?style=for-the-badge&logo=vuedotjs)](https://vuejs.org/)&nbsp;[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.0-6DB33F?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)&nbsp;[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql)](https://www.mysql.com/)

**一个现代化的智能招聘平台，采用前后端分离架构**

</div>

## 功能特性

### 求职者功能
- 智能职位搜索与推荐
- 在线简历管理与编辑
- 职位收藏与投递
- 实时消息沟通
- 投递记录跟踪

### 招聘者功能
- 职位发布与管理
- 候选人筛选与管理
- 企业信息展示
- 简历查看与处理
- 实时消息沟通

### 管理员功能
- 用户管理
- 企业管理
- 职位管理
- 数据统计分析

## 测试账号

### 求职者账号
- 手机号：`13397062330`
- 验证码：`123456`

### 招聘者账号
- 手机号：`13397062331`
- 验证码：`123456`

### 管理员账号
- 手机号：`13397062332`
- 验证码：`123456`

## 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/YangShengzhou03/LeafGo.git
cd LeafGo
```

### 2. 数据库初始化

```bash
mysql -u root -p < data.sql
```

### 3. 启动后端

```bash
cd backend
mvn clean package
java -jar target/leafgo-backend-1.0.0.jar
```

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
```

### 5. 访问地址

- 前端：http://localhost:8080
- 后端API：http://localhost:8081

## Docker部署

```bash
docker-compose up -d
```

## 技术栈

- 前端：Vue 3 + TypeScript + Element Plus + Pinia
- 后端：Spring Boot 3 + Spring Security + JWT
- 数据库：MySQL 8.0
- 部署：Docker + Nginx

## 许可证

[MIT License](LICENSE)
