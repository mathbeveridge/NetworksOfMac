package edu.macalester.mscs.characters;

import edu.macalester.mscs.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * TODO: make more modular
 * @author Ari Weiland
 */
public class CharacterFinder {

    // Words that should be completely ignored
    public static final Set<String> IGNORE_WORDS = new HashSet<>(Arrays.asList(
            "My", "He", "His", "We", "Their", "Your", // pronouns  (It???)
            "This", "That", "There", // indirect pronouns
            "Who", "Why", // questions
            "Man", "Men", "With", "If", "And", "Will", "Half", "Free", "Watch",
            "Wolf", "Hall", "Kingdoms", "Watchmen", "Shepherd", // miscellaneous
            "House", "Houses", "Clan", "Lords", "Ladies", "Kings", "Dothraki", // GoT specific
            "Father", "Mother", "Uncle", "Aunt", "Brother", "Brothers", "Sons" // familial references
    ));

    // Words that are not unique, but may still be descriptive, expecially in combination
    public static final Set<String> GENERAL_WORDS = new HashSet<>(Arrays.asList(
            "The", // titular articles
            "Lord", "Lady", "King", "Queen", "Regent", "Steward", "Prince", "Princess", // royal titles
            "Ser", "Maester", "Captain", "Commander", "Magister", "Master", "Builder",
            "Septon", // professional titles
            "Young", "Old", "Fat", // endearing titles
            "Khal", "Ko", // dothraki titles
            "High", "Great", "Grand", "First", "Second", // superlatives
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", // numbers
            "Black", "Red", "Green", "Blue", // colors
            "Land", "Lands", "Sea", "Seas", "Island", "Isles", "City", "Cities", // geographics
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

    private static void addToCounter(Map<String, Integer> counter, String string, int increment) {
        if (counter.containsKey(string)) {
            counter.put(string, counter.get(string) + increment);
        } else {
            counter.put(string, increment);
        }
    }

    private static boolean isCapitalized(String word) {
        return word.matches("[A-Z][a-z]+");
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
                    if (isCapitalized(part) && !IGNORE_WORDS.contains(part)) {
                        if (!GENERAL_WORDS.contains(part)) {
                            addToCounter(counter, part, 0);
                        }
                        if (phrase == null) {
                            phrase = new StringBuilder(part);
                        } else {
                            phrase.append(part);
                        }
                        toAdd = phrase.toString();
                    } else {
                        if (phrase != null) {
                            if (part.equals(" ") || part.equals("of") || part.equals("the")) {
                                phrase.append(part);
                            } else {
                                if (!GENERAL_WORDS.contains(toAdd)) {
                                    addToCounter(counter, toAdd, 1);
                                }
                                phrase = null;
                            }
                        }
                    }
                }
            }
            // go back and get sentence starters that we've already seen
            phrase = null;
            for (int i = 0; i < parts.size(); i++) {
                String part = parts.get(i);
                if (phrase != null || counter.containsKey(part) && (i == 0 || precedesSentenceStart(parts.get(i - 1)))) {
                    if (part.length() > 1 && isCapitalized(part) && !IGNORE_WORDS.contains(part)) {
                        if (!GENERAL_WORDS.contains(part)) {
                            addToCounter(counter, part, 0);
                        }
                        if (phrase == null) {
                            phrase = new StringBuilder(part);
                        } else {
                            phrase.append(part);
                        }
                        toAdd = phrase.toString();
                    } else {
                        if (phrase != null) {
                            if (part.equals(" ") || part.equals("of") || part.equals("the")) {
                                phrase.append(part);
                            } else {
                                if (!GENERAL_WORDS.contains(toAdd)) {
                                    addToCounter(counter, toAdd, 1);
                                }
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
        if (titles.contains(split[0]) && isCapitalized(split[1])) {
            return cap.substring(cap.indexOf(" ") + 1);
        } else {
            return cap;
        }
    }

    /**
     * Returns a set of names that occur in a word triplet preceded by an ignored word
     * @param words
     * @param ignored
     * @return
     */
    private static Set<String> getTitledNames(Collection<String> words, Collection<String> ignored) {
        Set<String> names = new HashSet<>();
        Set<String> partNames = new HashSet<>();
        for (String cap : words) {
            String[] split = cap.split(" ");
            String name = cap.substring(cap.indexOf(" ") + 1);
            if (split.length > 1 && ignored.contains(split[0]) && isCapitalized(split[1]) && !ignored.contains(split[1])) {
                if (split.length == 2 && (split[0].equals("Ko") || split[0].equals("Khal")) || split.length == 3) {
                    names.add(name);
                } else {
                    partNames.add(split[1]);
                }
            }
        }
        for (String name : partNames) {
            boolean isUnique = true;
            for (String cap : words) {
                String[] split = cap.split(" ");
                if (split.length == 2) {
                    // it's still unique if this is Title + Name
                    isUnique = isUnique && !name.equals(split[0]) && (ignored.contains(split[0]) || !name.equals(split[1]));
                    if (!ignored.contains(split[0]) && !ignored.contains(split[1])
                            && (name.equals(split[0]) || name.equals(split[1]))) {
                        names.add(cap);
                    }
                } else if (split.length > 2) {
                    for (String s : split) {
                        isUnique = isUnique && !name.equals(s) && !name.equals(s + "s");
                    }
                }
            }
            if (isUnique) {
                names.add(name);
            }
        }
        return names;
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

    private static Map<String, Integer> removePlaces(Map<String, Integer> counter) {
        Map<String, Integer> reducedCounter = new HashMap<>();
        for (String cap : counter.keySet()) {
            String noPlace = cap;
            // strip "of..." bits
            if (noPlace.contains(" of ")) {
                String temp = noPlace.substring(0, noPlace.indexOf(" of "));
                if (!GENERAL_WORDS.contains(temp)) {
                    noPlace = temp;
                }
            }
            if (!reducedCounter.containsKey(noPlace)) {
                reducedCounter.put(noPlace, 0);
            }
            reducedCounter.put(noPlace, reducedCounter.get(noPlace) + counter.get(cap));
        }
        return reducedCounter;
    }

    private static Map<String, Integer> removeTitles(Map<String, Integer> counter) {
        Map<String, Integer> reducedCounter = new HashMap<>();
        for (String cap : counter.keySet()) {
            String noTitle = cap;
            for (int i = StringUtils.countMatches(noTitle, ' '); i > 1; i--) {
                noTitle = stripTitle(noTitle, GENERAL_WORDS);
            }
            if (!reducedCounter.containsKey(noTitle)) {
                reducedCounter.put(noTitle, 0);
            }
            reducedCounter.put(noTitle, reducedCounter.get(noTitle) + counter.get(cap));
        }
        return reducedCounter;
    }

    private static Map<String, Integer> removeLonely(Map<String, Integer> counter, Collection<String> lonely, int threshold) {
        Map<String, Integer> reducedCounter = new HashMap<>();
        for (String cap : counter.keySet()) {
            if (!lonely.contains(cap) || counter.get(cap) > threshold) {
                reducedCounter.put(cap, counter.get(cap));
            }
        }
        return reducedCounter;
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

        counter = removePlaces(counter);
        counter = removeTitles(counter);
        counter = removeLonely(counter, lonely, 10);

//        List<Map.Entry<String, Integer>> caps = new ArrayList<>(counter.entrySet());
//        caps.sort(ENTRY_COMPARATOR);
//        for (Map.Entry<String, Integer> cap : caps) {
//            System.out.println(cap.getKey() + "\t" + cap.getValue());
//        }
//        System.out.println();
//        System.out.println(counter.size());
//        System.out.println();
//        System.out.println("==============================");
//        System.out.println();

        Set<String> nondescriptors = new HashSet<>();
        nondescriptors.addAll(pluralizedNames);
        nondescriptors.addAll(getPlurals(pluralizedNames));
        nondescriptors.addAll(surnames);
        nondescriptors.addAll(getPlurals(surnames));
        nondescriptors.addAll(places);
        CharacterGroups characterGroups = new CharacterGroups(counter, nondescriptors);

        names.add("Varys");
        names.add("Bronn");
        names.add("Septa Mordane");
        names.add("Pycelle");
        names.add("Mirri Maz Duur");
        names.add("Hodor");
        names.add("Pyp");
        names.add("Syrio Forel");
        names.add("Grenn");
        names.add("Irri");
        names.add("Jhiqui");
        names.add("Qotho");
        names.add("Osha");
        names.add("Shagga");
        names.add("Doreah");
        names.add("Yoren");
        names.add("Halder");
        names.add("Lyanna");
        names.add("Jhogo");
        names.add("Mord");
        names.add("Gared");
        names.add("Marillion");
        names.add("Aggo");
        names.add("Alyn");
        names.add("Haggo");
        names.add("Chett");
        names.add("Cohollo");
        names.add("Timett");
        names.add("Mycah");
        names.add("Conn");
        names.add("Shae");
        names.add("Rakharo");
        names.add("Chiggen");
        names.add("Desmond");
        names.add("Jyck");
        names.add("Donal Noye");
        names.add("Masha Heddle");
        names.add("Quaro");
        names.add("Hullen");
        names.add("Harwin");
        names.add("Moreo Tumitis");
        names.add("Dareon");
        names.add("Rhaego");
        names.add("Cayn");
        names.add("High Septon");
        names.add("Morrec");
        names.add("Maegor");
        names.add("Mance Rayder");
        names.add("Hobb");
        names.add("Malleon");
        names.add("Helman Tallhart");
        names.add("Jafer Flowers");
        names.add("Cotter Pyke");
        names.add("Hugh");
        names.add("Lothor Brune");
        names.add("Howland Reed");
        names.add("Bryce Caron");
        names.add("Mychel Redfort");
        names.add("Florian");
        names.add("Quorin Halfhand");
        names.add("Endrew Tarth");
        names.add("Jaehaerys");
        names.add("Jalabhar Xho");
        names.add("Podrick Payne");

        characterGroups.combineGroups("Eddard", "Ned");
        characterGroups.combineGroups("Bran", "Brandon Stark");
        characterGroups.combineGroups("Robert", "Usurper");
        characterGroups.combineGroups("Petyr", "Littlefinger");
        characterGroups.combineGroups("Petyr", "Lord Baelish");
        characterGroups.combineGroups("Daenerys", "Dany");
        characterGroups.combineGroups("Daenerys", "Khaleesi");
        characterGroups.combineGroups("Daenerys", "Princess of Dragonstone");
        characterGroups.combineGroups("Joffrey", "Joff");
        characterGroups.combineGroups("Samwell", "Sam");
        characterGroups.combineGroups("Samwell", "Piggy");
        characterGroups.combineGroups("Samwell", "Lord of Ham");
        characterGroups.combineGroups("Sandor", "Hound");
        characterGroups.combineGroups("Benjen", "Ben");
        characterGroups.combineGroups("Jeor", "Old Bear");
        characterGroups.combineGroups("Jeor", "Commander Mormont");
        characterGroups.combineGroups("Jeor", "Lord Mormont");
        characterGroups.combineGroups("Tomard", "Tom");
        characterGroups.combineGroups("Jon Arryn", "Lord Arryn");
        characterGroups.combineGroups("Catelyn", "Lady Stark");
        characterGroups.combineGroups("Pycelle", "Grand Maester");
        characterGroups.combineGroups("Walder", "Lord Frey");
        characterGroups.combineGroups("Walder", "Lord of the Crossing");
        characterGroups.combineGroups("Robert Arryn", "Lord of the Eyrie");
        characterGroups.combineGroups("Lysa", "Lady Arryn");
        characterGroups.combineGroups("Maege", "Lady Mormont");
        characterGroups.combineGroups("Greatjon", "Lord Umber");
        characterGroups.combineGroups("Shella", "Lady Whent");
        characterGroups.combineGroups("Drogo", "Great Rider");
        characterGroups.combineGroups("Renly", "Lord of Storm");
        characterGroups.combineGroups("Benjen", "First Ranger");
        characterGroups.combineGroups("Marq", "Lord Piper");
        characterGroups.combineGroups("Tywin", "Lord of Casterly Rock");
        characterGroups.combineGroups("Horas", "Horror");
        characterGroups.combineGroups("Jonos", "Lord Bracken");
        characterGroups.combineGroups("Tytos Blackwood", "Lord Blackwood");
        characterGroups.combineGroups("Hobber", "Slobber");
        characterGroups.combineGroups("Karyl", "Lord Vance");
        characterGroups.combineGroups("Roose", "Lord of the Dreadfort");
        characterGroups.combineGroups("Roose", "Lord Bolton");
        characterGroups.combineGroups("Hoster", "Lord of Riverrun");
        characterGroups.combineGroups("Loras", "Daisy");

        names.remove("Yard");           // mistake
        names.remove("Valyrian");       // mistake
        names.remove("Ned Stark");      // as Eddard Stark
        names.remove("Jon Stark");      // as Jon Snow
        names.remove("Daenerys Stormborn"); // as Daenerys Targaryen
        names.remove("Catelyn Tully");  // as Catelyn Stark
        names.remove("Joff");           // as Joffrey Baratheon
        names.remove("Rider");          // as Drogo
        names.remove("Sam Tarly");      // as Samwell Tarly
        names.remove("Piggy");          // as Samwell Tarly
        names.remove("Rodrik Stark");   // unused
        names.remove("Ben Stark");      // as Benjen Stark
        names.remove("Ranger");         // as Benjen Stark
        names.remove("Theon Stark");    // as Theon Greyjoy
        names.remove("Brynden Blackfish");  // as Brynden Tully
        names.remove("Daisy");          // as Loras Tyrell
        names.remove("Aegon Targaryen");// unused, too problematic
        names.remove("Torrhen Stark");  // unused
        names.remove("Horror");         // as Horas
        names.remove("Slobber");        // as Hobber

        Set<String> firstNames = getFirstNames(names, GENERAL_WORDS);

        FileUtils.writeFile(getFullNameList(characterGroups), "src/main/resources/data/characters/ari-list-full.txt");
        FileUtils.writeFile(getCleanNameList(characterGroups, names), "src/main/resources/data/characters/ari-list-clean.txt");
        FileUtils.writeFile(getCleanNameList(characterGroups, firstNames), "src/main/resources/data/characters/ari-list-no-dup.txt");
        FileUtils.writeFile(getFirstNameList(characterGroups, names, GENERAL_WORDS), "src/main/resources/data/characters/ari-list-first.txt");

    }

    private static final Comparator<Map.Entry<?, Integer>> ENTRY_COMPARATOR = new Comparator<Map.Entry<?, Integer>>() {
        @Override
        public int compare(Map.Entry<?, Integer> o1, Map.Entry<?, Integer> o2) {
            return o2.getValue() - o1.getValue();
        }
    };

}
