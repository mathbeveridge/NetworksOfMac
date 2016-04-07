package edu.macalester.mscs.network;

public class Encounter {
	
	public final String character1;
	public final String character2;
	public final int position;
	public final String context;

	public Encounter(String character1, String character2, int position, String context) {
		this.character1 = character1;
		this.character2 = character2;
		this.position = position;
		this.context = context;
	}

	public String toString() {
		return this.character1 + ", " + this.character2 + ", " + this.position + ", \"" + this.context + "\"";
	}

}
