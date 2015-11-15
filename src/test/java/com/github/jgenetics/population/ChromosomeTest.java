package com.github.jgenetics.population;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.jgenetics.enums.Crossover;
import com.github.jgenetics.population.Chromosome;
import com.github.jgenetics.population.FitnessFunction;
import com.github.jgenetics.utils.Random;

public class ChromosomeTest {
	
	private static final double DEFAULT_DELTA = 0;
	
	private FitnessFunction numberOnesFunction;
	
	private Chromosome firstChromosome;
	
	private Chromosome secondChromosome;
	
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
		
		firstChromosome = new Chromosome("ABCDEF");
		secondChromosome = new Chromosome("QWERTY");
	}
	
	@Test
	public void validLengthOfGeneratedChromosomes() {
		int sizeOfChromosome = 2;
		String encodingType = "ABCD";
		
		Chromosome chromosome = Chromosome.generate(sizeOfChromosome, encodingType);
		
		assertEquals(sizeOfChromosome, chromosome.getGenome().length());
	}
	
	@Test
	public void validStructureOfGeneratedChromosomes() {
		int sizeOfChromosome = 6;
		String encodingType = "0A1C^&#";
		
		Chromosome chromosome = Chromosome.generate(sizeOfChromosome, encodingType);
		
		long numberOfCorrectGene = 
				chromosome.getGenome().chars()
				.mapToObj(i -> (char)i)
				.filter(c -> encodingType.indexOf(c) >= 0)
				.count();
		
		assertEquals(sizeOfChromosome, numberOfCorrectGene);
	}
	
	/**
	 * | - split point
	 * 
	 * Input pair:
	 * AB|CDEF
	 * QW|ERTY
	 * 
	 * Output pair:
	 * ABERTY
	 * QWCDEF
	 */
	@Test
	public void validOnePointCrossover() {
		Crossover crossoverType = Crossover.OnePoint;
		int splitPoint = 2;
		
		Chromosome expectedFirstChromosome = new Chromosome("ABERTY");
		Chromosome expectedSecondChromosome = new Chromosome("QWCDEF");
		
		Random random = mock(Random.class);  
		when(random.nextInt(anyInt())).thenReturn(splitPoint);
		
		firstChromosome.setRand(random);
		List<Chromosome> newChromosomes = firstChromosome.crosover(secondChromosome, crossoverType);
		
		assertEquals(expectedFirstChromosome, newChromosomes.get(0));
		assertEquals(expectedSecondChromosome, newChromosomes.get(1));
	}
	
	/**
	 * | - split point
	 * 
	 * Input pair:
	 * |ABCDEF|
	 * |QWERTY|
	 * 
	 * Output pair:
	 * ABCDEF
	 * QWERTY
	 */
	@Test
	public void validTwoPointCrossoverWithExtremeSplitPoints() {
		Crossover crossoverType = Crossover.TwoPoint;
		int firstSplitPoint = 0;
		int secondSplitPoint = 6;
		
		Chromosome expectedFirstChromosome = new Chromosome("ABCDEF");
		Chromosome expectedSecondChromosome = new Chromosome("QWERTY");
		
		Random random = mock(Random.class);  
		when(random.nextInt(anyInt())).thenReturn(firstSplitPoint).thenReturn(secondSplitPoint);
		
		firstChromosome.setRand(random);
		List<Chromosome> newChromosomes = firstChromosome.crosover(secondChromosome, crossoverType);
		
		assertEquals(expectedFirstChromosome, newChromosomes.get(1));
		assertEquals(expectedSecondChromosome, newChromosomes.get(0));
	}
	
	/**
	 * | - split point
	 * 
	 * Input pair:
	 * A|BCDE|F
	 * Q|WERT|Y
	 * 
	 * Output pair:
	 * AWERTF
	 * QBCDEY
	 */
	@Test
	public void validTwoPointCrossover() {
		Crossover crossoverType = Crossover.TwoPoint;
		int firstSplitPoint = 5;
		int secondSplitPoint = 1;
		
		Chromosome expectedFirstChromosome = new Chromosome("AWERTF");
		Chromosome expectedSecondChromosome = new Chromosome("QBCDEY");
		
		Random random = mock(Random.class);  
		when(random.nextInt(anyInt())).thenReturn(firstSplitPoint).thenReturn(secondSplitPoint);
		
		firstChromosome.setRand(random);
		List<Chromosome> newChromosomes = firstChromosome.crosover(secondChromosome, crossoverType);
		
		assertEquals(expectedFirstChromosome, newChromosomes.get(0));
		assertEquals(expectedSecondChromosome, newChromosomes.get(1));
	}
	
	@Test(expected = IllegalArgumentException.class)  
	public void invalidLengthOfChromosomesInCrossover() {
		Crossover crossoverType = Crossover.OnePoint;
		Chromosome firstChromosome = new Chromosome("ABCD");
		Chromosome secondChromosome = new Chromosome("QWERTY");

		firstChromosome.crosover(secondChromosome, crossoverType);
	}
	
	@Test
	public void validMutation() {
		int sizeOfChromosome = 6;
		String encodingType = "0A1C^&#";
		
		Chromosome chromosome = Chromosome.generate(sizeOfChromosome, encodingType);
		chromosome = chromosome.mutation(0.5, encodingType);
		
		long numberOfCorrectGene = 
				chromosome.getGenome().chars()
				.mapToObj(i -> (char)i)
				.filter(c -> encodingType.indexOf(c) >= 0)
				.count();
		
		assertEquals(sizeOfChromosome, numberOfCorrectGene);
	}
	
	@Test
	public void validCalculateFitnessFunction() {
		double expectedFitnessFunctionValue = 0;
		double fitnessFunctionValue = firstChromosome.calculateFitnessValue(numberOnesFunction).getFitnessValue();
		
		assertEquals(expectedFitnessFunctionValue, fitnessFunctionValue, DEFAULT_DELTA);
	}
}
