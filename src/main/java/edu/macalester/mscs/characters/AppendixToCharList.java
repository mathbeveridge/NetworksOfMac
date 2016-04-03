package edu.macalester.mscs.characters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Input: the appendix
 * Output: a list of characters
 */
public class AppendixToCharList {

	public static void main(String[] args) {
		String line = null;
		ArrayList<String> nameList = new ArrayList<>();
		String[] words;
		int count = 0;
		
		try {
			BufferedReader fileReader = new BufferedReader(
			new FileReader("/mac/research/gameofthrones/newdata/storm-of-swords-appendix.tex"));

			BufferedWriter writer = new BufferedWriter(new FileWriter("/mac/research/gameofthrones/newdata/storm-of-swords.txt"));



			while ((line = fileReader.readLine()) != null) {
				if (line.startsWith("%%%%%")) {
					writer.write(line);
					writer.write('\n');
				} else if (line.startsWith("%")) {
					// do nothing
				} else {
					line = line.trim();
					if (line.length() > 0) {
						line =line.substring(0, line.indexOf(';'));
						writer.write(line.trim());
						writer.write('\n');

						words = line.split(",");

						for (int i=1; i < words.length; i++) {
							if (nameList.contains(words[i].trim())) {
								System.out.println("Conflict on key: " + words[i] );
							} else {
								nameList.add(words[i].trim());
								count++;
							}
						}
					}
				}
				//System.out.println(line);
			}
			
			System.out.println("Number of characters:" + count);
			
			writer.close();
			fileReader.close();
			
			fileReader = new BufferedReader(
					new FileReader("/mac/research/gameofthrones/newdata/short-list.tex"));
			
			while ((line = fileReader.readLine()) != null) {
				if (line.startsWith("%")) {
					// do nothing
				} else {
					line = line.trim();
					
					if (line.length() > 0) {
						words = line.split(",");
						
						if (! nameList.contains(words[1].trim())) {
							System.out.println(line + ";ADDED");
						}
					}
				}
			}
			
			fileReader.close();
			
		} catch (Exception e) {
			System.out.println(line);
			e.printStackTrace();
			
		}

	}


}
