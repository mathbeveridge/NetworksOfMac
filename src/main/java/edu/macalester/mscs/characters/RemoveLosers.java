package edu.macalester.mscs.characters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
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
		ArrayList<String> nameList = new ArrayList<String>();
		String[] words;
		int count = 0;
		
		List<String> loserList = Arrays.asList(losers);
		
		try{
			
			// int i=0;
			
			BufferedReader fileReader = new BufferedReader(
			new FileReader("/mac/research/gameofthrones/finaldata/GOT3-NewShortlistQuote-Log-15-4.csv"));				

			BufferedWriter writer = 
				new BufferedWriter(new FileWriter
						("/mac/research/gameofthrones/finaldata/GOT3-NoLosers-15-4.csv"));
			

			boolean done = false;
			String[] tokens;
			
			
			while ((line = fileReader.readLine()) != null) {	

				// i++;
				
				if (line.startsWith("Removed Singleton")) {
					done = true;
				}
				
				if (!done) {
					tokens = line.split(",");
					
					if (tokens.length > 1) {
						
						if (! loserList.contains(tokens[0].trim()) && 
								 ! loserList.contains(tokens[1].trim())) {
					
							// line does not contain a loser
							writer.write(line);
							writer.write('\n');
						} else {
							System.out.println("Contained loser:" + tokens[0] + " or " + tokens[1]);
						}

					
					} else {
						// line does not contain a loser
						writer.write(line);
						writer.write('\n');
					}
				} else {
					writer.write(line);
					writer.write('\n');
				}
				
			}
			
			
			writer.close();
			fileReader.close();
		} catch (Exception e) {
			System.out.println(line);
			e.printStackTrace();
		}
	}

}
