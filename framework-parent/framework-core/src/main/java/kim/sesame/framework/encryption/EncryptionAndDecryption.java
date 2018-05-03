package kim.sesame.framework.encryption;


import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * 对称加密,解密
 */
public class EncryptionAndDecryption {

    private static byte[] keyiv = {1, 2, 3, 4, 5, 6, 7, 8};
    public final static String ENCODE = "UTF-8";

    private String keysecret;

    public EncryptionAndDecryption() {
        keysecret = "kimsesame-123456789123456789123456789";
    }

    public EncryptionAndDecryption(String keysecret) {
        this.keysecret = computeKey(keysecret);
    }

    public String getKeysecret() {
        return keysecret;
    }

    public void setKeysecret(String keysecret) {
        this.keysecret = computeKey(keysecret);
    }

    public String computeKey(String keysecret) {
        if (keysecret == null) {
            keysecret = "";
        }
        if (keysecret.length() >= 24) {
            return keysecret;
        }
        System.out.println("keysecret 的长度小于24,需要默认处理");
        int size = 24 - keysecret.length();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {
            sb.append("0");
        }
        return keysecret + sb.toString();
    }

    /**
     * CBC加密
     */
    public String encodeCBC(String data) {
        try {
            byte[] key = keysecret.getBytes(ENCODE);
            byte[] str7 = des3EncodeCBC(data.getBytes(ENCODE));
            return new String(Base64.encode(str7, Base64.NO_WRAP), ENCODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * CBC解密
     */
    public String decodeCBC(String data) {
        try {
            byte[] key = keysecret.getBytes(ENCODE);
            byte[] str8 = des3DecodeCBC(Base64.decode(data, Base64.NO_WRAP));
            return new String(str8, ENCODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ECB加密
     */
    public String encodeECB(String data) {
        try {
            byte[] key = keysecret.getBytes(ENCODE);
            byte[] str9 = des3EncodeECB(key, data.getBytes(ENCODE));
            return new String(Base64.encode(str9, Base64.NO_WRAP), ENCODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ECB解密
     */
    public String decodeECB(String data) {
        try {
            byte[] key = keysecret.getBytes(ENCODE);
            return new String(des3DecodeECB(key, Base64.decode(data, Base64.NO_WRAP)), ENCODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /*-----------------------------------------------------------------------*/
    /*----------    分割线,内部方法   ---------------------------------*/
    /*-----------------------------------------------------------------------*/

    /**
     * ECB加密,不要IV
     *
     * @param key  密钥
     * @param data 明文
     * @return Base64编码的密文
     * @throws Exception 异常
     */
    private byte[] des3EncodeECB(byte[] key, byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    /**
     * ECB解密,不要IV
     *
     * @param key  密钥
     * @param data Base64编码的密文
     * @return 明文
     * @throws Exception 异常
     */
    private byte[] des3DecodeECB(byte[] key, byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    /**
     * CBC加密
     *
     * @param data btye arr
     * @return byte arr
     * @throws Exception 异常
     */
    private byte[] des3EncodeCBC(byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(keysecret.getBytes(ENCODE));
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    /**
     * CBC解密
     *
     * @param data byte arr
     * @return btye arr
     * @throws Exception 异常
     */
    private byte[] des3DecodeCBC(byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(keysecret.getBytes(ENCODE));
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }
}
