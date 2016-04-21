package edu.macalester.mscs.characters;

import edu.macalester.mscs.utils.FileUtils;
import edu.macalester.mscs.utils.WordUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ari Weiland
 */
public class ClashOfKings {

    public static final Set<String> IGNORED_WORDS = new HashSet<>(Arrays.asList(
            "My", "He", "His", "Her", "We", "Their", "You", "Your", "It", // pronouns
            "This", "That", "There", // indirect pronouns
            "Who", "Why", "What", // questions
            "Man", "Men", "With", "If", "And", "Will", "Half", "Free", "Watch", "God",
            "Wolf", "Hall", "Kingdoms", "Watchmen", "Shepherd", "Not", "Do", "Hot",
            "Tongue", "Guard", "Whores", "Stone", "Red", // miscellaneous
            "House", "Houses", "Clan", "Lords", "Ladies", "Kings", "Dothraki", "Grace", // GoT specific
            "Father", "Mother", "Uncle", "Aunt", "Brother", "Brothers", "Sons" // familial references
    ));

    // Words that are not unique, but may still be descriptive, expecially in combination
    public static final Set<String> TITLE_WORDS = new HashSet<>(Arrays.asList(
            "Lord", "Lady", "King", "Queen", "Regent", "Steward", "Prince", "Princess", // royal titles
            "Ser", "Maester", "Captain", "Commander", "Magister", "Master", "Builder",
            "Septon", "Knight", "Shipwright", "Goodwife", "Ranger", // professional titles
            "Khal", "Ko" // dothraki titles
    ));

    // Words that are not unique, but may still be descriptive, expecially in combination
    public static final Set<String> GENERAL_WORDS = new HashSet<>(Arrays.asList(
            "The", // titular articles
            "Young", "Old", "Fat", "Big", "Little", "Bastard", "Boy", // endearing titles
            "High", "Great", "Grand", "First", "Second", // superlatives
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", // numbers
            "Black", "Red", "Green", "Blue", "White", // colors
            "Land", "Lands", "Sea", "Seas", "Island", "Islands", "Isles", "Bay", "River", "Shore", // geographics
            "City", "Cities", "Alley", "Gate", "Keep", "Market", "Tower", "Hall", "Rock", "Castle", // landmarks
            "Cruel", "Bold", "Brave", // adjective titles
            "Flowers", "Storm", "Bull", "Long", "Spring" // miscellaneous
    ));

    public static void main(String[] args) {
        // initialize the finder
        CharacterFinder finder = new CharacterFinder(IGNORED_WORDS, TITLE_WORDS, GENERAL_WORDS, ".?!�");
        // read in the text
        finder.countCapitalized(FileUtils.readFile("src/main/resources/text/clashofkings.txt"));
        // fix a few mistakes
        finder.incrementName("Tytos Blackwood", 2);
        finder.removeWords("Walders"); // wtf is this???
        finder.removeWords("Petyr Pimple"); // wtf is this???
        finder.removeWords("Pimple");

        // gather names, titles, places, and things
        Set<String> titledNames = finder.getTitledNames();
        Set<String> pluralizedNames = finder.getPluralizedNames();
        Set<String> surnames = finder.getSurnames();
        Set<String> names = finder.getNamesBySurname(surnames);
        names.addAll(titledNames);
        Set<String> places = finder.getPlaces(names);
        places.add("Casterly");
        Set<String> lonely = finder.getLonelyWords();

        System.out.println(titledNames);
        System.out.println(pluralizedNames);
        System.out.println(surnames);
        System.out.println(names);
        System.out.println(places);
        System.out.println(lonely);
        System.out.println();

        finder.removePlaces();
        finder.removeTitles();

        finder.printCounter().writeLog("src/main/resources/data/characters/cok-counter.csv");

        // gather phrases that are not inherently descriptive
        Set<String> nondescriptors = new HashSet<>();
        nondescriptors.addAll(pluralizedNames);
        nondescriptors.addAll(WordUtils.getPlurals(pluralizedNames));
        nondescriptors.addAll(surnames);
        nondescriptors.addAll(WordUtils.getPlurals(surnames));
        nondescriptors.addAll(places);

        // build character groups
        finder.buildCharacterGroups(nondescriptors);

        // manually combine more character groups
        finder.combineGroups("Eddard", "Ned");
        finder.combineGroups("Bran", "Brandon Stark");
        finder.combineGroups("Robert", "Usurper");
        finder.combineGroups("Petyr", "Littlefinger", "Lord Baelish");
        finder.combineGroups("Daenerys", "Dany", "Khaleesi");
        finder.combineGroups("Joffrey", "Joff");
        finder.combineGroups("Samwell", "Sam");
        finder.combineGroups("Sandor", "Hound");
        finder.combineGroups("Benjen", "Ben");
        finder.combineGroups("Jeor", "Old Bear", "Commander Mormont", "Lord Mormont");
        finder.combineGroups("Jon Arryn", "Lord Arryn");
        finder.combineGroups("Catelyn", "Lady Stark");
        finder.combineGroups("Pycelle", "Grand Maester");
        finder.combineGroups("Walder", "Lord Frey", "Lord of the Crossing");
        finder.combineGroups("Lysa", "Lady Arryn");
        finder.combineGroups("Greatjon", "Lord Umber");
        finder.combineGroups("Renly", "Lord of Storm");
        finder.combineGroups("Tywin", "Lord of Casterly Rock");
        finder.combineGroups("Roose", "Lord of the Dreadfort", "Lord Bolton");
        finder.combineGroups("Hoster", "Lord of Riverrun");
        finder.combineGroups("Loras", "Knight of Flowers");
        finder.combineGroups("Davos", "Onion Knight");
        finder.combineGroups("Varys", "Spider");
        finder.combineGroups("Gregor", "Mountain");
        finder.combineGroups("Podrick", "Pod");

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
        names.add("Lady Whent");

        // manually remove a few names that are either mistakes, duplicates, or unused
        names.remove("Ned Stark");      // as Eddard Stark
        names.remove("Joff");           // as Joffrey Baratheon
        names.remove("Sam Tarly");      // as Samwell Tarly
        names.remove("Ben Stark");      // as Benjen Stark
        names.remove("Ranger");         // as Benjen Stark
        names.remove("Theon Stark");    // as Theon Greyjoy
        names.remove("Theon Turncloak");// as Theon Greyjoy
        names.remove("Brynden Blackfish");  // as Brynden Tully
        names.remove("Davos Shorthand");// as Davos Seaworth
        names.remove("Littlefinger");   // as Petyr Baelish
        names.remove("Grey Wind");      // dire wolf
        names.remove("Walder Freys");   // mistake
        names.remove("Aegon Targaryen");// unused, too problematic
        names.remove("Torrhen Stark");  // unused
        names.remove("Aerys Oakheart"); // unused

        Set<String> firstNames = finder.getFirstNames(names);

        FileUtils.writeFile(finder.getNameList(), "src/main/resources/data/characters/cok-list-full.txt");
        FileUtils.writeFile(finder.getNameList(names), "src/main/resources/data/characters/cok-list-clean.txt");
        FileUtils.writeFile(finder.getNameList(firstNames), "src/main/resources/data/characters/cok-list-no-dup.txt");
        FileUtils.writeFile(finder.getFirstNameList(names), "src/main/resources/data/characters/cok-list-first.txt");

    }
}
