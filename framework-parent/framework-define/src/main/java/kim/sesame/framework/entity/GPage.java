package kim.sesame.framework.entity;

import lombok.Data;

/**
 * 返回page
 */
@Data
public class GPage implements java.io.Serializable {

    public static int DEFAULT_PAGE_SIZE = 10;

    /**
     * 页码，从1开始
     */
    private long pageNum = 1;
    /**
     * 页面大小
     */
    private long pageSize = DEFAULT_PAGE_SIZE;
    /**
     * 总数
     */
    private long total;
    /**
     * 总页数
     */
    private long pages;
    /**
     * 包含count查询
     */
    private boolean count = true;

    /**
     * 是否分页,默认ture,架构不会使用这个字段,可以自己看情况使用
     */
    private boolean pageflag = true;

    public GPage() {
    }

    public void setPageNum(long pageNum) {
        if (pageNum <= 0) {
            pageNum = 1;
        }
        this.pageNum = pageNum;
    }

    public void setPageSize(long pageSize) {
        if (pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        this.pageSize = pageSize;
    }

}
