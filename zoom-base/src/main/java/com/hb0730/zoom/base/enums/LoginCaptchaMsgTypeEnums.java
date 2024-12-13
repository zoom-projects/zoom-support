package com.hb0730.zoom.base.enums;

import com.hb0730.zoom.base.Pair;
import com.hb0730.zoom.base.PairEnum;
import com.hb0730.zoom.base.exception.ZoomException;

/**
 * 登录验证码消息类型
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/18
 */
public enum LoginCaptchaMsgTypeEnums implements PairEnum<String, Pair<String, String>> {
    SMS(new Pair<>("SMS_1000", "短信验证码")),
    EMAIL(new Pair<>("EMAIL_1000", "邮箱验证码")),
    ;
    private final Pair<String, String> status;

    LoginCaptchaMsgTypeEnums(Pair<String, String> status) {
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

    /**
     * 获取消息类型
     *
     * @return 消息类型
     */
    public MessageTypeEnums getMsgType() {
        if (SMS.equals(this)) {
            return MessageTypeEnums.SMS;
        } else if (EMAIL.equals(this)) {
            return MessageTypeEnums.EMAIL;
        }
        throw new ZoomException("不支持的消息类型");
    }


    public static LoginCaptchaMsgTypeEnums of(String type) {
        if (LoginGrantEnums.MOBILE.getCode().equals(type)) {
            return SMS;
        } else if (LoginGrantEnums.EMAIL.getCode().equals(type)) {
            return EMAIL;
        }
        throw new ZoomException("不支持的验证码消息类型");
    }
}
