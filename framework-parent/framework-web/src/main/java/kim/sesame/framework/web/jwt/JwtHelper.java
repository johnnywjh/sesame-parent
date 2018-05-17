package kim.sesame.framework.web.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kim.sesame.framework.web.context.SpringContextUtil;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtHelper {

    public static Claims parseJWT(String jsonWebToken, String base64Security) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String createJWT(Map<String,Object> claims) {
        JWTProperties jwt = SpringContextUtil.getBean(JWTProperties.class);
        if(jwt==null){
            return "JWTProperties is null";
        }
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwt.getSecret());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

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
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);

            long expMillis = nowMillis + jwt.getInvalidSecond()* 1000;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }

        //生成JWT
        return builder.compact();
    }
}