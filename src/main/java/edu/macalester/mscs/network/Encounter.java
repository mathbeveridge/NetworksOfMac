package edu.macalester.mscs.network;

public class Encounter implements Comparable<Encounter> {
	
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

	@Override
	public int compareTo(Encounter o) {
		int posDif = position - o.position;
		int c1dif = character1.compareTo(o.character1);
		if (posDif != 0) {
			return posDif;
		} else if (c1dif != 0) {
			return c1dif;
		} else {
			return character2.compareTo(o.character2);
		}
	}

	@Override
	public String toString() {
		return this.character1 + ", " + this.character2 + ", " + this.position + ", \"" + this.context + "\"";
	}
}
