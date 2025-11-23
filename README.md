# langchain4j Study - MCP调用示例

这是一个用于学习langchain4j的Spring Boot项目，集成了本地Ollama服务，通过MCP (Model Context Protocol) 协议实现AI工具调用功能示例。

**Package**: `com.cnblogs.yjmyzz.langchain4j.study`

## 🚀 项目特性

- **Java 25**: 使用最新的Java版本
- **Spring Boot 4.0.0**: 现代化的Spring Boot框架
- **LangChain4j 1.8.0**: 强大的Java AI框架
- **Ollama集成**: 支持本地大语言模型
- **MCP支持**: 支持Model Control Protocol (MCP)工具调用
- **工具调用**: 支持通过MCP协议调用远程工具（如订单查询）
- **RESTful API**: 提供完整的MCP工具调用API接口
- **完整测试**: 包含单元测试和集成测试

## 📋 前置要求

1. **Java 25**: 确保已安装JDK 25
2. **Maven 3.6+**: 确保已安装Maven
3. **Ollama**: 确保已安装并启动Ollama服务
4. **SSE服务器**: 如需使用MCP功能，请确保SSE服务器已启动（默认地址：http://localhost:8070/sse）

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
git clone https://github.com/yjmyzz/langchain4j-study.git
cd langchain4j-study
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

#### MCP工具调用功能

##### 查询订单状态（通过MCP）

```bash
# 通过MCP协议调用工具查询订单状态
curl "http://localhost:8080/api/mcp/order?orderId=12345"
```

**功能说明**：
- MCP调用需要先启动SSE服务器（默认地址：http://localhost:8070/sse）
- MCP调用通过Ollama模型进行智能交互，AI自动理解工具功能并调用
- 支持自定义超时时间和工具执行超时设置（默认10秒）
- 提供完整的请求和响应日志记录
- 使用 `AiServices` 构建AI助手，自动处理工具调用流程
- 自动管理MCP客户端连接的生命周期

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

# Ollama配置
ollama:
  base-url: http://localhost:11434  # Ollama服务地址
  model: qwen3:0.6b                 # 使用的模型名称
  timeout: 60                       # 请求超时时间（秒）

# 应用信息
info:
  app:
    name: langchain4j Study
    version: 1.0.0
    description: langchain4j学习项目 - MCP工具调用示例

## 📁 项目结构

```
src/
├── main/
│   ├── java/com/cnblogs/yjmyzz/langchain4j/study/
│   │   ├── LongChain4jStudyApplication.java    # 主启动类
│   │   ├── config/
│   │   │   └── OllamaConfig.java              # Ollama配置类
│   │   └── controller/
│   │       └── McpController.java             # MCP工具调用控制器
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
- **主类**: `LongChain4jStudyApplication`

## 🔧 核心组件说明

### 1. 配置类

#### OllamaConfig.java
- 配置Ollama聊天模型和流式聊天模型
- 支持自定义模型名称、服务地址和超时时间
- 启用请求和响应日志记录
- 使用 `@Bean` 注解注册为Spring Bean，支持依赖注入
- Bean名称：`ollamaChatModel` 和 `ollamaStreamingChatModel`

### 2. 控制器

#### McpController.java
- 提供基于MCP (Model Control Protocol)的工具调用示例
- 支持通过SSE服务器进行工具调用（默认地址：http://localhost:8070/sse）
- 集成Ollama模型进行智能交互，AI自动理解并调用远程工具
- 使用 `AiServices` 构建AI助手，简化工具调用流程
- 实现订单状态查询的MCP调用示例
- 支持CORS跨域请求
- 自动管理MCP客户端连接的生命周期，确保资源正确释放

### 3. 主要依赖
- **Spring Boot Web**: Web应用支持
- **Spring Boot Validation**: 数据验证支持
- **Spring WebFlux**: 响应式编程支持
- **LangChain4j**: AI框架核心（版本 1.8.0）
- **LangChain4j Ollama**: Ollama集成
- **LangChain4j MCP**: MCP协议支持（版本 1.1.0-beta7）
- **Lombok**: 代码简化工具（注意：由于Java 25兼容性问题，项目已移除Lombok注解处理器的使用）

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

### 添加新的MCP工具调用

1. 在 `McpController` 中添加新的端点方法
2. 使用 `initSseClient` 方法初始化MCP客户端连接（可自定义SSE服务器地址）
3. 使用 `AiServices.builder` 构建AI助手，配置聊天模型和 `McpToolProvider`
4. 通过AI助手发送自然语言请求，AI会自动理解工具功能并调用
5. 确保在 `finally` 块中关闭MCP客户端连接，避免资源泄漏

**示例**：
```java
@GetMapping("/my-tool")
public ResponseEntity<String> callMyTool(@RequestParam String param) {
    McpClient mcpClient = null;
    try {
        mcpClient = initSseClient("http://localhost:8070/sse");
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(chatModel)
                .toolProvider(McpToolProvider.builder().mcpClients(mcpClient).build())
                .build();
        String response = assistant.chat("使用工具处理：" + param);
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.ok("{\"error\":\"" + e.getMessage() + "\"}");
    } finally {
        if (mcpClient != null) {
            mcpClient.close();
        }
    }
}
```

### 自定义配置

可以通过修改 `application.yml` 来调整：
- Ollama服务配置
  - 服务地址（`ollama.base-url`）
  - 使用的模型（`ollama.model`）
  - 超时时间（`ollama.timeout`，单位：秒）
- MCP客户端配置（在代码中）
  - SSE服务器地址（默认：http://localhost:8070/sse）
  - 连接超时时间（默认：10秒）
  - 工具执行超时时间（默认：10秒）
- 日志级别和格式
- 服务器端口（默认8080）

**注意**: 
- 日志配置中的package路径为 `com.cnblogs.yjmyzz.langchain4j.study`
- 修改配置后需要重启应用才能生效
- MCP客户端连接需要在每次请求时创建，使用完毕后必须关闭
- SSE服务器地址可以在代码中自定义，默认使用 `http://localhost:8070/sse`

