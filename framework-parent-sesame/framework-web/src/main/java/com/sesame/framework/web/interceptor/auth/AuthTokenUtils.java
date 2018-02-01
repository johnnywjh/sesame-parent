package com.sesame.framework.web.interceptor.auth;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.sesame.framework.web.util.FileUtil;

import net.sf.json.JSONObject;

/**
 * @ClassName: AuthTokenUtils 
 * @Description: 根据对应的公钥和密钥规则创建相应的动态口令，对比相应的动态口令
 * @author liuhuan liuhuan@duoduopei.com.cn 
 * @date 2016年9月27日 上午10:43:34  
 */
public class AuthTokenUtils {
	// JavaScript容器引擎管理器
	private static ScriptEngine scriptEngine = null;
	private static Invocable inv = null;
	private static JSONObject authSource = null;
	static {
		try {
			ScriptEngineManager sem = new ScriptEngineManager();
			scriptEngine = sem.getEngineByName("js");
			scriptEngine.eval(FileUtil.inputStream2String(AuthTokenUtils.class.getResourceAsStream("AuthToken.js")));
			inv = (Invocable) AuthTokenUtils.scriptEngine;
			// authSource =
			// JSON.parseObject(FileUtil.inputStream2String(AuthTokenUtils.class.getResourceAsStream("AuthTokenSource.json")));
			authSource = JSONObject.fromObject(FileUtil.inputStream2String(AuthTokenUtils.class.getResourceAsStream("AuthTokenSource.json")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取动态口令值 token : 口令，c:误差时效 单位 分钟
	 * 
	 * @Title: getAuthToken_s 
	 * @param token
	 * @param private_token
	 * @param c
	 * @return String    返回类型 
	 * @throws js执行异常 
	 */
	public static String getAuthToken_s(String token, String private_token, int c) throws Exception {
		String res = (String) inv.invokeFunction("getAuthToken_s", token, private_token, c);
		return res;
	}

	/**
	 * 获取5分钟误差时效的动态口令值 token : 口令
	 * 
	 * @Title: getAuthToken_s 
	 * @param token
	 * @param private_token
	 * @return String    返回类型 
	 * @throws js异常 
	 */
	public static String getAuthToken_5minute(String token, String private_token) throws Exception {
		String res = (String) inv.invokeFunction("getAuthToken_5minute", token, private_token);
		return res;
	}

	/**
	 * 获取30分钟误差时效的动态口令值 token : 口令
	 * 
	 * @Title: getAuthToken_s 
	 * @param token
	 * @param private_token
	 * @return String    返回类型 
	 * @throws js异常 
	 */
	public static String getAuthToken_30minute(String token, String private_token) throws Exception {
		String res = (String) inv.invokeFunction("getAuthToken_30minute", token, private_token);
		return res;
	}

	/**
	 * @Title: isVaild 
	 * @Description: 对比动态口令是否正确，如果动态口令正确则返回true，不正确则返回false
	 * @param public_token
	 * @param authToken
	 * @throws Exception   
	 *             设定文件 
	 * @return boolean 返回类型 
	 */
	public static boolean isValid(String public_token, String authToken) throws Exception {
		// 1.根据公钥获取私钥
		JSONObject tokenObj = authSource.getJSONObject(public_token);
		String private_token = null;
		if (tokenObj != null && (private_token = tokenObj.getString("private_token")) != null) {
			// 2.通过公钥私钥和时间有效数获取动态authToken
			String localAuthToken = AuthTokenUtils.getAuthToken_s(public_token, private_token, tokenObj.getInt("token_c"));
			// 3.对比动态客户端提交的authToken和服务器authToken是否一致，如果一致返回true，不一致返回false
			if (localAuthToken.equals(authToken)) {
				System.out.println("token:" + public_token + ",认证成功," + tokenObj.getString("desc"));
				return true;
			}
		}
		return false;
	}
}
