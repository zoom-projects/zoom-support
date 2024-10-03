package com.hb0730.zoom.base.util;

import com.hb0730.zoom.base.utils.MaskUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MaskUtilTest {

    @Test
    void chineseName() {
        String name = "张三";
        String chineseName = MaskUtil.chineseName(name);
        Assertions.assertEquals("张*", chineseName);
        name = "张三封";
        chineseName = MaskUtil.chineseName(name);
        Assertions.assertEquals("张**", chineseName);
        name = "张四五六";
        chineseName = MaskUtil.chineseName(name);
        Assertions.assertEquals("张***", chineseName);
    }

    @Test
    void idCard() {
        //15位
        String idCard = "420123456789012";
        String idCard1 = MaskUtil.idCard(idCard);
        Assertions.assertEquals("420**********9012", idCard1);
        //18位
        idCard = "420123456789012345";
        idCard1 = MaskUtil.idCard(idCard);
        Assertions.assertEquals("420*************2345", idCard1);

    }

    @Test
    void fixedPhone() {
        String phone = "12345678901";
        String fixedPhone = MaskUtil.fixedPhone(phone);
        Assertions.assertEquals("*******8901", fixedPhone);
    }

    @Test
    void mobile() {
        String mobile = "12345678901";
        String mobile1 = MaskUtil.mobile(mobile);
        Assertions.assertEquals("123****8901", mobile1);

        mobile = "1234567890";
        mobile1 = MaskUtil.mobile(mobile);
        Assertions.assertEquals("123***7890", mobile1);
    }

    @Test
    void address() {
        String address = "北京市海淀区中关村";
        String address1 = MaskUtil.address(address, 3);
        Assertions.assertEquals("北京市海淀区***", address1);
    }

    @Test
    void email() {
        String email = "123@qq.com";
        String email1 = MaskUtil.email(email);
        Assertions.assertEquals("1**@qq.com", email1);
    }

    @Test
    void bankCard() {
        String bankCard = "1234567890123456";
        String bankCard1 = MaskUtil.bankCard(bankCard);
        Assertions.assertEquals("123456******3456", bankCard1);
    }

    @Test
    void apiSecret() {
        String apiSecret = "1234567890123456";
        String apiSecret1 = MaskUtil.apiSecret(apiSecret);
        Assertions.assertEquals("123**********456", apiSecret1);
    }
}