## 🐛 故障排除

### 常见问题

1. **Ollama连接失败**
   - 确保Ollama服务已启动：`ollama serve`
   - 检查端口11434是否被占用
   - 验证模型是否已下载：`ollama list`

2. **MCP连接失败**
   - 确保SSE服务器已启动（默认地址：http://localhost:8070/sse）
   - 检查SSE服务器是否可访问：`curl http://localhost:8070/sse`
   - 验证网络连接是否正常
   - 检查SSE服务器端口是否被占用
   - 查看应用日志中的MCP连接错误信息

3. **模型响应缓慢**
   - 检查硬件资源（CPU、内存）
   - 考虑使用更小的模型
   - 调整超时配置
   - 对于本地模型，考虑使用GPU加速

4. **内存不足**
   - 增加JVM堆内存：`-Xmx4g`
   - 使用更小的模型
   - 优化批处理大小

5. **MCP工具调用失败**
   - 检查SSE服务器是否正常运行
   - 验证MCP客户端连接是否成功建立（查看日志中的连接信息）
   - 确认工具在SSE服务器端是否正确注册和暴露
   - 查看日志中的工具调用请求和响应详情
   - 检查超时设置是否合理（默认10秒，可根据实际情况调整）
   - 确认AI模型能够正确理解工具的功能描述

6. **Java 25 编译问题**
   - 项目使用 Java 25，确保已安装 JDK 25
   - 如果遇到 Lombok 相关编译错误，项目已移除 Lombok 注解处理器的使用
   - 所有日志记录使用标准的 SLF4J Logger，不依赖 Lombok

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
- [Ollama官网](https://ollama.ai/) - 本地大语言模型运行环境
- [MCP协议文档](https://modelcontextprotocol.io/) - Model Context Protocol 官方文档

## ⚠️ 重要说明

### Java 25 兼容性

项目使用 Java 25 进行开发。由于 Java 25 是较新的版本，某些工具可能尚未完全支持：

- **Lombok**: 当前版本的 Lombok 与 Java 25 存在兼容性问题，项目已移除 Lombok 注解处理器的使用
- 所有日志记录使用标准的 SLF4J Logger，不依赖 Lombok 的 `@Slf4j` 注解
- 如果遇到编译问题，请确保使用 JDK 25

### MCP工具调用说明

项目演示了如何使用 LangChain4j MCP 实现 AI 工具调用功能：

1. **MCP客户端**: 使用 `DefaultMcpClient` 连接到SSE服务器，支持HTTP传输层
2. **工具提供者**: 使用 `McpToolProvider` 将MCP工具提供给AI模型
3. **AI助手**: 使用 `AiServices` 构建AI助手，简化工具调用流程
4. **智能调用**: AI模型可以理解工具的功能描述，并根据用户请求自动调用相应的远程工具
5. **连接管理**: 每次请求创建新的MCP客户端连接，使用完毕后自动关闭，确保资源正确释放

---

**注意**: 请确保在使用前已正确安装和配置Ollama服务，以及启动SSE服务器（如果使用MCP功能）。
