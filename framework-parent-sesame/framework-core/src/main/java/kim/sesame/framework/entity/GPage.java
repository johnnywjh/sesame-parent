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
    private int pageNum = 1;
    /**
     * 页面大小
     */
    private int pageSize = DEFAULT_PAGE_SIZE;
    /**
     * 总数
     */
    private long total;
    /**
     * 总页数
     */
    private int pages;
    /**
     * 包含count查询
     */
    private boolean count = true;

    public GPage() {
    }

    public void setPageNum(int pageNum) {
        if (pageNum <= 0) {
            pageNum = 1;
        }
        this.pageNum = pageNum;
    }

    public void setPageSize(int pageSize) {
        if (pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        this.pageSize = pageSize;
    }

}
