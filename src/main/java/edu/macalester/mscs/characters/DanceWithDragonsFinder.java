package edu.macalester.mscs.characters;

import edu.macalester.mscs.utils.FileUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ari Weiland
 */
public class DanceWithDragonsFinder {

    public static final Set<String> IGNORED_WORDS = new HashSet<>(Arrays.asList(
            "The",
            "My", "She", "He", "His", "Her", "We", "They", "Their", "You", "Your", "It", // pronouns
            "This", "That", "There", // indirect pronouns
            "Who", "Why", "What", "Will", // questions
            "House", "Houses", "Clan", "Lords", "Ladies", "Kings", "Dothraki", "God", // GoT specific
            "Father", "Mother", "Uncle", "Aunt", "Brother", "Brothers", "Sons", "Daughter", "Cousin", // familial references
            "Men", "Man", "And", "Sleepy", "Worship", "Magnificence", "Planky", "Town", "Beyond",
            "Dornish", "Wedding", "Common", "Velvet", "Noble", "Broken", "Star", "Tongue", "Took",
            "Glass", "Winter"//, "Queer" // miscellaneous
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
            "Young", "Old", "Fat", "Big", "Little", "Small", "Bastard", "Boy", "Deaf", "Blind", "Hero", // endearing titles
            "High", "Great", "Grand", "First", "Second", // superlatives
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", // numbers
            "Black", "Red", "Green", "Blue", "White", "Grey", "Brown", "Yellow", // colors
            "Land", "Lands", "Sea", "Seas", "Island", "Islands", "Isles", "Bay", "River", "Shore", "Point",
            "Lake", "Hills", "Straits", "Vale", // geographics
            "City", "Cities", "Alley", "Gate", "Keep", "Market", "Tower", "Hall", "Rock", "Castle", "Lane",
            "Bridge", "Sept", "Harbor", // landmarks
            "Cruel", "Bold", "Brave", "Good", "Strong", "Bitter", "Sweet", "Bad", "Clever", "Cautious",
            "Wise", "Craven", "Poor", "Pretty", "Scared", "Homeless", "Hot", "Shy", "True", "Mad", "Blessed",
            "Queer", "Sour", "Cunning", "Hairy", // adjective titles
            "Bear", "Iron", "Beggar", "Whore", "Wench", "Grandfather", "Water", "Crow", "Wolf", "Shepherd",
            "Dance", "Butcher", "Grass", "Belly", "Stalwart", "Beasts", "Cunt", "Pig" // miscellaneous
    ));

    // Words that are sometimes placed between first and last names
    public static final Set<String> FILLER_WORDS = new HashSet<>(Arrays.asList("zo", "mo"));

