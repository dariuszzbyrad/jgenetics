package com.github.jgenetics.model;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
abstract class Chromosome<Gene> {

    protected float fitnessValue;

    protected List<Gene> genes;

    protected abstract ChromosomeType getType();
}
