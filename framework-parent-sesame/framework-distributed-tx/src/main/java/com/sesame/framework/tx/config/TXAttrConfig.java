package com.sesame.framework.tx.config;

import com.codingapi.tx.config.service.TxManagerTxUrlService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 修改  TxManagerTxUrlService 的默认配置
 */
//@Component
public class TXAttrConfig /*implements TxManagerTxUrlService */{

//    @Resource
//    private Environment env;

/*
    @Override
    public String getTxUrl() {
        String url = env.getProperty("tx.url");
        if (url == null || url.length() == 0) {
            throw new NullPointerException("tx.url not null");
        } else {
            return url;
        }

    }
    */
}
