package edu.macalester.mscs.characters;

import edu.macalester.mscs.utils.FileUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ari Weiland
 */
public class StormOfSwordsFinder {

    public static final Set<String> IGNORED_WORDS = new HashSet<>(Arrays.asList(
            "My", "She", "He", "His", "Her", "We", "They", "Their", "You", "Your", "It", // pronouns
            "This", "That", "There", // indirect pronouns
            "Who", "Why", "What", "Will", "Was", // questions
            "House", "Houses", "Clan", "Lords", "Ladies", "Kings", "Dothraki", "Grace", "God", // GoT specific
            "Father", "Mother", "Uncle", "Aunt", "Brother", "Brothers", "Sons", "Daughter", "Cousin", // familial references
            "Men", "Man", "And", "With", "Griffin", "No", "Summer", "Half", "Tongue", "Without",
            "People", "Took", "Ther" // miscellaneous
    ));

    // Words that are not unique, but may still be descriptive, expecially in combination
    public static final Set<String> TITLE_WORDS = new HashSet<>(Arrays.asList(
            "Lord", "Lady", "King", "Queen", "Regent", "Steward", "Prince", "Princess", "Triarch", // royal titles
            "Ser", "Maester", "Captain", "Commander", "Magister", "Master", "Builder", "Knight",
            "Septon", "Septa", "Shipwright", "Goodwife", "Ranger", "Squire", "Admiral", // professional titles
            "Khal", "Ko" // dothraki titles
    ));

    // Words that are not unique, but may still be descriptive, expecially in combination
    public static final Set<String> GENERAL_WORDS = new HashSet<>(Arrays.asList(
            "The", // titular articles
            "Young", "Old", "Fat", "Big", "Little", "Small", "Bastard", "Boy", "Deaf", "Blind", "Hero", // endearing titles
            "High", "Great", "Grand", "First", "Second", "Third", // superlatives
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", // numbers
            "Black", "Red", "Green", "Blue", "White", "Grey", "Brown", "Yellow", "Silver", // colors
            "Land", "Lands", "Sea", "Seas", "Island", "Islands", "Isle", "Isles", "Bay", "River", "Shore",
            "Point", "Lake", "Hills", "Straits", "Vale", "Wood", // geographics
            "City", "Cities", "Alley", "Gate", "Keep", "Market", "Tower", "Hall", "Rock", "Castle", "Lane",
            "Bridge", "Sept", "Harbor", "Mill", // landmarks
            "Cruel", "Bold", "Brave", "Good", "Strong", "Bitter", "Sweet", "Bad", "Clever", "Cautious",
            "Wise", "Craven", "Poor", "Pretty", "Scared", "Homeless", "Hot", "Shy", "True", "Mad", "Blessed",
            "Queer", "Sour", "Cunning", "Hairy", "Broken", "Bloody", "Late", "Fair", // adjective titles
            "Conqueror", "Wolf", "Fool", "Iron", "Worm", "Bull", "Kingswood", "Sword", "Nymeros", "Wife",
            "Bar", "Dornish", "Dornishman", "Spare" // miscellaneous
    ));

    // Words that are sometimes placed between first and last names
    public static final Set<String> FILLER_WORDS = new HashSet<>(Arrays.asList("zo", "mo", "na"));

