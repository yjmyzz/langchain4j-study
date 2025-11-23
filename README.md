# LangChain4j Study - DeepSeek大模型聊天示例

这是一个用于学习LangChain4j的Spring Boot项目，集成了DeepSeek大模型，提供强大的AI聊天功能示例。

**Package**: `com.cnblogs.yjmyzz.langchain4j.study`

## 🚀 项目特性

- **Java 25**: 使用最新的Java版本
- **Spring Boot 4.0.0**: 现代化的Spring Boot框架
- **LangChain4j 1.8.0**: 强大的Java AI框架
- **DeepSeek集成**: 支持云端DeepSeek大语言模型
- **RESTful API**: 提供完整的聊天API接口
- **流式响应**: 支持Server-Sent Events (SSE)流式聊天
- **多租户支持**: 支持动态API密钥配置和缓存管理
- **环境变量配置**: 支持安全的API密钥管理
- **Spring WebFlux**: 响应式编程支持
- **完整测试**: 包含单元测试和集成测试

## 📋 前置要求

1. **Java 25**: 确保已安装JDK 25
2. **Maven 3.6+**: 确保已安装Maven
3. **DeepSeek API密钥**: 需要有效的DeepSeek API密钥

## 🛠️ 安装和配置

### 1. 获取DeepSeek API密钥

