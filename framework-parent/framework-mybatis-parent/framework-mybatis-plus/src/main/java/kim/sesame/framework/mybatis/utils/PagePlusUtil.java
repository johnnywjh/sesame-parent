package kim.sesame.framework.mybatis.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import kim.sesame.framework.entity.GPage;

public class PagePlusUtil {

    public static GPage recount(Page pages) {
        return recount(null, pages);
    }

    public static GPage recount(GPage gPage, Page pages) {
        if (gPage == null) {
            gPage = new GPage();
        }
        gPage.setPageNum((int) pages.getCurrent());
        gPage.setPageSize((int) pages.getSize());
        gPage.setTotal(pages.getTotal());
        gPage.setPages((int) pages.getPages());
        gPage.setCount(pages.isSearchCount());
        return gPage;
    }

}
