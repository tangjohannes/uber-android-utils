package com.uber.utils;

public class NumberUtils {

	public static Integer parseInteger(String integerString) {
		try {
			return Integer.valueOf(integerString);
		} catch (NumberFormatException nfe) {
			return null;
		}
	}

	public static Double parseDouble(String doubleString) {
		try {
			return Double.valueOf(doubleString);
		} catch (NumberFormatException nfe) {
			return parseInteger(doubleString).doubleValue();
		}
	}

	public static Long parseLong(String longString) {
		try {
			return Long.valueOf(longString);
		} catch (NumberFormatException nfe) {
			return null;
		}
	}

	public static String intToTwoDigitsString(Integer i) {
		if (i < 10) {
			return "0" + i;
		} else {
			return i.toString();
		}
	}

}
