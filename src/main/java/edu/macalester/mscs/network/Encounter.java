package edu.macalester.mscs.network;

public class Encounter implements Comparable<Encounter> {

	public final String character1;
	public final String name1;
	public final String character2;
	public final String name2;
	public final int position;
	public final String context;

	public Encounter(String character1, String name1, String character2, String name2, int position, String context) {
		this.character1 = character1;
		this.name1 = name1;
		this.character2 = character2;
		this.name2 = name2;
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Encounter encounter = (Encounter) o;

		return position == encounter.position
				&& character1.equals(encounter.character1)
				&& character2.equals(encounter.character2);

	}

	@Override
	public int hashCode() {
		int result = character1.hashCode();
		result = 31 * result + character2.hashCode();
		result = 31 * result + position;
		return result;
	}

	@Override
	public String toString() {
		return this.character2 + " (" + name2 + "), " + this.character1 + " (" + name1 + "), " + this.position + ", \"" + this.context + "\"";
	}
}
