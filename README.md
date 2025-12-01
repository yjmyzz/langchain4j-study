# langchain4j Study - RAG示例

这是一个用于学习langchain4j的Spring Boot项目，集成了本地Ollama服务，演示了RAG（检索增强生成）和Embedding向量存储功能。

**Package**: `com.cnblogs.yjmyzz.langchain4j.study`

## 🚀 项目特性

- **Java 25**: 使用最新的Java版本
- **Spring Boot 4.0.0**: 现代化的Spring Boot框架
- **LangChain4j 1.8.0**: 强大的Java AI框架
- **Ollama集成**: 支持本地大语言模型（默认使用deepseek-v3.1:671b-cloud）
- **RAG支持**: 支持检索增强生成（Retrieval-Augmented Generation）
- **Embedding模型**: 集成Ollama Embedding模型（默认使用nomic-embed-text:latest）
- **向量存储**: 支持InMemory向量存储和语义搜索
- **RESTful API**: 提供完整的RAG功能API接口（向量存储、语义搜索、RAG聊天）

## 📋 前置要求

1. **Java 25**: 确保已安装JDK 25
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
# 下载聊天模型（默认模型）
ollama pull deepseek-v3.1:671b-cloud

# 下载Embedding模型（用于向量化）
ollama pull nomic-embed-text:latest

# 或者下载其他模型
ollama pull qwen3:0.6b
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

#### RAG功能演示

##### 1. 向量存储（Embedding）

```bash
# 将文本片段转换为向量并存储到内存
curl "http://localhost:8080/api/rag/embed/memory"
```

**功能说明**：
- 使用Ollama Embedding模型（nomic-embed-text）将文本转换为向量
- 将文本片段存储到内存向量数据库（InMemoryEmbeddingStore）
- 示例中包含两个文本片段："I like football." 和 "The weather is good today."

##### 2. 语义搜索（Query）

```bash
# 根据查询问题在向量数据库中搜索最相关的文本片段
curl "http://localhost:8080/api/rag/query/memory?query=What%20is%20your%20favourite%20sport?"
```

**功能说明**：
- 将查询问题转换为向量
- 在向量数据库中进行语义搜索
- 返回相似度分数和匹配的文本内容
- 默认查询："What is your favourite sport?"

##### 3. RAG聊天（Bot）

```bash
# 基于RAG的AI聊天，自动检索相关上下文并生成回答
curl "http://localhost:8080/api/rag/query/bot?query=What%20is%20your%20favourite%20sport?"
```

**功能说明**：
- 使用 `EmbeddingStoreContentRetriever` 自动检索相关上下文
- 将检索到的上下文与用户问题一起发送给AI模型
- AI基于检索到的上下文生成更准确的回答
- 支持对话记忆（MessageWindowChatMemory）
- 演示完整的RAG（检索增强生成）工作流程

## ⚙️ 配置说明

项目配置文件位于 `src/main/resources/application.yml`：

```yaml
# 服务器配置
server:
  port: 8080
  servlet:
    context-path: /

# Spring应用配置
spring:
  application:
    name: langchain4j-study
  
  # 日志配置
  logging:
    level:
      com.example.langchain4jstudy: DEBUG
      dev.langchain4j: DEBUG
    pattern:
      console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"

# Ollama配置
ollama:
  base-url: http://localhost:11434          # Ollama服务地址
  model: deepseek-v3.1:671b-cloud           # 聊天模型名称
  embedding-model: nomic-embed-text:latest  # Embedding模型名称
  timeout: 60                               # 请求超时时间（秒）

# 应用信息
info:
  app:
    name: langchain4j Study
    version: 1.0.0
    description: langchain4j学习项目 - RAG示例
```

## 📁 项目结构

```
src/
├── main/
│   ├── java/com/cnblogs/yjmyzz/langchain4j/study/
│   │   ├── LongChain4jStudyApplication.java    # 主启动类
│   │   ├── config/
│   │   │   └── OllamaConfig.java              # Ollama配置类
│   │   └── controller/
│   │       └── RAGController.java             # RAG功能控制器
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
- 配置Ollama聊天模型、流式聊天模型和Embedding模型
- 支持自定义模型名称、Embedding模型名称、服务地址和超时时间
- 启用请求和响应日志记录
- 使用 `@Bean` 注解注册为Spring Bean，支持依赖注入
- Bean名称：
  - `ollamaChatModel` - 聊天模型
  - `ollamaStreamingChatModel` - 流式聊天模型
  - `ollamaEmbeddingModel` - Embedding模型

### 2. 控制器

#### RAGController.java
- 提供RAG（检索增强生成）功能演示
- 实现InMemory向量存储功能（`InMemoryEmbeddingStore`）
- 集成Ollama Embedding模型进行文本向量化
- 支持语义搜索和相似度匹配
- 提供三个API接口：
  - `/api/rag/embed/memory` - 向量存储接口
  - `/api/rag/query/memory` - 语义搜索接口
  - `/api/rag/query/bot` - RAG聊天接口
- 使用 `EmbeddingStoreContentRetriever` 实现内容检索
- 使用 `AiServices` 构建RAG助手，自动集成检索功能
- 支持CORS跨域请求
- 返回相似度分数和匹配文本

### 3. 主要依赖
- **Spring Boot Web**: Web应用支持
- **Spring Boot Validation**: 数据验证支持
- **Spring WebFlux**: 响应式编程支持
- **LangChain4j**: AI框架核心（版本 1.8.0）
- **LangChain4j Ollama**: Ollama集成（包含聊天模型和Embedding模型支持）
- **Lombok**: 代码简化工具（可选依赖）

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

### 添加新的RAG功能

1. 在 `RAGController` 中添加新的端点方法
2. 注入 `OllamaEmbeddingModel` 和 `OllamaChatModel`（已配置为Spring Bean）
3. 使用 `EmbeddingModel` 将文本转换为向量
4. 将向量和文本片段存储到 `InMemoryEmbeddingStore`
5. 使用查询向量进行语义搜索
6. 返回匹配结果和相似度分数

**示例**：
```java
@Autowired
@Qualifier("ollamaEmbeddingModel")
OllamaEmbeddingModel embeddingModel;

