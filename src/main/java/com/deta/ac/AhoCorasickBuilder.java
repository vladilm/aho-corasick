package com.deta.ac;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

public final class AhoCorasickBuilder<S, P> {
    private final State<S, P> root = new State<>(0);
    private int nextStateId = 1;
    private boolean built;

    public AhoCorasickBuilder() {
        root.setFailure(root);
    }

    public AhoCorasickBuilder<S, P> addPattern(Iterable<S> pattern, P payload) {
        ensureMutable();
        Objects.requireNonNull(pattern, "pattern");

        State<S, P> current = root;
        int length = 0;

        for (S symbol : pattern) {
            Objects.requireNonNull(symbol, "pattern symbol");

            State<S, P> next = current.transition(symbol);
            if (next == null) {
                next = newState();
                current.putTransition(symbol, next);
            }

            current = next;
            length++;
        }

        if (length == 0) {
            throw new IllegalArgumentException("Pattern must not be empty");
        }

        current.addOutput(new Output<>(payload, length));
        return this;
    }

    public AhoCorasickMachine<S, P> build() {
        ensureMutable();
        buildFailureLinks();
        built = true;
        return new AhoCorasickMachine<>(root);
    }

    private void buildFailureLinks() {
        Queue<State<S, P>> queue = new ArrayDeque<>();

        for (State<S, P> child : root.transitions().values()) {
            child.setFailure(root);
            queue.add(child);
        }

        while (!queue.isEmpty()) {
            State<S, P> state = queue.remove();

            for (var entry : state.transitions().entrySet()) {
                S symbol = entry.getKey();
                State<S, P> target = entry.getValue();

                State<S, P> fallback = state.failure();
                while (fallback != root && fallback.transition(symbol) == null) {
                    fallback = fallback.failure();
                }

                State<S, P> failureTarget = fallback.transition(symbol);
                if (failureTarget == null) {
                    failureTarget = root;
                }

                target.setFailure(failureTarget);
                target.addOutputs(failureTarget.outputs());
                queue.add(target);
            }
        }
    }

    private State<S, P> newState() {
        return new State<>(nextStateId++);
    }

    private void ensureMutable() {
        if (built) {
            throw new IllegalStateException("Builder was already used to build a machine");
        }
    }
}
