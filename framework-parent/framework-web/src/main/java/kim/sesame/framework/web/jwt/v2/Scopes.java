package kim.sesame.framework.web.jwt.v2;

/**
 * Scopes
 * 领域
 *
 * @author vladimir.stankovic
 * <p>
 * Aug 18, 2016
 */
public enum Scopes {
    REFRESH_TOKEN;

    public String authority() {
        return "ROLE_" + this.name();
    }
}
