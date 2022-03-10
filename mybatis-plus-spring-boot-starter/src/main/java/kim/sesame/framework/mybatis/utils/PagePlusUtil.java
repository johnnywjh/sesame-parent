package kim.sesame.framework.mybatis.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import kim.sesame.common.entity.GPage;

public class PagePlusUtil {

    public static GPage toGpage(Page page) {
        return toGpage(null, page);
    }

    public static GPage toGpage(GPage gPage, Page page) {
        if (gPage == null) {
            gPage = new GPage();
        }
        gPage.setCurrent(page.getCurrent());
        gPage.setSize(page.getSize());
        gPage.setTotal(page.getTotal());
        gPage.setPages(page.getPages());
        gPage.setSearchCount(page.isSearchCount());
        return gPage;
    }

}
