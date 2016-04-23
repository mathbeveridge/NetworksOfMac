package edu.macalester.mscs.characters;

import edu.macalester.mscs.utils.FileUtils;
import edu.macalester.mscs.utils.WordUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ari Weiland
 */
public class GameOfThrones {

    public static final Set<String> IGNORED_WORDS = new HashSet<>(Arrays.asList(
            "My", "He", "His", "We", "Their", "Your", // pronouns  (It???)
            "This", "That", "There", // indirect pronouns
            "Who", "Why", // questions
            "House", "Houses", "Clan", "Lords", "Ladies", "Kings", "Dothraki", "Grace", // GoT specific
            "Father", "Mother", "Uncle", "Aunt", "Brother", "Brothers", "Sons", // familial references
            "If", "And", "Will", "With", "Half", "Men", "Man", "Tongue" // miscellaneous
    ));

    // Words that are titles
    public static final Set<String> TITLE_WORDS = new HashSet<>(Arrays.asList(
            "Lord", "Lady", "King", "Queen", "Regent", "Steward", "Prince", "Princess", // royal titles
            "Ser", "Maester", "Captain", "Commander", "Magister", "Master", "Builder",
            "Septon", "Knight", "Hand", "Protector", // professional titles
            "Khal", "Ko" // dothraki titles
    ));

    // Words that are not unique, but may still be descriptive, expecially in combination
    public static final Set<String> GENERAL_WORDS = new HashSet<>(Arrays.asList(
            "The", // titular articles
            "Young", "Old", "Fat", // endearing titles
            "High", "Great", "Grand", "First", "Second", // superlatives
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", // numbers
            "Black", "Red", "Green", "Blue", "White", // colors
            "Land", "Lands", "Sea", "Seas", "Island", "Isles", "City", "Cities", // geographics
            "Alley", "Gate", "Keep", "Market", "Tower", // landmarks
            "Flowers", "Storm", "Bear", "Stone" // miscellaneous
    ));

    // Words that are sometimes placed between first and last names
    public static final Set<String> FILLER_WORDS = new HashSet<>(Arrays.asList("zo", "mo"));

