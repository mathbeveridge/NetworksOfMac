package edu.macalester.mscs.network;

/**
 * @author Andrew Beveridge
 */
public class ClashOfKingsConstructor extends MatrixConstructor {


    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/cok-list-merged.csv";
    public static final String TEXT_FILE_NAME = "src/main/resources/text/clashofkings-merged-2.txt";

    public static final int BOOK_NUMBER = 2;

    /**
     * Main method for generating the matrix, edge list and log files for "A Clash of Kings."
     * @param args
     */
    public static void main(String[] args) {
        int fileNum = 6;
        String fileDesc = "merged";

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
