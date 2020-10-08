package edu.macalester.mscs.network;

/**
 * @author Andrew Beveridge
 */
public class PrideAndPrejudiceConstructor extends MatrixConstructor {


    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/pp-char-hyphen.csv";
    public static final String TEXT_FILE_NAME = "src/main/resources/text/pandp-hyphen.txt";
    public static final String BOOK_PREFIX = "PP";

    /**
     * Main method for generating the matrix, edge list and log files for "Pride and Prejudice."
     * @param args
     */
    public static void main(String[] args) {
        int fileNum = 1;
        String fileDesc = "pride";

        PrideAndPrejudiceConstructor ppConstructor = new PrideAndPrejudiceConstructor(fileNum,15, 1);

        ppConstructor.constructMatrix(fileDesc, DEFAULT_LOG_FOLDER);
        ppConstructor.writeFiles(fileDesc, DEFAULT_LOG_FOLDER, false);
    }

    public PrideAndPrejudiceConstructor(int file_num, int radius, int noise) {
        super(BOOK_PREFIX + file_num, TEXT_FILE_NAME, CHARACTER_FILE_NAME, radius, noise);
    }
}


