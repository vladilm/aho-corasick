package com.deta.ac;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class AhoCorasickMachine<S, P> {
    private final State<S, P> root;

    AhoCorasickMachine(State<S, P> root) {
        this.root = Objects.requireNonNull(root);
    }

    public State<S, P> root() {
        return root;
    }

    public List<Match<P>> scan(Iterable<S> input) {
        Objects.requireNonNull(input, "input");

        State<S, P> state = root;
        int index = 0;
        List<Match<P>> matches = new ArrayList<>();

        for (S symbol : input) {
            state = advance(state, symbol);

            int endExclusive = index + 1;
            for (Output<P> output : state.outputs()) {
                matches.add(new Match<>(
                    endExclusive - output.patternLength(),
                    endExclusive,
                    output.payload()
                ));
            }

            index++;
        }

        return matches;
    }

    public ScanState<S, P> start() {
        return new ScanState<>(root);
    }

    State<S, P> advance(State<S, P> state, S symbol) {
        State<S, P> current = state;

        while (current != root && current.transition(symbol) == null) {
            current = current.failure();
        }

        State<S, P> next = current.transition(symbol);
        return next != null ? next : root;
    }
}
