package kim.sesame.framework.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 */
public class DateUtils {

    private static final Log logger = LogFactory.getLog(DateUtils.class);

    public static final String DATE_SHORT_FORMAT = "yyyyMMdd";
    public static final String DATE_CH_FORMAT = "yyyy年MM月dd日";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_MONTH_FORMAT = "yyyy-MM";
    public static final String TIME_FORMAT = "HH:mm:ss";

    public static final String DAYTIME_START = "00:00:00";
    public static final String DAYTIME_END = "23:59:59";

    private DateUtils() {
    }

    private static final String[] FORMATS = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd HH:mm:ss", "HH:mm", "HH:mm:ss", "yyyy-MM",
            "yyyy-MM-dd HH:mm:ss.S"};

    public static Date convert(String str) {
        if (str != null && str.length() > 0) {
            if (str.length() > 10 && str.charAt(10) == 'T') {
                str = str.replace('T', ' '); // 去掉json-lib加的T字母
            }
            for (String format : FORMATS) {
                if (str.length() == format.length()) {
                    try {
                        Date date = new SimpleDateFormat(format).parse(str);

                        return date;
                    } catch (ParseException e) {
                        if (logger.isWarnEnabled()) {
                            logger.warn(e.getMessage());
                        }
                        // logger.warn(e.getMessage());
                    }
                }
            }
        }
        return null;
    }

    public static Date convert(String str, String format) {
        if (!StringUtils.isEmpty(str)) {
            try {
                Date date = new SimpleDateFormat(format).parse(str);
                return date;
            } catch (ParseException e) {
                if (logger.isWarnEnabled()) {
                    logger.warn(e.getMessage());
                }
                // logger.warn(e.getMessage());
            }
        }
        return null;
    }

    /**
     * 时间拼接 将日期和实现拼接
     *
     * @param ymd 如2012-05-15
     * @param hm  0812
     * @return date
     */
    public static Date concat(String ymd, String hm) {
        if (StringUtil.isNotBlank(ymd) && StringUtil.isNotBlank(hm)) {
            try {
                String dateString = ymd.concat(" ").concat(
                        hm.substring(0, 2).concat(":")
                                .concat(hm.substring(2, 4)).concat(":00"));
                Date date = DateUtils.convert(dateString,
                        DateUtils.DATE_TIME_FORMAT);
                return date;
            } catch (NullPointerException e) {
                if (logger.isWarnEnabled()) {
                    logger.warn(e.getMessage());
                }
            }
        }
        return null;
    }

    /**
     * 根据传入的日期返回年月日的6位字符串，例：20101203
     *
     * @param date 带处理的时间
     * @return 返回的字符串
     */
    public static String getDay(Date date) {
        return convert(date, DATE_SHORT_FORMAT);
    }

    /**
     * 根据传入的日期返回中文年月日字符串，例：2010年12月03日
     *
     * @param date 待处理的时间
     * @return str
     */
    public static String getChDate(Date date) {
        return convert(date, DATE_CH_FORMAT);
    }

    /**
     * 将传入的时间格式的字符串转成时间对象
     * <p>
     * 例：传入2012-12-03 23:21:24
     *
     * @param dateStr 2012-12-03 23:21:24
     * @return date
     */
    public static Date strToDate(String dateStr) {
        SimpleDateFormat formatDate = new SimpleDateFormat(DATE_TIME_FORMAT);
        Date date = null;
        try {
            date = formatDate.parse(dateStr);
        } catch (Exception e) {

        }
        return date;
    }

    public static String convert(Date date) {
        return convert(date, DATE_TIME_FORMAT);
    }

    public static String convert(Date date, String dateFormat) {
        if (date == null) {
            return null;
        }

        if (null == dateFormat) {
            dateFormat = DATE_TIME_FORMAT;
        }

        return new SimpleDateFormat(dateFormat).format(date);
    }

    /**
     * 返回该天从00:00:00开始的日期
     *
     * @param date 待处理的时间
     * @return date
     */
    public static Date getStartDatetime(Date date) {
        String thisdate = convert(date, DATE_FORMAT);
        return convert(thisdate + " " + DAYTIME_START);

    }

    /**
     * 返回n天后从00:00:00开始的日期
     *
     * @param date     待处理的时间
     * @param diffDays diffDays
     * @return date
     */
    public static Date getStartDatetime(Date date, Integer diffDays) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        String thisdate = df.format(date.getTime() + 1000L * 24 * 60 * 60
                * diffDays);
        return convert(thisdate + " " + DAYTIME_START);
    }

    /**
     * 返回该天到23:59:59结束的日期
     *
     * @param date 待处理的时间
     * @return date
     */
    public static Date getEndDatetime(Date date) {
        String thisdate = convert(date, DATE_FORMAT);
        return convert(thisdate + " " + DAYTIME_END);

    }

    /**
     * 返回n天到23:59:59结束的日期
     *
     * @param date     带处理的时间
     * @param diffDays diffDays
     * @return date
     */
    public static Date getEndDatetime(Date date, Integer diffDays) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        String thisdate = df.format(date.getTime() + 1000L * 24 * 60 * 60
                * diffDays);
        return convert(thisdate + " " + DAYTIME_END);

    }

    /**
     * 返回该日期的最后一刻，精确到纳秒
     *
     * @param endTime 待处理的时间
     * @return date
     */
    public static Timestamp getLastEndDatetime(Date endTime) {
        Timestamp ts = new Timestamp(endTime.getTime());
        ts.setNanos(999999999);
        return ts;
    }

    /**
     * 返回该日期加1秒
     *
     * @param endTime 待处理的时间
     * @return date
     */
    public static Timestamp getEndTimeAdd(Date endTime) {
        Timestamp ts = new Timestamp(endTime.getTime());
        Calendar c = Calendar.getInstance();
        c.setTime(ts);
        c.add(Calendar.MILLISECOND, 1000);
        c.set(Calendar.MILLISECOND, 0);
        return new Timestamp(c.getTimeInMillis());
    }

    /**
     * 返回该日期加 millisecond 毫秒，正数为加，负数为减
     *
     * @param date        date
     * @param millisecond millisecond
     * @return date
     */
    public static Timestamp getDateAdd(Date date, int millisecond) {
        Timestamp ts = new Timestamp(date.getTime());
        Calendar c = Calendar.getInstance();
        c.setTime(ts);
        c.add(Calendar.MILLISECOND, millisecond);
        c.set(Calendar.MILLISECOND, 0);
        return new Timestamp(c.getTimeInMillis());
    }

    /**
     * 相对当前日期，增加或减少天数
     *
     * @param date date
     * @param day  day
     * @return str
     */
    public static String addDay(Date date, int day) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);

        return df.format(new Date(date.getTime() + 1000L * 24 * 60 * 60 * day));
    }

    /**
     * 相对当前日期，增加或减少天数
     *
     * @param date date
     * @param day  day
     * @return date
     */
    public static Date addDayToDate(Date date, int day) {
        return new Date(date.getTime() + 1000L * 24 * 60 * 60 * day);
    }

    /**
     * 返回两个时间的相差天数
     *
     * @param startTime 对比的开始时间
     * @param endTime   对比的结束时间
     * @return 相差天数
     */

    public static Long getTimeDiff(String startTime, String endTime) {
        Long days = null;
        Date startDate = null;
        Date endDate = null;
        try {
            if (startTime.length() == 10 && endTime.length() == 10) {
                startDate = new SimpleDateFormat(DATE_FORMAT).parse(startTime);
                endDate = new SimpleDateFormat(DATE_FORMAT).parse(endTime);
            } else if (startTime.length() == 7 && endTime.length() == 7) {
                startDate = new SimpleDateFormat(DATE_MONTH_FORMAT)
                        .parse(startTime);
                endDate = new SimpleDateFormat(DATE_MONTH_FORMAT).parse(endTime);
            } else {
                startDate = new SimpleDateFormat(DATE_TIME_FORMAT)
                        .parse(startTime);
                endDate = new SimpleDateFormat(DATE_TIME_FORMAT).parse(endTime);
            }

            days = getTimeDiff(startDate, endDate);
        } catch (ParseException e) {
            if (logger.isWarnEnabled()) {
                logger.warn(e.getMessage());
            }
            days = null;
        }
        return days;
    }

    /**
     * 返回两个时间的相差天数
     *
     * @param startTime 对比的开始时间
     * @param endTime   对比的结束时间
     * @return 相差天数
     */
    public static Long getTimeDiff(Date startTime, Date endTime) {
        Long days = null;
        long l = 1000L * 60 * 60 * 24;
        days = getDiff(startTime, endTime, l);
        return days;
    }

    /**
     * 返回两个时间的相差小时
     *
     * @param startTime 对比的开始时间
     * @param endTime   对比的结束时间
     * @return 相差分钟数
     */
    public static Long getHourDiff(Date startTime, Date endTime) {
        Long hour = null;
        long l = 1000L * 60 * 60;
        hour = getDiff(startTime, endTime, l);
        return hour;
    }

    /**
     * 返回两个时间的相差分钟数
     *
     * @param startTime 对比的开始时间
     * @param endTime   对比的结束时间
     * @return 相差分钟数
     */
    public static Long getMinuteDiff(Date startTime, Date endTime) {
        Long minutes = null;
        long l = 1000L * 60;
        minutes = getDiff(startTime, endTime, l);
        return minutes;
    }

    /**
     * 获取间隔时间的字符串 天, 小时
     * @return
     */
    public static String getDayHourDiff(Date startTime, Date endTime) {
        if (startTime == null || endTime == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Long d = getTimeDiff(startTime, endTime);
        Long h = getHourDiff(startTime, endTime);
        if (d != 0) {
            sb.append(d).append("天");
        }
        if (h != 0) {
            long h2 = h % 24;
            if (h2 != 0) {
                sb.append(h2).append("小时");
            }
        }
        return sb.toString();
    }

    private static Long getDiff(Date startTime, Date endTime, long l) {
        Long diff = null;

        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        long l_s = c.getTimeInMillis();
        c.setTime(endTime);
        long l_e = c.getTimeInMillis();
        diff = (l_e - l_s) / l;
        return diff;
    }

    /**
     * 返回两个时间的相差秒数
     *
     * @param startTime 对比的开始时间
     * @param endTime   对比的结束时间
     * @return 相差秒数
     */
    public static Long getSecondDiff(Date startTime, Date endTime) {

        return (endTime.getTime() - startTime.getTime()) / 1000;
    }

    public static String getPidFromDate(Date date) {
        if (date == null) {
            return "";
        }

        String m = convert(date, "yyyyMM");
        String d = convert(date, "dd");

        if (Integer.valueOf(d) <= 10) {
            d = "01";
        } else if (Integer.valueOf(d) <= 20) {
            d = "02";
        } else {
            d = "03";
        }

        return m.concat(d);
    }

    // 获取本月的开始时间
    public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }

    // 获取本月的结束时间
    public static Date getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());
    }

    // 获取本周的开始时间
    @SuppressWarnings("unused")
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }


    // 获取本周的结束时间
    public static Date getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }


    // 获取今年是哪一年
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }

    // 获取本年的开始时间
    public static Date getBeginDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        return getDayStartTime(cal.getTime());
    }

    // 获取本年的结束时间
    public static Date getEndDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return getDayEndTime(cal.getTime());
    }

    // 获取某个日期的开始时间
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d)
            calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    // 获取某个日期的结束时间
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d)
            calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    // 获取本月是哪一月
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param beginDate
     * @param endDate
     * @return List
     */
    public static List<String> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<String> lDate = new ArrayList<>();
        lDate.add(convert(beginDate, DATE_FORMAT));// 把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(convert(cal.getTime(), DATE_FORMAT));
            } else {
                break;
            }
        }
        lDate.add(convert(endDate, DATE_FORMAT));// 把结束时间加入集合
        return lDate;
    }

}
