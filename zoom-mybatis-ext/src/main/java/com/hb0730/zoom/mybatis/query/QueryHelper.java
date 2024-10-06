package com.hb0730.zoom.mybatis.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hb0730.zoom.base.exception.ZoomException;
import com.hb0730.zoom.base.utils.CollectionUtil;
import com.hb0730.zoom.base.utils.ReflectUtil;
import com.hb0730.zoom.base.utils.StrUtil;
import com.hb0730.zoom.mybatis.query.bean.Pair;
import com.hb0730.zoom.mybatis.query.core.AbstractQueryHandler;
import com.hb0730.zoom.mybatis.query.core.impl.EqualsQueryHandler;
import com.hb0730.zoom.mybatis.query.core.impl.GreaterThanEqualQueryHandler;
import com.hb0730.zoom.mybatis.query.core.impl.GreaterThanQueryHandler;
import com.hb0730.zoom.mybatis.query.core.impl.IsNullQueryHandler;
import com.hb0730.zoom.mybatis.query.core.impl.LessThanEqualQueryHandler;
import com.hb0730.zoom.mybatis.query.core.impl.LessThanQueryHandler;
import com.hb0730.zoom.mybatis.query.core.impl.LikeLeftQueryHandler;
import com.hb0730.zoom.mybatis.query.core.impl.LikeQueryHandler;
import com.hb0730.zoom.mybatis.query.core.impl.LikeRightQueryHandler;
import com.hb0730.zoom.mybatis.query.doamin.PageParams;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询帮助类
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/5
 */
public class QueryHelper {
    private static final Map<Class<?>, AbstractQueryHandler> QUERY_HANDLER_MAP = new HashMap<>(16);


    static {
        // 注册QueryHandler
        register(new EqualsQueryHandler());
        register(new LikeQueryHandler());
        register(new LikeLeftQueryHandler());
        register(new LikeRightQueryHandler());
        register(new GreaterThanQueryHandler());
        register(new GreaterThanEqualQueryHandler());
        register(new LessThanQueryHandler());
        register(new LessThanEqualQueryHandler());
        register(new IsNullQueryHandler());
    }

    /**
     * 注册QueryHandler
     *
     * @param queryHandler queryHandler
     */
    public static void register(AbstractQueryHandler queryHandler) {
        QUERY_HANDLER_MAP.put(queryHandler.getAnnotation(), queryHandler);
    }

    /**
     * 创建QueryWrapper
     *
     * @param bean bean
     * @param <T>  泛型
     * @return QueryWrapper
     */
    public static <T> QueryWrapper<T> ofBean(Object bean) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        Field[] fields = ReflectUtil.getFields(bean.getClass());
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            if (null == annotations) {
                continue;
            }
            for (Annotation annotation : annotations) {
                AbstractQueryHandler queryHandler = QUERY_HANDLER_MAP.get(annotation.annotationType());
                if (null != queryHandler) {
                    buildQuery(queryWrapper, bean, field, queryHandler);
                }
            }
        }
        return queryWrapper;
    }

    /**
     * 创建QueryWrapper,包含排序
     *
     * @param query query
     * @param <T>   泛型
     * @param <E>   参数范型
     * @return QueryWrapper
     */
    public static <T, E extends PageParams> QueryWrapper<T> ofBean(E query) {
        QueryWrapper<T> queryWrapper = ofBean((Object) query);
        return order(queryWrapper, query);
    }

    private static <T> void buildQuery(QueryWrapper<T> queryWrapper, Object beanParam, Field field,
                                       AbstractQueryHandler handler) {
        Class<? extends Annotation> annotationClass = handler.getAnnotation();
        Annotation annotation = field.getAnnotation(annotationClass);
        if (annotation == null) {
            return;
        }
        Pair<String, Object> pair = getFieldNameAndValue(field, beanParam, annotation);
        if (null == pair) {
            return;
        }
        handler.buildQuery(queryWrapper, pair.getLeft(), pair.getRight(), annotation);
    }

    /**
     * 获取字段名称和值
     *
     * @param field      字段
     * @param beanParam  bean
     * @param annotation 注解
     * @return Pair
     */
    private static Pair<String, Object> getFieldNameAndValue(Field field, Object beanParam, Annotation annotation) {
        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(beanParam.getClass(), field.getName());
        if (null == descriptor) {
            return null;
        }
        try {
            String column = getColumnName(field, annotation);
            return Pair.of(column, descriptor.getReadMethod().invoke(beanParam));
        } catch (ReflectiveOperationException e) {
            throw new ZoomException("构建【" + annotation.getClass().getName()
                    + "】注解的条件时，反射调用获取对应的属性字段值异常。", e);
        }
    }

    private static String getColumnName(Field field, Annotation annotation) {
        String column;
        try {
            column = (String) annotation.getClass().getMethod("value").invoke(annotation);
            column = StrUtil.isBlank(column) ? field.getName() : column;
        } catch (ReflectiveOperationException e) {
            throw new ZoomException("构建【" + annotation.getClass().getName()
                    + "】注解的条件时，反射调用获取对应的属性字段值异常。", e);
        }
        boolean underCamel = true;
        try {
            underCamel = (boolean) annotation.getClass().getMethod("underCamel").invoke(annotation);
        } catch (ReflectiveOperationException ignored) {
        }
        if (underCamel) {
            column = StrUtil.toUnderlineCase(column);
        }
        // boolean类型自动添加 is_
        if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
            column = "is_" + column;
        }
        return column;
    }

    /**
     * 排序
     *
     * @param queryWrapper queryWrapper
     * @param query        query
     * @param <T>          泛型
     * @param <Q>          参数范型
     * @return QueryWrapper
     */
    public static <T, Q extends PageParams> QueryWrapper<T> order(QueryWrapper<T> queryWrapper, Q query) {
        if (null == queryWrapper) {
            queryWrapper = new QueryWrapper<>();
        }
        List<PageParams.SortMate> sorts = query.getSorts();
        if (CollectionUtil.isNotEmpty(sorts)) {
            for (PageParams.SortMate sort : sorts) {
                // mybatis-plus关键字处理`rank`,`index`等字段
                String column = StrUtil.toUnderlineCase(sort.getField());
                column = "`" + column + "`";

                if (sort.getOrder().equalsIgnoreCase("ASC")) {
                    queryWrapper.orderByAsc(column);
                } else {
                    queryWrapper.orderByDesc(column);
                }
            }
        }
        return queryWrapper;
    }

    /**
     * 转换为分页,包含排序
     *
     * @param query query
     * @param <T>   泛型
     * @param <E>   参数范型
     * @return IPage
     */
    public static <T, E extends PageParams> IPage<T> toPage(E query) {
        Page<T> page = new Page<>(query.getCurrent(), query.getSize());
        List<PageParams.SortMate> sorts = query.getSorts();
        if (CollectionUtil.isNotEmpty(sorts)) {
            for (PageParams.SortMate sort : sorts) {
                OrderItem orderItem = new OrderItem();
                orderItem.setColumn(sort.getField());
                orderItem.setAsc(sort.getOrder().equalsIgnoreCase("ASC"));
                page.addOrder(orderItem);
            }
        }
        return page;
    }

}
