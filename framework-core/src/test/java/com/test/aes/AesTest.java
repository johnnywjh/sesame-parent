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
    public static void main(String[] args) {
        String content = "test中文";

// 随机生成密钥
//        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
//        System.out.println(new String(key));

        AES aes = new AES(Mode.CTS, Padding.PKCS5Padding,
                // 密钥，可以自定义
                "T9HOF7pgqKkkFDHc".getBytes(),
                // iv加盐，按照实际需求添加
                "FbrRHExNNbhmvVgh".getBytes()
        );

// 加密为16进制表示
        String encryptHex = aes.encryptHex(content);
        System.out.println(encryptHex);
// 解密为字符串
        String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
        System.out.println(decryptStr);
    }
}
