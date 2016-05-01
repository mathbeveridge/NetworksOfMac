package edu.macalester.mscs.characters;

import edu.macalester.mscs.utils.FileUtils;

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
            "Flowers", "Storm", "Bull", "Long", "Spring", "Bear", "Hot", "Pie", "Ben", "Iron", "Watch",
            "Horn" // miscellaneous
    ));

    // Words that are sometimes placed between first and last names
    public static final Set<String> FILLER_WORDS = new HashSet<>(Arrays.asList("zo", "mo"));

    public static void main(String[] args) {
        // initialize the finder
        CharacterFinder finder = new CharacterFinder(IGNORED_WORDS, TITLE_WORDS, GENERAL_WORDS, FILLER_WORDS, ".?!�");
        // read in the text
        finder.countCapitalized(FileUtils.readFile("src/main/resources/text/clashofkings.txt"));
        // fix a few mistakes
        finder.incrementWord("Tytos Blackwood", 2);
        finder.removeWords("Walder Freys");
        finder.removeWords("Ser Guyard of King Renly");

        finder.printCounter().writeLog("src/main/resources/data/characters/cok-counter.csv");
        
        // manually add important names that get missed
        finder.addNames("Varys");
        finder.addNames("Hodor");
        finder.addNames("Yoren");
        finder.addNames("Qhorin Halfhand"); // note that in book one, his name was spelled "Quorin"
        finder.addNames("Gendry");
        finder.addNames("Bronn");
        finder.addNames("Rickon");
        finder.addNames("Shae");
        finder.addNames("Craster");
        finder.addNames("Xaro Xhoan Daxos");
        finder.addNames("Weese");
        finder.addNames("Osha");
        finder.addNames("Jaqen");
        finder.addNames("Pycelle");
        finder.addNames("Hot Pie");
        finder.addNames("Nan");
        finder.addNames("Rorge");
        finder.addNames("Pyat Pree");
        finder.addNames("Dagmer Cleftjaw");
        finder.addNames("Shagga");
        finder.addNames("Thoren Smallwood");
        finder.addNames("Mance Rayder");
        finder.addNames("Salladhor Saan");
        finder.addNames("Lorren");
        finder.addNames("Timett");
        finder.addNames("Wex");
        finder.addNames("High Septon");
        finder.addNames("Edd");
        finder.addNames("Hallyne");
        finder.addNames("Ygritte");
        finder.addNames("Patchface");
        finder.addNames("Syrio Forel");
        finder.addNames("Aggo");
        finder.addNames("Bael");
        finder.addNames("Esgred");
        finder.addNames("Mikken");
        finder.addNames("Ebben");
        finder.addNames("Farlen");
        finder.addNames("Viserys");
        finder.addNames("Belwas");
        finder.addNames("Dywen");
        finder.addNames("Devan");
        finder.addNames("Raff");
        finder.addNames("Alebelly");
        finder.addNames("Grenn");
        finder.addNames("Chataya");
        finder.addNames("Vylarr");
        finder.addNames("Allard");
        finder.addNames("Urzen");
        finder.addNames("Alayaya");
        finder.addNames("Rymund");
        finder.addNames("Arstan");
        finder.addNames("Jonquil");
        finder.addNames("Koss");
        finder.addNames("Chella");
        finder.addNames("Tysha");
        finder.addNames("Rattleshirt");
        finder.addNames("Mordane");

        finder.addPlaces("Casterly");

        finder.addNondescriptors("Jon");
        finder.addNondescriptors("Brandon");
        finder.addNondescriptors("Robert");
        finder.addNondescriptors("Petyr");
        finder.addNondescriptors("Balon");
        finder.addNondescriptors("Walder");
        finder.addNondescriptors("Aemon");
        finder.addNondescriptors("Martyn");
        finder.addNondescriptors("Rodrik");

        // gather names, titles, places, and things
        finder.processCapitalized();

        // manually remove a few names that are either mistakes, duplicates, or unused
        finder.removeNames("Ned Stark");      // as Eddard Stark
        finder.removeNames("Brandon Stark");  // as Bran Stark
        finder.removeNames("Joff");           // as Joffrey Baratheon
        finder.removeNames("Sam Tarly");      // as Samwell Tarly
        finder.removeNames("Ben Stark");      // as Benjen Stark
        finder.removeNames("Ranger");         // as Benjen Stark
        finder.removeNames("Theon Stark");    // as Theon Greyjoy
        finder.removeNames("Theon Turncloak");// as Theon Greyjoy
        finder.removeNames("Brynden Blackfish");  // as Brynden Tully
        finder.removeNames("Davos Shorthand");// as Davos Seaworth
        finder.removeNames("Littlefinger");   // as Petyr Baelish
        finder.removeNames("Reaper");         // as Balon Greyjoy
        finder.removeNames("Eunuch");         // as Varys
        finder.removeNames("Grey Wind");      // dire wolf
        finder.removeNames("Paramount");      // unused
        finder.removeNames("Torrhen Stark");  // unused
        finder.removeNames("Aerys Oakheart"); // unused
        finder.removeNames("Bleeding Star");  // ???

        finder.removeNondescriptors("Bywater");

        System.out.println(finder.getTitledNames());
        System.out.println(finder.getPluralized());
        System.out.println(finder.getSurnames());
        System.out.println(finder.getNames());
        System.out.println(finder.getPlaces());
        System.out.println(finder.getNondescriptors());
        System.out.println();

        // build character groups
        CharacterGroups groups = finder.buildCharacterGroups();

        // add back problematic names as necessary
        groups.addAliasToGroup("Jon Snow", "Jon", finder.getCount("Jon"));
        groups.addAliasToGroup("Robert Baratheon", "Robert", finder.getCount("Robert"));
        groups.addAliasToGroup("Petyr Baelish", "Petyr", finder.getCount("Petyr"));
        groups.addAliasToGroup("Walder Frey", "Walder", finder.getCount("Walder"));
        groups.addAliasToGroup("Maester Aemon", "Aemon", finder.getCount("Aemon"));

        // manually combine more character groups
        groups.combineGroups("Jon Snow", "Lord Snow");
        groups.combineGroups("Jon Fossoway", "Ser Jon", "Ser Jon Fossoway");
        groups.combineGroups("Bran", "Brandon Stark");
        groups.combineGroups("Robert Baratheon", "King Robert", "Robert the Usurper");
        groups.combineGroups("Robert Arryn", "Lord Robert", "Lord Robert of the Eyrie");
        groups.combineGroups("Petyr Baelish", "Littlefinger", "Lord Petyr");
        groups.combineGroups("Balon Greyjoy", "Lord Greyjoy", "Lord of the Iron Islands", "Reaper", "King Balon", "Lord Balon");
        groups.combineGroups("Balon Swann", "Ser Balon", "Ser Balon Swann");
        groups.combineGroups("Walder Frey", "Lord Frey", "Lord of the Crossing", "Lord Walder", "Lord Walder Frey");
        groups.combineGroups("Aemon the Dragonknight", "Prince Aemon");
        groups.combineGroups("Rodrik Cassel", "Ser Rodrik", "Ser Rodrik Cassel");
        groups.combineGroups("Eddard", "Ned");
        groups.combineGroups("Daenerys", "Dany", "Khaleesi");
        groups.combineGroups("Joffrey", "Joff");
        groups.combineGroups("Samwell", "Sam");
        groups.combineGroups("Sandor", "Hound");
        groups.combineGroups("Benjen", "Ben Stark");
        groups.combineGroups("Jeor", "Old Bear", "Commander Mormont", "Lord Mormont", "Lord of Bear Island", "Lord Crow");
        groups.combineGroups("Jon Arryn", "Lord Arryn");
        groups.combineGroups("Catelyn", "Lady Stark");
        groups.combineGroups("Pycelle", "Grand Maester");
        groups.combineGroups("Lysa", "Lady Arryn");
        groups.combineGroups("Greatjon", "Lord Umber");
        groups.combineGroups("Renly", "Lord of Storm");
        groups.combineGroups("Tywin", "Lord of Casterly Rock");
        groups.combineGroups("Roose", "Lord of the Dreadfort", "Lord Bolton");
        groups.combineGroups("Hoster", "Lord of Riverrun");
        groups.combineGroups("Loras", "Knight of Flowers");
        groups.combineGroups("Davos", "Onion Knight");
        groups.combineGroups("Varys", "Spider", "Eunuch");
        groups.combineGroups("Gregor", "Mountain");
        groups.combineGroups("Podrick", "Pod");
        groups.combineGroups("Ramsay", "Reek");
        groups.combineGroups("Arya", "Lumpyface");
        groups.combineGroups("Cersei", "Queen Regent");
        groups.combineGroups("Alester", "Lord of Brightwater", "Lord Florent");
        groups.combineGroups("Howland", "Lord of Greywater");
        groups.combineGroups("Velaryon", "Lord of the Tides");
        groups.combineGroups("Pylos", "Lord Maester");
        groups.combineGroups("Gulian", "Lord Swann");
        groups.combineGroups("Vargo", "Lord Hoat");
        groups.combineGroups("Rattleshirt", "Lord of Bones");
        groups.combineGroups("Wyman", "Lord of White Harbor");
        groups.combineGroups("Randyll", "Lord Tarly");
        groups.combineGroups("Mace", "Lord of Highgarden", "Lord Tyrell");
        groups.combineGroups("Stannis", "Lord of Dragonstone");
        groups.combineGroups("Rickard Karstark", "Lord Karstark");
        groups.combineGroups("Bryce", "Lord Caron");
        groups.combineGroups("Janos", "Lord Slynt");
        groups.combineGroups("Donella", "Lady Hornwood");
        groups.combineGroups("Emmon Cuy", "Ser Emmon");
        groups.combineGroups("Arya", "Arry");
        groups.combineGroups("Catelyn", "Cat");
        groups.combineGroups("Gerold", "White Bull");

        groups.writeNameList("src/main/resources/data/characters/cok-list-full.txt");
        groups.writeNameList(finder.getNames(), false, 4, "src/main/resources/data/characters/cok-list-clean-redundant.txt");

    }
}
