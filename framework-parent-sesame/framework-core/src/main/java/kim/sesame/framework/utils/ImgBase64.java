package kim.sesame.framework.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 图片和图片base64字符串转化
 * 
 * @author johnny
 * @date 2017年9月2日 下午8:58:48
 * @Description: >= jdk 1.8
 */
public class ImgBase64 {

	/**
	 * 图片地址转化成Base64字符串 jdk1.8
	 * 
	 * @author johnny
	 * @date 2017年9月2日 下午8:46:49
	 * @Title conversionString
	 * @Description
	 * @param imgFile
	 * @return String
	 */
	public static String conversionString(String imgFile) {
		InputStream in = null;
		byte[] data = null;
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return conversionString(data);
	}

	/**
	 * 图片byte转化成Base64字符串 jdk1.8
	 * 
	 * @author johnny
	 * @date 2017年9月2日 下午8:47:40
	 * @Title conversionString
	 * @Description
	 * @param imageContents
	 * @return String
	 */
	public static String conversionString(byte[] imageContents) {
		if (imageContents != null)
			return java.util.Base64.getEncoder().encodeToString(imageContents);
		else
			return null;
	}

	/**
	 * base64字符串转化成图片字节
	 * 
	 * @param imgStr
	 * @return
	 */
	public static byte[] imageConversionByte(String imgStr) { // 对字节数组字符串进行Base64解码并生成图片
		if (imgStr != null)
			return java.util.Base64.getDecoder().decode(imgStr);
		else
			return null;
		// 生成jpeg图片
		// String imgFilePath = "d://222.jpg";//新生成的图片
		// OutputStream out = new FileOutputStream(imgFilePath);
		// out.write(b);
		// out.flush();
		// out.close();
	}

	public static void writeFile(Path path, byte[] imageContents) {
		if (imageContents != null)
			try {
				Files.write(path, imageContents, StandardOpenOption.CREATE);
			} catch (IOException e) {
				System.out.println("写入文件出错了...~zZ");
			}
	}

	public static void main(String[] args) {
		String strImg = conversionString("D:\\images\\1.png");
		System.out.println(strImg);
		writeFile(Paths.get("D:\\images\\2.png"), imageConversionByte(strImg));
	}
}
