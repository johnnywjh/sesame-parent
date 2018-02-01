package com.sesame.framework.web.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

/**
 * ip 信息
 * @author  wangjianghai
 * @date 2015年10月12日 上午9:39:38
 * @Title: IPUitl
 * @ClassName: IPUitl
 * @Description:
 */
public class IPUitl {
	
	/**
	 * 获取客户端的ip
	 * @author  wangjianghai
	 * @date 2015年10月12日 上午9:42:26
	 * @Title getRemortIP
	 * @Description 
	 * @param request
	 * @return String 
	 * @throws
	 */
	public static String getRemortIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();    
	    }     
	    return request.getHeader("x-forwarded-for");
	}
	/**
	 * 获取地址
	 * @param params
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getAddress(String ip) throws Exception{
		Map<String, String> mm = new HashMap<String, String>();
		String path = "http://ip.taobao.com/service/getIpInfo.php?ip="+ip;
		URL url = new URL(path);
		HttpURLConnection conn  = (HttpURLConnection) url.openConnection();
		InputStream is = conn.getInputStream();
		
		byte[] b = new byte[is.available()];
		is.read(b);
		
		
		is.close();
		conn.disconnect();// 关闭连接
		
		String returnStr = new String(b,"utf-8");  
		JSONObject jb =JSONObject.fromObject(returnStr); 
		//{"code":0,"data":{"country":"中国","country_id":"CN","area":"华南","area_id":"800000","region":"广东省","region_id":"440000","city":"深圳市","city_id":"440300","county":"","county_id":"-1","isp":"联通","isp_id":"100026","ip":"112.90.78.25"}}
		//{"code":0,"data":{"country":"中国","country_id":"CN","area":"华东","area_id":"300000","region":"浙江省","region_id":"330000","city":"杭州市","city_id":"330100","county":"","county_id":"-1","isp":"阿里云","isp_id":"1000323","ip":"121.40.219.5"}}
		System.out.println(jb.toString());
		String code = jb.getString("code");
		if(code.equals("0")){
			jb = jb.getJSONObject("data");
			mm.put("country", jb.getString("country"));
			mm.put("region", jb.getString("region"));
			mm.put("city", jb.getString("city"));
			mm.put("country", jb.getString("country"));
			mm.put("country", jb.getString("country"));
		}else{
			System.out.println("无效的ip");
		}
		return mm;
	}
	
	
}
