package edu.macalester.mscs.network;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

	public List<String> cleanNoise(int noise) {
		List<String> lines = new ArrayList<>();
		lines.add("Removing noisy connections:");
		// clean noise
		for (int i=0; i<length(); i++) {
			for (int j=0; j<length(); j++) {
				if (matrix[i][j] < noise && matrix[i][j] > 0) {
					// remove really weak connections
					lines.add(names[i] +  ", " + names[j] + ", " + matrix[i][j]);
					matrix[i][j] = 0;
				}
			}
			// make sure the diagonal is 0
			matrix[i][i] = 0;
		}
		// get people without connections
		Set<Integer> removed = new HashSet<>();
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
		lines.add(removeRows(removed));
		return lines;
	}

	public List<String> cleanSingletons() {
		List<String> lines = new ArrayList<>();
		// get people with only one connection
		Set<Integer> removed = new HashSet<>();
		int interations = 0;
		do {
			removed.clear();
			for (int i = 0; i < length(); i++) {
				int neighbors = 0;
				for (int j = 0; j < length(); j++) {
					if (matrix[i][j] > 0) {
						neighbors++;
					}
				}
				if (neighbors < 2) {
					removed.add(i);
				}
			}
			lines.add(removeRows(removed));
			interations++;
		} while (!removed.isEmpty());
		return lines;
	}

	public List<String> cleanSingletons(int iterations) {
		List<String> lines = new ArrayList<>();
		// get people with only one connection
		for (int n=0; n<iterations; n++) {
			Set<Integer> removed = new HashSet<>();
			for (int i=0; i<length(); i++) {
                int neighbors = 0;
                for (int j=0; j<length(); j++) {
                    if (matrix[i][j] > 0) {
                        neighbors++;
                    }
                }
                if (neighbors < 2) {
                    removed.add(i);
                }
            }
			lines.add(removeRows(removed));
		}
		return lines;
	}

	private String removeRows(Set<Integer> removed) {
		StringBuilder sb = new StringBuilder();
		if (!removed.isEmpty()) {
			int newLength = length() - removed.size();
			String[] cleanNames = new String[newLength];
			int[][] cleanMatrix = new int[newLength][newLength];
			sb.append("Names removed(s): ");
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
                    sb.append(names[i]).append(" ");
                }
            }
			names = cleanNames;
			matrix = cleanMatrix;
		}
		return sb.toString();
	}
}
