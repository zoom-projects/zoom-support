# zoom-desensitize

用于去除zoom视频中的敏感信息，如人脸、车牌等。

## 基本用法

### 1. 引入依赖

```xml

<dependency>
    <groupId>com.github.shootercheng</groupId>
    <artifactId>zoom-desensitize-web</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 注解

在需要脱敏的字段上添加`@Desensitize`注解。

```java
public class User {

    private String name;

    @Desensitize(type = DesensitizeType.ID_CARD)
    private String idCard;
}
```

## 说明

`zoom-desensitize-core`是通过`jackson`的`serialize`和`modifier`来实现对相关字段的脱敏处理。

- `AnnotationDesensitizeSerializer`: 用于处理`@Desensitize`注解的字段
- `FieldDesensitizeSerializer`: 直接处理字段的脱敏处理
- `MapDesensitizeSerializer`: 处理`Map`类型的字段
- `DesensitizeModifier`: 通过`BeanSerializerModifier`来实现对相关字段的脱敏处理

`zoom-desensitize-web`则是通过`MappingJackson2HttpMessageConverter`来实现对`@Desensitize`注解的脱敏处理。

