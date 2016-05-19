package edu.macalester.mscs.network;

/**
 * @author Ari Weiland
 */
public class StormOfSwordsConstructor extends MatrixConstructor {

    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/sos-list-curated-hyphenated.csv";
    public static final String TEXT_FILE_NAME = "src/main/resources/text/stormofswords-hyphenated.txt";
    public static final int BOOK_NUMBER = 3;

    /**
     * Main method for generating the matrix, edge list and log files for "A Storm of Swords."
     * @param args
     */
    public static void main(String[] args) {
        int fileNum = 3;
        String fileDesc = "hyphen";

        StormOfSwordsConstructor constructor = new StormOfSwordsConstructor(15, 3);

        constructor.constructMatrix(fileNum, fileDesc, LOG_FOLDER);
        constructor.writeFiles(fileNum, fileDesc, LOG_FOLDER, false);
    }

    public StormOfSwordsConstructor(int radius, int noise) {
        super(BOOK_NUMBER, TEXT_FILE_NAME, CHARACTER_FILE_NAME, radius, noise);
    }


}
