package com.sesame.framework.entity;

import lombok.Data;

/**
 * 菜单权限控制
 * @author wangjianghai
 * @date 2016年1月17日 下午4:17:39
 * @Title: Ztter
 * @ClassName: Ztter
 * @Description:
 */
@Data
public class Ztter implements java.io.Serializable {
	
	private Integer id; 
	private Integer pId;
	private String name;
	private boolean checked; //是否选中
	private boolean open; //是否展开
	private boolean chkDisabled;//是否禁用,true 禁用

	public Ztter() {
		super();
	}
	
	public Ztter(Integer id, Integer pId, String name, boolean open) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.open = open;
	}

	public Ztter(Integer id, Integer pId, String name, boolean checked, boolean open, boolean chkDisabled) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.checked = checked;
		this.open = open;
		this.chkDisabled = chkDisabled;
	}

	
}
