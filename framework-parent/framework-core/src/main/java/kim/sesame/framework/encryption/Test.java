package kim.sesame.framework.encryption;


public class Test {
	public static void main(String[] args) throws Exception {

		String str = "中国ABCabc123";


		System.out.println("CBC加密解密");

		System.out.println("加密前字符串:" + str);

		String str1 = DES3Utils.encodeCBC(str);
		System.out.println("加密后字符串:" + str1);

		System.out.println("解密后字符串:" + DES3Utils.decodeCBC(str1));

	}
}
