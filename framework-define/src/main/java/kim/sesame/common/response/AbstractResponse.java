package kim.sesame.common.response;


import kim.sesame.common.web.PrintFriendliness;

/**
 * 响应基类.
 * <em>服务对外的响应（即Facade中每个方法的返回值）必须是该类的子类</em>
 * <em>其所有子类必须有默认的构造函数</em>
 */
public abstract class AbstractResponse extends PrintFriendliness {

    /**
     * 请求处理结束，是否需要将响应输出到日志。默认为<code>true</code>
     * @return
     */
    public boolean needsLog( ) {
        return true;
    }

    public String getDataClassName(){
        return null;
    }
}