1. 访问 [DeepSeek官网](https://platform.deepseek.com/)
2. 注册账号并登录
3. 在控制台中创建API密钥
4. 复制您的API密钥

### 2. 克隆项目

```bash
git clone https://github.com/yjmyzz/langchain4j-study.git
cd langchain4j-study
```

### 3. 配置API密钥

有两种方式配置API密钥：

#### 方式一：环境变量（推荐）

```bash
# Windows PowerShell
$env:DEEPSEEK_API_KEY="your-actual-api-key-here"

# Windows CMD
set DEEPSEEK_API_KEY=your-actual-api-key-here

# Linux/Mac
export DEEPSEEK_API_KEY="your-actual-api-key-here"
```

#### 方式二：修改配置文件

编辑 `src/main/resources/application.yml` 文件：

```yaml
deepseek:
  api-key: your-actual-api-key-here  # 替换为您的实际API密钥
```

### 4. 编译项目

```bash
mvn clean compile
```

### 5. 运行项目

```bash
mvn spring-boot:run
```

## 🌐 使用方式

### API接口

#### 发送聊天消息

```bash
# 使用默认API密钥
curl "http://localhost:8080/api/chat?prompt=你好，请介绍一下Java编程语言"

# 使用自定义API密钥
curl "http://localhost:8080/api/chat?prompt=你好，请介绍一下Java编程语言&apiKey=your-api-key-here"
```

#### 流式聊天消息

```bash
# 使用默认API密钥
curl "http://localhost:8080/api/chat/stream?prompt=你好，请介绍一下Java编程语言"

# 使用自定义API密钥
curl "http://localhost:8080/api/chat/stream?prompt=你好，请介绍一下Java编程语言&apiKey=your-api-key-here"
```

**注意**: 流式API返回HTML格式的SSE数据，适合在浏览器中直接测试。

#### 健康检查

```bash
curl http://localhost:8080/api/health
```

#### 缓存管理

```bash
# 查看缓存统计
curl http://localhost:8080/api/cache/stats

# 清除指定API密钥的缓存
curl -X POST "http://localhost:8080/api/cache/clear?apiKey=your-api-key-here"

# 清除所有缓存
curl -X POST http://localhost:8080/api/cache/clear-all

# 验证API密钥格式
curl "http://localhost:8080/api/validate-api-key?apiKey=your-api-key-here"
```

### Web界面

项目提供了友好的Web测试界面：

1. 启动应用后，访问 `http://localhost:8080/`
2. 在输入框中输入您的问题
3. 点击"发送"进行普通聊天，或点击"流式发送"体验流式响应
4. 支持实时显示AI回复内容

## ⚙️ 配置说明

项目配置文件位于 `src/main/resources/application.yml`：

```yaml
# 服务器配置
server:
  port: 8080

# Spring应用配置
spring:
  application:
    name: langchain4j-study
  
  # 日志配置
  logging:
    level:
      com.cnblogs.yjmyzz.langchain4j.study: DEBUG
      dev.langchain4j: DEBUG
    pattern:
      console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"

# DeepSeek配置
deepseek:
  api-key: ${DEEPSEEK_API_KEY:your-deepseek-api-key-here}  # API密钥
  base-url: https://api.deepseek.com                       # DeepSeek API地址
  model: deepseek-chat                                     # 使用的模型名称
  timeout: 60                                              # 请求超时时间（秒）
  temperature: 0.7                                         # 生成文本的随机性（0-1）
  max-tokens: 2048                                         # 最大生成token数

# 应用信息
info:
  app:
    name: langchain4j Study
    version: 1.0.0
    description: LangChain4j学习项目 - 集成DeepSeek大模型聊天示例
```

### 配置参数说明

| 参数 | 默认值 | 说明 |
|------|--------|------|
| `deepseek.api-key` | your-deepseek-api-key-here | DeepSeek API密钥 |
| `deepseek.base-url` | https://api.deepseek.com | DeepSeek API基础URL |
| `deepseek.model` | deepseek-chat | 使用的模型名称 |
| `deepseek.timeout` | 60 | 请求超时时间（秒） |
| `deepseek.temperature` | 0.7 | 生成文本的随机性（0-1） |
| `deepseek.max-tokens` | 2048 | 最大生成token数 |

## 📁 项目结构

```
src/
├── main/
│   ├── java/com/cnblogs/yjmyzz/langchain4j/study/
│   │   ├── LangChain4jStudyApplication.java    # 主启动类
│   │   ├── config/
│   │   │   └── DeepSeekConfig.java            # DeepSeek配置类
│   │   ├── controller/
│   │   │   └── ChatController.java            # 聊天控制器
│   │   └── service/
│   │       └── DynamicConfigService.java      # 动态配置服务（支持多租户）
│   └── resources/
│       └── application.yml                     # 应用配置
└── test/
    └── java/com/cnblogs/yjmyzz/langchain4j/study/
        └── LangChain4jStudyApplicationTests.java  # 应用测试
```

## 📦 Package结构

项目使用标准的Maven package命名规范：
- **GroupId**: `com.yjmyzz`
- **ArtifactId**: `langchain4j-study`
- **Version**: `1.0.0`
- **Package**: `com.cnblogs.yjmyzz.langchain4j.study`
- **主类**: `LangChain4jStudyApplication`

## 🔧 核心组件说明

### 1. DeepSeekConfig.java
- 配置DeepSeek聊天模型和流式聊天模型
- 支持自定义模型名称、API地址和超时时间
- 支持温度、最大token数等参数配置
- 启用请求和响应日志记录
- 支持环境变量配置API密钥

### 2. ChatController.java
- 提供RESTful API接口
- 支持普通聊天和流式聊天
- 支持动态API密钥配置（多租户支持）
- 实现Server-Sent Events (SSE)流式响应
- 使用Spring WebFlux的Flux实现响应式流式处理
- 包含健康检查端点
- 支持CORS跨域请求
- 提供缓存管理API

### 3. DynamicConfigService.java
- 动态配置服务，支持运行时创建和管理多个API密钥的模型实例
- 提供模型实例缓存，提高性能
- 支持多租户场景
- 提供缓存统计和清理功能

### 4. 主要依赖
- **Spring Boot Web**: Web应用支持
- **Spring Boot Validation**: 数据验证支持
- **Spring WebFlux**: 响应式编程支持（用于流式响应）
- **LangChain4j**: AI框架核心（版本 1.8.0）
- **LangChain4j OpenAI**: DeepSeek集成（兼容OpenAI API）

## 🧪 测试

### 运行所有测试

```bash
mvn test
```

### 运行特定测试

```bash
mvn test -Dtest=com.cnblogs.yjmyzz.langchain4j.study.LangChain4jStudyApplicationTests
```

## 🔧 开发指南

### 添加新的模型支持

1. 在 `application.yml` 中修改 `deepseek.model` 配置
2. 确保对应的模型在DeepSeek中可用

### 扩展聊天功能

1. 在 `ChatController` 中添加新的业务逻辑
2. 添加新的API端点
3. 实现自定义的响应处理器

### 自定义配置

可以通过修改 `application.yml` 来调整：
- DeepSeek API地址
- 使用的模型
- 超时时间
- 温度和最大token数
- 日志级别

**注意**: 
- 日志配置中的package路径为 `com.cnblogs.yjmyzz.langchain4j.study`
- 修改配置后需要重启应用才能生效

## 🐛 故障排除

### 常见问题

1. **API密钥问题**
   - 确保API密钥正确设置
   - 检查环境变量是否正确加载
   - 验证API密钥是否有效

2. **网络连接问题**
   - 确保网络连接正常
   - 检查防火墙设置
   - 验证DeepSeek服务状态

3. **认证失败**
   - 检查API密钥格式是否正确
   - 确认API密钥权限
   - 查看错误日志获取详细信息

4. **请求频率限制**
   - 检查API使用配额
   - 调整请求频率
   - 考虑升级API套餐

5. **流式响应问题**
   - 确保浏览器支持SSE
   - 检查网络连接稳定性
   - 查看应用日志排查问题

6. **Java 25 编译问题**
   - 项目使用 Java 25，确保已安装 JDK 25
   - 如果遇到 Lombok 相关编译错误，项目已移除 Lombok 注解处理器的使用
   - 所有日志记录使用标准的 SLF4J Logger，不依赖 Lombok

### 调试方法

1. 检查API密钥是否正确设置
2. 查看应用日志中的详细错误信息
3. 确认网络连接正常
4. 验证DeepSeek服务状态
5. 使用缓存管理API查看模型实例缓存状态

## 📝 许可证

本项目采用 MIT 许可证。

## 🤝 贡献

欢迎提交Issue和Pull Request来改进这个项目！

## 📞 联系方式

如有问题，请通过以下方式联系：
- 提交GitHub Issue: https://github.com/yjmyzz/langchain4j-study/issues
- 作者博客: http://yjmyzz.cnblogs.com
- 作者: 菩提树下的杨过

## 🙏 致谢

感谢 [LangChain4j](https://github.com/langchain4j/langchain4j) 开源项目提供的强大支持！

特别感谢以下官方文档资源：
- [LangChain4j 中文文档](https://docs.langchain4j.info/) - 为Java应用赋能大模型能力的官方中文指南
- [LangChain4j 英文文档](https://docs.langchain4j.dev/) - 官方英文文档，提供完整的技术参考
- [DeepSeek官网](https://platform.deepseek.com/) - 提供强大的大语言模型服务

## 📚 相关文档

- [DeepSeek设置指南](DEEPSEEK_SETUP.md) - 详细的DeepSeek配置和使用指南
- [环境变量示例](env.example) - API密钥配置示例

## ⚠️ 重要说明

### Java 25 兼容性

项目使用 Java 25 进行开发。由于 Java 25 是较新的版本，某些工具可能尚未完全支持：

- **Lombok**: 当前版本的 Lombok (1.18.36) 与 Java 25 存在兼容性问题，项目已移除 Lombok 注解处理器的使用
- 所有日志记录使用标准的 SLF4J Logger，不依赖 Lombok 的 `@Slf4j` 注解
- 如果遇到编译问题，请确保使用 JDK 25

### 多租户支持

项目支持多租户场景，可以通过以下方式使用：

1. **默认配置**: 在 `application.yml` 中配置默认 API 密钥
2. **动态配置**: 在 API 请求中通过 `apiKey` 参数传递不同的 API 密钥
3. **缓存管理**: 使用缓存管理 API 查看和管理模型实例缓存

---

**注意**: 请确保在使用前已正确配置DeepSeek API密钥。
