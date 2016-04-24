package edu.macalester.mscs.network;

/**
 * @author Andrew Beveridge
 */
public class ClashOfKingsConstructor extends MatrixConstructor {

    /**
     * Legacy file creation for Clash of Kings
     */

//		String text = getText("src/main/resources/text/clashofkings-intercap.txt");
//		String characterString = getCharacterString("src/main/resources/data/characters/cok-list-curated.txt");
//		writeFiles(constructMatrix(characterString, text, RADIUS, NOISE, folder + "/log.txt"), folder, 2, 1, "curated");
//		writeFiles(constructMatrix(characterString, text, RADIUS, NOISE, folder + "/log.txt"), folder, 2, 2, "intercap");

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

        GameOfThronesConstructor constructor = new GameOfThronesConstructor(15, 4);

        constructor.constructMatrix(LOG_FILE_MATRIX);
        constructor.writeFiles(fileNum, fileDesc, LOG_FOLDER, false);
    }

    public ClashOfKingsConstructor(int radius, int noise) {
        super(BOOK_NUMBER, TEXT_FILE_NAME, CHARACTER_FILE_NAME, radius, noise);
    }

//    public void writeFiles() {
//        writeFiles(VOLUME_NUMBER, fileNumber, fileDescriptor);
//    }


}
