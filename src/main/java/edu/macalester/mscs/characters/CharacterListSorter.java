package edu.macalester.mscs.characters;

import com.opencsv.CSVReader;
import edu.macalester.mscs.utils.FileUtils;
import edu.macalester.mscs.utils.WordUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author abeverid
 */
public class CharacterListSorter {


    public static void main(String args[]) {

        try {
            CSVReader reader = new CSVReader(new FileReader("src/main/resources/data/characters/ffc-list-clean-redundant.txt"));
            List<String[]> lines = reader.readAll();
            List<String> newlines = new ArrayList<>();
            for (String[] line : lines) {
                List<String> list = new ArrayList<>(Arrays.asList(line));
                Collections.sort(list.subList(1, list.size()), WordUtils.DESCENDING_LENGTH_COMPARATOR);
                newlines.add(asString(list));
            }
            FileUtils.writeFile(newlines, "src/main/resources/data/characters/ffc-list-clean-redundant-ordered.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String asString(List<String> list) {
        StringBuilder sb = new StringBuilder(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            sb.append(',').append(list.get(i));
        }
        return sb.toString();
    }
}
