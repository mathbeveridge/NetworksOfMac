package edu.macalester.mscs.network;

import edu.macalester.mscs.utils.FileUtils;
import edu.macalester.mscs.utils.Logger;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: make more modular
public class MatrixConstructor {

	public static final int NOISE = 4;
	// threshold: 3=13
	public static final int REACH = 20;

	public static void main(String[] args) {
		String folder = "src/main/resources/data/logs";
		String text = getText("src/main/resources/text/got.txt");

//		String characters = getCharacters("src/main/resources/data/characters/ari-list-no-dup.txt");
//		writeFiles(getData(characters, text, REACH, NOISE, folder + "/log.txt"), folder, 1, 6, "full-names");
		String characters = getCharacters("src/main/resources/data/characters/ari-list-curated.txt");
		writeFiles(getData(characters, text, REACH, NOISE, folder + "/log.txt"), folder, 1, 7, "dup-names");
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
		return sb.toString();
	}

	public static String getText(String file) {
		List<String> lines = FileUtils.readFile(file);
		StringBuilder sb = new StringBuilder();
		for (String line : lines) {
			sb.append(line).append(" ");
		}
		return sb.toString();
	}

	public static Matrix getData(String characters, String text, int reach, int noise, String logFile) {

		Logger logger = new Logger();

		List<String> characterList = new ArrayList<>();
		// nicknames direct to the primary name
		Map<String, Integer> nameIndices = new HashMap<>();

		characters = characters.trim();
		StringBuilder sb = new StringBuilder();
		String last = null;
		for (int i = 0; i <= characters.length(); i++) {
			char c = 0;
			if (i < characters.length()) { // handle the last person
				c = characters.charAt(i);
			}
			if (c == ' ' || Character.isAlphabetic(c)) {
				sb.append(c);
			} else {
				int index = characterList.size();
				if (last == null) {
					last = sb.toString();
					String[] split = last.split(" ");
					String name = split[0];
					if (split.length > 1) {
						name += " " + split[1].charAt(0);
					}
					characterList.add(name);
					nameIndices.put(last, index);
				} else {
					nameIndices.put(sb.toString(), index - 1);
				}
				sb = new StringBuilder();
			}
			if (c == ',') {
				last = null;
			}
		}

		logger.log("=============================================================");
		logger.log("=================== PART 1: General Info ====================");
		logger.log("=============================================================");
		logger.log();
		logger.log("Character names input: " + characters);
		logger.log("Proximity check range: " + reach);
		logger.log("Noise threshold level: " + noise);
		logger.log();
		logger.log();
		logger.log("=============================================================");
		logger.log("================== PART 2: Edge Collection ==================");
		logger.log("=============================================================");

		Matrix matrix = new Matrix(characterList, nameIndices, text, reach);
		logger.log(matrix.getEncounterList());

		logger.log();
		logger.log();
		logger.log("=============================================================");
		logger.log("=================== PART 3: Refining Data ===================");
		logger.log("=============================================================");
		logger.log();

		logger.log(matrix.cleanNoise(noise));
		logger.log(matrix.cleanFloaters());

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

	private static void writeFiles(Matrix matrix, String parentFolder, int bookNumber, int fileNumber, String descriptor) {
		// write matrix file
		Logger logger = new Logger();
		logger.log(matrix.toMatrixCsvLines());
		logger.writeLog(getFileName(parentFolder, bookNumber, "mat", fileNumber, descriptor, "csv"));

		// write edge file
		logger.clear();
		logger.log(matrix.toEdgeListCsvLines());
		logger.writeLog(getFileName(parentFolder, bookNumber, "edge", fileNumber, descriptor, "csv"));

		// write encounters by character
		for (String name : matrix.getCharacters()) {
			logger.clear();
			logger.log(name + " Encounters:");
			logger.log(matrix.getEncounterList(name));
			logger.writeLog(getFileName(parentFolder, bookNumber, "encounters", fileNumber, descriptor)
					+ '/' + name.replace(' ', '_') + ".csv");
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
	private static String getFileName(String parentFolder, int bookNumber, String type,
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
	 * @param extention
	 * @return
	 */
	private static String getFileName(String parentFolder, int bookNumber, String type,
									  int fileNumber, String descriptor, String extention) {
		if (descriptor == null) {
			descriptor = "";
		} else if (!descriptor.isEmpty()) {
			descriptor = '-' + descriptor;
		}
		if (extention == null) {
			extention = "";
		} else if (!extention.isEmpty() && !extention.startsWith(".")) {
			extention = '.' + extention;
		}
		return parentFolder + "/GoT" + bookNumber + "-" + type + fileNumber + descriptor + extention;
	}

}
