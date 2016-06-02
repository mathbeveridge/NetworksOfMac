package edu.macalester.mscs.network;

/**
 * Created by abeverid on 5/8/16.
 */
public class FeastForCrowsConstructor extends MatrixConstructor {


    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/ffc-list-mynowiki6.csv";
    public static final String TEXT_FILE_NAME = "src/main/resources/text/feastforcrows-mynowiki6.txt";

    public static final int BOOK_NUMBER = 4;

    /**
     * Main method for generating the matrix, edge list and log files for "A Feast for Crows."
     *
     * @param args
     */
    public static void main(String[] args) {
        int fileNum = 6;
        String fileDesc = "mynowiki";

        FeastForCrowsConstructor constructor = new FeastForCrowsConstructor(15, 3);

        constructor.constructMatrix(fileNum, fileDesc, LOG_FOLDER);
        constructor.writeFiles(fileNum, fileDesc, LOG_FOLDER, false);
    }

    public FeastForCrowsConstructor(int radius, int noise) {
        super(BOOK_NUMBER, TEXT_FILE_NAME, CHARACTER_FILE_NAME, radius, noise);
    }

}
