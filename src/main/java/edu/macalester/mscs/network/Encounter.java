package edu.macalester.mscs.network;

public class Encounter {
	
	public final String character1;
	public final String character2;
	public final int proximity;
	public final String context;

	public Encounter(String character1, String character2, String context) {
		this(character1, character2, -1, context);
	}

	public Encounter(String character1, String character2, int proximity, String context) {
		this.character1 = character1;
		this.character2 = character2;
		this.proximity = proximity;
		this.context = context;
	}

	public String toString() {
		if (proximity > -1) {
			return this.character1 + ", " + this.character2 + ", " + this.proximity + ", \"" + this.context + "\"";
		} else {
			return this.character1 + ", " + this.character2 + ", \"" + this.context + "\"";
		}
	}

}
