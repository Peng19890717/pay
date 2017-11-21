package com.third.emaxPlus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 简单时间转换
 * @author pengzhenyi
 *
 */
public class EmaxPlusDateUtil {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat dateFormatyyyyMMDD = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat dateTimeFormatyyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * 将时间转换成时分秒的字符串
	 * @param date
	 * @return
	 */
	public static String formateDateSecond(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static String dateTimeFormatyyyyMMddHHmmss(Date date) {
		return dateTimeFormatyyyyMMddHHmmss.format(date);
	}

	/**
	 * 返回年月日
	 * @param date
	 * @return
	 */
	public static String formateDate(Date date){
		return dateFormat.format(date);
	}

	/**
	 * 返回年月日
	 * @param date
	 * @return
	 */
	public static String formateDateyyyyMMDD(Date date){
		return dateFormatyyyyMMDD.format(date);
	}
	/**
	 * 将字符串转换成天
	 * @param date
	 * @return
	 */
	public static Date parseDateyyyyMMDD(String date){
		try {
			return dateFormatyyyyMMDD.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将字符串转换成时分秒的时间
	 * @param date
	 * @return
	 */
	public static Date parseDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将字符串转换成天
	 * @param date
	 * @return
	 */
	public static Date parseShortDate(String date){
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断当前日期是星期几
	 *
	 * @param dateTime 要判断的时间
	 * @return dayForWeek 判断结果
	 */
	public static int dayForWeek(Date dateTime) {
		Calendar c = Calendar.getInstance();
		c.setTime(dateTime);
		int dayForWeek = 0;
		if(c.get(Calendar.DAY_OF_WEEK) == 1){
			dayForWeek = 7;
		}else{
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	/**
	 * 是否在当月1~5日 全包含
	 * @return
	 */
	public static boolean canApply(int start,int end){
		Date date=new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day=c.get(Calendar.DAY_OF_MONTH);
		return (day>=start && day<=end);
	}
	/**
	 * 获取本周第一天
	 * @param date
	 * @return
	 */
	public static Date getMinWeekDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMinimum(Calendar.DAY_OF_WEEK));
		return calendar.getTime();
	}
	/**
	 * 获取本周最后
	 * @param date
	 * @return
	 */
	public static Date getMaxWeekDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMaximum(Calendar.DAY_OF_WEEK));
		return calendar.getTime();
	}
	/**
	 * 获取月份起始日期
	 * @return
	 * @throws ParseException
	 */
	public static Date getMinMonthDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	/**
	 * 获取月份最后日期
	 * @return
	 * @throws ParseException
	 */
	public static Date getMaxMonthDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	/**
	 * 计算两个日期之间相差的天数
	 * @param smdate 较小的时间
	 * @param bdate  较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate,Date bdate) {
		try{
			smdate=dateFormat.parse(dateFormat.format(smdate));
			bdate=dateFormat.parse(dateFormat.format(bdate));
			Calendar cal = Calendar.getInstance();
			cal.setTime(smdate);
			long time1 = cal.getTimeInMillis();
			cal.setTime(bdate);
			long time2 = cal.getTimeInMillis();
			long between_days=(time2-time1)/(1000*3600*24);
			return Integer.parseInt(String.valueOf(between_days));
		}catch (ParseException e){
			e.printStackTrace();
		}

		return -1;
	}

	/**
	 * 分钟加减
	 */
	public static Date addMinutes(Date date,int minute) {
		try{
			date=dateTimeFormat.parse(dateTimeFormat.format(date));
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MINUTE,minute);
			return cal.getTime();
		}catch (ParseException e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 天数加减
	 */
	public static Date addDays(Date date,int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}
	/**
	 * 计算两个日期之间相差的分钟
	 * @param smdate 较小的时间
	 * @param bdate  较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int minutesBetween(Date smdate,Date bdate) {
		try{
			Calendar cal = Calendar.getInstance();
			cal.setTime(smdate);
			long time1 = cal.getTimeInMillis();
			cal.setTime(bdate);
			long time2 = cal.getTimeInMillis();
			long between_days=(time2-time1)/(1000*60);
			return Integer.parseInt(String.valueOf(between_days));
		}catch (Exception e){
			e.printStackTrace();
		}

		return -1;
	}
	/**
	 * 分钟加减
	 */
	public static String addMinutes(String dateStr,int minute) {
		try{
			Date date=dateTimeFormat.parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MINUTE,minute);
			return dateTimeFormat.format(cal.getTime());
		}catch (ParseException e){
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception{

		System.out.println(daysBetween(parseDate("2016-12-22 14:04:00"),new Date()));


		System.out.println(minutesBetween(parseDate("2016-12-21 14:04:00"),new Date()));

		System.out.println(minutesBetween(new Date(),parseDate("2017-01-12 17:04:00")));

		Date now = new Date();
		System.out.println(dateTimeFormat.format(now));
		now = addMinutes(now,30);
		System.out.println(dateTimeFormat.format(now));
	}

}
