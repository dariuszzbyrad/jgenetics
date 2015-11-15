package com.github.jgenetics.model;

import lombok.Builder;
import lombok.Data;

/**
 * The PopulationStatistic class represented the most important statistic parameter for each population
 */
@Data
@Builder
public class PopulationStatistic {
	/**
	 * None of the calculated statistics
	 */
	public static PopulationStatistic EMPTY;

	/**
	 * Minimum fitness value in population
	 */
	private double minFitnessValue;
	
	/**
	 * Avarage fitness value in population
	 */
	private double avgFitnessValue;
	
	/**
	 * Maximum fitness value in population
	 */
	private double maxFitnessValue;

	@Override
	public String toString() {
		return "min=" + minFitnessValue + ", avg=" + avgFitnessValue
				+ ", max=" + maxFitnessValue + "]";
	}
}