    public static void main(String[] args) {
        // initialize the finder
        CharacterFinder finder = new CharacterFinder(IGNORED_WORDS, TITLE_WORDS, GENERAL_WORDS, FILLER_WORDS, ".?!�");
        // read in the text
        finder.countCapitalized(FileUtils.readFile("src/main/resources/text/stormofswords.txt"));
        // fix a few mistakes
        finder.removeWords("Robb Starks");
        finder.removeWords("Lysa of Bran");
        finder.removeWords("Ned Dayne"); // unused
        finder.removeWords("Ser Denys Arryn"); // unused
        finder.removeWords("Edric Dayne"); // unused
        finder.removeWords("Mad Marq Rankenfell"); // unused
        finder.removeWords("Fat Tom"); // unused
        finder.removeWords("Samwell Spicer"); // unused
        
        finder.printCounter().writeLog("src/main/resources/data/characters/sos-counter.csv");

        // manually add important names that get missed
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
        finder.addNames("Baelor the Blessed");
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

        finder.addSurnames("Cerwyn");
        finder.addSurnames("Merryweather");
        finder.addSurnames("Mooton");
        finder.addSurnames("Bulwer");
        finder.addSurnames("Roote");
        finder.addSurnames("Blackfyre");

        finder.addPlaces("Casterly");
        finder.addPlaces("Tarth");
        finder.addPlaces("Hedge");

        finder.addNondescriptors("Jon");
        finder.addNondescriptors("Brandon");
        finder.addNondescriptors("Robert");
        finder.addNondescriptors("Petyr");
        finder.addNondescriptors("Walder");
        finder.addNondescriptors("Aemon");
        finder.addNondescriptors("Lothar");
        finder.addNondescriptors("Jeyne");
        finder.addNondescriptors("Balon");
        finder.addNondescriptors("Ben");
        finder.addNondescriptors("Robin");
        finder.addNondescriptors("Donnel");
        finder.addNondescriptors("Baelor");
        finder.addNondescriptors("Walda");
        finder.addNondescriptors("Willem");
        finder.addNondescriptors("Martyn");
        finder.addNondescriptors("Rodrik");
        finder.addNondescriptors("Tytos");
        finder.addNondescriptors("Garth");
        finder.addNondescriptors("Lucas");
        finder.addNondescriptors("Tim");
        finder.addNondescriptors("Daemon");
        finder.addNondescriptors("Myles");

        // gather names, titles, places, and things
        finder.processCapitalized();

        // manually remove a few names that are either mistakes, duplicates, or unused
        finder.removeNames("Jon Stark");      // as Jon Snow
        finder.removeNames("Sam Tarly");      // as Samwell Tarly
        finder.removeNames("Piggy");      // as Samwell Tarly
        finder.removeNames("Joff");           // as Joffrey Baratheon
        finder.removeNames("Brandon Stark");  // as Bran
        finder.removeNames("Petyr Littlefinger"); // as Petyr Baelish
        finder.removeNames("Ned Stark");      // as Eddard Stark
        finder.removeNames("Lysa Tully");     // as Lysa Arryn
        finder.removeNames("Brynden Blackfish");  // as Brynden Tully
        finder.removeNames("Ben Stark");      // as Benjen Stark
        finder.removeNames("Merrett Muttonhead"); // as Merrett Frey
        finder.removeNames("Baelor Brightsmile"); // as Baelor Hightower
        finder.removeNames("Baelor Breakwind");   // as Baelor Hightower
        finder.removeNames("Roslin Tully");   // as Roslin Frey
        finder.removeNames("Walda Frey");     // as Walda Bolton
        finder.removeNames("Grandfather");    // as Walder Frey
        finder.removeNames("Puff Fish");      // as Mace Tyrell
        finder.removeNames("Scab");           // as Aerys Targaryen
        finder.removeNames("Onions");         // as Davos
        finder.removeNames("Wife");           // as Sansa
        finder.removeNames("Aerion Brightfire");  // as Aerion Brightflame
        finder.removeNames("Jon Darry");      // as Jonothor Darry
        finder.removeNames("Jaime Jaime");    // as Jaime Lannister

        finder.removeNames("Soldier");        // a doll
        finder.removeNames("Paramount");      // a generic title
        finder.removeNames("Rickard Stark");  // unused
        finder.removeNames("Horn Hill");      // mistake
        finder.removeNames("Maiden Fair");    // mistake

        finder.removeNondescriptors("Blackfish");

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
        groups.addAliasToGroup("Aemon Targaryen", "Aemon", finder.getCount("Aemon"));
        groups.addAliasToGroup("Lothar Frey", "Lothar", finder.getCount("Lothar"));
        groups.addAliasToGroup("Jeyne Westerling", "Jeyne", finder.getCount("Jeyne"));
        groups.addAliasToGroup("Baelor the Blessed", "Baelor", finder.getCount("Baelor"));
        groups.addAliasToGroup("Willem Lannister", "Willem", finder.getCount("Willem"));
        groups.addAliasToGroup("Martyn Lannister", "Martyn", finder.getCount("Martyn"));

        // manually combine more character groups
        groups.combineGroups("Jon Snow", "Lord Snow", "Jon Stark");
        groups.combineGroups("Jon Connington", "Lord Connington", "Lord Jon");
        groups.combineGroups("Jon Arryn", "Lord Arryn");
        groups.combineGroups("Jon Darry", "Jonothor Darry");
        groups.combineGroups("Bran", "Brandon Stark");
        groups.combineGroups("Samwell Tarly", "Sam", "Piggy");
        groups.combineGroups("Robert Baratheon", "Usurper", "King Robert");
        groups.combineGroups("Robert Arryn", "Lord Robert", "Lord of the Eyrie");
        groups.combineGroups("Petyr Baelish", "Littlefinger", "Lord Petyr");
        groups.combineGroups("Walder Frey", "Lord Walder", "Lord of the Crossing", "Lord Frey", "Lord Grandfather");
        groups.combineGroups("Aemon Targaryen", "Maester Aemon");
        groups.combineGroups("Aemon the Dragonknight", "Prince Aemon");
        groups.combineGroups("Lothar Frey", "Lame Lothar");
        groups.combineGroups("Jeyne Westerling", "Queen Jeyne");
        groups.combineGroups("Balon Greyjoy", "King Balon", "Lord Balon");
        groups.combineGroups("Balon Swann", "Ser Balon");
        groups.combineGroups("Robin Ryger", "Ser Robin");
        groups.combineGroups("Donnel Hill", "Sweet Donnel");
        groups.combineGroups("Baelor the Blessed", "King Baelor");
        groups.combineGroups("Baelor Hightower", "Baelor Brightsmile", "Baelor Breakwind");
        groups.combineGroups("Walda Bolton", "Lady Walda", "Fat Walda", "Walda Frey", "Lady Bolton");
        groups.combineGroups("Rodrik Cassel", "Ser Rodrik");
        groups.combineGroups("Tytos Lannister", "Lord Tytos");
        groups.combineGroups("Garth of Greenaway", "Garth Greenaway");
        groups.combineGroups("Myles Mooton", "Ser Myles");
        groups.combineGroups("Eddard", "Ned");
        groups.combineGroups("Catelyn", "Cat", "Lady Stark");
        groups.combineGroups("Daenerys", "Dany", "Khaleesi");
        groups.combineGroups("Joffrey", "Joff", "Robert the Second");
        groups.combineGroups("Sandor", "Hound");
        groups.combineGroups("Jeor Mormont", "Old Bear", "Commander Mormont", "Lord Mormont");
        groups.combineGroups("Lysa", "Lady Arryn", "Lady of the Eyrie");
        groups.combineGroups("Tywin", "Lord of Casterly Rock");
        groups.combineGroups("Roose", "Lord of the Dreadfort", "Lord Bolton");
        groups.combineGroups("Loras", "Knight of Flowers");
        groups.combineGroups("Davos", "Onion Knight", "Ser Onions");
        groups.combineGroups("Varys", "Spider");
        groups.combineGroups("Gregor", "Mountain");
        groups.combineGroups("Cersei", "Queen Regent");
        groups.combineGroups("Rattleshirt", "Lord of Bones");
        groups.combineGroups("Randyll", "Lord Tarly");
        groups.combineGroups("Mace", "Lord Tyrell", "Puff Fish");
        groups.combineGroups("Stannis", "Lord of Dragonstone");
        groups.combineGroups("Rickard Karstark", "Lord Karstark");
        groups.combineGroups("Janos", "Lord Slynt");
        groups.combineGroups("Tyrion", "Imp");
        groups.combineGroups("Hother", "Whoresbane");
        groups.combineGroups("Arya", "Arry", "Squab");
        groups.combineGroups("Aerys", "Mad King", "King Scab");
        groups.combineGroups("Ben Plumm", "Brown Ben");
        groups.combineGroups("Salladhor", "Salla");
        groups.combineGroups("Paxter", "Lord Redwyne");
        groups.combineGroups("Jonos", "Lord Bracken");
        groups.combineGroups("Tytos Blackwood", "Lord Blackwood");
        groups.combineGroups("Robb", "Young Wolf", "King of the Trident");
        groups.combineGroups("Benjen", "Ben Stark");
        groups.combineGroups("Podrick", "Pod");
        groups.combineGroups("Gerold", "White Bull");
        groups.combineGroups("Arthur", "Sword of the Morning");
        groups.combineGroups("Cley Cerwyn", "Lord Cerwyn");
        groups.combineGroups("Sansa Stark", "Lady Wife", "Lady of Winterfell");
        groups.combineGroups("Jaime Lannister", "Kingslayer");
        groups.combineGroups("Maege Mormont", "Lady Mormont");
        groups.combineGroups("Mathis Rowan", "Lord of Goldengrove", "Lord Rowan");
        groups.combineGroups("Tremond Gargalen", "Lord Gargalen");
        groups.combineGroups("Alester Florent", "Lord Florent");
        groups.combineGroups("Jason Mallister", "Lord of Seagard", "Lord Mallister");
        groups.combineGroups("Selwyn", "Lord of Evenfall");
        groups.combineGroups("Leyton Hightower", "Lord Hightower");
        groups.combineGroups("Lymond Goodbrook", "Lord Goodbrook");
        groups.combineGroups("Hoster Tully", "Lord of Riverrun", "Lord Tully");
        groups.combineGroups("Greatjon Umber", "Lord Umber");
        groups.combineGroups("Alerie", "Lady Tyrell");
        groups.combineGroups("Roslin Frey", "Lady Tully", "Roslin Tully");
        groups.combineGroups("Dontos Hollard", "Ser Fool");
        groups.combineGroups("Olenna Tyrell", "Queen of Thorns");
        groups.combineGroups("Oberyn Martell", "Red Viper");
        groups.combineGroups("Pello", "Greenbeard");
        groups.combineGroups("Mance Rayder", "King-beyond-the-Wall");

        groups.writeNameList("src/main/resources/data/characters/sos-list-full.txt");
        groups.writeNameList(finder.getNames(), true, 4, "src/main/resources/data/characters/sos-list-clean.txt");

    }
}
