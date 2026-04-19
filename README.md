# mymongo

## 项目简介

`mymongo` 是一个基于 Spring Boot 3.5.6 的后端项目，当前主要围绕两类数据能力展开：

- **MongoDB 文档数据管理**：对知识库下的对象信息进行新增、修改、删除、分页查询。
- **Nebula Graph 图谱关系管理**：维护定量库知识标签之间的点边关系，并输出图结构展示数据。

从代码结构来看，这是一个典型的分层式 Java 后端服务，已经具备 Controller、Service、配置、异常处理、统一返回结构等基础设施，适合作为文档数据库与图数据库联合使用的业务基础工程。

---

## 技术栈

基于 `pom.xml` 当前可确认的技术栈如下：

- Java 17
- Spring Boot 3.5.6
- Spring Web
- Spring Validation
- Spring Data MongoDB
- Spring Data Elasticsearch
- MyBatis Plus 3.5.6
- MySQL Connector/J 8.0.33
- Druid 1.2.23
- Hutool 5.8.20
- Nebula Graph Java Client 3.8.4
- Lombok
- Maven / Maven Wrapper

---

## 项目结构

```text
mymongo/
├── .mvn/                                  # Maven Wrapper 配置
├── mvnw                                   # Maven Wrapper 启动脚本（Unix）
├── mvnw.cmd                               # Maven Wrapper 启动脚本（Windows）
├── pom.xml                                # Maven 依赖与构建配置
├── README.md                              # 项目说明文档
└── src/
    ├── main/
    │   ├── java/com/mongo/mymongo/
    │   │   ├── config/                    # 配置模块
    │   │   ├── controller/                # 接口控制层
    │   │   ├── domain/                    # 业务对象定义
    │   │   │   ├── dto/                   # 图谱模块 DTO
    │   │   │   ├── param/                 # Mongo 模块请求参数
    │   │   │   ├── po/                    # 持久化对象 / 数据承载对象
    │   │   │   └── vo/                    # 返回对象
    │   │   ├── exception/                 # 全局异常与业务异常
    │   │   ├── service/                   # 服务接口
    │   │   ├── service/impl/              # 服务实现
    │   │   ├── util/                      # 通用返回结构与分页工具
    │   │   └── MymongoApplication.java    # Spring Boot 启动入口
    │   └── resources/
    │       └── application.yml            # 项目配置文件
    └── test/
        └── java/com/mongo/mymongo/
            └── MymongoApplicationTests.java
```

---

## 包结构说明

### 1. 启动入口

#### `src/main/java/com/mongo/mymongo/MymongoApplication.java`
项目启动类，负责引导 Spring Boot 应用启动。

---

### 2. controller 接口层

#### `src/main/java/com/mongo/mymongo/controller/MongoController.java`
MongoDB 模块控制器，请求前缀为 `/mongo/`，暴露以下接口：

- `POST /mongo/add`：新增对象
- `PUT /mongo/edit`：修改对象
- `DELETE /mongo/del`：删除对象
- `GET /mongo/list`：分页查询列表

该控制器统一返回 `Result<T>` 结构，业务逻辑委托给 `MongoService`。

#### `src/main/java/com/mongo/mymongo/controller/KnowledgeQuantitativeKnTagController.java`
定量库知识标签图谱控制器，请求前缀为 `/quantitative/kn/tag`，暴露以下接口：

- `POST /quantitative/kn/tag/list`：查询标签关系列表
- `POST /quantitative/kn/tag/add`：新增标签关系
- `DELETE /quantitative/kn/tag/del`：删除标签关系
- `POST /quantitative/kn/tag/showTags`：查询图谱展示数据

该模块主要面向 Nebula Graph 图数据库操作。

---

### 3. service 服务层

#### `src/main/java/com/mongo/mymongo/service/MongoService.java`
定义 MongoDB 模块业务能力接口。

#### `src/main/java/com/mongo/mymongo/service/KnowledgeQuantitativeKnTagService.java`
定义知识标签图谱模块业务能力接口。

#### `src/main/java/com/mongo/mymongo/service/GraphExecutor.java`
对 Nebula Graph 执行能力进行抽象，屏蔽底层会话与执行细节。

---

### 4. service/impl 服务实现层

#### `src/main/java/com/mongo/mymongo/service/impl/MongoServiceImpl.java`
基于 `MongoTemplate` 实现 MongoDB 文档操作，当前围绕集合 `democoll` 展开。

核心逻辑：

- 按 `lib_id` 查询知识库文档
- 在 `info` 数组中新增或更新对象信息
- 使用 `DemoCollection` / `DemoCollectionInfo` 作为文档结构映射

当前实现状态：

- `add()`：已实现
- `edit()`：已实现
- `del()`：暂未实现，当前返回 `null`
- `list()`：暂未实现，当前返回 `null`

