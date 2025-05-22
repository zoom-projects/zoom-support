# ZOOM SUPPORT

为[ZOOM](https://github.com/zoom-projects)提供统一的支持

1. `zoom-base` 提供基础支持，包含基本的工具类、异常类、常量类等
2. `zoom-cache` 提供缓存支持,基于`spring-data-redis`的缓存支持
3. `zoom-desensitize` 提供数据脱敏支持，包含常用的脱敏策略&`spring-web`中`jackson` 序列化脱敏支持
4. `zoom-mail` 提供邮件支持，基于`jakarta.mail`的邮件发送支持
5. `zoom-mybatis` 基于[Mybatis-Plus](https://github.com/baomidou/mybatis-plus)扩展。提供`数据加密`,`数据填充`等支持
6. `zoom-mybatis-ext` 基于[Mybatis-Plus](https://github.com/baomidou/mybatis-plus)扩展,通过`注解`的方式通过SQL查询数据
7. `zoom-opentelemetry` 提供`opentelemetry`支持& 针对[sofa-rpc](https://github.com/sofastack/sofa-rpc)的追踪支持
8. `zoom-operator-log` 提供操作日志支持，基于`spring-aop`的操作日志记录支持
9. `zoom-oss` 提供对象存储支持，支持`S3`等
10. `zoom-poi` 提供`POI`支持，支持`Excel`等
11. `zoom-rabbitmq` 提供`RabbitMQ`支持，包含`消息发送`、`接收`、`重试`等支持
12. `zoom-robots` 提供`企业微信`、`钉钉`等自定义机器人消息发送的支持
13. `zoom-security` 提供安全支持，包含`XSS`、`CSRF`、`SQL注入`等支持
14. `zoom-social` 提供社交支持，包含`QQ`、`微信`等支持
15. `zoom-sofa-rpc` 提供[sofa-rpc](https://github.com/sofastack/sofa-rpc)支持
16. `zoom-spring-retry` 提供`spring-retry`支持
17. `zoom-springdoc` 提供`springdoc`支持，支持`swagger`等
18. `zoom-web` 处理`全局异常`、`跨域` 等配置

