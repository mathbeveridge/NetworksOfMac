package edu.macalester.mscs.characters;

import edu.macalester.mscs.utils.FileUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ari Weiland
 */
public class GameOfThronesFinder {

    public static final Set<String> IGNORED_WORDS = new HashSet<>(Arrays.asList(
            "The",
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
            "Septon", "Knight", "Hand", "Protector", "Rider", // professional titles
            "Khal", "Ko" // dothraki titles
    ));

    // Words that are not unique, but may still be descriptive, expecially in combination
    public static final Set<String> GENERAL_WORDS = new HashSet<>(Arrays.asList(
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
        finder.incrementWord("Jeor Mormont", 1); // gets wrecked
        finder.incrementWord("Jeor", 0);
        finder.removeWords("Tully Stark"); // gets picked up accidentally

        finder.printCounter().writeLog("src/main/resources/data/characters/got-counter.csv");

        // manually add important names that get missed
        finder.addNames("Varys");
        finder.addNames("Bronn");
        finder.addNames("Septa Mordane");
        finder.addNames("Pycelle");
        finder.addNames("Mirri Maz Duur");
        finder.addNames("Hodor");
        finder.addNames("Nan");
        finder.addNames("Pyp");
        finder.addNames("Syrio Forel");
        finder.addNames("Grenn");
        finder.addNames("Irri");
        finder.addNames("Jhiqui");
        finder.addNames("Qotho");
        finder.addNames("Tom");
        finder.addNames("Osha");
        finder.addNames("Shagga");
        finder.addNames("Doreah");
        finder.addNames("Yoren");
        finder.addNames("Halder");
        finder.addNames("Lyanna");
        finder.addNames("Jhogo");
        finder.addNames("Mord");
        finder.addNames("Gared");
        finder.addNames("Marillion");
        finder.addNames("Aggo");
        finder.addNames("Alyn");
        finder.addNames("Haggo");
        finder.addNames("Chett");
        finder.addNames("Cohollo");
        finder.addNames("Timett");
        finder.addNames("Mycah");
        finder.addNames("Conn");
        finder.addNames("Shae");
        finder.addNames("Rakharo");
        finder.addNames("Chiggen");
        finder.addNames("Desmond");
        finder.addNames("Jyck");
        finder.addNames("Donal Noye");
        finder.addNames("Masha Heddle");
        finder.addNames("Quaro");
        finder.addNames("Hullen");
        finder.addNames("Harwin");
        finder.addNames("Moreo Tumitis");
        finder.addNames("Dareon");
        finder.addNames("Rhaego");
        finder.addNames("Cayn");
        finder.addNames("High Septon");
        finder.addNames("Morrec");
        finder.addNames("Maegor");
        finder.addNames("Mance Rayder");
        finder.addNames("Hobb");
        finder.addNames("Malleon");
        finder.addNames("Helman Tallhart");
        finder.addNames("Jafer Flowers");
        finder.addNames("Cotter Pyke");
        finder.addNames("Hugh");
        finder.addNames("Lothor Brune");
        finder.addNames("Howland Reed");
        finder.addNames("Bryce Caron");
        finder.addNames("Mychel Redfort");
        finder.addNames("Florian");
        finder.addNames("Quorin Halfhand");
        finder.addNames("Endrew Tarth");
        finder.addNames("Jaehaerys");
        finder.addNames("Jalabhar Xho");
        finder.addNames("Podrick Payne");

        finder.addNondescriptors("Jon");
        finder.addNondescriptors("Eddard");
        finder.addNondescriptors("Robert");
        finder.addNondescriptors("Balon");
        finder.addNondescriptors("Tytos");
        finder.addNondescriptors("Torrhen");
        finder.addNondescriptors("Martyn");

        // gather names, titles, places, and things
        finder.processCapitalized();

        // manually remove a few names that are either mistakes, duplicates, or unused
        finder.removeNames("Yard");           // mistake
        finder.removeNames("Valyrian");       // mistake
        finder.removeNames("Ned Stark");      // as Eddard Stark
        finder.removeNames("Jon Stark");      // as Jon Snow
        finder.removeNames("Daenerys Stormborn"); // as Daenerys Targaryen
        finder.removeNames("Catelyn Tully");  // as Catelyn Stark
        finder.removeNames("Joff");           // as Joffrey Baratheon
        finder.removeNames("Rider");          // as Drogo
        finder.removeNames("Sam Tarly");      // as Samwell Tarly
        finder.removeNames("Piggy");          // as Samwell Tarly
        finder.removeNames("Rodrik Stark");   // unused
        finder.removeNames("Ben Stark");      // as Benjen Stark
        finder.removeNames("Ranger");         // as Benjen Stark
        finder.removeNames("Theon Stark");    // as Theon Greyjoy
        finder.removeNames("Brynden Blackfish");  // as Brynden Tully
        finder.removeNames("Daisy");          // as Loras Tyrell
        finder.removeNames("Horror");         // as Horas
        finder.removeNames("Slobber");        // as Hobber

        System.out.println(finder.getTitledNames());
        System.out.println(finder.getPluralized());
        System.out.println(finder.getSurnames());
        System.out.println(finder.getNames());
        System.out.println(finder.getPlaces());
        System.out.println(finder.getNondescriptors());
        System.out.println();

        // build character groups
        CharacterGroups groups = finder.buildCharacterGroups();
        
        // add problematic names as appropriate
        groups.addAliasToGroup("Jon Snow", "Jon", finder.getCount("Jon"));
        groups.addAliasToGroup("Eddard Stark", "Eddard", finder.getCount("Eddard"));
        groups.addAliasToGroup("Robert Baratheon", "Robert", finder.getCount("Robert"));
        groups.addAliasToGroup("Torrhen Karstark", "Torrhen", finder.getCount("Torrhen"));

        // manually combine more character groups
        groups.combineGroups("Jon Snow", "Jon Stark");
        groups.combineGroups("Jon Arryn", "Lord Jon");
        groups.combineGroups("Eddard Stark", "Ned", "Lord Eddard", "Lord Eddard Stark");
        groups.combineGroups("Robert Baratheon", "Usurper", "King Robert");
        groups.combineGroups("Robert Arryn", "Lord of the Eyrie", "Lord Robert");
        groups.combineGroups("Balon Swann", "Ser Balon", "Ser Balon Swann");
        groups.combineGroups("Balon Greyjoy", "Lord Balon Greyjoy");
        groups.combineGroups("Tytos Blackwood", "Lord Tytos", "Lord Blackwood", "Lord Tytos Blackwood");
        groups.combineGroups("Tytos Brax", "Ser Tytos Brax");
        groups.combineGroups("Bran", "Brandon Stark");
        groups.combineGroups("Petyr", "Littlefinger", "Lord Baelish");
        groups.combineGroups("Daenerys", "Dany", "Khaleesi", "Princess of Dragonstone");
        groups.combineGroups("Joffrey", "Joff");
        groups.combineGroups("Samwell", "Sam", "Piggy", "Lord of Ham");
        groups.combineGroups("Sandor", "Hound");
        groups.combineGroups("Benjen", "Ben");
        groups.combineGroups("Jeor", "Old Bear", "Commander Mormont", "Lord Mormont");
        groups.combineGroups("Tomard", "Tom");
        groups.combineGroups("Jon Arryn", "Lord Arryn");
        groups.combineGroups("Catelyn", "Lady Stark");
        groups.combineGroups("Pycelle", "Grand Maester");
        groups.combineGroups("Walder", "Lord Frey", "Lord of the Crossing");
        groups.combineGroups("Lysa", "Lady Arryn");
        groups.combineGroups("Maege", "Lady Mormont");
        groups.combineGroups("Greatjon", "Lord Umber");
        groups.combineGroups("Shella", "Lady Whent");
        groups.combineGroups("Drogo", "Great Rider");
        groups.combineGroups("Renly", "Lord of Storm");
        groups.combineGroups("Benjen", "First Ranger");
        groups.combineGroups("Marq", "Lord Piper");
        groups.combineGroups("Tywin", "Lord of Casterly Rock");
        groups.combineGroups("Horas", "Horror");
        groups.combineGroups("Jonos", "Lord Bracken");
        groups.combineGroups("Hobber", "Slobber");
        groups.combineGroups("Karyl", "Lord Vance");
        groups.combineGroups("Roose", "Lord of the Dreadfort", "Lord Bolton");
        groups.combineGroups("Hoster", "Lord of Riverrun");
        groups.combineGroups("Loras", "Knight of Flowers", "Daisy");
        groups.combineGroups("Gerold", "White Bull");

        groups.writeNameList("src/main/resources/data/characters/got-list-full.txt");
        groups.writeNameList(finder.getNames(), true, 4, "src/main/resources/data/characters/got-list-clean.txt");
        groups.writeNameList(finder.getNames(), false, 4, "src/main/resources/data/characters/got-list-clean-redundant.txt");
    }
}
