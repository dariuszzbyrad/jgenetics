package com.github.jgenetics.model;

import com.github.jgenetics.enums.Crossover;
import com.github.jgenetics.enums.Selection;

import lombok.Builder;
import lombok.Data;


/**
 * The class with necessary parameters to run the genetic algorithm
 */
@Data
@Builder
public class Parameters {
	/**
	 * Size of chromosome
	 */
	private int sizeOfChromosome;

	/**
	 * Number of chromosomes in each population
	 */
	private int populationSize;

	/**
	 * Probability of gene mutation 
	 */
	private double mutationRate;
	
	/**
	 * Probability of crossover
	 */
	private double crossoverRate;

	/**
	 * Set of values for the gene. For example:
	 * <ul>
	 * <li>"1234567890"</li>
	 * <li>"01"</li>
	 * <li>"ABCDEFGHIJKLMNOPRSTUWXYZ"</li>
	 * </ul>
	 */
	private String encodingAlphabet;

	/**
	 * Type of selection chromosome pairs to crosover
	 */
	private Selection selectionType;

	/**
	 * Type of crossover
	 */
	private Crossover crossoverType;
	
	/**
	 * Max numbers of algorithm iteration
	 */
	private int maxIteration;
	
	/**
	 * Save report to file after each iteration
	 */
	private boolean saveReport = false;
	
	/**
	 * Min percent of population homogeneity for stop GA.
	 */
	private double percentHomogeneous = 1.0;
}
