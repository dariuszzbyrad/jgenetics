package com.github.jgenetics.utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Set of utils function used in the library
 */
public class Utils {
	
	/**
	 * The method scales linearly predetermined value from the old to the new range of numerical range.
	 * 
	 * @param value Value for normalization
	 * @param oldMinValue The lower limit of the old numerical range
	 * @param oldMaxValue The upper limit of the old numerical range
	 * @param newMinValue The lower limit of the new numerical range
	 * @param newMaxValue The upper limit of the new numerical range
	 * @return The normalized value
	 */
	public static double normalize (double value, double oldMinValue, double oldMaxValue, double newMinValue, double newMaxValue) {
		double normalizedValue = (value - oldMinValue) / (oldMaxValue - oldMinValue) * (newMaxValue - newMinValue) + newMinValue;
		
		return normalizedValue;
	}
	
	/**
	 * Checks whether the number is in the range of numerical
	 * @param number The number to check
	 * @param startOfRange The lower limit of the numerical range
	 * @param endOfRange The upper limit of the numerical range
	 * @return Is point in the range
	 */
	public static boolean isPointInRange(double number, double startOfRange, double endOfRange) {
		return number >= startOfRange && number<= endOfRange;
	}

	
	/**
	 * Generate random double value in the range of numerical
	 * @param rangeMin The lower limit of the numerical range
	 * @param rangeMax The upper limit of the numerical range
	 * @param rand The Random object
	 * @return Random value
	 */
	public static double generateRandomValue(double rangeMin, double rangeMax) {
		double randomValue = Random.getInstance().nextDouble();
		return rangeMin + (rangeMax - rangeMin) * randomValue;
	}
	
	/**
	 * Convert list to another list
	 * @param from Input list
	 * @param func Function converts the elements of the list
	 * @return New list
	 */
	public static <T, U> List<U> convertList(List<T> from, Function<T, U> func){
	    return from.stream().map(func).collect(Collectors.toList());
	}
}
