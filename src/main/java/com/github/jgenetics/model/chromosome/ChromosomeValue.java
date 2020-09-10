package com.github.jgenetics.model.chromosome;

import com.github.jgenetics.model.gene.Gene;

import java.util.List;

public interface ChromosomeValue {
    List<? extends Gene> getGenes();
}
