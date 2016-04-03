package edu.macalester.mscs.network;

public class MatrixAndNames {
	
	private int[][] matrix;
	private String[] names;
	
	public MatrixAndNames(int[][] inputMatrix, String[] inputNames){
		this.matrix = inputMatrix;
		this.names = inputNames;
	}

	public int[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}

	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

}
