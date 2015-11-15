package com.github.jgenetics;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import com.github.jgenetics.enums.Crossover;
import com.github.jgenetics.enums.Selection;
import com.github.jgenetics.exception.GeneticsException;
import com.github.jgenetics.model.Parameters;
import com.github.jgenetics.population.Chromosome;
import com.github.jgenetics.population.FitnessFunction;

public class KnapsackProblem {

	public static void main(String[] args) {
		double weightLimit = 20;
		List<Double> weights = Arrays.asList(1.0, 5.0, 10.0, 1.0, 7.0, 5.0, 1.0);
		List<Double> survivalPoints = Arrays.asList(10.0, 20.0, 15.0, 2.0, 30.0, 10.0, 30.0);
		
		GeneticsAlgorithm algorithm = new GeneticsAlgorithm();
		
		FitnessFunction fitnessFunction = new FitnessFunction() {
			
			@Override
			public double calculate(String genom) {
				double sumOfWeights = 0;
				double sumOfSurvivalPoints = 0;
				
				char[] genes = genom.toCharArray();
				
				for (int i=0; i<genes.length; i++) {
					if ('1' == genes[i]) {
						sumOfWeights += weights.get(i);
						sumOfSurvivalPoints += survivalPoints.get(i);
					}
				}
				
				//If the total weight of the packaged items were not exceeded
				if (sumOfWeights <= weightLimit) {
					return sumOfSurvivalPoints;
				} else {
					return 0;
				}
			}
		};
		
		Parameters parameters = Parameters.builder()
				.crossoverType(Crossover.TwoPoint)
				.encodingAlphabet("10")
				.maxIteration(40)
				.mutationRate(0.1)
				.populationSize(9)
				.selectionType(Selection.CAROUSEL)
				.sizeOfChromosome(weights.size())
				.build();
				
		Chromosome solution = Chromosome.EMPTY;
		try {
			solution = algorithm.run(parameters, fitnessFunction);
		} catch (GeneticsException e) {
			System.out.println("Something were wrong...");
		}
		
		assertEquals(new Chromosome("1101111"), solution);

	}
}
