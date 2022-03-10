package kim.sesame.common.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 返回page
 */
@Setter
@Getter
public class GPage implements java.io.Serializable {

    /**
     * 页码，从1开始
     */
    private long current = 1;
    /**
     * 页面大小
     */
    private long size = 10;
    /**
     * 总数
     */
    private long total;
    /**
     * 总页数
     */
    private long pages;
    /**
     *  是否进行 count 查询
     */
    private boolean searchCount = true;

    /**
     * 是否分页,默认ture,架构不会使用这个字段,可以自己看情况使用
     */
//    private boolean pageflag = true;

}
