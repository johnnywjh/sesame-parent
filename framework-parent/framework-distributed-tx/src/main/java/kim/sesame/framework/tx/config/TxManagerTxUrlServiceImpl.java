package kim.sesame.framework.tx.config;

import com.codingapi.tx.config.service.TxManagerTxUrlService;
import kim.sesame.framework.utils.StringUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * create by lorne on 2017/11/18
 */
@CommonsLog
@Service
public class TxManagerTxUrlServiceImpl implements TxManagerTxUrlService {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${tm.manager.url}")
    private String url;

    @Override
    public String getTxUrl() {
        String tmUrl = "";
        if (StringUtil.isEmpty(url)) {
            throw new NullPointerException("tm.manager.url 不能为空");
        }
        if (url.contains("http://")) {
            tmUrl = url;
            log.info(">>>>>>>>>1  tm.manager.url = " + tmUrl);
        } else {
            List<ServiceInstance> list = discoveryClient.getInstances(url);// 注册中心服务名 tx-manager
            String url = "";
            tmUrl = loadBalance(list).getUri()+ "/tx/manager/";
            log.info(">>>>>>>>>2  tm.manager.url = " + tmUrl);
        }

        return tmUrl;
    }

    /**
     * 负载的算法,随机获取一个tm 连接
     * @param list
     * @return
     */
    private ServiceInstance loadBalance(List<ServiceInstance> list) {
        Random rand = new Random();
        int index = rand.nextInt(list.size());
        return list.get(index);
    }

}
