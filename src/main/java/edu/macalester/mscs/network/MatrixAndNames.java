package edu.macalester.mscs.network;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MatrixAndNames {
	
	private int[][] matrix;
	private String[] names;
	
	public MatrixAndNames(int[][] inputMatrix, String[] inputNames) {
		this.matrix = inputMatrix;
		this.names = inputNames;
		if (matrix.length != names.length) {
			throw new IllegalArgumentException("Yo lengths be whack yo");
		}
	}

	public int[][] getMatrix() {
		return matrix;
	}

	public String[] getNames() {
		return names;
	}

	public int length() {
		return names.length;
	}

	public void clean(Map<String, List<String>> nicknameMap, int noise) {
		// total nickname data and get nicknames to be removed
		Set<Integer> removed = new HashSet<>();
		for (int i=0; i<length(); i++) {
			if (nicknameMap.containsKey(names[i])) {
				int j = i + 1;
				while (j < length() && nicknameMap.get(names[i]).contains(names[j])) {
					for (int k=0; k<length(); k++){
						if (k != i) {
							matrix[k][i] += matrix[k][j];
							matrix[i][k] = matrix[k][i];
						}
					}
					removed.add(j);
					j++;
				}
			}
		}
		// clean noise
		for (int i=0; i<length(); i++) {
			for (int j=0; j<length(); j++) {
				if (matrix[i][j] < noise && matrix[i][j] > 0) {
					// remove really weak connections
					System.out.println(names[i] +  ", " + names[j] + ", " + matrix[i][j]);
					matrix[i][j] = 0;
				}
			}
			// make sure the diagonal is 0
			matrix[i][i] = 0;
		}
		// get people without connections
		for (int i=0; i<length(); i++) {
			boolean single = true;
			for (int j=0; j<length() && single; j++) {
				if (matrix[i][j] > 0) {
					single = false;
				}
			}
			if (single) {
				removed.add(i);
			}
		}
		// actually remove the data
		int newLength = length() - removed.size();
		String[] cleanNames = new String[newLength];
		int[][] cleanMatrix = new int[newLength][newLength];
		System.out.println();
		System.out.print("Names removed(s): ");
		int row=0;
		for(int i=0; i < length(); i++){
			if (!removed.contains(i)) {
				cleanNames[row] = names[i]; // clean names
				int col = 0;
				for (int j=0; j<length(); j++) { // clean matrix
					if (!removed.contains(j)) {
						cleanMatrix[row][col] = matrix[i][j];
						col++;
					}
				}
				row++;
			} else {
				System.out.print(names[i]+" ");
			}
		}
		names = cleanNames;
		matrix = cleanMatrix;
	}

	public void clean2(Map<String, String> nicknameRedirects, int noise) {
		// clean noise
		for (int i=0; i<length(); i++) {
			for (int j=0; j<length(); j++) {
				if (matrix[i][j] < noise && matrix[i][j] > 0) {
					// remove really weak connections
					System.out.println(names[i] +  ", " + names[j] + ", " + matrix[i][j]);
					matrix[i][j] = 0;
				}
			}
			// make sure the diagonal is 0
			matrix[i][i] = 0;
		}
		// total nickname data and get nicknames to be removed
		Set<Integer> removed = new HashSet<>();
		for (int i=0; i<length(); i++) {
			String name = names[i];
			if (nicknameRedirects.containsKey(name)) { // we have a nickname
				for (int k=0; k<length(); k++){
					if (k != i) {
						matrix[k][i] += matrix[k][indexOfName(nicknameRedirects.get(name))];
						matrix[i][k] = matrix[k][i];
					}
				}
			}
		}
		// get people without connections
		for (int i=0; i<length(); i++) {
			boolean single = true;
			for (int j=0; j<length() && single; j++) {
				if (matrix[i][j] > 0) {
					single = false;
				}
			}
			if (single) {
				removed.add(i);
			}
		}
		// actually remove the data
		int length = length() - removed.size();
		String[] cleanNames = new String[length];
		int[][] cleanMatrix = new int[length][length];
		System.out.println();
		System.out.print("Names removed(s): ");
		int row=0;
		for(int i=0; i < length; i++){
			if (!removed.contains(i)) {
				cleanNames[row] = names[i]; // clean names
				int col = 0;
				for (int j=0; j<length; j++) { // clean matrix
					if (!removed.contains(j)) {
						cleanMatrix[row][col] = matrix[i][j];
						col++;
					}
				}
				row++;
			} else {
				System.out.print(names[i]+" ");
			}
		}
		names = cleanNames;
		matrix = cleanMatrix;
	}

	private int indexOfName(String name) {
		for (int i = 0; i < names.length; i++) {
			if (names[i].equals(name)) {
				return i;
			}
		}
		return -1;
	}
}
