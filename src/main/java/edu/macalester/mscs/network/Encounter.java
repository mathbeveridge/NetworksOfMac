package edu.macalester.mscs.network;

public class Encounter {
	
	public final String character1;
	public final String character2;
	public final int index;
	public final String context;

	public Encounter(String character1, String character2, int index, String context) {
		this.character1 = character1;
		this.character2 = character2;
		this.index = index;
		this.context = context;
	}

	public String toString() {
		return this.character1 + ", " + this.character2 + ", " + this.index + ", \"" + this.context + "\"";
	}

}
