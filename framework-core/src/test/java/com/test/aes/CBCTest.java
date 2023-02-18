package com.test.aes;

import kim.sesame.common.encryption.AESUtil;
import kim.sesame.common.encryption.Base64Util;
import kim.sesame.common.encryption.MD5;
import org.junit.jupiter.api.Test;

public class CBCTest {

    static String appsecret = "";

    static {
        try {
            appsecret = MD5.md5("aaa.bbb.ccc.ddd");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStr(){
        String pwd = "skajfklicwere中文";
        String pwdCbc=encodeCBC(pwd);
        System.out.println(pwdCbc);
        System.out.println(decodeCBC(pwdCbc));
    }

    public String encodeCBC(String pwd ) {
        //加密
        byte[] bytes = null;
        try {
            bytes = AESUtil.encryptAndDecrypt(pwd.getBytes("UTF-8"), appsecret, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String encode = Base64Util.encode(bytes);
        return encode;
    }
    public String decodeCBC(String encode){
        //解密
        try {
            byte[] bytes = AESUtil.encryptAndDecrypt(Base64Util.decode(encode), appsecret, 2);
            return new String(bytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
