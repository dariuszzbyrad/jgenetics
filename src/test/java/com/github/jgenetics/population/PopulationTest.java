package com.github.jgenetics.population;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.jgenetics.exception.GeneticsException;

public class PopulationTest {
	
	private static final double DEFAULT_DELTA = 0;
	
	private FitnessFunction numberOnesFunction;
	
	@Before
	public void setUp() {
		numberOnesFunction = new FitnessFunction() {
			@Override
			public double calculate(String genom) {
				Long numberOnes = 
						genom.chars()
						.mapToObj(i -> (char)i)
						.filter(c -> c == '1')
						.count();
				
				return numberOnes.doubleValue();
			}
		};
	}
	
	@Test
	public void generateEmptyPopulaton() {
		int populationSize = 0;
		int sizeOfChromosome = 2;
		String encodingType = "ABCD";
		
		Population population = Population.generate(populationSize, sizeOfChromosome, encodingType);
		
		assertEquals(0, population.getChromosomes().size());
	}
	
	@Test
	public void defaultEmptyPopulaton() {
		Population population = new Population();
		
		assertEquals(0, population.getChromosomes().size());
	}
	
	@Test
	public void validLengthAndNumberOfChromosomes() {
		int populationSize = 5;
		int sizeOfChromosome = 2;
		String encodingType = "ABCD";
		
		Population population = Population.generate(populationSize, sizeOfChromosome, encodingType);
		
		long numberOfCorrectLengthChromosome = 
				population.getChromosomes()
				.stream()
				.filter(c -> c.getGenome().length() == sizeOfChromosome)
				.count();
		
		assertEquals(populationSize, numberOfCorrectLengthChromosome);
		assertEquals(populationSize, population.getChromosomes().size());
	}
	
	@Test
	public void validStructureOfChromosomes() {
		int populationSize = 1;
		int sizeOfChromosome = 6;
		String encodingType = "0A1C^&#";
		
		Population population = Population.generate(populationSize, sizeOfChromosome, encodingType);
		
		String chromosome = population.getChromosomes().get(0).getGenome();
		
		long numberOfCorrectGene = 
				chromosome.chars()
				.mapToObj(i -> (char)i)
				.filter(c -> encodingType.indexOf(c) >= 0)
				.count();
		
		assertEquals(sizeOfChromosome, numberOfCorrectGene);
		assertEquals(populationSize, population.getChromosomes().size());
	}
	
	@Test
	public void calculateFitnessFunction() {
		Chromosome firstChromosome = new Chromosome("0010");
		Chromosome secondChromosome = new Chromosome("");
		List<Chromosome> chromosomes = new LinkedList<Chromosome>();
		chromosomes.add(firstChromosome);
		chromosomes.add(secondChromosome);
		
		Population population = new Population();
		population.setChromosomes(chromosomes);
		
		population = population.calculateFitnessFunction(numberOnesFunction);
		
		assertEquals(1, population.getChromosomes().get(0).getFitnessValue(), DEFAULT_DELTA);
		assertEquals(0, population.getChromosomes().get(1).getFitnessValue(), DEFAULT_DELTA);
	}
	
	@Test
	public void calculateStatistic() {
		Chromosome firstChromosome = new Chromosome("10010");
		Chromosome secondChromosome = new Chromosome("01110");
		List<Chromosome> chromosomes = new LinkedList<Chromosome>();
		chromosomes.add(firstChromosome);
		chromosomes.add(secondChromosome);
		
		Population population = new Population();
		population.setChromosomes(chromosomes);
		
		population = population.calculateFitnessFunction(numberOnesFunction);
		
		assertEquals(2, population.getStatistic().getMinFitnessValue(), DEFAULT_DELTA);
		assertEquals(2.5, population.getStatistic().getAvgFitnessValue(), DEFAULT_DELTA);
		assertEquals(3, population.getStatistic().getMaxFitnessValue(), DEFAULT_DELTA);
	}
	
	@Test
	public void validIsHomogeneousPopulation() throws GeneticsException {
		Chromosome firstChromosome = new Chromosome("0010");
		Chromosome secondChromosome = new Chromosome("0010");
		List<Chromosome> chromosomes = new LinkedList<Chromosome>();
		chromosomes.add(firstChromosome);
		chromosomes.add(secondChromosome);
		
		Population population = new Population();
		population.setChromosomes(chromosomes);
		
		assertTrue(population.isPopulationHomogeneous());
	}
	
	@Test
	public void validIsNotHomogeneousPopulation() throws GeneticsException {
		Chromosome firstChromosome = new Chromosome("0110");
		Chromosome secondChromosome = new Chromosome("0010");
		List<Chromosome> chromosomes = new LinkedList<Chromosome>();
		chromosomes.add(firstChromosome);
		chromosomes.add(secondChromosome);
		
		Population population = new Population();
		population.setChromosomes(chromosomes);
		
		assertFalse(population.isPopulationHomogeneous());
	}
	
	@Test
	public void validHomogeneousForEmptyPopulation() {
		List<Chromosome> chromosomes = new LinkedList<Chromosome>();
		
		Population population = new Population();
		population.setChromosomes(chromosomes);
		
		try {
			population.isPopulationHomogeneous();
			fail("Expected GeneticsException");
		} catch (GeneticsException e) {
			assertEquals("Population is empty", e.getMessage());
		}
	}
	
	@Test
	public void generatePopulationWithIncorrectSize() {
		int populationSize = -1;
		int sizeOfChromosome = 2;
		String encodingType = "ABCD";
		
		try {
			Population.generate(populationSize, sizeOfChromosome, encodingType);;
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid populationSize value", e.getMessage());
		}
	}
	
	@Test
	public void generatePopulationFromEmptyAlphabet() {
		int populationSize = 1;
		int sizeOfChromosome = 2;
		String encodingType = "";
		
		try {
			Population.generate(populationSize, sizeOfChromosome, encodingType);;
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid encodingType value", e.getMessage());
		}
	}
}
