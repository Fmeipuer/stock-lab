# stock-lab

## 项目定位

`stock-lab` 是一个用于个人技术练手和股票相关功能探索的 Spring Boot 项目。

这个项目不再只围绕某一个单独技术命名，而是作为一个长期演进的实验场：遇到新的、没用过的技术时，可以先在这里接入并写 demo，例如 MongoDB、Nebula Graph、Elasticsearch、MyBatis Plus 等；后续如果某些 demo 沉淀出稳定能力，再逐步扩展成和股票分析、投资计算、数据建模相关的实际功能。

项目当前阶段更偏向：

- 学习和验证新技术的使用方式
- 保留可运行、可复盘的技术 demo
- 逐步沉淀股票相关的小工具和业务能力
- 为后续可能的股票数据分析平台打基础

---

## 命名说明

项目名称：`stock-lab`

含义：

- `stock`：未来重点会逐步向股票相关功能发展
- `lab`：当前主要定位是个人技术实验室，用来练习和验证新技术

`stock-lab` 不会被某一个技术栈限制，更适合承载 MongoDB、Nebula Graph、Elasticsearch、MySQL、股票计算工具等多种模块。

---

## 当前已有能力

### 1. MongoDB 文档 demo

当前已有 MongoDB 文档操作模块，请求前缀为：

```text
/mongo/
```

接口概览：

| 方法 | 路径 | 说明 | 当前状态 |
| --- | --- | --- | --- |
| POST | `/mongo/add` | 新增对象 | 已实现 |
| PUT | `/mongo/edit` | 修改对象 | 已实现 |
| DELETE | `/mongo/del` | 删除对象 | 接口预留，逻辑待完善 |
| GET | `/mongo/list` | 分页查询 | 接口预留，逻辑待完善 |

核心实现：

- `src/main/java/com/stocklab/controller/MongoController.java`
- `src/main/java/com/stocklab/service/MongoService.java`
- `src/main/java/com/stocklab/service/impl/MongoServiceImpl.java`
- `src/main/java/com/stocklab/domain/po/DemoCollection.java`

该模块主要用于练习 Spring Data MongoDB、`MongoTemplate`、文档结构映射、嵌套数组更新等能力。

---

### 2. Nebula Graph 图谱 demo

当前已有 Nebula Graph 图数据库模块，请求前缀为：

```text
/quantitative/kn/tag
```

接口概览：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/quantitative/kn/tag/list` | 查询标签关系列表 |
| POST | `/quantitative/kn/tag/add` | 新增标签关系 |
| DELETE | `/quantitative/kn/tag/del` | 删除标签关系 |
| POST | `/quantitative/kn/tag/showTags` | 查询图谱展示数据 |

核心实现：

- `src/main/java/com/stocklab/controller/KnowledgeQuantitativeKnTagController.java`
- `src/main/java/com/stocklab/service/KnowledgeQuantitativeKnTagService.java`
- `src/main/java/com/stocklab/service/impl/KnowledgeQuantitativeKnTagServiceImpl.java`
- `src/main/java/com/stocklab/service/GraphExecutor.java`
- `src/main/java/com/stocklab/service/impl/NebulaGraphExecutor.java`
- `src/main/java/com/stocklab/config/NebulaGraphAutoConfiguration.java`
- `src/main/java/com/stocklab/config/NebulaGraphProperties.java`

该模块主要用于练习 Nebula Graph Java Client、连接池配置、nGQL 执行、点边关系维护、图结构数据返回等能力。

---

### 3. 股票涨停复利计算器

当前已有一个股票相关小工具：涨停复利计算器。

后端接口：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/stock/limit-up/calculate` | 根据起始金额、目标金额、涨停利率计算需要的周期数 |

前端页面：

```text
src/main/resources/static/limit-up-calculator.html
```

核心实现：

- `src/main/java/com/stocklab/controller/LimitUpCalculatorController.java`
- `src/main/java/com/stocklab/service/LimitUpCalculatorService.java`
- `src/main/java/com/stocklab/service/impl/LimitUpCalculatorServiceImpl.java`
- `src/main/java/com/stocklab/domain/dto/LimitUpCalculateDTO.java`
- `src/main/java/com/stocklab/domain/vo/LimitUpCalculateVO.java`
- `src/main/java/com/stocklab/domain/vo/LimitUpPeriodAmountVO.java`

该模块是项目向股票实际功能发展的起点。

---

## 技术栈

当前项目基于以下技术：

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

其中部分依赖当前只是作为练习和预留能力引入，后续可以按模块逐步补齐实际 demo。

---

## 项目结构

