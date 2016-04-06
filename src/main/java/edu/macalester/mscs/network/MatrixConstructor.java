package edu.macalester.mscs.network;

import edu.macalester.mscs.utils.FileUtils;
import edu.macalester.mscs.utils.Logger;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class MatrixConstructor {

	public static final int NOISE = 4;
	// threshold: 3=13
	public static final int REACH = 20;

	public static void main(String[] args){
		String characters = getCharacters("src/main/resources/data/characters/ari-list-first.txt");
		String text = getText("src/main/resources/text/got.txt");
		printResultCSV(getData(characters, text, REACH, NOISE, "src/main/resources/data/logs/log.txt"),
				"src/main/resources/data/logs/GoT1-16-4-matrix3.csv");
	}

	private static String getCharacters(String file) {
		List<String> lines = FileUtils.readFile(file);
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String line : lines) {
			if (first) {
				first = false;
			} else {
				sb.append(" ");
			}
			String[] split = StringUtils.split(line, " ,");
			sb.append(split[0]);
			for (int i=1; i<split.length; i++) {
				if (!split[0].equals(split[i])) {
					sb.append("=").append(split[i]);
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

	public static MatrixAndNames getData(String characters, String text, int reach, int noise, String logFile) {

		Logger logger = new Logger();

		List<String> nameList = new ArrayList<>();
		Map<String, Integer> nameIndices = new HashMap<>();
		Map<String, Integer> nicknameRedirects = new HashMap<>();

		characters = characters.trim();
		StringBuilder sb = new StringBuilder();
		String last = null;
		for (int i=0; i<=characters.length(); i++) {
			char c = 0;
			if (i < characters.length()) { // handle the last person
				c = characters.charAt(i);
			}
			if (Character.isAlphabetic(c)) {
				sb.append(c);
			} else {
				int index = nameIndices.size();
				if (last == null) {
					last = sb.toString();
					nameList.add(last);
					nameIndices.put(last, index);
					nicknameRedirects.put(last, index);
				} else {
					nicknameRedirects.put(sb.toString(), index - 1);
				}
				sb = new StringBuilder();
			}
			if (c == ' ') {
				last = null;
			}
		}

		// split into every distinct name, independent of people
		int nameCount = nameIndices.size();
		String[] names = nameList.toArray(new String[nameCount]);
		int[][] matrix = new int[nameCount][nameCount];

		String[] input = text.split(" ");

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

		// TODO: avoid multiple hits per encounter
		// TODO: allow for multi-word names
		List<Encounter> edgeWeights = new ArrayList<>();
		Queue<String> nameQueue = new ArrayDeque<>(reach + 1);
		for (String chunk : input) { // iterate through text
			// get the current name
			String primary = "";
			for (String name : nicknameRedirects.keySet()) {
				if (isName(chunk, name)) {
					primary = name;
				}
			}
			// tally neighbors
			if (!primary.isEmpty()) {
				int index1 = nicknameRedirects.get(primary);
				for (String secondary : nameQueue) {
					if (!secondary.isEmpty()) {
						int index2 = nicknameRedirects.get(secondary);
						if (index1 != index2) {
                            matrix[index1][index2]++;
                            matrix[index2][index1]++;
                            String context = "";
                            for (int contextInd = Math.min(index1, index2) - 5; contextInd < Math.max(index1, index2) + 5; contextInd++) {
                                try {
                                    context += input[contextInd] + " ";
                                } catch (Exception ignored) {
                                }
                            }
                            edgeWeights.add(new Encounter(primary, secondary, index1, index2, context));
                        }
					}
				}
			}
			// update the queue
			nameQueue.add(primary);
			if (nameQueue.size() > reach) { // limit its size
				nameQueue.poll();
			}
		}

		// This section is exclusively printing out stuff, has no actual code purpose
		logger.log();
		logger.log("============ By Character (including alt. names): ===========");

		for (String name : nicknameRedirects.keySet()) {
			logger.log();
            logger.log(name + ":");
            for (Encounter edgeWeight : edgeWeights) {
                if (edgeWeight.character1.equals(name)) {
					logger.log(edgeWeight.toString());
                }
            }
            for (Encounter edgeWeight : edgeWeights) {
                if (edgeWeight.character2.equals(name)) {
					logger.log(edgeWeight.toString());
				}
			}
		}

		logger.log();
		logger.log("============ By Proximity (including alt. names): ===========");

		for (int i = reach; i > 0; i--) {
			logger.log();
			logger.log("Proximity = " + i + "-word :");
            for (Encounter edgeWeight : edgeWeights) {
                if (edgeWeight.proximity == i) {
					logger.log(edgeWeight.toString());
				}
			}
		}

		logger.log();
		logger.log();
		logger.log("=============================================================");
		logger.log("=================== PART 3: Refining Data ===================");
		logger.log("=============================================================");
		logger.log();

		MatrixAndNames data = new MatrixAndNames(matrix, names);
		data.cleanNoise(noise);

		logger.log();
		logger.log();
		logger.log("=============================================================");
		logger.log("========================= End of log ========================");
		logger.log("=============================================================");
		logger.log();
		logger.writeLog(logFile);

		return data;
    }

	private static boolean isName(String input, String name) {
		return input.matches("(.*\\W)*" + name + "(\\W.*)*");
	}

	private static void printResultCSV(MatrixAndNames data, String file) {
		Logger logger = new Logger();
		logger.log(cleanArrayString(Arrays.toString(data.getNames())));
		for (int[] row : data.getMatrix()) {
			logger.log(cleanArrayString(Arrays.toString(row)));
		}
		logger.writeLog(file);
	}

	private static String cleanArrayString(String arrayString) {
		return StringUtils.strip(arrayString, "[]").replaceAll(" ", "");
	}
}
