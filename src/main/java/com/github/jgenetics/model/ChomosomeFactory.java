package com.github.jgenetics.model;

public class ChomosomeFactory {

    public Chromosome randomChromosome(ChromosomeType type) {
        switch (type) {
            case BINARY:
                return BinaryChromosome.random();
            case MULTIPLE_FLOAT:
            case SINGLE_FLOAT:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        return null;
    }
}
