package com.github.jgenetics.population;

/**
 * The interface, which must implement every object that calculates the value of the fitness function.
 */
public interface FitnessFunction {
	public double calculate(String genom);
}
