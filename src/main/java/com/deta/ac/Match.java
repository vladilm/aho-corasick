package com.deta.ac;

public record Match<P>(int start, int endExclusive, P payload) {
}
