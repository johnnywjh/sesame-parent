package kim.sesame.common.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * https://hutool.cn/docs/#/core/%E6%97%A5%E6%9C%9F%E6%97%B6%E9%97%B4/%E6%97%A5%E6%9C%9F%E6%97%B6%E9%97%B4%E5%B7%A5%E5%85%B7-DateUtil?id=%e6%96%b9%e6%b3%95
 * @see DatePattern#NORM_DATETIME_PATTERN //DATETIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";
 * {@link DatePattern#NORM_DATE_PATTERN} //DATE_FORMATTER = "yyyy-MM-dd";
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)// 构造器私有化
@Slf4j
public class DateUtils extends DateUtil {

    /**
     * 时间进制
     */
    public static final Integer TIME_RADIX = 60;
    public static final Integer MILLISECONDS_RADIX = 1000;

    /**
     * 获取两个日期相差的月数
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 如果d2 到 d1返回 月数差 否则返回0
     */
    public static int getMonthDiff(Date startDate, Date endDate) {
        if (endDate == null || startDate == null) {
            return 0;
        }
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(endDate);
        c2.setTime(startDate);
        if (c1.getTimeInMillis() < c2.getTimeInMillis()) {
            return 0;
        }
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值 假设 d2 = 2015-8-16  d1 = 2011-9-30
        int yearInterval = year1 - year2;
        // 如果 d2的 月-日 小于 d1的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2) {
            yearInterval--;
        }
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) {
            monthInterval--;
        }
        monthInterval %= 12;
        return yearInterval * 12 + monthInterval;
    }


    /**
     * 比较两个时间相差的天数
     *
     * @param beginDate 1
     * @param endDate   2
     * @return long
     */
    public static Long getDaysDiff(LocalDate beginDate, LocalDate endDate) {
        return endDate.toEpochDay() - beginDate.toEpochDay();
    }

    /**
     * 比较两个时间相差的天数
     *
     * @param startDate 1
     * @param endDate   2
     * @return long
     */
    public static Long getDaysDiff(Date startDate, Date endDate) {
        LocalDate beginTime = DateUtils.getDateToLocalDate(startDate);
        LocalDate endTime = DateUtils.getDateToLocalDate(endDate);
        return getDaysDiff(beginTime, endTime);
    }

    /**
     * 获取相差月份时的时间
     *
     * @param date      日期
     * @param month     距date 相差月份 正数加 负数减
     * @param formatter fmt
     * @return string
     */
    public static String getMonthByDate(Date date, int month, String formatter) {
        if (StringUtils.isEmpty(formatter)) {
            formatter = DatePattern.NORM_DATETIME_PATTERN;
        }
        return DateUtil.format(getDifferInByDate(date, month), formatter);
    }

    /**
     * 获取相差月份时的时间
     *
     * @param date  日期
     * @param month 距date 相差月份 正数加 负数减
     * @return date
     */
    public static Date getDifferInByDate(Date date, int month) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, month);
        Date m = c.getTime();
        return m;
    }


    /**
     * 获取localDate日期的开始时间 {@link LocalTime}
     * eg. 2018-06-11 13:31:31 return 2018-06-11 00:00:00
     *
     * @param localDate localdate
     * @return date
     */
    public static Date getDateBeginTime(LocalDate localDate) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 获取localDate日期的结束时间 {@link LocalTime}
     * eg. 2018-06-11 13:31:31 return 2018-06-11 23:59:59.999999999
     *
     * @param localDate localDate
     * @return date
     */
    public static Date getDateEndTime(LocalDate localDate) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MAX);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * date 转 LocalDate
     *
     * @param date date
     * @return localste
     */
    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    /**
     * 获取localDate日期所在月的开始日期
     * eg. 2018-06-11 13:31:31 return 2018-06-01 00:00:00
     *
     * @param localDate localDate
     * @return date
     */
    public static Date getMonthFirstDay(LocalDate localDate) {
        LocalDate firstDate = localDate.with(TemporalAdjusters.firstDayOfMonth());
        return getDateBeginTime(firstDate);
    }

    /**
     * 获取localDate日期所在月的结束日期
     * eg. 2018-06-11 13:31:31 return 2018-06-30 23:59:59.999999999
     *
     * @param localDate localDate
     * @return date
     */
    public static Date getMonthLastDay(LocalDate localDate) {
        LocalDate firstDate = localDate.with(TemporalAdjusters.lastDayOfMonth());
        return getDateEndTime(firstDate);
    }