```text
stock-lab/
├── .mvn/                                  # Maven Wrapper 配置
├── mvnw                                   # Maven Wrapper 启动脚本（Unix）
├── mvnw.cmd                               # Maven Wrapper 启动脚本（Windows）
├── pom.xml                                # Maven 依赖与构建配置
├── README.md                              # 项目说明文档
└── src/
    ├── main/
    │   ├── java/com/stocklab/
    │   │   ├── config/                    # 配置模块
    │   │   ├── controller/                # 接口控制层
    │   │   ├── domain/                    # DTO / Param / PO / VO
    │   │   ├── exception/                 # 全局异常与业务异常
    │   │   ├── service/                   # 服务接口
    │   │   ├── service/impl/              # 服务实现
    │   │   ├── util/                      # 通用返回结构与分页工具
    │   │   └── StockLabApplication.java   # Spring Boot 启动入口
    │   └── resources/
    │       ├── application.yml            # 项目配置文件
    │       └── static/                    # 静态页面
    └── test/
        └── java/com/stocklab/
            └── StockLabApplicationTests.java
```

---

## 配置说明

配置文件：

```text
src/main/resources/application.yml
```

### 应用信息

```yaml
spring:
  application:
    name: stock-lab
    version: 1.0.0
```

### 服务端口

```yaml
server:
  port: 9199
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

> 当前配置适合本地练习和 demo 调试。后续如果项目开始承载真实业务，应将数据库账号、密码、地址等敏感配置迁移到环境变量、配置中心或密钥管理方案中。

---

## 运行方式

### 环境要求

建议本地准备：

- JDK 17
- Maven 3.9+，或直接使用项目内置 Maven Wrapper
- MongoDB
- Nebula Graph
- MySQL，可按实际 demo 需要启动

### 启动项目

使用 Maven Wrapper：

```bash
./mvnw spring-boot:run
```

或使用本地 Maven：

```bash
mvn spring-boot:run
```

启动后默认端口：

```text
9199
```

股票涨停复利计算器页面：

```text
http://localhost:9199/limit-up-calculator.html
```

---

## 后续演进方向

### 技术练习方向

后续可以按技术模块补充更多 demo：

- MongoDB：聚合查询、索引、事务、复杂文档建模
- Nebula Graph：股票、行业、概念、公司之间的图谱关系建模
- Elasticsearch：股票公告、新闻、研报文本检索
- MyBatis Plus：结构化股票数据的 CRUD 与分页查询
- 定时任务：定时拉取或模拟股票行情数据
- 缓存：热点股票、概念板块、计算结果缓存
- 消息队列：行情事件、异步计算、数据清洗流程

### 股票功能方向

后续可以逐步扩展为更实用的股票工具集：

- 涨停复利、收益率、回撤等投资计算工具
- 股票基础信息管理
- 行业、概念、个股关系图谱
- 自选股观察列表
- 简单策略回测 demo
- 股票公告、新闻、研报搜索
- 个股标签体系和知识库

---

## 当前待完善事项

- `MongoServiceImpl.del()` 逻辑待实现
- `MongoServiceImpl.list()` 逻辑待实现
- 图谱模块中部分字段仍是 demo 拼接值，后续可按真实数据模型重构
- Elasticsearch、MyBatis Plus、MySQL 依赖已引入，但实际业务 demo 还不完整
- 配置中仍存在本地明文连接信息
- 测试覆盖较少，需要按模块补充单元测试和集成测试

---

## 建议阅读顺序

如果后续继续扩展，可以按下面顺序熟悉项目：

1. `pom.xml`
2. `src/main/resources/application.yml`
3. `src/main/java/com/stocklab/StockLabApplication.java`
4. `src/main/java/com/stocklab/controller/LimitUpCalculatorController.java`
5. `src/main/java/com/stocklab/service/impl/LimitUpCalculatorServiceImpl.java`
6. `src/main/java/com/stocklab/controller/MongoController.java`
7. `src/main/java/com/stocklab/service/impl/MongoServiceImpl.java`
8. `src/main/java/com/stocklab/controller/KnowledgeQuantitativeKnTagController.java`
9. `src/main/java/com/stocklab/service/impl/KnowledgeQuantitativeKnTagServiceImpl.java`
10. `src/main/java/com/stocklab/service/impl/NebulaGraphExecutor.java`

---

## 总结

`stock-lab` 是一个面向个人成长和长期演进的技术实验项目。

短期目标是把遇到的新技术用 demo 的方式跑通、记录下来、形成可复用经验；长期目标是逐步把这些技术能力和股票场景结合起来，沉淀出一套股票相关的小工具、数据服务和分析能力。
