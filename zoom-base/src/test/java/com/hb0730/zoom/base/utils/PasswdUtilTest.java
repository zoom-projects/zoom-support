package com.hb0730.zoom.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class PasswdUtilTest {

    @Test
    void checkPwd() {
        String pwd = PasswdUtil.generatePwd(6);
        log.info("pwd:{}", pwd);
        assertTrue(PasswdUtil.checkPwd(pwd));
    }

    @Test
    void generatePwd() {
        String pwd = PasswdUtil.generatePwd(6);
        log.info("pwd:{}", pwd);
        assertTrue(pwd.length() == 6);
    }
}
