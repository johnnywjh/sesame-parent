package com.sesame.framework.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 全局Map ,封装参数,返回结果
 * @author wangjianghai
 * @date 2015年2月28日 下午11:39:23
 * @Description:
 */
public class GMap extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	/**
	 * @函数名 put
	 * @函数描述 key做预处理，在SQL的查询对结果key做出预处理
	 * @创建时间 2015年2月28日 下午11:39:23
	 */
	@Override
	public Object put(String key, Object value) {
		return super.put(convertKey(key), value);
	}

	/**
	 * @函数名 putAction
	 * @函数描述 key不做预处理，在SQL的操作中当作参数使用
	 * @创建时间 2015年2月28日 下午11:39:23
	 */
	public Object putAction(String key, Object value) {
		return super.put(key, value);
	}

	private String convertKey(String key) {
		if (key.contains("_")) {
			String[] keys = key.toLowerCase().split("_");
			StringBuffer keySub = new StringBuffer();
			int i = 0;
			for (String k : keys) {
				if (i == 0) {
					keySub.append(k);
				} else {
					keySub.append(k.substring(0, 1).toUpperCase());
					keySub.append(k.substring(1));
				}
				i++;
			}
			return keySub.toString();
		} else {
			//return key.toLowerCase();
			return key;
		}
	}

	public String getString(Object key) {
		return (String) super.get(key);
	}

	public Date getDate(String key) {
		return (Date) super.get(key);
	}

	public Double getDouble(String key) {
		return (Double) super.get(key);
	}

	public Integer getInteger(Object key) {
		return (Integer) super.get(key);
	}

	public Long getLong(Object key) {
		return (Long) super.get(key);
	}

	public Boolean getBoolean(Object key) {
		if (super.get(key) == null) {
			return false;
		}
		return (Boolean) super.get(key);
	}

	public BigDecimal getBigDecimal(Object key) {
		return (BigDecimal) super.get(key);
	}

	@SuppressWarnings("unchecked")
	public List<GMap> getCoMapList(Object key) {
		return (List<GMap>) super.get(key);
	}

	@SuppressWarnings("unchecked")
	public GMap getCoMap(Object key) {
		return (GMap) super.get(key);
	}

	public static GMap newMap() {
		return new GMap();
	}

}
