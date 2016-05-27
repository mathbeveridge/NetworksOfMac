package edu.macalester.mscs.characters;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import edu.macalester.mscs.utils.FileUtils;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Merges two character files into one. Assumes that both files are sorted alphabetically.
 * When there is a match, the line from the first file is used and the second line is ignored.
 *
 * Created by abeverid on 5/22/16.
 */
public class CharacterListMerger {

    public static final String GOT_CURATED_FILE_NAME = "src/main/resources/data/characters/got-list-long.csv";
    public static final String GOT_MYNOTOAR_FILE_NAME = "src/main/resources/data/characters/mynotoar/GameOfThronesList.csv";
    public static final String GOT_OUTPUT_FILE_NAME = "src/main/resources/data/characters/got-list-merged-temp.csv";


    public static final String COK_CURATED_FILE_NAME = "src/main/resources/data/characters/cok-list-curated-hyphenated3.csv";
    public static final String COK_MYNOTOAR_FILE_NAME = "src/main/resources/data/characters/mynotoar/ClashOfKingsList.csv";
    public static final String COK_OUTPUT_FILE_NAME = "src/main/resources/data/characters/cok-list-merged-temp.csv";


    public static void main(String[] args) {

        // Game of Thrones
        //processFiles(GOT_CURATED_FILE_NAME, GOT_MYNOTOAR_FILE_NAME, GOT_OUTPUT_FILE_NAME);

        // Clash of Kings
        //processFiles(COK_CURATED_FILE_NAME, COK_MYNOTOAR_FILE_NAME, COK_OUTPUT_FILE_NAME);

        removeDoubles();

    }

    public static void processFiles(String curatedFileName, String mynotoarFileName, String outputFileName) {

        try {
            CSVReader curatedReader = new CSVReader(new FileReader(curatedFileName));
            CSVReader mynotoarReader = new CSVReader(new FileReader(mynotoarFileName));

            CSVWriter outputWriter = new CSVWriter(new FileWriter(outputFileName));


            List<String[]> curatedList = curatedReader.readAll();
            List<String[]> mynotoarList = mynotoarReader.readAll();

            String[] curatedLine = null;
            String[] mynotoarLine = null;

            int curatedIndex = 0;
            int mynotoarIndex = 0;

            while (curatedIndex < curatedList.size() || mynotoarIndex < mynotoarList.size()) {
                if (curatedIndex < curatedList.size()) {
                    curatedLine = curatedList.get(curatedIndex);
                } else {
                    curatedLine = null;
                }

                if (mynotoarIndex < mynotoarList.size()) {
                    mynotoarLine = mynotoarList.get(mynotoarIndex);
                } else {
                    mynotoarLine = null;
                }

                if (curatedLine != null && mynotoarLine != null) {
                    int val = curatedLine[0].compareTo(mynotoarLine[0]);

                    if (val == 0) {
                        System.out.println("match: " + curatedLine[0]);
                        outputWriter.writeNext(curatedLine);
                        curatedIndex++;
                        mynotoarIndex++;
                    } else if (val < 0) {
                        System.out.println("curated no match: " + curatedLine[0]);
                        outputWriter.writeNext(curatedLine);
                        curatedIndex++;
                    } else {
                        System.out.println("official no match: " + mynotoarLine[0]);
                        outputWriter.writeNext(mynotoarLine);
                        mynotoarIndex++;
                    }
                } else if (curatedLine == null) {
                    System.out.println("mynotoar endgame: " + mynotoarLine[0]);
                    outputWriter.writeNext(mynotoarLine);
                    mynotoarIndex++;
                } else {
                    System.out.println("curated endgame: " + curatedLine[0]);
                    outputWriter.writeNext(curatedLine);
                    curatedIndex++;
                }

            }

            curatedReader.close();
            mynotoarReader.close();
            outputWriter.flush();
            outputWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private static void removeDoubles() {
        List<String> lines = FileUtils.readFile("src/main/resources/data/characters/cok-list-merged-temp2.csv");
        List<String> newLines = new ArrayList<String>();

        String prevLine = null;

        for (String currentLine : lines) {
            if (prevLine == null) {
                prevLine = currentLine;
            } else {
                String prevToken = prevLine.split(",")[0];
                String currentToken = currentLine.split(",")[0];

                //System.out.println(prevToken + " and " + currentToken);

                if (prevToken.equals(currentToken)) {
                    if (prevLine.contains("A Game of Thrones") || prevLine.contains("A Clash of Kings")) {
                        System.out.println("skipping:" + prevLine);
                        newLines.add(currentLine);
                    } else {
                        System.out.println("skipping:" + currentLine);
                        newLines.add(prevLine);
                    }

                    prevLine = null;
                } else {
                    newLines.add(prevLine);
                    prevLine = currentLine;
                }

            }

        }


        if (prevLine != null) {
            newLines.add(prevLine);
        }


        FileUtils.writeFile(newLines, "src/main/resources/data/characters/cok-list-merged-temp3.csv");

    }
}