package edu.macalester.mscs.network;

import edu.macalester.mscs.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class MatrixConstructor {

	public static void main(String[] args){
		printResultCSV(getData(CharacterLists.NEW_SHORT_LIST, getText("src/main/resources/text/got.txt")),
				"src/main/resources/data/logs/GoT1-16-4-matrix2.csv");
	}

	public static final int NOISE = 4;
	// threshold: 3=13
	public static final int REACH = 20;

	public static String getText(String file) {
		List<String> lines = FileUtils.readFile(file);
		StringBuilder sb = new StringBuilder();
		for (String line : lines) {
			sb.append(line).append(" ");
		}
		return sb.toString();
	}

	public static MatrixAndNames getData(String characters, String text) {

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

		System.out.println("=============================================================");
		System.out.println("=================== PART 1: General Info ====================");
		System.out.println("=============================================================");
		System.out.println("Book: A Game of Thrones");
		System.out.println("Character names input: " + characters);
		System.out.println("Proximity check range: " + REACH);
		System.out.println("Noise threshold level: " + NOISE);
		System.out.println();

		List<Encounter> edgeWeights = new ArrayList<>();
		for (int i=0; i < input.length; i++) { // iterate through text
			for (String primary : nicknameRedirects.keySet()) { // iterate through names
				if (isName(input[i], primary)) {
					int m1 = nicknameRedirects.get(primary);
//					Set<Integer> neighbors = new HashSet<>();
					// search adjacent words in either direction
					for (int j = i - 1; j >= Math.max(0, i - REACH); j--) {
						for (String secondary : nicknameRedirects.keySet()) {
							if (isName(input[j], secondary)) {
								int m2 = nicknameRedirects.get(secondary);
								if (m1 != m2) {
									matrix[m1][m2]++;
									matrix[m2][m1]++;
//									neighbors.add(m2);

									String context = "";
									for (int contextInd = Math.min(i, j) - 5; contextInd < Math.max(i, j) + 5; contextInd++) {
										try {
											context += input[contextInd] + " ";
										} catch (Exception ignored) {
										}
									}
									edgeWeights.add(new Encounter(primary, secondary, i, j, context));
								}
							}
						}
					}
//					for (int m2 : neighbors) {
//						matrix[m1][m2]++;
//						matrix[m2][m1]++;
//					}
				}
			}
        }

		System.out.println();
		System.out.println();
		System.out.println("=============================================================");
		System.out.println("================== PART 2: Edge Collection ==================");
		System.out.println("=============================================================");
		// This section is exclusively printing out stuff, has no actual code purpose
		System.out.println();
		System.out.println("============ By Character (including alt. names): ===========");

		for (String name : nicknameRedirects.keySet()) {
            System.out.println();
            System.out.println(name + ":");
            for (Encounter edgeWeight : edgeWeights) {
                if (edgeWeight.character1.equals(name)) {
                    System.out.println(edgeWeight.toString());
                }
            }
            for (Encounter edgeWeight : edgeWeights) {
                if (edgeWeight.character2.equals(name)) {
                    System.out.println(edgeWeight.toString());
                }
            }
        }

		System.out.println();
		System.out.println("============ By Proximity (including alt. names): ===========");

		for (int i = REACH; i > 0; i--) {
            System.out.println();
            System.out.println("Proximity = " + i + "-word :");
            for (Encounter edgeWeight : edgeWeights) {
                if (edgeWeight.proximity == i) {
                    System.out.println(edgeWeight.toString());
                }
            }
        }

		System.out.println();
		System.out.println();
		System.out.println("=============================================================");
		System.out.println("=================== PART 3: Refining Data ===================");
		System.out.println("=============================================================");
		System.out.println();

		MatrixAndNames data = new MatrixAndNames(matrix, names);
		data.cleanNoise(NOISE);

		System.out.println();
		System.out.println();
		System.out.println("=============================================================");
		System.out.println("=================== PART 4: Matrix as CSV ===================");
		System.out.println("=============================================================");
		System.out.println();

		printResultCSV(data, "src/main/resources/data/logs/GoT1-16-4-matrix.csv");

		System.out.println();
		System.out.println();
		System.out.println("=============================================================");
		System.out.println("========================= End of log ========================");
		System.out.println("=============================================================");
		return data;
    }

	private static boolean isName(String input, String name) {
		// this way accounts for non-letter characters, but ignores word containment
		return input.matches("(.*\\W)*" + name + "(\\W.*)*");
//		return input.contains(name); // the old way
	}

	private static void printResultCSV(MatrixAndNames data, String file) {
		List<String> lines = new ArrayList<>();
		String line = StringUtils.strip(Arrays.toString(data.getNames()), "[]").replaceAll(" ", "");
		lines.add(line);
		System.out.println(line);
		for(int i=0; i<data.getMatrix().length; i++){
			line = StringUtils.strip(Arrays.toString(data.getMatrix()[i]), "[]").replaceAll(" ", "");
			lines.add(line);
			System.out.println(line);
		}
		FileUtils.writeFile(lines, file);
	}

}
