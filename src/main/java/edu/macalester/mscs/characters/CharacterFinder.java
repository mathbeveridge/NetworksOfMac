package edu.macalester.mscs.characters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Ari Weiland
 */
public class CharacterFinder {

    public static final Set<String> IGNORE_WORDS = new HashSet<>(Arrays.asList(
            "My", "You", "He", "His", "She", "It", "We", "They", "Their", // pronouns  (It???)
            "This", "That", "These", "Those", "There", // indirect pronouns
            "Who", "Where", "Why", "How", // questions
            "An", "Nor", "Do", "No", "Yes", "Afterward", "Ask", "While", "Old", "Men", "Above", "Done",
            "Does", "Certainly", "First", "To", "Without", "Unless", "Some", "Sometimes", "On", "Both", // miscellaneous
            "Ser", "House", "Houses", "Lords", "Ladies", "Kings" // GoT specific words
    ));

    public static final Set<String> TITLES = new HashSet<>(Arrays.asList(
            "Lord", "Lady", "Maester", "Maestor", "King", "Queen", "Prince", "Princess", "Regent",
            "Khal", "Ko", "Uncle", "Aunt", "Captain" // these should never stand alone
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

    private static AliasGroups getAliases(Map<String, Integer> counter, Set<String> nondescriptors) {
        AliasGroups aliasGroups = new AliasGroups();
        for (String alias : counter.keySet()) {
            if (!alias.contains(" ") && !nondescriptors.contains(alias)) {
                aliasGroups.addAlias(alias);
            }
        }
        for (String alias : counter.keySet()) {
            String[] split = alias.split(" ");
            if (split.length > 1) {
                String group = null;
                for (String s : split) {
                    if (aliasGroups.isAlias(s)) {
                        if (group == null) {
                            group = aliasGroups.getPrimaryAlias(s);
                            aliasGroups.addAliasToGroup(group, alias);
                        } else {
                            aliasGroups.combineGroups(group, s);
                        }
                    }
                }
            }
        }
        return aliasGroups;
    }

    public static void main(String[] args) {
        Map<String, Integer> counter = new HashMap<>();

        String line = null;
        BufferedReader fileReader = null;
//        BufferedWriter writer = null;
        try {
            fileReader = new BufferedReader(new FileReader("src/main/resources/text/got.txt"));
//            writer = new BufferedWriter(new FileWriter("src/main/resources/data/characters/#######"));
            while ((line = fileReader.readLine()) != null) {
                line = line.trim();
                List<String> parts = breakLine(line);
                StringBuilder phrase = null;
                String toAdd = null;
                for (String part : parts) {
                    if (part.length() > 1 && isCapitalized(part) && !IGNORE_WORDS.contains(part)) {
                        if (!TITLES.contains(part)) {
                            addToCounter(counter, part);
                        }
                        if (phrase == null) {
                            phrase = new StringBuilder(part);
                        } else {
                            phrase.append(part);
//                            addToCounter(counter, phrase.toString());
                            toAdd = phrase.toString();
                        }
                    } else {
                        if (phrase != null) {
                            if (part.equals(" ") || part.equals("of") || part.equals("the")) {
                                phrase.append(part);
                            } else {
                                if (toAdd != null) {
                                    addToCounter(counter, toAdd);
                                }
                                toAdd = null;
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

        // Words that are also found pluralized
        Set<String> surnames = new HashSet<>();
        for (String cap : counter.keySet()) {
            String[] split = cap.split(" ");
            for (String s : split) {
                if (counter.containsKey(s) && counter.containsKey(s + "s")) {
                    surnames.add(s);
                }
            }
        }
//        System.out.println(surnames);
//        System.out.println();

        // Pairs whose second word is from the surnames group
        Set<String> names = new HashSet<>();
        Set<String> firstNames = new HashSet<>();
        for (String cap : counter.keySet()) {
            String[] split = cap.split(" ");
            if (split.length == 2 && !TITLES.contains(split[0]) && surnames.contains(split[1])) {
                names.add(cap);
                firstNames.add(split[0]);
            }
        }
        System.out.println(names);
//        System.out.println(firstNames);
        System.out.println();

        // words that appear in front of different "names" *** Mya ***
        Set<String> bad = new HashSet<>();
        for (String cap : counter.keySet()) {
            String[] split = cap.split(" ");
            if (split.length > 1 && firstNames.contains(split[1]) && !TITLES.contains(split[0])) {
                bad.add(split[0]);
            }
        }
//        System.out.println(bad);
//        System.out.println();

        Map<String, Integer> temp = new HashMap<>(counter);
        for (String cap : counter.keySet()) {
            if (bad.contains(cap.split(" ")[0])) {
                temp.remove(cap);
            }
        }
        counter = temp;

        // Phrases following 'of' or 'of the'
        Set<String> places = new HashSet<>();
        for (String cap : counter.keySet()) {
            if (cap.contains(" of")) {
                String place = cap.substring(cap.indexOf(" of") + 4);
                if (place.startsWith("the")) {
                    place = place.substring(4);
                }
                places.add(place);
            }
        }
//        System.out.println(places);
//        System.out.println();

        // Words that are never parts of phrases
        Set<String> lonely = new HashSet<>(counter.keySet());
        for (String cap : counter.keySet()) {
            if (cap.contains(" ")) {
                lonely.remove(cap);
                for (String s : cap.split(" ")) {
                    lonely.remove(s);
                }
            }
        }
//        System.out.println(lonely);
//        System.out.println();

        Map<String, Integer> reducedCounter = new HashMap<>(counter);
        for (String cap : counter.keySet()) {
            if (lonely.contains(cap) && counter.get(cap) < 10) { // arbitrary cutoff
                reducedCounter.remove(cap);
            }

        }

        List<Map.Entry<String, Integer>> caps = new ArrayList<>(reducedCounter.entrySet());
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
        System.out.println(reducedCounter.size());
        System.out.println();
        System.out.println("==============================");
        System.out.println();

        Set<String> nondescriptors = new HashSet<>();
        nondescriptors.addAll(places);
        nondescriptors.addAll(surnames);
        AliasGroups aliasGroups = getAliases(reducedCounter, nondescriptors);
        List<Set<String>> groups = new ArrayList<>(aliasGroups.getGroups());
        groups.sort(new Comparator<Set<String>>() {
            @Override
            public int compare(Set<String> o1, Set<String> o2) {
                return o2.size() - o1.size();
            }
        });
        for (Set<String> group : groups) {
            System.out.println(group);
        }
        System.out.println();
        System.out.println(groups.size());
        System.out.println();
    }

}
