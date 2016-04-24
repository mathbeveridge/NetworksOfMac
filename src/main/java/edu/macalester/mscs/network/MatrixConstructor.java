package edu.macalester.mscs.network;

import edu.macalester.mscs.utils.EntryComparator;
import edu.macalester.mscs.utils.FileUtils;
import edu.macalester.mscs.utils.Logger;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;

public class MatrixConstructor {

	public static final int NOISE = 4;
	public static final int RADIUS = 15;
    public static final String LOG_FOLDER = "src/main/resources/data/logs";
    public static final String LOG_FILE_MATRIX = LOG_FOLDER + "/log.txt";


    private String characterFileName;
    private String textFileName;
    private int bookNumber;
    private int fileNumber;
    private String fileDescriptor;
    private int radius;
    private int noise;
    private Matrix matrix;

    /**
     * Constructor
     * @param characterFileName The name of the CSV file containing the list of characters and nicknames
     * @param textFileName The name of the file containing the book itselt
     * @param bookNum The volume in the series. Will appear in the output file names.
     * @param fileNum The increment for the current run. Will appear in the output file names.
     * @param fileDesc A short description of what characterizes this run. Will appear in the output file names.
     */
    public MatrixConstructor(String characterFileName,String textFileName, int bookNum, int fileNum, String fileDesc) {
        this.characterFileName = characterFileName;
        this.textFileName = textFileName;
        this.bookNumber = bookNum;
        this.fileNumber = fileNum;
        this.fileDescriptor = fileDesc;

        // eventually add getters and setters for these
        this.radius = RADIUS;
        this.noise = NOISE;
    }


    /**
     * Creates a Matrix containing all of the encounters
     *
     */
    public void constructMatrix() {
        String text = getText(textFileName);
        String characterString = getCharacterString(characterFileName);

        constructMatrix(characterString, text, radius, noise, LOG_FILE_MATRIX);
    }

    /**
     * Writes out the log files so that the encounters can be disambiguated and varified.
     * Must be called after constructMatrix
     *
     */
    public void writeFiles() {
        writeFiles(matrix, LOG_FOLDER, bookNumber, fileNumber, fileDescriptor);
    }

    /**
     * The main method to produce the matrix, the edge file, and the log files.
     * This main method should be deprecated. Instead, we can use the subclasses. In particular, there are a
     * lot of commented lines below that can be deleted after the code review of the object oriented revision.
     * Those commented lines can now be found in the appropriate subclass.
     *
     * @param args
     */
	public static void main(String[] args) {


		/**
		 * Clash of Kings
		 */

//		String text = getText("src/main/resources/text/clashofkings-intercap.txt");
//		String characterString = getCharacterString("src/main/resources/data/characters/cok-list-curated.txt");
//		writeFiles(constructMatrix(characterString, text, RADIUS, NOISE, folder + "/log.txt"), folder, 2, 1, "curated");
//		writeFiles(constructMatrix(characterString, text, RADIUS, NOISE, folder + "/log.txt"), folder, 2, 2, "intercap");

        // The new way to call the non-static version of MatrixConstructor.
        // Subclasses make the constructor call even easier, since the only arugments that really change are
        // the run number and the descriptor
        MatrixConstructor mc =
                new MatrixConstructor("src/main/resources/data/characters/cok-list-curated.txt",
                        "src/main/resources/text/clashofkings-intercap.txt",
                        2, 3, "test");

        mc.constructMatrix();
        mc.writeFiles();

//		String characterString = getCharacterString("src/main/resources/data/characters/cok-list-curated.txt");
//		writeFiles(constructMatrix(characterString, text, RADIUS, NOISE, folder + "/log.txt"), folder, 2, 1, "curated");
//		writeFiles(constructMatrix(characterString, text, RADIUS, NOISE, folder + "/log.txt"), folder, 2, 2, "intercap");


        /**
         * Dance with Dragons
         */

		//String text = getText("src/main/resources/text/dancewithdragons.txt");
		//String characterString = getCharacterString("src/main/resources/data/characters/dwd-list-no-dup.txt");
		//writeFiles(constructMatrix(characterString, text, RADIUS, NOISE, folder + "/log.txt"), folder, 5, 1, "no-dup");
	}

