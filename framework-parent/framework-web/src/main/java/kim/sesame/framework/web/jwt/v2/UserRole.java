package kim.sesame.framework.web.jwt.v2;


/**
 * UserRole
 *
 * @author vladimir.stankovic
 * <p>
 * Aug 18, 2016
 */
public class UserRole implements java.io.Serializable {

    private String appUserName;
    private String role;


    public String getAppUserName() {
        return appUserName;
    }

    public void setAppUserName(String appUserName) {
        this.appUserName = appUserName;
    }


    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
    //    public static class Id implements Serializable {
//        private static final long serialVersionUID = 1322120000551624359L;
//
//        protected Long userId;
//
//        protected Role role;
//
//        public Id() {
//        }
//
//        public Id(Long userId, Role role) {
//            this.userId = userId;
//            this.role = role;
//        }
//    }
//
//    Id id = new Id();
//
//    protected Role role;
//
//    public Role getRole() {
//        return role;
//    }
}