    public static void main(String[] args) {
        // initialize the finder
        CharacterFinder finder = new CharacterFinder(IGNORED_WORDS, TITLE_WORDS, GENERAL_WORDS, FILLER_WORDS, ".?!�");
        // read in the text
        finder.countCapitalized(FileUtils.readFile("src/main/resources/text/dancewithdragons.txt"));
        // fix a few mistakes
        finder.incrementWord("Petyr Baelish", 2);
        finder.incrementWord("Petyr", 0);
        finder.incrementWord("Jeor Mormont", 0);
        finder.removeWords("Reek Ramsay");
        finder.removeWords("Summer Islander King Robert");
        finder.removeWords("Pyke Lord Dagon");
        finder.removeWords("Jon of Hodor");
        finder.removeWords("Tyrion of Bronn");
        finder.removeWords("Rhodry Lord Cerwyn");
        finder.removeWords("Henly Lord Slate");
        finder.removeWords("Hugor of the Hill"); // unused
        finder.removeWords("Ned Woods"); // unused
        finder.removeWords("Tytos Lannister"); // unused
        finder.removeWords("Hot Harry Merrell"); // unused
        finder.removeWords("Merrell"); // unused

        finder.printCounter().writeLog("src/main/resources/data/characters/dwd-counter.csv");

        // manually add important names that get missed
        finder.addNames("Green Grace");
        finder.addNames("Tommen");
        finder.addNames("Ben Bones");
        finder.addNames("Mance Rayder");
        finder.addNames("Hodor");
        finder.addNames("Edd Tollett");
        finder.addNames("Missandei");
        finder.addNames("Belwas");
        finder.addNames("Tattered Prince");
        finder.addNames("Jeyne Poole");
        finder.addNames("Irri");
        finder.addNames("Othell Yarwyck");
        finder.addNames("Moqorro");
        finder.addNames("Grey Worm");
        finder.addNames("Meris");
        finder.addNames("Yandry");
        finder.addNames("Xaro Xhoan Daxos");
        finder.addNames("Areo Hotah");
        finder.addNames("Dick Straw");
        finder.addNames("Joffrey");
        finder.addNames("Jhiqui");
        finder.addNames("Wun");
        finder.addNames("Emmett");
        finder.addNames("Gilly");
        finder.addNames("Nurse");
        finder.addNames("Khrazz");
        finder.addNames("Rattleshirt");
        finder.addNames("Holly");
        finder.addNames("Rowan");
        finder.addNames("Ysilla");
        finder.addNames("Mully");
        finder.addNames("Haggon");
        finder.addNames("Caggo");
        finder.addNames("Pycelle");
        finder.addNames("Garth");
        finder.addNames("Bloodbeard");
        finder.addNames("Symon Stripeback");
        finder.addNames("Alyn");
        finder.addNames("High Septon");
        finder.addNames("Hobb");
        finder.addNames("Barsena");
        finder.addNames("Coldhands");
        finder.addNames("Weeper");
        finder.addNames("Aeron");
        finder.addNames("Damon");
        finder.addNames("Hugh Hungerford");
        finder.addNames("Rhaegal");
        finder.addNames("Benerro");
        finder.addNames("Rakharo");
        finder.addNames("Skinner");
        finder.addNames("Tysha");
        finder.addNames("Hagen");
        finder.addNames("Aggo");
        finder.addNames("Jhogo");
        finder.addNames("Thistle");
        finder.addNames("Qezza");
        finder.addNames("Nan");
        finder.addNames("Qavo Nogarys");
        finder.addNames("Donal Noye");
        finder.addNames("Leaf");
        finder.addNames("Squirrel");
        finder.addNames("Kasporio");
        finder.addNames("Goghor");
        finder.addNames("Belaquo");
        finder.addNames("Balaq");
        finder.addNames("Cromm");
        finder.addNames("Pynto");
        finder.addNames("Gerrick");
        finder.addNames("Petyr Baelish");
        finder.addNames("Wick Whittlestick");
        finder.addNames("Torgon Greyiron");
        finder.addNames("Marselen");
        finder.addNames("Sigorn");
        finder.addNames("Rory");
        finder.addNames("Bump");
        finder.addNames("Stalwart Shield");
        finder.addNames("Grimtongue");
        finder.addNames("Owen");
        finder.addNames("Kyra");

        // 10 or less occurrences, but previously relevant
        finder.addNames("Shae");
        finder.addNames("Patchface");
        finder.addNames("Craster");
        finder.addNames("Dywen");
        finder.addNames("Dagmer Cleftjaw");
        finder.addNames("Lorren");
        finder.addNames("Rickon");
        finder.addNames("Qhorin Halfhand"); // note that in book one, his name was spelled "Quorin"
        finder.addNames("Grenn");
        finder.addNames("Mikken");
        finder.addNames("Wex");

        finder.addSurnames("Merryweather");
        finder.addSurnames("Whent");

        finder.addPlaces("Mander");

        finder.addNondescriptors("Jon");
        finder.addNondescriptors("Robert");
        finder.addNondescriptors("Brandon");
        finder.addNondescriptors("Balon");
        finder.addNondescriptors("Roose");
        finder.addNondescriptors("Rhaegar");
        finder.addNondescriptors("Rodrik");
        finder.addNondescriptors("Rickard");
        finder.addNondescriptors("Hoster");
        finder.addNondescriptors("Artos");
        finder.addNondescriptors("Ben");
        finder.addNondescriptors("Qarl");
        finder.addNondescriptors("Jack");
        finder.addNondescriptors("Ralf");
        finder.addNondescriptors("Dick");

        // gather names, titles, places, and things
        finder.processCapitalized();

        // manually remove a few names that are either mistakes, duplicates, or unused
        finder.removeNames("Ned Stark");      // as Eddard Stark
        finder.removeNames("Jon Stark");      // as Jon Snow
        finder.removeNames("Daenerys Stormborn"); // as Daenerys Targaryen
        finder.removeNames("Frog");           // as Quentyn Martell
        finder.removeNames("Arya Horseface"); // as Quentyn Martell
        finder.removeNames("Arya Underfoot"); // as Quentyn Martell
        finder.removeNames("Joff");           // as Joffrey Baratheon
        finder.removeNames("Sam Tarly");      // as Samwell Tarly
        finder.removeNames("Ben Stark");      // as Benjen Stark
        finder.removeNames("Theon Stark");    // as Theon Greyjoy
        finder.removeNames("Theon Turncloak");// as Theon Greyjoy
        finder.removeNames("Brynden Blackfish");  // as Brynden Tully
        finder.removeNames("Davos Shorthand");// as Davos Seaworth
        finder.removeNames("Littlefinger");   // as Petyr Baelish
        finder.removeNames("Paramount");      // as Petyr Baelish
        finder.removeNames("Reaper");         // as Balon Greyjoy
        finder.removeNames("Eunuch");         // as Varys
        finder.removeNames("Varamyr Threeskins"); // as Varamyr Sixskins
        finder.removeNames("Godry Giantslayer");  // as Godry Farring
        finder.removeNames("Mad Aerys");      // as Aerys Targaryen
        finder.removeNames("Whoresbane Umber");   // as Hother Umber
        finder.removeNames("Hizdahr Loraq");  // as Hizdahr zo Loraq
        finder.removeNames("Middle Liddle");  // as Morgan Liddle
        finder.removeNames("Yellowbelly");    // as Yezzan
        finder.removeNames("Lard");           // as Wyman Manderly
        finder.removeNames("Imp");            // as Tyrion Lannister
        finder.removeNames("Hugor Hill");     // as Tyrion Lannister
        finder.removeNames("Hugor Halfmaester");  // as Tyrion Lannister
        finder.removeNames("Ramsay Bolton");  // as Ramsay Snow
        finder.removeNames("Duck");           // as Rolly Duckfield
        finder.removeNames("Crowfood Umber"); // as Mors Umber

        finder.removeNames("Eyed Maid");      // unused
        finder.removeNames("Lu");             // named axe
        finder.removeNames("Pounce");         // a cat
        finder.removeNames("Fiery Hand");     // mistake
        finder.removeNames("Hoarfrost Hill"); // mistake
        finder.removeNames("Horn Hill");      // mistake

        finder.removeNondescriptors("Walder", "Aegon");

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
        groups.addAliasToGroup("Arya", "Cat", finder.getCount("Cat"));
        groups.addAliasToGroup("Robert Baratheon", "Robert", finder.getCount("Robert"));
        groups.addAliasToGroup("Rhaegar Targaryen", "Rhaegar", finder.getCount("Rhaegar"));
        groups.addAliasToGroup("Hoster Blackwood", "Hoster", finder.getCount("Hoster"));

        // manually combine more character groups
        groups.combineGroups("Jon Snow", "Lord Snow", "Lord Crow");
        groups.combineGroups("Jon Connington", "Griff", "Lord Connington", "Lord Jon", "Lord of Griffin");
        groups.combineGroups("Arya", "Arry", "Cat of the Canals");
        groups.combineGroups("Robert Baratheon", "Usurper", "King Robert");
        groups.combineGroups("Robert Strong", "Ser Robert");
        groups.combineGroups("Bran", "Brandon Stark");
        groups.combineGroups("Balon Greyjoy", "Reaper", "Lord of the Iron Islands", "Lord of Pyke", "Lord Balon");
        groups.combineGroups("Balon Swann", "Ser Balon");
        groups.combineGroups("Roose Bolton", "Lord of the Dreadfort", "Lord Bolton", "Lord Roose");
        groups.combineGroups("Rhaegar Targaryen", "Prince Rhaegar");
        groups.combineGroups("Rodrik Cassel", "Ser Rodrik");
        groups.combineGroups("Rodrik Ryswell", "Lord Ryswell");
        groups.combineGroups("Rodrik the Reader", "Lord Rodrik");
        groups.combineGroups("Hoster Blackwood", "Hos");
        groups.combineGroups("Artos Stark", "Artos the Implacable");
        groups.combineGroups("Ben Plumm", "Brown Ben", "Brown Ben Plumm");
        groups.combineGroups("Eddard", "Ned", "Lord Stark");
        groups.combineGroups("Petyr", "Littlefinger");
        groups.combineGroups("Daenerys", "Dany", "Khaleesi");
        groups.combineGroups("Joffrey", "Joff");
        groups.combineGroups("Samwell", "Sam");
        groups.combineGroups("Sandor", "Hound");
        groups.combineGroups("Jeor Mormont", "Old Bear", "Lord Commander Mormont", "Lord Mormont");
        groups.combineGroups("Pycelle", "Grand Maester");
        groups.combineGroups("Lysa", "Lady Arryn", "Lady of the Eyrie");
        groups.combineGroups("Tywin", "Lord of Casterly Rock", "Lord Lannister");
        groups.combineGroups("Loras", "Knight of Flowers");
        groups.combineGroups("Davos", "Onion Knight", "Lord Seaworth");
        groups.combineGroups("Varys", "Spider");
        groups.combineGroups("Gregor", "Mountain");
        groups.combineGroups("Theon", "Reek");
        groups.combineGroups("Cersei", "Queen Regent", "Queen Dowager");
        groups.combineGroups("Rattleshirt", "Lord of Bones");
        groups.combineGroups("Wyman", "Lord of White Harbor", "Lord Lard", "Lord Manderly", "Lord Pig");
        groups.combineGroups("Randyll", "Lord Tarly");
        groups.combineGroups("Mace", "Lord Tyrell");
        groups.combineGroups("Stannis", "Lord of Dragonstone");
        groups.combineGroups("Rickard Karstark", "Lord Karstark", "Lord Rickard");
        groups.combineGroups("Janos", "Lord Slynt");
        groups.combineGroups("Quentyn", "Frog");
        groups.combineGroups("Rolly", "Duck");
        groups.combineGroups("Tyrion", "Imp", "Hugor", "Yollo");
        groups.combineGroups("Hother", "Whoresbane");
        groups.combineGroups("Nymeria", "Nym");
        groups.combineGroups("Barristan", "Ser Grandfather");
        groups.combineGroups("Ghazdor", "Wobblecheeks");
        groups.combineGroups("Morgan", "Middle Liddle");
        groups.combineGroups("Yezzan", "Yellowbelly");
        groups.combineGroups("Aerys", "Mad King");
        groups.combineGroups("Tattered Prince", "Tatters", "Rags");
        groups.combineGroups("Mance Rayder", "Abel");
        groups.combineGroups("Barbrey", "Lady Dustin", "Lady of Barrowton");
        groups.combineGroups("Ben Plumm", "Brown Ben");
        groups.combineGroups("Varamyr", "Lump");
        groups.combineGroups("Salladhor", "Salla");
        groups.combineGroups("Aegor", "Bittersteel");
        groups.combineGroups("Cleon", "Butcher King");
        groups.combineGroups("Tytos Blackwood", "Lord of Raventree");
        groups.combineGroups("Godric", "Lord Borrell", "Lord of Sweetsister");
        groups.combineGroups("Harwood Stout", "Lord Stout");
        groups.combineGroups("Paxter", "Lord Redwyne", "Lord of the Arbor");
        groups.combineGroups("Brandon Norrey", "Lord Norrey");
        groups.combineGroups("Jonos", "Lord Bracken");
        groups.combineGroups("Tytos Blackwood", "Lord Blackwood");
        groups.combineGroups("Lord Hornwood", "Lord of the Hornwood");
        groups.combineGroups("Willam", "Lord Dustin");
        groups.combineGroups("Shrouded Lord", "Prince of Sorrows");
        groups.combineGroups("Sansa", "Lady Lannister");
        groups.combineGroups("Sybelle", "Lady Glover");
        groups.combineGroups("Melisandre", "Lady Red");
        groups.combineGroups("Arnolf", "Lord of Karhold");
        groups.combineGroups("Triston", "Lord of the Three Sisters");
        groups.combineGroups("Bowen Marsh", "Lord Steward");

        groups.writeNameList("src/main/resources/data/characters/dwd-list-full2.txt");
        groups.writeNameList(finder.getNames(), true, 2, "src/main/resources/data/characters/dwd-list-clean2.txt");
        groups.writeNameList(finder.getNames(), false, 2, "src/main/resources/data/characters/dwd-list-clean-redundant2.txt");

    }
}