@GetMapping("/search")
public ResponseEntity<String> semanticSearch(@RequestParam String query) {
    try {
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        
        // 添加文本片段
        TextSegment segment = TextSegment.from("Your text here");
        Embedding embedding = embeddingModel.embed(segment).content();
        embeddingStore.add(embedding, segment);
        
        // 语义搜索
        Embedding queryEmbedding = embeddingModel.embed(query).content();
        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(1)
                .build();
        List<EmbeddingMatch<TextSegment>> matches = embeddingStore.search(request).matches();
        
        return ResponseEntity.ok(matches.toString());
    } catch (Exception e) {
        return ResponseEntity.ok("{\"error\":\"" + e.getMessage() + "\"}");
    }
}
```

### 实现完整的RAG聊天

使用 `AiServices` 和 `EmbeddingStoreContentRetriever` 实现完整的RAG功能：

```java
ContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
        .embeddingStore(embeddingStore)
        .embeddingModel(embeddingModel)
        .maxResults(3)      // 最多返回3个相关片段
        .minScore(0.6)      // 最小相似度分数
        .build();

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .contentRetriever(retriever)  // 自动集成检索功能
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .build();
```

### 自定义配置

可以通过修改 `application.yml` 来调整：
- Ollama服务配置
    - 服务地址（`ollama.base-url`）
    - 聊天模型（`ollama.model`，默认：deepseek-v3.1:671b-cloud）
    - Embedding模型（`ollama.embedding-model`，默认：nomic-embed-text:latest）
    - 超时时间（`ollama.timeout`，单位：秒）
- 日志级别和格式
- 服务器端口（默认8080）

**注意**:
- 日志配置中的package路径为 `com.example.langchain4jstudy`
- 修改配置后需要重启应用才能生效
- Embedding模型需要在Ollama中提前下载：`ollama pull nomic-embed-text:latest`

## 🐛 故障排除

### 常见问题

1. **Ollama连接失败**
    - 确保Ollama服务已启动：`ollama serve`
    - 检查端口11434是否被占用
    - 验证模型是否已下载：`ollama list`
    - 确认使用的模型名称正确（默认：deepseek-v3.1:671b-cloud）

2. **Embedding模型加载失败**
   - 确保已在Ollama中下载Embedding模型：`ollama pull nomic-embed-text:latest`
   - 检查Ollama服务是否正常运行
   - 验证模型名称是否正确（默认：nomic-embed-text:latest）
   - 查看日志中的模型加载错误信息

3. **模型响应缓慢**
    - 检查硬件资源（CPU、内存）
    - 考虑使用更小的模型
    - 调整超时配置
    - 对于本地模型，考虑使用GPU加速

4. **内存不足**
    - 增加JVM堆内存：`-Xmx4g`
    - 使用更小的模型
    - 减少向量数据库中存储的文本片段数量

5. **向量搜索结果不准确**
    - 调整 `maxResults` 参数获取更多候选结果
    - 检查相似度分数阈值是否合理
    - 优化文本预处理和分段策略
    - 考虑使用更强大的Embedding模型

6. **Java 25 兼容性**
    - 项目使用 Java 25，确保已安装 JDK 25
    - Maven编译器插件设置为Java 25
    - Lombok为可选依赖，打包时会被排除

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

项目使用 Java 25 和 Spring Boot 4.0.0 进行开发：

- **Java 25**: 确保已安装 JDK 25
- **Maven配置**: 编译器源码和目标版本都设置为25
- **Lombok**: 作为可选依赖，打包时会被排除
- 所有日志记录使用标准的 SLF4J Logger

### RAG功能说明

项目演示了如何使用 LangChain4j 实现 RAG（检索增强生成）功能：

1. **Embedding模型**: 使用 Ollama Embedding模型（nomic-embed-text）将文本转换为向量
2. **向量存储**: 使用 `InMemoryEmbeddingStore` 存储文本向量和元数据
3. **语义搜索**: 根据查询问题的语义相似度检索相关文本片段
4. **相似度计算**: 返回匹配文本和相似度分数（0-1之间）
5. **内容检索器**: 使用 `EmbeddingStoreContentRetriever` 自动检索相关上下文
6. **AI集成**: 使用 `AiServices` 将检索到的上下文与用户问题一起发送给AI模型
7. **扩展性**: 可以轻松替换为其他向量数据库（如Pinecone、Qdrant、Chroma等）

### 技术架构

- **Spring Boot**: 提供Web服务和依赖注入
- **LangChain4j**: 提供AI集成能力
- **Ollama**: 提供本地大语言模型服务
- **Embedding**: 提供文本向量化能力

---

**注意**: 请确保在使用前已正确安装和配置Ollama服务，并下载所需的模型。
