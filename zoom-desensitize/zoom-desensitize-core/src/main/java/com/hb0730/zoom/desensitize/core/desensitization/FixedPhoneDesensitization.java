package com.hb0730.zoom.desensitize.core.desensitization;

import com.hb0730.zoom.base.pool.RegexPool;
import com.hb0730.zoom.base.utils.DesensitizeUtil;
import jakarta.annotation.Nonnull;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/10
 */
public class FixedPhoneDesensitization extends StringDesensitization {
    @Override
    String doDesensitize(@Nonnull String original) {
        return apply(RegexPool.TEL, original, DesensitizeUtil::fixedPhone);
    }
}
