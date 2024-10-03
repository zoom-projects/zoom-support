package com.hb0730.zoom.mybatis.configure;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.hb0730.zoom.base.exception.ZoomException;
import com.hb0730.zoom.mybatis.configure.config.MybatisConfig;
import com.hb0730.zoom.mybatis.core.encrypt.AesMybatisEncryptService;
import com.hb0730.zoom.mybatis.core.encrypt.Base64MybatisEncryptService;
import com.hb0730.zoom.mybatis.core.encrypt.MybatisEncryptService;
import com.hb0730.zoom.mybatis.core.handler.EncryptTypeHandler;
import com.hb0730.zoom.mybatis.core.handler.FieldFillHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/9/24
 */
@AutoConfiguration(before = MybatisPlusAutoConfiguration.class)
@MapperScan(value = "com.hb0730.**.mapper")
@EnableTransactionManagement(proxyTargetClass = true)
@EnableConfigurationProperties(MybatisConfig.class)
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
    @ConditionalOnProperty(prefix = "zoom.mybatis", name = "optimisticLocker", havingValue = "true", matchIfMissing = true)
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
    @ConditionalOnProperty(prefix = "zoom.mybatis", name = "pagination", havingValue = "true", matchIfMissing = true)
    public MybatisPlusInterceptor mybatisPlusPaginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        return interceptor;
    }

    /**
     * 加密
     *
     * @return .
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "zoom.mybatis", name = "encrypt.enable", havingValue = "true")
    public MybatisEncryptService mybatisPlusEncryptInterceptor(MybatisConfig mybatisConfig) {
        MybatisConfig.Encrypt encrypt = mybatisConfig.getEncrypt();
        MybatisEncryptService mybatisEncryptService;
        if (MybatisConfig.Algorithm.AES.equals(encrypt.getAlgorithm())) {
            mybatisEncryptService = new AesMybatisEncryptService(
                    encrypt.getKey(),
                    encrypt.getIv()
            );
        } else if (MybatisConfig.Algorithm.BASE64.equals(encrypt.getAlgorithm())) {
            mybatisEncryptService = new Base64MybatisEncryptService();
        } else {

            throw new ZoomException("不支持的加密算法 " + encrypt.getAlgorithm());
        }
        return mybatisEncryptService;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(MybatisEncryptService.class)
    public EncryptTypeHandler encryptTypeHandler(MybatisEncryptService mybatisEncryptService) {
        return new EncryptTypeHandler(mybatisEncryptService);
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
