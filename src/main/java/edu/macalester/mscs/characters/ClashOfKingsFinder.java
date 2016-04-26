package edu.macalester.mscs.characters;

import edu.macalester.mscs.utils.FileUtils;
import edu.macalester.mscs.utils.WordUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ari Weiland
 */
public class ClashOfKingsFinder {

    public static final Set<String> IGNORED_WORDS = new HashSet<>(Arrays.asList(
            "My", "He", "His", "Her", "We", "Their", "You", "Your", "It", // pronouns
            "This", "That", "There", // indirect pronouns
            "Who", "Why", "What", // questions
            "House", "Houses", "Clan", "Lords", "Ladies", "Kings", "Dothraki", "Grace", // GoT specific
            "Father", "Mother", "Uncle", "Aunt", "Brother", "Brothers", "Sons", // familial references
            "Not", "Stone", "Men", "Man", "Guard", "Was", "Bread", "Wind", "Tongue" // miscellaneous
    ));

    // Words that are not unique, but may still be descriptive, expecially in combination
    public static final Set<String> TITLE_WORDS = new HashSet<>(Arrays.asList(
            "Lord", "Lady", "King", "Queen", "Regent", "Steward", "Prince", "Princess", // royal titles
            "Ser", "Maester", "Captain", "Commander", "Magister", "Master", "Builder",
            "Septon", "Knight", "Shipwright", "Goodwife", "Ranger", "Squire", // professional titles
            "Khal", "Ko" // dothraki titles
    ));

    // Words that are not unique, but may still be descriptive, expecially in combination
    public static final Set<String> GENERAL_WORDS = new HashSet<>(Arrays.asList(
            "The", // titular articles
            "Young", "Old", "Fat", "Big", "Little", "Bastard", "Boy", // endearing titles
            "High", "Great", "Grand", "First", "Second", // superlatives
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", // numbers
            "Black", "Red", "Green", "Blue", "White", // colors
            "Land", "Lands", "Sea", "Seas", "Island", "Islands", "Isles", "Bay", "River", "Shore", "Point", // geographics
            "City", "Cities", "Alley", "Gate", "Keep", "Market", "Tower", "Hall", "Rock", "Castle", "Lane", // landmarks
            "Cruel", "Bold", "Brave", "Good", "Strong", "Bitter", "Sweet", "Bad", // adjective titles
            "Flowers", "Storm", "Bull", "Long", "Spring", "Bear", "Hot", "Pie", "Ben", "Iron" // miscellaneous
    ));

    // Words that are sometimes placed between first and last names
    public static final Set<String> FILLER_WORDS = new HashSet<>(Arrays.asList("zo", "mo"));

