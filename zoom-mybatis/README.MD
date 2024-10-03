# zoom-mybatis

对mybatis-plus相关配置的封装，提供了一些基础的功能，方便使用

## 配置

```yaml
zoom:
  mybatis:
    optimisticLocker: true // 是否开启乐观锁
    interceptor: true // 是否开启拦截器
    encrypt:
      enabled: false // 是否开启加密解密
      algorithm: AES // 加密算法 支持 AES/CBC/PKCS5Padding BASE64
      key: 1234567890123456 // 加密密钥 只有AES算法需要
      iv: 1234567890123456 // 加密向量 只有AES算法需要
```

## 自动装配BEAN

1. 自动填充字段 `@TableField(fill = FieldFill.INSERT)` `@TableField(fill = FieldFill.INSERT_UPDATE)`
2. 乐观锁 `@Version`
3. 拦截器 `PaginationInterceptor`
4. 加密解密 `@TableField(typeHandler = EncryptTypeHandler.class)`
