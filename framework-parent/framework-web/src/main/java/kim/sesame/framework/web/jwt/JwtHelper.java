package kim.sesame.framework.web.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kim.sesame.framework.utils.GData;
import kim.sesame.framework.utils.StringUtil;
import kim.sesame.framework.web.context.SpringContextUtil;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtHelper {

    public static Claims parseJWT(String jsonWebToken) {
        JWTProperties jwt = SpringContextUtil.getBean(JWTProperties.class);
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(jwt.getSecret()))
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String createJWT(Map<String, Object> claims) {
        JWTProperties jwt = SpringContextUtil.getBean(JWTProperties.class);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwt.getSecret());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        long nowMillis = System.currentTimeMillis();
        claims.put("jwt_create_time", nowMillis);

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("typ", "JWT")
//                .claim("userCode", userCode)
//                .claim("roleCode", roleCode)
                .setClaims(claims)
                .setIssuer(jwt.getIss())
//                .setAudience(audience)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        if (jwt.getInvalidSecond() > 0) {
            Date now = new Date(nowMillis);

            long expMillis = nowMillis + jwt.getInvalidSecond() * 1000;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }

        //生成JWT
        return builder.compact();
    }

    public static String getJwtUser(String sessionId) {
        return getJwtUser(sessionId, null);
    }

    public static String getJwtUser(String sessionId, String account) {
        Map<String, Object> claims = new HashMap<>();
        if (StringUtil.isNotEmpty(sessionId)) {
            claims.put(GData.JWT.SESSION_ID, sessionId);
        }
        if (StringUtil.isNotEmpty(account)) {
            claims.put(GData.JWT.USER_ACCOUNT, account);
        }
        return createJWT(claims);
    }
}