//    /**
//     * 日期转字符串
//     *
//     * @param date      date
//     * @param formatter formatter
//     * @return string
//     */
//    public static String dateToString(Date date, String formatter) {
//        if (date == null) {
//            return null;
//        }
//        if (StringUtils.isEmpty(formatter)) {
//            formatter = DatePattern.NORM_DATETIME_PATTERN;
//        }
//        SimpleDateFormat simpleDateFormat = getSDF(formatter);
//        return simpleDateFormat.format(date);
//    }
//
//    /**
//     * 字符串转日期
//     *
//     * @param dateString 1
//     * @param formatter  2
//     * @return date
//     */
//    public static Date stringToDate(String dateString, String formatter) {
//        if (StringUtils.isEmpty(dateString)) {
//            return null;
//        }
//        if (StringUtils.isEmpty(formatter)) {
//            formatter = DatePattern.NORM_DATETIME_PATTERN;
//        }
//        SimpleDateFormat simpleDateFormat = getSDF(formatter);
//        try {
//            return simpleDateFormat.parse(dateString);
//        } catch (ParseException e) {
//            log.error("字符串转日期错误!", e);
//            e.printStackTrace();
//            return null;
//        }
//    }

//        private static ThreadLocal<Map<String, SimpleDateFormat>> sdfThreadLocalMap = new ThreadLocal<>();

    public static SimpleDateFormat getSDF(String formatter) {
        return new SimpleDateFormat(formatter);
/*
        Map<String, SimpleDateFormat> stringSimpleDateFormatMap = sdfThreadLocalMap.get();
        if (stringSimpleDateFormatMap == null) {
            stringSimpleDateFormatMap = new HashMap<>();
        }
        SimpleDateFormat simpleDateFormat = stringSimpleDateFormatMap.get(formatter);
        if (simpleDateFormat == null) {

            simpleDateFormat = new SimpleDateFormat(formatter);
            stringSimpleDateFormatMap.put(formatter, simpleDateFormat);
            sdfThreadLocalMap.set(stringSimpleDateFormatMap);
//            System.out.println("初始化 " + formatter + "" + Thread.currentThread().getName() + " " + simpleDateFormat);
        }
//        System.out.println(stringSimpleDateFormatMap.keySet().size() + " " + formatter + " " + Thread.currentThread().getName() + "" + simpleDateFormat);
        return simpleDateFormat;

 */
    }

    /**
     * java.util.Date 到 java.time.LocalDate
     *
     * @param date date
     * @return java.util.Date 到 java.time.LocalDate
     */
    public static LocalDate getDateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    /**
     * 得到两日期之间的毫秒数
     *
     * @param date1 date1
     * @param date2 date2
     * @return long
     */
    public static long subDate(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }
        return date1.getTime() - date2.getTime();
    }

    /**
     * 计算已用耗时小时
     *
     * @param date1 date1
     * @param date2 date2
     * @return long
     */
    public static Long getTwoDateHour(Date date1, Date date2) {
        return getTwoDateTotalMin(date1, date2) / TIME_RADIX;
    }

    /**
     * 计算已用耗时分钟
     *
     * @param date1 date1
     * @param date2 date2
     * @return long
     */
    public static Long getTwoDateMin(Date date1, Date date2) {
        return getTwoDateTotalMin(date1, date2) % TIME_RADIX;
    }

    /**
     * 获取两个时间之间的分钟数
     *
     * @param date1 one
     * @param date2 two
     * @return long
     */
    public static long getTwoDateTotalMin(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0L;
        }
        long timeMils = date1.getTime() - date2.getTime();
        return timeMils / (MILLISECONDS_RADIX * TIME_RADIX);
    }

    /**
     * 根据分钟获取小时和分钟
     *
     * @param min min
     * @return string
     */
    public static String getHourAndMin(Long min) {
        if (min <= 0) {
            return "";
        }
        return min / TIME_RADIX + "小时" + min % TIME_RADIX + "分钟";
    }

    /**
     * 返回当天0点的时间
     *
     * @return long
     */
    public static Long getNowDateStart() {
        long nowTime = System.currentTimeMillis();
        return nowTime - (nowTime + TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24);
    }

    /**
     * 返回时间0点
     *
     * @param nowTime date
     * @return date
     */
    public static Date getNowDateStart(Date nowTime) {
        Long time = nowTime.getTime();
        return new Date(time - (time + TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24));
    }

    /**
     * 返回当天0点
     *
     * @return date
     */
    public static Date getNowDateStartTime() {
        return new Date(getNowDateStart());
    }

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
                try {
//                    System.out.println(sdf.parse("2022-11-15 17:05:20"));
//                    System.out.println(DateUtil.parse("2022-11-15 17:05:20",null));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();

    }


}
