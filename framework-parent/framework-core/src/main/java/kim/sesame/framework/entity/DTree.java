package kim.sesame.framework.entity;

import lombok.Data;

/**
 * Dtree树结构数据
 *
 * @author johnny
 * @date 2018/12/19 16:44
 */
@Data
public class DTree implements java.io.Serializable {

    private String id;//数据的id
    private String code; // 数据code
    private String parentCode; // 数据父节点code
    private String name; // 数据名称
    private boolean checked; //是否选中
    private boolean chkDisabled;//是否禁用,true 禁用
    private String basicData; //自定义数据
    private Integer level;// 等级
    private Integer sequence;// 顺序
}
