package com.github.jgenetics.model.chromosome;

import com.github.jgenetics.model.gene.BinaryGene;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BinaryChromosome extends Chromosome<BinaryGene> implements ChromosomeValue {
    private BinaryChromosome(List<BinaryGene> binaryGenes) {
        super(0, binaryGenes);
    }

    public static Chromosome random(int length) {
       validateChromosomeLength(length);
       
        return new BinaryChromosome(
                IntStream.range(0, length)
                        .mapToObj((i) -> BinaryGene.random())
                        .collect(Collectors.toList())
        );
    }

    private static void validateChromosomeLength(int length) {
        if (length <= 1) {
            throw new IllegalArgumentException("Minimum length of chromosome is 2. Provided " + length);
        }
    }

    protected ChromosomeType getType() {
        return ChromosomeType.BINARY;
    }

    @Override
    public void calculateFitnessValue(Function<ChromosomeValue, Float> fitnessFunction) {
        fitnessValue = fitnessFunction.apply(this);
    }

    @Override
    public List<BinaryGene> getGenes() {
        return genes;
    }
}
