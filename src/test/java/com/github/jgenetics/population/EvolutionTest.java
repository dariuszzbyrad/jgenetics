package com.github.jgenetics.population;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.internal.WhiteboxImpl;

import com.github.jgenetics.population.Chromosome;
import com.github.jgenetics.population.Evolution;
import com.github.jgenetics.population.FitnessFunction;
import com.github.jgenetics.population.Population;
import com.github.jgenetics.utils.Utils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Evolution.class, Utils.class})
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
	
	@Test
	@Ignore
	public void testSelectionChromosomesForCarousel() throws Exception {
		 PowerMockito.spy(Utils.class);
		 given(Utils.generateRandomValue(Mockito.anyDouble(), Mockito.anyDouble())).willReturn(60.0, 40.0);

		
		List<Chromosome> newChromosomes = 
				WhiteboxImpl.invokeMethod(evolution, "getCandidatesForCarousel", population);
		
		assertEquals(SECOND_GENOME, newChromosomes.get(0).getGenome());
		assertEquals(FIRST_GENOME, newChromosomes.get(1).getGenome());
		assertEquals(SECOND_GENOME, newChromosomes.get(2).getGenome());
	}
	
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
		
		List<Double> normalizedFitnessValues= 
				WhiteboxImpl.invokeMethod(evolution, "normalizeFitnessValues", chromosomes);

		assertEquals(0, normalizedFitnessValues.get(0), DEFAULT_DELTA);
		assertEquals(2.2, normalizedFitnessValues.get(1), DEFAULT_DELTA);
		assertEquals(100, normalizedFitnessValues.get(2), DEFAULT_DELTA);
	}
}