	/**
	 * Reads in a character file where each unique character has a comma-separated line of names,
	 * and converts it to a single string where a character's primary name is followed by their
	 * other aliases separated by '=', and each character is separated from the next by a comma.
	 * This is the proper input string for the getCharacters() and getNameIndices() methods.
	 * @param file
	 * @return
	 */
	public String getCharacterString(String file) {
		List<String> lines = FileUtils.readFile(file);
		StringBuilder sb = new StringBuilder();
		for (String line : lines) {
			sb.append(line).append('\n');
		}
		return sb.toString().trim().replace(',', '=').replace('\n', ',');
	}

	/**
	 * This method reads in a massive text file and combines it into a single string,
	 * with line breaks replaced by spaces.
	 * @param file
	 * @return
	 */
	public static String getText(String file) {
		List<String> lines = FileUtils.readFile(file);
		StringBuilder sb = new StringBuilder();
		for (String line : lines) {
			sb.append(line).append(" ");
		}
		return sb.toString().trim();
	}

	/**
	 * This primary workhorse method builds a matrix from a characterString, text, and other parameters.
	 * The log data is written to the console and printed to logFile if specified, or ignored if null.
	 * @param characterString
	 * @param text
	 * @param radius
	 * @param noise
	 * @param logFile
	 * @return
	 */
	public void constructMatrix(String characterString, String text, int radius, int noise, String logFile) {

		Logger logger = new Logger();
		logger.log("=============================================================");
		logger.log("=================== PART 1: General Info ====================");
		logger.log("=============================================================");
		logger.log();
		logger.log("Character names input: " + characterString);
		logger.log("Proximity check range: " + radius);
		logger.log("Noise threshold level: " + noise);
		logger.log();
		logger.log("Character list:");
		List<String> characters = getCharacters(characterString);
		logger.log(characters);
		logger.log();
		logger.log("Name Indices:");
		Map<String, Integer> nameIndices = getNameIndices(characterString);
		List<Map.Entry<String, Integer>> entries = new ArrayList(nameIndices.entrySet());
		entries.sort(EntryComparator.ASCENDING);
		logger.log(entries);
		logger.log();
		logger.log();
		logger.log("=============================================================");
		logger.log("================== PART 2: Edge Collection ==================");
		logger.log("=============================================================");
		logger.log();

		matrix = new Matrix(characters, nameIndices, text, radius);

		logger.log(matrix.getEncounterList());
		logger.log();
		logger.log();
		logger.log("=============================================================");
		logger.log("=================== PART 3: Refining Data ===================");
		logger.log("=============================================================");
		logger.log();
		logger.append(matrix.cleanNoise(noise));
		logger.append(matrix.cleanFloaters());
		logger.log();
		logger.log();
		logger.log("=============================================================");
		logger.log("========================= End of log ========================");
		logger.log("=============================================================");
		logger.log();

		if (logFile != null) {
			logger.writeLog(logFile);
		}

	}

	/**
	 * Converts a characterString to a list of character's primary names, where a primary name
	 * is either their first name and last initial, or their single name. The input string must
	 * have each character separated by a comma, and each alias separated by an '=', where the
	 * primary name is the first alias given.
	 * @param characterString
	 * @return
	 */
	private static List<String> getCharacters(String characterString) {
		List<String> characterList = new ArrayList<>();
		String[] characters = characterString.split(",");
		for (String c : characters) {
			c = StringUtils.substringBefore(c, "=");
			c = c.replaceAll(" [a-z]+ ", " "); // remove lowercase filler words
			if (c.contains(" ")) {
				c = c.substring(0, c.indexOf(' ') + 2);
			}
			characterList.add(c);
		}
		return characterList;
	}

