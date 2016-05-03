package edu.macalester.mscs.characters;

import edu.macalester.mscs.utils.FileUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ari Weiland
 */
public class FeastForCrowsFinder {

    public static final Set<String> IGNORED_WORDS = new HashSet<>(Arrays.asList(
            "My", "She", "He", "His", "Her", "We", "They", "Their", "You", "Your", "It", // pronouns
            "This", "That", "There", // indirect pronouns
            "Who", "Why", "What", "Will", "Was", // questions
            "House", "Houses", "Clan", "Lords", "Ladies", "Kings", "Dothraki", "Grace", "God", // GoT specific
            "Father", "Mother", "Uncle", "Aunt", "Brother", "Brothers", "Sons", "Daughter", "Cousin", // familial references
            "Men", "Man", "All", "Storm", "Summer", "Merry", "Common" // miscellaneous
    ));

    // Words that are not unique, but may still be descriptive, expecially in combination
    public static final Set<String> TITLE_WORDS = new HashSet<>(Arrays.asList(
            "Lord", "Lady", "King", "Queen", "Regent", "Steward", "Prince", "Princess", "Triarch", // royal titles
            "Ser", "Maester", "Captain", "Commander", "Magister", "Master", "Builder", "Knight",
            "Septon", "Septa", "Shipwright", "Goodwife", "Ranger", "Squire", "Admiral", "Archmaester", // professional titles
            "Khal", "Ko" // dothraki titles
    ));

    // Words that are not unique, but may still be descriptive, expecially in combination
    public static final Set<String> GENERAL_WORDS = new HashSet<>(Arrays.asList(
            "The", // titular articles
            "Young", "Old", "Fat", "Big", "Little", "Small", "Bastard", "Boy", "Deaf", "Blind", "Hero", // endearing titles
            "High", "Great", "Grand", "First", "Second", "Third", // superlatives
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", // numbers
            "Black", "Red", "Green", "Blue", "White", "Grey", "Brown", "Yellow", "Silver", "Golden", // colors
            "Land", "Lands", "Sea", "Seas", "Island", "Islands", "Isle", "Isles", "Bay", "River", "Shore",
            "Point", "Lake", "Hills", "Straits", "Vale", "Wood", // geographics
            "City", "Cities", "Alley", "Gate", "Keep", "Market", "Tower", "Hall", "Rock", "Castle", "Lane",
            "Bridge", "Sept", "Harbor", "Mill", "Port", "Town", // landmarks
            "Just", "Sweet", "Brave", "Bold", "Scared", "Poor", "Blessed", "Mad", "Unworthy", "Good", "Long",
            "Faithful", "Gallant", "Lusty", // adjective titles
            "Spotted", "Iron", "Bloody", "Belly", "Fool", "Ghost" // miscellaneous
    ));

    // Words that are sometimes placed between first and last names
    public static final Set<String> FILLER_WORDS = new HashSet<>(Arrays.asList("zo", "mo", "na"));

