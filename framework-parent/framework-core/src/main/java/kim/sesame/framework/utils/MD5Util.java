package kim.sesame.framework.utils;

import java.security.MessageDigest;
/**
 * MD5加密算法类
 * @author johnny
 * date :  2015年5月29日 上午11:22:56
 * Description:
 */
public class MD5Util {
	/**
	 * 十六进制下数字到字符的映射数组 
	 */
	private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
	
	public static void main(String[] args) {
		System.out.println(encodeByMD5("123456"));
	}
	/**
	 * 对字符串进行MD5加密  
	 * @author  johnny
	 * date :  2015年5月29日 上午11:14:29
	 * @param originString originString
	 * @return String 
	 */
    public static String encodeByMD5(String originString){  
    	try{
    		if (originString != null){
    			//创建具有指定算法名称的信息摘要  
    			MessageDigest md = MessageDigest.getInstance("MD5");  
    			//使用指定的字节数组对摘要进行最后更新，然后完成摘要计算  
    			byte[] results = md.digest(originString.getBytes());  
    			//将得到的字节数组变成字符串返回  
    			String resultString = byteArrayToHexString(results);  
    			return resultString.toUpperCase(); 
    		} 
    	} catch(Exception ex){  
    		ex.printStackTrace();  
    	}  
        return null;  
    }
    /**
     * 转换字节数组为十六进制字符串 
     * @author  johnny
     * date :  2015年5月29日 上午11:18:28
     * @param b 字节数组
     * @return String  十六进制字符串 
     */
    private static String byteArrayToHexString(byte[] b){  
    	StringBuffer resultSb = new StringBuffer();  
    	for (int i = 0; i < b.length; i++){  
    		resultSb.append(byteToHexString(b[i]));  
    	}  
    	return resultSb.toString();  
    }  
    /**
     * 将一个字节转化成十六进制形式的字符串
     * @author  johnny
     * date :  2015年5月29日 上午11:18:44
     * @param b 字节数
     * @return String 
     */
    private static String byteToHexString(byte b){  
    	int n = b;  
    	if (n < 0)  n = 256 + n;  
    	int d1 = n / 16;  
    	int d2 = n % 16;  
    	return hexDigits[d1] + hexDigits[d2];  
    } 
}
