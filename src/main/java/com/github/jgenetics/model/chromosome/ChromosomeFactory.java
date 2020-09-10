package com.github.jgenetics.model.chromosome;

public class ChromosomeFactory {

    public Chromosome randomChromosome(ChromosomeType type, int length) {
        switch (type) {
            case BINARY:
                return BinaryChromosome.random(length);
            case MULTIPLE_FLOAT:
            case SINGLE_FLOAT:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        return null;
    }
}
