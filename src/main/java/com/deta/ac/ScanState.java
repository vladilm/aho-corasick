package com.deta.ac;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ScanState<S, P> {
    private final State<S, P> root;
    private State<S, P> current;
    private int index;

    ScanState(State<S, P> root) {
        this.root = Objects.requireNonNull(root, "root");
        this.current = root;
    }

    public List<Match<P>> next(S symbol) {
        current = advance(symbol);
        int endExclusive = index + 1;
        List<Match<P>> matches = new ArrayList<>(current.outputs().size());

        for (Output<P> output : current.outputs()) {
            matches.add(new Match<>(
                endExclusive - output.patternLength(),
                endExclusive,
                output.payload()
            ));
        }

        index++;
        return matches;
    }

    public int index() {
        return index;
    }

    public State<S, P> current() {
        return current;
    }

    public void reset() {
        current = root;
        index = 0;
    }

    private State<S, P> advance(S symbol) {
        State<S, P> state = current;

        while (state != root && state.transition(symbol) == null) {
            state = state.failure();
        }

        State<S, P> next = state.transition(symbol);
        return next != null ? next : root;
    }
}
