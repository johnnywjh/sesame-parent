package kim.sesame.common.sleuth.config;

import brave.baggage.BaggageFields;
import brave.baggage.CorrelationScopeConfig;
import brave.context.slf4j.MDCScopeDecorator;
import brave.propagation.CurrentTraceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SleuthConfiguration {

    @Bean
    public CurrentTraceContext.ScopeDecorator legacyMDCKeys() {
        return MDCScopeDecorator.newBuilder()
                .clear()
                .add(CorrelationScopeConfig.SingleCorrelationField.newBuilder(BaggageFields.TRACE_ID)
                        .name("X-B3-TraceId").build())
                .add(CorrelationScopeConfig.SingleCorrelationField.newBuilder(BaggageFields.PARENT_ID)
                        .name("X-B3-ParentSpanId").build())
                .add(CorrelationScopeConfig.SingleCorrelationField.newBuilder(BaggageFields.SPAN_ID)
                        .name("X-B3-SpanId").build())
                .add(CorrelationScopeConfig.SingleCorrelationField.newBuilder(BaggageFields.SAMPLED)
                        .name("X-Span-Export").build())
                .build();
    }

}
