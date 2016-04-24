package edu.macalester.mscs.network;

/**
 * Created by abeverid on 4/23/16.
 */
public class DanceWithDragonsConstructor extends MatrixConstructor {



    /**
     * Legacy file creation for Dance with Dragons
     */

    //String text = getText("src/main/resources/text/dancewithdragons.txt");
    //String characterString = getCharacterString("src/main/resources/data/characters/dwd-list-no-dup.txt");
    //writeFiles(constructMatrix(characterString, text, RADIUS, NOISE, folder + "/log.txt"), folder, 5, 1, "no-dup");


    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/dwd-list-no-dup.txt";
    public static final String TEXT_FILE_NAME = "src/main/resources/text/dancewithdragons.txt";
    public static final int BOOK_NUMBER = 5;


    /**
     * Main method for generating the matrix, edge list and log files for "A Clash of Kings."
     * @param args
     */
    public static void main(String[] args) {
        int fileNum = 1;
        String fileDesc = "no-dup";

        DanceWithDragonsConstructor dwdConstructor = new DanceWithDragonsConstructor(fileNum, fileDesc);

        dwdConstructor.constructMatrix();
        dwdConstructor.writeFiles();
    }

    public DanceWithDragonsConstructor(int fileNumber, String fileDescriptor) {
        super(CHARACTER_FILE_NAME, TEXT_FILE_NAME, BOOK_NUMBER, fileNumber, fileDescriptor);
    }
}
