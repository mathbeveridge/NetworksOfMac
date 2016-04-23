package edu.macalester.mscs.characters;

import edu.macalester.mscs.utils.FileUtils;
import edu.macalester.mscs.utils.WordUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ari Weiland
 */
public class DanceWithDragons {

    public static final Set<String> IGNORED_WORDS = new HashSet<>(Arrays.asList(
            "My", "She", "He", "His", "Her", "We", "They", "Their", "You", "Your", "It", // pronouns
            "This", "That", "There", // indirect pronouns
            "Who", "Why", "What", "Will", // questions
            "House", "Houses", "Clan", "Lords", "Ladies", "Kings", "Dothraki", "Grace", "God", // GoT specific
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
            "The", // titular articles
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
            "Dance", "Butcher" // miscellaneous
    ));

    // Words that are sometimes placed between first and last names
    public static final Set<String> FILLER_WORDS = new HashSet<>(Arrays.asList("zo", "mo"));

    public static void main(String[] args) {
        // initialize the finder
        CharacterFinder finder = new CharacterFinder(IGNORED_WORDS, TITLE_WORDS, GENERAL_WORDS, FILLER_WORDS, ".?!�");
        // read in the text
        finder.countCapitalized(FileUtils.readFile("src/main/resources/text/dancewithdragons.txt"));
        // fix a few mistakes
        finder.incrementName("Petyr Baelish", 2);
        finder.incrementName("Petyr", 0);
        finder.incrementName("Jeor Mormont", 0);
        finder.removeWords("Reek Ramsay");
        finder.removeWords("Summer Islander King Robert");
        finder.removeWords("Pyke Lord Dagon");
        finder.removeWords("Jon of Hodor");
        finder.removeWords("Rhodry Lord Cerwyn");
        finder.removeWords("Henly Lord Slate");

        // gather names, titles, places, and things
        Set<String> titledNames = finder.getTitledNames();
        Set<String> pluralizedNames = finder.getPluralizedNames();
        pluralizedNames.remove("Walder");
        pluralizedNames.remove("Cat");
        pluralizedNames.remove("Aegon");
        Set<String> surnames = finder.getSurnames();
        Set<String> names = finder.getNamesBySurname(surnames);
        names.addAll(titledNames);
        Set<String> places = finder.getPlaces(names);
//        places.add("Vale");
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
        finder.removeWordsBelowThreshold(lonely, 1);

        finder.printCounter().writeLog("src/main/resources/data/characters/dwd-counter.csv");

        // gather phrases that are not inherently descriptive
        Set<String> nondescriptors = new HashSet<>();
        nondescriptors.addAll(pluralizedNames);
        nondescriptors.addAll(WordUtils.getPlurals(pluralizedNames));
        nondescriptors.addAll(surnames);
        nondescriptors.addAll(WordUtils.getPlurals(surnames));
        nondescriptors.addAll(places);
        nondescriptors.add("Qarl");
        nondescriptors.add("Jack");
        nondescriptors.add("Ralf");
        nondescriptors.add("Dick");

        // build character groups
        finder.buildCharacterGroups(nondescriptors);

        // manually combine more character groups
        finder.combineGroups("Robert Baratheon", "Usurper");
        finder.combineGroups("Eddard", "Ned");
        finder.combineGroups("Jon", "Lord Snow", "Lord Crow");
        finder.combineGroups("Bran", "Brandon Stark");
        finder.combineGroups("Petyr", "Littlefinger");
        finder.combineGroups("Daenerys", "Dany", "Khaleesi");
        finder.combineGroups("Joffrey", "Joff");
        finder.combineGroups("Samwell", "Sam");
        finder.combineGroups("Sandor", "Hound");
        finder.combineGroups("Jeor Mormont", "Old Bear", "Commander Mormont", "Lord Mormont");
        finder.combineGroups("Pycelle", "Grand Maester");
        finder.combineGroups("Lysa", "Lady Arryn", "Lady of the Eyrie");
        finder.combineGroups("Tywin", "Lord of Casterly Rock", "Lord Lannister");
        finder.combineGroups("Roose", "Lord of the Dreadfort", "Lord Bolton");
        finder.combineGroups("Loras", "Knight of Flowers");
        finder.combineGroups("Davos", "Onion Knight", "Lord Seaworth");
        finder.combineGroups("Varys", "Spider");
        finder.combineGroups("Gregor", "Mountain");
        finder.combineGroups("Balon Greyjoy", "Reaper", "Lord of the Iron Islands", "Lord of Pyke");
        finder.combineGroups("Theon", "Reek");
        finder.combineGroups("Cersei", "Queen Regent");
        finder.combineGroups("Rattleshirt", "Lord of Bones");
        finder.combineGroups("Wyman", "Lord of White Harbor", "Lord Lard", "Lord Manderly");
        finder.combineGroups("Randyll", "Lord Tarly");
        finder.combineGroups("Mace", "Lord Tyrell");
        finder.combineGroups("Stannis", "Lord of Dragonstone");
        finder.combineGroups("Rickard Karstark", "Lord Karstark");
        finder.combineGroups("Janos", "Lord Slynt");
        finder.combineGroups("Quentyn", "Frog");
        finder.combineGroups("Rolly", "Duck");
        finder.combineGroups("Jon Connington", "Griff", "Lord Connington");
        finder.combineGroups("Tyrion", "Imp", "Hugor", "Yollo");
        finder.combineGroups("Hother", "Whoresbane");
        finder.combineGroups("Nymeria", "Nym");
        finder.combineGroups("Barristan", "Ser Grandfather");
        finder.combineGroups("Ghazdor", "Wobblecheeks");
        finder.combineGroups("Arya", "Arry", "Cat");
        finder.combineGroups("Morgan", "Middle Liddle");
        finder.combineGroups("Yezzan", "Yellowbelly");
        finder.combineGroups("Aerys", "Mad King");
        finder.combineGroups("Tattered Prince", "Tatters", "Rags");
        finder.combineGroups("Mance Rayder", "Abel");
        finder.combineGroups("Barbrey", "Lady Dustin", "Lady of Barrowton");
        finder.combineGroups("Ben Plumm", "Brown Ben");
        finder.combineGroups("Varamyr", "Lump");
        finder.combineGroups("Salladhor", "Salla");
        finder.combineGroups("Aegor", "Bittersteel");
        finder.combineGroups("Cleon", "Butcher King");
        finder.combineGroups("Tytos Blackwood", "Lord of Raventree");
        finder.combineGroups("Godric", "Lord Borrell", "Lord of Sweetsister");
        finder.combineGroups("Harwood Stout", "Lord Stout");
        finder.combineGroups("Paxter", "Lord Redwyne");
        finder.combineGroups("Rodrik Ryswell", "Lord Ryswell");
        finder.combineGroups("Brandon Norrey", "Lord Norrey");
        finder.combineGroups("Jonos", "Lord Bracken");
        finder.combineGroups("Tytos Blackwood", "Lord Blackwood");
        finder.combineGroups("Lord Hornwood", "Lord of the Hornwood");
        finder.combineGroups("Willam", "Lord Dustin");
        finder.combineGroups("Shrouded Lord", "Prince of Sorrows");
        finder.combineGroups("Sansa", "Lady Lannister");
        finder.combineGroups("Sybelle", "Lady Glover");
        finder.combineGroups("Melisandre", "Lady Red");

        // manually add important names that get missed
        names.add("Ned Woods");
        names.add("Ben Bones");
        names.add("Mance Rayder");
        names.add("Hodor");
        names.add("Edd Tollett");
        names.add("Missandei");
        names.add("Varamyr");
        names.add("Belwas");
        names.add("Tattered Prince");
        names.add("Jeyne Poole");
        names.add("Irri");
        names.add("Othell Yarwyck");
        names.add("Moqorro");
        names.add("Grey Worm");
        names.add("Meris");
        names.add("Yandry");
        names.add("Xaro Xhoan Daxos");
        names.add("Aero Hotah");
        names.add("Dick Straw");
        names.add("Joffrey");
        names.add("Jhiqui");
        names.add("Wun");
        names.add("Emmett");
        names.add("Gilly");
        names.add("Nurse");
        names.add("Khrazz");
        names.add("Rattleshirt");
        names.add("Holly");
        names.add("Rowan");
        names.add("Ysilla");
        names.add("Mully");
        names.add("Haggon");
        names.add("Caggo");
        names.add("Pycelle");
        names.add("Garth");
        names.add("Bloodbeard");
        names.add("Symon Stripeback");
        names.add("Alyn");
        names.add("High Septon");
        names.add("Hobb");
        names.add("Barsena");
        names.add("Coldhands");
        names.add("Weeper");
        names.add("Aeron");
        names.add("Damon");
        names.add("Hugh Hungerford");
        names.add("Rhaegal");
        names.add("Benerro");
        names.add("Rakharo");
        names.add("Skinner");
        names.add("Tysha");
        names.add("Hagen");
        names.add("Aggo");
        names.add("Jhogo");
        names.add("Thistle");
        names.add("Qezza");
        names.add("Nan");
        names.add("Qavo Nogarys");
        names.add("Donal Noye");
        names.add("Leaf");
        names.add("Squirrel");
        names.add("Kasporio");
        names.add("Goghor");
        names.add("Belaquo");
        names.add("Balaq");
        names.add("Cromm");
        names.add("Pynto");
        names.add("Gerrick");
        names.add("Petyr Baelish");
        names.add("Wick Whittlestick");
        names.add("Torgon Greyiron");
        names.add("Marselen");
        names.add("Sigorn");
        names.add("Rory");
        names.add("Bump");
        names.add("Stalwart Shield");
        names.add("Grimtongue");
        names.add("Owen");
        names.add("Kyra");

        // 10 or less occurrences, but previously relevant
        names.add("Shae");
        names.add("Patchface");
        names.add("Craster");
        names.add("Dywen");
        names.add("Dagmer Cleftjaw");
        names.add("Lorren");
        names.add("Rickon");
        names.add("Qhorin Halfhand"); // note that in book one, his name was spelled "Quorin"
        names.add("Grenn");
        names.add("Mikken");
        names.add("Wex");
        names.add("Pyat Pree");
        names.add("Raff");
        names.add("Arstan");
        names.add("Shagga");
        names.add("Timett");
        names.add("Jaqen");
        names.add("Farlen");
        names.add("Syrio Forel");

        // manually remove a few names that are either mistakes, duplicates, or unused
        names.remove("Ned Stark");      // as Eddard Stark
        names.remove("Jon Stark");      // as Jon Snow
        names.remove("Daenerys Stormborn"); // as Daenerys Targaryen
        names.remove("Frog");           // as Quentyn Martell
        names.remove("Arya Horseface"); // as Quentyn Martell
        names.remove("Arya Underfoot"); // as Quentyn Martell
        names.remove("Joff");           // as Joffrey Baratheon
        names.remove("Sam Tarly");      // as Samwell Tarly
        names.remove("Ben Stark");      // as Benjen Stark
        names.remove("Theon Stark");    // as Theon Greyjoy
        names.remove("Theon Turncloak");// as Theon Greyjoy
        names.remove("Brynden Blackfish");  // as Brynden Tully
        names.remove("Davos Shorthand");// as Davos Seaworth
        names.remove("Littlefinger");   // as Petyr Baelish
        names.remove("Paramount");      // as Petyr Baelish
        names.remove("Reaper");         // as Balon Greyjoy
        names.remove("Eunuch");         // as Varys
        names.remove("Varamyr Threeskins"); // as Varamyr
        names.remove("Varamyr Sixskins");   // as Varamyr
        names.remove("Godry Giantslayer");  // as Godry Farring
        names.remove("Mad Aerys");      // as Aerys Targaryen
        names.remove("Whoresbane Umber");   // as Hother Umber
        names.remove("Hizdahr Loraq");  // as Hizdahr zo Loraq
        names.remove("Middle Liddle");  // as Morgan Liddle
        names.remove("Yellowbelly");    // as Yezzan
        names.remove("Lard");           // as Wyman Manderly
        names.remove("Imp");            // as Tyrion Lannister
        names.remove("Hugor Hill");     // as Tyrion Lannister
        names.remove("Hugor Halfmaester");  // as Tyrion Lannister
        names.remove("Ramsay Bolton");  // as Ramsay Snow
        names.remove("Duck");           // as Rolly Duckfield
        names.remove("Crowfood Umber"); // as Mors Umber

        names.remove("Eyed Maid");      // unused
        names.remove("Lu");             // named axe
        names.remove("Pounce");         // a cat
        names.remove("Fiery Hand");     // mistake
        names.remove("Hoarfrost Hill"); // mistake
        names.remove("Horn Hill");      // mistake

        Set<String> firstNames = finder.getFirstNames(names);

        FileUtils.writeFile(finder.getNameList(), "src/main/resources/data/characters/dwd-list-full.txt");
        FileUtils.writeFile(finder.getNameList(names), "src/main/resources/data/characters/dwd-list-clean.txt");
        FileUtils.writeFile(finder.getNameList(firstNames), "src/main/resources/data/characters/dwd-list-no-dup.txt");
        FileUtils.writeFile(finder.getFirstNameList(names), "src/main/resources/data/characters/dwd-list-first.txt");

    }
}
