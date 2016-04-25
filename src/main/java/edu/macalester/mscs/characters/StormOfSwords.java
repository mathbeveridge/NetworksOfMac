package edu.macalester.mscs.characters;

import edu.macalester.mscs.utils.FileUtils;
import edu.macalester.mscs.utils.WordUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ari Weiland
 */
public class StormOfSwords {

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
            "Queer", "Sour", "Cunning", "Hairy", "Broken", "Bloody", "Late", // adjective titles
            "Conqueror", "Wolf", "Fool", "Iron", "Worm", "Bull", "Kingswood", "Sword", "Nymeros", "Wife", "Bar" // miscellaneous
    ));

    // Words that are sometimes placed between first and last names
    public static final Set<String> FILLER_WORDS = new HashSet<>(Arrays.asList("zo", "mo"));

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

        // gather names, titles, places, and things
        Set<String> titledNames = finder.getTitledNames();
        Set<String> pluralizedNames = finder.getPluralizedNames();
        pluralizedNames.remove("Walder");
        Set<String> surnames = finder.getSurnames();
        surnames.add("Cerwyn");
        surnames.add("Merryweather");
        surnames.add("Mooton");
        surnames.add("Bulwer");
        Set<String> names = finder.getNamesBySurname(surnames);
        names.addAll(titledNames);
        Set<String> places = finder.getPlaces(names);
        places.add("Tarth");
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
        finder.removeWordsBelowThreshold(lonely, 1);

        finder.printCounter().writeLog("src/main/resources/data/characters/sos-counter.csv");

        // gather phrases that are not inherently descriptive
        Set<String> nondescriptors = new HashSet<>();
        nondescriptors.addAll(pluralizedNames);
        nondescriptors.addAll(WordUtils.getPlurals(pluralizedNames));
        nondescriptors.addAll(surnames);
        nondescriptors.addAll(WordUtils.getPlurals(surnames));
        nondescriptors.addAll(places);

        // add problematic names
        nondescriptors.add("Jon");
        nondescriptors.add("Samwell");
        nondescriptors.add("Brandon");
        nondescriptors.add("Robert");
        nondescriptors.add("Petyr");
        nondescriptors.add("Walder");
        nondescriptors.add("Aemon");
        nondescriptors.add("Lothar");
        nondescriptors.add("Jeyne");
        nondescriptors.add("Balon");
        nondescriptors.add("Ben");
        nondescriptors.add("Robin");
        nondescriptors.add("Donnel");
        nondescriptors.add("Baelor");
        nondescriptors.add("Walda");
        nondescriptors.add("Willem");
        nondescriptors.add("Martyn");
        nondescriptors.add("Rodrik");
        nondescriptors.add("Tytos");
        nondescriptors.add("Garth");
        nondescriptors.add("Lucas");
        nondescriptors.add("Tim");
        nondescriptors.add("Daemon");
        nondescriptors.add("Myles");

        // build character groups
        finder.buildCharacterGroups(nondescriptors);

        // add back problematic names as necessary
        finder.addToCharacterGroup("Jon Snow", "Jon");
        finder.addToCharacterGroup("Robert Baratheon", "Robert");
        finder.addToCharacterGroup("Petyr Baelish", "Petyr");
        finder.addToCharacterGroup("Aemon Targaryen", "Aemon");
        finder.addToCharacterGroup("Lothar Frey", "Lothar");
        finder.addToCharacterGroup("Jeyne Westerling", "Jeyne");
        finder.addToCharacterGroup("Baelor the Blessed", "Baelor");
        finder.addToCharacterGroup("Willem Lannister", "Willem");
        finder.addToCharacterGroup("Martyn Lannister", "Martyn");

        // manually combine more character groups
        finder.combineGroups("Jon Snow", "Lord Snow", "Jon Stark");
        finder.combineGroups("Jon Connington", "Lord Connington", "Lord Jon");
        finder.combineGroups("Jon Arryn", "Lord Arryn");
        finder.combineGroups("Jon Darry", "Jonothor Darry");
        finder.combineGroups("Bran", "Brandon Stark");
        finder.combineGroups("Samwell Tarly", "Sam", "Piggy", "Slayer");
        finder.combineGroups("Robert Baratheon", "Usurper", "King Robert");
        finder.combineGroups("Robert Arryn", "Lord Robert", "Lord of the Eyrie");
        finder.combineGroups("Petyr Baelish", "Littlefinger", "Lord Petyr");
        finder.combineGroups("Walder Frey", "Lord Walder", "Lord of the Crossing", "Lord Frey", "Lord Grandfather");
        finder.combineGroups("Aemon Targaryen", "Maester Aemon");
        finder.combineGroups("Aemon the Dragonknight", "Prince Aemon");
        finder.combineGroups("Lothar Frey", "Lame Lothar");
        finder.combineGroups("Jeyne Westerling", "Queen Jeyne");
        finder.combineGroups("Balon Greyjoy", "King Balon", "Lord Balon");
        finder.combineGroups("Balon Swann", "Ser Balon");
        finder.combineGroups("Robin Ryger", "Ser Robin");
        finder.combineGroups("Donnel Hill", "Sweet Donnel");
        finder.combineGroups("Baelor the Blessed", "King Baelor");
        finder.combineGroups("Baelor Hightower", "Baelor Brightsmile", "Baelor Breakwind");
        finder.combineGroups("Walda Bolton", "Lady Walda", "Fat Walda", "Walda Frey", "Lady Bolton");
        finder.combineGroups("Rodrik Cassel", "Ser Rodrik");
        finder.combineGroups("Tytos Lannister", "Lord Tytos");
        finder.combineGroups("Eddard", "Ned");
        finder.combineGroups("Catelyn", "Cat", "Lady Stark");
        finder.combineGroups("Daenerys", "Dany", "Khaleesi");
        finder.combineGroups("Joffrey", "Joff");
        finder.combineGroups("Sandor", "Hound");
        finder.combineGroups("Jeor Mormont", "Old Bear", "Commander Mormont", "Lord Mormont");
        finder.combineGroups("Lysa", "Lady Arryn", "Lady of the Eyrie");
        finder.combineGroups("Tywin", "Lord of Casterly Rock");
        finder.combineGroups("Roose", "Lord of the Dreadfort", "Lord Bolton");
        finder.combineGroups("Loras", "Knight of Flowers");
        finder.combineGroups("Davos", "Onion Knight", "Ser Onions");
        finder.combineGroups("Varys", "Spider");
        finder.combineGroups("Gregor", "Mountain");
        finder.combineGroups("Cersei", "Queen Regent");
        finder.combineGroups("Rattleshirt", "Lord of Bones");
        finder.combineGroups("Randyll", "Lord Tarly");
        finder.combineGroups("Mace", "Lord Tyrell", "Puff Fish");
        finder.combineGroups("Stannis", "Lord of Dragonstone");
        finder.combineGroups("Rickard Karstark", "Lord Karstark");
        finder.combineGroups("Janos", "Lord Slynt");
        finder.combineGroups("Tyrion", "Imp");
        finder.combineGroups("Hother", "Whoresbane");
        finder.combineGroups("Arya", "Arry");
        finder.combineGroups("Aerys", "Mad King", "King Scab");
        finder.combineGroups("Ben Plumm", "Brown Ben");
        finder.combineGroups("Salladhor", "Salla");
        finder.combineGroups("Paxter", "Lord Redwyne");
        finder.combineGroups("Jonos", "Lord Bracken");
        finder.combineGroups("Tytos Blackwood", "Lord Blackwood");
        finder.combineGroups("Robb", "Young Wolf", "King of the Trident");
        finder.combineGroups("Benjen", "Ben Stark");
        finder.combineGroups("Podrick", "Pod");
        finder.combineGroups("Gerold", "White Bull");
        finder.combineGroups("Arthur", "Sword of the Morning");
        finder.combineGroups("Cley Cerwyn", "Lord Cerwyn");
        finder.combineGroups("Sansa Stark", "Lady Wife", "Lady of Winterfell");
        finder.combineGroups("Jaime Lannister", "Kingslayer");
        finder.combineGroups("Maege Mormont", "Lady Mormont");
        finder.combineGroups("Mathis Rowan", "Lord of Goldengrove", "Lord Rowan");
        finder.combineGroups("Tremond Gargalen", "Lord Gargalen");
        finder.combineGroups("Alester Florent", "Lord Florent");
        finder.combineGroups("Jason Mallister", "Lord of Seagard", "Lord Mallister");
        finder.combineGroups("Selwyn", "Lord of Evenfall");
        finder.combineGroups("Leyton Hightower", "Lord Hightower");
        finder.combineGroups("Lymond Goodbrook", "Lord Goodbrook");
        finder.combineGroups("Hoster Tully", "Lord of Riverrun", "Lord Tully");
        finder.combineGroups("Greatjon Umber", "Lord Umber");
        finder.combineGroups("Alerie", "Lady Tyrell");
        finder.combineGroups("Roslin Frey", "Lady Tully", "Roslin Tully");
        finder.combineGroups("Dontos Hollard", "Ser Fool");
        finder.combineGroups("Olenna Tyrell", "Queen of Thorns");

        // manually add important names that get missed
        names.add("Mance Rayder");
        names.add("Hodor");
        names.add("Ygritte");
        names.add("Lem");
        names.add("Grenn");
        names.add("Tom Sevenstrings");

        names.add("Arstan");
        names.add("Craster");
        names.add("Donal Noye");
        names.add("Qhorin Halfhand"); // note that in book one, his name was spelled "Quorin"
        names.add("Gilly");
        names.add("Belwas");
        names.add("Shae");
        names.add("Rattleshirt");
        names.add("Edd Tollett");
        names.add("Pycelle");
        names.add("Missandei");
        names.add("Nan");
        names.add("Rickon");
        names.add("Irri");
        names.add("Owen");
        names.add("Jhiqui");
        names.add("High Septon");
        names.add("Othell Yarwyck");
        names.add("Hobb");
        names.add("Varamyr");
        names.add("Baelor the Blessed");
        names.add("Dywen");
        names.add("Grey Worm");
        names.add("Raff");
        names.add("Tysha");
        names.add("Emmett");

        // 10 or less occurrences, but previously relevant
        names.add("Rhaegal");
        names.add("Jaqen");
        names.add("Jhogo");
        names.add("Mully");
        names.add("Rakharo");
        names.add("Syrio Forel");
        names.add("Weeper");
        names.add("Aggo");
        names.add("Patchface");
        names.add("Xaro Xhoan Daxos");
        names.add("Timett");
        names.add("Pyat Pree");
        names.add("Shagga");
        names.add("Mikken");
        names.add("Farlen");
        names.add("Coldhands");
        names.add("Kyra");

        // manually remove a few names that are either mistakes, duplicates, or unused
        names.remove("Jon Stark");      // as Jon Snow
        names.remove("Sam Tarly");      // as Samwell Tarly
        names.remove("Piggy");      // as Samwell Tarly
        names.remove("Joff");           // as Joffrey Baratheon
        names.remove("Brandon Stark");  // as Bran
        names.remove("Petyr Littlefinger"); // as Petyr Baelish
        names.remove("Ned Stark");      // as Eddard Stark
        names.remove("Lysa Tully");     // as Lysa Arryn
        names.remove("Brynden Blackfish");  // as Brynden Tully
        names.remove("Ben Stark");      // as Benjen Stark
        names.remove("Merrett Muttonhead"); // as Merrett Frey
        names.remove("Baelor Brightsmile"); // as Baelor Hightower
        names.remove("Baelor Breakwind");   // as Baelor Hightower
        names.remove("Roslin Tully");   // as Roslin Frey
        names.remove("Walda Frey");     // as Walda Bolton
        names.remove("Grandfather");    // as Walder Frey
        names.remove("Puff Fish");      // as Mace Tyrell
        names.remove("Scab");           // as Aerys Targaryen
        names.remove("Onions");         // as Davos
        names.remove("Wife");           // as Sansa
        names.remove("Aerion Brightfire");  // as Aerion Brightflame
        names.remove("Jon Darry");      // as Jonothor Darry

        names.remove("Soldier");        // a doll
        names.remove("Paramount");      // a generic title
        names.remove("Horn Hill");      // mistake
        names.remove("Maiden Fair");    // mistake

        Set<String> firstNames = finder.getFirstNames(names);

        FileUtils.writeFile(finder.getNameList(), "src/main/resources/data/characters/sos-list-full.txt");
        FileUtils.writeFile(finder.getNameList(names, true, 4), "src/main/resources/data/characters/sos-list-clean.txt");
        FileUtils.writeFile(finder.getNameList(firstNames, true, 4), "src/main/resources/data/characters/sos-list-no-dup.txt");
        FileUtils.writeFile(finder.getFirstNameList(names), "src/main/resources/data/characters/sos-list-first.txt");

    }
}
