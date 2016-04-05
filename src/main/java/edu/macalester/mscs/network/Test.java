package edu.macalester.mscs.network;

import edu.macalester.mscs.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Test {

	public static final String APOSTROPHE = "�";

	public static void main(String[] args){
		//System.out.println("===== Start");
		getData();
		//System.out.println("===== End");
	}

	public static final int NOISE = 4;
	// threshold: 3=13
	public static final int REACH = 20;

	public static MatrixAndNames getData() {
		String book = "A Game of Thrones";
		List<String> lines = FileUtils.readFile("src/main/resources/text/got.txt");

		// Read the file into a MASSIVE single line string
		StringBuilder sb = new StringBuilder();
		for (String line : lines) {
            sb.append(line).append(" ");
        }
		String text = sb.toString();

		String nameInput = CharacterLists.NEW_SHORT_LIST;

//					+"Qhorin Kraznys Qyburn Craster Gilly Styr";
		Map<String, List<String>> nicknameMap = new HashMap<>();
		Map<String, String> nicknameRedirects = new HashMap<>();
		String[] nicknames = nameInput.split(" ");
		for (String nickname : nicknames) {
			String[] split = nickname.split("=");
			for (int j = 0; j < split.length; j++) {
				nicknameRedirects.put(split[j], split[0]);
				if (j == 0) {
					nicknameMap.put(split[0], new ArrayList<String>());
				} else {
					nicknameMap.get(split[0]).add(split[j]);
				}
			}
        }
		// split into every distinct name, independent of people
		String[] names = nameInput.split("[ =]");
		int nameCount = names.length;
		int[][] matrix = new int[nameCount][nameCount];
		int uniqueNameCount = nameCount - nicknameRedirects.size();
		int[][] matrix2 = new int[uniqueNameCount][uniqueNameCount];

		String[] input = text.split(" ");

		System.out.println("=============================================================");
		System.out.println("=================== PART 1: General Info ====================");
		System.out.println("=============================================================");
		System.out.println("Book: " + book);
		System.out.println("Character names input: ");
		System.out.println(nameInput);
		System.out.println("Proximity check range: " + REACH);
		System.out.println("Noise threshold level: " + NOISE);
		System.out.println();
		System.out.println();

		List<Encounter> edgeWeights = new ArrayList<>();
		for (int i=0; i < input.length; i++) { // iterate through text
			for (int n1=0; n1 < nameCount; n1++) { // iterate through names
				String primary = names[n1];
				if (isName(input[i], primary)) {
					Set<Integer> neighbors = new HashSet<>();
					// search adjacent words in either direction
                    for (int j = Math.max(0, i-REACH); j<=Math.min(input.length-1, i+REACH); j++) {
                        for (int n2=0; n2 < nameCount; n2++){
							String secondary = names[n2];
							if (n1 < n2 && isName(input[j], secondary)) {
//								neighbors.add(n2);
								if (!nicknameRedirects.get(primary).equals(nicknameRedirects.get(secondary))) {
                                    matrix[n1][n2]++;
                                    matrix[n2][n1]++;
                                    String context = "";
                                    for (int contextInd = Math.min(i, j) - 5; contextInd <  Math.max(i, j) + 5; contextInd++) {
                                        try{
                                            context += input[contextInd] + " ";
                                        } catch(Exception ignored) {}
                                    }
                                    edgeWeights.add(new Encounter(primary, secondary, i, j, context));
                                }
							}
                        }
                    }
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

		for (String name : names) {
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
		data.clean(nicknameMap, NOISE);

		System.out.println();
		System.out.println();
		System.out.println("=============================================================");
		System.out.println("=================== PART 4: Matrix as CSV ===================");
		System.out.println("=============================================================");
		System.out.println();

		printResultCSV(data, "src/main/resources/data/logs/GoT1-16-4-matrix2.csv");

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
