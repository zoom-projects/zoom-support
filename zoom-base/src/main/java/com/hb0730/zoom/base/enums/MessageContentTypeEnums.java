package com.hb0730.zoom.base.enums;

import com.hb0730.zoom.base.Pair;
import com.hb0730.zoom.base.PairEnum;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/2/1
 */
public enum MessageContentTypeEnums implements PairEnum<String, Pair<String, String>> {

    TEXT(new Pair<>("text", "文本")),
    HTML(new Pair<>("html", "html")),
    ;
    private final Pair<String, String> status;

    MessageContentTypeEnums(Pair<String, String> status) {
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
