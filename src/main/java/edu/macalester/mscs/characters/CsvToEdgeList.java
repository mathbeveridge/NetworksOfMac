package edu.macalester.mscs.characters;

import com.opencsv.CSVReader;

import java.io.*;
import java.util.ArrayList;

/**
 * Takes in an adjacency matrix (CSV) and outputs an edge list that is ready to
 * import into Gephi (CSV, 4 columns).
 */
public class CsvToEdgeList {
	
	public static void main(String[] args) {
		CSVReader csvReader = null;
		BufferedWriter edgeWriter = null;
		try {
			csvReader = new CSVReader(new BufferedReader(new FileReader("src/main/resources/data/logs/GoT3-15-4-matrix.csv")), ',');
			edgeWriter = new BufferedWriter(new FileWriter("src/main/resources/data/logs/GoT3-15-4-edges2.csv"));

			String[] headers = csvReader.readNext();

			edgeWriter.write("Source,Target,Weight,Type\n");

			ArrayList<String> processedList = new ArrayList<>();
			String[] temp;
			while ((temp = csvReader.readNext()) != null) {
				for (int i=1; i < temp.length; i++) {
					if (!temp[i].equals("0")) {
						
						//don't double count for undirected
						if (!processedList.contains(headers[i] + '+' + temp[0])) {
							edgeWriter.write('\"');
							edgeWriter.write(temp[0]);
							edgeWriter.write('\"');
							edgeWriter.write(',');
							edgeWriter.write('\"');
							edgeWriter.write(headers[i]);
							edgeWriter.write('\"');

							edgeWriter.write(',');
							edgeWriter.write(temp[i]);						
							edgeWriter.write(".0,undirected");
							edgeWriter.write('\n');
							
						
							processedList.add(temp[0] + '+' + headers[i]);
						
						}
					}
				}
			}
		} catch (Exception ignored) {
		} finally {
			if (csvReader != null) {
				try {
					csvReader.close();
				} catch (IOException ignored) {}
			}
			if (edgeWriter != null) {
				try {
					edgeWriter.close();
				} catch (IOException ignored) {}
			}
		}
	}
}
