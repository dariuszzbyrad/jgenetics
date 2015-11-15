package com.github.jgenetics.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.github.jgenetics.utils.Utils;

public class UtilsTest {
	
	private static final double DEFAULT_DELTA = 0;
	
	@Test
	public void testNormalizeValue() {
		double firstValueToNormalize = -1.23;
		double secondValueToNormalize = 0.001;
		double firstExpectedNormalizedValue = 43.85;
		double secondExpectedNormalizedValue = 50.005;
		
		double oldMinValue = -10;
		double oldMaxValue = 10;
		double newMinValue = 0;
		double newMaxValue = 100;
		
		assertEquals(firstExpectedNormalizedValue, 
				Utils.normalize(firstValueToNormalize, oldMinValue, oldMaxValue, newMinValue, newMaxValue), DEFAULT_DELTA);
		assertEquals(secondExpectedNormalizedValue, 
				Utils.normalize(secondValueToNormalize, oldMinValue, oldMaxValue, newMinValue, newMaxValue), DEFAULT_DELTA);
	}
	
	@Test
	public void testPointIsInRange() {
		double point = 1;
		double startOfRange = 0.99;
		double endOfRange = 1.01;
		
		assertTrue(Utils.isPointInRange(point, startOfRange, endOfRange));
	}
	
	@Test
	public void testPointIsNotInRange() {
		double point = 1;
		double startOfRange = 1.000000001;
		double endOfRange = 1.02;
		
		assertFalse(Utils.isPointInRange(point, startOfRange, endOfRange));
	}
	
	@Test
	public void generateRandomValue() throws Exception {
		double rangeMin = -10;
		double rangeMax = 10;
		
		assertTrue(Utils.generateRandomValue(rangeMin, rangeMax) >= rangeMin);
		assertTrue(Utils.generateRandomValue(rangeMin, rangeMax) <= rangeMax);
	}
	
	@Test
	public void testConvertIntListToStringList() {
		List<Integer> inputList = Arrays.asList(1, 2, 3);
		List<String> expectedOutputList = Arrays.asList("1", "2", "3");
		
		List<String> outputList = Utils.convertList(inputList, e -> e.toString());
		
		assertEquals(expectedOutputList.get(0), outputList.get(0));
		assertEquals(expectedOutputList.get(1), outputList.get(1));
		assertEquals(expectedOutputList.get(2), outputList.get(2));
	}
	
}
