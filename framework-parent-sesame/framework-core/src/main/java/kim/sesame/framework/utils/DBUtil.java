package kim.sesame.framework.utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * s数据库连接处理
 * 
 * @author johnny
 * date :  2016年10月28日 下午9:40:44
 * Description:
 */
public class DBUtil {
	public static Connection getConn(String driver, String url, String name, String pwd) {
		try {
			Class.forName(driver);
			return DriverManager.getConnection(url, name, pwd);
		} catch (Exception e) {
			System.err.println("数据库连接失败");
			System.err.println("driver:" + driver);
			System.err.println("url:" + url);
			System.err.println("name:" + name);
			System.err.println("pwd:" + pwd);
			return null;
		}
	}

	public static void closeConn(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
