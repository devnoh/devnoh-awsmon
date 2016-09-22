/*
 * @(#)DateUtil.java
 *
 * Copyright (c) 2012 KW iTech, Inc.
 * All rights reserved.
 */
package devnoh.awsmon.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class description goes here.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 * @version 1.0
 */
public class DateUtil {

	public static final String ISO_DATE_PATTERN = "yyyy-MM-dd";
	public static final String ISO_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String LOCALIZED_DATE_PATTERN = "M/d/yyyy";
	public static final String LOCALIZED_DATE_TIME_PATTERN = "M/d/yyyy HH:mm:ss";

	public static String formatDate(Date date) {
		return formatDateTime(date, ISO_DATE_PATTERN);
	}

	public static String formatDateTime(Date date) {
		return formatDateTime(date, ISO_DATE_TIME_PATTERN);
	}

	public static Date parseDate(String date) {
		return parseDateTime(date, ISO_DATE_PATTERN);
	}

	public static Date parseDateTime(String date) {
		return parseDateTime(date, ISO_DATE_TIME_PATTERN);
	}

	public static String formatLocalizedDate(Date date) {
		return formatDateTime(date, LOCALIZED_DATE_PATTERN);
	}

	public static String formatLocalizedDateTime(Date date) {
		return formatDateTime(date, LOCALIZED_DATE_TIME_PATTERN);
	}

	public static Date parseLocalizedDate(String date) {
		return parseDateTime(date, LOCALIZED_DATE_PATTERN);
	}

	public static Date parseLocalizedDateTime(String date) {
		return parseDateTime(date, LOCALIZED_DATE_TIME_PATTERN);
	}

	public static String formatLocalizedDate(long millis) {
		return formatLocalizedDate(new Date(millis));
	}

	public static String formatLocalizedDateTime(long millis) {
		return formatLocalizedDate(new Date(millis));
	}

	public static String formatDateTime(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static Date parseDateTime(String strDate, String pattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.parse(strDate);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public static Date getToday() {
		return parseDate(formatDate(new Date()));
	}

	public static int getElapsedDays(long time) {
		return (int) Math.floor(time / (24 * 60 * 60 * 1000D));
	}

	public static String getElapsedTimeString(long time) {
		String timeStr = "";
		if (time < 24 * 60 * 60 * 1000L) {
			int hours = (int) (time / (60 * 60 * 1000L));
			int minutes = (int) ((time % (60 * 60 * 1000L)) / (60 * 1000L));
			timeStr = pad(hours) + ":" + pad(minutes);
		} else {
			int days = (int) Math.round(time / (24 * 60 * 60 * 1000D));
			timeStr = days + "d " + getElapsedTimeString((long) (time % (24 * 60 * 60 * 1000D)));
		}
		return timeStr;
	}

	public static String getElapsedTimeStringWithSeconds(long time) {
		String timeStr = "";
		if (time < 24 * 60 * 60 * 1000L) {
			int hours = (int) (time / (60 * 60 * 1000L));
			int minutes = (int) ((time % (60 * 60 * 1000L)) / (60 * 1000L));
			int seconds = (int) ((time % (60 * 1000L) / 1000L));
			timeStr = pad(hours) + ":" + pad(minutes) + ":" + pad(seconds);
		} else {
			int days = (int) Math.round(time / (24 * 60 * 60 * 1000L));
			// timeStr = days + "d " + getElapsedTimeStringWithSeconds((long) (time % (24 * 60 * 60 * 1000L)));
			timeStr = days + "d " + getElapsedTimeString((long) (time % (24 * 60 * 60 * 1000L)));
		}
		return timeStr;
	}

	public static String getElapsedTimeString2(long time) {
		String timeStr = "";
		if (time < 60 * 1000L) {
			int seconds = (int) ((time % (60 * 1000L)) / 1000L);
			timeStr = seconds + (seconds > 1 ? " seconds" : " second");
		} else if (time < 60 * 60 * 1000L) {
			int minutes = (int) ((time % (60 * 60 * 1000L)) / (60 * 1000L));
			timeStr = minutes + (minutes > 1 ? " minutes" : " minute");
		} else if (time < 24 * 60 * 60 * 1000L) {
			int hours = (int) (time / (60 * 60 * 1000L));
			timeStr = hours + (hours > 1 ? " hours" : " hour");
		} else {
			int days = (int) Math.round(time / (24 * 60 * 60 * 1000D));
			timeStr = days + (days > 1 ? " days" : " day");
		}
		return timeStr;
	}

	private static String pad(int c) {
		return (c >= 10) ? String.valueOf(c) : "0" + String.valueOf(c);
	}

}
