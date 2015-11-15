package com.github.jgenetics.population;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.github.jgenetics.exception.GeneticsException;
import com.github.jgenetics.model.PopulationStatistic;

import lombok.Data;

/**
 * The population class represents each population with list of chromosomes and calculated statistic.
 */
@Data
public class Population {
	
	/**
	 * List of chromosomes in the population.
	 */
	private List<Chromosome> chromosomes;
	
	/**
	 * Calculated statistic. At the beginning is not calculated.
	 */
	private PopulationStatistic statistic = PopulationStatistic.EMPTY;
	
	private Chromosome theBestChromosome;
	
	/**
	 * Create new population with empty list of chromosomes.
	 */
	public Population() {
		chromosomes = new LinkedList<Chromosome>();
	}
	
	/**
	 * Generate new population for a known population size, size of chromosome and encoding type.
	 * @param populationSize Number of chromosome in population.
	 * @param sizeOfChromosome The length of the genome in the chromosome.
	 * @param encodingType Set of available characters for genome.
	 * @return New population.
	 */
	public static Population generate(int populationSize, int sizeOfChromosome, String encodingType) {
		if (populationSize < 0)
			throw new IllegalArgumentException("Invalid populationSize value");
		if (encodingType.length() < 1)
			throw new IllegalArgumentException("Invalid encodingType value");
		
		List<Chromosome> chromosomes = new ArrayList<Chromosome>();
		Population population = new Population();
		
		IntStream.range(0, populationSize)
			.forEach(l -> chromosomes
				.add(Chromosome.generate(sizeOfChromosome, encodingType)));
		
		population.setChromosomes(chromosomes);
		
		return population;
	}
	
	/**
	 * Calculate fitness function for each chromosome in the population.
	 * @param fitnessFunction Ftiness function used to calculate
	 * @param calculateStatistic Should statistics be calculated?
	 * @return New population with calculated fitness values
	 */
	public Population calculateFitnessFunction(FitnessFunction fitnessFunction) {
		Population resultPopulation = new Population();
		List<Chromosome> resultChromosomes = new LinkedList<Chromosome>();
		
		for (Chromosome chromosome : chromosomes) {
			Chromosome newChromosome = chromosome.calculateFitnessValue(fitnessFunction);
			resultChromosomes.add(newChromosome);
			
			if (!resultPopulation.isTheBestChromosomeSet() || resultPopulation.getTheBestChromosome().getFitnessValue() < newChromosome.getFitnessValue()) {
				resultPopulation.setTheBestChromosome(newChromosome);
			}
		}
		
		resultPopulation.setChromosomes(resultChromosomes);
		
		DoubleSummaryStatistics stats = resultChromosomes
			.stream()
			.map(c -> c.getFitnessValue())
			.collect(Collectors.summarizingDouble(Double::doubleValue));
			
		statistic = PopulationStatistic.builder()
			.avgFitnessValue(stats.getAverage())
			.maxFitnessValue(stats.getMax())
			.minFitnessValue(stats.getMin())
			.build();
			
		resultPopulation.setStatistic(statistic);
		
		return resultPopulation;
	}

	/**
	 * Check is population homogeneus, it means that all chromosomes are the same genomes.
	 * @return Is population homoneneus.s
	 * @throws GeneticsException Exception is throw when list of chromosomes is empty.
	 */
	public boolean isPopulationHomogeneous() throws GeneticsException {
		if (chromosomes.isEmpty()) {
			throw new GeneticsException("Population is empty");
		}
		
		long numberOfIdentical = 1;
		Chromosome pattern = chromosomes.get(0);
		
		numberOfIdentical = chromosomes
			.stream()
			.filter(c -> pattern.equals(c))
			.count();
		
		return numberOfIdentical == chromosomes.size();
	}
	
	/**
	 * Check is the best chromosome in population set.
	 * @return Is the best chromosome set.
	 */
	public boolean isTheBestChromosomeSet() {
		return theBestChromosome != null;
	}
	
	@Override
	public String toString() {
		return "Population [chromosomes=" + chromosomes + ", statistic=" + statistic + "]";
	}
}
