package com.hb0730.zoom.base.exception;

/**
 * 异常
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/9/23
 */
public class ZoomException extends RuntimeException {
    public ZoomException(String message) {
        super(message);
    }

    public ZoomException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZoomException(Throwable cause) {
        super(cause);
    }
}
