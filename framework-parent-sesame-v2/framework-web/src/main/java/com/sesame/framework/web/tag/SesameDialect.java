package com.sesame.framework.web.tag;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * 标签模板
 * 
 * @author wangjianghai (johnny_hzz@qq.com)
 * @date 2017年7月8日 下午5:25:29
 * @Description:
 */
@Component
public class SesameDialect extends AbstractProcessorDialect {

	private static final String NAME = "Hzz";
	private static final String PREFIX = "hzz";

	public SesameDialect() {
		super(NAME, PREFIX, StandardDialect.PROCESSOR_PRECEDENCE);
	}

	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		return createStandardProcessorsSet(dialectPrefix);
	}

	private Set<IProcessor> createStandardProcessorsSet(String dialectPrefix) {
		LinkedHashSet<IProcessor> processors = new LinkedHashSet<IProcessor>();
		processors.add(new ShowHideProcessor(TemplateMode.HTML, dialectPrefix));
		return processors;
	}

}
