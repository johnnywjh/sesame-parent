package kim.sesame.framework.web.entity;

/**
 * IUser
 *
 * @author johnny
 * @date 2017-11-07 13:20
 * @Description: 用户信息接口
 */
public interface IUser {
    String getId();
    String getAccount();
    String getName();
    String getPwd();
}