#### `src/main/java/com/mongo/mymongo/service/impl/KnowledgeQuantitativeKnTagServiceImpl.java`
图谱关系核心服务实现，基于 `GraphExecutor` 调用 Nebula Graph。

当前包含的主要能力：

- 查询标签关系
- 新增点和边
- 删除边及无关联孤立点
- 输出前端图谱展示结构

注意点：

- 当前 `SPACENAME` 固定为 `table_relation_space`
- 新增标签时，部分表名与标题值仍为临时拼接字符串
- 部分查询语句直接拼接 `libId` 等参数

#### `src/main/java/com/mongo/mymongo/service/impl/NebulaGraphExecutor.java`
Nebula Graph 执行器实现，负责：

- 获取 `NebulaPool` 会话
- 切换 graph space
- 执行 nGQL
- 返回 `ResultSet` 或 JSON 结果
- 提供简单重试能力

---

### 5. config 配置模块

#### `src/main/java/com/mongo/mymongo/config/NebulaGraphAutoConfiguration.java`
Nebula Graph 自动配置类，负责：

- 从配置读取 graph 地址
- 构建 `NebulaPoolConfig`
- 初始化 `NebulaPool`
- 可选处理 SSL 连接配置
- 应用退出时关闭连接池

#### `src/main/java/com/mongo/mymongo/config/NebulaGraphProperties.java`
用于绑定 `nebula.graph` 配置项，承载连接地址、认证信息、连接池参数、SSL 配置等。

---

### 6. domain 领域对象

#### dto
位于 `src/main/java/com/mongo/mymongo/domain/dto/`，主要服务于图谱模块：

- `QuantitativeKnTagAddDTO`
- `QuantitativeKnTagDelDTO`
- `QuantitativeKnTagQueryDTO`
- `QuantitativeKnTagShowQueryDTO`

#### param
位于 `src/main/java/com/mongo/mymongo/domain/param/`，主要服务于 Mongo 模块接口入参：

- `MongoAddParam`
- `MongoEditParam`
- `MongoDelParam`
- `MongoListParam`

#### po
位于 `src/main/java/com/mongo/mymongo/domain/po/`，主要包括：

- `DemoCollection`：MongoDB 主文档对象，对应集合 `democoll`
- `DemoCollectionInfo`：文档中的嵌套对象信息
- `GraphData`：图数据库 JSON 返回结果映射对象
- `AdsLmRetlLoanDaySum`：当前从已读代码看未参与核心链路

#### vo
位于 `src/main/java/com/mongo/mymongo/domain/vo/`，主要用于接口返回：

Mongo 模块：

- `MongoAddVO`
- `MongoEditVO`
- `MongoDelVO`
- `MongoListVO`

图谱模块：

- `QuantitativeKnTagListVO`
- `QuantitativeKnTagShowVO`
- `QuantitativeKnTagShowDataVO`
- `QuantitativeKnTagShowLinkVO`

---

### 7. exception 异常处理模块

#### `src/main/java/com/mongo/mymongo/exception/BusinessException.java`
自定义业务异常。

#### `src/main/java/com/mongo/mymongo/exception/GlobalExceptionHandler.java`
全局异常处理器，负责统一异常响应：

- `BusinessException`：返回业务错误码与错误信息
- `Exception`：兜底返回 `500` 和“系统繁忙，请稍后再试”

#### `src/main/java/com/mongo/mymongo/exception/ResultCodeEnum.java`
定义业务异常相关状态码枚举。

---

### 8. util 通用工具模块

#### `src/main/java/com/mongo/mymongo/util/Result.java`
统一响应结构，字段包括：

- `code`
- `msg`
- `data`
- `timestamp`

#### `src/main/java/com/mongo/mymongo/util/ResultCode.java`
定义通用成功 / 失败状态码。

#### `src/main/java/com/mongo/mymongo/util/PageResult.java`
统一分页结果封装。

#### `src/main/java/com/mongo/mymongo/util/PageQueryDTO.java`
分页查询基类。

---

## 核心业务说明

### MongoDB 文档管理模块

该模块的核心数据模型定义在：

- `src/main/java/com/mongo/mymongo/domain/po/DemoCollection.java`

从当前代码看，MongoDB 中以 `democoll` 集合存储知识库数据，结构大致如下：

- 顶层以 `lib_id` 作为知识库标识
- 每个知识库下通过 `info` 列表保存多个对象信息
- 每个对象包含 `objId`、`objName`、`objContent` 等字段

当前能力：

- 新增对象到指定知识库
- 修改指定知识库下指定对象
- 删除和列表查询接口已预留，但服务逻辑未完成

调用链：

