package com.hb0730.zoom.base.util;

import com.hb0730.zoom.base.utils.DesensitizeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DesensitizeUtilTest {

    @Test
    void chineseName() {
        String name = "张三";
        String chineseName = DesensitizeUtil.chineseName(name);
        Assertions.assertEquals("张*", chineseName);
        name = "张三封";
        chineseName = DesensitizeUtil.chineseName(name);
        Assertions.assertEquals("张**", chineseName);
        name = "张四五六";
        chineseName = DesensitizeUtil.chineseName(name);
        Assertions.assertEquals("张***", chineseName);
    }

    @Test
    void idCard() {
        //15位
        String idCard = "420123456789012";
        String idCard1 = DesensitizeUtil.idCard(idCard);
        Assertions.assertEquals("420**********9012", idCard1);
        //18位
        idCard = "420123456789012345";
        idCard1 = DesensitizeUtil.idCard(idCard);
        Assertions.assertEquals("420*************2345", idCard1);

    }

    @Test
    void fixedPhone() {
        String phone = "12345678901";
        String fixedPhone = DesensitizeUtil.fixedPhone(phone);
        Assertions.assertEquals("*******8901", fixedPhone);
    }

    @Test
    void mobile() {
        String mobile = "12345678901";
        String mobile1 = DesensitizeUtil.mobile(mobile);
        Assertions.assertEquals("123****8901", mobile1);

        mobile = "1234567890";
        mobile1 = DesensitizeUtil.mobile(mobile);
        Assertions.assertEquals("123***7890", mobile1);
    }

    @Test
    void address() {
        String address = "北京市海淀区中关村";
        String address1 = DesensitizeUtil.address(address, 3);
        Assertions.assertEquals("北京市海淀区***", address1);
    }

    @Test
    void email() {
        String email = "123@qq.com";
        String email1 = DesensitizeUtil.email(email);
        Assertions.assertEquals("1**@qq.com", email1);
    }

    @Test
    void bankCard() {
        String bankCard = "1234567890123456";
        String bankCard1 = DesensitizeUtil.bankCard(bankCard);
        Assertions.assertEquals("123456******3456", bankCard1);
    }

    @Test
    void apiSecret() {
        String apiSecret = "1234567890123456";
        String apiSecret1 = DesensitizeUtil.apiSecret(apiSecret);
        Assertions.assertEquals("123**********456", apiSecret1);
    }
}
