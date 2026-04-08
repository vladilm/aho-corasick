package com.deta.ac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class State<S, P> {
    private final int id;
    private final Map<S, State<S, P>> transitions = new LinkedHashMap<>();
    private final List<Output<P>> outputs = new ArrayList<>();
    private State<S, P> failure;

    State(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }

    public Map<S, State<S, P>> transitions() {
        return Collections.unmodifiableMap(transitions);
    }

    public State<S, P> failure() {
        return failure;
    }

    public List<Output<P>> outputs() {
        return Collections.unmodifiableList(outputs);
    }

    State<S, P> transition(S symbol) {
        return transitions.get(symbol);
    }

    void putTransition(S symbol, State<S, P> target) {
        transitions.put(Objects.requireNonNull(symbol, "symbol"), Objects.requireNonNull(target, "target"));
    }

    void setFailure(State<S, P> failure) {
        this.failure = Objects.requireNonNull(failure, "failure");
    }

    void addOutput(Output<P> output) {
        outputs.add(Objects.requireNonNull(output, "output"));
    }

    void addOutputs(Collection<Output<P>> outputs) {
        this.outputs.addAll(outputs);
    }
}
