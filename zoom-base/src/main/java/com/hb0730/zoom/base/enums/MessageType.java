package com.hb0730.zoom.base.enums;

import com.hb0730.zoom.base.Pair;
import com.hb0730.zoom.base.PairEnum;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/18
 */
public enum MessageType implements PairEnum<String, Pair<String, String>> {
    SMS(new Pair<>("SMS", "短信")),
    EMAIL(new Pair<>("EMAIL", "邮箱"));
    private final Pair<String, String> status;

    MessageType(Pair<String, String> status) {
        this.status = status;
    }


    @Override
    public Pair<String, String> getValue() {
        return status;
    }

    @Override
    public String getCode() {
        return status.getCode();
    }

    @Override
    public String getMessage() {
        return status.getMessage();
    }
}
