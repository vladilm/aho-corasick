package com.deta.ac;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AhoCorasickMachineTest {
    @Test
    void findsClassicOverlappingMatches() {
        AhoCorasickMachine<Character, String> machine = new AhoCorasickBuilder<Character, String>()
            .addPattern(chars("he"), "he")
            .addPattern(chars("she"), "she")
            .addPattern(chars("his"), "his")
            .addPattern(chars("hers"), "hers")
            .build();

        List<Match<String>> matches = machine.scan(chars("ushers"));

        assertEquals(List.of(
            new Match<>(1, 4, "she"),
            new Match<>(2, 4, "he"),
            new Match<>(2, 6, "hers")
        ), matches);
    }

    @Test
    void findsClassic1OverlappingMatches() {
        AhoCorasickMachine<Character, String> machine = new AhoCorasickBuilder<Character, String>()
                .addPattern(chars("aabab"), "aabab")
                .build();

        List<Match<String>> matches = machine.scan(chars("aabaabab"));

        assertEquals(List.of(
                new Match<>(1, 4, "she"),
                new Match<>(2, 4, "he"),
                new Match<>(2, 6, "hers")
        ), matches);
    }

    @Test
    void findsSuffixMatches() {
        AhoCorasickMachine<Character, String> machine = new AhoCorasickBuilder<Character, String>()
            .addPattern(chars("a"), "a")
            .addPattern(chars("aa"), "aa")
            .addPattern(chars("aaa"), "aaa")
            .build();

        List<Match<String>> matches = machine.scan(chars("aaaa"));

        assertEquals(List.of(
            new Match<>(0, 1, "a"),
            new Match<>(0, 2, "aa"),
            new Match<>(1, 2, "a"),
            new Match<>(0, 3, "aaa"),
            new Match<>(1, 3, "aa"),
            new Match<>(2, 3, "a"),
            new Match<>(1, 4, "aaa"),
            new Match<>(2, 4, "aa"),
            new Match<>(3, 4, "a")
        ), matches);
    }

    @Test
    void rejectsEmptyPatterns() {
        AhoCorasickBuilder<Character, String> builder = new AhoCorasickBuilder<>();

        assertThrows(IllegalArgumentException.class, () -> builder.addPattern(List.of(), "empty"));
    }

    @Test
    void supportsStreamingScan() {
        AhoCorasickMachine<Character, String> machine = new AhoCorasickBuilder<Character, String>()
            .addPattern(chars("ab"), "ab")
            .addPattern(chars("bc"), "bc")
            .build();

        ScanState<Character, String> scanState = machine.start();
        List<Match<String>> matches = new ArrayList<>();
        for (Character symbol : chars("zabc")) {
            matches.addAll(scanState.next(symbol));
        }

        assertEquals(List.of(
            new Match<>(1, 3, "ab"),
            new Match<>(2, 4, "bc")
        ), matches);
    }

    private static List<Character> chars(String value) {
        List<Character> chars = new ArrayList<>(value.length());
        for (int i = 0; i < value.length(); i++) {
            chars.add(value.charAt(i));
        }
        return chars;
    }
}
