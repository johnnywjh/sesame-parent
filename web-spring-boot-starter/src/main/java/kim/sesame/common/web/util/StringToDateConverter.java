package kim.sesame.common.web.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * String 转换成 日期
 * Description:
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
        if (StringUtils.isNotEmpty(source)) {
            try {
                if (source.length() > 10 && source.charAt(10) == 'T') {
                    source = source.replace('T', ' '); // 去掉j'T'
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat(StringUtils.isNotEmpty(datePattern) ? datePattern : (source.length() == 10 ? DATE_FROMAT : DATE_TIME_FORMAT));
                dateFormat.setLenient(false);
                return dateFormat.parse(source);
            } catch (ParseException e) {
                throw new IllegalArgumentException("invalid date format. Please use this pattern\"" + datePattern + "\"");
            }
        }
        return null;
    }
}
