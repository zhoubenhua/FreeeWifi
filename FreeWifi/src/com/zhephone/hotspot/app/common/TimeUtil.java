package com.zhephone.hotspot.app.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 时间工具
 * 
 * @author 程康
 * 
 */
public class TimeUtil {
	
	public static String getData() {
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);//获取年份
		int month=ca.get(Calendar.MONTH);//获取月份
		int day=ca.get(Calendar.DATE);//获取日
		return year + "-" + (month+1) + "-" + day;
	}
	
	public static String getTime() {
		Calendar ca = Calendar.getInstance();
		int minute=ca.get(Calendar.MINUTE);//分
		int hour=ca.get(Calendar.HOUR_OF_DAY);//小时
//		int second=ca.get(Calendar.SECOND);//秒
//		int WeekOfYear = ca.get(Calendar.DAY_OF_WEEK);
		return hour + ":" + minute;
	}

	/**
	 * 时间转换
	 * 
	 * @param d
	 * @param flag
	 *            返回时间类型 0 返回yyyy-MM-dd HH:mm:ss 1 返回yyyy年MM月dd HH:mm:ss 2
	 *            yyyy年MM月dd 3 返回yyyy-MM-dd
	 * @return
	 */
	public static String getTimeConvert(String time, int flag) {
		SimpleDateFormat sDateFormat;
		switch (flag) {
		case 0:
			sDateFormat = new SimpleDateFormat("mm:ss");
			break;
		case 1:
			sDateFormat = new SimpleDateFormat("HH");
			break;
		case 2:
			sDateFormat = new SimpleDateFormat("mm");
			break;
		case 3:
			sDateFormat = new SimpleDateFormat("ss");
			break;
		default:
			sDateFormat = new SimpleDateFormat("HH:mm:ss");
			break;
		}
		String date = sDateFormat.format(new Date(time));
		return date;
	}
	
	/**
	 * 比较聊天时间和现在时间,如果聊天时间超和现在在时间相差超过5分钟就显示,如果不超过就不显示聊天时间
	 * @param dateTime
	 * @return
	 */
	public static String parseChatDateTime(String times) {
		String str = "";
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			//Date chatDate = sDateFormat.parse(dateTime);
			long chatTimes = Long.parseLong(times);  //聊天毫秒时间
			long fiveMinusTimes = 5*60*1000;  //5分钟毫秒
			if((getCurrentTime() - chatTimes) >= fiveMinusTimes) {
				str = sDateFormat.format(new Date(chatTimes));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
		
	}
	
	
	public static String getDateConvert(Date date, int flag) {
		SimpleDateFormat sDateFormat;
		switch (flag) {
		case 0:
//			sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			break;
		case 1:
//			sDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
			sDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
			break;
		case 2:
			sDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
			break;
		case 3:
			sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			break;
		case 4:
			sDateFormat = new SimpleDateFormat("MM月dd日");
			break;
		case 5:
			sDateFormat = new SimpleDateFormat("yyyy");
			break;
		case 6:
			sDateFormat = new SimpleDateFormat("MM");
			break;
		case 7:
			sDateFormat = new SimpleDateFormat("dd");
			break;
		case 8:
			sDateFormat = new SimpleDateFormat("HH");
			break;
		case 9:
			sDateFormat = new SimpleDateFormat("mm");
			break;
		case 10:
			sDateFormat = new SimpleDateFormat("ss");
			break;
		case 11:
			sDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
			break;
		case 12:
			sDateFormat = new SimpleDateFormat("yyyyMMdd");
			break;
		default:
//			sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			break; 
		}
		String dateStr = sDateFormat.format(date);
		return dateStr;
	}
	
	/**
	 * 获取当前是星期几
	 * @param dw
	 * @return
	 */
	public static String getCurrentWeekOfMonth(int dw) {
		String strWeek = null;
		if (dw == 1) {
			strWeek = "周日";
		} else if (dw == 2) {
			strWeek = "周一";
		} else if (dw == 3) {
			strWeek = "周二";
		} else if (dw == 4) {
			strWeek = "周三";
		} else if (dw == 5) {
			strWeek = "周四";
		} else if (dw == 6) {
			strWeek = "周五";
		} else if (dw == 7) {
			strWeek = "周六";
		}
		return strWeek;
	}
	
	/**
	 * 获取当前时间
	 * 格式  2012-12-21 12:12:12
	 * @return
	 */
	public static String getCurrentDate() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss"); 
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}
	
	/**
	 * 获取当前时间
	 * 格式  2012-12-21 12:12:12
	 * @return
	 */
	public static long getCurrentTime() {
		Date date = new Date();
		return date.getTime();
	}


}
