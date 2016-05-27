package edu.macalester.mscs.characters;

import edu.macalester.mscs.utils.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The mynotoar list only has first occurrence of a character. So we don't know whether a character in AGOT actually
 * appears in ACOK. The point of this code is to go through the previous characters lists (including appendix) and then
 * suggest which characters to include in the next book. This is not foolproof-- with both false positives and
 * false negatives. But it cuts out a lot of manual work.
 *
 * Created by abeverid on 5/27/16.
 */
public class CharacterSearch {

    public static void main(String[] args) {

        // Clash of Kings
        processFiles("src/main/resources/text/clashofkings.txt",
                "src/main/resources/data/characters/mynotoar/GameOfThronesAppendixCatalog.csv",
                "src/main/resources/data/characters/mynotoar/COK-GameOfThronesAppendix-Keep.csv",
                "src/main/resources/data/characters/mynotoar/COK-GameOfThronesAppendix-Remove.csv"
                );

        processFiles("src/main/resources/text/clashofkings.txt",
                "src/main/resources/data/characters/mynotoar/GameOfThronesCatalog2.csv",
                "src/main/resources/data/characters/mynotoar/COK-GameOfThrones-Keep.csv",
                "src/main/resources/data/characters/mynotoar/COK-GameOfThrones-Remove.csv"
        );

    }


    private static void processFiles(String textName, String catalogName, String keepCatalogName, String removeCatalogName) {
        List<String> charLines = FileUtils.readFile(catalogName);
        HashMap<String, String[]> charMap = new HashMap<String, String[]>();
        //List<String> keyList = new ArrayList<String>();

        for (String line : charLines) {
            String[] tokens = line.split(",");
            //keyList.add(tokens[0]);
            charMap.put(tokens[0], tokens);
        }

        List<String> textLines = FileUtils.readFile(textName);

        for (String textLine : textLines) {

            List<String> removalList = new ArrayList<String>();

            for (String key : charMap.keySet()) {
                String[] tokens = charMap.get(key);


                if (textLine.contains(tokens[1])) {
                    System.out.println("matched: " + key + " token:" + tokens[1]);
                    removalList.add(key);
                } else if ( textLine.contains(tokens[9])) {
                    System.out.println("matched: " + key + " token:" + tokens[9]);
                    removalList.add(key);
                } else if ( tokens[8].length() > 0) {
                    if (tokens[10].length() >0) {
                        if (textLine.contains(tokens[8] + ' ' + tokens[10])) {
                            System.out.println("matched: " + key + " token:" + tokens[8] + ' ' + tokens[10]);
                            removalList.add(key);
                        }
                    }

                    if (tokens[9].length() >0) {
                        if (textLine.contains(tokens[8] + ' ' + tokens[9])) {
                            System.out.println("matched: " + key + " token:" + tokens[8] + ' ' + tokens[9]);
                            removalList.add(key);
                        }
                    }
                }


            }

            if (removalList.size() > 0) {
                for (String k : removalList) {
                    charMap.remove(k);
                }
            }
        }


        System.out.println("===== didn't match " + charMap.size());

        for (String key : charMap.keySet()) {
            System.out.println("\t suggest removing:" + key );
        }


        List<String> keepLines = new ArrayList<String>();
        List<String> removeLines = new ArrayList<String>();

        for (String line : charLines) {
            String charKey = line.split(",")[0];

            if (charMap.containsKey(charKey)) {
                removeLines.add(line);
            } else {
                keepLines.add(line);
            }
        }

        FileUtils.writeFile(keepLines,keepCatalogName);
        FileUtils.writeFile(removeLines,removeCatalogName);

    }
}