	/**
	 * Converts a characterString to a map of aliases to primary name indices, where the index is
	 * the position of the primary name in the output of getCharacters(). The input string must
	 * have each character separated by a comma, and each alias separated by an '=', where the
	 * primary name is the first alias given.
	 * @param characterString
	 * @return
	 */
	private static Map<String, Integer> getNameIndices(String characterString) {
		Map<String, Integer> nameIndices = new HashMap<>();
		int index = 0;
		String[] characters = characterString.split(",");
		for (String c : characters) {
			String[] split = c.split("=");
			for (String name : split) {
				nameIndices.put(name, index);
			}
			index++;
		}
		return nameIndices;
	}

	/**
	 * Writes the data in matrix to a set of files. These files are:
	 *  - matrix file
	 *  - edge file
	 *  - total encounter list
	 *  - encounter lists by character
	 * The files will be descriptively named and placed within parentFolder,
	 * with the encounter files placed in their own subdirectory.
	 * @param matrix
	 * @param parentFolder
	 * @param bookNumber
	 * @param fileNumber
	 * @param descriptor
	 */
	public static void writeFiles(Matrix matrix, String parentFolder, int bookNumber, int fileNumber, String descriptor) {
		// write encounters file
		String encountersFolder = getFileName(parentFolder, bookNumber, "encounters", fileNumber, descriptor);
		Logger logger = new Logger();
		logger.log("char 1, char2, index, text");
		logger.log(matrix.getEncounterList());
		logger.writeLog(encountersFolder + "/_All.csv");
		// no longer write encounters files for each character, but keeping the code for now, just in case.
//		for (String name : matrix.getCharacters()) {
//			logger.clear();
//			logger.log(name + " Encounters:");
//			logger.log(matrix.getEncounterList(name));
//			logger.writeLog(encountersFolder + '/' + name.replace(' ', '_') + ".csv");
//		}
		// write matrix file
		matrix.toMatrixCsvLog().writeLog(getFileName(parentFolder, bookNumber, "mat", fileNumber, descriptor, "csv"));
		// write edge file
		matrix.toEdgeListCsvLog().writeLog(getFileName(parentFolder, bookNumber, "edge", fileNumber, descriptor, "csv"));
	}

	/**
	 * Returns a file name for a directory in parentFolder, with name
	 *
	 * GoT[bookNumber]-[type][fileNumber]-[descriptor]
	 *
	 * Note that descriptor is optional, and if specified as null or
	 * the empty string, it and the final hyphen will not be included
	 *
	 * @param parentFolder
	 * @param bookNumber
	 * @param type
	 * @param fileNumber
	 * @param descriptor
	 * @return
	 */
	public static String getFileName(String parentFolder, int bookNumber, String type,
									  int fileNumber, String descriptor) {
		String fileName = getFileName(parentFolder, bookNumber, type, fileNumber, descriptor, null);
		new File(fileName).mkdirs();
		return fileName;
	}

	/**
	 * Returns a file name in parentFolder, with name
	 *
	 * GoT[bookNumber]-[type][fileNumber]-[descriptor].[extension]
	 *
	 * Note that descriptor is optional, and if specified as null or the empty string,
	 * it and the final hyphen will not be included. Also if the '.' is included in
	 * extension, or if extension is specified as null or the empty string, the '.'
	 * will not be included. However, the getFileName method without the extension
	 * parameter should be used instead because that method also calls mkdirs() for
	 * the returned file name.
	 *
	 * @param parentFolder
	 * @param bookNumber
	 * @param type
	 * @param fileNumber
	 * @param descriptor
	 * @param extension
	 * @return
	 */
	public static String getFileName(String parentFolder, int bookNumber, String type,
									  int fileNumber, String descriptor, String extension) {
		if (descriptor == null) {
			descriptor = "";
		} else if (!descriptor.isEmpty()) {
			descriptor = '-' + descriptor;
		}
		if (extension == null) {
			extension = "";
		} else if (!extension.isEmpty() && !extension.startsWith(".")) {
			extension = '.' + extension;
		}
		return parentFolder + "/GoT" + bookNumber + "-" + type + fileNumber + descriptor + extension;
	}

}
