package com.github.jgenetics.population;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.github.jgenetics.enums.Crossover;
import com.github.jgenetics.enums.Selection;
import com.github.jgenetics.exception.GeneticsException;
import com.github.jgenetics.utils.Utils;

/**
 * Class provides a set of operations to population evolution.
 */
public class Evolution {
	
	private static final Logger logger = Logger.getLogger(Evolution.class);
	
	/**
	 * The end of the range with normalized fitness values.
	 */
	private static final double MIN_NORMALIZE_VALUE = 0;
	
	/**
	 * The start of the range with normalized fitness values.
	 */
	private static final double MAX_NORMALIZE_VALUE = 100;
	
	/**
	 * Crossover population for a known selection chromosome type and crossover type.
	 * The first step is chromosomes selection for hybridization. 
 	 * The second step is crossover each pair of chromosome.
	 * @param population Population for hybridization.
	 * @param selectionType Type selection of chromosomes to cross. 
	 * @param crossoverType Type crossover of chromosomes.
	 * @return Population with new chromosomes.
	 * @throws GeneticsException 
	 */
	public Population crossover(Population population, Selection selectionType, Crossover crossoverType) throws GeneticsException {
		if (population.isPopulationHomogeneous()) {
			throw new GeneticsException("Population is homogeneous");
		}
		
		Population newPopulation = new Population();
		List<Chromosome> newChromosomes = null;
		
		switch (selectionType) {
			case CAROUSEL:
				List<Chromosome> candidates = getCandidatesForCarousel(population);
				newChromosomes = crossoverListOfChromosomes(candidates, crossoverType);
				break;
		}

		newPopulation.setChromosomes(newChromosomes);
		return newPopulation;
	}
	
	/**
	 * Mutation of population with a known probability and encoding type.
	 * @param population Population for mutation.
	 * @param probability Probability of mutation each gene in the chromosome.
	 * @param encodingType Set of available characters for genome.
	 * @return New population after mutation.
	 */
	public Population mutation(Population population, double probability, String encodingType) {
		Population newPopulation = new Population();
		List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
		List<Chromosome> oldChromosomes = population.getChromosomes();
		
		oldChromosomes
			.stream()
			.forEach(c -> newChromosomes
					.add(c.mutation(probability, encodingType)));
		
		newPopulation.setChromosomes(newChromosomes);
		return newPopulation;
	}
	
	/**
	 * Crossing each pair of chromosomes from the list.
	 * @param candidates List of chromosomes to crossing.
	 * @param crossoverType Type crossover of chromosomes.
	 * @return New list of chromosomes after crossing.
	 */
	private List<Chromosome> crossoverListOfChromosomes(List<Chromosome> candidates, Crossover crossoverType) {
		List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
		int numberOfChromosomes = candidates.size();
		
		for (int i=0; i<numberOfChromosomes-1; i=i+2) {
			Chromosome firstCandidate = candidates.get(i);
			Chromosome secondCandidate = candidates.get(i+1);
			
			List<Chromosome> newPair = firstCandidate.crosover(secondCandidate, crossoverType);
			
			newChromosomes.add(newPair.get(0));
			newChromosomes.add(newPair.get(1));
		}
		
		if (newChromosomes.size() != numberOfChromosomes) {
			newChromosomes.add(candidates.get(numberOfChromosomes - 1));
		}
		
		return newChromosomes;
	}

	/**
	 * Preparing list of chromosomes for crossing with the carousel algorithm.
	 * @param population Population with chromosomes to selection.
	 * @return The list of candidates to cross.
	 */
	private List<Chromosome> getCandidatesForCarousel(Population population) {
		List<Chromosome> selectedChromosomes = new ArrayList<Chromosome>();
		List<Chromosome> oldChromosomes = population.getChromosomes();

		int numberOfGeneratedChromosomes = oldChromosomes.size();
		
		List<Double> normalizedFitnessValues = normalizeFitnessValues(oldChromosomes);
		Double sumOfNormalizedFitnessValues = normalizedFitnessValues
				.stream()
				.collect(Collectors.summarizingDouble(Double::doubleValue)) 
				.getSum();
		
		logger.debug("Normalized fitness values = " + normalizedFitnessValues);
		
		for (int i=0; i<numberOfGeneratedChromosomes; i++) {
			double randomPoint = Utils.generateRandomValue(0, sumOfNormalizedFitnessValues);
			double startOfRange = MIN_NORMALIZE_VALUE;
			double endOfRange = normalizedFitnessValues.get(0);
			
			for (int j=0; j<oldChromosomes.size(); j++) {
				Chromosome oldChromosome = oldChromosomes.get(j);
				//logger.debug("Spr " + j + "; rand=" + randomPoint + "; begin=" + startOfRange + "; end=" + endOfRange + "; sum=" + sumOfNormalizedFitnessValues);
				if (Utils.isPointInRange(randomPoint, startOfRange, endOfRange)) {
					selectedChromosomes.add(oldChromosome);
					break;
				} else {
					startOfRange = MIN_NORMALIZE_VALUE;
					endOfRange += normalizedFitnessValues.get(j+1);
				}
			}
		}

		return selectedChromosomes;
	}

	/**
	 * Normalize fitness values for the range < {@see MIN_NORMALIZE_VALUE}; {@see MAX_NORMALIZE_VALUE} >
	 * @param chromosomes List of chromosomes with fitness value to normalize
	 * @return List of normalized values.
	 */
	private List<Double> normalizeFitnessValues(List<Chromosome> chromosomes) {
		List<Double> normalizedValues = new ArrayList<Double>();
		
		DoubleSummaryStatistics stats = chromosomes
				.stream()
				.map(c -> c.getFitnessValue())
				.collect(Collectors.summarizingDouble(Double::doubleValue));
		
		double minValue = stats.getMin();
		double maxValue = stats.getMax();
		
		chromosomes
			.stream()
			.forEach(c -> normalizedValues.add(
					Utils.normalize(c.getFitnessValue(), minValue, maxValue, MIN_NORMALIZE_VALUE, MAX_NORMALIZE_VALUE)));
		
		return normalizedValues;
	}
}
