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
            "The",
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
            "Young", "Old", "Fat", "Big", "Little", "Small", "Bastard", "Boy", "Deaf", "Blind", "Hero", // endearing titles
            "High", "Great", "Grand", "First", "Second", "Third", // superlatives
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", // numbers
            "Black", "Red", "Green", "Blue", "White", "Grey", "Brown", "Yellow", "Silver", "Golden", // colors
            "Land", "Lands", "Sea", "Seas", "Island", "Islands", "Isle", "Isles", "Bay", "River", "Shore",
            "Point", "Lake", "Hills", "Straits", "Vale", "Wood", "Rock", // locations (natural)
            "City", "Cities", "Alley", "Gate", "Keep", "Market", "Tower", "Hall", "Castle", "Lane",
            "Bridge", "Sept", "Harbor", "Mill", "Port", "Town", "Guild", // locations (man-made)
            "Just", "Sweet", "Brave", "Bold", "Scared", "Poor", "Blessed", "Mad", "Unworthy", "Good", "Long",
            "Faithful", "Gallant", "Lusty", "Cruel", "Tall", // adjective titles
            "Spotted", "Iron", "Bloody", "Belly", "Fool", "Ghost", "Crow", "Dog" // miscellaneous
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
        finder.incrementWord("Josmyn Peckledon", 4);
        finder.incrementWord("Gilwood Hunter", 2);
        finder.incrementWord("Nymeria Sand", 1);
        finder.removeWords("Winterfell Tommen"); // mistake
        finder.removeWords("Arya the Lannisters"); // mistake
        finder.removeWords("Ser Hyle of Podrick"); // mistake
        finder.removeWords("Lord Nestor the Gates of the Moon"); // mistake
        finder.removeWords("Randyll Tarly the Hand of the King"); // mistake
        finder.removeWords("Blackbird Gilly"); // mistake
        finder.removeWords("Petyr Pimple"); // unused
        finder.removeWords("Strong Sam Stone"); // unused
        finder.removeWords("Brynden Rivers"); // unused
        finder.removeWords("Rodrik Freeborn"); // unused
        finder.removeWords("Harys Haigh"); // unused
        finder.removeWords("Harry Sawyer"); // unused
        finder.removeWords("Tytos Lannister"); // unused
        finder.removeWords("Long Tom Costayne"); // unused
        finder.removeWords("Ser Rodrik"); // unused
        finder.removeWords("Canker Jeyne"); // unused
        finder.removeWords("Jeyne Fowler"); // unused
        finder.removeWords("Jeyne Poole"); // unused
        finder.removeWords("Lord Lucas"); // unused
        finder.removeWords("Raynard Ruttiger"); // unused
        finder.removeWords("Gormond Drumm the Oldfather"); // unused
        finder.removeWords("Queen Alysanne"); // unused
        finder.removeWords("Garth the Twelfth"); // unused
        finder.removeWords("Loves of Queen Nymeria"); // a book
        finder.removeWords("Hand Lew"); // a book

        finder.printCounter().writeLog("src/main/resources/data/characters/ffc-counter.csv");

        // manually add important names that get missed
        finder.addNames("Gilly");
        finder.addNames("Pycelle");
        finder.addNames("Pate");
        finder.addNames("High Septon");
        finder.addNames("Areo Hotah");
        finder.addNames("Dareon");
        finder.addNames("Alleras");
        finder.addNames("Baelor the Blessed");
        finder.addNames("Nute");
        finder.addNames("Xhondo");
        finder.addNames("Josmyn Peckledon");
        finder.addNames("Brusco");
        finder.addNames("Mollander");
        finder.addNames("Maddy");
        finder.addNames("Armen");
        finder.addNames("Shagwell");
        finder.addNames("Pia");
        finder.addNames("Melara");
        finder.addNames("Dorcas");
        finder.addNames("Maggy");
        finder.addNames("Moon Boy");
        finder.addNames("Timeon");
        finder.addNames("Marillion");
        finder.addNames("Willow");
        finder.addNames("Blue Bard");
        finder.addNames("Rosey");
        finder.addNames("Gretchel");
        finder.addNames("Craster");
        finder.addNames("Denyo");
        finder.addNames("Whitesmile Wat");
        finder.addNames("Gendry");
        finder.addNames("Cedra");
        finder.addNames("Biter");
        finder.addNames("Roone");
        finder.addNames("Senelle");
        finder.addNames("Pyg");
        finder.addNames("Garth");
        finder.addNames("Rennifer Longwaters");
        finder.addNames("Lollys");
        finder.addNames("Brea");
        finder.addNames("Ralf the Limper");
        finder.addNames("Red Oarsman");
        finder.addNames("Umma");
        finder.addNames("Raff");
        finder.addNames("Thoros");
        finder.addNames("Pyp");
        finder.addNames("Mance Rayder");
        finder.addNames("Urri");

        // 10 or less occurrences, but previously relevant
        finder.addNames("Jaqen");
        finder.addNames("Edd Tollett");
        finder.addNames("Nan");
        finder.addNames("Dalla");
        finder.addNames("Grenn");
        finder.addNames("Syrio Forel");
        finder.addNames("Aemon the Dragonknight");
        finder.addNames("Rorge");
        finder.addNames("Clydas");
        finder.addNames("Vaellyn");
        finder.addNames("Val");
        finder.addNames("Lem");
        finder.addNames("Rickon");
        finder.addNames("Shae");
        finder.addNames("Tom Sevenstrings");
        finder.addNames("Tickler");

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
        finder.addNondescriptors("Jeyne");
        finder.addNondescriptors("Hugh");
        finder.addNondescriptors("Wat");

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
        finder.removeNames("Vinegar Vaellyn");  // as Vaellyn
        finder.removeNames("Sweetrobin");       // as Robert Arryn
        finder.removeNames("Brynden Blackfish");// as Brynden Tully
        finder.removeNames("Lew Piper");        // as Lewis Piper

        finder.removeNames("Protector");        // unused
        finder.removeNames("Seneschal");        // unused
        finder.removeNames("Horn Hill");        // mistake

        finder.removeNondescriptors("Tywin");
        finder.removeNondescriptors("Viper");

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
        groups.addAliasToGroup("Jon Connington", "Lord Jon", finder.getCount("Lord Jon")); // needs to be completely manually disambiguated
        groups.addAliasToGroup("Jon Arryn", "Lord Jon", finder.getCount("Lord Jon")); // needs to be completely manually disambiguated
        groups.addAliasToGroup("Robert Baratheon", "Robert", finder.getCount("Robert"));
        groups.addAliasToGroup("Balon Greyjoy", "Balon", finder.getCount("Balon"));
        groups.addAliasToGroup("Aemon Targaryen", "Aemon", finder.getCount("Aemon"));
        groups.addAliasToGroup("Baelor the Blessed", "Baelor", finder.getCount("Baelor"));
        groups.addAliasToGroup("Gyles Rosby", "Gyles", finder.getCount("Gyles"));
        groups.addAliasToGroup("Black Walder", "Walder", finder.getCount("Walder"));
        groups.addAliasToGroup("Denys Arryn", "Denys", finder.getCount("Denys"));
        groups.addAliasToGroup("Jeyne Westerling", "Jeyne", finder.getCount("Jeyne")); // needs to be completely manually disambiguated
        groups.addAliasToGroup("Jeyne Heddle", "Jeyne", finder.getCount("Jeyne")); // needs to be completely manually disambiguated
        groups.addAliasToGroup("Jeyne Farman", "Jeyne", finder.getCount("Jeyne")); // needs to be completely manually disambiguated
        groups.addAliasToGroup("Blue Bard", "Wat", finder.getCount("Wat")); // needs to be completely manually disambiguated
        groups.addAliasToGroup("Whitesmile Wat", "Wat", finder.getCount("Wat")); // needs to be completely manually disambiguated

        // manually combine more character groups
        groups.combineGroups("Jon Snow", "Lord Snow");
        groups.combineGroups("Jon Arryn", "Lord Arryn");
        groups.combineGroups("Robert Baratheon", "King Robert");
        groups.combineGroups("Robert Arryn", "Lord Robert", "Lord of the Eyrie", "Sweetrobin");
        groups.combineGroups("Balon Greyjoy", "King Balon", "Lord Balon", "Balon the Blessed", "Balon the Brave", "Balon Twice-Crowned", "Balon the Widowmaker", "Balon the Bold");
        groups.combineGroups("Balon Swann", "Ser Balon");
        groups.combineGroups("Maester Aemon", "Aemon Targaryen", "Aemon Battleborn", "Aemon Steelsong");
        groups.combineGroups("Aemon the Dragonknight", "Prince Aemon");
        groups.combineGroups("Baelor the Blessed", "King Baelor the Blessed", "Blessed Baelor", "Beloved Baelor", "King Baelor");
        groups.combineGroups("Gyles Rosby", "Lord Gyles");
        groups.combineGroups("Walder Frey", "Lord Walder", "Lord of the Crossing", "Lord Frey");
        groups.combineGroups("Red Ralf Stonehouse", "Red Ralf");
        groups.combineGroups("Ralf the Limper", "Ralf the Limp");
        groups.combineGroups("Quellon Greyjoy", "Lord Quellon");
        groups.combineGroups("Denys Arryn", "Ser Denys", "Ser Denys Arryn");
        groups.combineGroups("Robin Ryger", "Ser Robin Ryger");
        groups.combineGroups("Owen Inchfield", "Ser Owen Inchfield");
        groups.combineGroups("Jeyne Heddle", "Long Jeyne Heddle", "Long Jeyne");
        groups.combineGroups("Jeyne Farman", "Fat Jeyne Farman");

        groups.combineGroups("Samwell Tarly", "Sam");
        groups.combineGroups("Petyr Baelish", "Littlefinger");
        groups.combineGroups("Eddard", "Ned");
        groups.combineGroups("Catelyn", "Lady Stark");
        groups.combineGroups("Joffrey", "Joff");
        groups.combineGroups("Sandor", "Hound");
        groups.combineGroups("Jeor Mormont", "Old Bear", "Lord Commander Mormont", "Lord Mormont");
        groups.combineGroups("Tywin", "Lord of Casterly Rock");
        groups.combineGroups("Roose", "Lord of the Dreadfort", "Lord Bolton");
        groups.combineGroups("Loras", "Knight of Flowers");
        groups.combineGroups("Varys", "Spider");
        groups.combineGroups("Gregor", "Mountain");
        groups.combineGroups("Cersei", "Queen Regent");
        groups.combineGroups("Randyll", "Lord Tarly");
        groups.combineGroups("Mace", "Lord Tyrell");
        groups.combineGroups("Tyrion", "Imp");
        groups.combineGroups("Arya", "Arry", "Squab", "Cat", "Salty");
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
        groups.combineGroups("Nym", "Nymeria Sand");
        groups.combineGroups("Harry", "Harrold Hardyng");
        groups.combineGroups("Lewys Piper", "Lew");
        groups.combineGroups("Emmon Frey", "Emm");
        groups.combineGroups("Rorge", "Mad Dog of Saltpans");
        groups.combineGroups("Taena Merryweather", "Lady Merryweather");
        groups.combineGroups("Andrey Dalt", "Drey");
        groups.combineGroups("High Septon", "High Holiness");
        groups.combineGroups("Lyle Crakehall", "Strongboar");
        groups.combineGroups("Josmyn Peckledon", "Peck");
        groups.combineGroups("Gerold Dayne", "Darkstar");
        groups.combineGroups("Victarion", "Nuncle");
        groups.combineGroups("Anya Waynwood", "Lady Waynwood");
        groups.combineGroups("Aurane Waters", "Lord Waters");
        groups.combineGroups("Orton Merryweather", "Lord Merryweather");

        groups.writeNameList("src/main/resources/data/characters/ffc-list-full.txt");
        groups.writeNameList(finder.getNames(), true, 4, "src/main/resources/data/characters/ffc-list-clean.txt");
        groups.writeNameList(finder.getNames(), false, 4, "src/main/resources/data/characters/ffc-list-clean-redundant.txt");

    }
}
