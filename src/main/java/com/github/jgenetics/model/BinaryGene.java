package com.github.jgenetics.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class BinaryGene implements Gene<Boolean> {

    private final Boolean value;

    public static boolean random() {
        return Math.random() < 0.5;
    }

    public Boolean getValue() {
        return value;
    }

    public static Gene<Boolean> fromValue(Boolean value) {
        return new BinaryGene(value);
    }
}
