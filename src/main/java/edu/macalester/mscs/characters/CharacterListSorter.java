package edu.macalester.mscs.characters;

import com.opencsv.CSVReader;
import edu.macalester.mscs.utils.FileUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by abeverid on 5/1/16.
 */
public class CharacterListSorter {


    public static void main(String args[]) {

        try {
            CSVReader reader = new CSVReader(new FileReader("src/main/resources/data/characters/ffc-list-clean-redundant.txt"));

            List<String[]> lines = reader.readAll();
            List<String> newlines = new ArrayList<String>();

            for (String[] line : lines) {
                List<String> list = Arrays.asList(line);

                ArrayList<String> alist = new ArrayList<String>(list);

                alist.remove(0);

                Collections.sort(alist, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return -o1.length() + o2.length();
                    }
                });

                alist.add(0,line[0]);

                newlines.add(asString(alist));

            }

            FileUtils.writeFile(newlines, "src/main/resources/data/characters/ffc-list-clean-redundant-ordered.csv");

        } catch (Exception e) {
            e.printStackTrace();
        }




    }



    private static String asString(List<String> list) {
        StringBuffer buffer = new StringBuffer();

        for (int i=0; i < list.size(); i++) {
            buffer.append(list.get(i));
            if (i < list.size()-1) {
                buffer.append(',');
            }
        }

        return buffer.toString();
    }
}
