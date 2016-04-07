package edu.macalester.mscs.network;

import edu.macalester.mscs.utils.FileUtils;
import edu.macalester.mscs.utils.Logger;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

// TODO: make more modular
public class MatrixConstructor {

	public static final int NOISE = 4;
	// threshold: 3=13
	public static final int REACH = 20;

	public static void main(String[] args) {
//		String characters = getCharacters("src/main/resources/data/characters/ari-list-no-dup.txt");
		String characters = getCharacters("src/main/resources/data/characters/ari-list-curated.txt");
		String text = getText("src/main/resources/text/got.txt");
//		printResultCSV(getData(characters, text, REACH, NOISE, "src/main/resources/data/logs/log.txt"),
//				"src/main/resources/data/logs/GoT1-mat6-full-names.csv");
		printResultCSV(getData(characters, text, REACH, NOISE, "src/main/resources/data/logs/log.txt"),
				"src/main/resources/data/logs/GoT1-mat7-dup-names.csv");
	}

	private static String getCharacters(String file) {
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
			for (int i=1; i<split.length; i++) {
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

		List<String> nameList = new ArrayList<>();
		// nicknames direct to the primary name
		Map<String, Integer> nameIndices = new HashMap<>();

		characters = characters.trim();
		StringBuilder sb = new StringBuilder();
		String last = null;
		for (int i=0; i<=characters.length(); i++) {
			char c = 0;
			if (i < characters.length()) { // handle the last person
				c = characters.charAt(i);
			}
			if (c == ' ' || Character.isAlphabetic(c)) {
				sb.append(c);
			} else {
				int index = nameList.size();
				if (last == null) {
					last = sb.toString();
					String[] split = last.split(" ");
					String name = split[0];
					if (split.length > 1) {
						name += " " + split[1].charAt(0);
					}
					nameList.add(name);
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
		String[] names = nameList.toArray(new String[nameList.size()]);

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

		Matrix matrix = new Matrix(names, nameIndices, text, reach);

		List<Encounter> edgeWeights = matrix.getEncounterList();
		for (Encounter encounter : edgeWeights) {
			logger.log(encounter);
		}

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
		logger.writeLog(logFile);

		return matrix;
    }

	private static void printResultCSV(Matrix matrix, String file) {
		Logger logger = new Logger();
		logger.log(matrix.toLines());
		logger.writeLog(file);
	}

}
