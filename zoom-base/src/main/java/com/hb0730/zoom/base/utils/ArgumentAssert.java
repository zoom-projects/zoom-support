package com.hb0730.zoom.base.utils;

import com.hb0730.zoom.base.exception.ZoomException;

import java.util.function.Supplier;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/5
 */
public class ArgumentAssert {

    /**
     * 判断是否为空
     *
     * @param Object   对象
     * @param supplier 异常
     * @param <T>      对象
     * @param <X>      异常
     * @return 对象
     * @throws X 异常
     */
    public static <T, X extends Throwable> T notNull(T Object, Supplier<X> supplier) throws X {
        if (Object == null) {
            throw supplier.get();
        }
        return Object;
    }

    /**
     * 判断是否为空
     *
     * @param Object  对象
     * @param message 异常信息
     * @param <T>     对象
     * @return 对象
     * @throws ZoomException 异常
     */
    public static <T> T notNull(T Object, String message) {
        return notNull(Object, () -> new ZoomException(message));
    }

    /**
     * 判断是否为空
     *
     * @param Oject       对象
     * @param templateMsg 模板信息
     * @param args        参数
     * @param <T>         对象
     * @return 对象
     * @throws ZoomException 异常
     */
    public static <T> T notNull(T Oject, String templateMsg, Object... args) {
        return notNull(Oject, String.format(templateMsg, args));
    }


    /**
     * 判断是否为空
     *
     * @param Oject    对象
     * @param supplier 异常
     * @param <T>      对象
     * @param <X>      异常
     * @return 对象
     * @throws X 异常
     */
    public static <T extends CharSequence, X extends Throwable> T notEmpty(T Oject, Supplier<X> supplier) throws X {
        if (StrUtil.isEmpty(Oject)) {
            throw supplier.get();
        }
        return Oject;
    }

    /**
     * 判断是否为空
     *
     * @param Oject   对象
     * @param message 异常信息
     * @param <T>     对象
     * @return 对象
     * @throws ZoomException 异常
     */
    public static <T extends CharSequence> T notEmpty(T Oject, String message) {
        return notEmpty(Oject, () -> new ZoomException(message));
    }

    /**
     * 判断是否为空
     *
     * @param Oject       对象
     * @param templateMsg 模板信息
     * @param args        参数
     * @param <T>         对象
     * @return 对象
     * @throws ZoomException 异常
     */
    public static <T extends CharSequence> T notEmpty(T Oject, String templateMsg, Object... args) {
        return notEmpty(Oject, String.format(templateMsg, args));
    }

    /**
     * 判断是否为空
     *
     * @param Oject    对象
     * @param supplier ��常
     * @param <T>      对象
     * @param <X>      异常
     * @return 对象
     * @throws X 异常
     */
    public static <T extends CharSequence, X extends Throwable> T notBlank(T Oject, Supplier<X> supplier) throws X {
        if (StrUtil.isBlank(Oject)) {
            throw supplier.get();
        }
        return Oject;
    }

    /**
     * 判断是否为空
     *
     * @param Oject   对象
     * @param message ��常信息
     * @param <T>     对象
     * @return 对象
     * @throws ZoomException 异常
     */
    public static <T extends CharSequence> T notBlank(T Oject, String message) {
        return notBlank(Oject, () -> new ZoomException(message));
    }

    /**
     * 判断是否为空
     *
     * @param Oject       对象
     * @param templateMsg 模板信息
     * @param args        参数
     * @param <T>         对象
     * @return 对象
     * @throws ZoomException 异常
     */
    public static <T extends CharSequence> T notBlank(T Oject, String templateMsg, Object... args) {
        return notBlank(Oject, String.format(templateMsg, args));
    }

    
}
