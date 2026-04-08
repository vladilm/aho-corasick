package com.deta.ac1;

import java.util.HashMap;

public class ParentMap extends HashMap<State, State> {
    State get(State s) {
        return super.get(s);
    }

    void set(State s, State p) {
        super.put(s, p);
    }
}
