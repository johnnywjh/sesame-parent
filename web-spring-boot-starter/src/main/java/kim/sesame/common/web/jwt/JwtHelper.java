package kim.sesame.common.web.jwt;

import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import kim.sesame.common.utils.GData;
import kim.sesame.common.web.context.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class JwtHelper {

    private static JWTProperties jwtProperties;

    private static JWTProperties getJwtProperties() {
        if (jwtProperties == null) {
            jwtProperties = SpringContextUtil.getBean(JWTProperties.class);
        }
        return jwtProperties;
    }


    public static JSONObject parseJwtStr(String jwtStry) {
        return parseJwtStr(jwtStry, true);
    }

    public static JSONObject parseJwtStr(String jwtStr, boolean verify) {
        if (verify) {
            boolean resVerify = JWTUtil.verify(jwtStr, getJwtProperties().getSecret().getBytes());
            if (!resVerify) {
                log.info("token不合法 验证失败");
//                throw new RuntimeException("token不合法 验证失败");
                return null;
            }
        }

        JWT jwt = JWTUtil.parseToken(jwtStr);
        JSONObject claimsJson = jwt.getPayload().getClaimsJson();
        Long aLong = claimsJson.getLong(GData.JWT.JWT_EXPIRE_TIME);
        if (aLong == null) {
            return claimsJson;
        }
        long nowMillis = System.currentTimeMillis();
        if (aLong < nowMillis) {
            log.info("token 过期了");
            return null;
        }
        return claimsJson;
    }

    public static String createJWT(Consumer<Map<String, Object>> consumer) {
        JWTProperties jwtProperties = SpringContextUtil.getBean(JWTProperties.class);

        long nowMillis = System.currentTimeMillis();

        Map<String, Object> jwtMap = new HashMap<>();
        jwtMap.put(GData.JWT.JWT_CREATE_TIME, nowMillis);
        consumer.accept(jwtMap);

        //添加Token过期时间
        if (jwtProperties.getInvalidSecond() > 0) {
            long expMillis = nowMillis + jwtProperties.getInvalidSecond() * 1000;
            jwtMap.put(GData.JWT.JWT_EXPIRE_TIME, expMillis);
        }

        return JWTUtil.createToken(jwtMap, getJwtProperties().getSecret().getBytes());
    }

}