package kim.sesame.framework.web.entity;

import kim.sesame.framework.entity.BaseEntity;
import lombok.Data;

/**
 * 用户实体接口
 */
@Data
public class IUser extends BaseEntity {

    private String account;//账号,唯一
    private String name;//昵称
    private String pwd;//密码
    private String deptCode;//部门编号
    private String sex;//性别: 1 男,2 女,3 未知
    private String phone;//手机
    private String email;//邮箱
    private String idcard;//身份证号码
    private String txurl;//头像地址
    private String qq;//QQ号
    private String weixin;//微信号
    private String isDelete;//是否允许删除 : Y/N
    private String isEnable;//是否允许登录 : Y/N
    private String active;//是否有效 : Y/N
    private String skin;//界面主题

}