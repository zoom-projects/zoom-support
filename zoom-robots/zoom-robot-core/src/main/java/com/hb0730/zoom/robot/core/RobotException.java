package com.hb0730.zoom.robot.core;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/4/11
 */
public class RobotException extends RuntimeException {
    public RobotException() {
    }

    public RobotException(String message) {
        super(message);
    }

    public RobotException(String message, Throwable cause) {
        super(message, cause);
    }

    public RobotException(Throwable cause) {
        super(cause);
    }

    public RobotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
