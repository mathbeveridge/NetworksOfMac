package edu.macalester.mscs.network;

import edu.macalester.mscs.utils.FileUtils;

import java.util.List;

/**
 * @author Andrew Beveridge
 */
public class GameOfThronesConstructor extends MatrixConstructor {

    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/got-list-merged.csv";
    public static final String CHARACTER_DATA_FILE_NAME = "src/main/resources/data/characters/awoiaf/A-Game-of-Thrones-characters.csv";
    public static final String ORDERED_CHARACTER_FILE_NAME = "src/main/resources/data/characters/got-top-ordered.csv";
    public static final String TEXT_FILE_NAME = "src/main/resources/text/gameofthrones-merged.txt";

    public static final int BOOK_NUMBER = 1;

    /**
     * Main method for generating the matrix, edge list and log files for "A Game of Thrones."
     * @param args
     */
    public static void main(String[] args) {
        int fileNum = 19;
        String fileDesc = "complete";

        GameOfThronesConstructor constructor = new GameOfThronesConstructor(15, 3);

        constructor.constructMatrix(fileNum, fileDesc, LOG_FOLDER);
        constructor.writeFiles(fileNum, fileDesc, LOG_FOLDER, false);
    }

    public GameOfThronesConstructor(int radius, int noise) {
        super(BOOK_NUMBER, TEXT_FILE_NAME, CHARACTER_FILE_NAME, radius, noise);
    }

    /**
     * Returns a list of characters that are ordered according to communities.
     * @return The list of characters in ORDERED_CHARACTER_FILE_NAME
     */
    public String[] getOrderedCharacters() {
        try {
            List<String> charLines = FileUtils.readFile(ORDERED_CHARACTER_FILE_NAME);
            String[] orderedChars = new String[charLines.size()-1];

            for (int i=0; i < orderedChars.length; i++) {
                orderedChars[i] = charLines.get(i).split(",")[0];
            }

            return orderedChars;
        } catch (Exception e) {
            return null;
        }
    }

    public String getCharacterDataFileName() { return CHARACTER_DATA_FILE_NAME; }

}
