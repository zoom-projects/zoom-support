package com.hb0730.zoom.mybatis.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.hb0730.zoom.base.security.SecurityUtils;
import com.hb0730.zoom.base.security.UserInfo;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 字段填充
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/9/24
 */
public class FieldFillHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        fill(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        fill(metaObject);
    }

    private void fill(MetaObject metaObject) {
        Date date = new Date();
        UserInfo userInfo = SecurityUtils.getLoginUser().orElse(new UserInfo());
        if (metaObject.hasSetter("created")) {
            setFieldValByName("created", date, metaObject);
        }
        if (metaObject.hasSetter("createdBy")) {
            setFieldValByName("createdBy", userInfo.getUsername(), metaObject);
        }
        if (metaObject.hasSetter("modified")) {
            setFieldValByName("modified", date, metaObject);
        }
        if (metaObject.hasSetter("modifiedBy")) {
            setFieldValByName("modifiedBy", userInfo.getUsername(), metaObject);
        }

    }
}
