package edu.macalester.mscs.characters;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

/**
 * Created by abeverid on 5/22/16.
 */
public class CharacterListMerger {

    public static final String curatedFileName = "src/main/resources/data/characters/got-list-long.csv";
    public static final String officialFileName = "src/main/resources/data/characters/gameOfThronesCatalog.csv";

    public static final String outputFileName = "src/main/resources/data/characters/got-list-merged.csv";

    public static void main(String[] args) {

        try {
            CSVReader curatedReader = new CSVReader(new FileReader(curatedFileName));
            CSVReader officialReader = new CSVReader(new FileReader(officialFileName));

            CSVWriter outputWriter = new CSVWriter(new FileWriter(outputFileName));


            List<String[]> curatedList =  curatedReader.readAll();
            List<String[]> officialList =  officialReader.readAll();

            String[] curatedLine = null;
            String[] officialLine = null;

            int curatedIndex = 0;
            int officialIndex = 0;

            while (curatedIndex < curatedList.size() || officialIndex < officialList.size()) {
                if (curatedIndex < curatedList.size()) {
                    curatedLine = curatedList.get(curatedIndex);
                } else {
                    curatedLine = null;
                }

                if (officialIndex < officialList.size()) {
                    officialLine = officialList.get(officialIndex);
                } else {
                    officialLine = null;
                }

                if (curatedLine != null && officialLine != null) {
                    int val = curatedLine[0].compareTo(officialLine[0]);

                    if (val == 0) {
                        System.out.println("match: " + curatedLine[0]);
                        outputWriter.writeNext(curatedLine);
                        curatedIndex++;
                        officialIndex++;
                    } else if (val < 0){
                        System.out.println("curated no match: " + curatedLine[0]);
                        outputWriter.writeNext(curatedLine);
                        curatedIndex++;
                    } else {
                        System.out.println("official no match: " + officialLine[0]);
                        outputWriter.writeNext(officialLine);
                        officialIndex++;
                    }
                } else if (curatedLine == null) {
                    System.out.println("official endgame: " + officialLine[0]);
                    outputWriter.writeNext(officialLine);
                    officialIndex++;
                } else {
                    System.out.println("curated endgame: " + curatedLine[0]);
                    outputWriter.writeNext(curatedLine);
                    curatedIndex++;
                }

            }

            curatedReader.close();
            officialReader.close();
            outputWriter.flush();
            outputWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
