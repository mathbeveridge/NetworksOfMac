package edu.macalester.mscs.characters;

import edu.macalester.mscs.utils.FileUtils;

import java.util.*;

/**
 * @author Ari Weiland
 */
public class CharacterFinder {

    // Words that should be completely ignored
    public static final Set<String> IGNORE_WORDS = new HashSet<>(Arrays.asList(
            "My", "He", "His", "We", "Their", "Your", // pronouns  (It???)
            "This", "That", "There", // indirect pronouns
            "Who", "Why", // questions
            "Man", "Men", "With", "If", "And", "Will", "Half", // miscellaneous
            "House", "Houses", "Clan", "Lords", "Ladies", "Kings", // GoT specific
            "Father", "Mother", "Uncle", "Aunt", "Brother", "Brothers", "Sons" // familial references
    ));

    // Words that are not unique, but may still be descriptive, expecially in combination
    public static final Set<String> GENERAL_WORDS = new HashSet<>(Arrays.asList(
            "The", // titular articles
            "Lord", "Lady", "King", "Queen", "Regent", "Steward", "Prince", "Princess", // royal titles
            "Ser", "Maester", "Captain", "Commander", "Magister", "Master", "Builder", // professional titles
            "Young", "Old", // endearing titles
            "Khal", "Ko", // dothraki titles
            "High", "Great", "Grand", "First", "Second", // superlatives
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", // numbers
            "Black", "Red", "Green", "Blue", // colors
            "Land", "Lands", "Sea", "Seas", "Island", "City", "Cities", // geographics
            "Alley", "Gate", "Keep", "Market", "Tower" // landmarks
    ));

    public static final String QUOTE = "�";

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

    private static void addToCounter(Map<String, Integer> counter, String string) {
        if (counter.containsKey(string)) {
            counter.put(string, counter.get(string) + 1);
        } else {
            counter.put(string, 1);
        }
    }

    private static boolean isCapitalized(String word) {
        return word.matches("[A-Z][a-z]*");
    }

    private static boolean precedesSentenceStart(String part) {
        return part.contains(".") || part.contains("?") || part.contains("!") || part.contains(QUOTE);
    }

