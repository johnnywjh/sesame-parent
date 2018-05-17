package kim.sesame.framework.web.jwt.v2;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RawAccessJwtToken implements JwtToken {
    private static Logger logger = LoggerFactory.getLogger(RawAccessJwtToken.class);

    private String token;

    public RawAccessJwtToken(String token) {
        this.token = token;
    }

    /**
     * Parses and validates JWT Token signature.
     */
    public Jws<Claims> parseClaims(String signingKey) {
        Jws<Claims> claimsJws = null;
        try {
            if (logger.isDebugEnabled()) {
                System.err.println("解析的token为:" + this.token);
            }
            JwtParser jwtParser = Jwts.parser().setSigningKey(signingKey);
            claimsJws = jwtParser.parseClaimsJws(this.token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            logger.error("Invalid JWT Token");
//            throw new BadCredentialsException("Invalid JWT token: ", ex);
        } catch (ExpiredJwtException expiredEx) {
            logger.info("JWT Token is expired");
//            throw new JwtExpiredTokenException(expiredEx.getClaims(), this, "用户信息已过期或已注销，请重新登录", expiredEx);
        }
        return claimsJws;
    }


    @Override
    public String getToken() {
        return token;
    }
}
