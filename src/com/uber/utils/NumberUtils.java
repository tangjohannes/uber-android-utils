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
			return null;
		}
	}

}
