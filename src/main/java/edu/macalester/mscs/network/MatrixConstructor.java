package edu.macalester.mscs.network;

import edu.macalester.mscs.utils.EntryComparator;
import edu.macalester.mscs.utils.FileUtils;
import edu.macalester.mscs.utils.Logger;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;

public class MatrixConstructor {

	public static final int NOISE = 4;
	public static final int REACH = 20;

	public static void main(String[] args) {
		String folder = "src/main/resources/data/logs";
		String text = getText("src/main/resources/text/got.txt");

//		String characters = getCharacters("src/main/resources/data/characters/ari-list-no-dup.txt");
//		writeFiles(constructMatrix(characters, text, REACH, NOISE, folder + "/log.txt"), folder, 1, 6, "full-names");
		String characters = getCharacters("src/main/resources/data/characters/ari-list-curated.txt");
		writeFiles(constructMatrix(characters, text, REACH, NOISE, folder + "/log.txt"), folder, 1, 7, "dup-names");
//		writeFiles(constructMatrix(characters, text, 15, NOISE, folder + "/log.txt"), folder, 1, 8, "less-reach");
	}

	public static String getCharacters(String file) {
		List<String> lines = FileUtils.readFile(file);
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String line : lines) {
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}
			String[] split = StringUtils.split(line, ",");
			String name = split[0].trim();
			sb.append(name);
			for (int i = 1; i < split.length; i++) {
				String nickname = split[i].trim();
				if (!name.equals(nickname)) {
					sb.append("=").append(nickname);
				}
			}
		}
		return sb.toString().trim();
	}

	public static String getText(String file) {
		List<String> lines = FileUtils.readFile(file);
		StringBuilder sb = new StringBuilder();
		for (String line : lines) {
			sb.append(line).append(" ");
		}
		return sb.toString();
	}

	public static Matrix constructMatrix(String characterString, String text, int reach, int noise, String logFile) {

		Logger logger = new Logger();
		logger.log("=============================================================");
		logger.log("=================== PART 1: General Info ====================");
		logger.log("=============================================================");
		logger.log();
		logger.log("Character names input: " + characterString);
		logger.log("Proximity check range: " + reach);
		logger.log("Noise threshold level: " + noise);
		logger.log();
		logger.log("Character list:");
		List<String> characterList = getCharacterList(characterString);
		logger.log(characterList);
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
		Matrix matrix = new Matrix(characterList, nameIndices, text, reach);
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

	private static List<String> getCharacterList(String characterString) {
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

	public static void writeFiles(Matrix matrix, String parentFolder, int bookNumber, int fileNumber, String descriptor) {
		// write matrix file
		matrix.toMatrixCsvLog().writeLog(getFileName(parentFolder, bookNumber, "mat", fileNumber, descriptor, "csv"));
		// write edge file
		matrix.toEdgeListCsvLog().writeLog(getFileName(parentFolder, bookNumber, "edge", fileNumber, descriptor, "csv"));
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
