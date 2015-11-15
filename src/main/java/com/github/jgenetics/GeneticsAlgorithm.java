package com.github.jgenetics;

import org.apache.log4j.Logger;

import com.github.jgenetics.exception.GeneticsException;
import com.github.jgenetics.model.Parameters;
import com.github.jgenetics.population.Chromosome;
import com.github.jgenetics.population.Evolution;
import com.github.jgenetics.population.FitnessFunction;
import com.github.jgenetics.population.Population;

/**
 * Main class for run genetic algorithm.
 */
public class GeneticsAlgorithm {
	
	private static final Logger logger = Logger.getLogger(GeneticsAlgorithm.class);
	
	/**
	 * Basic method for run genetic algorithm.
	 * @param parameters Algorithm parameters
	 * @param fitnessFunction The fitness function
	 * @return The best solution
	 * @throws GeneticsException Ff something was wrong
	 */
	public Chromosome run(Parameters parameters, FitnessFunction fitnessFunction) throws GeneticsException {
		
		Chromosome theBestChromosome = Chromosome.EMPTY;
		int iteration = 0;
		
		Evolution evolution = new Evolution();
		
		Population population = Population.generate(parameters.getPopulationSize(), parameters.getSizeOfChromosome(), parameters.getEncodingAlphabet());
		population = population.calculateFitnessFunction(fitnessFunction);
		theBestChromosome = population.getTheBestChromosome();
		logger.debug("Iteration #" + iteration + ": " + population);
		
		while (iteration < parameters.getMaxIteration() || !population.isPopulationHomogeneous()) {
			iteration++;
			population = evolution.crossover(population, parameters.getSelectionType(), parameters.getCrossoverType());
			population = evolution.mutation(population, parameters.getMutationRate(), parameters.getEncodingAlphabet());
			population = population.calculateFitnessFunction(fitnessFunction);
			
			if (population.getTheBestChromosome().getFitnessValue() > theBestChromosome.getFitnessValue()) {
				theBestChromosome = population.getTheBestChromosome();
			}
			
			logger.debug("Iteration #" + iteration + ": " + population);
		}
		logger.info("The best chromosome: " + theBestChromosome + " after " + iteration + " iternation");
		
		return theBestChromosome;
	}
}
