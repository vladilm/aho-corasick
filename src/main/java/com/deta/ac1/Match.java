package com.deta.ac1;

public class Match {
    private final int pos;
    private final int end;
    private final String match;

    public Match(int pos, int end, String match) {
        this.pos = pos;
        this.end = end;
        this.match = match;
    }

    public String getMatch() {
        return match;
    }

    public int pos() {
        return pos;
    }
}
