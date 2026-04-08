package com.deta.ac1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NextsMap extends HashMap<State, List<State>> {
    List<State> get(State s) {
        return computeIfAbsent(s, k -> new ArrayList<>());
    }

    void add(State state, State s) {
        computeIfAbsent(state, k -> new ArrayList<>()).add(s);
    }

    State slowNext(State s1, char v) {
        List<State> next = get(s1);
        for (State s : next) {
            if (s.matchValue(v)) return s;
        }
        return null;
    }

}
