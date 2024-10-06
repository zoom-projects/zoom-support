package com.hb0730.zoom.mybatis.query.core.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hb0730.zoom.mybatis.query.annotation.LessThan;
import com.hb0730.zoom.mybatis.query.core.AbstractQueryHandler;
import jakarta.annotation.Nonnull;

import java.lang.annotation.Annotation;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/5
 */
public class LessThanQueryHandler extends AbstractQueryHandler {
    @Override
    public Class<? extends Annotation> getAnnotation() {
        return LessThan.class;
    }

    @Override
    public void buildQuery(QueryWrapper<?> queryWrapper, String column, @Nonnull Object value) {
        queryWrapper.lt(column, value);
    }
}
