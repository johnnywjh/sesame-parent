package kim.sesame.framework.tablelog.db.bean;

import kim.sesame.framework.entity.BaseEntity;
import lombok.Data;

/**
 * TableOpLog
 * @author johnny
 * @date 2018-09-05 14:50:29
 * @Description: 表操作日志
 */
@Data
public class TableOpLog extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private String tableName;//表名称
	private String pkName;//主键字段
	private String pkValue;//主键的值
	private String fieldName;//目标字段
	private String fieldComment;//目标字段注释
	private String valueBefore;//修改前的值
	private String valueAfter;//修改后的值
	private String updateId;//修改标识,同一次修改的标识

    // not database field ...

	public TableOpLog(){}
}