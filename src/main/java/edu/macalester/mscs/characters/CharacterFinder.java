package edu.macalester.mscs.characters;

import edu.macalester.mscs.utils.EntryComparator;
import edu.macalester.mscs.utils.Logger;
import edu.macalester.mscs.utils.WordUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * CharacterFinder provides lots of different utility for generating a list of characters from a text.
 * The basis of the algorithm is to find capitalized words that don't start sentences.
 *
 * It takes into its constructor a list of capitalized words that should be ignored, a list of titles
 * (ie. President in President Obama), a list of capitalized descriptors that should not be unique
 * (ie. Wise in Merlin the Wise), and a list of uncapitalized filler words (ie. things like the Arabic
 * "ibn", the Dutch "van", etc). It also takes in a string of characters that should define a sentence
 * start. These should normally include a period, question mark, exclamation mark, and whatever form
 * of double quote is used to indicate the beginning of a dialogue sentence.
 *
 * The class has a few underlying data structures: a counter and a bunch of categorized sets of words.
 * The counter is used initially to accumulate capitalized words and phrases, and maintains a count
 * of each of their occurrences. A capitalized phrase is a sequence of capitalized words seperated by
 * only spaces and the words "of" and "the".
 *
 * The counter can be built up manually using incrementWord, or by using the countCapitalized method
 * which takes a list of strings representing lines in a text file and processes them automatically.
 * The two methods can be used in combination for precise control.
 *
 * For further pruning the counter, there are methods removeWords and removeWordsBelowThreshold.
 * These remove words from a specific subset of words, where one requires the word to appear in the
 * text less than a specified threshold of times.
 *
 * The class then provides a sequence of methods for manipulating the internal word sets. Primarily
 * it includes add, remove, and get methods for these sets, plus a processCapitalize method for
 * automatically filling these lists. The add methods must be called before process, and remove
 * methods must be called after. The sets include titled names (names generated by looking at words
 * that come after general words), pluralized words (one indication of surnames or non-name words),
 * surnames (words that appear as the second word in more than one pair of words), and places (words
 * that appear following "of" in a name). There is also a names set that includes title names and
 * names derived by finding word pairs that end in a word from the set of surnames, and a set of
 * nondescriptive words that includes pluralized words, surnames, and places.
 *
 * Finally, the class uses the nondescriptive words and the counter to build CharacterGroups.
 *
 * @author Ari Weiland
 */
public class CharacterFinder {

    private final Set<String> ignoredWords;
    private final Set<String> titleWords;
    private final Set<String> generalWords;
    private final Set<String> fillerWords;
    private final String punctuation;

    private Map<String, Integer> counter = new HashMap<>();
    private Set<String> titledNames = new HashSet<>();
    private Set<String> pluralized = new HashSet<>();
    private Set<String> surnames = new HashSet<>();
    private Set<String> places = new HashSet<>();
    private Set<String> names = new HashSet<>();
    private Set<String> nondescriptors = new HashSet<>();

    public CharacterFinder(Set<String> ignoredWords, Set<String> titleWords, Set<String> generalWords,
                           Set<String> fillerWords, String punctuation) {
        this.ignoredWords = ignoredWords;
        this.titleWords = titleWords;
        this.generalWords = generalWords;
        this.fillerWords = fillerWords;
        this.punctuation = punctuation;
    }

    //==========================================//
    //            General Functions             //
    //==========================================//

    public Set<String> getIgnoredWords() {
        return ignoredWords;
    }

    public Set<String> getTitleWords() {
        return titleWords;
    }

    public Set<String> getGeneralWords() {
        return generalWords;
    }

    public String getPunctuation() {
        return punctuation;
    }

    /**
     * Clears the counter and sets the characterGroups to null
     */
    public void clear() {
        counter.clear();
        clearSets();
    }

    public void clearSets() {
        titledNames.clear();
        pluralized.clear();
        surnames.clear();
        places.clear();
        names.clear();
        nondescriptors.clear();
    }

    //==========================================//
    //            Counter Functions             //
    //==========================================//

    public Map<String, Integer> getCounter() {
        return counter;
    }

    public int getCount(String word) {
        if (counter.containsKey(word)) {
            return counter.get(word);
        } else {
            return 0;
        }
    }

