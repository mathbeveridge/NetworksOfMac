package edu.macalester.mscs.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ari Weiland
 */
public class WordUtils {

    public static boolean isCapitalized(String word) {
        return word.matches("[A-Z][a-z]+");
    }

    public static boolean containsWord(String input, String word) {
        return input.matches("(.*\\W)?" + word + "(\\W.*)?");
    }

    public static boolean endsWithWord(String input, String word) {
        return input.matches("(.*\\W)?" + word);
    }

    public static boolean precedesSentenceStart(String part, String punctuation) {
        for (int i=0; i<punctuation.length(); i++) {
            if (part.contains("" + punctuation.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static Set<String> getPlurals(Set<String> words) {
        Set<String> plurals = new HashSet<>();
        for (String w : words) {
            if (!w.endsWith("s")) {
                plurals.add(w + "s");
            }
        }
        return plurals;
    }


}
