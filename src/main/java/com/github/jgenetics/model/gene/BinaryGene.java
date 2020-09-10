package com.github.jgenetics.model.gene;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class BinaryGene implements Gene<Boolean> {

    private final Boolean value;

    public static BinaryGene random() {
        return new BinaryGene(Math.random() < 0.5);
    }

    public Boolean getValue() {
        return value;
    }
}
