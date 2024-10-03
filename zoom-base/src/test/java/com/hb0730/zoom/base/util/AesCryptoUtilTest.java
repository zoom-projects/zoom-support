package com.hb0730.zoom.base.util;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import com.hb0730.zoom.base.utils.AesCryptoUtil;
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

    @Test
    void testDecrypt() {
        String iv = "NklZqaGgkvy7BH69";
        String timestamp = "1727854248033";
        String data = "e82de9d32d3b1a462c2ddfc9dd270182";

        String key = SecureUtil.sha256(iv + timestamp);
        byte[] _key = HexUtil.decodeHex(key);
        byte[] _iv = iv.getBytes();
        String decrypt = AesCryptoUtil.decrypt(data, AesCryptoUtil.mode, _key, _iv);
        System.out.println(decrypt);


//        System.out.println(decrypt);
    }
}
