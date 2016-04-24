package edu.macalester.mscs.network;

/**
 * @author Andrew Beveridge
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

    /**
     * Main method for generating the matrix, edge list and log files for "A Game of Thrones."
     * @param args
     */
    public static void main(String[] args) {
        int fileNum = 11;
        String fileDesc = "test";

        GameOfThronesConstructor constructor = new GameOfThronesConstructor(15, 4);

        constructor.constructMatrix(LOG_FILE_MATRIX);
        constructor.writeFiles(fileNum, fileDesc, LOG_FOLDER, false);
    }

    public GameOfThronesConstructor(int radius, int noise) {
        super(BOOK_NUMBER, TEXT_FILE_NAME, CHARACTER_FILE_NAME, radius, noise);
    }

//    public void writeFiles() {
//        writeFiles(VOLUME_NUMBER, fileNumber, fileDescriptor);
//    }

}
