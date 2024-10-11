package com.hb0730.zoom.operator.log.core.factory;

import com.hb0730.zoom.operator.log.core.annotation.Module;
import com.hb0730.zoom.operator.log.core.model.OperatorType;
import jakarta.annotation.PostConstruct;

/**
 * 操作类型初始化器
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/9
 */
public abstract class InitializingOperatorTypes implements OperatorTypeDefinition {


    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        Module moduleDefinition = this.getClass().getAnnotation(Module.class);
        if (null == moduleDefinition) {
            return;
        }
        OperatorType[] types = this.types();
        if (null == types || types.length == 0) {
            return;
        }
        // 定义类型
        String module = moduleDefinition.value();
        for (OperatorType type : types) {
            type.setModule(module);
            OperatorTypeHolder.set(type);
        }
    }
}
