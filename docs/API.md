# 接口文档

基础路径：`/api`（如 http://localhost:8080/api）

除登录、验证码、公告列表/详情外，其余接口需在请求头携带：`Authorization: Bearer <token>`

统一响应格式：

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {},
  "timestamp": 1711888888888
}
```

状态码：200 成功，400 参数错误，401 未授权，403 权限不足，500 服务器错误。

---

## 一、认证（学生/管理员通用）

### 1. 获取验证码

- **URL**: `/auth/captcha`
- **方法**: GET
- **响应**: `{ "captchaKey": "xxx", "captchaCode": "1234" }`

### 2. 登录

- **URL**: `/auth/login`
- **方法**: POST
- **请求体**:  
  `{ "studentId": "学号/工号", "password": "密码", "captcha": "验证码", "captchaKey": "验证码key" }`
- **响应**:  
  `{ "token", "userId", "username", "realName", "userType", "permissions" }`
- **限流**: 单 IP 每分钟最多 20 次

### 3. 登出

- **URL**: `/auth/logout`
- **方法**: POST
- **请求头**: Authorization

---

## 二、公告

### 1. 公告列表（学生端/公开）

- **URL**: `/announcement/list`
- **方法**: GET
- **参数**: pageNum, pageSize, title（可选）
- **响应**: `{ "total", "list": [ { "id", "title", "content", "publisherName", "publishTime", "isTop" } ] }`

### 2. 公告详情

- **URL**: `/announcement/detail/{id}`
- **方法**: GET

### 3. 管理端-公告分页

- **URL**: `/announcement/admin/page`
- **方法**: GET
- **参数**: pageNum, pageSize, title, status

### 4. 管理端-新增公告

- **URL**: `/announcement/admin/add`
- **方法**: POST
- **请求体**: `{ "title", "content", "publisherId", "publisherName", "publishTime", "status", "isTop" }`

### 5. 管理端-编辑公告

- **URL**: `/announcement/admin/update`
- **方法**: PUT
- **请求体**: 同实体，含 id

### 6. 管理端-删除公告（仅草稿）

- **URL**: `/announcement/admin/delete/{id}`
- **方法**: DELETE

---

## 三、报修

### 1. 学生端-提交报修

- **URL**: `/repair/submit`
- **方法**: POST
- **请求体**: `{ "repairType", "roomNo", "description", "images", "phone" }`
- **限流**: 单 IP 每分钟最多 20 次

### 2. 学生端-撤销报修（仅待处理）

- **URL**: `/repair/cancel/{id}`
- **方法**: PUT

### 3. 学生端-我的报修列表

- **URL**: `/repair/my/list`
- **方法**: GET
- **参数**: pageNum, pageSize, status, repairType, startTime, endTime

### 4. 报修详情

- **URL**: `/repair/detail/{id}`
- **方法**: GET

### 5. 学生端-评价（仅已完成）

- **URL**: `/repair/evaluate`
- **方法**: POST
- **请求体**: `{ "repairId", "score", "content" }`

### 6. 管理端-报修分页

- **URL**: `/repair/admin/page`
- **方法**: GET
- **参数**: pageNum, pageSize, status, repairType, buildingNo, handlerId, startTime, endTime

### 7. 管理端-分配处理人

- **URL**: `/repair/admin/assign`
- **方法**: PUT
- **请求体**: `{ "id", "handlerId", "handlerName" }`

### 8. 管理端-处理/完成

- **URL**: `/repair/admin/handle`
- **方法**: PUT
- **请求体**: `{ "id", "handleRemark", "handleImages", "complete" }`

### 9. 管理端-按类型/楼栋统计

- **URL**: `/repair/admin/stats/type`、`/repair/admin/stats/building`
- **方法**: GET
- **参数**: startTime, endTime

---

## 四、入住登记

### 1. 学生端-提交入住登记

- **URL**: `/checkin/submit`
- **方法**: POST
- **请求体**: CheckIn 实体字段（学号姓名等可从当前用户带出，可传 phone, roomDisplay, checkInTime 等）

### 2. 学生端-我的登记列表

- **URL**: `/checkin/my/list`
- **方法**: GET

### 3. 管理端-分页与审核

- **URL**: `/checkin/admin/page` GET，参数：pageNum, pageSize, status, college  
- **URL**: `/checkin/admin/audit` PUT，请求体：`{ "id", "pass", "rejectReason", "bedId" }`

---

## 五、留言

### 1. 学生端-发起留言

- **URL**: `/message/add`
- **方法**: POST
- **请求体**: `{ "title", "content", "phone" }`

### 2. 学生端-我的留言列表

- **URL**: `/message/my/list`
- **方法**: GET

### 3. 学生端-标记已读

- **URL**: `/message/read/{id}`
- **方法**: PUT

### 4. 管理端-分页与回复

- **URL**: `/message/admin/page` GET，参数：pageNum, pageSize, status  
- **URL**: `/message/admin/reply` PUT，请求体：`{ "id", "replyContent" }`

---

## 六、宿舍申请

### 1. 学生端-提交申请

- **URL**: `/dorm-application/submit`
- **方法**: POST
- **请求体**: `{ "applicationType", "originalRoom", "targetRoom", "reason", "attachment" }`，理由≥20字

### 2. 学生端-我的申请列表

- **URL**: `/dorm-application/my/list`
- **方法**: GET

### 3. 管理端-分页与审核

- **URL**: `/dorm-application/admin/page` GET  
- **URL**: `/dorm-application/admin/audit` PUT，请求体：`{ "id", "pass", "rejectReason" }`

---

## 七、文件上传

- **URL**: `/upload/image`
- **方法**: POST
- **Content-Type**: multipart/form-data
- **参数**: file（文件）, dir（可选，默认 repair）
- **限制**: 单文件 ≤2MB，格式 jpg/png
- **响应**: `data` 为访问路径（如 `/uploads/repair/xxx.jpg`）
