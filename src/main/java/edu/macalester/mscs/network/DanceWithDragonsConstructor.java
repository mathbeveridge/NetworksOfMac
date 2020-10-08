package edu.macalester.mscs.network;

/**
 * @author Andrew Beveridge
 */
public class DanceWithDragonsConstructor extends MatrixConstructor {

    /**
     * Legacy file creation for Dance with Dragons
     */

    //String text = getText("src/main/resources/text/dancewithdragons.txt");
    //String characterString = getCharacterString("src/main/resources/data/characters/dwd-list-no-dup.txt");
    //writeFiles(constructMatrix(characterString, text, RADIUS, NOISE, folder + "/log.txt"), folder, 5, 1, "no-dup");

    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/dwd-list-11complete.csv";
    public static final String TEXT_FILE_NAME = "src/main/resources/text/dancewithdragons-11complete.txt";
    public static final String CHARACTER_DATA_FILE_NAME = "src/main/resources/data/characters/awoiaf/A-Dance-with-Dragons-characters.csv";
    public static final int BOOK_NUMBER = 5;

    /**
     * Main method for generating the matrix, edge list and log files for "A Dance with Dragons."
     * @param args
     */
    public static void main(String[] args) {
        // use fileNum so you don't override old runs
        // fileDesc lets you comment on the changes from previous run
        int fileNum = 4;
        String fileDesc = "11complete";

        DanceWithDragonsConstructor dwdConstructor = new DanceWithDragonsConstructor(15, 3);

        dwdConstructor.constructMatrix(fileNum, fileDesc, LOG_FOLDER);
        dwdConstructor.writeFiles(fileNum, fileDesc, LOG_FOLDER, false);
    }

    public DanceWithDragonsConstructor(int radius, int noise) {
        super("GoT" + BOOK_NUMBER, TEXT_FILE_NAME, CHARACTER_FILE_NAME, radius, noise);
    }

    public String getCharacterDataFileName() { return CHARACTER_DATA_FILE_NAME; }
}
