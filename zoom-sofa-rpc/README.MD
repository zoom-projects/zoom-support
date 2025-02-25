# zoom-sofa-rpc

针对[sofa-rpc](https://github.com/sofastack/sofa-rpc)的扩展，便于项目的使用

## 配置

启动配置主要是`sofa`和`zoom`的配置`application.yml`或者`application.properties`

```yaml
sofa:
  boot:
    rpc:
      #      registry-address: local://${user.home}/.sofa-boot/registry/zoom.reg
      registry-address: nacos://127.0.0.1:8848/2a5c96be-97e5-4e36-99c6-18915867ab21
      virtual-host: 127.0.0.1
      enable-auto-publish: true
      bolt-port: 12200
zoom:
  sofa:
    rpc:
      load-type: NACOS | LOCAL # 加载类型 NACOS或者LOCAL
      # 如果配置的是LOCAL,则需要配置文件名称为`zoom-app-${profile}.properties` 或者 `zoom-app.properties`
      # 如果配置的是NACOS,则需要配置nacos的配置，且配置文件类型为`properties`
      nacos: # nacos配置
        server-addr: localhost:8848
        namespace: 2a5c96be-97e5-4e36-99c6-18915867ab21
        group: DEFAULT_GROUP
        data-id: zoom-sofa-rpc
      scan-rpc-service: classpath*:com/hb0730/**/remote/**/*RpcService.class # 扫描Rpc接口
```

`zoom-app-${profile}.properties` 或者 `zoom-app.properties`配置文件，放在`resources`目录下

```properties
# 格式为 服务名称.name=服务提供者的应用名,服务名称.address=服务提供者的地址
demo-app.name=demo-app
# 服务提供者的地址 如果启用，则使用直连模式
#demo-app.address=localhost:12200
```

## 注解

1. `@RemoteService` 标记为服务提供者,每一个服务提供者都需要标记
2. `@RpcAppName` 服务消费者使用，标记服务提供者的应用名,作用于接口
3. `@RpcPkg` 服务消费者使用，标记服务提供者的应用名,作用于包
4. `RpcApi` 标记为Rpc接口服务

## 如何使用

### 方式一,使用`@RpcAppName`注解

```java
// demo-app 则是服务提供者的应用名
@RpcAppName("demo-app")
public interface SayHelloService {
    String sayHello(String name);
}
```

服务提供者

```java
import com.hb0730.zoom.sofa.rpc.core.annotation.RemoteService;

@RemoteService // 标记为服务提供者
public class SayHelloServiceImpl implements SayHelloService {
    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }
}
```

服务消费者

```java
import com.hb0730.zoom.sofa.rpc.core.RpcApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SayHelloCustomerService implements SayHelloService, RpcApi {
    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }
}
```

### 方式二 使用`@RpcPkg`注解

使用`package-info.java`文件

```java
@com.hb0730.zoom.sofa.rpc.core.annotation.RpcPkg("demo-app")
package com.hb0730.zoom.sofa.rpc.demo;
```

```java
public interface SayHelloService {
    String sayHello(String name);
}
```

服务提供者

```java
import com.hb0730.zoom.sofa.rpc.core.annotation.RemoteService;

@RemoteService // 标记为服务提供者
public class SayHelloServiceImpl implements SayHelloService {
    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }
}
```

服务消费者

```java
import com.hb0730.zoom.sofa.rpc.core.RpcApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SayHelloCustomerService implements SayHelloService, RpcApi {
    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }
}
```

## 关于`@RpcAppName`和`@RpcPkg`的区别

`@RpcAppName`是在接口上使用，`@RpcPkg`是在包上使用，

```java

@RpcAppName("demo-app")
public interface SayHelloService {
    String sayHello(String name);
}
```

```java
@com.hb0730.zoom.sofa.rpc.core.annotation.RpcPkg("demo-app")
package com.hb0730.zoom.sofa.rpc.demo;
```