    /**
     * Builds up the counter from lines of a text
     *
     * @param lines
     */
    public void countCapitalized(List<String> lines) {
        for (String line : lines) {
            List<String> parts = breakLine(line);
            StringBuilder phrase = null;
            String toAdd = null;
            // find capitals that don't start sentences
            for (int i = 0; i < parts.size(); i++) {
                String part = parts.get(i);
                if (i > 0 && (isGeneralWord(part) || !WordUtils.precedesSentenceStart(parts.get(i - 1), punctuation))) {
                    if (WordUtils.isCapitalized(part) && !isIgnoredWord(part)) {
                        if (!isGeneralWord(part)) {
                            incrementWord(part, 0);
                        }
                        if (phrase == null) {
                            phrase = new StringBuilder(part);
                        } else {
                            phrase.append(part);
                        }
                        toAdd = phrase.toString();
                    } else {
                        if (phrase != null) {
                            if (part.equals(" ") || part.equals("of") || part.equals("the") || fillerWords.contains(part)) {
                                phrase.append(part);
                            } else {
                                if (!isGeneralWord(toAdd)) {
                                    incrementWord(toAdd, 1);
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
                if (phrase != null || counter.containsKey(part) && (i == 0 || WordUtils.precedesSentenceStart(parts.get(i - 1), punctuation))) {
                    if (part.length() > 1 && WordUtils.isCapitalized(part) && !isIgnoredWord(part)) {
                        if (!isGeneralWord(part)) {
                            incrementWord(part, 0);
                        }
                        if (phrase == null) {
                            phrase = new StringBuilder(part);
                        } else {
                            phrase.append(part);
                        }
                        toAdd = phrase.toString();
                    } else {
                        if (phrase != null) {
                            if (part.equals(" ") || part.equals("of") || part.equals("the") || fillerWords.contains(part)) {
                                phrase.append(part);
                            } else {
                                if (!isGeneralWord(toAdd)) {
                                    incrementWord(toAdd, 1);
                                }
                                phrase = null;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Manually increment a name some amount
     * If the name is not in the counter, it gets added
     *
     * @param name
     * @param increment
     */
    public void incrementWord(String name, int increment) {
        counter.put(name, getCount(name) + increment);
    }

    /**
     * Removes certain words from the counter
     *
     * @param words
     */
    public void removeWords(String... words) {
        removeWords(Arrays.asList(words));
    }

    /**
     * Removes certain words from the counter
     *
     * @param words
     */
    public void removeWords(Collection<String> words) {
        removeWordsBelowThreshold(words, Integer.MAX_VALUE);
    }

    /**
     * Removes certain words from the counter only if their occurrence is less than threshold
     *
     * @param words
     * @param threshold
     */
    public void removeWordsBelowThreshold(Collection<String> words, int threshold) {
        Map<String, Integer> reducedCounter = new HashMap<>();
        for (String cap : counter.keySet()) {
            if (!words.contains(cap) || getCount(cap) >= threshold) {
                reducedCounter.put(cap, getCount(cap));
            }
        }
        counter = reducedCounter;
    }

    /**
     * Prints out the counter in a nice format to the console,
     * and returns the strings as a Logger
     */
    public Logger printCounter() {
        Logger logger = new Logger();
        List<Map.Entry<String, Integer>> caps = new ArrayList<>(counter.entrySet());
        caps.sort(EntryComparator.DESCENDING);
        for (Map.Entry<String, Integer> cap : caps) {
            logger.log(cap.getKey() + "," + cap.getValue());
        }
        logger.log();
        logger.log(counter.size());
        return logger;
    }

    //==========================================//
    //       Name Manipulation Functions        //
    //==========================================//

    public Set<String> getTitledNames() {
        return titledNames;
    }

    public Set<String> getPluralized() {
        return pluralized;
    }

    public Set<String> getSurnames() {
        return surnames;
    }

    public void addSurnames(String... surnames) {
        addSurnames(Arrays.asList(surnames));
    }

    public void addSurnames(Collection<String> surnames) {
        this.surnames.addAll(surnames);
    }

    public Set<String> getPlaces() {
        return places;
    }

    public void addPlaces(String... places) {
        addPlaces(Arrays.asList(places));
    }

    public void addPlaces(Collection<String> places) {
        this.places.addAll(places);
    }

    public Set<String> getNames() {
        return names;
    }

    public void addNames(String... names) {
        addNames(Arrays.asList(names));
    }

    public void addNames(Collection<String> names) {
        this.names.addAll(names);
    }

    public void removeNames(String... names) {
        removeNames(Arrays.asList(names));
    }

    public void removeNames(Collection<String> names) {
        this.names.removeAll(names);
    }

    public Set<String> getNondescriptors() {
        return nondescriptors;
    }

    public void addNondescriptors(String... nondescriptors) {
        addNondescriptors(Arrays.asList(nondescriptors));
    }

    public void addNondescriptors(Collection<String> nondescriptors) {
        this.nondescriptors.addAll(nondescriptors);
    }

    public void removeNondescriptors(String... nondescriptors) {
        removeNondescriptors(Arrays.asList(nondescriptors));
    }

    public void removeNondescriptors(Collection<String> nondescriptors) {
        this.nondescriptors.removeAll(nondescriptors);
    }

    /**
     * This method generates internal sets of titled names, pluralized words, surnames, names from surnames,
     * places, and nondescriptive words. Titled names and names from surnames are combined into names, which
     * is all of the things the program thinks are names. Pluralized words, surnames, and places are added
     * to nondescriptors which is used by the CharacterGroups to prevent grouping on these common terms.
     * Surnames is also of course used to generate names from surnames.
     *
     * Use the above add and remove methods for more precise control over these fields. Note that adding
     * should be done prior to calling this method, and removing must be done after.
     */
    public void processCapitalized() {
        findTitledNames();
        findPluralized();
        findSurnames();
        findNames();
        findPlaces();
    }

    /**
     * Finds a set of names that occur in a word triplet preceded by an ignored word
     *
     * @return
     */
    private void findTitledNames() {
        Set<String> words = counter.keySet();
        Set<String> partNames = new HashSet<>();
        for (String cap : words) {
            String[] split = cap.split(" ");
            String name = StringUtils.substringAfter(cap, " ");
            if (split.length > 1 && isTitleWord(split[0]) && WordUtils.isCapitalized(split[1]) && !isGeneralWord(split[1])) {
                if (split.length == 3) {
                    titledNames.add(name);
                } else {
                    partNames.add(split[1]);
                }
            }
        }
        for (String name : partNames) {
            boolean isUnique = true;
            for (String cap : words) {
                // cut off after "of" and "the" because anything preceding is either still unique, or inherently not unique
                cap = StringUtils.substringBefore(cap, " of ");
                cap = StringUtils.substringBefore(cap, " the ");
                String[] split = cap.split(" ");
                if (split.length == 2) {
                    // not unique if first in a pair, or if second in a pair and not preceded by a general word
                    if (name.equals(split[0]) || (!isGeneralWord(split[0]) && name.equals(split[1]))) {
                        isUnique = false;
                    }
                    // if something contains name and another non-general word, it's a name
                    if (!isGeneralWord(split[0]) && !isGeneralWord(split[1])
                            && (name.equals(split[0]) || name.equals(split[1]))) {
                        titledNames.add(cap);
                    }
                } else if (split.length > 2) {
                    for (String s : split) {
                        // if it comes up as part of anything else, or pluralized, its not unique
                        if (name.equals(s) || name.equals(s + "s")) {
                            isUnique = false;
                        }
                    }
                }
            }
            if (isUnique) {
                titledNames.add(name);
            }
        }
        for (String name : titledNames) {
            incrementWord(name, 0);
        }
        names.addAll(titledNames);
    }

    /**
     * Finds words that exist in words and phrases as both "****" and "****s" and at least once do not follow "the"
     * Ignores words found in generalWords
     *
     * @return
     */
    private void findPluralized() {
        Set<String> words = counter.keySet();
        for (String cap : words) {
            String[] split = cap.split(" ");
            for (int i = 1; i < split.length; i++) {
                String s = split[i];
                if (!isGeneralWord(s) && !split[i - 1].equalsIgnoreCase("the")
                        && words.contains(s) && words.contains(s + "s")) {
                    pluralized.add(s);
                }
            }
        }
        nondescriptors.addAll(pluralized);
        nondescriptors.addAll(WordUtils.getPlurals(pluralized));
    }

    /**
     * Finds words that come second in multiple word pairs
     * Ignores words found in generalWords
     *
     * @return
     */
    private void findSurnames() {
        if (surnames == null) {
            surnames = new HashSet<>();
        }
        Set<String> words = counter.keySet();
        Set<String> once = new HashSet<>();
        for (String cap : words) {
            String name = stripTitle(stripPlace(cap));
            String[] split = name.split(" ");
            if (!isGeneralWord(split[0])) {
                if (split.length == 2 && !isGeneralWord(split[1])) {
                    if (once.contains(split[1])) {
                        surnames.add(split[1]);
                    } else {
                        once.add(split[1]);
                    }
                } else if (split.length == 3 && fillerWords.contains(split[1])) {
                    surnames.add(split[2]);
                }
            }
        }
        nondescriptors.addAll(surnames);
        nondescriptors.addAll(WordUtils.getPlurals(surnames));
    }

    /**
     * Returns phrases that follow "of" or "of the" in the capitalized phrases
     *
     * @return
     */
    private void findPlaces() {
        Set<String> words = counter.keySet();
        String ignoreString = names.toString();
        for (String cap : words) {
            if (cap.contains(" of ")) {
                String place = StringUtils.substringAfter(cap, " of ");
                if (place.startsWith("the")) {
                    place = place.substring(4);
                }
                if (!ignoreString.contains(stripTitle(place))) {
                    places.add(place);
                }
            }
        }
        nondescriptors.addAll(places);
    }

    /**
     * Finds all word pairs whose second word is a surname, and combines this with titled names
     * This method is completely dependent on the contents of titledNames and surnames
     * Ignores pairs with the first word in generalWords
     *
     * @return
     */
    private void findNames() {
        Set<String> words = counter.keySet();
        for (String cap : words) {
            String name = stripTitle(cap);
            String[] split = name.split(" ");
            if (!isGeneralWord(split[0]) && (split.length == 2 && surnames.contains(split[1])
                    || split.length == 3 && fillerWords.contains(split[1]) && surnames.contains(split[2]))) {
                names.add(name);
            }
        }
        for (String name : names) {
            incrementWord(name, 0);
        }
    }

    /**
     * Builds the internal CharacterGroups data structure
     * The nondescriptors set is passed to the CharacterGroups as nondescriptors
     */
    public CharacterGroups buildCharacterGroups() {
        return new CharacterGroups(counter, nondescriptors);
    }

    //==========================================//
    //            Utility functions             //
    //==========================================//

    private boolean isIgnoredWord(String part) {
        return ignoredWords.contains(part);
    }

    private boolean isTitleWord(String s) {
        return titleWords.contains(s);
    }

    private boolean isGeneralWord(String s) {
        return isTitleWord(s) || generalWords.contains(s);
    }

    /**
     * If the phrase ends with "of...", that chunk gets removed.
     * Otherwise returns name.
     *
     * @param name
     * @return
     */
    private String stripPlace(String name) {
        String noPlace = StringUtils.substringBefore(name, " of ");
        if (noPlace.contains(" ")) {
            return noPlace;
        } else {
            return name;
        }
    }

    /**
     * If the first word of name is a title (in generalWords), it gets removed.
     * Otherwise returns name.
     *
     * @param name
     * @return
     */
    private String stripTitle(String name) {
        String[] split = name.split(" ");
        if (split.length > 1 && isGeneralWord(split[0]) && WordUtils.isCapitalized(split[1])) {
            return StringUtils.substringAfter(name, " ");
        } else {
            return name;
        }
    }

    /**
     * Breaks a line of text into chunks
     * Each chunk is either a contiguous sequence of alphabetic characters, or
     * a filler sequence of non-alphabetic characters
     * Every character in the line occurs in the output (as distinct from
     * String.split, which removes instances of the splitting character)
     * @param line
     * @return
     */
    private static List<String> breakLine(String line) {
        List<String> pieces = new ArrayList<>();
        StringBuilder sb = null;
        boolean letters = true;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (WordUtils.isWordCharacter(c)) {
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

}
