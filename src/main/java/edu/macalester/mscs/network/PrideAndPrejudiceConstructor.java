package edu.macalester.mscs.network;

/**
 * @author Andrew Beveridge
 */
public class PrideAndPrejudiceConstructor extends MatrixConstructor {


    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/pp-char-hyphen.csv";
    public static final String TEXT_FILE_NAME = "src/main/resources/text/pandp-hyphen.txt";
    public static final String BOOK_ID = "PP";

    /**
     * Main method for generating the matrix, edge list and log files for "Pride and Prejudice."
     * @param args
     */
    public static void main(String[] args) {
        int fileNum = 1;
        String fileDesc = "pride";

        PrideAndPrejudiceConstructor ppConstructor = new PrideAndPrejudiceConstructor(15, 1);

        ppConstructor.constructMatrix(fileNum, fileDesc, LOG_FOLDER);
        ppConstructor.writeFiles(fileNum, fileDesc, LOG_FOLDER, false);
    }

    public PrideAndPrejudiceConstructor(int radius, int noise) {
        super(BOOK_ID, TEXT_FILE_NAME, CHARACTER_FILE_NAME, radius, noise);
    }
}


