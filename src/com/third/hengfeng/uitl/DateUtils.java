package com.third.hengfeng.uitl;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

public class DateUtils {
	public static final String HHMM = "HHmm";
	public static final String DDMMMYY = "ddMMMyy";
	public static final String DD_MMMYYYY_HHMM = "ddMMMyyyy HHmm";
	public static final String DD_MMMYYYY_HH_MM = "ddMMMyyyy HH:mm";
	public static final String DD_MMMYYYY_HH_MM_SS = "ddMMMyyyy HH:mm:ss";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String YYYY2MM2DD = "yyyy/MM/dd";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String HH_MM = "HH:mm";
	public static final String HH_MM_SS = "HH:mm:ss";
	public static final String YYYY_MM_DD2HH_mm = "yyyy-MM-dd-HH-mm";
	public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	/**
	 * 解析日期<br>
	 * 支持格式：<br>
	 * 
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date parseDate(String dateStr) {
		SimpleDateFormat format = null;
		if (StringUtils.isBlank(dateStr)) {
			return null;
		}

		String _dateStr = dateStr.trim();
		try {
			if (_dateStr.matches("\\d{1,2}[A-Z]{3}")) {
				_dateStr = _dateStr + (Calendar.getInstance().get(Calendar.YEAR) - 2000);
			}
			// 01OCT12
			if (_dateStr.matches("\\d{1,2}[A-Z]{3}\\d{2}")) {
				format = new SimpleDateFormat("ddMMMyy");
			} else if (_dateStr.matches("\\d{1,2}[A-Z]{3}\\d{4}.*")) {// 01OCT2012
				// ,01OCT2012
				// 1224,01OCT2012
				// 12:24
				_dateStr = _dateStr.replaceAll("[^0-9A-Z]", "").concat("000000").substring(0, 15);
				format = new SimpleDateFormat("ddMMMyyyyHHmmss");
			} else {
				StringBuffer sb = new StringBuffer(_dateStr);
				String[] tempArr = _dateStr.split("\\s+");
				tempArr = tempArr[0].split("-|\\/");
				if (tempArr.length == 3) {
					if (tempArr[1].length() == 1) {
						sb.insert(5, "0");
					}
					if (tempArr[2].length() == 1) {
						sb.insert(8, "0");
					}
				}
				_dateStr = sb.append("000000").toString().replaceAll("[^0-9]", "").substring(0, 14);
				if (_dateStr.matches("\\d{14}")) {
					format = new SimpleDateFormat("yyyyMMddHHmmss");
				}
			}

			Date date = format.parse(_dateStr);
			return date;
		} catch (Exception e) {
			throw new RuntimeException("无法解析日期字符[" + dateStr + "]");
		}
	}

	/**
	 * 解析日期字符串转化成日期格式<br>
	 * 
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Date parseDate(String dateStr, String pattern) {
		try {
			SimpleDateFormat format = null;
			if (StringUtils.isBlank(dateStr)) {
				return null;
			}

			if (StringUtils.isNotBlank(pattern)) {
				format = new SimpleDateFormat(pattern);
				return format.parse(dateStr);
			}
			return parseDate(dateStr);
		} catch (Exception e) {
			throw new RuntimeException("无法解析日期字符[" + dateStr + "]");
		}
	}

	/**
	 * 获取一天开始时间<br>
	 * generate by: vakin jiang at 2011-12-23
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDayBegin(Date date) {
		String format = DateFormatUtils.format(date, YYYY_MM_DD);
		return parseDate(format.concat(" 00:00:00"));
	}

	/**
	 * 获取一天结束时间<br>
	 * generate by: vakin jiang at 2011-12-23
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDayEnd(Date date) {
		String format = DateFormatUtils.format(date, YYYY_MM_DD);
		return parseDate(format.concat(" 23:59:59"));
	}

	/**
	 * 时间戳格式转换为日期（年月日）格式<br>
	 * generate by: vakin jiang at 2011-12-23
	 * 
	 * @param date
	 * @return
	 */
	public static Date timestamp2Date(Date date) {
		return formatDate(date, YYYY_MM_DD);
	}