    private static Map<String, Integer> countCapitalized(List<String> lines) {
        Map<String, Integer> counter = new HashMap<>();
        for (String line : lines) {
            List<String> parts = breakLine(line);
            StringBuilder phrase = null;
            String toAdd = null;
            // find capitals that don't start sentences
            for (int i = 0; i < parts.size(); i++) {
                String part = parts.get(i);
                if (i > 0 && !precedesSentenceStart(parts.get(i - 1))) {
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
            // go back and get sentence starters that we've already seen
            phrase = null;
            toAdd = null;
            for (int i = 0; i < parts.size(); i++) {
                String part = parts.get(i);
                if (phrase != null || counter.containsKey(part) && (i == 0 || precedesSentenceStart(parts.get(i - 1)))) {
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
        }
        return counter;
    }

    private static String stripTitle(String cap, Collection<String> titles) {
        String[] split = cap.split(" ");
//        if (split.length == 3 && titles.contains(split[0]) && isCapitalized(split[1])) {
        if (titles.contains(split[0]) && isCapitalized(split[1])) {
            return cap.substring(cap.indexOf(" ") + 1);
        } else {
            return cap;
        }
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

    private static Set<String> getPlurals(Set<String> words) {
        Set<String> plurals = new HashSet<>();
        for (String w : words) {
            if (!w.endsWith("s")) {
                plurals.add(w + "s");
            }
        }
        return plurals;
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
    private static Set<String> getFirstNames(Collection<String> names, Collection<String> ignored) {
        Set<String> firstNames = new HashSet<>();
        for (String cap : names) {
            String[] split = stripTitle(cap, ignored).split(" ");
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

    private static List<String> getFullNameList(CharacterGroups characterGroups) {
        Map<Set<String>, Integer> groupMap = characterGroups.getGroups();
        List<Map.Entry<Set<String>, Integer>> groups = new ArrayList<>(groupMap.entrySet());
        groups.sort(ENTRY_COMPARATOR);
        List<String> lines = new ArrayList<>();
        for (Map.Entry<Set<String>, Integer> group : groups) {
            System.out.println(group.getValue() + "\t" + group.getKey());
            StringBuilder line = new StringBuilder();
            boolean first = true;
            for (String name : group.getKey()) {
                if (first) {
                    first = false;
                } else {
                    line.append(",");
                }
                line.append(name);
            }
            lines.add(line.toString());
        }
        System.out.println();
        System.out.println(groups.size());
        System.out.println();
        return lines;
    }

    private static List<String> getCleanNameList(CharacterGroups characterGroups, Collection<String> names) {
        Map<List<String>, Integer> groupMap = new HashMap<>();
        for (String s : names) {
            if (characterGroups.isAlias(s)) {
                List<String> list = new ArrayList<>();
                list.add(s);
                list.addAll(characterGroups.getGroup(s));
                groupMap.put(list, characterGroups.getAliasCount(s));
            } else {
                System.out.println(s);
            }
        }

        List<Map.Entry<List<String>, Integer>> groups = new ArrayList<>(groupMap.entrySet());
        groups.sort(ENTRY_COMPARATOR);
        List<String> lines = new ArrayList<>();
        for (Map.Entry<List<String>, Integer> group : groups) {
            System.out.println(group.getValue() + "\t" + group.getKey());
            StringBuilder line = new StringBuilder();
            boolean first = true;
            for (String name : group.getKey()) {
                if (first) {
                    first = false;
                } else {
                    line.append(",");
                }
                line.append(name);
            }
            lines.add(line.toString());
        }
        System.out.println();
        System.out.println(groups.size());
        System.out.println();
        return lines;
    }

    private static List<String> getFirstNameList(CharacterGroups characterGroups, Collection<String> names, Collection<String> ignored) {
        Map<List<String>, Integer> groupMap = new HashMap<>();
        for (String name : names) {
            if (characterGroups.isAlias(name)) {
                List<String> list = new ArrayList<>();
                String[] split = stripTitle(name, ignored).split(" ");
                list.add(split[0]);
                Set<String> set = new HashSet<>();
                for (String s : characterGroups.getGroup(name)) {
                    split = stripTitle(s, ignored).split(" ");
                    if (!ignored.contains(split[0])) {
                        set.add(split[0]);
                    }
                }
                list.addAll(set);
                groupMap.put(list, characterGroups.getAliasCount(name));
            } else {
                System.out.println(name);
            }
        }

        List<Map.Entry<List<String>, Integer>> groups = new ArrayList<>(groupMap.entrySet());
        groups.sort(ENTRY_COMPARATOR);
        List<String> lines = new ArrayList<>();
        for (Map.Entry<List<String>, Integer> group : groups) {
            System.out.println(group.getValue() + "\t" + group.getKey());
            StringBuilder line = new StringBuilder();
            boolean first = true;
            for (String name : group.getKey()) {
                if (first) {
                    first = false;
                } else {
                    line.append(",");
                }
                line.append(name);
            }
            lines.add(line.toString());
        }
        System.out.println();
        System.out.println(groups.size());
        System.out.println();
        return lines;
    }

    public static void main(String[] args) {
        Map<String, Integer> counter = countCapitalized(FileUtils.readFile("src/main/resources/text/got.txt"));
        counter.put("Jeor Mormont", 1); // gets wrecked
        counter.put("Jeor", 1);
        counter.remove("Tully Stark"); // gets awkwardly generated as the only double-surname

        Set<String> titledNames = getTitledNames(counter.keySet(), GENERAL_WORDS);
        Set<String> pluralizedNames = getPluralizedNames(counter.keySet(), GENERAL_WORDS);
        Set<String> surnames = getSurnames(counter.keySet(), GENERAL_WORDS);
        Set<String> names = getNames(counter.keySet(), surnames, GENERAL_WORDS);
        names.addAll(titledNames);
        Set<String> firstNames = getFirstNames(names, GENERAL_WORDS);
        Set<String> places = getPlaces(counter.keySet());
        Set<String> lonely = getLonelyWords(counter.keySet());

//        System.out.println(titledNames);
//        System.out.println(pluralizedNames);
//        System.out.println(surnames);
//        System.out.println(names);
//        System.out.println(firstNames);
//        System.out.println(places);
//        System.out.println(lonely);
//        System.out.println();

        Map<String, Integer> reducedCounter = new HashMap<>(counter);
        for (String cap : counter.keySet()) {

        }

        List<Map.Entry<String, Integer>> caps = new ArrayList<>(reducedCounter.entrySet());
        caps.sort(ENTRY_COMPARATOR);
//        for (Map.Entry<String, Integer> cap : caps) {
//            System.out.println(cap.getKey() + "\t" + cap.getValue());
//        }
//        System.out.println();
//        System.out.println(reducedCounter.size());
//        System.out.println();
//        System.out.println("==============================");
//        System.out.println();

        Set<String> nondescriptors = new HashSet<>();
        nondescriptors.addAll(pluralizedNames);
        nondescriptors.addAll(getPlurals(pluralizedNames));
        nondescriptors.addAll(surnames);
        nondescriptors.addAll(getPlurals(surnames));
        for (String s : surnames) {
            if (!s.endsWith("s")) {
                nondescriptors.add(s + "s");
            }
        }
        nondescriptors.addAll(places);
        CharacterGroups characterGroups = new CharacterGroups(reducedCounter, nondescriptors);

        names.add("Mirri Maz Duur");
        names.add("Varys");
        names.add("Pycelle");
        names.add("Bowen Marsh");
        names.add("Illyrio Mopatis");
        names.add("Syrio Forel");
        names.add("Doreah");
        names.add("Nan");
        names.add("Hodor");
        names.add("Janos Slynt");
        names.add("Myrcella");
        names.add("Roose Bolton");
        names.add("Tommen");
        names.add("Moreo");
        names.add("Othor");
        names.add("Osha");
        names.add("Shae");
        names.add("Bronn");
        names.add("Cayn");
        names.add("Yoren");
        names.add("Rast");
        names.add("Halder");
        names.add("Chett");
        names.add("Cohollo");
        names.add("Dareon");
        names.add("Quaro");
        names.add("Lyanna");
        names.add("Hullen");
        names.add("Irri");
        names.add("Desmond");
        names.add("Qotho");
        names.add("Mycah");
        names.add("Morrec");
        names.add("Jyck");
        names.add("Chella");
        names.add("Harwin");
        names.add("Rhaego");
        names.add("Marillion");
        names.add("Jhogo");
        names.add("Stiv");
        names.add("Pyp");
        names.add("Timett");
        names.add("Rakharo");
        names.add("Aggo");
        names.add("Chiggen");
        names.add("Grenn");
        names.add("Haggo");

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
        characterGroups.combineGroups("Benjen", "Ben");
        characterGroups.combineGroups("Jeor", "Old Bear");
        characterGroups.combineGroups("Jeor", "Lord Commander Mormont");
        characterGroups.combineGroups("Jeor", "Lord Mormont");

        names.remove("Free Cities");   // accidentally picked up
        names.remove("Ned Stark");     // as Eddard Stark
        names.remove("Catelyn Tully"); // as Catelyn Stark
        names.remove("Sam Tarly");     // as Samwell Tarly
        names.remove("Ben Stark");     // as Benjen Stark
        names.remove("Theon Stark");   // as Theon Greyjoy

        FileUtils.writeFile(getFullNameList(characterGroups), "src/main/resources/data/characters/ari-list-full.txt");
        FileUtils.writeFile(getCleanNameList(characterGroups, names), "src/main/resources/data/characters/ari-list-clean.txt");
        FileUtils.writeFile(getFirstNameList(characterGroups, names, GENERAL_WORDS), "src/main/resources/data/characters/ari-list-first.txt");

    }

    private static final Comparator<Map.Entry<?, Integer>> ENTRY_COMPARATOR = new Comparator<Map.Entry<?, Integer>>() {
        @Override
        public int compare(Map.Entry<?, Integer> o1, Map.Entry<?, Integer> o2) {
            return o2.getValue() - o1.getValue();
        }
    };

}
