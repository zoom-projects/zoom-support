# Zoom opentelementry sofa rpc

针对[sofa-rpc](https://github.com/sofastack/sofa-rpc)进行trace封装,主要参数`traceId`和`spanId`的传递
参考自`ProviderTracerFilter`和`ConsumerTracerFilter`
的实现以及[OpenTelemetry](https://github.com/open-telemetry/opentelemetry-java-instrumentation)的`dubbo`,`GRPC`等
