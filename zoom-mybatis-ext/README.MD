# zoom-mybatis-ext

`zoom-mybatis-ext`是对`mybatis-plus`单一表查询的增强，支持`@Equals`,`@Like`,`@In`,`@Between`等注解的使用。

## 基本用法

### 1. 引入依赖

```xml

<dependency>
    <groupId>com.github.shootercheng</groupId>
    <artifactId>zoom-mybatis-ext</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 注解

`@Equals`,`@Like`,`@In`,`@Between`等注解的使用。

```java

@Data
public class UserQuery {

    @Equals
    private String name;

    @Like
    private String email;

    @In
    private List<String> ids;

    @Between
    private List<Integer> ages;
}
```

### 3. 查询

```java
UserQuery query = new UserQuery();
query.

setName("zoom");

QueryWrapper<UserQuery> queryWrapper = QueryHelper.ofBean(query);
List<User> users = userMapper.selectList(queryWrapper);
```

