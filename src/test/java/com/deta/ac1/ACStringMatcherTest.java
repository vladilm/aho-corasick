package com.deta.ac1;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ACStringMatcherTest {
    @Test
    void findsClassicOverlappingMatches() {
        ACStringMatcher acsm = new ACStringMatcher("he", "she", "his", "hers");
        List<Match> res = acsm.search("ushers");
        for (Match m : res) {
            System.out.println(m.getMatch());
        }
        assertEquals(List.of(
                new Match(1, 4, "she"),
                new Match(2, 4, "he"),
                new Match(2, 6, "hers")
        ), res);

    }

    @Test
    void findsClassic1OverlappingMatches() {
        ACStringMatcher acsm = new ACStringMatcher("aabab");
        List<Match> res = acsm.search("aabaabab");

        assertEquals(List.of(
                new Match(1, 4, "she"),
                new Match(2, 4, "he"),
                new Match(2, 6, "hers")
        ), res);
    }

    @Test
    void findsSuffixMatches() {
        ACStringMatcher machine = new ACStringMatcher("a", "aa", "aaa");
        List<Match> matches = machine.search("aaaa");

        assertEquals(List.of(
            new Match(0, 1, "a"),
            new Match(0, 2, "aa"),
            new Match(1, 2, "a"),
            new Match(0, 3, "aaa"),
            new Match(1, 3, "aa"),
            new Match(2, 3, "a"),
            new Match(1, 4, "aaa"),
            new Match(2, 4, "aa"),
            new Match(3, 4, "a")
        ), matches);
    }
}
