package edu.macalester.mscs.characters;

import java.io.*;
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
            "An", "Nor", "Do", "No", "Yes", "Afterward", "Ask", "While", "Man", "Men", "Above", "Done",
            "Does", "Certainly", "To", "Without", "Unless", "Some", "Sometimes", "On", "Both", "In",
            "From", "Never", "Most", "Nervously", "Inside", "Of", // miscellaneous
            "House", "Houses", "Clan", "Lords", "Ladies", "Kings", // GoT specific
            "Father", "Mother" // familial references
    ));

    // Words that are not unique, but may still be descriptive, expecially in combination
    public static final Set<String> GENERAL_WORDS = new HashSet<>(Arrays.asList(
            "Lord", "Lady", "King", "Queen", "Regent", "Steward", "Prince", "Princess", // royal titles
            "Ser", "Maester", "Captain", "Commander", "Magister", "Master", // professional titles
            "Young", "Old", // endearing titles
            "Khal", "Ko", // dothraki titles
            "High", "Great", "Grand", "First", "Second", // superlatives
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

    private static String stripTitle(String cap, Collection<String> titles) {
        String[] split = cap.split(" ");
        if (split.length == 3 && titles.contains(split[0]) && isCapitalized(split[1])) {
            return cap.substring(cap.indexOf(" ") + 1);
        } else {
            return cap;
        }
    }

    private static Map<String, Integer> countCapitalized(String file) {
        Map<String, Integer> counter = new HashMap<>();

        String line = null;
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(file));
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
        }
        return counter;
    }

    /**
     * Returns words that are exist in words as both "xxxx" and "xxxxs" and at least once do not follow "the"
     * Ignores words found in ignored
     * @param words
     * @param ignored
     * @return
     */
    private static Set<String> getPluralizedNames(Collection<String> words, Collection<String> ignored) {
        Set<String> pluralized = new HashSet<>();
        for (String cap : words) {
            String[] split = cap.split(" ");
            for (int i = 1; i < split.length; i++) {
                String s = split[i];
                if (!ignored.contains(s) && !split[i-1].equalsIgnoreCase("the") && words.contains(s) && words.contains(s + "s")) {
                    pluralized.add(s);
                }
            }
        }
        return pluralized;
    }

    /**
     * Returns words that come second in multiple word pairs
     * @param words
     * @param ignored
     * @return
     */
    private static Set<String> getSurnames(Collection<String> words, Collection<String> ignored) {
        Set<String> surnames = new HashSet<>();
        Set<String> once = new HashSet<>();
        for (String cap : words) {
            String name = stripTitle(cap, ignored);
            String[] split = name.split(" ");
            if (split.length == 2 && !ignored.contains(split[0]) && !ignored.contains(split[1])) {
                if (once.contains(split[1])) {
                    surnames.add(split[1]);
                } else {
                    once.add(split[1]);
                }
            }
        }
        return surnames;
    }

    /**
     * Returns word pairs from words whose second word is in surnames
     * Ignores pairs with the first word in ignored
     * @param words
     * @param surnames
     * @param ignored
     * @return
     */
    private static Set<String> getNames(Collection<String> words, Collection<String> surnames, Collection<String> ignored) {
        Set<String> names = new HashSet<>();
        for (String cap : words) {
            String name = stripTitle(cap, ignored);
            String[] split = name.split(" ");
            if (split.length == 2 && !ignored.contains(split[0]) && surnames.contains(split[1])) {
                if (words.contains(name)) {
                    names.add(name);
                } else {
                    names.add(cap);
                }
            }
        }
        return names;
    }

    /**
     * Returns a set of names that occur in a word triplet preceded by an ignored word
     * @param words
     * @param ignored
     * @return
     */
    private static Set<String> getTitledNames(Collection<String> words, Collection<String> ignored) {
        Set<String> names = new HashSet<>();
        for (String cap : words) {
            String[] split = cap.split(" ");
            String name = cap.substring(cap.indexOf(" ") + 1);
            if (split.length == 3 && isCapitalized(split[1]) && !ignored.contains(split[1])
                    && ignored.contains(split[0])
                    || split.length == 2 && (split[0].equals("Ko") || split[0].equals("Khal"))) {
                if (words.contains(name)) {
                    names.add(name);
                } else {
                    names.add(cap);
                }
            }
        }
        return names;
    }

    /**
     * Returns a set of the first words in names, a collection of word pairings
     * @param names
     * @return
     */
    private static Set<String> getFirstNames(Collection<String> names) {
        Set<String> firstNames = new HashSet<>();
        for (String cap : names) {
            String[] split = cap.split(" ");
            firstNames.add(split[0]);
        }
        return firstNames;
    }

    /**
     * Returns a set of phrases that follow "of" or "of the"
     * @param words
     * @return
     */
    private static Set<String> getPlaces(Collection<String> words) {
        Set<String> places = new HashSet<>();
        for (String cap : words) {
            if (cap.contains(" of ")) {
                String place = cap.substring(cap.indexOf(" of") + 4);
                if (place.startsWith("the")) {
                    place = place.substring(4);
                }
                places.add(place);
            }
        }
        return places;
    }

    /**
     * Returns a set of all words that are not compound or part of compound phrases
     * @param words
     * @return
     */
    private static Set<String> getLonelyWords(Collection<String> words) {
        Set<String> lonely = new HashSet<>(words);
        for (String cap : words) {
            if (cap.contains(" ")) {
                lonely.remove(cap);
                for (String s : cap.split(" ")) {
                    lonely.remove(s);
                }
            }
        }
        return lonely;
    }

    /**
     * Returns a set of bad words by finding words that precede names
     * Ignores words that are in ignored
     * @param words
     * @param firstNames
     * @param ignored
     * @return
     */
    private static Set<String> getBadWords(Collection<String> words, Collection<String> firstNames, Collection<String> ignored) {
        Set<String> bad = new HashSet<>();
        for (String cap : words) {
            String[] split = cap.split(" ");
            if (split.length > 1 && !ignored.contains(split[0]) && firstNames.contains(split[1])) {
                bad.add(split[0]);
            }
        }
        return bad;
    }

    /**
     * Removes bad words from the beginnings of any phrases in the counter and returns the leftovers
     * If the resulting phrase is preceded by any combination of "of" and "the", it strips those off
     * If the end result is an ignored word, it is not returned to the counter
     * @param counter
     * @param badWords
     */
    private static void cleanCounter(Map<String, Integer> counter, Set<String> badWords, Collection<String> ignored) {
        Map<String, Integer> temp = new HashMap<>(counter);
        for (String cap : temp.keySet()) {
            String[] split = cap.split(" ");
            if (badWords.contains(split[0])) {
                int amount = counter.remove(cap);
                if (split.length > 2) {
                    cap = cap.substring(split[0].length() + 1);
                    if (cap.startsWith("of the")) {
                        cap = cap.substring(6);
                    } else if (cap.startsWith("the")) {
                        cap = cap.substring(4);
                    } else if (cap.startsWith("of")) {
                        cap = cap.substring(3);
                    }
                    if (!ignored.contains(cap)) {
                        if (!counter.containsKey(cap)) {
                            counter.put(cap, 0);
                        }
                        counter.put(cap, counter.get(cap) + amount);
                    }
                }
            }
        }
    }

    /**
     * Returns a new set that is the intersection of two sets, via the retainAll method
     * @param set1
     * @param set2
     * @param <T>
     * @return
     */
    private static <T> Set<T> intersectSets(Set<T> set1, Set<T> set2) {
        Set<T> intersection = new HashSet<>(set2);
        intersection.retainAll(set1);
        return intersection;
    }

    private static void writeFile(CharacterGroups characterGroups, Collection<String> names, String file) {
        Map<List<String>, Integer> groupMap = new HashMap<>();
        for (String s : names) {
            List<String> list = new ArrayList<>();
            list.add(s);
//            if (!characterGroups.isAlias(s)) {
//                System.out.println(s);
//            }
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
        for (int i = 0; i < 100; i++) {
            Map.Entry<List<String>, Integer> group = groups.get(i);
            System.out.println(group.getValue() + "\t" + group.getKey());
        }
        System.out.println();
        System.out.println(groups.size());
        System.out.println();

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<List<String>, Integer> group : groups) {
                boolean first = true;
                for (String name : group.getKey()) {
                    if (first) {
                        first = false;
                    } else {
                        writer.write(", ");
                    }
                    writer.write(name);
                }
                writer.write('\n');
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ignored) {}
            }
        }
    }

    public static void main(String[] args) {
        Map<String, Integer> counter = countCapitalized("src/main/resources/text/got.txt");

        Set<String> titledNames = getTitledNames(counter.keySet(), GENERAL_WORDS);
        Set<String> surnames = getPluralizedNames(counter.keySet(), GENERAL_WORDS);
        Set<String> names = getNames(counter.keySet(), surnames, GENERAL_WORDS);
        names.addAll(titledNames);
        Set<String> firstNames = getFirstNames(names);
        Set<String> bad = getBadWords(counter.keySet(), firstNames, GENERAL_WORDS); // Mya???
        cleanCounter(counter, bad, GENERAL_WORDS);

        Set<String> surnames2 = getSurnames(counter.keySet(), GENERAL_WORDS);
        Set<String> names2 = getNames(counter.keySet(), surnames2, GENERAL_WORDS);
        names2.addAll(titledNames);
        Set<String> firstNames2 = getFirstNames(names2);
        Set<String> bad2 = getBadWords(counter.keySet(), firstNames2, GENERAL_WORDS);
        cleanCounter(counter, bad2, GENERAL_WORDS);

        Set<String> names3 = getNames(counter.keySet(), surnames2, GENERAL_WORDS);
        names3.addAll(titledNames);
        Set<String> firstNames3 = getFirstNames(names2);

        Set<String> places = getPlaces(counter.keySet());
        Set<String> lonely = getLonelyWords(counter.keySet());

//        System.out.println(titledNames);
//        System.out.println(bad);
//        System.out.println(bad2);
//        System.out.println(names);
//        System.out.println(names2);
//        System.out.println(names3);
//        System.out.println(firstNames);
//        System.out.println(firstNames2);
//        System.out.println(firstNames3);
//        System.out.println(surnames);
//        System.out.println(surnames2);
//        System.out.println(places);
//        System.out.println(lonely);
//        System.out.println();

        Map<String, Integer> reducedCounter = new HashMap<>(counter);
        for (String cap : counter.keySet()) {

        }

//        List<Map.Entry<String, Integer>> caps = new ArrayList<>(reducedCounter.entrySet());
//        Collections.sort(caps, new Comparator<Map.Entry<String, Integer>>() {
//            @Override
//            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
//                return o2.getValue() - o1.getValue();
//            }
//        });
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

        names3.add("Mirri Maz Duur");
        names3.add("Varys");
        names3.add("Pycelle");
        names3.add("Bowen Marsh");
        names3.add("Illyrio Mopatis");
        names3.add("Syrio Forel");
        names3.add("Doreah");
        names3.add("Nan");
        names3.add("Hodor");
        names3.add("Janos Slynt");
        names3.add("Myrcella");
        names3.add("Roose Bolton");
        names3.add("Tommen");
        names3.add("Moreo");
        names3.add("Othor");
        names3.add("Osha");
        names3.add("Shae");
        names3.add("Bronn");
        names3.add("Cayn");
        names3.add("Yoren");
        names3.add("Rast");
        names3.add("Halder");
        names3.add("Chett");
        names3.add("Cohollo");
        names3.add("Dareon");
        names3.add("Quaro");
        names3.add("Lyanna");
        names3.add("Hullen");
        names3.add("Irri");
        names3.add("Desmond");
        names3.add("Qotho");
        names3.add("Mycah");
        names3.add("Morrec");
        names3.add("Jyck");
        names3.add("Chella");
        names3.add("Harwin");
        names3.add("Rhaego");
        names3.add("Marillion");
        names3.add("Jhogo");
        names3.add("Stiv");
        names3.add("Pyp");
        names3.add("Timett");
        names3.add("Rakharo");
        names3.add("Aggo");
        names3.add("Chiggen");
        names3.add("Grenn");
        names3.add("Haggo");

        characterGroups.combineGroups("Eddard", "Ned");
        characterGroups.combineGroups("Bran", "Brandon Stark");
        characterGroups.combineGroups("Robert", "Usurper");
        characterGroups.combineGroups("Petyr", "Littlefinger");
        characterGroups.combineGroups("Daenerys", "Dany");
        characterGroups.combineGroups("Daenerys", "Khaleesi");
        characterGroups.combineGroups("Joffrey", "Joff");
        characterGroups.combineGroups("Samwell", "Sam");
        characterGroups.combineGroups("Samwell", "Piggy");
        characterGroups.combineGroups("Sandor", "Hound");
        characterGroups.combineGroups("Sandor", "Dog");
        characterGroups.combineGroups("Benjen", "Ben");
        characterGroups.combineGroups("Jeor", "Old Bear");
        characterGroups.combineGroups("Jeor", "Lord Commander Mormont");
        characterGroups.combineGroups("Jeor", "Lord Mormont");

        names3.remove("Free Cities");   // accidentally picked up
        names3.remove("Ned Stark");     // as Eddard Stark
        names3.remove("Catelyn Tully"); // as Catelyn Stark
        names3.remove("Sam Tarly");     // as Samwell Tarly
        names3.remove("Ben Stark");     // as Benjen Stark
        names3.remove("Theon Stark");   // as Theon Greyjoy

        // this would add in "Lord Surname" and "Lady Surname", but it breaks for Lord Stark
//        for (String cap : reducedCounter.keySet()) {
//            String[] split = cap.split(" ");
//            if (split.length == 3) {
//                String name = split[1] + " " + split[2];
//                String title = split[0] + " " + split[2];
//                if (GENERAL_WORDS.contains(split[0]) && names2.contains(name) && reducedCounter.containsKey(title)) {
//                    characterGroups.combineGroups(split[1], title);
//                }
//            }
//        }

        writeFile(characterGroups, names3, "src/main/resources/data/characters/ari-list-v3-clean.txt");

    }

}
