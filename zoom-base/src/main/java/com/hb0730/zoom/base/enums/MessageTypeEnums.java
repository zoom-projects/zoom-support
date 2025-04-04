package com.hb0730.zoom.base.enums;

import com.hb0730.zoom.base.Pair;
import com.hb0730.zoom.base.PairEnum;
import lombok.Getter;

/**
 * 消息类型
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/18
 */
public enum MessageTypeEnums implements PairEnum<String, Pair<String, String>> {
    SMS(new Pair<>("SMS", "短信"), MessageContentTypeEnums.TEXT),
    EMAIL(new Pair<>("EMAIL", "邮箱"), MessageContentTypeEnums.HTML),
    SITE(new Pair<>("SYS", "站内消息"), MessageContentTypeEnums.TEXT),
    ;
    private final Pair<String, String> status;
    @Getter
    private final MessageContentTypeEnums contentType;

    MessageTypeEnums(Pair<String, String> status, MessageContentTypeEnums contentType) {
        this.status = status;
        this.contentType = contentType;
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

    /**
     * 根据code获取枚举
     *
     * @param code code
     * @return {@link MessageTypeEnums}
     */
    public static MessageTypeEnums of(String code) {
        for (MessageTypeEnums value : MessageTypeEnums.values()) {
            if (value.getCode().equals(code.toUpperCase())) {
                return value;
            }
        }
        return null;
    }

}
