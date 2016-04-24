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




    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/cok-list-curated.txt";
    public static final String TEXT_FILE_NAME = "src/main/resources/text/clashofkings-intercap.txt";
    public static final int BOOK_NUMBER = 2;


    /**
     * Main method for generating the matrix, edge list and log files for "A Clash of Kings."
     * @param args
     */
    public static void main(String[] args) {
        int fileNum = 2;
        String fileDesc = "intercap";

        ClashOfKingsConstructor cokConstructor = new ClashOfKingsConstructor(fileNum, fileDesc);

        cokConstructor.constructMatrix();
        cokConstructor.writeFiles();
    }

    public GameOfThronesConstructor(int fileNumber, String fileDescriptor) {
        super(CHARACTER_FILE_NAME, TEXT_FILE_NAME, BOOK_NUMBER, fileNumber, fileDescriptor);
    }

//    public void writeFiles() {
//        writeFiles(VOLUME_NUMBER, fileNumber, fileDescriptor);
//    }


}
