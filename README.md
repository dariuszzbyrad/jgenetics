# jGenetics
Library to simple use genetic algorithm in Java.

Algorithm
=========

The algorithm parameters
------------------------
* __sizeOfChromosome__ - String length coding
* __populationSize__ - Number of chromosomes in each population
* __mutationRate__ - Probability of each gene mutation 
* __encodingAlphabet__ - Set of available values for the gene, for example: "01", "1234567890", "ABC", etc
* __selectionType__ - Type of selection chromosome pairs to crosover. In this moment, available only CAROUSEL type
* __crossoverType__ - Type of crossover chromosomes. Available: one point and two points
* __maxIteration__ - Max numbers of algorithm iteration

Stop condition
--------------
Currently, the only available stop condition is exceed the amount of set iterations or homogeneous iteration (all chromosomes in the population have the same coding sequence).

Example - the knapsack problem
==========================

Description
----------------
Solution of traditional knapsack problem.

See description: https://en.wikipedia.org/wiki/Knapsack_problem

Parameters
----------

__Items:__

| Item number | Weight | Survival points |
| ----------- | :----: | :-------------: |
| 1           | 1      | 10              |
| 2           | 5      | 20              |
| 3           | 10     | 15              |
| 4           | 1      | 2               |
| 5           | 7      | 30              |
| 6           | 5      | 10              |
| 7           | 1      | 30              |

__Capacity of the knapsack:__ 20

__Solution:__ 1101111 (Items: 1, 2, 4, 5, 6, 7)

Code
----
```java
    //Define the knapsack problem parameters
    double weightLimit = 20;
	List<Double> weights = Arrays.asList(1.0, 5.0, 10.0, 1.0, 7.0, 5.0, 1.0);
	List<Double> survivalPoints = Arrays.asList(10.0, 20.0, 15.0, 2.0, 30.0, 10.0, 30.0);
		
	GeneticsAlgorithm algorithm = new GeneticsAlgorithm();
	
	//Define fitness function	
	FitnessFunction fitnessFunction = new FitnessFunction() {
			
		@Override
		public double calculate(String genom) {
			double sumOfWeights = 0;
			double sumOfSurvivalPoints = 0;
			
			char[] genes = genom.toCharArray();
			
			for (int i=0; i<genes.length; i++) {
				if ('1' == genes[i]) { //Pack the item?
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
	
	//Define parameters for genetics algorithm	
	Parameters parameters = Parameters.builder()
			.crossoverType(Crossover.TwoPoint)
			.encodingAlphabet("10") //Binary encoding
			.maxIteration(40)
			.mutationRate(0.1)
			.populationSize(9)
			.selectionType(Selection.CAROUSEL)
			.sizeOfChromosome(weights.size())
			.build();
			
	Chromosome solution = Chromosome.EMPTY;
	try {
		solution = algorithm.run(parameters, fitnessFunction); //Run algorithm
	} catch (GeneticsException e) {
		System.out.println("Something were wrong...");
	}
	
	assertEquals(new Chromosome("1101111"), solution);
```

TODO
====

Short term
----------
- Add to maven central repo
- Crossover probability
- An additional type of selection - tournament selection
- An additional type of crossover - uniform
- Inheritance always two best chromosomes
 
Medium term
-----------
- Rebuilding to be faster an dstronger
- Refactoring to be easier

Long term
---------
- Developed strategies


