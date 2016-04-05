package edu.macalester.mscs.network;

public class Encounter {
	
	public final String character1;
	public final String character2;
	public final int cIndex1;
	public final int cIndex2;
	public final int proximity;
	public final String context;
	
	public Encounter(String character1, String character2, int cIndex1, int cIndex2, String context) {
		this.character1 = character1;
		this.character2 = character2;
		this.cIndex1 = cIndex1;
		this.cIndex2 = cIndex2;
		this.proximity = Math.abs(cIndex1 - cIndex2);
		this.context = context;
	}
	
	public String toString() {
		return this.character1 + ", " + this.character2 + ", " 
				+ this.cIndex1 + ", " + this.cIndex2 + ", " + this.proximity + ", " + this.context;
	}

}
