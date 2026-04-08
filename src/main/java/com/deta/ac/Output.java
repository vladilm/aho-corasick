package com.deta.ac;

public record Output<P>(P payload, int patternLength) {
    public Output {
        if (patternLength <= 0) {
            throw new IllegalArgumentException("patternLength must be positive");
        }
    }
}
