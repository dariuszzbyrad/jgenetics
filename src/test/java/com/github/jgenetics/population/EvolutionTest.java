package com.github.jgenetics.population;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class EvolutionTest {
	
	private static final double DEFAULT_DELTA = 0.01;
	
	private static final String FIRST_GENOME = "10010";
	
	private static final String SECOND_GENOME = "01110";
	
	private static final String THIRD_GENOME = "00000";
	
	private Population population;
	
	private Evolution evolution;
	
	private FitnessFunction numberOnesFunction;
	
	@Before
	public void setUp() throws Exception {
		numberOnesFunction = new FitnessFunction() {
			@Override
			public double calculate(String genom) {
				Long numberOnes = 
						genom.chars()
						.mapToObj(i -> (char)i)
						.filter(c -> c == '1')
						.count();
				
				return (double) numberOnes / genom.length();
			}
		};
		evolution = new Evolution();
		
		List<Chromosome> chromosomes = new LinkedList<Chromosome>();
		chromosomes.add(new Chromosome(FIRST_GENOME));
		chromosomes.add(new Chromosome(SECOND_GENOME));
		chromosomes.add(new Chromosome(THIRD_GENOME));
		
		population = new Population();
		population.setChromosomes(chromosomes);
		population = population.calculateFitnessFunction(numberOnesFunction);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testNormalizationFitnessValues() throws Exception {
		List<Chromosome> chromosomes = new ArrayList<>();
		
		Chromosome firstChromosome = new Chromosome("01");
		firstChromosome.setFitnessValue(-3.21);
		chromosomes.add(firstChromosome);
		
		Chromosome secondChromosome = new Chromosome("01");
		secondChromosome.setFitnessValue(-2.92);
		chromosomes.add(secondChromosome);
		
		Chromosome thirdChromosome = new Chromosome("01");
		thirdChromosome.setFitnessValue(10);
		chromosomes.add(thirdChromosome);

		Method normalizeFitnessValuesMethod = Evolution.class.getDeclaredMethod("normalizeFitnessValues", List.class);
		normalizeFitnessValuesMethod.setAccessible(true);
		List<Double> normalizedFitnessValues = 
				(List<Double>) normalizeFitnessValuesMethod.invoke(evolution, chromosomes);
		
		assertEquals(0, normalizedFitnessValues.get(0), DEFAULT_DELTA);
		assertEquals(2.2, normalizedFitnessValues.get(1), DEFAULT_DELTA);
		assertEquals(100, normalizedFitnessValues.get(2), DEFAULT_DELTA);
	}
}
