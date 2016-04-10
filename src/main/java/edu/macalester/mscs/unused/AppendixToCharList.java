package edu.macalester.mscs.unused;

import java.io.*;
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

		BufferedReader fileReader = null;
		BufferedWriter writer = null;
		try {
			fileReader = new BufferedReader(new FileReader("src/main/resources/data/characters/storm-of-swords-appendix.txt"));
			writer = new BufferedWriter(new FileWriter("src/main/resources/data/characters/storm-of-swords2.txt"));
			while ((line = fileReader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("%%%%%")) {
					writer.write(line);
					writer.write('\n');
				} else if (!line.isEmpty() && !line.startsWith("%")) {
					line = line.substring(0, line.indexOf(';'));
					writer.write(line.trim());
					writer.write('\n');

					words = line.split(",");

					for (int i = 1; i < words.length; i++) {
						if (nameList.contains(words[i].trim())) {
							System.out.println("Conflict on key: " + words[i]);
						} else {
							nameList.add(words[i].trim());
							count++;
						}
					}
				}
				//System.out.println(line);
			}
			
			System.out.println("Number of characters:" + count);
			
			fileReader.close();
			
			fileReader = new BufferedReader(new FileReader("src/main/resources/data/characters/short-list.txt"));
			
			while ((line = fileReader.readLine()) != null) {
				if (!line.startsWith("%")) {
					line = line.trim();

					if (!line.isEmpty()) {
						words = line.split(",");

						if (!nameList.contains(words[1].trim())) {
							System.out.println(line + ";ADDED");
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(line);
			e.printStackTrace();
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
