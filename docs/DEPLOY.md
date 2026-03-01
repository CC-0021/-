# 部署说明

## 一、环境要求

- JDK 21
- MySQL 8.0
- Redis 6.0+
- Node.js 14+（仅前端构建需要）
- Nginx（前端生产部署）

## 二、数据库部署

1. 创建数据库：

```sql
CREATE DATABASE dorm_manage DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行脚本（按顺序）：

```bash
mysql -u root -p dorm_manage < db/schema.sql
mysql -u root -p dorm_manage < db/init_data.sql
```

3. 初始化数据说明：
   - 超级管理员：`admin` / `123456`
   - 测试学生：`2024001` / `123456`
   - 角色与权限、示例宿舍/床位、示例公告已写入

## 三、后端部署

1. 修改配置（如需要）：

编辑 `dorm-manage-backend/src/main/resources/application.yml`：
- `spring.datasource.url/username/password`：数据库连接
- `spring.data.redis.host/port/password`：Redis
- `dorm.cors.allowed-origins`：前端访问域名，多个用逗号分隔

2. 编译打包：

```bash
cd dorm-manage-backend
mvn clean package -DskipTests
```

3. 运行：

```bash
java -jar target/dorm-manage-1.0.0.jar
```

或指定配置：

```bash
java -Dspring.datasource.password=你的密码 -jar target/dorm-manage-1.0.0.jar
```

4. 可选：使用 systemd（Linux）：

创建 `/etc/systemd/system/dorm-manage.service`：

```ini
[Unit]
Description=Dorm Manage Backend
After=network.target mysql.service redis.service

[Service]
Type=simple
User=app
WorkingDirectory=/opt/dorm-manage
ExecStart=/usr/bin/java -jar /opt/dorm-manage/dorm-manage-1.0.0.jar
Restart=on-failure

[Install]
WantedBy=multi-user.target
```

## 四、前端部署

1. 构建：

```bash
cd dorm-manage-frontend
npm install
npm run build
```

产物在 `dist/` 目录。

2. Nginx 配置示例：

将 `dist/` 内容放到站点根目录，并代理 `/api` 到后端：

```nginx
server {
    listen 80;
    server_name your-domain.com;
    root /var/www/dorm-manage-frontend/dist;
    index index.html;
    location / {
        try_files $uri $uri/ /index.html;
    }
    location /api/ {
        proxy_pass http://127.0.0.1:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
    location /uploads/ {
        alias /opt/dorm-manage/uploads/;
    }
}
```

3. 若前端与后端不同域，需在后端 `application.yml` 的 `dorm.cors.allowed-origins` 中加入前端实际访问域名。

## 五、上传目录

后端默认上传路径：`./uploads`（相对运行目录）。  
生产环境建议改为绝对路径，并在 Nginx 中做静态映射（见上 `location /uploads/`）。

## 六、性能与并发

- 后端 Tomcat 最大线程数已设为 500（`server.tomcat.threads.max`），支持并发用户数 ≥500。
- 数据库连接池：HikariCP，maximum-pool-size=50，可按压测调整。
- 公告列表、报修查询已做索引与缓存配置，响应时间目标：公告 ≤100ms，报修查询 ≤200ms。
