package kim.sesame.framework.web.interceptor.auth;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kim.sesame.framework.web.util.FileUtil;

import net.sf.json.JSONArray;

/**
 *  token认证的白名单工具类，白名单匹配成功的编号可以不用动态口令的校验，反之则需要校验动态口令
 * @author johnny
 * date :  2016年9月28日 上午8:42:08  
 */
public class AuthTokenWhiteListUtils {
	private static Set<String> whiteList = new HashSet<String>();
	static {
		try {
			// JSONArray authTokenWLJArr =
			// JSONArray.parseArray(FileUtil.inputStream2String(AuthTokenWhiteListUtils.class.getResourceAsStream("AuthTokenWhiteList.json")));
			JSONArray authTokenWLJArr = JSONArray.fromObject(FileUtil.inputStream2String(AuthTokenWhiteListUtils.class.getResourceAsStream("AuthTokenWhiteList.json")));
			System.out.println(authTokenWLJArr);
			Iterator<Object> wlItr = (Iterator<Object>) authTokenWLJArr.iterator();
			while (wlItr.hasNext())
				whiteList.add((String) wlItr.next());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Description:验证白名单内容
	 * @param srcIp srcip
	 * @return 设定文件 
	 * @return boolean  返回类型
	 */
	public static boolean isValid(String srcIp) {
		if (!whiteList.contains(srcIp)) {
			for (String whIp : whiteList) {
				if (whIp.contains("*")) {
					Pattern p = Pattern.compile(whIp.replace("*", "\\w*"));
					Matcher m = p.matcher(srcIp);
					return m.matches();
				}
			}
			return false;
		}
		return true;
	}

}
