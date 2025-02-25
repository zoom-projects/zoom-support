# zoom-cache

`zoom-cache`是基于`spring-data-redis`的缓存组件，支持项目基本的`prefix`、`expire`、`cacheName`等配置。

- 支持`spring-boot-cache`的注解
- 支持`com.hb0730.zoom.cache.core.CacheUtil`注入

## 基本用法

1. 引入依赖

```xml

<dependency>
    <groupId>com.github.shootercheng</groupId>
    <artifactId>zoom-cache</artifactId>
    <version>1.0.0</version>
</dependency>
```

2. 配置

```yaml
spring:
  redis:
    host:
    port:
    password:
    database:
zoom:
  cache:
    enabled: true
    prefix: zoom
```

3. 注解

```java

@Cacheable(cacheName = "user", key = "#id")
public User getUserById(String id) {
    return new User(id, "zoom");
}
```

or

```java
public User getUserById(String id) {
    return CacheUtils.get("user", id, () -> new User(id, "zoom"));
}
```
