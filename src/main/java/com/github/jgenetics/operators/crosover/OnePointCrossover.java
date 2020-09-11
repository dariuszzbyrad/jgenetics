package com.github.jgenetics.operators.crosover;

import com.github.jgenetics.model.chromosome.Chromosome;
import com.github.jgenetics.model.chromosome.ChromosomeValue;
import com.github.jgenetics.model.gene.Gene;
import io.vavr.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class OnePointCrossover {

    Tuple2<Chromosome, Chromosome> simpleCross(ChromosomeValue firstChromosome, ChromosomeValue secondChromosome, int point) {
        validateCrossoverPoint(firstChromosome.getGenes(), point);

        List<? extends Gene> firstGenes = firstChromosome.getGenes();
        List<? extends Gene> secondGenes = secondChromosome.getGenes();

        List<Gene> firstOffspring = new ArrayList<>();
        firstOffspring.addAll(firstGenes.subList(0, point));
        firstOffspring.addAll(secondGenes.subList(point, secondGenes.size() - 1));

        List<Gene> secondOffspring = new ArrayList<>();
        secondOffspring.addAll(secondGenes.subList(0, point));
        secondOffspring.addAll(firstGenes.subList(point, firstGenes.size() - 1));

        return new Tuple2(firstOffspring, secondOffspring);
    }

    private void validateCrossoverPoint(List<? extends Gene> genes, int point) {
        if (point < 0 || point >= genes.size()) {
            throw new IllegalArgumentException("Invalid crossover point. Provided: " + point);
        }
    }
}
