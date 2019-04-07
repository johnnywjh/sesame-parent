package kim.sesame.framework.web.context;

import kim.sesame.framework.web.response.Response;

/**
 * 日志打印上下文对象
 */
public final class LogProintContext {

    private final static ThreadLocal<Boolean> IGNORE = new ThreadLocal<>();
    private final static ThreadLocal<Response> RESPONSE = new ThreadLocal<>();

    //构造器私有化
    private LogProintContext() {
        this.setIsIgnore(false);// 默认false
    }

    //单例
    private static class ContextHolder {
        private final static LogProintContext logProintContext = new LogProintContext();
    }

    public static LogProintContext getLogProintContext() {
        return ContextHolder.logProintContext;
    }

    /*-------------------------------------------------------*/
    public Boolean getIsIgnore() {
        Boolean flg = null;
        try {
            flg = IGNORE.get();
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
        if (flg == null) {
            flg = false;
        }
        return flg;
    }

    public Response getResponse() {
        return RESPONSE.get();
    }


    public void setIsIgnore(Boolean flg) {
        IGNORE.set(flg);
    }

    public void setResponse(Response response) {
        RESPONSE.set(response);
    }


    /*-------------------------------------------------------*/
    public void clean() {
        IGNORE.remove();
        RESPONSE.remove();
    }
}