package edu.macalester.mscs.network;

/**
 * Created by abeverid on 4/23/16.
 */
public class GameOfThronesConstructor extends MatrixConstructor {


    /**
     * Legacy file creation for Game of Thrones
     */

//		String text = getText("src/main/resources/text/gameofthrones-intercap.txt");
//		String characterString = getCharacterString("src/main/resources/data/characters/got-list-curated.txt");

//		String characterString = getCharacterString("src/main/resources/data/characters/got-list-no-dup.txt");
//		writeFiles(constructMatrix(characterString, text, RADIUS, NOISE, folder + "/log.txt"), folder, 1, 6, "full-names");
//		writeFiles(constructMatrix(characterString, text, RADIUS, NOISE, folder + "/log.txt"), folder, 1, 7, "dup-names");
//		writeFiles(constructMatrix(characterString, text, 15, NOISE, folder + "/log.txt"), folder, 1, 8, "smaller-radius");

//		writeFiles(constructMatrix(characterString, text, 15, NOISE, folder + "/log.txt"), folder, 1, 9, "curated2");
//		writeFiles(constructMatrix(characterString, text, RADUIS, NOISE, folder + "/log.txt"), folder, 1, 10, "intercap");


    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/got-list-curated.txt";
    public static final String TEXT_FILE_NAME = "src/main/resources/text/gameofthrones-intercap.txt";
    public static final int BOOK_NUMBER = 1;


    public static final String[] ORDERED_CHARACTER_LIST = {
            "Aerys T", "Alyn", "Barristan S", "Brandon S", "Cayn", "Cersei L", "Desmond", "Eddard S",
            "Harwin", "Hullen", "Janos S", "Jon A", "Jory C", "Lyanna", "Malleon", "Petyr B", "Pycelle",
            "Raymun D", "Renly B", "Rhaegar T", "Robert B", "Stannis B", "Tobho M", "Tom", "Torrhen K",
            "Varys", "Vayon P", "Aemon T", "Alliser T", "Benjen S", "Bowen M", "Chett", "Dareon", "Donal N",
            "Grenn", "Halder", "Hobb", "Jaremy R", "Jeor M", "Jon S", "Pyp", "Randyll T", "Samwell T", "Yoren",
            "Addam M", "Bronn", "Chiggen", "Colemon", "Conn", "Jaime L", "Jyck", "Kevan L", "Lefford", "Lysa A",
            "Marillion", "Mord", "Morrec", "Robert A", "Shae", "Shagga", "Timett", "Tyrion L", "Tywin L", "Vardis E",
            "Willis W", "Aggo", "Cohollo", "Daenerys T", "Doreah", "Drogo", "Haggo", "Illyrio M", "Irri", "Jhiqui",
            "Jhogo", "Jorah M", "Mirri M", "Ogo", "Qotho", "Quaro", "Rakharo", "Rhaego", "Viserys T", "Arya S", "Beric D",
            "Boros B", "Gregor C", "Ilyn P", "Jeyne P", "Joffrey B", "Loras T", "Maegor", "Meryn T", "Mycah", "Myrcella",
            "Sandor C", "Sansa S", "Septa M", "Syrio F", "Tommen", "Bran S", "Brynden T", "Catelyn S", "Donnel W", "Edmure T",
            "Galbart G", "Greatjon U", "Hal M", "Hallis M", "Hodor", "Hoster T", "Karyl V", "Luwin", "Maege M", "Marq P",
            "Masha H", "Nan", "Osha", "Rickard K", "Rickon S", "Robb S", "Rodrik C", "Roose B", "Stevron F", "Theon G", "Walder F"
    };

    /**
     * Main method for generating the matrix, edge list and log files for "A Clash of Kings."
     * @param args
     */
    public static void main(String[] args) {
        int fileNum = 2;
        String fileDesc = "intercap";

        ClashOfKingsConstructor cokConstructor = new ClashOfKingsConstructor(fileNum, fileDesc);

        cokConstructor.constructMatrix();
//        cokConstructor.writeFiles(ORDERED_CHARACTER_LIST);
        cokConstructor.writeFiles();
    }

    public GameOfThronesConstructor(int fileNumber, String fileDescriptor) {
        super(CHARACTER_FILE_NAME, TEXT_FILE_NAME, BOOK_NUMBER, fileNumber, fileDescriptor);
    }

//    public void writeFiles() {
//        writeFiles(VOLUME_NUMBER, fileNumber, fileDescriptor);
//    }


}
