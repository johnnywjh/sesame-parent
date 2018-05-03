package kim.sesame.framework.encryption;


public class Test {
    public static void main(String[] args) throws Exception {

        EncryptionAndDecryption ead = new EncryptionAndDecryption();
        String key ="aaaaaaaaaaa";

        ead.setKeysecret(key);
        System.out.println(ead.getKeysecret());
        System.out.println(ead.getKeysecret().length());

        String str = "123456";
        System.out.println("加密前字符串:" + str);

        cbc(str,ead);
        System.out.println("--------------------------------------");
        ecb(str,ead);

    }

    public static void cbc(String str, EncryptionAndDecryption ead) {

        System.out.println("CBC加密解密");
        String str1 = ead.encodeCBC(str);
        System.out.println("加密后字符串:" + str1);
        System.out.println("解密后字符串:" + ead.decodeCBC(str1));
    }
    public static void ecb(String str, EncryptionAndDecryption ead) {
        System.out.println("ECB加密解密");
        String str1 = ead.encodeECB(str);
        System.out.println("加密后字符串:" + str1);
        System.out.println("解密后字符串:" + ead.decodeECB(str1));
    }

}
