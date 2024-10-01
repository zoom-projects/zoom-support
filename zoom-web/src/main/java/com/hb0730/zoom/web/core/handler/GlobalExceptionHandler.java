package com.hb0730.zoom.web.core.handler;

import com.hb0730.zoom.base.R;
import com.hb0730.zoom.base.exception.ZoomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/9/24
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * ZoomException 异常处理
     *
     * @param e 异常
     * @return {@link R}
     */
    @ExceptionHandler(ZoomException.class)
    public R<?> handlerZoomException(ZoomException e) {
        log.error("ZoomException", e);
        return R.NG(e.getMessage());
    }

    /**
     * 异常处理
     *
     * @param e 异常
     * @return {@link R}
     */
    @ExceptionHandler(Exception.class)
    public R<?> handlerException(Exception e) {
        log.error("系统异常", e);
        return R.error(500, "系统异常");
    }
}
