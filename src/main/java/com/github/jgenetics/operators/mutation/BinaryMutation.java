package com.github.jgenetics.operators.mutation;

import com.github.jgenetics.model.chromosome.BinaryChromosome;
import com.github.jgenetics.model.gene.BinaryGene;

import java.util.function.Predicate;

public class BinaryMutation {
    public BinaryChromosome apply(BinaryChromosome inputChromosome, float mutationRate) {
        validMutationRate(mutationRate);

        inputChromosome.getGenes().stream()
                .filter(shouldBeMutate(mutationRate))
                .forEach(BinaryGene::switchToOppositeValue);

        return inputChromosome;
    }

    private void validMutationRate(float mutationRate) {
        if (mutationRate < 0 || mutationRate > 1) {
            throw new IllegalArgumentException("Mutation rate must be between 0.0 and 1.0. Provided: " + mutationRate);
        }
    }

    private Predicate<BinaryGene> shouldBeMutate(float mutationRate) {
        return g -> Math.random() < mutationRate;
    }
}
