package edu.macalester.mscs.network;

/**
 * @author Andrew Beveridge
 */
public class ClashOfKingsConstructor extends MatrixConstructor {


    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/cok-list-merged3.csv";
    public static final String TEXT_FILE_NAME = "src/main/resources/text/clashofkings-merged-3.txt";
    public static final String CHARACTER_DATA_FILE_NAME = "src/main/resources/data/characters/awoiaf/A-Clash-of-Kings-characters.csv";
    public static final int BOOK_NUMBER = 2;

    /**
     * Main method for generating the matrix, edge list and log files for "A Clash of Kings."
     * @param args
     */
    public static void main(String[] args) {
        // use fileNum so you don't override old runs
        // fileDesc lets you comment on the changes from previous run
        int fileNum = 7;
        String fileDesc = "complete";

        ClashOfKingsConstructor constructor = new ClashOfKingsConstructor(fileNum,15, 3);

        constructor.constructMatrix(fileDesc, DEFAULT_LOG_FOLDER);
        constructor.writeFiles(fileDesc, DEFAULT_LOG_FOLDER, false);
    }

    public ClashOfKingsConstructor(int fileNum, int radius, int noise) {
        super("GoT" + BOOK_NUMBER + '-' + fileNum, TEXT_FILE_NAME, CHARACTER_FILE_NAME, radius, noise);
    }

//    public void writeFiles() {
//        writeFiles(VOLUME_NUMBER, fileNumber, fileDescriptor);
//    }

    public String getCharacterDataFileName() { return CHARACTER_DATA_FILE_NAME; }
}
