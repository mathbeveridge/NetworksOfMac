package edu.macalester.mscs.network;

import edu.macalester.mscs.utils.FileUtils;

import java.util.List;

/**
 * @author Andrew Beveridge
 */
public class GameOfThronesConstructor extends MatrixConstructor {

    /**
     * Legacy file creation for Game of Thrones
     */

//		String text = getText("src/main/resources/text/gameofthrones-intercap.txt");
//		String characterString = getCharacterString("src/main/resources/data/characters/got-list-curated.txt");

//		String characterString = getCharacterString("src/main/resources/data/characters/got-list-no-dup.txt");
//		writeFiles(constructMatrix(characterString, text, RADIUS, NOISE, folder + "/log.txt"), folder, 1, 6, "full-names");
//		writeFiles(constructMatrix(characterString, text, RADIUS, NOISE, folder + "/log.txt"), folder, 1, 7, "dup-names");
//		writeFiles(constructMatrix(characterString, text, 15, NOISE, folder + "/log.txt"), folder, 1, 8, "smaller-radius");

//		writeFiles(constructMatrix(characterString, text, 15, NOISE, folder + "/log.txt"), folder, 1, 9, "curated2");
//		writeFiles(constructMatrix(characterString, text, RADUIS, NOISE, folder + "/log.txt"), folder, 1, 10, "intercap");

    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/got-list-curated-hyphenated.csv";
    public static final String ORDERED_CHARACTER_FILE_NAME = "src/main/resources/data/characters/got-community-ordered.txt";
    public static final String TEXT_FILE_NAME = "src/main/resources/text/gameofthrones-hyphenated-update.txt";
    public static final int BOOK_NUMBER = 1;

//    public static final String[] GOT1_ORDERED_CHAR = {
//            "AemonT", "AlliserT", "BenjenS", "BowenM", "Chett", "Dareon", "DonalN", "Grenn", "Halder", "Hobb",
//            "JaremyR", "JeorM", "JonS", "Pyp", "RandyllT", "SamwellT", "Yoren",
//
//            "AerysT", "Alyn", "BarristanS", "BrandonS", "Cayn", "CerseiL", "Desmond", "EddardS", "Harwin",
//            "Hullen", "JanosS", "JonA", "JoryC", "Lyanna", "Malleon", "PetyrB", "Pycelle", "RaymunD", "RenlyB",
//            "RhaegarT", "RobertB", "StannisB", "TobhoM", "Tom", "TorrhenK", "Varys", "VayonP",
//
//            "Aggo", "Cohollo", "DaenerysT", "Doreah", "Drogo", "Haggo", "IllyrioM", "Irri", "Jhiqui", "Jhogo",
//            "JorahM", "MirriM", "Ogo", "Qotho", "Quaro", "Rakharo", "Rhaego", "ViserysT",
//
//            "AddamM", "Bronn", "Chiggen", "Colemon", "Conn", "JaimeL", "Jyck", "KevanL", "Lefford", "LysaA",
//            "Marillion", "Mord", "Morrec", "RobertA", "Shae", "Shagga", "Timett", "TyrionL", "TywinL", "VardisE",
//            "WillisW",
//
//            "BranS", "BryndenT", "CatelynS", "DonnelW", "EdmureT", "GalbartG", "GreatjonU", "HalM", "HallisM",
//            "Hodor", "HosterT", "KarylV", "Luwin", "MaegeM", "MarqP", "MashaH",
//            "Nan", "Osha", "RickardK", "RickonS", "RobbS", "RodrikC", "RooseB", "StevronF", "TheonG", "WalderF",
//
//            "AryaS", "BericD", "BorosB", "GregorC", "IlynP", "JeyneP", "JoffreyB", "LorasT", "Maegor", "MerynT",
//            "Mycah", "Myrcella", "SandorC", "SansaS", "SeptaM", "SyrioF", "Tommen"
//    };


    /**
     * Main method for generating the matrix, edge list and log files for "A Game of Thrones."
     * @param args
     */
    public static void main(String[] args) {
        int fileNum = 14;
        String fileDesc = "hyphen";

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
            List<String> orderedCharList = FileUtils.readFile(ORDERED_CHARACTER_FILE_NAME);
            return orderedCharList.toArray(new String[orderedCharList.size()]);
        } catch (Exception e) {
            return null;
        }
    }

}
