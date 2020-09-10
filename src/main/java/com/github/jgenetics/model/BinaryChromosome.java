package com.github.jgenetics.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class BinaryChromosome extends Chromosome<BinaryGene> {
    private BinaryChromosome(List<BinaryGene> binaryGenes) {
        super(0, binaryGenes);
    }

    public static Chromosome random(int length) {
        //TODO add validator
        return new BinaryChromosome(
                IntStream.range(0, length).boxed()
                        .map(() -> BinaryGene.random())
                        .collect(ArrayList::new)
        );
    }

    protected ChromosomeType getType() {
        return ChromosomeType.BINARY;
    }
}
