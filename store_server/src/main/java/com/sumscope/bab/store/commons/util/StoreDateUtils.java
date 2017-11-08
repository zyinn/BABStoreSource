package com.sumscope.bab.store.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shaoxu.wang on 2016/12/19.
 * 报价日期工具类
 */
public final class StoreDateUtils {
    private StoreDateUtils(){}
    /**
     * 判断两个日期是否是同一天
     * @param date1 日期1
     * @param date2 日期2
     * @return 同一天：true
     */
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(date1);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(date2);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB
                .get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 设置某一日期的时间为23：59：59
     */
    public static Date getLatestTimeOfDate(Date date) {
        return getDateWithSpecifiedTime(date, 23, 59, 59);
    }

    public static Date getDateWithSpecifiedTime(Date date, int hour, int min, int sec) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

    public static Date getBigoningDateWithSpecifiedTime(Date date){
        return getDateWithSpecifiedTime(date, 0,0, 0);
    }
    /**
     * 获取某一日期时间为一日开始时间，既00：00：01
     */
    public static Date getBeginingTimeOfDate(Date date) {
        return getDateWithSpecifiedTime(date, 0, 0, 1);
    }

    public static Date getBeginingTimeByDate(Date date) {
        return getDateWithSpecifiedTime(date, 0, 0, 0);
    }
    /**
     * 设置日期为用于矩阵计算的时间，既18:00:00，该时间是工作日结束进行结转的时间
     */
    public static Date getCalculationTimeOfDate(Date date) {
        return getDateWithSpecifiedTime(date, 18, 0, 0);
    }

    /**
     * 设置日期为过期时间，既23:00:00
     */
    public static Date getExpiredTimeOfDate(Date date) {
        return getDateWithSpecifiedTime(date, 23, 0, 0);
    }

    /**
     * 获取昨日过期日期，时间为23：00：00
     */
    public static Date getYesterdayExpiredTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return getExpiredTimeOfDate(calendar.getTime());
    }

    /**
     *获取昨天日期
     */
    public static Date getYesterdayTime(){
        return getTime(Calendar.DATE,-1);
    }

    /**
     *获取上个月日期
     */
    public static Date getLastMonthTime(){
       return getTime(Calendar.MONTH,-1);
    }
    /**
     *获取七天
     */
    public static Date getLastWeekTime(){
        return getTime(Calendar.DATE,-7);
    }

    public static Date getTime(int i,int j){
        Calendar cal = Calendar.getInstance();
        cal.add( i,j);
        return cal.getTime();
    }

    public static Date parserSimpleDateFormatString(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateString);
    }


    public static Calendar getDateTime(int i){
        Calendar calendar = setYearCalendar(i);
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar;
    }

    private static Calendar setYearCalendar(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, i);
        return calendar;
    }

    public static Calendar getIsDateTime(int i){
        Calendar calendar = setYearCalendar(i);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar;
    }

}
