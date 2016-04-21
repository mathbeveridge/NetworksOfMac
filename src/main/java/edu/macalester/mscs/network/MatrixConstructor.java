package edu.macalester.mscs.network;

import edu.macalester.mscs.utils.EntryComparator;
import edu.macalester.mscs.utils.FileUtils;
import edu.macalester.mscs.utils.Logger;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;

public class MatrixConstructor {

	public static final int NOISE = 4;
	public static final int RADIUS = 20;

	public static void main(String[] args) {
		String folder = "src/main/resources/data/logs";

//		String text = getText("src/main/resources/text/gameofthrones.txt");
//		String characterString = getCharacterString("src/main/resources/data/characters/got-list-no-dup.txt");
//		writeFiles(constructMatrix(characterString, text, RADIUS, NOISE, folder + "/log.txt"), folder, 1, 6, "full-names");
//		String characterString = getCharacterString("src/main/resources/data/characters/got-list-curated.txt");
//		writeFiles(constructMatrix(characterString, text, RADIUS, NOISE, folder + "/log.txt"), folder, 1, 7, "dup-names");
//		writeFiles(constructMatrix(characterString, text, 15, NOISE, folder + "/log.txt"), folder, 1, 8, "smaller-radius");
        writeFiles(constructMatrix(characterString, text, 15, NOISE, folder + "/log.txt"), folder, 1, 9, "curated2");
//		writeFiles(constructMatrix(characterString, text, 15, NOISE, folder + "/log.txt"), folder, 1, 8, "smaller-radius");

		String text = getText("src/main/resources/text/clashofkings.txt");
		String characterString = getCharacterString("src/main/resources/data/characters/cok-list-curated.txt");
		writeFiles(constructMatrix(characterString, text, RADIUS, NOISE, folder + "/log.txt"), folder, 2, 1, "curated");
	}

	/**
	 * Reads in a character file where each unique character has a comma-separated line of names,
	 * and converts it to a single string where a character's primary name is followed by their
	 * other aliases separated by '=', and each character is separated from the next by a comma.
	 * This is the proper input string for the getCharacters() and getNameIndices() methods.
	 * @param file
	 * @return
	 */
	public static String getCharacterString(String file) {
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
	public static Matrix constructMatrix(String characterString, String text, int radius, int noise, String logFile) {

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
		Matrix matrix = new Matrix(characters, nameIndices, text, radius);
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

		return matrix;
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
		logger.log("All Encounters:");
		logger.log(matrix.getEncounterList());
		logger.writeLog(encountersFolder + "/_All.csv");
		// write encounters files for each character
		for (String name : matrix.getCharacters()) {
			logger.clear();
			logger.log(name + " Encounters:");
			logger.log(matrix.getEncounterList(name));
			logger.writeLog(encountersFolder + '/' + name.replace(' ', '_') + ".csv");
		}
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
