package edu.macalester.mscs.characters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Ari Weiland
 */
public class CharacterFinder {

    public static final Set<String> IGNORE_WORDS = new HashSet<>(Arrays.asList(
            "A", "The", "There", "That", "This", "These", "Those", "Then", // articles
            "I", "My", "You", "Your", "He", "His", "She", "Her", "It", "We", "Our", "They", "Their", // pronouns
            "For", "And", "Nor", "But", "Or", "Yet", "So", // conjuctions
            "Who", "What", "Where", "When", "Why", "How", // questions
            "If", "As", "In", "Do", "No", "Yes", "Even", // miscellaneous
            "Ser", "House", "Houses", "Lords", "Ladies", "Kings" // GoT specific words
    ));

    public static final Set<String> TITLES = new HashSet<>(Arrays.asList(
            "Lord", "Lady", "Maester" // these should never stand alone
    ));

    private static void addToCounter(Map<String, Integer> counter, String string) {
        if (counter.containsKey(string)) {
            counter.put(string, counter.get(string) + 1);
        } else {
            counter.put(string, 1);
        }
    }

    private static List<String> breakLine(String line) {
        List<String> pieces = new ArrayList<>();
        StringBuilder sb = null;
        boolean letters = true;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (Character.isAlphabetic(c)) {
                if (sb == null) {
                  sb = new StringBuilder();
                }
                if (letters) {
                    sb.append(c);
                } else {
                    pieces.add(sb.toString());
                    letters = true;
                    sb = new StringBuilder("" + c);
                }
            } else {
                if (sb != null) { // ignore leading punctuation
                    if (!letters) {
                        sb.append(c);
                    } else {
                        pieces.add(sb.toString());
                        letters = false;
                        sb = new StringBuilder("" + c);
                    }
                }
            }
        }
        if (sb != null) {
            pieces.add(sb.toString());
        }
        return pieces;
    }

    private static boolean isCapitalized(String word) {
        return word.matches("[A-Z][a-z]*");
    }

    public static void main(String[] args) {
        Map<String, Integer> counter = new HashMap<>();

        String line = null;
        BufferedReader fileReader = null;
        BufferedWriter writer = null;
        try {
            fileReader = new BufferedReader(new FileReader("src/main/resources/text/got.txt"));
//            writer = new BufferedWriter(new FileWriter("src/main/resources/data/characters/#######"));
            while ((line = fileReader.readLine()) != null) {
                line = line.trim();
                List<String> parts = breakLine(line);
                StringBuilder phrase = null;
                for (String part : parts) {
                    if (isCapitalized(part) && !IGNORE_WORDS.contains(part)) {
                        if (!TITLES.contains(part)) {
                            addToCounter(counter, part);
                        }
                        if (phrase == null) {
                            phrase = new StringBuilder(part);
                        } else {
                            phrase.append(part);
                            addToCounter(counter, phrase.toString());
                        }
                    } else {
                        if (phrase != null) {
                            if (part.equals(" ") || part.equals("of") || part.equals("the")) {
                                phrase.append(part);
                            } else {
                                phrase = null;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error at \'" + line + "\'", e);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException ignored) {}
            }
//            if (writer != null) {
//                try {
//                    writer.close();
//                } catch (IOException ignored) {}
//            }
        }

        List<Map.Entry<String, Integer>> caps = new ArrayList<>(counter.entrySet());
        Collections.sort(caps, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });
        for (Map.Entry<String, Integer> cap : caps) {
            System.out.println(cap.getKey() + "\t" + cap.getValue());
        }
        System.out.println();
        System.out.println(counter.size());
    }

}
