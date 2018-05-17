package kim.sesame.framework.web.jwt.v2;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kim.sesame.framework.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Factory class that should be always used to create {@link JwtToken}.
 *
 * @author vladimir.stankovic
 * <p>
 * May 31, 2016
 * <p>
 * 我们已经创建了工厂类(JwtTokenFactory)分离令牌创建逻辑。
 * 方法JwtTokenFactory # createAccessJwtToken创建签署了JWT访问令牌。
 * 方法JwtTokenFactory # createRefreshToken创建签署了JWT刷新令牌。
 */
@Component
public class JwtTokenFactory {
    private final JwtSettings settings;

    @Autowired
    public JwtTokenFactory(JwtSettings settings) {
        this.settings = settings;
    }

    /**
     * Factory method for issuing new JWT Tokens.
     */
    public AccessJwtToken createAccessJwtToken(UserContext userContext) {
        if (StringUtils.isBlank(userContext.getUsername()))
            throw new IllegalArgumentException("Cannot create JWT Token without username");

        if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty())
            throw new IllegalArgumentException("User doesn't have any privileges");

        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
        claims.put("scopes", userContext.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()));

        LocalDateTime currentTime = LocalDateTime.now();

        //创建时间
        Date iat = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());
        //过期时间
        Date exp = Date.from(currentTime.plusMinutes(settings.getTokenExpirationTime()).atZone(ZoneId.systemDefault()).toInstant());

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(iat)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();

        return new AccessJwtToken(token, claims);
    }

    public AccessJwtToken createRefreshToken(UserContext userContext) {
        if (StringUtil.isBlank(userContext.getUsername())) {
            throw new IllegalArgumentException("Cannot create JWT Token without username");
        }

        LocalDateTime currentTime = LocalDateTime.now();

        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
        claims.put("scopes", Collections.singletonList(Scopes.REFRESH_TOKEN.authority()));

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.getTokenIssuer())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime
                        .plusMinutes(settings.getRefreshTokenExpTime())
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();

        return new AccessJwtToken(token, claims);
    }


}