    public static void main(String[] args) {
        // initialize the finder
        CharacterFinder finder = new CharacterFinder(IGNORED_WORDS, TITLE_WORDS, GENERAL_WORDS, FILLER_WORDS, ".?!�");
        // read in the text
        finder.countCapitalized(FileUtils.readFile("src/main/resources/text/clashofkings.txt"));
        // fix a few mistakes
        finder.incrementName("Tytos Blackwood", 2);
        finder.removeWords("Walder Freys");

        // gather names, titles, places, and things
        Set<String> titledNames = finder.getTitledNames();
        Set<String> pluralizedNames = finder.getPluralizedNames();
        pluralizedNames.remove("Walder");
        Set<String> surnames = finder.getSurnames();
        Set<String> names = finder.getNamesBySurname(surnames);
        surnames.remove("Bywater");
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
        finder.removeWordsBelowThreshold(lonely, 5);

        finder.printCounter().writeLog("src/main/resources/data/characters/cok-counter.csv");

        // gather phrases that are not inherently descriptive
        Set<String> nondescriptors = new HashSet<>();
        nondescriptors.addAll(pluralizedNames);
        nondescriptors.addAll(WordUtils.getPlurals(pluralizedNames));
        nondescriptors.addAll(surnames);
        nondescriptors.addAll(WordUtils.getPlurals(surnames));
        nondescriptors.addAll(places);

        // add problematic names
        nondescriptors.add("Jon");
        nondescriptors.add("Brandon");
        nondescriptors.add("Robert");
        nondescriptors.add("Petyr");
        nondescriptors.add("Balon");
        nondescriptors.add("Walder");
        nondescriptors.add("Aemon");
        nondescriptors.add("Martyn");
        nondescriptors.add("Rodrik");

        // build character groups
        finder.buildCharacterGroups(nondescriptors);

        // add back problematic names as necessary
        finder.addToCharacterGroup("Jon Snow", "Jon");
        finder.addToCharacterGroup("Robert Baratheon", "Robert");
        finder.addToCharacterGroup("Petyr Baelish", "Petyr");
        finder.addToCharacterGroup("Walder Frey", "Walder");
        finder.addToCharacterGroup("Maester Aemon", "Aemon");

        // manually combine more character groups
        finder.combineGroups("Jon Snow", "Lord Snow");
        finder.combineGroups("Jon Fossoway", "Ser Jon");
        finder.combineGroups("Bran", "Brandon Stark");
        finder.combineGroups("Robert Baratheon", "King Robert", "Robert the Usurper");
        finder.combineGroups("Robert Arryn", "Lord Robert");
        finder.combineGroups("Petyr Baelish", "Littlefinger", "Lord Petyr");
        finder.combineGroups("Balon Greyjoy", "Lord Greyjoy", "Lord of the Iron Islands", "Reaper", "King Balon", "Lord Balon");
        finder.combineGroups("Balon Swann", "Ser Balon");
        finder.combineGroups("Walder Frey", "Lord Frey", "Lord of the Crossing", "Lord Walder");
        finder.combineGroups("Rodrik Cassel", "Ser Rodrik");
        finder.combineGroups("Eddard", "Ned");
        finder.combineGroups("Daenerys", "Dany", "Khaleesi");
        finder.combineGroups("Joffrey", "Joff");
        finder.combineGroups("Samwell", "Sam");
        finder.combineGroups("Sandor", "Hound");
        finder.combineGroups("Benjen", "Ben Stark");
        finder.combineGroups("Jeor", "Old Bear", "Commander Mormont", "Lord Mormont", "Lord of Bear Island", "Lord Crow");
        finder.combineGroups("Jon Arryn", "Lord Arryn");
        finder.combineGroups("Catelyn", "Lady Stark");
        finder.combineGroups("Pycelle", "Grand Maester");
        finder.combineGroups("Lysa", "Lady Arryn");
        finder.combineGroups("Greatjon", "Lord Umber");
        finder.combineGroups("Renly", "Lord of Storm");
        finder.combineGroups("Tywin", "Lord of Casterly Rock");
        finder.combineGroups("Roose", "Lord of the Dreadfort", "Lord Bolton");
        finder.combineGroups("Hoster", "Lord of Riverrun");
        finder.combineGroups("Loras", "Knight of Flowers");
        finder.combineGroups("Davos", "Onion Knight");
        finder.combineGroups("Varys", "Spider", "Eunuch");
        finder.combineGroups("Gregor", "Mountain");
        finder.combineGroups("Podrick", "Pod");
        finder.combineGroups("Ramsay", "Reek");
        finder.combineGroups("Arya", "Lumpyface");
        finder.combineGroups("Cersei", "Queen Regent");
        finder.combineGroups("Alester", "Lord of Brightwater", "Lord Florent");
        finder.combineGroups("Howland", "Lord of Greywater");
        finder.combineGroups("Velaryon", "Lord of the Tides");
        finder.combineGroups("Pylos", "Lord Maester");
        finder.combineGroups("Gulian", "Lord Swann");
        finder.combineGroups("Vargo", "Lord Hoat");
        finder.combineGroups("Rattleshirt", "Lord of Bones");
        finder.combineGroups("Wyman", "Lord of White Harbor");
        finder.combineGroups("Randyll", "Lord Tarly");
        finder.combineGroups("Mace", "Lord of Highgarden", "Lord Tyrell");
        finder.combineGroups("Stannis", "Lord of Dragonstone");
        finder.combineGroups("Rickard Karstark", "Lord Karstark");
        finder.combineGroups("Bryce", "Lord Caron");
        finder.combineGroups("Janos", "Lord Slynt");
        finder.combineGroups("Donella", "Lady Hornwood");
        finder.combineGroups("Emmon Cuy", "Ser Emmon");
        finder.combineGroups("Arya", "Arry");
        finder.combineGroups("Catelyn", "Cat");
        finder.combineGroups("Gerold", "White Bull");

        // manually add important names that get missed
        names.add("Varys");
        names.add("Hodor");
        names.add("Yoren");
        names.add("Qhorin Halfhand"); // note that in book one, his name was spelled "Quorin"
        names.add("Gendry");
        names.add("Bronn");
        names.add("Rickon");
        names.add("Shae");
        names.add("Craster");
        names.add("Xaro Xhoan Daxos");
        names.add("Weese");
        names.add("Osha");
        names.add("Jaqen");
        names.add("Pycelle");
        names.add("Hot Pie");
        names.add("Nan");
        names.add("Rorge");
        names.add("Pyat Pree");
        names.add("Dagmer Cleftjaw");
        names.add("Shagga");
        names.add("Thoren Smallwood");
        names.add("Mance Rayder");
        names.add("Salladhor Saan");
        names.add("Lorren");
        names.add("Timett");
        names.add("Wex");
        names.add("High Septon");
        names.add("Edd");
        names.add("Hallyne");
        names.add("Ygritte");
        names.add("Patchface");
        names.add("Syrio Forel");
        names.add("Aggo");
        names.add("Bael");
        names.add("Esgred");
        names.add("Mikken");
        names.add("Ebben");
        names.add("Farlen");
        names.add("Viserys");
        names.add("Belwas");
        names.add("Dywen");
        names.add("Devan");
        names.add("Raff");
        names.add("Alebelly");
        names.add("Grenn");
        names.add("Chataya");
        names.add("Vylarr");
        names.add("Allard");
        names.add("Urzen");
        names.add("Alayaya");
        names.add("Rymund");
        names.add("Arstan");
        names.add("Jonquil");
        names.add("Koss");
        names.add("Chella");
        names.add("Tysha");
        names.add("Rattleshirt");
        names.add("Mordane");

        // manually remove a few names that are either mistakes, duplicates, or unused
        names.remove("Ned Stark");      // as Eddard Stark
        names.remove("Brandon Stark");  // as Bran Stark
        names.remove("Joff");           // as Joffrey Baratheon
        names.remove("Sam Tarly");      // as Samwell Tarly
        names.remove("Ben Stark");      // as Benjen Stark
        names.remove("Ranger");         // as Benjen Stark
        names.remove("Theon Stark");    // as Theon Greyjoy
        names.remove("Theon Turncloak");// as Theon Greyjoy
        names.remove("Brynden Blackfish");  // as Brynden Tully
        names.remove("Davos Shorthand");// as Davos Seaworth
        names.remove("Littlefinger");   // as Petyr Baelish
        names.remove("Reaper");         // as Balon Greyjoy
        names.remove("Eunuch");         // as Varys
        names.remove("Grey Wind");      // dire wolf
        names.remove("Paramount");      // unused
        names.remove("Torrhen Stark");  // unused
        names.remove("Aerys Oakheart"); // unused
        names.remove("Bleeding Star");  // ???

        Set<String> firstNames = finder.getFirstNames(names);

        FileUtils.writeFile(finder.getNameList(), "src/main/resources/data/characters/cok-list-full.txt");
        FileUtils.writeFile(finder.getNameList(names, true, 4), "src/main/resources/data/characters/cok-list-clean.txt");
        FileUtils.writeFile(finder.getNameList(firstNames, true, 4), "src/main/resources/data/characters/cok-list-no-dup.txt");
        FileUtils.writeFile(finder.getFirstNameList(names), "src/main/resources/data/characters/cok-list-first.txt");

    }
}
