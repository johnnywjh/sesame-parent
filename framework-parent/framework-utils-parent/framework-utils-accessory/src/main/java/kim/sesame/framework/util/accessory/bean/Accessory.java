package kim.sesame.framework.util.accessory.bean;

import kim.sesame.framework.entity.BaseEntity;
import lombok.Data;

/**
 * Accessory
 * @author wangjianghai
 * @date 2018-07-23 16:54:27
 * @Description: 附件表
 */
@Data
public class Accessory extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private String name;//名称
	private String url;//地址
	private String type;//类型
	private String exId;//外部id

    // not database field ...

	public Accessory(){}

	public Accessory(String type, String exId) {
		this.type = type;
		this.exId = exId;
	}
}