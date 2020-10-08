package edu.macalester.mscs.network;

/**
 * @author Ari Weiland
 */
public class StormOfSwordsConstructor extends MatrixConstructor {

    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/sos-list-mynowiki3.csv";
    public static final String TEXT_FILE_NAME = "src/main/resources/text/stormofswords-mynowiki3.txt";
    public static final String CHARACTER_DATA_FILE_NAME = "src/main/resources/data/characters/awoiaf/A-Storm-of-Swords-characters.csv";
    public static final int BOOK_NUMBER = 3;

    /**
     * Main method for generating the matrix, edge list and log files for "A Storm of Swords."
     * @param args
     */
    public static void main(String[] args) {
        // use fileNum so you don't override old runs
        // fileDesc lets you comment on the changes from previous run
        int fileNum = 8;
        String fileDesc = "complete";

        StormOfSwordsConstructor constructor = new StormOfSwordsConstructor(15, 3);

        constructor.constructMatrix(fileNum, fileDesc, LOG_FOLDER);
        constructor.writeFiles(fileNum, fileDesc, LOG_FOLDER, false);
    }

    public StormOfSwordsConstructor(int radius, int noise) {
        super("GoT" + BOOK_NUMBER, TEXT_FILE_NAME, CHARACTER_FILE_NAME, radius, noise);
    }


    public String getCharacterDataFileName() { return CHARACTER_DATA_FILE_NAME; }


}
