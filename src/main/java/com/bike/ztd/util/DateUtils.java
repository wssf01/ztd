package com.bike.ztd.util;

import com.google.common.collect.Lists;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class DateUtils {

    /**
     * 默认格式
     */
    public final static String DATETIME_FORMAT = "yyyy.MM.dd HH:mm:ss";

    public final static String DATETIME_FORMAT2 = "yyyy-MM-dd HH:mm:ss";

    public final static String DATETIME_FORMAT3 = "yyyy年MM月dd日 HH:mm:ss";

    public final static String DATETIME_FORMAT4 = "yyyy-MM-dd HH:mm";

    public final static String DATETIME_MONTH = "yyyy.MM";

    public final static String DATETIME_MONTH2 = "yyyy-MM";

    public final static String DATETIME_YEAR = "yyyy";

    public final static String DATETIME_DAY = "MM-dd";

    /**
     * 时间戳格式
     */
    public final static String TIMESTAMP_FORMAT = "yyyyMMdd";

    public final static String DATE_FORMAT = "yyyy.MM.dd";

    public final static String DATE_FORMAT3 = "yyyy-MM-dd";

    public final static String TIME_FORMAT = "HH:mm:ss";

    public final static String API_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public final static String TIME_MIN = "HH:mm";

    public static final ThreadLocal<Map<String, SimpleDateFormat>> THREADLOCAL = new ThreadLocal<>();

    private static SimpleDateFormat getSimpleDateFormat(final String format) {
        Map<String, SimpleDateFormat> map = THREADLOCAL.get();
        if (map == null) {
            synchronized (THREADLOCAL) {
                if ((map = THREADLOCAL.get()) == null) {
                    THREADLOCAL.set(map = new HashMap<>());
                }
            }
        }
        return map.computeIfAbsent(format, k -> new SimpleDateFormat(format));
    }

    public static Date getDateByObj(Object objDate, String format) {
        String date = CommonUtils.tranString(objDate);
        Date returnDate = null;
        if (!"".equals(date)) {
            returnDate = getDateByString(date);
        }
        return returnDate;
    }

    /**
     * 整点时间
     *
     * @param time
     * @return
     */
    public static Date getZeroHourDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 格式化时间
     *
     * @param date
     * @param format
     * @return
     */
    public static Date formatDate(Date date, String format) {
        if (format == null) {
            format = DATETIME_FORMAT;
        }
        if (date == null) {
            return null;
        }
        SimpleDateFormat formatter = getSimpleDateFormat(format);
        String dateStr = formatter.format(date);
        Date returnDate = null;
        if (!"".equals(dateStr)) {
            try {
                returnDate = formatter.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return returnDate;
    }


    public static Date getDateByObj(Object objDate) {
        return getDateByObj(objDate, DATETIME_FORMAT);
    }

    public static Date getDateByString(String date, String format) {
        if (format == null) {
            return getDateByString(date);
        }
        SimpleDateFormat formatter = getSimpleDateFormat(format);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getDateByString() {
        SimpleDateFormat formatter = getSimpleDateFormat(DATETIME_FORMAT);
        Date date = null;
        try {
            date = formatter.parse(getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDateByString(String strDate) {
        SimpleDateFormat formatter = getSimpleDateFormat(DATETIME_FORMAT);
        Date date = null;
        try {
            date = formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获得某天最大时间 2018-8-15 23:59:59
     *
     * @param date
     * @return
     */
    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        ;
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获得某天最小时间 2018-8-15 00:00:00
     *
     * @param date
     * @return
     */
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取上周一
     *
     * @param date
     * @return
     */
    public static Date geLastWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, -7);
        return cal.getTime();
    }

    /**
     * 获取本周一
     *
     * @param date
     * @return
     */
    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    public static String getDate(String format) {
        return getDate(new Date(), format);
    }

    public static String getDate(Date date) {
        return getDate(date, DATETIME_FORMAT);
    }

    public static String getDate() {
        return getDate(new Date(), DATETIME_FORMAT);
    }

    public static String getDate(Date date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat formatter = getSimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String formatDate(long time, String format) {
        SimpleDateFormat _df = getSimpleDateFormat(format);
        return _df.format(new Date(time));
    }


    public static LocalDateTime parse(CharSequence text) {
        return LocalDateTime.parse(text, ISO_LOCAL_DATE_TIME);
    }

    public static long toMillis(LocalDateTime ldt) {
        return ldt.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
    }

    public static String now() {
        return LocalDateTime.now().format(ISO_LOCAL_DATE_TIME);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }

    /**
     * 得到时间 yyyy.mm 包含当前月份
     *
     * @param num 当前月往前推的月数
     * @return yyyy.mm 最早月份
     */
    public static List<String> getYearAndMoth(int num) {
        List<String> list = Lists.newArrayList();
        for (int i = num - 1; i >= 0; i--) {
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MONTH, -i + 2);
            date = calendar.getTime();
            list.add(getDate(date, DATETIME_MONTH));
        }
        return list;
    }

    /**
     * 获取上个季度的第一天
     *
     * @return
     */
    public static Date getLastQuarterFirstDay() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.MONTH, ((int) startCalendar.get(Calendar.MONTH) / 3 - 1) * 3);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        return startCalendar.getTime();
    }

    /**
     * 获取上个季度的最后一天
     *
     * @return
     */
    public static Date getLastQuarterLastDay() {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.MONTH, ((int) endCalendar.get(Calendar.MONTH) / 3 - 1) * 3 + 2);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        endCalendar.set(Calendar.HOUR_OF_DAY, endCalendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        endCalendar.set(Calendar.MINUTE, endCalendar.getActualMaximum(Calendar.MINUTE));
        endCalendar.set(Calendar.SECOND, endCalendar.getActualMaximum(Calendar.SECOND));
        endCalendar.set(Calendar.MILLISECOND, endCalendar.getActualMaximum(Calendar.MILLISECOND));
        return endCalendar.getTime();
    }

    /**
     * 获取上个月的最后一天
     *
     * @return
     */
    public static Date getPreMonthLastDay() {
        Calendar calendar = Calendar.getInstance();
        //月份减1
        calendar.add(Calendar.MONTH, -1);
        //计算当月最大天数
        int maxCurrentMonthDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设为月末最后一天
        calendar.set(Calendar.DAY_OF_MONTH, maxCurrentMonthDay);
        return calendar.getTime();
    }

    /**
     * 计算两个时间相差多少个月
     *
     * @param befDate
     * @param aftDate
     * @return
     */
    public static int calculateMonth(Date befDate, Date aftDate) {
        Calendar bef = Calendar.getInstance();
        bef.setTime(befDate);
        Calendar aft = Calendar.getInstance();
        aft.setTime(aftDate);
        int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
        int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
        return Math.abs(month + result);
    }

    /**
     * 返回指定日期的季的最后一天
     *
     * @return
     */
    public static Date getLastDayOfQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getLastDayOfQuarter(calendar.get(Calendar.YEAR),
                getQuarterOfYear(date));
    }

    /**
     * 返回指定日期的季度
     *
     * @param date
     * @return
     */
    public static int getQuarterOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) / 3 + 1;
    }

    /**
     * 返回指定年季的季的最后一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static Date getLastDayOfQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 3 - 1;
        } else if (quarter == 2) {
            month = 6 - 1;
        } else if (quarter == 3) {
            month = 9 - 1;
        } else if (quarter == 4) {
            month = 12 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastDayOfMonth(year, month);
    }

    /**
     * 返回指定年月的月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

}
