# 多租户功能指南

## 概述

本项目已升级支持多租户架构，允许不同用户使用各自的DeepSeek API密钥进行聊天，为构建大型多租户系统奠定了基础。

## 🚀 核心特性

### 1. 动态API密钥管理
- 支持运行时动态输入API密钥
- 自动缓存不同API密钥对应的模型实例
- 支持API密钥格式验证

### 2. 多租户隔离
- 每个API密钥对应独立的模型实例
- 线程安全的缓存管理
- 支持租户级别的缓存清理

### 3. 灵活的API设计
- 向后兼容原有API
- 支持可选的API密钥参数
- 提供丰富的管理接口

## 📋 API接口说明

### 聊天接口

#### 普通聊天
```bash
# 使用动态API密钥
curl "http://localhost:8080/api/chat?prompt=你好&apiKey=your-api-key"

# 不使用API密钥（需要默认配置）
curl "http://localhost:8080/api/chat?prompt=你好"
```

#### 流式聊天
```bash
# 使用动态API密钥
curl "http://localhost:8080/api/chat/stream?prompt=你好&apiKey=your-api-key"

# 不使用API密钥（需要默认配置）
curl "http://localhost:8080/api/chat/stream?prompt=你好"
```

### 管理接口

#### 验证API密钥
```bash
curl "http://localhost:8080/api/validate-api-key?apiKey=your-api-key"
```

#### 获取缓存统计
```bash
curl "http://localhost:8080/api/cache/stats"
```

#### 清除指定API密钥缓存
```bash
curl -X POST "http://localhost:8080/api/cache/clear?apiKey=your-api-key"
```

#### 清除所有缓存
```bash
curl -X POST "http://localhost:8080/api/cache/clear-all"
```

## 🏗️ 架构设计

### 核心组件

#### 1. DynamicConfigService
- **职责**: 管理动态API密钥和模型实例
- **特性**: 
  - 线程安全的缓存管理
  - 自动模型实例创建
  - API密钥验证
  - 缓存统计和清理

#### 2. ChatController
- **职责**: 提供RESTful API接口
- **特性**:
  - 支持动态API密钥参数
  - 向后兼容设计
  - 错误处理和验证
  - 多租户支持

### 缓存策略

```java
// 聊天模型缓存
ConcurrentHashMap<String, ChatModel> chatModelCache

// 流式模型缓存  
ConcurrentHashMap<String, StreamingChatModel> streamingChatModelCache
```

**优势**:
- 线程安全
- 自动去重
- 内存高效
- 支持并发访问

## 🔧 使用方式

### 1. Web界面使用

1. 访问 `http://localhost:8080/`
2. 在API密钥输入框中输入您的DeepSeek API密钥
3. 输入聊天消息
4. 选择"发送"或"流式发送"

### 2. API调用

#### JavaScript示例
```javascript
// 普通聊天
const response = await fetch(`/api/chat?prompt=${message}&apiKey=${apiKey}`);
const result = await response.text();

// 流式聊天
const response = await fetch(`/api/chat/stream?prompt=${message}&apiKey=${apiKey}`);
const reader = response.body.getReader();
// ... 处理流式响应
```

#### Java示例
```java
// 使用RestTemplate
String url = "http://localhost:8080/api/chat?prompt=你好&apiKey=your-api-key";
String response = restTemplate.getForObject(url, String.class);
```

### 3. 测试工具

使用提供的测试页面 `test-multitenant.html` 进行功能测试：

1. 打开 `test-multitenant.html` 文件
2. 测试各个API接口
3. 验证多租户功能

## 🛡️ 安全考虑

### 1. API密钥安全
- 不在日志中记录完整API密钥
- 支持密码输入框隐藏显示
- 提供API密钥格式验证

### 2. 缓存安全
- 线程安全的缓存实现
- 支持按需清理缓存
- 防止内存泄漏

### 3. 错误处理
- 详细的错误信息
- 优雅的异常处理
- 用户友好的错误提示

## 📊 性能优化

### 1. 缓存策略
- 模型实例复用
- 减少重复创建开销
- 内存使用优化

### 2. 并发处理
- 线程安全的实现
- 支持高并发访问
- 异步流式响应

### 3. 资源管理
- 自动缓存清理
- 内存使用监控
- 可配置的超时设置

## 🔮 扩展方向

### 1. 数据库集成
- 持久化API密钥存储
- 用户认证和授权
- 使用量统计和计费

### 2. 负载均衡
- 多实例部署
- 请求分发
- 故障转移

### 3. 监控和日志
- 详细的访问日志
- 性能监控
- 告警机制

### 4. 高级功能
- API密钥轮换
- 使用配额限制
- 多模型支持

## 🧪 测试指南

### 1. 单元测试
```bash
mvn test
```

### 2. 集成测试
使用 `test-multitenant.html` 进行手动测试

### 3. 压力测试
```bash
# 使用Apache Bench进行压力测试
ab -n 1000 -c 10 "http://localhost:8080/api/health"
```

## 📝 最佳实践

### 1. API密钥管理
- 使用环境变量存储生产环境API密钥
- 定期轮换API密钥
- 监控API密钥使用情况

### 2. 缓存管理
- 定期清理过期缓存
- 监控内存使用情况
- 设置合理的缓存大小限制

### 3. 错误处理
- 实现重试机制
- 记录详细错误日志
- 提供用户友好的错误信息

## 🚨 注意事项

1. **API密钥安全**: 确保API密钥不被泄露
2. **内存管理**: 定期清理不需要的缓存
3. **并发控制**: 注意高并发场景下的性能
4. **错误处理**: 实现完善的错误处理机制

## 📞 技术支持

如有问题，请查看：
- 项目GitHub Issues
- 作者博客: http://yjmyzz.cnblogs.com
- 作者: 菩提树下的杨过

---

**注意**: 本多租户功能为构建大型系统的基础，建议在生产环境中添加适当的认证、授权和监控机制。
