package com.hb0730.zoom.base.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrFormatter;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/18
 */
public class StrFormatTest {
    @Test
    void test() {
        String value = "hell ${name}";
        Map<String, String> map = Map.of("name", "world");
        String format = StrFormatter.formatWith(value, "${}", map);
        Assert.isTrue("hell world".equals(format));
    }
}
