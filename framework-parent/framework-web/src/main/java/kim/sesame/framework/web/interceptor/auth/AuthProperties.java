package kim.sesame.framework.web.interceptor.auth;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * 权限验证属性
 *
 * @author johnny
 * date :  2017年9月7日 下午7:20:50
 * Description:
 */
@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.auth")
public class AuthProperties implements InitializingBean {

    /**
     * ip 白名单
     */
    private Set<String> ipWhiteList;

    /**
     * key : 公钥
     * value : AuthToken
     */
    private Map<String, AuthToken> authToken;

    @Override
    public void afterPropertiesSet() throws Exception {
        AuthTokenWhiteListUtils.setWhiteList(ipWhiteList);
    }

    /**
     * sesame.framework.auth.auth-token.auth-h5.private-token=kim.sesame.h5
     * 公钥为auth-h5 , 私钥为kim.sesame.h5
     */
    @Data
    public static class AuthToken {
        /**
         * 是否开启
         */
        private boolean enable = true;
        /**
         * 私钥
         */
        private String privateToken;
        /**
         * 超时时间,单位分钟
         */
        private int timeOut;
        /**
         * 备注
         */
        private String remark;
    }
}


