package com.github.jgenetics.model;

import com.github.jgenetics.model.chromosome.BinaryChromosome;
import com.github.jgenetics.model.chromosome.Chromosome;
import com.github.jgenetics.model.chromosome.ChromosomeType;
import com.github.jgenetics.model.chromosome.ChromosomeValue;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Population {

    private final List<Chromosome> population;
    private Function<ChromosomeValue, Float> fitnessFunction;

    public static Population init(ChromosomeType type, int size, int chromosomeLength) {
        return new Population(IntStream.range(0, size)
                .mapToObj(generateChromosome(chromosomeLength, type))
                .collect(Collectors.toList()));
    }

    public Population withFitnessFunction(Function<ChromosomeValue, Float> fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
        return this;
    }

    public Population calculateFitnessFunction() {
        if (Objects.isNull(fitnessFunction)) {
            throw new IllegalStateException("Fitness function must be defined before invoke");
        }

        population.forEach(p -> p.calculateFitnessValue(fitnessFunction));
        return this;
    }

    private static IntFunction<Chromosome> generateChromosome(int chromosomeLength, ChromosomeType type) {
        switch (type) {
            case BINARY:
                return (i) -> BinaryChromosome.random(chromosomeLength);
            case MULTIPLE_FLOAT:
            case SINGLE_FLOAT:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        return null;
    }
}
