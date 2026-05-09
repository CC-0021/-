# 公寓管理系统

前后端分离架构：Vue2 + ElementUI 前端，SpringBoot + MyBatis + Redis + JWT 后端。

## 技术栈

- **后端**: JDK 21、Spring Boot 3.2、MyBatis、MySQL 8.0、Redis 6+、JWT、BCrypt
- **前端**: Vue 2.7、Vue Router、Vuex、ElementUI、Axios、ECharts、xlsx

## 目录结构

```
SUSHE/
├── db/                    # 数据库脚本
│   ├── schema.sql         # 建表语句
│   └── init_data.sql      # 初始化数据（含 admin/123456、2024001/123456）
├── dorm-manage-backend/   # 后端工程
└── dorm-manage-frontend/  # 前端工程
```

## 快速启动

### 1. 数据库

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE dorm_manage DEFAULT CHARACTER SET utf8mb4;"

# 执行脚本
mysql -u root -p dorm_manage < db/schema.sql
mysql -u root -p dorm_manage < db/init_data.sql
```

### 2. Redis

确保 Redis 已启动（默认 localhost:6379，无密码）。

### 3. 后端

```bash
cd dorm-manage-backend
# 修改 application.yml 中数据库密码（默认 root/123456）
mvn clean package -DskipTests
java -jar target/dorm-manage-1.0.0.jar
```

后端地址：http://localhost:8080/api

### 4. 前端

```bash
cd dorm-manage-frontend
npm install
npm run serve
```

前端地址：http://localhost:9527（开发时代理 /api 到后端）

### 5. 登录账号

- 管理员：`admin` / `123456`
- 学生：`2024001` / `123456`

## 生产部署

- **后端**：`java -jar dorm-manage-1.0.0.jar`，可配合 systemd 或 Docker。
- **前端**：`npm run build` 后，将 `dist/` 部署到 Nginx，并配置反向代理 `/api` 到后端。
- 详见 `docs/DEPLOY.md` 与 `docs/API.md`。

## 安全说明

- 密码使用 BCrypt 存储，禁止明文。
- 接口鉴权：JWT + Redis，token 有效期 2 小时。
- 登录、报修提交接口：单 IP 每分钟限流 20 次。
- 跨域：仅允许配置的前端域名（见 application.yml 中 dorm.cors）。
