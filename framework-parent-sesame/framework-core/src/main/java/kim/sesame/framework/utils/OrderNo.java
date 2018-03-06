package kim.sesame.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class OrderNo {
	/**
	 * 生成交易订单
	 * 
	 * @author johnny
	 * @date 2015年10月27日 上午10:52:55
	 * @Title getNo
	 * @Description
	 * @return String
	 * @throws
	 */
	public static String getNo() {
		Date d = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sf.format(d) + getRandomNum(6);
	}

	/**
	 * 生成N位随机数
	 * 
	 * @author johnny
	 * @date 2015年10月27日 上午10:52:12
	 * @Title getRandomNum
	 * @Description
	 * @param num
	 *            几位
	 * @return String
	 * @throws
	 */
	public static String getRandomNum(int num) {
		Random random = new Random();
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < 6; i++) {
			result.append(random.nextInt(10));
		}
		return result.toString();
	}

	public static void main(String[] args) {
		System.out.println(getNo());
		System.out.println(getNo());
		System.out.println(getNo());
		System.out.println(getNo());
		System.out.println(getNo());
		System.out.println(getNo());
		System.out.println(getNo());
		System.out.println(getNo().length());
	}
}
