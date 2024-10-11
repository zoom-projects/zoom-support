package com.hb0730.zoom.desensitize.core.desensitization;

import com.hb0730.zoom.base.utils.DesensitizeUtil;
import jakarta.annotation.Nonnull;

/**
 * 身份证脱敏
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/10
 */
public class IdCardDesensitization extends StringDesensitization {
    @Override
    String doDesensitize(@Nonnull String original) {
        return apply(original, DesensitizeUtil::idCard);
    }
}