	/**
	 * 时间戳格式转换为日期（年月日）格式<br>
	 * generate by: vakin jiang at 2011-12-23
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate2YYYY_MM_DD(Date date) {
		return format(date, YYYY_MM_DD);
	}

	/**
	 * 格式化日期格式为：ddMMMyy<br>
	 * generate by: vakin jiang at 2012-10-17
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String format2ddMMMyy(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("ddMMMyy");
		return format.format(date).toUpperCase();
	}

	/**
	 * 格式化日期格式为：ddMMMyy<br>
	 * generate by: vakin jiang at 2012-10-17
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String format2ddMMMyy(String dateStr) {
		SimpleDateFormat format = new SimpleDateFormat("ddMMMyy");
		return format.format(parseDate(dateStr)).toUpperCase();
	}

	/**
	 * 格式化日期字符串<br>
	 * 
	 * 
	 * @param dateStr
	 * @param patterns
	 * @return
	 */
	public static String formatDateStr(String dateStr, String... patterns) {
		String pattern = YYYY_MM_DD_HH_MM_SS;
		if (patterns != null && patterns.length > 0 && StringUtils.isNotBlank(patterns[0])) {
			pattern = patterns[0];
		}
		return DateFormatUtils.format(parseDate(dateStr), pattern);
	}

	/**
	 * 格式化日期为日期字符串<br>
	 * 
	 * 
	 * @param orig
	 * @param patterns
	 * @return
	 */
	public static String format(Date date, String... patterns) {
		if (date == null)
			return "";
		String pattern = YYYY_MM_DD_HH_MM_SS;
		if (patterns != null && patterns.length > 0 && StringUtils.isNotBlank(patterns[0])) {
			pattern = patterns[0];
		}
		return DateFormatUtils.format(date, pattern);
	}

	public static String format2YYYY_MM_DD(Date date) {
		return format(date, YYYY_MM_DD);
	}

	/**
	 * 格式化日期为指定格式<br>
	 * 
	 * 
	 * @param orig
	 * @param patterns
	 * @return
	 */
	public static Date formatDate(Date orig, String... patterns) {
		String pattern = YYYY_MM_DD_HH_MM_SS;
		if (patterns != null && patterns.length > 0 && StringUtils.isNotBlank(patterns[0])) {
			pattern = patterns[0];
		}
		return parseDate(DateFormatUtils.format(orig, pattern));
	}

	/**
	 * 得到当前日期<br>
	 * 
	 * 
	 * @param orig
	 * @param patterns
	 * @return
	 */
	public static String getCurrentDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到当前日期<br>
	 * 
	 * 
	 * @param orig
	 * @param patterns
	 * @return
	 */
	public static String getDefaultDate() {
		return getCurrentDate(YYYY_MM_DD);
	}

	/**
	 * 得到当前日期<br>
	 * 
	 * 
	 * @param orig
	 * @param patterns
	 * @return
	 */
	public static String getDefaultDateTime(String pattern) {
		return getCurrentDate(pattern);
	}

	/**
	 * 得到当前日期<br>
	 * 
	 * 
	 * @param orig
	 * @param patterns
	 * @return
	 */
	public static String getDefaultDateTime() {
		return getCurrentDate(YYYY_MM_DD_HH_MM_SS);
	}
	
	
	/**
	 * 得到当前日期<br>
	 * 
	 * @param orig
	 * @param patterns
	 * @return
	 */
	public static String getDefaultTime() {
		return getCurrentDate(HH_MM_SS);
	}
	
	/**
	 * date1 >= date2 return true; date1 < date2 return false;
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean compareDate(String date1, String date2) {
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			calendar1.setTime(df.parse(date1));
			calendar2.setTime(df.parse(date2));
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		if (calendar1.compareTo(calendar2) >= 0) {
			return true;
		} else {
			return false;
		}
	}

	public static String getAfterMonth(int month, int day) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.MONTH, month);
		calendar.add(Calendar.DATE, day);
		String date = format.format(calendar.getTime());
		return date;
	}

	public static String getAfterDate(int day) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, day);
		String date = format.format(calendar.getTime());
		return date;
	}

	public static String getAfterMinute(int minute) {
		SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.MINUTE, minute);
		String date = format.format(calendar.getTime());
		return date;
	}

	public static String getAfterSecond(int second) {
		SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.SECOND, second);
		String date = format.format(calendar.getTime());
		return date;
	}
}
