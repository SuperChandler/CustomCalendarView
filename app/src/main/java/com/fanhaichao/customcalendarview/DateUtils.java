package com.fanhaichao.customcalendarview;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    /**
     * 通过年份和月份 得到当月的日子个数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
//        month++;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return -1;
        }
    }

    /**
     * 返回当前月份几号位于周几
     *
     * @param year  年份
     * @param month 月份，传入系统获取的，不需要正常的
     * @return 日：0	一：1		二：2		三：3		四：4		五：5		六：6
     */
    public static int getFirstDayWeek(int year, int month,int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day);

        return calendar.get(Calendar.DAY_OF_WEEK)-1;
    }

    /**
     * 根据列明获取周
     *
     * @param
     * @return
     */
    public static String getWeekName(int year, int month, int day) {
        int column = getFirstDayWeek(year, month, day);
        switch (column) {
            case 0:
                return "周日";
            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";
            default:
                return "";
        }
    }

    /**
     * 得到日期字符串
     * @param date
     * @return  年月日时分秒
     */
    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    /**
     * 得到日期字符串
     * @param date
     * @return  年月日
     */
    public static String getTimeYMD(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 得到日期字符串
     * @param date 年月
     * @return
     */
    public static String getTimeYM(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }

    /**
     * 得到日期字符串
     * @param date 年
     * @return
     */
    public static String getTimeY(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(date);
    }

    /**
     * 获取当前年月日字符串
     * @return
     */
    public static String currentYMD(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    /**
     * 获取当前年月字符串
     *
     * @return
     */
    public static String currentYM(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(new Date());
    }

    /**
     * 通过日期字符串获取日期对象
     * @param dateStr
     * @return
     */
    public static Date Str2DateYMD(String dateStr)  {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date ;
        try{
           date = format.parse(dateStr);
        }catch (Exception e){
            Log.d("DateUtils.Str2DateYMD()","Str2DateYMD() returned: " + e.toString() );
            date = new Date();
        }

        return date;
    }

    /**
     * 获取当前所在月份
     * @return
     */
    public static int getToday_MONTH(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 获取当前所在天
     * @return
     */
    public static int getToday_DAY(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
