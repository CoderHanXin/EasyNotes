package com.addict.easynotes.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {

    /**
     * 根据格式将日期格式化为字符串
     *
     * @param date    the date to format
     * @param pattern the pattern
     * @return
     */
    public static String formatDateToString(Date date, String pattern) {

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date formatStringToDate(String date, String pattern) throws ParseException {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.parse(date);
    }

    /**
     * 按照以下原则格式化日期：
     * 如果是当天：HH:mm:ss
     * 如果是当年：MM-dd
     * 如果不是当年：yyyy-MM-dd
     *
     * @param param the date to format
     * @return
     */
    public static String dateOrTimeFormat(Date param) {
        SimpleDateFormat sdfDateWithYear = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

        Calendar cParam = Calendar.getInstance();
        cParam.setTime(param);

        Calendar cNow = Calendar.getInstance();

        if (cParam.get(Calendar.YEAR) == cNow.get(Calendar.YEAR)) {
            if (cParam.get(Calendar.DATE) == cNow.get(Calendar.DATE)) {
                return sdfTime.format(param);
            } else {
                return sdfDate.format(param);
            }
        } else {
            return sdfDateWithYear.format(param);
        }
    }

    /**
     * 获取当前日期，去掉时分秒，只保留日期
     *
     * @return
     */
    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取日期，去掉时分秒，只保留日期
     *
     * @param date the date to cut
     * @return
     */
    public static Date getDateWithoutTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    public static Long changeDateToLong(Date date) {
        return date.getTime();
    }

}
