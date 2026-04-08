package com.deta.ac1;

import java.util.Arrays;
import java.util.List;

public class State {
    private final char value;
    private char[] nextChars = new char[0];
    private State[] nextStates = new State[0];
    private State suffix = null;
    private State output = null;
    private int terminalLength = 0; // match length for terminal state

    State(char value) {
        this.value = value;
    }

    State() {
        this.value = 0;
        this.suffix = this;
        this.output = null;
    }

    public String toString() {
        return value + ":" + ((output != null) ? "o:" + output.value : "") + " / s:" + suffix.value;
    }

    public void calcSuff(NextsMap nexts, ParentMap parents) {
        if (isRoot())
            suffix = this;
        else {
            char v = value;
            State lookup = parents.get(this);
            while (suffix == null) {
                if (lookup.isRoot()) {
                    suffix = lookup;
                    break;
                }
                State s = lookup.suffix;
                State child = nexts.slowNext(s, v);
                if (child != null) {
                    suffix = child;
                } else {
                    lookup = lookup.suffix;
                }
            }
        }
        // suffix is set,
        if (terminalLength > 0) {
            output = suffix;
        } else {
            output = suffix.output;
        }

        List<State> next = nexts.get(this);
        next.sort(State::compareStates);
        nextChars = new char[next.size()];
        nextStates = new State[next.size()];
        for (int i = 0; i < next.size(); i++) {
            State state = next.get(i);
            nextChars[i] = state.value;
            nextStates[i] = state;
        }
    }

    private static int compareStates(State a, State b) {
        return a.value - b.value;
    }

    public State next(char v) {
        int ix = Arrays.binarySearch(nextChars, v);
        if (ix >= 0) return nextStates[ix];
        return null;
    }

    boolean isRoot() {
        return value == 0;
    }

    public void setFinal(int length) {
        this.terminalLength = length;
    }

    public boolean isTerminal() {
        return terminalLength > 0;
    }

    public boolean matchValue(char v) {
        return value == v;
    }

    public int length() {
        return terminalLength;
    }

    public State output() {
        return output;
    }
}