package edu.macalester.mscs.characters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Ari Weiland
 */
public class CharacterFinder {

    // Words that should be completely ignored
    public static final Set<String> IGNORE_WORDS = new HashSet<>(Arrays.asList(
            "My", "You", "He", "His", "She", "It", "We", "They", "Their", "Our", // pronouns  (It???)
            "This", "That", "These", "Those", "There", // indirect pronouns
            "Who", "Where", "Why", "How", // questions
            "An", "Nor", "Do", "No", "Yes", "Afterward", "Ask", "While", "Men", "Above", "Done", "Does",
            "Certainly", "To", "Without", "Unless", "Some", "Sometimes", "On", "Both", "In", "From",
            "Never", // miscellaneous
            "Ser", "House", "Houses", "Lords", "Ladies", "Kings", // GoT specific
            "Father", "Mother", "Uncle", "Aunt" // familial references
    ));

    // Words that are not unique, but may still be descriptive, expecially in combination
    public static final Set<String> GENERAL_WORDS = new HashSet<>(Arrays.asList(
            "Lord", "Lady", "King", "Queen", "Regent", "Steward", "Prince", "Princess", // royal titles
            "Maester", "Captain", "Commander", // professional titles
            "Young", "Old", // casual titles
            "Khal", "Ko", // dothraki titles
            "High", "Great", "Grand", "First", // superlatives
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", // numbers
            "Black", "Red", "Green", "Blue", // colors
            "Land", "Lands", "Sea", "Seas", "City", "Cities", // geographics
            "Alley", "Gate", "Keep", "Market", "Tower" // landmarks
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
                        if (!GENERAL_WORDS.contains(part)) {
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
                if (!GENERAL_WORDS.contains(s) && counter.containsKey(s) && counter.containsKey(s + "s")) {
                    surnames.add(s);
                }
            }
        }

        // Pairs whose second word is from the surnames group
        Set<String> names = new HashSet<>();
        Set<String> firstNames = new HashSet<>();
        for (String cap : counter.keySet()) {
            String[] split = cap.split(" ");
            if (split.length == 2 && !GENERAL_WORDS.contains(split[0]) && surnames.contains(split[1])) {
                names.add(cap);
                firstNames.add(split[0]);
            }
        }

        // words that appear in front of different "names" *** Mya ***
        Set<String> bad = new HashSet<>();
        for (String cap : counter.keySet()) {
            String[] split = cap.split(" ");
            if (split.length > 1 && !GENERAL_WORDS.contains(split[0]) && firstNames.contains(split[1])) {
                bad.add(split[0]);
            }
        }
        for (String cap : counter.keySet()) {
            if (bad.contains(cap.split(" ")[0])) {
                names.remove(cap);
            }
        }
        firstNames.removeAll(bad);

        Map<String, Integer> temp = new HashMap<>(counter);
        for (String cap : counter.keySet()) {
            if (bad.contains(cap.split(" ")[0])) {
                temp.remove(cap);
            }
        }
        counter = temp;

        // words that appear as second words in multiple pairs
        Set<String> surnames2 = new HashSet<>();
        Set<String> once = new HashSet<>();
        for (String cap : counter.keySet()) {
            String[] split = cap.split(" ");
            if (split.length == 2 && !GENERAL_WORDS.contains(split[0]) && !GENERAL_WORDS.contains(split[1])) {
                if (once.contains(split[1])) {
                    surnames2.add(split[1]);
                } else {
                    once.add(split[1]);
                }
            }
        }
        // intersection of surnames and surnames2
        Set<String> surnames3 = new HashSet<>(surnames2);
        surnames3.retainAll(surnames);

        // Pairs whose second word is from the surnames2 group
        Set<String> names2 = new HashSet<>();
        Set<String> firstNames2 = new HashSet<>();
        for (String cap : counter.keySet()) {
            String[] split = cap.split(" ");
            if (split.length == 2 && !GENERAL_WORDS.contains(split[0]) && surnames2.contains(split[1])) {
                names2.add(cap);
                firstNames2.add(split[0]);
            }
        }

        // words that appear in front of different "names" *** Mya ***
        Set<String> bad2 = new HashSet<>();
        for (String cap : counter.keySet()) {
            String[] split = cap.split(" ");
            if (split.length > 1 && !GENERAL_WORDS.contains(split[0]) && firstNames2.contains(split[1])) {
                bad2.add(split[0]);
            }
        }
        for (String cap : counter.keySet()) {
            if (bad2.contains(cap.split(" ")[0])) {
                names2.remove(cap);
            }
        }
        firstNames2.removeAll(bad2);

        temp = new HashMap<>(counter);
        for (String cap : counter.keySet()) {
            if (bad2.contains(cap.split(" ")[0])) {
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

//        System.out.println(bad);
//        System.out.println(bad2);
//        System.out.println(names);
        System.out.println(names2);
//        System.out.println(firstNames);
//        System.out.println(firstNames2);
//        System.out.println(surnames);
//        System.out.println(surnames2);
//        System.out.println(surnames3);
//        System.out.println(places);
//        System.out.println(lonely);
        System.out.println();

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
//        for (Map.Entry<String, Integer> cap : caps) {
//            System.out.println(cap.getKey() + "\t" + cap.getValue());
//        }
//        System.out.println();
//        System.out.println(reducedCounter.size());
//        System.out.println();
//        System.out.println("==============================");
//        System.out.println();

        Set<String> nondescriptors = new HashSet<>();
        nondescriptors.addAll(surnames);
        nondescriptors.addAll(surnames2);
        nondescriptors.addAll(places);
        CharacterGroups characterGroups = new CharacterGroups(reducedCounter, nondescriptors);

        Map<List<String>, Integer> groupMap = new HashMap<>();
        for (String s : names2) {
            List<String> list = new ArrayList<>();
            list.add(s);
            list.addAll(characterGroups.getGroup(s));
            groupMap.put(list, characterGroups.getAliasCount(s));
        }

        List<Map.Entry<List<String>, Integer>> groups = new ArrayList<>(groupMap.entrySet());
        groups.sort(new Comparator<Map.Entry<List<String>, Integer>>() {
            @Override
            public int compare(Map.Entry<List<String>, Integer> o1, Map.Entry<List<String>, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });
        for (Map.Entry<List<String>, Integer> group : groups) {
            System.out.println(group.getValue() + "\t" + group.getKey());
        }
        System.out.println();
        System.out.println(groups.size());
        System.out.println();
    }

}
