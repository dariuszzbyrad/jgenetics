package com.github.jgenetics.population;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.github.jgenetics.enums.Crossover;
import com.github.jgenetics.utils.Random;
import com.github.jgenetics.utils.Utils;

import lombok.Data;

/**
 * Class represents a single chromosome and is used to perform basic operations on it.
 */
@Data
public class Chromosome {
	
	private static final Logger logger = Logger.getLogger(Chromosome.class);
	
	/**
	 * Empty chromosome, with empty genome
	 */
	public static Chromosome EMPTY;

	private Random rand = Random.getInstance();
	
	/**
	 * Represents chromosome with the empty genome
	 */
	private String genome = StringUtils.EMPTY;
	
	/**
	 * Fitness value. At the beginning it's not calculated.
	 */
	private double fitnessValue = Double.MIN_VALUE;
	
	/**
	 * Create chromosome for a known genome.
	 * @param genome genome.
	 */
	public Chromosome(String genome) {
		this.genome = genome;
	}

	/**
	 * Generate new chromosome with a random genome.
	 * @param chromosomeLength String length coding.
	 * @param encodingAlphabet Set of available characters for genome.
	 * @return New chromosome.
	 */
	public static Chromosome generate(int chromosomeLength,
			String encodingAlphabet) {
		Random rand = Random.getInstance();
		char[] chromosome = new char[chromosomeLength];
		int maxLocus = encodingAlphabet.length();
		
		IntStream
			.range(0, chromosomeLength)
			.forEach(l -> chromosome[l] = encodingAlphabet.charAt(rand.nextInt(maxLocus)));

		return new Chromosome(String.valueOf(chromosome));
	}
	
	/**
	 * Calculate fitness value.
	 * @param fitnessFunction.
	 * @return Fitness value.
	 */
	public Chromosome calculateFitnessValue(FitnessFunction fitnessFunction) {
		Chromosome resultChromosome = new Chromosome(genome);
		
		double fitnessValue = fitnessFunction.calculate(genome);
		resultChromosome.setFitnessValue(fitnessValue);
		
		return resultChromosome;
	}
	
	/**
	 * Chromosome mutation with a known probability and a set of available characters.
	 * @param probability Probability of mutation each gene in the chromosome.
	 * @param encodingType Set of available characters for genome.
	 * @return New chromosome after mutation.
	 */
	public Chromosome mutation(double probability, String encodingAlphabet) {
		char[] newCodeArray = genome.toCharArray();
		int maxLocus = encodingAlphabet.length();
		
		for (int i = 0; i< newCodeArray.length; i++) {
			double randomRate = rand.nextDouble();
			if (randomRate < probability) {
				newCodeArray[i] = encodingAlphabet.charAt(rand.nextInt(maxLocus));
			}
		}
		
		String newGenome = String.copyValueOf(newCodeArray);
		
		if (!newGenome.equals(genome)) {
			logger.debug("Mutation " + genome + " -> " + newGenome);
		}
		
		return new Chromosome(newGenome);
	}
	
	/**
	 * Crossing two chromosomes for a particular algorithm crossing.
	 * @param secondCh Second chromosome to crossover.
	 * @param crossoverType Crossover type.
	 * @return List of two new offsprings.
	 */
	public List<Chromosome> crosover(Chromosome secondCh, Crossover crossoverType) {
		String firstGenome = this.genome;
		String secondGenome = secondCh.getGenome();
		
		List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
		
		if (firstGenome.length() != secondGenome.length())
			throw new IllegalArgumentException("Invalid chromosomes length");
		
		if (Crossover.OnePoint.equals(crossoverType)) {
			newChromosomes = Utils.convertList(onePointCrossover(firstGenome, secondGenome), s -> new Chromosome(s));
		} else if (Crossover.TwoPoint.equals(crossoverType)) {
			newChromosomes = Utils.convertList(twoPointCrossover(firstGenome, secondGenome), s -> new Chromosome(s));
		}
		
		return newChromosomes;
	}

	/**
	 * Two-point crossing genes.
	 * @param fistGenome The first genome.
	 * @param secondGenome The second genome.
	 * @return List of two new genomes.
	 */
	private List<String> twoPointCrossover(String fistGenome, String secondGenome) {
		List<String> newChromosomes = new ArrayList<String>(2);
		int sizeOfChromosome = fistGenome.length();
		
		int firstSplitPoint = rand.nextInt(sizeOfChromosome);
		int secondSplitPoint = rand.nextInt(sizeOfChromosome);
		
		//Sort split points
		if (firstSplitPoint > secondSplitPoint) {
			int swap = firstSplitPoint;
			firstSplitPoint = secondSplitPoint;
			secondSplitPoint = swap;
		}
		
		//Create first offspring
		String leftPart = StringUtils.left(fistGenome, firstSplitPoint);
		String midPart = StringUtils.mid(secondGenome, firstSplitPoint, secondSplitPoint - firstSplitPoint);
		String rightPart = StringUtils.right(fistGenome, sizeOfChromosome - secondSplitPoint);
		String newFirstChromosomes = String.format("%s%s%s", leftPart, midPart, rightPart);
		
		//Create second offspring
		leftPart = StringUtils.left(secondGenome, firstSplitPoint);
		midPart = StringUtils.mid(fistGenome, firstSplitPoint, secondSplitPoint - firstSplitPoint);
		rightPart = StringUtils.right(secondGenome, sizeOfChromosome - secondSplitPoint);
		String newSecondChromosomes = String.format("%s%s%s", leftPart, midPart, rightPart);
		
		newChromosomes.add(newFirstChromosomes);
		newChromosomes.add(newSecondChromosomes);
		
		logger.debug("Cross [" + firstSplitPoint + ";" + secondSplitPoint + "] " + fistGenome + " X " + secondGenome + " => " +newFirstChromosomes + " + " + newSecondChromosomes);
		
		return newChromosomes;
	}

	/**
	 * One-point crossing genes.
	 * @param firstGenome The first genome.
	 * @param secondGenome The second genome.
	 * @return List of two new genomes.
	 */
	private List<String> onePointCrossover(String firstGenome, String secondGenome) {
		List<String> newChromosomes = new ArrayList<String>(2);
		int sizeOfChromosome = firstGenome.length();
		
		int splitPoint = rand.nextInt(sizeOfChromosome);
		
		//Create first offspring
		String firstPart = StringUtils.left(firstGenome, splitPoint);
		String secondPart = StringUtils.right(secondGenome, sizeOfChromosome - splitPoint);
		String newFirstChromosomes = String.format("%s%s", firstPart, secondPart);
		
		//Create second offspring
		firstPart = StringUtils.left(secondGenome, splitPoint);
		secondPart = StringUtils.right(firstGenome, sizeOfChromosome - splitPoint);
		String newSecondChromosomes = String.format("%s%s", firstPart, secondPart);
		
		newChromosomes.add(newFirstChromosomes);
		newChromosomes.add(newSecondChromosomes);
		
		logger.debug("Cross [" + splitPoint + "] " + firstGenome + " X " + secondGenome + " => " +newFirstChromosomes + " + " + newSecondChromosomes);
		
		return newChromosomes;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Chromosome))
			return false;
		Chromosome other = (Chromosome) obj;
		if (genome == null) {
			if (other.genome != null)
				return false;
		} else if (!genome.equals(other.genome))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((genome == null) ? 0 : genome.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Chromosome [code=" + genome + ", fitness value=" + fitnessValue + "]";
	}
}
