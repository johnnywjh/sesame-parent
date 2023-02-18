package com.test.aes;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.junit.jupiter.api.Test;

public class AesTest {

    @Test
    public void aes() {
        String content = "test中文";

// 随机生成密钥
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();

        // 构建
//        AES aes = SecureUtil.aes(key);
        AES aes = new AES(
                Mode.CTS, Padding.PKCS5Padding,
                "0CoJUm6Qyw8W8ju1".getBytes(),
                "0CoJUm6Qyw8W8jud".getBytes()
        );

        // 加密
        byte[] encrypt = aes.encrypt(content);

        // 解密
        byte[] decrypt = aes.decrypt(encrypt);

        System.out.println("------------------");
        // 加密为16进制表示
        String encryptHex = aes.encryptHex(content);
        System.out.println(encryptHex);
        System.out.println(encryptHex.length());
        // 解密为字符串
        String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
        System.out.println(decryptStr);
    }
}