```text
HTTP Request
  -> MongoController
  -> MongoService
  -> MongoServiceImpl
  -> MongoTemplate
  -> MongoDB
```

---

### Nebula Graph 图谱关系模块

该模块主要维护定量库知识标签间的关系，当前使用的 Space 固定为：

```text
table_relation_space
```

核心能力：

- 查询节点关系列表
- 新增标签节点和关系边
- 删除关系边
- 在边删除后尝试清理无关联孤立点
- 查询图谱展示数据，返回节点和连线结构

调用链：

```text
HTTP Request
  -> KnowledgeQuantitativeKnTagController
  -> KnowledgeQuantitativeKnTagService
  -> KnowledgeQuantitativeKnTagServiceImpl
  -> GraphExecutor
  -> NebulaGraphExecutor
  -> NebulaPool / Session
  -> Nebula Graph
```

---

## 配置说明

配置文件路径：

- `src/main/resources/application.yml`

### 服务端口

```yaml
server:
  port: 9154
```

### 应用信息

```yaml
spring:
  application:
    name: mymongo
    version: 1.0.0
```

### MySQL 配置

```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/e3-mall?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root
```

### MongoDB 配置

```yaml
spring:
  data:
    mongodb:
      host: 127.0.0.1
      port: 27017
      database: demodb
      auto-index-creation: true
```

### Nebula Graph 配置

```yaml
nebula:
  graph:
    addresses:
      - 192.168.213.148
    user: root
    password: nebula
    max-conn-size: 100
    timeout: 1000
```

> 注意：当前配置文件中存在明文账号密码，仅适合本地开发调试。正式环境建议改为环境变量、配置中心或密钥管理方案。

---

## 运行方式

### 环境要求

建议本地准备以下环境：

- JDK 17
- Maven 3.9+，或直接使用项目内置 `mvnw`
- MySQL
- MongoDB
- Nebula Graph

### 启动项目

使用 Maven Wrapper：

```bash
./mvnw spring-boot:run
```

或使用本地 Maven：

```bash
mvn spring-boot:run
```

启动后默认访问端口：

```text
9154
```

---

## 接口概览

### Mongo 模块

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/mongo/add` | 新增对象 |
| PUT | `/mongo/edit` | 修改对象 |
| DELETE | `/mongo/del` | 删除对象 |
| GET | `/mongo/list` | 分页列表 |

### 图谱模块

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/quantitative/kn/tag/list` | 查询标签关系 |
| POST | `/quantitative/kn/tag/add` | 新增标签关系 |
| DELETE | `/quantitative/kn/tag/del` | 删除标签关系 |
| POST | `/quantitative/kn/tag/showTags` | 查询图谱展示数据 |

---

## 当前项目状态评估

### 已具备的部分

- 基础 Spring Boot Web 服务框架已搭建完成
- MongoDB 与 Nebula Graph 双数据源能力已接入
- 控制层、服务层、配置层、异常层划分清晰
- 统一返回结构 `Result<T>` 已建立
- 图谱关系模块主流程较完整

### 当前待完善部分

- `src/main/java/com/mongo/mymongo/service/impl/MongoServiceImpl.java` 中：
  - `del()` 尚未实现
  - `list()` 尚未实现
- Elasticsearch、MyBatis Plus、MySQL 依赖已引入，但从当前主链路看使用度有限
- 图谱新增逻辑中部分业务字段仍使用临时拼接值
- 配置中仍使用明文连接信息
- 测试覆盖较少

---

## 建议阅读顺序

如果是新成员接手项目，建议按下面顺序阅读：

1. `pom.xml`
2. `src/main/resources/application.yml`
3. `src/main/java/com/mongo/mymongo/MymongoApplication.java`
4. `src/main/java/com/mongo/mymongo/controller/MongoController.java`
5. `src/main/java/com/mongo/mymongo/service/impl/MongoServiceImpl.java`
6. `src/main/java/com/mongo/mymongo/controller/KnowledgeQuantitativeKnTagController.java`
7. `src/main/java/com/mongo/mymongo/service/impl/KnowledgeQuantitativeKnTagServiceImpl.java`
8. `src/main/java/com/mongo/mymongo/service/impl/NebulaGraphExecutor.java`
9. `src/main/java/com/mongo/mymongo/exception/GlobalExceptionHandler.java`
10. `src/main/java/com/mongo/mymongo/util/Result.java`

---

## 总结

`mymongo` 当前是一个以 Spring Boot 为基础的后端服务项目，重点围绕：

- MongoDB 文档管理
- Nebula Graph 图关系维护
- 统一响应与异常处理

整体架构清晰，图谱模块相对更完整，Mongo 模块仍有部分接口待补齐。作为一个同时接入文档数据库与图数据库的后端工程骨架，当前已经具备继续扩展业务的基础。
