package kim.sesame.framework.web.jwt.v2;


import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author vladimir.stankovic
 * <p>
 * Aug 4, 2016
 */
public class UserContext {

    interface GrantedAuthority extends Serializable {
        String getAuthority();
    }


    private final String username;
    private final List<GrantedAuthority> authorities;

    private UserContext(String username, List<GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    public static UserContext create(String username, List<GrantedAuthority> authorities) {
        if (StringUtils.isBlank(username)) throw new IllegalArgumentException("Username is blank: " + username);
        return new UserContext(username, authorities);
    }

    public String getUsername() {
        return username;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
