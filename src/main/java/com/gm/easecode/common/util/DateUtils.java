package com.gm.easecode.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class DateUtils {

	private static Log logger = LogFactory.getLog(DateUtils.class);
    public static final int YEAR_RETURN = 0;
    public static final int MONTH_RETURN = 1;
    public static final int DAY_RETURN = 2;
    public static final int HOUR_RETURN = 3;
    public static final int MINUTE_RETURN = 4;
    public static final int SECOND_RETURN = 5;
    public static final String P_yyyyMMdd = "yyyyMMdd";
    public static final String P_yyyyMM = "yyyyMM";
    public static final String P_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String P_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
    public static final String P_yyyy_MM_dd_HH = "yyyy-MM-dd HH";
    public static final String P_yyyy_MM_dd = "yyyy-MM-dd";
    public static final String P_yyyy_M_d = "yyyy-M-d";
    public static final String P_yyyyMd = "yyyyMd";
    public static final String P_yyyy_MM = "yyyy-MM";
    public static final String P_yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String P_yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
    public static final String P_HH_mm_ss = "HH:mm:ss";
    public static final String P_HHmmssSSS = "HHmmssSSS";

    /**
     * 得到两个时间相差时间(年/月/日/时/分/秒/)
     * 
     * @param beginTime
     * @param endTime
     * @param formatPattern
     * @param returnPattern
     * @return
     * @throws ParseException
     */
    public static long getBetween(Date beginDate, Date endDate, int returnPattern) {
        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDate);
        endCalendar.setTime(endDate);
        switch (returnPattern) {
        case YEAR_RETURN:
            return DateUtils.getByField(beginCalendar, endCalendar, Calendar.YEAR);
        case MONTH_RETURN:
            return DateUtils.getByField(beginCalendar, endCalendar, Calendar.YEAR) * 12 + DateUtils.getByField(beginCalendar, endCalendar, Calendar.MONTH);
        case DAY_RETURN:
            return DateUtils.getTime(beginDate, endDate) / (24 * 60 * 60 * 1000);
        case HOUR_RETURN:
            return DateUtils.getTime(beginDate, endDate) / (60 * 60 * 1000);
        case MINUTE_RETURN:
            return DateUtils.getTime(beginDate, endDate) / (60 * 1000);
        case SECOND_RETURN:
            return DateUtils.getTime(beginDate, endDate) / 1000;
        default:
            return 0;
        }

    }

    /**
     * 得到两个时间相差时间(年/月/日/时/分/秒/)
     * 
     * @param beginTime
     * @param endTime
     * @param formatPattern
     * @param returnPattern
     * @return
     * @throws ParseException
     */
    public static long getBetween(String beginTime, String endTime, String formatPattern, int returnPattern) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatPattern);
        Date beginDate = simpleDateFormat.parse(beginTime);
        Date endDate = simpleDateFormat.parse(endTime);

        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDate);
        endCalendar.setTime(endDate);
        switch (returnPattern) {
        case YEAR_RETURN:
            return DateUtils.getByField(beginCalendar, endCalendar, Calendar.YEAR);
        case MONTH_RETURN:
            return DateUtils.getByField(beginCalendar, endCalendar, Calendar.YEAR) * 12 + DateUtils.getByField(beginCalendar, endCalendar, Calendar.MONTH);
        case DAY_RETURN:
            return DateUtils.getTime(beginDate, endDate) / (24 * 60 * 60 * 1000);
        case HOUR_RETURN:
            return DateUtils.getTime(beginDate, endDate) / (60 * 60 * 1000);
        case MINUTE_RETURN:
            return DateUtils.getTime(beginDate, endDate) / (60 * 1000);
        case SECOND_RETURN:
            return DateUtils.getTime(beginDate, endDate) / 1000;
        default:
            return 0;
        }
    }

    private static long getByField(Calendar beginCalendar, Calendar endCalendar, int calendarField) {
        return endCalendar.get(calendarField) - beginCalendar.get(calendarField);
    }

    private static long getTime(Date beginDate, Date endDate) {
        return endDate.getTime() - beginDate.getTime();
    }

    /**
     * 字符串转换为日期（包含小时分)
     * 
     * @param str 日期字符串
     * @param pattern 时间模式 为null时默认为“yyy-MM-dd HH:mm:ss”
     * @return Date 返回 pattern形式的日期
     */
    public final static Date StrToDateTime(String str, String pattern) {
        Date returnDate = null;
        if (pattern == null) {
            pattern = P_yyyy_MM_dd_HH_mm_ss;
        }
        if(str == null || str.trim().length() == 0)
        {
        	return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            returnDate = sdf.parse(str);
        } catch (Exception e) {
        	logger.debug("时间格式化异常-->" + str + "-->" + pattern + "-->" + e.getMessage());
            return returnDate;
        }
        return returnDate;
    }

    /**
     * 字符串yymmddhhmm转换为日期（包含小时分）
     * 
     * @param str
     * @return Date
     */
    public final static Date StringToDateTime(String str) {
        StringBuffer tmpStr = new StringBuffer();
        tmpStr.append(getThisYear().substring(0, 2)).append(str.substring(0, 2)).append("-").append(str.substring(2, 4)).append("-").append(str.substring(4, 6)).append(" ").append(str.substring(6, 8))
                .append(":").append(str.substring(8, 10));
        return StrToDateTime(tmpStr.toString());
    }

    /**
     * 返回当前年份
     * @return
     */
    public final static String getThisYear() {
        return getCurrDateTime("yyyy");
    }

    /**
     * 字符串转换为日期（包含小时分）,格式:yyyy-MM-dd HH:mm:ss
     * 
     * @param str
     * @return 
     */
    public final static Date StrToDateTime(String str) {
        if (str != null && str.trim().length() > 0) {
        	int strLength = str.length();
            if (strLength < 11) {
                str += " 00:00:00";
            } else if (strLength > 11 && strLength < 14) {
                str += ":00:00";
            } else if (strLength > 14 && strLength < 17) {
                str += ":00";
            }
        }
        return StrToDateTime(str, P_yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * 字符串转为日期
     * 
     * @param str
     * @return
     */
    public final static Date StrToDate(String str) {
    	return StrToDateTime(str, P_yyyy_MM_dd);
    }

    /**
     * 将日期时间转换成 pattern 格式的时间
     * 
     * @param date 要转换的日期时间
     * @param pattern 时间模式 为null时默认为“yyyyMMdd”
     * @return String 转换后的日期时间
     */
    public final static String getDateTimeStr(Date date, String pattern) {
        if (pattern == null || pattern.trim().length() == 0)
            pattern = P_yyyyMMdd;
        return getDateTime(date, pattern);
    }

    /**
     * 将日期转换成yyy-MM-dd的格式
     * 
     * @param date 要转换的日期
     * @return String 转换后的日期
     */
    public final static String getStrDate(Date date) {
        return getDateTime(date, P_yyyy_MM_dd);
    }
    
    /**
     * 将日期时间转换成 pattern 格式的时间
     * 
     * @param date 要转换的日期时间
     * @param pattern 时间模式 为null时默认为“yyy-MM-dd HH:mm:ss”
     * @return String 转换后的日期时间
     */
    public final static String getDateTime(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        if (pattern == null || pattern.trim().length() == 0)
            pattern = P_yyyy_MM_dd_HH_mm_ss;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 将日期转换成yyyyMM格式
     * 
     * @param date 要转换的日期
     * @return 转换后的月份
     */
    public final static String getStrYearMonth(Date date) {
        return getDateTime(date, P_yyyyMM);
    }

    /**
     * 将日期转换成yyyyMM格式月份
     * 
     * @param date
     * @return
     */
    public final static int getNumYearMonth(Date date) {
        String yearMonth = getStrYearMonth(date);
        if (yearMonth == "") {
            return 0;
        } else {
            return Integer.parseInt(yearMonth);
        }
    }

    /**
     * 将日期时间转换成yyy-MM-dd HH:mm:ss的格式
     * 
     * @param date 要转换的日期时间
     * @return 转换后的日期时间
     */
    public final static String getStrDateTime(Date date) {
        return getDateTime(date, P_yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * 获得当前日期
     * 
     * @return 当前日期，格式：yyyy-MM-dd
     */
    public final static String getCurrStrDate() {
        return getCurrDateTime( P_yyyy_MM_dd);
    }

    /**
     * 获得当前日期和时间
     * 
     * @return 当前日期和时间，格式：yyyy-MM-dd HH:mm:ss
     */
    public final static String getCurrStrDateTime() {
        return getCurrDateTime(P_yyyy_MM_dd_HH_mm_ss);
    }

    public final static String getCurrDateTime(String pattern) {
    	return getDateTime(new Date(), pattern);
    }

    /**
     * 获得当前日期 yyyy-MM-dd 00:00:00
     * @return
     */
    public final static Date getCurrDate() {
        return StrToDateTime(getCurrStrDate(), P_yyyy_MM_dd);
    }

    /**
     * 获得当前时间 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public final static Date getCurrDatetime() {
        return StrToDateTime(getCurrStrDateTime(), P_yyyy_MM_dd_HH_mm_ss);
    }

    public static String conver2Date(String str) {
        String date = null;
        if (str == null || str.trim().length() == 0) {
            return date;
        }
        String pattern = P_yyyy_MM_dd_HH_mm_ss;
        date = convertDateStrToStr(str, P_yyyy_MM_dd_HH_mm_ss, pattern);
        if (date != null) {
            return date;
        }
        date = convertDateStrToStr(str, P_yyyy_MM_dd_HH_mm, pattern);
        if (date != null) {
            return date;
        }
        date = convertDateStrToStr(str, P_yyyy_MM_dd_HH, pattern);
        if (date != null) {
            return date;
        }
        date = convertDateStrToStr(str, P_yyyy_MM_dd, pattern);
        if (date != null) {
            return date;
        }
        date = convertDateStrToStr(str, P_yyyyMMdd, pattern);
        if (date != null) {
            return date;
        }
        date = convertDateStrToStr(str, P_yyyy_M_d, pattern);
        if (date != null) {
            return date;
        }
        date = convertDateStrToStr(str, P_yyyyMd, pattern);
        if (date != null) {
            return date;
        }
        return date;
    }

    public static String convertDateStrToStr(String dateStr, String oldPattern, String newPattern) {
        try {
            SimpleDateFormat nowDate = new SimpleDateFormat(oldPattern);
            return getDateTime(nowDate.parse(dateStr), newPattern);
        } catch (Exception e) {
        	logger.debug("时间格式化异常-->" + dateStr + ",old=" + oldPattern + ",new=" + newPattern + "-->" + e.getMessage());
        }
        return null;
    }

    /**
     * 当前日期加减amount天，然后按pattern格式返回
     * 
     * @param pattern
     * @param amount
     * @return
     */
    public static String addCurrDateToStr(String pattern, int amount) {
        Calendar currCal = Calendar.getInstance();
        currCal.add(Calendar.DAY_OF_MONTH, amount);
        SimpleDateFormat nowDate = new SimpleDateFormat(pattern);
        return nowDate.format(currCal.getTime());
    }

    /**
     * 得到下半月开始日期
     * 
     * @param date
     * @return
     */
    public static Date getNextHalfMonthDate(Date date, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (day < 16) {
            cal.set(Calendar.DAY_OF_MONTH, 16);
        } else {
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        }

        return StrToDateTime(getDateTime(cal.getTime(), pattern), pattern);
    }

    /**
     * 获取下月第一天
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static Date getNextMonthFirstDate(Date date, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return StrToDateTime(getDateTime(cal.getTime(), pattern), pattern);
    }

    /**
     * 获取本月最后一天
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static Date getMonthLastDate(Date date, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getNextMonthFirstDate(date, pattern));
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);
        return StrToDateTime(getDateTime(cal.getTime(), pattern), pattern);
    }

    /**
     * 获取本月第一天
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static Date getMonthFirstDate(Date date, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return StrToDateTime(getDateTime(cal.getTime(), pattern), pattern);
    }

    /**
     * 获取上月第一天
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static Date getLastMonthFirstDate(Date date, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return StrToDateTime(getDateTime(cal.getTime(), pattern), pattern);
    }

    /**
     * 获取上月最后一天
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static Date getLastMonthLastDate(Date date, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getMonthFirstDate(date, pattern));
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);
        return StrToDateTime(getDateTime(cal.getTime(), pattern), pattern);
    }
    /**
     * 获取从当前向前1个月的时间
     * @param pattern
     * @return
     */
    public static String getLastOneMonth(String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -31);
        return getDateTime(cal.getTime(), pattern);
    }

    /**
     * 比较两个日期是否是同一天；
     * 
     * @param day1
     * @param day2
     * @return
     */
    public static boolean isSameDay(Date day1, Date day2) {
        SimpleDateFormat sdf = new SimpleDateFormat(P_yyyy_MM_dd);
        String ds1 = sdf.format(day1);
        String ds2 = sdf.format(day2);
        if (ds1.equals(ds2)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 当前日期加减amount天，然后按pattern格式返回
     * 
     * @param pattern
     * @param amount
     * @return
     */
    public static Date addCurrDate(int amount) {
        Calendar currCal = Calendar.getInstance();
        currCal.add(Calendar.DAY_OF_MONTH, amount);
        return currCal.getTime();
    }

    /**
     * 日期加减amount天
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addDate(Date date, int amount) {
        Calendar currCal = Calendar.getInstance();
        currCal.setTime(date);
        currCal.add(Calendar.DAY_OF_MONTH, amount);
        return currCal.getTime();
    }

    /**
     * 获取(stopDate-startDate)的间隔天数
     * 
     * @param startDateStr yyyy-MM-dd
     * @param stopDateStr yyyy-MM-dd
     * @return
     */
    public static long getIdleDay(String startDateStr, String stopDateStr) {
        Date sDate = StrToDate(startDateStr);
        Date eDate = StrToDate(stopDateStr);
        return getIdleDay(sDate, eDate);
    }

    /**
     * 获取(stopDate-startDate)的间隔天数
     * 
     * @param startDateStr yyyy-MM-dd
     * @param stopDateStr yyyy-MM-dd
     * @return
     */
    public static long getIdleDay(Date sDate, Date eDate) {
        if (sDate == null || eDate == null) {
            return -1;
        }
        long idleDay = (eDate.getTime() - sDate.getTime()) / 1000 / 3600 / 24;

        return idleDay;
    }

    /**
     * 判断HH:MM 时间合法性
     * 
     * @param hhss
     * @return
     */
    public static boolean isLegalHHMM(String hhss) {
        String reg = "(^([0-1][0-9])|2[0-3]):[0-5][0-9]$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(hhss);
        return matcher.matches();
    }

    /**
     * 获取当前系统上一月字符串：格式：yyyy-mm
     * 
     * @return
     */
    public static String getPreviousMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        return getDateTime(cal.getTime(), P_yyyy_MM);
    }

    /**
     * 获取当前月字符串，返回格式：yyyy-MM
     * @return
     */
    public static String getCurrMonth() {
        return getCurrDateTime(P_yyyy_MM);
    }

    /**
     * 获取根据月字符串的上一个月字符串，返回格式：yyyy-MM
     * @param month yyyy-MM
     * @return
     */
    public static String getPreviousMonth(String month) {
        return getPreviousMonth(StrToDateTime(month, P_yyyy_MM));
    }

    /**
     * 数字月份加减月数
     * 
     * @param month
     * @param num
     * @return
     */
    public static int addMonth(int month, int num) {
        return Integer.parseInt(addMonth(Integer.toString(month), num, P_yyyyMM));
    }

    /**
     * 月数加月数，返回月份
     * 
     * @param month
     * @param num
     * @return 月份(yyyy-MM)
     */
    public static String addMonth(String month, int num) {
        return addMonth(StrToDateTime(month, P_yyyy_MM), num);
    }

    /**
     * 月份加月数，返回月份
     * 
     * @param month 格式由pattern指定
     * @param num
     * @param pattern
     * @return 返回格式由pattern格式指定
     */
    public static String addMonth(String month, int num, String pattern) {
        return addMonth(StrToDateTime(month, pattern), num, pattern);
    }

    /**
     * 日期加月份，返回yyyy-MM
     * 
     * @param date
     * @param num
     * @return yyyy-MM格式
     */
    public static String addMonth(Date date, int num) {
        return addMonth(date, num, P_yyyy_MM);
    }

    /**
     * 日期加月数，返回月份
     * 
     * @param date
     * @param num
     * @param pattern
     * @return
     */
    public static String addMonth(Date date, int num, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, num);
        return getDateTime(cal.getTime(), pattern);
    }

    /**
     * 根据日期的上一个月月份，返回yyyy-MM
     * @param date
     * @return
     */
    public static String getPreviousMonth(Date date) {
        return addMonth(date, -1);
    }

    /**
     * 获取月底字符串
     * 
     * @param month 月，格式：yyyy-MM
     * @return 月底字符串，格式：yyyy-MM-dd
     */
    public static String getEndMonth(String month) {
        Date date = StrToDateTime(month + "-01", P_yyyy_MM_dd);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        return getDateTime(cal.getTime(), P_yyyy_MM_dd);
    }

    /**
     * 获取当前是第几周
     * 
     * @param date
     * @return
     */
    public static int getCurrWeekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * @throws ParseException
     */
    public static Date getDate(Long time) {
        SimpleDateFormat format = new SimpleDateFormat(P_yyyy_MM_dd_HH_mm_ss_SSS);
        String d = format.format(time);
        Date date = null;
        try {
            date = format.parse(d);
        } catch (ParseException e) {
        	logger.debug("时间格式化异常-->" + time + "-->" + e.getMessage());
        }
        return date;
    }

    /**
     * 判断当前时间是否在指定时间之间
     * 
     * @param before
     * @param after
     * @return
     */
    public static boolean currDateMiddleOfDate(Date before, Date after) {
        if (before == null && after == null) {
            return true;
        }
        Date currDate = new Date();
        if ((before == null || (before != null && currDate.after(before))) && (after == null || (after != null && currDate.before(after)))) {
            return true;
        }
        return false;
    }

    /**
     * 时间转换
     * 
     * @param time 需要转换的时间，单位：秒
     * @return HH:mm:ss
     */
    public static String convertTime2Str(long time) {
        if (time == 0) {
            return "00:00:00";
        }
        Calendar cal = Calendar.getInstance();
        cal.set(0, 0, 0, 0, 0, (int) time);
        return getDateTime(cal.getTime(), P_HH_mm_ss);
    }

    public static String convertTime2Str(long time, String pattern) {
        if (pattern == null || pattern.trim().length() == 0) {
            pattern = P_yyyy_MM_dd_HH_mm_ss;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return getDateTime(cal.getTime(), pattern);
    }

    public static boolean isDateBefore(Date date2) throws ParseException {

        Date date = new Date();
        return date.before(date2);

    }

    /**
     * 时间转换（将时间转换成秒）
     * 
     * @param time HH:mm:ss
     * @return 秒
     */
    public static int convertStr2Time(String time) {
        if (time == null || time.trim().length() == 0) {
            return 0;
        }
        try {
            Date currDate = StrToDateTime(time, P_HH_mm_ss);
            Calendar cal = Calendar.getInstance();
            cal.setTime(currDate);
            return cal.get(Calendar.HOUR) * 3600 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
        } catch (Exception e) {
        	logger.debug("时间格式化异常-->" + time + "-->" + e.getMessage());
            return 0;
        }
    }

    public static BigInteger getDateNum(Date date) {
        if (date == null) {
            return new BigInteger("0");
        }
        SimpleDateFormat format = new SimpleDateFormat(P_yyyyMMddHHmmssSSS);
        String dd = format.format(date);
        BigInteger num = new BigInteger(dd);
        return num;
    }

    public static int getTimeNumber(Date involveTime) {
        if (involveTime == null) {
            return 0;
        }
        SimpleDateFormat format = new SimpleDateFormat(P_HHmmssSSS);
        String dd = format.format(involveTime);
        int num = Integer.valueOf(dd);
        return num;
    }

    public static long getCountDown() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(P_yyyy_MM_dd_HH_mm);
        SimpleDateFormat format2 = new SimpleDateFormat(P_yyyy_MM_dd_HH_mm_ss);
        String dd = format.format(date);
        StringBuffer bf = new StringBuffer(dd);
        bf.append(":00");
        String ddd = bf.toString();
        try {
            date = format2.parse(ddd);
        } catch (ParseException e) {
        	logger.debug("时间格式化异常-->" + ddd + "-->" + e.getMessage());
        }
        long now = date.getTime();
        long time = now + 1 * 60 * 1000;
        return time;
    }
    /**
     * 获取今天的最后时间
     * @return
     */
    public static Date getTodayLastTime()
    {
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.HOUR_OF_DAY, 23);
    	cal.set(Calendar.MINUTE, 59);
    	cal.set(Calendar.SECOND, 59);
    	cal.set(Calendar.MILLISECOND, 999);
    	return cal.getTime();
    }
    /**
     * 获取指定时间的开始时间
     * @param date
     * @return
     */
    public static Date getFirstTime(Date date)
    {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);
    	return cal.getTime();
    }
    /**
     * 获取指定时间的最后时间
     * @param date
     * @return
     */
    public static Date getLastTime(Date date)
    {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.set(Calendar.HOUR_OF_DAY, 23);
    	cal.set(Calendar.MINUTE, 59);
    	cal.set(Calendar.SECOND, 59);
    	cal.set(Calendar.MILLISECOND, 999);
    	return cal.getTime();
    }
    /**
     * 获取指定时间的最后时间(精确到秒)
     * @param date
     * @return
     */
    public static Date getLastTimeExactSecond(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);//MySQL数据库对于毫秒大于500的数据进行进位,造成存储日期翻天
        return cal.getTime();
    }
    /**
     * 获取明天的开始时间
     * @return
     */
    public static Date getTomorrowFirstTime()
    {
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DAY_OF_MONTH, 1);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);
    	return cal.getTime();
    }
    /**
     * 获取明天的开始时间(延迟时间)
     * @param second	延迟的时长，单位：秒
     * @return
     */
    public static Date getTomorrowFirstTimeWithDelay(int second)
    {
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DAY_OF_MONTH, 1);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);
    	//延迟的时长
    	cal.add(Calendar.SECOND, second);
    	return cal.getTime();
    }
    /**
     * 将时间按段拆分
     * @param beginDate		开始时间
     * @param endDate		结束时间
     * @param splitType		1：按天拆分，2：按周拆分，3：按月拆分，4：按年拆分
     * @param pattern		返回时间格式
     * @return				[0]：时间段的开始时间，[1]：时间段的结束时间
     */
    public static List<String[]> splitTime(Date beginDate, Date endDate, int splitType, String pattern)
    {
    	Calendar start = Calendar.getInstance(Locale.CHINESE);
    	start.setFirstDayOfWeek(Calendar.MONDAY);
    	start.setTime(beginDate);
    	start.set(Calendar.HOUR_OF_DAY, 0);
    	start.set(Calendar.MINUTE, 0);
    	start.set(Calendar.SECOND, 0);
    	start.set(Calendar.MILLISECOND, 0);
    	Calendar end = Calendar.getInstance(Locale.CHINESE);
    	end.setFirstDayOfWeek(Calendar.MONDAY);
    	end.setTime(endDate);
    	end.set(Calendar.HOUR_OF_DAY, 0);
    	end.set(Calendar.MINUTE, 0);
    	end.set(Calendar.SECOND, 0);
    	end.set(Calendar.MILLISECOND, 0);
    	int growType = Calendar.DAY_OF_YEAR;
    	switch(splitType)
    	{
    		case 1:
    			growType = Calendar.DAY_OF_YEAR;
    			break;
    		case 2:
    			growType = Calendar.WEEK_OF_YEAR;
    			break;
    		case 3:
    			growType = Calendar.MONTH;
    			break;
    		case 4:
    			growType = Calendar.YEAR;
    			break;
    	}
    	List<String[]> retList = new ArrayList<String[]>();
    	while(start.compareTo(end) <= 0)
    	{
    		String[] values = new String[2];
    		values[0] = getDateTime(start.getTime(), pattern);
    		int startValue = start.get(growType);
    		start.add(growType, 1);
    		if(start.after(end))
    		{
    			start.setTime(end.getTime());
    		}
    		int endValue = start.get(growType);
    		if(startValue == endValue)
			{
				values[1] = getDateTime(start.getTime(), pattern);
			}
			else
			{
				while(startValue != endValue)
				{
					start.add(Calendar.DAY_OF_YEAR, -1);
					endValue = start.get(growType);
					if(startValue == endValue)
	    			{
						values[1] = getDateTime(start.getTime(), pattern);
						break;
	    			}
				}
			}
    		start.add(Calendar.DAY_OF_YEAR, 1);
    		retList.add(values);
    	}
    	return retList;
    }
    /**
     * 计算当前时间添加指定周期时间
     * @param cycleType		周期类型，1：小时，2：天，3：周，4：月，5：年
     * @param cycleValue	周期值
     * @param naturalCycle	是否自然周期，如是自然周期，则会包含当前小时、天、周、月、年
     * @return
     */
    public static Date addCurrDate(int cycleType, int cycleValue, boolean naturalCycle)
    {
    	Calendar cal = Calendar.getInstance();
		switch(cycleType)
		{
			case 1:
				cal.add(Calendar.HOUR_OF_DAY, cycleValue);
				if(naturalCycle)//自然小时，包含当前小时，且结果日期为上一小时的最后时间
				{
					cal.set(Calendar.MINUTE, 0);
					cal.add(Calendar.MINUTE, -1);
					cal.set(Calendar.SECOND, 59);
			    	cal.set(Calendar.MILLISECOND, 999);
				}
				break;
			case 2:
				cal.add(Calendar.DAY_OF_YEAR, cycleValue);
				if(naturalCycle)//自然天，包含当前天，且结果日期为昨天的最后时间
				{
					cal.add(Calendar.DAY_OF_YEAR, -1);
					cal.set(Calendar.HOUR_OF_DAY, 23);
			    	cal.set(Calendar.MINUTE, 59);
			    	cal.set(Calendar.SECOND, 59);
			    	cal.set(Calendar.MILLISECOND, 999);
				}
				break;
			case 3:
				cal.add(Calendar.WEEK_OF_YEAR, cycleValue);
				if(naturalCycle)//自然周，包含当前周，且结果日期为上周的最后一天
				{
					cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					cal.add(Calendar.DAY_OF_YEAR, -1);
					cal.set(Calendar.HOUR_OF_DAY, 23);
			    	cal.set(Calendar.MINUTE, 59);
			    	cal.set(Calendar.SECOND, 59);
			    	cal.set(Calendar.MILLISECOND, 999);
				}
				break;
			case 4:
				cal.add(Calendar.MONTH, cycleValue);
				if(naturalCycle)//自然月，包含当前月，且结果日期为上月的最后一天
				{
					cal.set(Calendar.DAY_OF_MONTH, 1);
					cal.add(Calendar.DAY_OF_YEAR, -1);
					cal.set(Calendar.HOUR_OF_DAY, 23);
			    	cal.set(Calendar.MINUTE, 59);
			    	cal.set(Calendar.SECOND, 59);
			    	cal.set(Calendar.MILLISECOND, 999);
				}
				break;
			case 5:
				cal.add(Calendar.YEAR, cycleValue);
				if(naturalCycle)//自然月，包含当前月，且结果日期为上月的最后一天
				{
					cal.set(Calendar.MONTH, Calendar.JANUARY);
					cal.set(Calendar.DAY_OF_MONTH, 1);
					cal.add(Calendar.DAY_OF_YEAR, -1);
					cal.set(Calendar.HOUR_OF_DAY, 23);
					cal.set(Calendar.MINUTE, 59);
					cal.set(Calendar.SECOND, 59);
					cal.set(Calendar.MILLISECOND, 999);
				}
				break;
		}
		return cal.getTime();
    }
    /**
     * 计算当前时间添加指定周期时间，并且以指定周期时间的结束时间
     * 如：天，则返回指定天的最后时间，周：则返回指定周的最后一天，月：则返回指定月的最后一天，年：则返回指定年的最天一天
     * @param cycleType		周期类型，1：小时，2：天，3：周，4：月，5：年
     * @param cycleValue	周期值
     * @return
     */
    public static Date addCurrDateForLastCycleTime(int cycleType, int cycleValue)
    {
    	return addCurrDate(cycleType, cycleValue + 1, true);
    }
    /**
     * 计算当前时间添加指定周期时间，并且以指定周期时间的当天的结束时间
     * @param cycleType		周期类型，1：小时，2：天，3：周，4：月，5：年
     * @param cycleValue	周期值
     * @return
     */
    public static Date addCurrDateForLastDayTime(int cycleType, int cycleValue)
    {
    	Calendar cal = Calendar.getInstance();
		switch(cycleType)
		{
			case 1:
				cal.add(Calendar.HOUR_OF_DAY, cycleValue);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
		    	cal.set(Calendar.MILLISECOND, 999);
				break;
			case 2:
				cal.add(Calendar.DAY_OF_YEAR, cycleValue);
				cal.set(Calendar.HOUR_OF_DAY, 23);
		    	cal.set(Calendar.MINUTE, 59);
		    	cal.set(Calendar.SECOND, 59);
		    	cal.set(Calendar.MILLISECOND, 999);
				break;
			case 3:
				cal.add(Calendar.WEEK_OF_YEAR, cycleValue);
				cal.set(Calendar.HOUR_OF_DAY, 23);
		    	cal.set(Calendar.MINUTE, 59);
		    	cal.set(Calendar.SECOND, 59);
		    	cal.set(Calendar.MILLISECOND, 999);
				break;
			case 4:
				cal.add(Calendar.MONTH, cycleValue);
				cal.set(Calendar.HOUR_OF_DAY, 23);
		    	cal.set(Calendar.MINUTE, 59);
		    	cal.set(Calendar.SECOND, 59);
		    	cal.set(Calendar.MILLISECOND, 999);
				break;
			case 5:
				cal.add(Calendar.YEAR, cycleValue);
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				cal.set(Calendar.MILLISECOND, 999);
				break;
		}
		return cal.getTime();
    }
    
    /**
     * 将日期时间转换成 pattern 格式的格林时间
     * @param date
     * @param pattern
     * @return
     */
    public final static String getGMTDateTime(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        if (pattern == null)
            pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); //设置时区为GMT
        return sdf.format(date);
    }
    
    public final static String getGMTDateTime(Date date) {
        return getGMTDateTime(date, null);
    }
    public final static String getCurrGMTDateTime()
    {
        return getCurrGMTDateTime(null);
    }
    public final static String getCurrGMTDateTime(String pattern)
    {
        return getGMTDateTime(new Date(), pattern);
    }
    /**
     * 获取当前时间的季度
     * @param date
     * @return
     */
    public final static int getQuarter(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        return BigDecimal.valueOf(month).divide(BigDecimal.valueOf(3), 0, RoundingMode.CEILING).intValue();
    }
    
    public static void main(String[] args)
    {
//    	int cycleType = 5;
//    	int cycleValue = 1;
//    	String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
//    	while(cycleType > 0)
//    	{
//	    	System.out.println(getDateTime(addCurrDateForLastDayTime(cycleType, cycleValue), pattern));
//	    	cycleType--;
//    	}
        String pattern = "yyyy-MM-dd HH:mm:ss";
    	String handlerTime = "2020-06-17 16:35:15";
    	Date handlerDate = DateUtils.StrToDateTime(handlerTime, "yyyy-MM-dd HH:mm");
    	System.out.println(handlerDate);
    	try {
    	    long diffMinu = getBetween(handlerTime, getCurrDateTime(pattern), "yyyy-MM-dd HH:mm", MINUTE_RETURN);
    	    System.out.println(diffMinu);
    	} catch (Exception e) {}
    }
}
