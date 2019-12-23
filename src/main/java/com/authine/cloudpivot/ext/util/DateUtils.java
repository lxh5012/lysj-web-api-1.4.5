package com.authine.cloudpivot.ext.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * 日期公共处理类
 * add by 2016.06.07
 *
 * @author 唐望
 */
public class DateUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    private Date now;

    /**
     * 获取系统当前日期
     * （注：setNow()不设置日期的情况下）
     *
     * @return
     * @author 唐望
     */
    public Date getNow() {
        return now;
    }

    /**
     * 设置日期
     *
     * @return
     * @author 唐望
     */
    public void setNow(Date now) {
        this.now = now;
    }

    /**
     * 构造方法，初始化now时间
     */
    public DateUtils() {
        now = new Date();
    }

    //最小日期常量
    private static final String MINVALUE = "1970-01-01 00:00:00";

    //最大日期常量
    private static final String MAXVALUE = "9998-12-31 00:00:00";

    //日期格式常量
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    //时间格式常量yyyy-MM-dd
    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_TIME_WITHOUT_SECOND = "yyyy-MM-dd HH:mm";

    public static final String TIME_FORMAT = "HH:mm:ss";

    //日期格式
    public static final String XG_DATE_FORMAT = "yyyy/MM/dd";

    /**
     * 获取去最小日期字符串
     *
     * @return
     * @author 唐望
     */
    public static String getMinValue() {
        return MINVALUE;
    }

    /**
     * 最小日期
     *
     * @author 唐望
     * @return
     */

    private static final Date MIN_DATE = getStringToDate(MINVALUE, DEFAULT_TIME_FORMAT);


    public static Date minValue() {
        return MIN_DATE;
    }

    /**
     * 获取去最大日期字符串
     *
     * @return
     * @author 唐望
     */
    public static String getMaxValue() {
        return MAXVALUE;
    }

    /**
     * 最小日期
     *
     * @author 唐望
     * @return
     */

    private static final Date MAX_DATE = getStringToDate(MAXVALUE, DEFAULT_TIME_FORMAT);

    public static Date maxValue() {

        return MAX_DATE;
    }

    /**
     * 将Date日期转换为指定格式字符串
     *
     * @param date 日期对象
     * @return 返回“yyyy-MM-dd”字符串
     * @author 唐望
     */
    public static String getDateToString(Date date) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        return sdf.format(date);

    }

    /**
     * 将Date时间转换为指定格式字符串
     *
     * @param date 日期对象
     * @return 返回“yyyy-MM-dd hh:mm:ss”字符串
     * @author 唐望
     */
    public static String getDateTimeToString(Date date) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
        return sdf.format(date);

    }

    /**
     * 将Date日期转换为指定格式字符串
     *
     * @param date    日期对象
     * @param pattern 格式模型（yyyy-MM-dd HH:mm:ss、yyyy年MM月dd日 HH时mm分ss秒 ...）
     * @return 返回对应格式字符串
     * @author 唐望
     */
    public static String getDateToString(Date date, String pattern) {
        if (date == null || "".equals(date)) {
            date = new Date();
        }
        if (pattern == null || "".equals(pattern)) {
            pattern = DEFAULT_TIME_FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 将字符串类型的时间转换为Date类型
     *
     * @param str     时间字符串
     * @param pattern 格式
     * @return 返回Date类型
     * update by zhangj
     * @author 唐望
     */
    public static Date getStringToDate(String strDate, String pattern) {
        if (strDate == null || strDate.isEmpty()) {
            return null;
        }

        if (pattern == null || pattern.isEmpty()) {
            pattern = DEFAULT_TIME_FORMAT;
        }
        if (strDate.length() == 28) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'CST' yyyy", Locale.US);
            //Fri Apr 27 20:36:42 SGT 2018
            if (strDate.indexOf("SGT") > 0) {
                strDate = strDate.replace("SGT", "CST");
            }
            //Fri Apr 27 20:36:42 GMT+08:00 2018
            else if (strDate.indexOf("GMT") > 0) {
                int gmtIndex = strDate.indexOf("GMT");
                strDate = (strDate.substring(0, gmtIndex)) + "CST" + (strDate.substring((gmtIndex + 9), strDate.length()));
            }
            Date date = null;
            try {
                date = sdf.parse(strDate);
            } catch (ParseException e1) {
                LOGGER.error("", e1);
            }
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            strDate = sdf.format(date);
        } else if (strDate.indexOf("T") > 0) {
            strDate = strDate.replace("T", " ");
        } else if (strDate.endsWith(".0")) {
            strDate = strDate.replace(".0", "");
        }

        pattern = DEFAULT_TIME_FORMAT.substring(0, strDate.trim().length());
        DateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(strDate);
        } catch (ParseException e) {
            LOGGER.error("", e);
            return null;
        }

    }

    /**
     * 将字符串类型的时间转换为Date类型
     *
     * @param strDate     时间字符串
     * @return 返回Date类型
     * update by zhangj
     * @author 唐望
     */
    public static Date getStringToDate(String strDate) {
        DateFormat format = DateFormat.getDateInstance();
        DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = null;
        // 需要捕获ParseException异常，如不要捕获，可以直接抛出异常，或者抛出到上层
        try {
            if (strDate != null && !strDate.isEmpty()) {
                strDate = GetTimeValueFromCST(strDate).trim();
                if (strDate.indexOf(" ") == -1) {
                    try {
                        time = format.parse(strDate);
                    } catch (Exception e) {
                        time = formatDate.parse(strDate);
                    }
                } else {
                    time = formatTime.parse(strDate);
                }
            }
        } catch (ParseException e) {
            LOGGER.warn("can not parse date : " + strDate);
        }
        return time;
    }

    /**
     * 日期加一天
     *
     * @param dateStr 日期字符串类型参数
     * @return
     * @author zhangj
     */
    public static Date getDatePlusOne(String dateStr) {
        Date date = DateUtils.getStringToDate(dateStr);
        return getDatePlusOne(date);
    }

    /**
     * 日期加一天
     *
     * @param date 日期类型参数
     * @return
     * @author zhangj
     */
    public static Date getDatePlusOne(Date date) {
        if (null != date) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);
            date = calendar.getTime();
        }
        return date;
    }

    /**
     * @param @param  date
     * @param @param  count
     * @param @return 设定文件
     * @return Date    返回类型
     * @throws
     * @Title: getDatePlus
     * @Description: 给指定的日期加上几天
     * @author zhangj
     * @date 2017年8月7日 下午8:56:48
     */
    public static Date getDatePlus(Date date, int count) {
        if (null != date) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, count);
            date = calendar.getTime();
        }
        return date;
    }

    /**
     * 日期加几天  "2019-11-12 00:00:00" --> "2019-11-15 00:00:00"
     * @param date
     * @param count
     * @return
     */
    public static String getDatePlus(String date, int count) {
        return getDateTimeToString(getDatePlus(getStringToDate(date), count));
    }

    public static String GetTimeValueFromCST(String strDate) {
        if (strDate.indexOf("CST") == -1) {
            return strDate;
        }
        String result = "";
        String[] arr = strDate.split(" ");
        if (arr.length < 6) {
            return strDate;
        }
        result = arr[5];
        switch (arr[1]) {
            case "Jan":
                result += "-01";
                break;
            case "Feb":
                result += "-02";
                break;
            case "Mar":
                result += "-03";
                break;
            case "Apr":
                result += "-04";
                break;
            case "May":
                result += "-05";
                break;
            case "Jun":
                result += "-06";
                break;
            case "Jul":
                result += "-07";
                break;
            case "Aug":
                result += "-08";
                break;
            case "Sep":
                result += "-09";
                break;
            case "Oct":
                result += "-10";
                break;
            case "Nov":
                result += "-11";
                break;
            default:
                result += "-12";
                break;
        }
        result += "-" + arr[2];
        result += " " + arr[3];
        return result;
        // Thu May 04 17:10:04 CST 2017
    }


    /**
     * 获取当前系统年份
     *
     * @return 返回int数值类型
     * @author 唐望
     */
    public static int getYear() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String month = sdf.format(date);
        return Integer.parseInt(month);
    }

    /**
     * 获取指定日期的年份
     *
     * @param date 日期对象
     * @return 返回int数值类型
     * @author 唐望
     */
    public static int getYear(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String month = sdf.format(date);
        return Integer.parseInt(month);
    }

    /**
     * 获取当前系统月份
     *
     *
     * @return 返回int数值类型
     * @author 唐望
     */
    public static int getMonth() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        String month = sdf.format(date);
        return Integer.parseInt(month);
    }

    /**
     * 获取指定日期的月份
     *
     * @param date 日期对象
     * @return 返回int数值类型
     * @author 唐望
     */
    public static int getMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        String month = sdf.format(date);
        return Integer.parseInt(month);
    }

    /**
     * 获取当前系统天
     *
     * @param date 日期对象
     * @return 返回int数值类型
     * @author 唐望
     */
    public static int getDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String month = sdf.format(date);
        return Integer.parseInt(month);
    }

    /**
     * 获取指定日期的天
     *
     * @param date 日期对象
     * @return 返回int数值类型
     * @author 唐望
     */
    public static int getDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String month = sdf.format(date);
        return Integer.parseInt(month);
    }

    /**
     * 获取当前系统小时
     *
     * @param date 日期对象
     * @return 返回int数值类型
     * @author 唐望
     */
    public static int getHour() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh");
        String hour = sdf.format(date);
        return Integer.parseInt(hour);
    }

    /**
     * 获取指定日期的小时
     *
     * @param date 日期对象
     * @return 返回int数值类型
     * @author 唐望
     */
    public static int getHour(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh");
        String hour = sdf.format(date);
        return Integer.parseInt(hour);
    }

    /**
     * 获取当前系统分钟数
     *
     *
     * @return 返回int数值类型
     * @author 唐望
     */
    public static int getMinute() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        String hour = sdf.format(date);
        return Integer.parseInt(hour);
    }

    /**
     * 获取指定日期的分钟数
     *
     * @param date 日期对象
     * @return 返回int数值类型
     * @author 唐望
     */
    public static int getMinute(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        String hour = sdf.format(date);
        return Integer.parseInt(hour);
    }

    /**
     * 获取当前系统秒钟数
     *
     * @param date 日期对象
     * @return 返回int数值类型
     * @author 唐望
     */
    public static int getSecond() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("ss");
        String hour = sdf.format(date);
        return Integer.parseInt(hour);
    }

    /**
     * 获取指定日期的秒钟数
     *
     * @param date 日期对象
     * @return 返回int数值类型
     * @author 唐望
     */
    public static int getSecond(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("ss");
        String hour = sdf.format(date);
        return Integer.parseInt(hour);
    }

    /**
     * 获取指定日期追加年份数后的日期
     *
     * @param date 日期对象
     * @param year 年份数
     * @return 返回Date日期类型
     * @author 唐望
     */
    public static Date addYears(Date date, int year) {
        Calendar clenr = Calendar.getInstance();
        clenr.setTime(date);
        clenr.add(Calendar.YEAR, year);

        return clenr.getTime();

    }


    /**
     * update by zhengyj 增加指定月份的方法
     *
     * @param date
     * @param Month
     * @return
     */
    public static Date addMonths(Date date, int Month) {
        Calendar clenr = Calendar.getInstance();
        clenr.setTime(date);
        clenr.add(Calendar.MONTH, Month);

        return clenr.getTime();

    }

    /**
     * 获取指定日期追加天数后的日期
     *
     * @param date 日期对象
     * @param day  天数
     * @return 返回Date日期类型
     * @author 唐望
     */
    public static Date addDates(Date date, int day) {
        Calendar clenr = Calendar.getInstance();
        clenr.setTime(date);
        clenr.add(Calendar.DATE, day);

        return clenr.getTime();

    }

    /**
     * 获取指定日期追加小时数后的日期
     *
     * @param date 日期对象
     * @param hour 小时数
     * @return 返回Date日期类型
     * @author 唐望
     */
    public static Date addHours(Date date, int hour) {
        Calendar clenr = Calendar.getInstance();
        clenr.setTime(date);
        clenr.add(Calendar.HOUR, hour);

        return clenr.getTime();

    }

    /**
     * 获取指定日期追加分钟数后的日期
     *
     * @param date   日期对象
     * @param second 分钟数
     * @return 返回Date日期类型
     * @author 唐望
     */
    public static Date addMinutes(Date date, int minute) {
        Calendar clenr = Calendar.getInstance();
        clenr.setTime(date);
        clenr.add(Calendar.MINUTE, minute);

        return clenr.getTime();

    }

    /**
     * 获取指定日期追加秒钟数后的日期
     *
     * @param date   日期对象
     * @param second 秒钟数
     * @return 返回Date日期类型
     * @author 唐望
     */
    public static Date addSeconds(Date date, int second) {
        Calendar clenr = Calendar.getInstance();
        clenr.setTime(date);
        clenr.add(Calendar.SECOND, second);

        return clenr.getTime();

    }

    /**
     * 获取指定日期所属月份的最后一天的日期
     *
     * @param date 日期对象
     * @return 返回Date日期类型
     * @author 唐望
     */
    public static Date getMonthLastDayToDate(Date date) {
        Calendar clenr = Calendar.getInstance();
        clenr.setTime(date);
        clenr.set(Calendar.DATE, clenr.getActualMaximum(Calendar.DATE));
        return clenr.getTime();

    }

    /**
     * 获取指定日期所在年的最后一天的日期
     *
     * @param date 日期对象
     * @return 返回Date日期类型
     * @author 唐望
     */
    public static Date getYearLastDayToDate(Date date) {
        Calendar clenr = Calendar.getInstance();
        clenr.clear();
        clenr.set(Calendar.YEAR, getYear(date));
        clenr.roll(Calendar.DAY_OF_YEAR, -1);
        return clenr.getTime();

    }

    /**
     * 获取指定日期是星期几
     *
     * @param date 日期对象
     * @return 返回int数值类型
     * @author 唐望
     */
    public static int getWeek(Date date) {
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

        //update by ouyangsk Linux环境下format后为英文，需加Locale参数
        //SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.CHINESE);

        String week = sdf.format(date);

        int result = 1;

        for (int i = 0; i < weeks.length; i++) {
            if (week.equals(weeks[i])) {
                break;
            }
            result++;
        }

        return result;

    }



    /**
     * @param date
     * @return
     * @author linwp
     */
    public static XMLGregorianCalendar getXGCalendarFromDate(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);//TODO luwei 产生大量对象
        } catch (DatatypeConfigurationException e) {
            // TODO Auto-generated catch block
            LOGGER.error("", e);
            return null;
        }
    }

    /**
     * @param dateString
     * @return
     * @author linwp
     */
    public static XMLGregorianCalendar getXGCalendarFromDateString(String dateString) {
        Date date = getStringToDate(dateString, DEFAULT_DATE_FORMAT);
        return getXGCalendarFromDate(date);
    }

    /**
     * @param XGCalendar
     * @return
     * @author linwp
     */
    public static Date getDateFromXGCalendar(XMLGregorianCalendar XGCalendar) {
        return XGCalendar.toGregorianCalendar().getTime();
    }


    //add by luwei
    public static String format(Date date) {
        return format(date, DEFAULT_TIME_FORMAT);

    }

    //add by luwei
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        if (pattern == null) {
            pattern = DEFAULT_TIME_FORMAT;
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    //add by luwei
    public static Date parse(String dateStr) {
        return parse(dateStr, DEFAULT_TIME_FORMAT);
    }

    //add by luwei
    public static Date parse(String dateStr, String pattern) {
        return parse(dateStr, pattern, null);
    }

    public static Date parse(String dateStr, String pattern, Locale locale) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        if (locale == null) {
            locale = Locale.getDefault(Locale.Category.FORMAT);
        }
        if (pattern == null) {
            pattern = DEFAULT_TIME_FORMAT;
        }
        try {
            return new SimpleDateFormat(pattern, locale).parse(dateStr);
        } catch (ParseException e) {
            LOGGER.error("parse date fail, dateStr=" + dateStr, e);
            return null;
        }
    }
}
