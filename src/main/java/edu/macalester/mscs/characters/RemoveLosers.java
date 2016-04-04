package edu.macalester.mscs.characters;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * This removes characters from the raw log file that don't matter. 
 * I think that I was trying to avoid having to manually check links that I new
 * that I was going to dump anyway.
 *
 * I had already created a preliminary network, and these guys didn't cut it.
 */
public class RemoveLosers {

	public static final String[] losers =  {
		"Benjen", "Lyanna", "Osha", "Rodrik", "Mordane", "Connington", "Doreah", 
		"Mirri", "Qotho", "Xaro", "Pyat", "Quaithe", "Sweetrobin", "Yohn", "Hallyne", 
		"Selyse", "Matthos", "Asha", "Euron", "Victarion", "Aeron", "Dagmer", "Arianne", 
		"Quentyn", "Trystane", "Obara", "Nymeria", "Tyene", "Sarella", "Obella", "Dorea", 
		"Loreza", "Areo", "Jeor", "Yoren", "Rast", "Pypar", "Lommy", "Arys", "Sparrow", "Syrio ",
		"Jaqen", "Duncan", "Hizdahr", "Yezzan", "Tycho", "Waif", "Unella"
	};
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String line = null;
		List<String> loserList = Arrays.asList(losers);
		BufferedReader fileReader = null;
		BufferedWriter writer = null;
		try{
			fileReader = new BufferedReader(new FileReader("src/main/resources/data/logs/GoT3-NewShortlistQuote-Log-15-4.csv"));
			writer = new BufferedWriter(new FileWriter("src/main/resources/data/logs/GOT3-NoLosers-15-4_v2.csv"));

			boolean done = false;

			while (!done && (line = fileReader.readLine()) != null) {
				if (line.startsWith("Removed Singleton")) {
					done = true;
				}
				String[] tokens = line.split(",");
				if (!done && tokens.length > 1
						&& (loserList.contains(tokens[0].trim()) || loserList.contains(tokens[1].trim()))) {
					System.out.println("Contained loser:" + tokens[0] + " or " + tokens[1]);
				} else {
					writer.write(line);
					writer.write('\n');
				}

			}
		} catch (Exception e) {
			throw new RuntimeException("Error at \'" + line + "\'");
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException ignored) {}
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ignored) {}
			}
		}
	}

}
