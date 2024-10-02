package com.hb0730.zoom.mybatis.configure;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.hb0730.zoom.mybatis.core.handler.FieldFillHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/9/24
 */
@AutoConfiguration(before = MybatisPlusAutoConfiguration.class)
@MapperScan(value = "com.hb0730.**.mapper")
@EnableTransactionManagement(proxyTargetClass = true)
public class MybatisAutoConfiguration {
    /**
     * @return 字段填充元数据处理器
     */
    @Bean
    public MetaObjectHandler defaultMetaObjectHandler() {
        // 设置填充工具参数
        return new FieldFillHandler();
    }

    /**
     * 注册 乐观锁插件
     *
     * @return 拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    /**
     * 注册分页插件
     *
     * @return 拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusPaginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        return interceptor;
    }

    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor interceptor = new PaginationInnerInterceptor();
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        interceptor.setMaxLimit(-1L);
        // 分页合理化
        interceptor.setOverflow(true);
        return interceptor;
    }

}
