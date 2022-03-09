package kim.sesame.framework.web.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import kim.sesame.framework.utils.GData;
import kim.sesame.framework.web.context.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JwtToken {

    public static String getJwtUser(String sessionId) {
        return getJwtUser(sessionId, null, null, null, false);
    }

    public static String getJwtUser(String account, String name, String pwdVersion) {
        return getJwtUser(null, account, name, pwdVersion, true);
    }

    private static String getJwtUser(String sessionId, String account, String name, String pwdVersion, boolean accLoad) {
        Map<String, Object> claims = new HashMap<>();
        if (StringUtils.isNotEmpty(sessionId)) {
            claims.put(GData.JWT.SESSION_ID, sessionId);
        }
        if (StringUtils.isNotEmpty(account)) {
            claims.put(GData.JWT.ACCOUNT, account);
        }
        if (StringUtils.isNotEmpty(name)) {
            claims.put(GData.JWT.NAME, name);
        }
        if (StringUtils.isNotEmpty(pwdVersion)) {
            claims.put(GData.JWT.PWD_VERSION, pwdVersion);
        }
        claims.put(GData.JWT.ACC_LOAD, accLoad);

        return createToken(claims);
    }

    /***
     * 创建Jwt令牌
     * 秘钥：secret
     * 载荷:dataMap(Map)
     */
    public static String createToken(Map<String, Object> dataMap) {
        return createToken(dataMap, null);
    }

    /***
     * 创建Jwt令牌
     * 秘钥：secret
     * 载荷:dataMap(Map)
     */
    public static String createToken(Map<String, Object> dataMap, String secret) {
        JWTProperties jwt = SpringContextUtil.getBean(JWTProperties.class);
        //确认秘钥
        if (StringUtils.isEmpty(secret)) {
            secret = jwt.getSecret();
        }
        //确认签名算法
        Algorithm algorithm = Algorithm.HMAC256(secret);

        //jwt令牌创建
        JWTCreator.Builder builder = JWT.create()
                .withClaim("body", dataMap)  //自定义载荷
                .withIssuer(jwt.getIss())   //签发者
                .withSubject(jwt.getSub())   //主题
                .withAudience(jwt.getAud()) //接收方
//                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))    //过期时间
                .withNotBefore(new Date(System.currentTimeMillis() + 1000))       //1秒后才能使用
                .withIssuedAt(new Date())   //签发时间
                .withJWTId(UUID.randomUUID().toString().replace("-", ""));    //唯一标识符

        if (jwt.getInvalidSecond() > 0) {
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            long expMillis = nowMillis + jwt.getInvalidSecond() * 1000;
            Date exp = new Date(expMillis);
            builder.withExpiresAt(new Date(System.currentTimeMillis() + 3600000));    //过期时间
        }
        return builder.sign(algorithm);
    }

    /****
     * 令牌解析
     */
    public static Map<String, Object> parseToken(String token) {
        return parseToken(token, null);
    }

    /****
     * 令牌解析
     */
    public static Map<String, Object> parseToken(String token, String secret) {
        JWTProperties jwtProperties = SpringContextUtil.getBean(JWTProperties.class);
        //确认秘钥
        if (StringUtils.isEmpty(secret)) {
            secret = jwtProperties.getSecret();
        }

        //确认签名算法
        Algorithm algorithm = Algorithm.HMAC256(secret);

        //创建令牌校验对象
        JWTVerifier verifier = JWT.require(algorithm).build();
        //校验解析
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("body").as(Map.class);
    }

    public static void main(String[] args) throws InterruptedException {
        //创建令牌
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("name", "zhangsan");
        dataMap.put("address", "湖南");

        //创建令牌
        String token = createToken(dataMap);
        System.out.println(token);

        //休眠一秒钟
        TimeUnit.SECONDS.sleep(1);

        //校验解析令牌
        Map<String, Object> stringObjectMap = parseToken(token);
        System.out.println(stringObjectMap);
    }
}
