package kim.sesame.framework.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kim.sesame.framework.utils.StringUtil;
import org.springframework.core.convert.converter.Converter;

/**
 * String 转换成 日期
 * @author johnny
 * @date 2017年9月6日 下午8:59:32
 * @Description:
 */
public class StringToDateConverter implements Converter<String, Date> {

    private static final String DATE_FROMAT = "yyyy-MM-dd";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private String datePattern;

    public StringToDateConverter() {

    }

    public StringToDateConverter(String datePattern) {
        this.datePattern = datePattern;
    }

    @Override
    public Date convert(String source) {
		if (StringUtil.isNotBlank(source)) {
			try {
				if (source.length() > 10 && source.charAt(10) == 'T') {
					source = source.replace('T', ' '); // 去掉j'T'
				}
				SimpleDateFormat dateFormat = new SimpleDateFormat(StringUtil.isNotBlank(datePattern) ? datePattern : (source.length() == 10 ? DATE_FROMAT : DATE_TIME_FORMAT));
				dateFormat.setLenient(false);
				return dateFormat.parse(source);
			} catch (ParseException e) {
				throw new IllegalArgumentException("invalid date format. Please use this pattern\"" + datePattern + "\"");
			}
		}
        return new Date();
    }
}
