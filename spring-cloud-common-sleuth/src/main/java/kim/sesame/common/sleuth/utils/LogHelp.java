package kim.sesame.common.sleuth.utils;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

//import java.util.function.Consumer;

@Slf4j
public class LogHelp {

    private final static String TRACE_ID = "traceId";
    private final static String SPAN_ID = "spanId";

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }
    public static String getSpanId() {
        return MDC.get(SPAN_ID);
    }

    public static void execute(Runnable runnable) {
        execute(runnable,null,null);
    }
    public static void execute(Runnable runnable,String traceId,String spanId) {
        if (StringUtils.isBlank(traceId)) {
            traceId = IdUtil.simpleUUID();
        }
        if (StringUtils.isBlank(spanId)) {
            spanId = IdUtil.simpleUUID();
        }
        try {
            MDC.put(TRACE_ID, traceId);
            MDC.put(SPAN_ID, spanId);
//            consumer.accept(null);
            runnable.run();
        } catch (Exception e) {
            log.error("JobLogHelp execute error", e);
        } finally {
            MDC.clear();
        }
    }

}