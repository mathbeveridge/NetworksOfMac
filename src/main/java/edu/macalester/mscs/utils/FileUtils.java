package edu.macalester.mscs.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class FileUtils {

    public static List<String> readFile(String file) {
        List<String> lines = new ArrayList<>();
        String line = null;
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(file));
            while ((line = fileReader.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error at \'" + line + "\'", e);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return lines;
    }

    public static void writeFile(List<String> lines, String file) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            for (String line : lines) {
                if (line == null) {
                    System.out.println("FileUtils.writeFile() is skipping null line");
                } else {
                    writer.write(line);
                    writer.write('\n');
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ignored) {}
            }
        }

    }
}
