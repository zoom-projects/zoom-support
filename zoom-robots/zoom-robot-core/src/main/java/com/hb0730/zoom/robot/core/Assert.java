package com.hb0730.zoom.robot.core;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/4/11
 */
public class Assert {
    /**
     * Assert that the given string is not blank; otherwise, throw a {@link RobotException}
     *
     * @param str     the string to check
     * @param message the exception message to use if the assertion fails
     * @return the validated string
     * @throws RobotException if the string is blank
     */
    public static String isNotBlank(String str, String message) {
        if (str == null || str.isEmpty()) {
            throw new RobotException(message);
        }
        return str;
    }
}
