package com.hb0730.zoom.base.util;

import org.junit.jupiter.api.Test;

class AesCryptoUtilTest {

    @org.junit.jupiter.api.Test
    void encrypt() {
        String content = "123456";
        String encrypt = AesCryptoUtil.encrypt(content);
        System.out.println(encrypt);
    }

    @Test
    void encrypt1() {
        String content = "123456";
        String key = "1234567890abcdef";
        String encrypt = AesCryptoUtil.encrypt(content, key);
        System.out.println(encrypt);
    }

    @Test
    void decrypt() {
        String content = "e12752dfb6a9a6d89c12d8f24366407c";
        String decrypt = AesCryptoUtil.decrypt(content);
        System.out.println(decrypt);
    }

    @Test
    void decrypt1() {
        String content = "e67a63b9f434013d119040dee91727b3";
        String key = "1234567890abcdef";
        String decrypt = AesCryptoUtil.decrypt(content, key);
        System.out.println(decrypt);
    }
}
