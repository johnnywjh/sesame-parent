package kim.sesame.common.context;

public final class TraceIdContext {

    private final static ThreadLocal<String> TRACE_ID = new ThreadLocal<>();

    //构造器私有化
    private TraceIdContext() {
    }


    //单例
    private static class ContextHolder {
        private final static TraceIdContext traceIdContext = new TraceIdContext();
    }
    public static TraceIdContext getContext() {
        return ContextHolder.traceIdContext;
    }

    public String getTraceId() {
        return TRACE_ID.get();
    }

    public void setTraceId(String traceId) {
        TRACE_ID.set(traceId);
    }


    public void clean() {
        TRACE_ID.remove();
    }
}