    public static void main(String[] args) {
        // initialize the finder
        CharacterFinder finder = new CharacterFinder(IGNORED_WORDS, TITLE_WORDS, GENERAL_WORDS, FILLER_WORDS, ".?!�");
        // read in the text
        finder.countCapitalized(FileUtils.readFile("src/main/resources/text/gameofthrones.txt"));
        // fix a few mistakes
        finder.incrementName("Jeor Mormont", 1); // gets wrecked
        finder.incrementName("Jeor", 0);
        finder.removeWords("Tully Stark"); // gets picked up accidentally
//        finder.removeWords("Storm Dancer"); // is a boat, not a name

        // gather names, titles, places, and things
        Set<String> titledNames = finder.getTitledNames();
        Set<String> pluralizedNames = finder.getPluralizedNames();
        Set<String> surnames = finder.getSurnames();
        Set<String> names = finder.getNamesBySurname(surnames);
        names.addAll(titledNames);
        Set<String> places = finder.getPlaces(names);
        Set<String> lonely = finder.getLonelyWords();

//        System.out.println(titledNames);
//        System.out.println(pluralizedNames);
//        System.out.println(surnames);
//        System.out.println(names);
//        System.out.println(places);
//        System.out.println(lonely);
//        System.out.println();

        finder.removePlaces();
        finder.removeTitles();
        finder.removeWordsBelowThreshold(lonely, 10);

//        finder.printCounter().writeLog("src/main/resources/data/characters/got-counter.csv");

        // gather phrases that are not inherently descriptive
        Set<String> nondescriptors = new HashSet<>();
        nondescriptors.addAll(pluralizedNames);
        nondescriptors.addAll(WordUtils.getPlurals(pluralizedNames));
        nondescriptors.addAll(surnames);
        nondescriptors.addAll(WordUtils.getPlurals(surnames));
        nondescriptors.addAll(places);

        // add problematic names
        nondescriptors.add("Jon");
        nondescriptors.add("Eddard");
        nondescriptors.add("Robert");
        nondescriptors.add("Balon");
        nondescriptors.add("Tytos");
        nondescriptors.add("Torrhen");
        nondescriptors.add("Martyn");

        // build character groups
        finder.buildCharacterGroups(nondescriptors);

        // add problematic names as appropriate
        finder.addToCharacterGroup("Jon Snow", "Jon");
        finder.addToCharacterGroup("Eddard Stark", "Eddard");
        finder.addToCharacterGroup("Robert Baratheon", "Robert");
        finder.addToCharacterGroup("Torrhen Karstark", "Torrhen");

        // manually combine more character groups
        finder.combineGroups("Jon Snow", "Jon Stark");
        finder.combineGroups("Eddard Stark", "Ned", "Lord Eddard");
        finder.combineGroups("Robert Baratheon", "Usurper", "King Robert");
        finder.combineGroups("Robert Arryn", "Lord of the Eyrie", "Lord Robert");
        finder.combineGroups("Balon Swann", "Ser Balon");
        finder.combineGroups("Tytos Blackwood", "Lord Tytos", "Lord Blackwood");
        finder.combineGroups("Bran", "Brandon Stark");
        finder.combineGroups("Petyr", "Littlefinger", "Lord Baelish");
        finder.combineGroups("Daenerys", "Dany", "Khaleesi", "Princess of Dragonstone");
        finder.combineGroups("Joffrey", "Joff");
        finder.combineGroups("Samwell", "Sam", "Piggy", "Lord of Ham");
        finder.combineGroups("Sandor", "Hound");
        finder.combineGroups("Benjen", "Ben");
        finder.combineGroups("Jeor", "Old Bear", "Commander Mormont", "Lord Mormont");
        finder.combineGroups("Tomard", "Tom");
        finder.combineGroups("Jon Arryn", "Lord Arryn");
        finder.combineGroups("Catelyn", "Lady Stark");
        finder.combineGroups("Pycelle", "Grand Maester");
        finder.combineGroups("Walder", "Lord Frey", "Lord of the Crossing");
        finder.combineGroups("Lysa", "Lady Arryn");
        finder.combineGroups("Maege", "Lady Mormont");
        finder.combineGroups("Greatjon", "Lord Umber");
        finder.combineGroups("Shella", "Lady Whent");
        finder.combineGroups("Drogo", "Great Rider");
        finder.combineGroups("Renly", "Lord of Storm");
        finder.combineGroups("Benjen", "First Ranger");
        finder.combineGroups("Marq", "Lord Piper");
        finder.combineGroups("Tywin", "Lord of Casterly Rock");
        finder.combineGroups("Horas", "Horror");
        finder.combineGroups("Jonos", "Lord Bracken");
        finder.combineGroups("Hobber", "Slobber");
        finder.combineGroups("Karyl", "Lord Vance");
        finder.combineGroups("Roose", "Lord of the Dreadfort", "Lord Bolton");
        finder.combineGroups("Hoster", "Lord of Riverrun");
        finder.combineGroups("Loras", "Knight of Flowers", "Daisy");
        finder.combineGroups("Gerold", "White Bull");

        // manually add important names that get missed
        names.add("Varys");
        names.add("Bronn");
        names.add("Septa Mordane");
        names.add("Pycelle");
        names.add("Mirri Maz Duur");
        names.add("Hodor");
        names.add("Nan");
        names.add("Pyp");
        names.add("Syrio Forel");
        names.add("Grenn");
        names.add("Irri");
        names.add("Jhiqui");
        names.add("Qotho");
        names.add("Tom");
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

        // manually remove a few names that are either mistakes, duplicates, or unused
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
        names.remove("Horror");         // as Horas
        names.remove("Slobber");        // as Hobber

        Set<String> firstNames = finder.getFirstNames(names);

        FileUtils.writeFile(finder.getNameList(), "src/main/resources/data/characters/got-list-full.txt");
        FileUtils.writeFile(finder.getNameList(names), "src/main/resources/data/characters/got-list-clean.txt");
        FileUtils.writeFile(finder.getNameList(firstNames), "src/main/resources/data/characters/got-list-no-dup.txt");
        FileUtils.writeFile(finder.getFirstNameList(names), "src/main/resources/data/characters/got-list-first.txt");

    }
}
