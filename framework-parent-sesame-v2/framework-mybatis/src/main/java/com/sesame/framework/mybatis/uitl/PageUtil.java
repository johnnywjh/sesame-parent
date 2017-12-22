package com.sesame.framework.mybatis.uitl;

import com.github.pagehelper.Page;
import com.sesame.framework.entity.GPage;

public class PageUtil {

    public static GPage recount(Page pages) {
        return recount(null, pages);
    }

    public static GPage recount(GPage gPage, Page pages) {
        if (gPage == null) {
            gPage = new GPage();
        }
        gPage.setPageNum(pages.getPageNum());
        gPage.setPageSize(pages.getPageSize());
        gPage.setTotal(pages.getTotal());
        gPage.setPages(pages.getPages());
        gPage.setCount(pages.isCount());
        return gPage;
    }

}
