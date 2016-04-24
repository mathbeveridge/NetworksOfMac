package edu.macalester.mscs.network;

/**
 * Created by abeverid on 4/23/16.
 */
public class ClashOfKingsConstructor extends MatrixConstructor {


    /**
     * Legacy file creation for Clash of Kings
     */

//		String text = getText("src/main/resources/text/clashofkings-intercap.txt");
//		String characterString = getCharacterString("src/main/resources/data/characters/cok-list-curated.txt");
//		writeFiles(constructMatrix(characterString, text, RADIUS, NOISE, folder + "/log.txt"), folder, 2, 1, "curated");
//		writeFiles(constructMatrix(characterString, text, RADIUS, NOISE, folder + "/log.txt"), folder, 2, 2, "intercap");





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

        ClashOfKingsConstructor gotConstructor = new ClashOfKingsConstructor(fileNum, fileDesc);

        gotConstructor.constructMatrix();
        gotConstructor.writeFiles();
    }

    public ClashOfKingsConstructor(int fileNumber, String fileDescriptor) {
        super(CHARACTER_FILE_NAME, TEXT_FILE_NAME, BOOK_NUMBER, fileNumber, fileDescriptor);
    }

//    public void writeFiles() {
//        writeFiles(VOLUME_NUMBER, fileNumber, fileDescriptor);
//    }


}
