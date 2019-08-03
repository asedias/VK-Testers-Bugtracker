package ru.asedias.vkbugtracker;

import android.annotation.SuppressLint;
import android.content.res.*;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class TimeUtils
{
	private static TimeZone customTimeZone;
	private static int diff;

	
	static {
		setCustomTimeZone(BTApp.context.getSharedPreferences(null, 0).getFloat("custom_timezone", 0.0F), BTApp.context.getSharedPreferences(null, 0).getInt("time_diff", 0));
	}

	public static String timeDuration(int var0) {
		long var3 = (long)var0 * 1000L;
		Calendar var7 = getCalendar();
		var7.set(Calendar.MINUTE, 0); //12
		var7.set(Calendar.HOUR_OF_DAY, 0); //11
		var7.set(Calendar.SECOND, 0); //13
		var7.set(Calendar.MILLISECOND, 0); //14
		var7.setTimeInMillis(var3);
		if(var3 < 3600000) {
			return String.format("%02d:%02d", var7.get(Calendar.MINUTE), var7.get(Calendar.SECOND));
		} else {
			return String.format("%d:%02d:%02d", var7.get(Calendar.HOUR), var7.get(Calendar.MINUTE), var7.get(Calendar.SECOND));
		}
	}

	public static String time(int var0) {
		long var3 = (long)var0 * 1000L;
		Calendar var5 = getCalendar();
		Calendar var7 = getCalendar();
		var7.set(Calendar.MINUTE, 0); //12
		var7.set(Calendar.HOUR_OF_DAY, 0); //11
		var7.set(Calendar.SECOND, 0); //13
		var7.set(Calendar.MILLISECOND, 0); //14
		long var1 = var7.getTimeInMillis();
		var7 = getCalendar();
		var7.setTimeInMillis(var3);
		return String.format("%d:%02d", var7.get(Calendar.HOUR_OF_DAY), var7.get(Calendar.MINUTE));
	}
	@SuppressLint("StringFormatMatches")
	public static String langDate(int var0) {
		Resources var6 = BTApp.context.getResources();
		long var3 = (long)var0 * 1000L;
		Calendar var5 = getCalendar();
		Calendar var7 = getCalendar();
		var7.set(Calendar.MINUTE, 0); //12
		var7.set(Calendar.HOUR_OF_DAY, 0); //11
		var7.set(Calendar.SECOND, 0); //13
		var7.set(Calendar.MILLISECOND, 0); //14
		long var1 = var7.getTimeInMillis();
		var7 = getCalendar();
		var7.setTimeInMillis(var3);
		String var8;
		if(var1 < var3 && 86400000L + var1 >= var3) {
			var8 = var6.getString(R.string.today);
			if(var7.get(Calendar.HOUR_OF_DAY) == 1) {
				var0 = R.string.date_at_1am;
			} else {
				var0 = R.string.date_at;
			}

			var8 = String.format("%s %s %d:%02d", var8, var6.getString(var0), var7.get(Calendar.HOUR_OF_DAY), var7.get(Calendar.MINUTE));
		} else if(86400000L + var1 < var3 && 172800000L + var1 > var3) {
			var8 = var6.getString(R.string.tomorrow);
			if(var7.get(Calendar.HOUR_OF_DAY) == 1) {
				var0 = R.string.date_at_1am;
			} else {
				var0 = R.string.date_at;
			}

			var8 = String.format("%s %s %d:%02d", var8, var6.getString(var0), var7.get(Calendar.HOUR_OF_DAY), var7.get(Calendar.MINUTE));
		} else if(var1 - 86400000L < var3 && var1 >= var3) {
			var8 = var6.getString(R.string.yesterday);
			if(var7.get(Calendar.HOUR_OF_DAY) == 1) {
				var0 = R.string.date_at_1am;
			} else {
				var0 = R.string.date_at;
			}

			var8 = String.format("%s %s %d:%02d", var8, var6.getString(var0), var7.get(Calendar.HOUR_OF_DAY), var7.get(Calendar.MINUTE));
		} else {
			if(var7.get(Calendar.YEAR) != var5.get(Calendar.YEAR)) {
				var8 = "" + var6.getString(R.string.date_format_day_month_year, var7.get(Calendar.DAY_OF_MONTH), var6.getStringArray(R.array.months_short)[Math.min(var7.get(Calendar.MONTH), 11)], var7.get(Calendar.YEAR));
			} else {
				var8 = "" + var6.getString(R.string.date_format_day_month, var7.get(Calendar.DAY_OF_MONTH), var6.getStringArray(R.array.months_full)[Math.min(var7.get(Calendar.MONTH), 11)]);
			}

			StringBuilder var9 = (new StringBuilder()).append(var8);
			if(var7.get(Calendar.HOUR_OF_DAY) == 1) {
				var0 = R.string.date_at_1am;
			} else {
				var0 = R.string.date_at;
			}

			var8 = var9.append(String.format(" %s %d:%02d", new Object[]{var6.getString(var0), Integer.valueOf(var7.get(Calendar.HOUR_OF_DAY)), Integer.valueOf(var7.get(Calendar.MINUTE))})).toString();
		}

		return var8;
	}
	
	private static Calendar getCalendar() {
		Calendar var0;
		if(customTimeZone != null) {
			var0 = Calendar.getInstance(customTimeZone);
			var0.setTimeInMillis((long)getCurrentTime() * 1000L);
		} else {
			var0 = Calendar.getInstance();
		}

		return var0;
	}

	private static int getCurrentTime() {
		int var0 = (int)(System.currentTimeMillis() / 1000L);
		if(customTimeZone != null) {
			var0 -= diff;
		}

		return var0;
	}

	private static void setCustomTimeZone(float var0, int var1) {
		if(var0 == 0.0F) {
			customTimeZone = null;
		} else {
			customTimeZone = new SimpleTimeZone((int)(3600.0F * var0 * 1000.0F), String.format("Custom %d:%02d", (int) var0, (int) (var0 * 60.0F % 60.0F)));
		}

		diff = var1;
		BTApp.context.getSharedPreferences(null, 0).edit().putFloat("custom_timezone", var0).putInt("time_diff", var1).apply();
	}



}
