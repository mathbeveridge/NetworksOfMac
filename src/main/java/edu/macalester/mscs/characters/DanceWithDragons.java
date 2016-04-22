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
            "Father", "Mother", "Uncle", "Aunt", "Brother", "Brothers", "Sons", "Cousin", // familial references
            "Men", "Man", "And", "Sleepy", "Worship", "Magnificence", "Planky", "Town",
            "Beyond", "Dornish", "Wedding", "Common", "Velvet", "Noble", "Broken" // miscellaneous
    ));

    // Words that are not unique, but may still be descriptive, expecially in combination
    public static final Set<String> TITLE_WORDS = new HashSet<>(Arrays.asList(
            "Lord", "Lady", "King", "Queen", "Regent", "Steward", "Prince", "Princess", // royal titles
            "Ser", "Maester", "Captain", "Commander", "Magister", "Master", "Builder",
            "Septon", "Septa", "Knight", "Shipwright", "Goodwife", "Ranger", "Squire", // professional titles
            "Khal", "Ko" // dothraki titles
    ));

    // Words that are not unique, but may still be descriptive, expecially in combination
    public static final Set<String> GENERAL_WORDS = new HashSet<>(Arrays.asList(
            "The", // titular articles
            "Young", "Old", "Fat", "Big", "Little", "Bastard", "Boy", "Deaf", "Blind", // endearing titles
            "High", "Great", "Grand", "First", "Second", // superlatives
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", // numbers
            "Black", "Red", "Green", "Blue", "White", "Grey", "Brown", "Yellow", // colors
            "Land", "Lands", "Sea", "Seas", "Island", "Islands", "Isles", "Bay", "River", "Shore", "Point",
            "Lake", // geographics
            "City", "Cities", "Alley", "Gate", "Keep", "Market", "Tower", "Hall", "Rock", "Castle", "Lane",
            "Bridge", "Sept", // landmarks
            "Cruel", "Bold", "Brave", "Good", "Strong", "Bitter", "Sweet", "Bad", "Clever", "Cautious",
            "Wise", "Craven", "Poor", "Pretty", "Scared", "Homeless", "Hot", "Shy", // adjective titles
            "Bear", "Iron", "Beggar", "Whore", "Wench", "Grandfather", "Water" // miscellaneous
    ));

    public static void main(String[] args) {
        // initialize the finder
        CharacterFinder finder = new CharacterFinder(IGNORED_WORDS, TITLE_WORDS, GENERAL_WORDS, ".?!�");
        // read in the text
        finder.countCapitalized(FileUtils.readFile("src/main/resources/text/dancewithdragons.txt"));
        // fix a few mistakes
        finder.incrementName("Petyr Baelish", 2);
        finder.incrementName("Petyr", 0);
        finder.removeWords("Reek Ramsay");
        finder.removeWords("Summer Islander King Robert");
        finder.removeWords("Pyke Lord Dagon");

        // gather names, titles, places, and things
        Set<String> titledNames = finder.getTitledNames();
        Set<String> pluralizedNames = finder.getPluralizedNames();
        Set<String> surnames = finder.getSurnames();
        Set<String> names = finder.getNamesBySurname(surnames);
//        surnames.remove("Bywater");
        names.addAll(titledNames);
        Set<String> places = finder.getPlaces(names);
        places.add("Vale");
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
//        finder.removeWordsBelowThreshold(lonely, 5);

        finder.printCounter().writeLog("src/main/resources/data/characters/dwd-counter.csv");

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
        finder.combineGroups("Eddard", "Ned", "Usurper");
        finder.combineGroups("Jon", "Lord Snow");
        finder.combineGroups("Bran", "Brandon Stark");
        finder.combineGroups("Petyr", "Littlefinger");
        finder.combineGroups("Daenerys", "Dany", "Khaleesi");
        finder.combineGroups("Joffrey", "Joff");
        finder.combineGroups("Samwell", "Sam");
        finder.combineGroups("Sandor", "Hound");
        finder.combineGroups("Old Bear", "Commander Mormont", "Lord Mormont", "Lord Crow");
        finder.combineGroups("Pycelle", "Grand Maester");
        finder.combineGroups("Lysa", "Lady Arryn");
        finder.combineGroups("Tywin", "Lord of Casterly Rock");
        finder.combineGroups("Roose", "Lord of the Dreadfort", "Lord Bolton");
        finder.combineGroups("Loras", "Knight of Flowers");
        finder.combineGroups("Davos", "Onion Knight");
        finder.combineGroups("Varys", "Spider");
        finder.combineGroups("Gregor", "Mountain");
        finder.combineGroups("Balon", "Reaper");
        finder.combineGroups("Theon", "Reek");
        finder.combineGroups("Cersei", "Queen Regent");
        finder.combineGroups("Rattleshirt", "Lord of Bones");
        finder.combineGroups("Wyman", "Lord of White Harbor");
        finder.combineGroups("Randyll", "Lord Tarly");
        finder.combineGroups("Balon Greyjoy", "Lord of the Iron Islands");
        finder.combineGroups("Mace", "Lord Tyrell");
        finder.combineGroups("Stannis", "Lord of Dragonstone");
        finder.combineGroups("Rickard Karstark", "Lord Karstark");
        finder.combineGroups("Janos", "Lord Slynt");
        finder.combineGroups("Quentyn", "Frog");
        finder.combineGroups("Rolly", "Duck");
        finder.combineGroups("Jon Connington", "Griff");
        finder.combineGroups("Tyrion", "Imp");
        finder.combineGroups("Hother", "Whoresbane");
        finder.combineGroups("Nymeria", "Nym");
        finder.combineGroups("Barristan", "Ser Grandfather");
        finder.combineGroups("Ghazdor", "Wobblecheeks");

        // manually add important names that get missed
        names.add("Varamyr");

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
        names.add("Arry");
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
        names.add("Belwas");
        names.add("Dywen");
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
        names.remove("Euron Crow");     // as Euron Greyjoy
        names.remove("Hugor Halfmaester");  // as Hugor Hill
        names.remove("Mad Aerys");      // as Aerys Targaryen
        names.remove("Whoresbane Umber");   // as Hother Umber
        names.remove("Hizdahr Loraq");  // as Hizdahr zo Loraq
        names.remove("Aegon Targaryen");// unused, too problematic
        names.remove("Brandon Norrey"); // unused
        names.remove("Roose Ryswell");  // unused
        names.remove("Lu");             // named axe
        names.remove("Fiery Hand");     // mistake

        Set<String> firstNames = finder.getFirstNames(names);

        FileUtils.writeFile(finder.getNameList(), "src/main/resources/data/characters/dwd-list-full.txt");
        FileUtils.writeFile(finder.getNameList(names), "src/main/resources/data/characters/dwd-list-clean.txt");
        FileUtils.writeFile(finder.getNameList(firstNames), "src/main/resources/data/characters/dwd-list-no-dup.txt");
        FileUtils.writeFile(finder.getFirstNameList(names), "src/main/resources/data/characters/dwd-list-first.txt");

    }
}
