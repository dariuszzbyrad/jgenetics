package com.github.jgenetics.model.chromosome;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
public abstract class Chromosome<Gene> {

    protected float fitnessValue = 0;

    protected List<Gene> genes;

    protected abstract ChromosomeType getType();

    public abstract void calculateFitnessValue(Function<ChromosomeValue, Float> fitnessFunction);
}
