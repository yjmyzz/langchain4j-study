# LongChain4j Study - 大模型聊天与工具调用示例

这是一个用于学习LongChain4j的Spring Boot项目，集成了本地Ollama服务和DeepSeek云服务，提供聊天功能和工具调用示例。

**Package**: `com.cnblogs.yjmyzz.longchain4j.study`

## 🚀 项目特性

- **Java 21**: 使用最新的Java LTS版本
- **Spring Boot 3.2.0**: 现代化的Spring Boot框架
- **LongChain4j 1.1.0**: 强大的Java AI框架
- **多模型支持**: 
  - Ollama: 支持本地大语言模型
  - DeepSeek: 支持云端大语言模型
- **工具调用**: 支持AI调用自定义工具（如订单查询）
- **RESTful API**: 提供完整的聊天和工具调用API接口
- **流式响应**: 支持Server-Sent Events (SSE)流式聊天
- **Lombok**: 减少样板代码
- **完整测试**: 包含单元测试和集成测试

## 📋 前置要求

1. **Java 21**: 确保已安装JDK 21
2. **Maven 3.6+**: 确保已安装Maven
3. **Ollama**: 确保已安装并启动Ollama服务

## 🛠️ 安装和配置

### 1. 安装Ollama

访问 [Ollama官网](https://ollama.ai/) 下载并安装Ollama。

### 2. 启动Ollama服务

```bash
# 启动Ollama服务
ollama serve
```

### 3. 下载模型

```bash
# 下载qwen3:0.6b模型（默认模型）
ollama pull qwen3:0.6b

# 或者下载其他模型
ollama pull llama2
ollama pull llama2:7b
ollama pull llama2:13b
```

### 4. 克隆项目

```bash
git clone https://github.com/yjmyzz/longchain4j-study.git
cd longchain4j-study
```

### 5. 编译项目

```bash
mvn clean compile
```

### 6. 运行项目

```bash
mvn spring-boot:run
```

## 🌐 使用方式

### API接口

#### 聊天功能

##### 发送聊天消息

```bash
# 使用Ollama模型
curl "http://localhost:8080/api/chat?prompt=你好，请介绍一下Java编程语言"

# 使用DeepSeek模型
curl "http://localhost:8080/api/chat/deepseek?prompt=你好，请介绍一下Java编程语言"
```

##### 流式聊天消息

```bash
# 使用Ollama模型
curl "http://localhost:8080/api/chat/stream?prompt=你好，请介绍一下Java编程语言"

# 使用DeepSeek模型
curl "http://localhost:8080/api/chat/deepseek/stream?prompt=你好，请介绍一下Java编程语言"
```

**注意**: 流式API返回HTML格式的SSE数据，适合在浏览器中直接测试。

#### 工具调用功能

##### 查询订单状态

```bash
# 直接调用工具
curl "http://localhost:8080/api/order/status/direct?orderId=12345"

# 通过Ollama模型调用工具
curl "http://localhost:8080/api/order/status/ollama?orderId=12345"

# 通过DeepSeek模型调用工具
curl "http://localhost:8080/api/order/status/deepseek?orderId=12345"
```

#### 健康检查

```bash
# 聊天服务健康检查
curl http://localhost:8080/api/health

# 订单服务健康检查
curl http://localhost:8080/api/order/health
```

## ⚙️ 配置说明

项目配置文件位于 `src/main/resources/application.yml`：

```yaml
# 服务器配置
server:
  port: 8080

# Spring应用配置
spring:
  application:
    name: longchain4j-study
  
  # 日志配置
  logging:
    level:
      com.cnblogs.yjmyzz.longchain4j.study: DEBUG
      dev.langchain4j: DEBUG

# Ollama配置
ollama:
  base-url: http://localhost:11434  # Ollama服务地址
  model: qwen3:0.6b                 # 使用的模型名称
  timeout: 60                       # 请求超时时间（秒）

# DeepSeek配置
deepseek:
  api-key: your-api-key-here        # DeepSeek API密钥
  base-url: https://api.deepseek.com # DeepSeek服务地址
  model: deepseek-chat              # 使用的模型名称
  timeout: 60                       # 请求超时时间（秒）
  temperature: 0.7                  # 温度参数
  max-tokens: 2048                  # 最大token数
```

## 📁 项目结构

```
src/
├── main/
│   ├── java/
│   │   ├── com/cnblogs/tools/
│   │   │   └── OrderTools.java                # 订单工具类
│   │   └── com/cnblogs/yjmyzz/longchain4j/study/
│   │       ├── LongChain4jStudyApplication.java    # 主启动类
│   │       ├── config/
│   │       │   ├── DeepSeekConfig.java            # DeepSeek配置类
│   │       │   ├── OllamaConfig.java              # Ollama配置类
│   │       │   └── OrderToolConfig.java           # 订单工具配置类
│   │       └── controller/
│   │           ├── ChatController.java            # 聊天控制器
│   │           └── OrderController.java           # 订单控制器
│   └── resources/
│       └── application.yml                     # 应用配置
└── test/
    └── java/com/cnblogs/yjmyzz/longchain4j/study/
        └── LongChain4jStudyApplicationTests.java  # 应用测试
```

## 📦 Package结构

项目使用标准的Maven package命名规范：
- **GroupId**: `com.yjmyzz`
- **Package**: `com.cnblogs.yjmyzz.longchain4j.study`
- **主类**: `LongChain4jStudyApplication`

## 🔧 核心组件说明

### 1. 配置类

#### OllamaConfig.java
- 配置Ollama聊天模型和流式聊天模型
- 支持自定义模型名称、服务地址和超时时间
- 启用请求和响应日志记录

#### DeepSeekConfig.java
- 配置DeepSeek聊天模型和流式聊天模型
- 支持API密钥、服务地址配置
- 可调整温度和最大token数等参数
- 启用请求和响应日志记录

#### OrderToolConfig.java
- 配置订单工具的注册和管理
- 集成工具到AI模型中

### 2. 控制器

#### ChatController.java
- 提供聊天相关的RESTful API接口
- 支持普通聊天和流式聊天
- 实现Server-Sent Events (SSE)流式响应
- 支持Ollama和DeepSeek两种模型
- 支持CORS跨域请求

#### OrderController.java
- 提供订单相关的RESTful API接口
- 支持直接工具调用和AI辅助工具调用
- 实现工具调用的多种方式（直接/Ollama/DeepSeek）
- 包含健康检查端点

### 3. 工具类

#### OrderTools.java
- 提供订单状态查询功能
- 支持AI工具调用集成
- 实现标准化的工具规范

### 4. 主要依赖
- **Spring Boot Web**: Web应用支持
- **Spring WebFlux**: 响应式编程支持
- **LongChain4j**: AI框架核心
- **LongChain4j Ollama**: Ollama集成
- **LongChain4j OpenAI**: DeepSeek集成（兼容OpenAI接口）
- **Lombok**: 代码简化工具

## 🧪 测试

### 运行所有测试

```bash
mvn test
```

### 运行特定测试

```bash
mvn test -Dtest=com.cnblogs.yjmyzz.longchain4j.study.LongChain4jStudyApplicationTests
```

## 🔧 开发指南

### 添加新的模型支持

1. 创建新的模型配置类（参考 `OllamaConfig.java` 或 `DeepSeekConfig.java`）
2. 在 `application.yml` 中添加相应的配置项
3. 确保模型服务可用（本地或云端）

### 扩展聊天功能

1. 在 `ChatController` 中添加新的业务逻辑
2. 添加新的API端点
3. 实现自定义的响应处理器
4. 支持新的模型调用方式

### 添加新的工具

1. 创建工具类并实现相应的功能方法
2. 添加工具配置类进行注册
3. 在控制器中实现工具调用接口
4. 支持直接调用和AI辅助调用

### 自定义配置

可以通过修改 `application.yml` 来调整：
- Ollama服务配置
  - 服务地址
  - 使用的模型
  - 超时时间
- DeepSeek服务配置
  - API密钥
  - 服务地址
  - 模型参数
- 日志级别

**注意**: 
- 日志配置中的package路径为 `com.cnblogs.yjmyzz.longchain4j.study`
- DeepSeek API密钥请妥善保管，不要提交到代码仓库

## 🐛 故障排除

### 常见问题

1. **Ollama连接失败**
   - 确保Ollama服务已启动：`ollama serve`
   - 检查端口11434是否被占用
   - 验证模型是否已下载：`ollama list`

2. **DeepSeek连接失败**
   - 检查API密钥是否正确配置
   - 验证网络连接是否正常
   - 确认API请求配额是否充足

3. **模型响应缓慢**
   - 检查硬件资源（CPU、内存）
   - 考虑使用更小的模型
   - 调整超时配置
   - 对于本地模型，考虑使用GPU加速

4. **内存不足**
   - 增加JVM堆内存：`-Xmx4g`
   - 使用更小的模型
   - 优化批处理大小
   - 考虑使用云端模型（DeepSeek）

5. **流式响应问题**
   - 确保浏览器支持SSE
   - 检查网络连接稳定性
   - 查看应用日志排查问题

6. **工具调用失败**
   - 检查工具类是否正确注册
   - 验证工具方法签名是否符合规范
   - 确认AI模型是否正确理解工具用途
   - 查看日志中的工具调用请求和响应

## 📝 许可证

本项目采用 MIT 许可证。

## 🤝 贡献

欢迎提交Issue和Pull Request来改进这个项目！

## 📞 联系方式

如有问题，请通过以下方式联系：
- 提交GitHub Issue: https://github.com/yjmyzz/longchain4j-study/issues
- 作者博客: http://yjmyzz.cnblogs.com
- 作者: 菩提树下的杨过

## 🙏 致谢

感谢 [LangChain4j](https://github.com/langchain4j/langchain4j) 开源项目提供的强大支持！

特别感谢以下官方文档资源：
- [LangChain4j 中文文档](https://docs.langchain4j.info/) - 为Java应用赋能大模型能力的官方中文指南
- [LangChain4j 英文文档](https://docs.langchain4j.dev/) - 官方英文文档，提供完整的技术参考

---

**注意**: 请确保在使用前已正确安装和配置Ollama服务。
