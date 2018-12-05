package kim.sesame.framework.entity;

import lombok.Data;

/**
 * 树结构数据
 * @author johnny
 * date :  2016年1月17日 下午4:17:39
 */
@Data
public class Tree implements java.io.Serializable {
	
	private String id;//数据的id
	private String code;
	private String parentCode;
	private String name;
	private boolean checked; //是否选中
	private boolean open; //是否展开
	private boolean chkDisabled;//是否禁用,true 禁用

	
}
