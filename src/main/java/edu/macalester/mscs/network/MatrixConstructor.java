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
		String characters = getCharacters("src/main/resources/data/characters/ari-list-curated.txt");
		String text = getText("src/main/resources/text/got.txt");
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

	public static MatrixAndNames getData(String characters, String text, int reach, int noise, String logFile) {

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
					nameList.add(last);
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

		// split into every distinct name, independent of people
		int nameCount = nameList.size();
		String[] names = nameList.toArray(new String[nameCount]);
		int[][] matrix = new int[nameCount][nameCount];

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

		List<Encounter> edgeWeights = buildMatrix2(matrix, nameIndices, text, reach);

		// This section is exclusively printing out stuff, has no actual code purpose
		logger.log();
		logger.log("============ By Character (including alt. names): ===========");

		for (String name : nameIndices.keySet()) {
			logger.log();
            logger.log(name + ":");
            for (Encounter edgeWeight : edgeWeights) {
                if (edgeWeight.character1.equals(name)) {
					logger.log(edgeWeight);
                }
            }
            for (Encounter edgeWeight : edgeWeights) {
                if (edgeWeight.character2.equals(name)) {
					logger.log(edgeWeight);
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
		logger.log(data.cleanNoise(noise));
//		logger.log(data.cleanSingletons());

		logger.log();
		logger.log("=============================================================");
		logger.log("========================= End of log ========================");
		logger.log("=============================================================");
		logger.log();
		logger.writeLog(logFile);

		return data;
    }

	private static List<Encounter> buildMatrix(int[][] matrix, Map<String, Integer> nameIndices, String text, int reach) {
		String[] input = text.split(" ");
		List<Encounter> edgeWeights = new ArrayList<>();
		Queue<String> nameQueue = new ArrayDeque<>(reach + 1);
		for (int i = 0; i < input.length; i++) {
			String chunk = input[i]; // iterate through text
			// get the current name
			String primary = "";
			for (String name : nameIndices.keySet()) {
				if (containsName(chunk, name)) {
					primary = name;
				}
			}
			// tally neighbors
			if (!primary.isEmpty()) {
				int index1 = nameIndices.get(primary);
				for (String secondary : nameQueue) {
					if (!secondary.isEmpty()) {
						int index2 = nameIndices.get(secondary);
						if (index1 != index2) {
							matrix[index1][index2]++;
							matrix[index2][index1]++;
							StringBuilder context = new StringBuilder();
							for (int j = i - 25; j < i + 5; j++) {
								try {
									context.append(input[j]).append(" ");
								} catch (Exception ignored) {}
							}
							edgeWeights.add(new Encounter(primary, secondary, i, context.toString()));
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
		return edgeWeights;
	}

	private static List<Encounter> buildMatrix2(int[][] matrix, Map<String, Integer> nameIndices, String text, int reach) {
		List<Encounter> edgeWeights = new ArrayList<>();
		StringBuilder search = new StringBuilder();
		Queue<String> nameQueue = new ArrayDeque<>(reach + 1);
		for (int i = 0; i < text.length() - 1; i++) {
			char c = text.charAt(i);
			String primary = "";
			boolean notLetter = !Character.isAlphabetic(c);
			if (notLetter) {
				for (String name : nameIndices.keySet()) {
					if (endsWithName(search.toString(), name)) {
						primary = name;
					}
				}
			}
			// tally neighbors
			if (!primary.isEmpty()) {
				int index1 = nameIndices.get(primary);
				Set<Integer> neighbors = new HashSet<>();
				for (String secondary : nameQueue) {
					if (!secondary.isEmpty()) {
						int index2 = nameIndices.get(secondary);
						if (index1 != index2) {
							neighbors.add(index2);
							edgeWeights.add(new Encounter(primary, secondary, i, search.toString()));
						}
					}
				}
				for (int n : neighbors) {
					matrix[index1][n]++;
					matrix[n][index1]++;
				}
			}
			// update the search string
			search.append(c);
			// cut the string to size
			if (StringUtils.countMatches(search, ' ') > reach) {
				search = new StringBuilder(search.substring(search.indexOf(" ") + 1));
			}
			// update the queue if a word just finished
			if (i > 0 && notLetter && Character.isAlphabetic(text.charAt(i - 1))) {
				nameQueue.add(primary);
				if (nameQueue.size() > reach) { // limit its size
                    nameQueue.poll();
                }
			}
		}
		return edgeWeights;
	}

	private static boolean containsName(String input, String name) {
		return input.matches("(.*\\W)?" + name + "(\\W.*)?");
	}

	/**
	 * Returns true if a string ends with a name
	 * @param input
	 * @param name
	 * @return
	 */
	private static boolean endsWithName(String input, String name) {
		return input.matches("(.*\\W)?" + name);
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
