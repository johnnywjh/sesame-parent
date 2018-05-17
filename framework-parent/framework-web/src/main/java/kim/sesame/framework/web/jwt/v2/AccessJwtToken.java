package kim.sesame.framework.web.jwt.v2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;

import java.text.SimpleDateFormat;

/**
 * Raw representation of JWT Token.
 *
 * @author vladimir.stankovic
 * <p>
 * May 31, 2016
 */
public final class AccessJwtToken implements JwtToken {


    private final String rawToken;

    private String iat;
    private String exp;

    public String getIat() {
        return iat;
    }

    public String getExp() {
        return exp;
    }

    @JsonIgnore
    private Claims claims;

    protected AccessJwtToken(final String token, Claims claims) {
        this.rawToken = token;
        this.claims = claims;
        this.iat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getIssuedAt());
        this.exp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getExpiration());
    }

    public String getToken() {
        return this.rawToken;
    }

    public Claims getClaims() {
        return claims;
    }

}
