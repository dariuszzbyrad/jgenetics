package com.github.jgenetics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.github.jgenetics.GeneticsAlgorithm;
import com.github.jgenetics.enums.Crossover;
import com.github.jgenetics.enums.Selection;
import com.github.jgenetics.exception.GeneticsException;
import com.github.jgenetics.model.Parameters;
import com.github.jgenetics.population.Chromosome;
import com.github.jgenetics.population.FitnessFunction;

public class BasicIT {
	
	/**
	 * The 2x^2+1 function
	 */
	private FitnessFunction fitnessFunction;
	
	private GeneticsAlgorithm algorithm;
	
	@Before
	public void setUP() {
		
		algorithm = new GeneticsAlgorithm();
		
		fitnessFunction = new FitnessFunction() {
			@Override
			public double calculate(String genom) {
				char[] genomChars = genom.toCharArray();
				double x = 0.0;		
				
				x += Character.getNumericValue(genomChars[0]) * 2;
				x += Character.getNumericValue(genomChars[1]) * 1;
				x += Character.getNumericValue(genomChars[2]) * 0.5;
				x += Character.getNumericValue(genomChars[3]) * 0.25;
				x += Character.getNumericValue(genomChars[4]) * 0.125;
				x += Character.getNumericValue(genomChars[5]) * 0.0625;
				x += Character.getNumericValue(genomChars[6]) * 0.03125;
				
				return 2*x*x+1;
			}
		};
	}
	
	@Test
	public void firstTest() throws GeneticsException {
		Parameters parameters = Parameters.builder()
				.crossoverType(Crossover.OnePoint)
				.encodingAlphabet("10")
				.maxIteration(100)
				.mutationRate(0.2)
				.populationSize(5)
				.selectionType(Selection.CAROUSEL)
				.sizeOfChromosome(7)
				.build();
				
		Chromosome solution = algorithm.run(parameters, fitnessFunction);
		
		assertEquals(new Chromosome("1111111"), solution);
	}
}
