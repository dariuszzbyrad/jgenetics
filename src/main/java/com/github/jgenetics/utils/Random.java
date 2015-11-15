package com.github.jgenetics.utils;

/**
 * The random class is singleton used to generate random value in the jGenetics library. 
 */
public class Random extends java.util.Random {
	
	/**
	 * Single instance 
	 */
	private static Random random = new Random();
	
	/**
	 * Blocking create new instance
	 */
	private Random() { }
	
	/**
	 * Get single instance
	 * @return single instance
	 */
	public static Random getInstance() {
		return random;
	}
}
