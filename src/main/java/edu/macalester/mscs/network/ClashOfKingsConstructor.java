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

//    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/cok-list-curated.txt";
//    public static final String TEXT_FILE_NAME = "src/main/resources/text/clashofkings-intercap.txt";

    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/cok-list-curated-hyphenated.csv";
    public static final String TEXT_FILE_NAME = "src/main/resources/text/clashofkings-hyphenated-update.txt";

    public static final int BOOK_NUMBER = 2;

    /**
     * Main method for generating the matrix, edge list and log files for "A Clash of Kings."
     * @param args
     */
    public static void main(String[] args) {
        int fileNum = 4;
        String fileDesc = "hyphen";

        ClashOfKingsConstructor constructor = new ClashOfKingsConstructor(15, 3);

        constructor.constructMatrix(fileNum, fileDesc, LOG_FOLDER);
        constructor.writeFiles(fileNum, fileDesc, LOG_FOLDER, false);
    }

    public ClashOfKingsConstructor(int radius, int noise) {
        super(BOOK_NUMBER, TEXT_FILE_NAME, CHARACTER_FILE_NAME, radius, noise);
    }

//    public void writeFiles() {
//        writeFiles(VOLUME_NUMBER, fileNumber, fileDescriptor);
//    }


}
