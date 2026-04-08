package com.deta.ac1;

import java.util.*;
import java.util.function.Consumer;

public class ACStringMatcher {
    private final NextsMap next = new NextsMap();
    private final ParentMap parent = new ParentMap();
    private final State root = new State();

    public ACStringMatcher(String ... searches) {
        parent.set(root, root);
        for (String search : searches) {
            State c = root;
            char[] charArray = search.toCharArray();
            int length = charArray.length;
            for (char value : charArray) {
                c = buildChild(c, value);
            }
            c.setFinal(length);

        }
        bfs((s) -> s.calcSuff(next, parent));
    }

    State buildChild(State c, char value) {
        for (State s : next.get(c)) {
            if (s.matchValue(value)) return s;
        }
        State s = new State(value);
        parent.set(s, c);
        next.add(c, s);
        return s;
    }

    public void bfs(Consumer<State> consumer) {
        Queue<State> bfsStatePass = new ArrayDeque<>();
        bfsStatePass.add(root);
        while (!bfsStatePass.isEmpty()) {
            State s = bfsStatePass.poll();
            consumer.accept(s);
            bfsStatePass.addAll(next.get(s));
        }
    }


    public List<Match> search(String string) {
        List<Match> out = new ArrayList<>();
        State s = root;
        int length = string.length();
        for (int i = 0; i < length; i++) {
            assert s != null;
            s = s.next(string.charAt(i));
            State t = s;
            while (t != null) {
                if (t.isTerminal()) {
                    int end = i + 1;
                    out.add(new Match(end - t.length(), end, string.substring(end - t.length(), end)));
                }
                t = t.output();
            }
        }
        return out;
    }

    static void main() {
        ACStringMatcher acsm = new ACStringMatcher("e", "he", "she", "hers", "his");
        List<Match> out = acsm.search("he");
        for (Match m : out) {
            System.out.println("Pos:" + m.pos() + ":" + m.getMatch());
        }
    }
}
