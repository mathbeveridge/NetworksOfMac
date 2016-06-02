package edu.macalester.mscs.unused;

import com.opencsv.CSVReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Takes in an adjacency matrix (CSV) and outputs an edge list that is ready to
 * import into Gephi (CSV, 4 columns).
 */
public class CsvToEdgeList {
	
	public static void main(String[] args) {
		CSVReader csvReader = null;
		BufferedWriter edgeWriter = null;
		try {
//			csvReader = new CSVReader(new BufferedReader(new FileReader("src/main/resources/data/logs/GoT1-mat6-full-names.csv")), ',');
//			edgeWriter = new BufferedWriter(new FileWriter("src/main/resources/data/logs/GoT1-edge6-full-names.csv"));
			csvReader = new CSVReader(new BufferedReader(new FileReader("src/main/resources/data/logs/GoT1-mat7-dup-names.csv")), ',');
			edgeWriter = new BufferedWriter(new FileWriter("src/main/resources/data/logs/GoT1-edge7-dup-names.csv"));

			String[] headers = csvReader.readNext();

			edgeWriter.write("Source,Target,Weight,Type\n");

			List<String> processedList = new ArrayList<>();
			String[] temp;
			int row = 0;
			while ((temp = csvReader.readNext()) != null) {
				for (int i=0; i < temp.length; i++) {
					if (!temp[i].equals("0")) {
						
						//don't double count for undirected
						if (!processedList.contains(headers[i] + '+' + headers[row])) {
							edgeWriter.write('\"');
							edgeWriter.write(headers[row]);
							edgeWriter.write('\"');
							edgeWriter.write(',');
							edgeWriter.write('\"');
							edgeWriter.write(headers[i]);
							edgeWriter.write('\"');

							edgeWriter.write(',');
							edgeWriter.write(temp[i]);						
							edgeWriter.write(".0,undirected");
							edgeWriter.write('\n');
							
						
							processedList.add(headers[row] + '+' + headers[i]);
						
						}
					}
				}
				row++;
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
