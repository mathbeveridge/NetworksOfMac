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
		try {
			Reader fileReader = new BufferedReader(new FileReader("/mac/research/gameofthrones/finaldata/GoT3-15-4-matrix.csv"));

			CSVReader csvReader = new CSVReader(fileReader, ',');


			
			BufferedWriter edgeWriter = new BufferedWriter(new FileWriter("/mac/research/gameofthrones/finaldata/GoT3-15-4-edges.csv"));			
			
			
			String[] headers = csvReader.readNext();
			String[] temp;
			int i;
			String id;

			
			edgeWriter.write("Source,Target,Weight,Type\n");
			

			
			ArrayList<String> processedList = new ArrayList<String>();
			
			while ((temp = csvReader.readNext()) != null) {

				
				for(i=1; i < temp.length; i++) {
					if (!temp[i].equals("0")) {
						
						//don't double count for undirected
						if (! processedList.contains(headers[i] + '+' + temp[0] ))
						{
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
				
				
		

			edgeWriter.close();
			csvReader.close();
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
