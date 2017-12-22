package com.sesame.framework.web.tag;

import com.sesame.framework.entity.GMap;
import com.sesame.framework.utils.GData;
import com.sesame.framework.utils.StringUtil;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.standard.processor.AbstractStandardConditionalVisibilityTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 权限控制处理器
 * 
 * @author wangjianghai (johnny_hzz@qq.com)
 * @date 2017年7月8日 下午5:26:25
 * @Description:
 */
public class ShowHideProcessor extends AbstractStandardConditionalVisibilityTagProcessor {

	public static final int PRECEDENCE = 300;
	public static final String ATTR_NAME = "show"; // <span
													// hzz:show="key_add"></span>

	public ShowHideProcessor(TemplateMode templateMode, String dialectPrefix) {
		super(templateMode, dialectPrefix, "show", 300);
	}

	protected boolean isVisible(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue) {
		IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(context.getConfiguration());

		IStandardExpression expression = expressionParser.parseExpression(context, attributeValue);

		String key = expression.execute(context).toString();

		IWebContext webContext = (IWebContext) context;
		HttpSession session = webContext.getSession();
		List<GMap> lis = (List<GMap>) session.getAttribute(GData.SESSION.MENU);

		boolean flg = false;

		if (lis != null) {
			if (StringUtil.equals(key, new String[] { "key_add", "key_select", "key_update", "key_delete" })) {
				flg = true;
			} else {
				String functionKey = null;
				for (GMap map : lis) {
					functionKey = map.getString("functionKey");//
					if (functionKey != null && functionKey.equals(key)) {
						flg = true;
						break;
					}
					// if (bean.getFunctionKey() != null) {
					// if (bean.getFunctionKey().equals(key)) {
					//
					// }
					// }
				}
			}
		}

		return flg;
	}

}
