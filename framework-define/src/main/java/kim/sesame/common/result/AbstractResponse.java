package kim.sesame.common.result;


import kim.sesame.common.req.PrintFriendliness;

/**
 * 响应基类.
 * <em>服务对外的响应（即Facade中每个方法的返回值）必须是该类的子类</em>
 * <em>其所有子类必须有默认的构造函数</em>
 */
public abstract class AbstractResponse extends PrintFriendliness {

    public void setTraceId(String traceId) {
    }
}