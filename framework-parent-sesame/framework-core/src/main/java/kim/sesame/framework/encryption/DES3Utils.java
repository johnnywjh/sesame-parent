package kim.sesame.framework.encryption;


import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * Created by mlick on 2017-06-17. des3 两种加密方式 ECB和CBC模式
 */
public class DES3Utils {

    private static byte[] keyiv = {1, 2, 3, 4, 5, 6, 7, 8};
    private static String keysecret = "com-sesame-123456789123456789123456789";
    public final static String ENCODE = "UTF-8";


    public static void setKeysecret(String keysecret) {
        DES3Utils.keysecret = keysecret;
    }

    public static String encodeECB(String data) throws Exception {
        byte[] key = keysecret.getBytes(ENCODE);
        byte[] str9 = des3EncodeECB(key, data.getBytes(ENCODE));
        return new String(Base64.encode(str9, Base64.NO_WRAP), ENCODE);
    }

    public static String decodeECB(String data) throws Exception {
        byte[] key = keysecret.getBytes(ENCODE);
        return new String(des3DecodeECB(key, Base64.decode(data, Base64.NO_WRAP)), ENCODE);
    }

    public static String decodeCBC(String data) {
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
     * ECB加密,不要IV
     *
     * @param data 明文
     * @return Base64编码的密文
     * @throws Exception
     */
    public static String encodeCBC(String data) throws Exception {
        byte[] key = keysecret.getBytes(ENCODE);
        byte[] str7 = des3EncodeCBC(data.getBytes(ENCODE));
        return new String(Base64.encode(str7, Base64.NO_WRAP), ENCODE);
    }

    /**
     * ECB加密,不要IV
     *
     * @param key  密钥
     * @param data 明文
     * @return Base64编码的密文
     * @throws Exception
     */
    public static byte[] des3EncodeECB(byte[] key, byte[] data) throws Exception {
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
     * @throws Exception
     */
    public static byte[] des3DecodeECB(byte[] key, byte[] data) throws Exception {
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
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] des3EncodeCBC(byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(DES3Utils.keysecret.getBytes(ENCODE));
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
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] des3DecodeCBC(byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(DES3Utils.keysecret.getBytes(ENCODE));
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }
}