    public static void main(String[] args) {
        // initialize the finder
        CharacterFinder finder = new CharacterFinder(IGNORED_WORDS, TITLE_WORDS, GENERAL_WORDS, FILLER_WORDS, ".?!�");
        // read in the text
        finder.countCapitalized(FileUtils.readFile("src/main/resources/text/feastforcrows.txt"));
        // fix a few mistakes
        finder.incrementWord("Jeor Mormont", 0);
        finder.removeWords("Winterfell Tommen"); // mistake
        finder.removeWords("Arya the Lannisters"); // mistake
        finder.removeWords("Ser Hyle of Podrick"); // mistake
        finder.removeWords("Lord Nestor the Gates of the Moon"); // mistake
        finder.removeWords("Randyll Tarly the Hand of the King"); // mistake
        finder.removeWords("Petyr Pimple"); // unused
        finder.removeWords("Strong Sam Stone"); // unused
        finder.removeWords("Brynden Rivers"); // unused
        finder.removeWords("Rodrik Freeborn"); // unused
        finder.removeWords("Harys Haigh"); // unused
        finder.removeWords("Harry Sawyer"); // unused
        finder.removeWords("Tytos Lannister"); // unused
        finder.removeWords("Long Tom Costayne"); // unused
        finder.removeWords("The Loves of Queen Nymeria"); // a book
        finder.removeWords("Hand Lew"); // a book

        finder.printCounter().writeLog("src/main/resources/data/characters/ffc-counter.csv");

        // manually add important names that get missed
        finder.addNames("Baelor the Blessed");
        finder.addNames("Vaellyn");

        finder.addNames("Mance Rayder");
        finder.addNames("Hodor");
        finder.addNames("Ygritte");
        finder.addNames("Lem");
        finder.addNames("Grenn");
        finder.addNames("Tom Sevenstrings");
        finder.addNames("Styr");
        finder.addNames("Arstan");
        finder.addNames("Thoros");
        finder.addNames("Craster");
        finder.addNames("Donal Noye");
        finder.addNames("Tormund");
        finder.addNames("Gilly");
        finder.addNames("Hot Pie");
        finder.addNames("Qhorin Halfhand"); // note that in book one, his name was spelled "Quorin"
        finder.addNames("Belwas");
        finder.addNames("Salladhor Saan");
        finder.addNames("Anguy");
        finder.addNames("Shae");
        finder.addNames("Chett");
        finder.addNames("Pyp");
        finder.addNames("Paul");
        finder.addNames("Rattleshirt");
        finder.addNames("Edd Tollett");
        finder.addNames("Harwin");
        finder.addNames("Bowen Marsh");
        finder.addNames("Walton");
        finder.addNames("Jarl");
        finder.addNames("Daario");
        finder.addNames("Satin");
        finder.addNames("Qyburn");
        finder.addNames("Pycelle");
        finder.addNames("Missandei");
        finder.addNames("Nan");
        finder.addNames("Polliver");
        finder.addNames("Rickon");
        finder.addNames("Harma");
        finder.addNames("Tickler");
        finder.addNames("Irri");
        finder.addNames("Greenbeard");
        finder.addNames("Owen");
        finder.addNames("High Septon");
        finder.addNames("Lark");
        finder.addNames("Jhiqui");
        finder.addNames("Clydas");
        finder.addNames("Val");
        finder.addNames("Marillion");
        finder.addNames("Hobb");
        finder.addNames("Urswyck");
        finder.addNames("Rorge");
        finder.addNames("Othell Yarwyck");
        finder.addNames("Bannen");
        finder.addNames("Ryk");
        finder.addNames("Jack-Be-Lucky");
        finder.addNames("Tansy");
        finder.addNames("Viserion");
        finder.addNames("Symon");
        finder.addNames("Butterbumps");
        finder.addNames("Varamyr");
        finder.addNames("Karl");
        finder.addNames("Dywen");
        finder.addNames("Mycah");
        finder.addNames("Dalla");
        finder.addNames("Mero");
        finder.addNames("Raff");
        finder.addNames("Kegs");
        finder.addNames("Dirk");
        finder.addNames("Grey Worm");
        finder.addNames("Tysha");
        finder.addNames("Rossart");
        finder.addNames("Porridge");
        finder.addNames("Utherydes Wayn");
        finder.addNames("Aemon the Dragonknight");
        finder.addNames("Gendel");
        finder.addNames("Shagwell");
        finder.addNames("Notch");
        finder.addNames("Florian");
        finder.addNames("Jack Bulwer");
        finder.addNames("Emmett");
        finder.addNames("Orell");
        finder.addNames("Errok");

        // 10 or less occurrences, but previously relevant
        finder.addNames("Rhaegal");
        finder.addNames("Jaqen");
        finder.addNames("Jhogo");
        finder.addNames("Mully");
        finder.addNames("Rakharo");
        finder.addNames("Syrio Forel");
        finder.addNames("Weeper");
        finder.addNames("Aggo");
        finder.addNames("Patchface");
        finder.addNames("Xaro Xhoan Daxos");
        finder.addNames("Timett");
        finder.addNames("Pyat Pree");
        finder.addNames("Shagga");
        finder.addNames("Mikken");
        finder.addNames("Farlen");
        finder.addNames("Coldhands");
        finder.addNames("Kyra");

        // other additions
        finder.addSurnames("Stokeworth");
        finder.addSurnames("Hewett");
        finder.addSurnames("Rykker");

        finder.addPlaces("Casterly");
        finder.addPlaces("Myr");

        finder.addNondescriptors("Jon");
        finder.addNondescriptors("Robert");
        finder.addNondescriptors("Balon");
        finder.addNondescriptors("Aemon");
        finder.addNondescriptors("Baelor");
        finder.addNondescriptors("Gyles");
        finder.addNondescriptors("Walder");
        finder.addNondescriptors("Ralf");
        finder.addNondescriptors("Quellon");
        finder.addNondescriptors("Denys");
        finder.addNondescriptors("Robin");
        finder.addNondescriptors("Alyn");
        finder.addNondescriptors("Ben");
        finder.addNondescriptors("Humfrey");
        finder.addNondescriptors("Tytos");
        finder.addNondescriptors("Owen");

        // gather names, titles, places, and things
        finder.processCapitalized();

        // manually remove a few names that are either mistakes, duplicates, or unused
        finder.removeNames("Littlefinger");     // as Petyr Baelish
        finder.removeNames("Maid Margaery");    // as Margaery Tyrell
        finder.removeNames("Balon Twice-Crowned");  // as Balon Greyjoy
        finder.removeNames("Aemon Battleborn"); // as Maester Aemon
        finder.removeNames("Aemon Steelsong");  // as Maester Aemon
        finder.removeNames("Baelor Butthole");  // as Bonifer Hasty
        finder.removeNames("Euron Crow");       // as Euron Greyjoy
        finder.removeNames("Lysa Tully");       // as Lysa Arryn
        finder.removeNames("Beloved Baelor");   // as Baelor the Blessed
        finder.removeNames("Bronze Yohn");      // as Yohn Royce
        finder.removeNames("Catelyn Tully");    // as Catelyn Stark
        finder.removeNames("Ned Stark");        // as Eddard Stark
        finder.removeNames("Vinegar Vaellyn");        // as Eddard Stark

        finder.removeNames("Protector");        // unused
        finder.removeNames("Seneschal");        // unused
        finder.removeNames("Horn Hill");        // mistake

        finder.removeNondescriptors("Tywin");

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
        groups.addAliasToGroup("Balon Greyjoy", "Balon", finder.getCount("Balon"));
        groups.addAliasToGroup("Aemon Targaryen", "Aemon", finder.getCount("Aemon"));
        groups.addAliasToGroup("Baelor the Blessed", "Baelor", finder.getCount("Baelor"));
        groups.addAliasToGroup("Gyles Rosby", "Gyles", finder.getCount("Gyles"));
        groups.addAliasToGroup("Black Walder", "Walder", finder.getCount("Walder"));
        groups.addAliasToGroup("Denys Arryn", "Denys", finder.getCount("Denys"));

        // manually combine more character groups
        groups.combineGroups("Jon Snow", "Lord Snow");
        groups.combineGroups("Jon Connington", "Lord Jon");
        groups.combineGroups("Jon Arryn", "Lord Arryn", "Lord Jon");
        groups.combineGroups("Robert Baratheon", "King Robert");
        groups.combineGroups("Robert Arryn", "Lord Robert", "Lord of the Eyrie", "Sweetrobin");
        groups.combineGroups("Balon Greyjoy", "King Balon", "Lord Balon", "Balon the Blessed", "Balon the Brave", "Balon Twice-Crowned", "Balon the Widowmaker", "Balon the Bold");
        groups.combineGroups("Balon Swann", "Ser Balon");
        groups.combineGroups("Maester Aemon", "Aemon Targaryen", "Aemon Battleborn", "Aemon Steelsong");
        groups.combineGroups("Aemon the Dragonknight", "Prince Aemon");
        groups.combineGroups("Baelor the Blessed", "King Baelor the Blessed", "Blessed Baelor", "Beloved Baelor", "King Baelor");
        groups.combineGroups("Gyles Rosby", "Lord Gyles");
        groups.combineGroups("Walder Frey", "Lord Walder", "Lord of the Crossing", "Lord Frey");
        groups.combineGroups("Ralf Stonehouse", "Red Ralf Stonehouse", "Red Ralf");
        groups.combineGroups("Ralf the Limper", "Ralf the Limp");
        groups.combineGroups("Quellon Greyjoy", "Lord Quellon");
        groups.combineGroups("Denys Arryn", "Ser Denys", "Ser Denys Arryn");
        groups.combineGroups("Robin Ryger", "Ser Robin Ryger");
        groups.combineGroups("Owen Inchfield", "Ser Owen Inchfield");

        groups.combineGroups("Samwell Tarly", "Sam");
        groups.combineGroups("Petyr Baelish", "Littlefinger");
        groups.combineGroups("Eddard", "Ned");
        groups.combineGroups("Catelyn", "Lady Stark");
        groups.combineGroups("Joffrey", "Joff");
        groups.combineGroups("Sandor", "Hound");
        groups.combineGroups("Jeor Mormont", "Old Bear", "Commander Mormont", "Lord Mormont");
        groups.combineGroups("Tywin", "Lord of Casterly Rock");
        groups.combineGroups("Roose", "Lord of the Dreadfort", "Lord Bolton");
        groups.combineGroups("Loras", "Knight of Flowers");
        groups.combineGroups("Varys", "Spider");
        groups.combineGroups("Gregor", "Mountain");
        groups.combineGroups("Cersei", "Queen Regent");
        groups.combineGroups("Randyll", "Lord Tarly");
        groups.combineGroups("Mace", "Lord Tyrell");
        groups.combineGroups("Tyrion", "Imp");
        groups.combineGroups("Arya", "Arry", "Squab", "Cat");
        groups.combineGroups("Aerys", "Mad King");
        groups.combineGroups("Paxter", "Lord Redwyne");
        groups.combineGroups("Robb", "Young Wolf");
        groups.combineGroups("Podrick", "Pod");
        groups.combineGroups("Arthur", "Sword of the Morning");
        groups.combineGroups("Jaime Lannister", "Kingslayer");
        groups.combineGroups("Leyton Hightower", "Lord Hightower");
        groups.combineGroups("Hoster Tully", "Lord of Riverrun", "Lord Tully");
        groups.combineGroups("Olenna Tyrell", "Queen of Thorns");
        groups.combineGroups("Mance Rayder", "King-beyond-the-Wall");
        groups.combineGroups("Bonifer", "Baelor Butthole");
        groups.combineGroups("Nym", "Nymeria");
        groups.combineGroups("Harry", "Harrold Hardyng");
        groups.combineGroups("Lewys Piper", "Lew");

        groups.writeNameList("src/main/resources/data/characters/ffc-list-full.txt");
        groups.writeNameList(finder.getNames(), true, 4, "src/main/resources/data/characters/ffc-list-clean.txt");
        groups.writeNameList(finder.getNames(), false, 4, "src/main/resources/data/characters/ffc-list-clean-redundant.txt");

    }
}
