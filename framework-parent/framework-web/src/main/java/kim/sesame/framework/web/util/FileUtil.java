package kim.sesame.framework.web.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件操作
 * 
 * @author johnny
 * date :  2016年4月1日 上午11:18:22
 * Description:
 */
public class FileUtil {

	/**
	 * 文件写入磁盘
	 * 
	 * @author johnny
	 * date :  2016年2月22日 下午3:55:55
	 * @param path
	 *            地址(磁盘路径)
	 * @param filename
	 *            文件名称(SysUser.java)
	 * @param content
	 *            文本内容
	 */
	public static void createFile(String path, String filename, String content) {
		String filePath = path + "/" + filename;
		try {
			org.apache.commons.io.FileUtils.writeStringToFile(new File(filePath), content, "UTF-8");
			System.err.println(filePath + "  创建成功!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件和目录
	 * 
	 * @author johnny
	 * date :  2017年5月8日 下午10:57:16
	 * @param file file
	 */
	public static void clearFiles(File file) {
		if (file.exists()) {
			deleteFile(file);
		}
	}

	/**
	 * 删除目录里的所有文件,保存源文件
	 * 
	 * @author johnny
	 * date :  2017年5月11日 下午9:27:46
	 * @param file file
	 */
	public static void cleanDir(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
			}
		}
	}

	/**
	 * 递归删除文件夹和里面的文件
	 * 
	 * @author johnny
	 * date :  2017年5月11日 下午9:27:08
	 * @param file file
	 */
	private static void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
			}
		}
		file.delete();
	}

	public static String inputStream2String(InputStream in) {
		try {
			StringBuffer out = new StringBuffer();
			byte[] b = new byte[4096];
			for (int n; (n = in.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
			return out.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
