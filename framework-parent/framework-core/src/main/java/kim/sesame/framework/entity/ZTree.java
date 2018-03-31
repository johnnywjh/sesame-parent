package kim.sesame.framework.entity;

import lombok.Data;

/**
 * 菜单权限控制
 * @author johnny
 * date :  2016年1月17日 下午4:17:39
 */
@Data
public class ZTree implements java.io.Serializable {
	
	private String id;
	private String parentId;
	private String name;
	private boolean checked; //是否选中
	private boolean open; //是否展开
	private boolean chkDisabled;//是否禁用,true 禁用

	
}
