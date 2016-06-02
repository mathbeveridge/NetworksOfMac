package edu.macalester.mscs.characters;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import edu.macalester.mscs.network.GameOfThronesConstructor;
import edu.macalester.mscs.utils.FileUtils;
import org.apache.commons.collections15.ArrayStack;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.*;

/**
 * This is an omnibus data cleaning file that contains methods that I used to convert the scrapped information
 * from a Wiki of Ice and Fire into actionable character lists.
 *
 * Much of this is one-time use code.
 *
 * Created by abeverid on 5/22/16.
 */
public class WikiUpdater {


//    public static final String AWOIAF_DIR = "//mac/gameofthrones/awoiaflist-bak";
    public static final String AWOIAF_DIR = "//mac/gameofthrones/awoiaflist2-raw";

//    public static final String NEW_AWOIAF_DIR = "//mac/gameofthrones/awoiaflist";
    public static final String NEW_AWOIAF_DIR = "//mac/gameofthrones/awoiaflist2";

    public static final String OLD_PREFIX1 = "http://awoiaf.westeros.org/index.php";

    public static final String OLD_PREFIX2 = "/index.php";

    public static final String NEW_PREFIX = "file:/"+ NEW_AWOIAF_DIR;


    public static final String FINAL_AWOIAF_DIR = "src/main/resources/data/characters/awoiaf/";

    public static final String MYNOTOAR_DIR = "src/main/resources/data/characters/mynotoar/";

    //public static final String CLEANED_FILE_NAME = FINAL_AWOIAF_DIR + "ASOIAF-character-refined-clean.csv";
    public static final String CLEANED_FILE_NAME = FINAL_AWOIAF_DIR + "Zlist-05-24.csv";

    //public static final String TRIMMED_FILE_NAME = FINAL_AWOIAF_DIR + "ASOIAF-character-refined-trim.csv";

    public static final String TRIMMED_FILE_NAME = FINAL_AWOIAF_DIR + "AWOIAF-characters.csv";

    public static final String ALL_CHAR_FILE_NAME = FINAL_AWOIAF_DIR + "all-characters.csv";



    public static final String[] BOOK_NAMES = { "A Game of Thrones", "A Clash of Kings", "A Storm of Swords",
        "A Feast for Crows", "A Dance with Dragons",
        "The World of Ice and Fire", "The Princess and the Queen", "The Hedge Knight",
        "The Sworn Sword", "The Mystery Knight"
    };

    public static void main(String[] args) {


        //renameFiles();

        //updateFiles();

        //createCharacterList();

        //trimWhitespace();

        //createBookCharacterLists();
        //createAllCharacterList();

        compareLists();

        //createNodeCsv();

        //checkMynotoarIds();
    }

    /**
     * Updates the files to point to the local copy of the character HTML files
     */
    private static void updateFiles() {
        try {
            File dir = new File(AWOIAF_DIR);
            File[] files = dir.listFiles();

            for (int i=0; i < files.length; i++) {
                System.out.println("Updating " + files[i].getName());

                List<String> data = FileUtils.readFile(files[i].getAbsolutePath());

                for (int j = 0; j < data.size(); j++) {
                    String line = data.get(j);

                    line = StringUtils.replace(line, OLD_PREFIX1, NEW_PREFIX);
                    line = StringUtils.replace(line, OLD_PREFIX2, NEW_PREFIX);


                    //line = line.replaceAll("mac/gameofthrones/awoiaflist/([a-zA-Z_]+)", "/$0.html");

                    // I think this works. Fixes % and - and '
                    line = line.replaceAll("awoiaflist2/([a-zA-Z_()%-']+)", "$0.html");

                    data.set(j, line);
                }

                FileUtils.writeFile(data, NEW_AWOIAF_DIR + "/" + files[i].getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Renames files to use underscores instead of spaces
     */
    private static void renameFiles() {
        try {
            File dir = new File(NEW_AWOIAF_DIR);
            File[] files = dir.listFiles();

            for (int i=0; i < files.length; i++) {
                String fileName = files[i].getName();
                fileName = fileName.replace(' ', '_') + ".html";
                files[i].renameTo(new File(dir, fileName));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method updates the csv list of characters by adding missing spaces between words and
     * then trimming the entries
     */
    private static void trimWhitespace() {
        try {
            CSVReader reader =
                    new CSVReader(new FileReader(CLEANED_FILE_NAME));

            List<String[]> lines = reader.readAll();

            for (int i=0; i < lines.size(); i++) {
                String[] line = lines.get(i);

                for (int j=0; j < line.length; j++) {
                    String entry = line[j];

                    entry = entry.replace("(appears)", "(appears) ");
                    entry = entry.replace("(mentioned)", "(mentioned) ");
                    entry = entry.replace("(Appears)", "(appears) ");
                    entry = entry.replace("(Mentioned)", "(mentioned) ");
                    entry = entry.replace("(appendix)", "(appendix) ");
                    entry = entry.replace("(Appendix)", "(Appendix) ");
                    entry = entry.replace("(POV)", "(POV) ");
                    entry = entry.replace("Books", "Book(s)");
                    entry = entry.replace("&nbsp;", " ");

                    for (int k=1; k < 8 ; k++) {
                        entry = entry.replace("[" + k + "]", ";");
                    }

                    entry = entry.trim();

                    String[] splitEntry = entry.split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])");
                    StringBuffer sb = new StringBuffer();

                    for (int k=0; k < splitEntry.length; k++) {
                        sb.append(splitEntry[k]);
                        if (k < splitEntry.length -1) {
                            sb.append(';');
                        }
                    }

                    //System.out.print(sb.toString());
                    //System.out.print(",\t");

                    line[j] = sb.toString().trim();
                }

                //System.out.println("");
                lines.set(i,line);
                System.out.println(ArrayUtils.toString(lines.get(i)));
            }

            CSVWriter writer = new CSVWriter(new FileWriter(TRIMMED_FILE_NAME));

            writer.writeAll(lines);

            reader.close();
            writer.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void createBookCharacterLists() {

        try {
            CSVReader reader = new CSVReader(new FileReader(TRIMMED_FILE_NAME));

            List<String[]> lines = reader.readAll();
            reader.close();

            for (int i = 0; i < BOOK_NAMES.length; i++) {

                System.out.println("Creating character list for " + BOOK_NAMES[i]);

                CSVWriter writer =
                        new CSVWriter(new FileWriter(FINAL_AWOIAF_DIR + BOOK_NAMES[i].replace(' ','-') + "-characters.csv"));
                String charId = null;
                String charName = null;
                String title = null;
                String fullName = null;
                String alias = null;
                String allegiance = null;
                String royalHouse = null;
                String culture = null;
                String refType = null;


                // header
                writer.writeNext(new String[] {"Id", "Name", "Full Name", "Title", "Alias",
                        "Allegiance", "Royal House", "Culture", "Reference"});

                // skip header line
                for (int j=1; j < lines.size(); j++) {
                    String[] data = lines.get(j);


                    if (! data[0].equals(charName)) {
                        // encountered a new character, so write the current info if
                        // the character is in the current book
                        if (charName != null && refType != null) {
                            writer.writeNext(new String[]
                                    {charId, charName, fullName, title, alias, allegiance, royalHouse, culture, refType});
                        }

                        // reset character data
                        charName = data[0];
                        charId = charName.replace(' ','-');
                        title = "";
                        fullName = "";
                        alias = "";
                        allegiance = "";
                        royalHouse = "";
                        culture = "";
                        refType = null;

                        System.out.println("char="+ charName);
                    }

                    if (data[1].equals("Book(s)")) {
                        refType = getBookReference(data[2], BOOK_NAMES[i]);
                    } else if (data[1].equals("Title")) {
                        if (title.length() > 0) {
                            throw (new Exception(charName + " already has title [" +
                                    title + "] when processing " + data[2]));
                        }
                        title = data[2];
                    } else if (data[1].equals("Full Name")) {
                        if (fullName.length() > 0) {
                            throw (new Exception(charName + " already has fullName [" + fullName + "] when processing " + data[2]));
                        }
                        fullName = data[2];
                    } else if (data[1].equals("Alias") || data[1].equals("Other Titles")) {
                        if (alias.length() == 0) {
                            alias = charName + ';' + data[2].replace(",", ";");
                        } else {
                            alias = alias + ";" + data[2].replace(",",";");
                        }
                   } else if (data[1].equals("Allegiance")) {
                        if (allegiance.length() > 0) {
                            throw (new Exception(charName + " already has allegiance [" + allegiance + "] when processing " + data[2]));
                        }
                        allegiance = data[2];
                    } else if (data[1].equals("Royal House")) {
                        if (royalHouse.length() > 0) {
                            throw (new Exception(charName + " already has royalHouse [" + royalHouse + "] when processing " + data[2]));
                        }
                        royalHouse = data[2];
                        allegiance =  (allegiance.length() > 0) ? allegiance = allegiance + ";" + data[2]: data[2];
                    } else if (data[1].equals("Culture")) {
                        if (culture.length() > 0) {
                            throw (new Exception(charName + " already has culture [" + culture + "] when processing " + data[2]));
                        }
                        culture = data[2];
                    }
                }

                writer.close();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    private static void createAllCharacterList() {

        try {
            CSVReader reader = new CSVReader(new FileReader(TRIMMED_FILE_NAME));

            List<String[]> lines = reader.readAll();
            reader.close();

               CSVWriter writer =
                        new CSVWriter(new FileWriter(ALL_CHAR_FILE_NAME));
                String charId = null;
                String charName = null;
                String title = null;
                String otherTitle = null;
                String fullName = null;
                String alias = null;
                String allegiance = null;
                String royalHouse = null;
                String culture = null;
                String books = null;


                // header
                writer.writeNext(new String[] {"Id", "Name", "Full Name", "Title", "Alias",
                        "Allegiance", "Royal House", "Culture", "Book(s)"});

                // skip header line
                for (int j=1; j < lines.size(); j++) {
                    String[] data = lines.get(j);


                    if (! data[0].equals(charName)) {
                        // encountered a new character, so write the current info if
                        // the character is in the current book
                        if (charName != null) {
                            writer.writeNext(new String[]
                                    {charId, charName, fullName, title, alias, allegiance, royalHouse, culture, books});
                        }

                        // reset character data
                        charName = data[0];
                        charId = charName.replace(' ','-');
                        title = "";
                        fullName = "";
                        alias = "";
                        allegiance = "";
                        royalHouse = "";
                        culture = "";
                        books = null;

                        System.out.println("char="+ charName);
                    }

                    if (data[1].equals("Book(s)")) {
                        books = data[2];
                    } else if (data[1].equals("Title")) {
                        if (title.length() > 0) {
                            throw (new Exception(charName + " already has title [" +
                                    title + "] when processing " + data[2]));
                        }
                        title = data[2];
                    } else if (data[1].equals("Full Name")) {
                        if (fullName.length() > 0) {
                            throw (new Exception(charName + " already has fullName [" + fullName + "] when processing " + data[2]));
                        }
                        fullName = data[2];
                    } else if (data[1].equals("Alias") || data[1].equals("Other Titles")) {
                        if (alias.length() == 0) {
                            alias = charName + ';' + data[2].replace(",", ";");
                        } else {
                            alias = alias + ";" + data[2].replace(",",";");
                        }
                    } else if (data[1].equals("Allegiance")) {
                        if (allegiance.length() > 0) {
                            throw (new Exception(charName + " already has allegiance [" + allegiance + "] when processing " + data[2]));
                        }
                        allegiance = data[2];
                    } else if (data[1].equals("Royal House")) {
                        if (royalHouse.length() > 0) {
                            throw (new Exception(charName + " already has royalHouse [" + royalHouse + "] when processing " + data[2]));
                        }
                        royalHouse = data[2];
                        allegiance =  (allegiance.length() > 0) ? allegiance = allegiance + ";" + data[2]: data[2];
                    } else if (data[1].equals("Culture")) {
                        if (culture.length() > 0) {
                            throw (new Exception(charName + " already has culture [" + culture + "] when processing " + data[2]));
                        }
                        culture = data[2];
                    }
                }

                writer.close();



        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    /**
     *
     * @param bookString The string of book references
     * @param bookName The book we are interested in
     * @return null if the character does not appear (or is in the appendix only), otherwise returns the reference type
     */
    private static String getBookReference(String bookString, String bookName) {
        String[] tokens = bookString.split("[)]");
        String retVal = null;

        for (String token : tokens) {
            if (token.contains(bookName)) {
                if (!token.contains("appendix")) {
                    // sometimes the type of reference is missing
                    if (token.contains("(")) {
                        retVal = token.split("[(]")[1];
                    } else {
                        retVal = token.trim();
                    }
                }
            }
        }

        return retVal;
    }

    /**
     * Comparing merged list and wiki list for A Game of Thrones
     */
    private static void compareLists() {
        try {
//            CSVReader wikiListReader = new CSVReader(new FileReader("/mac/gameofthrones/awoiaf/A-Game-of-Thrones-characters.csv"));
//            CSVReader mergedListReader = new CSVReader(new FileReader("src/main/resources/data/characters/got-list-merged.csv"));

//            CSVReader wikiListReader = new CSVReader(new FileReader("/mac/gameofthrones/awoiaf/A-Clash-of-Kings-characters.csv"));
//            CSVReader mergedListReader = new CSVReader(new FileReader("src/main/resources/data/characters/cok-list-merged2.csv"));


//            CSVReader wikiListReader = new CSVReader(new FileReader("/mac/gameofthrones/awoiaf/A-Storm-of-Swords-characters.csv"));
//            CSVReader mergedListReader = new CSVReader(new FileReader("src/main/resources/data/characters/sos-list-merged-clean.csv"));
//
//
//            List<String[]> wikiList =  wikiListReader.readAll();
//            List<String[]> mergedList =  mergedListReader.readAll();


            List<String> wikiList = FileUtils.readFile("src/main/resources/data/characters/awoiaf/A-Storm-of-Swords-characters.csv");
            List<String> mergedList = FileUtils.readFile("src/main/resources/data/characters/sos-list-mynowiki-updated.csv");



            //List<String> wikiList = FileUtils.readFile("src/main/resources/data/characters/awoiaf/A-Feast-for-Crows-characters.csv");
            //List<String> mergedList = FileUtils.readFile("src/main/resources/data/characters/ffc-list-myno5.csv");

//            wikiListReader.close();
//            mergedListReader.close();


            List<String> wikiCharList = new ArrayList<String>();
            List<String> mergedCharList = new ArrayList<String>();

            for (String wikiLine : wikiList) {
                wikiCharList.add(wikiLine.split(",")[0]);
            }

            for (String mergedLine : mergedList) {
                mergedCharList.add(mergedLine.split(",")[0]);
            }

            System.out.println("========= Processing Wiki-Only Characters =========");


            List<String> wikiOnlyList = new ArrayList<String>();

            for (String wikiChar : wikiCharList) {
                if (!mergedCharList.contains(wikiChar)) {
                    System.out.println(wikiChar);
                }
            }

            for (int i=0; i < wikiCharList.size(); i++) {
                String wikiChar = wikiCharList.get(i);
                if (!mergedCharList.contains(wikiChar)) {
                    System.out.println(wikiChar);
                    wikiOnlyList.add(wikiList.get(i));
                }
            }


            System.out.println("========= Processing Merged-Only Characters =========");
            for (String mergedChar : mergedCharList) {
                if (!wikiCharList.contains(mergedChar)) {
                    System.out.println(mergedChar);
                }
            }


            //FileUtils.writeFile(wikiOnlyList, "src/main/resources/data/characters/sos-list-wiki-only2.csv");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Grabs content from lines that are <li></li>
     */
    private static void createCharacterList() {
        try {
            List<String> lines = FileUtils.readFile(NEW_AWOIAF_DIR + "/List_of_characters#.html");
            List<String> characters = new ArrayList<String>();

            characters.add("Name, URL");

            for (String line: lines) {
//                if (line.startsWith("<ul><li> <a href=") || line.startsWith("<li> <a href=")) {

                if (line.contains("<li>")) {
                    String[] tokens = line.split("\"");
                    characters.add(tokens[3] + "," + tokens[1]);
                } else {
                    System.out.println(line);
                }
            }

            FileUtils.writeFile(characters, FINAL_AWOIAF_DIR +"charlist-05-24.csv");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Grabs a subset of the character data to create the file used by D3
     */
    private static void createNodeCsv() {

        try {

            CSVReader edgeReader = new CSVReader(new FileReader("src/main/resources/data/logs/GoT1-edge17-merged.csv"));
            CSVReader charReader = new CSVReader(new FileReader(FINAL_AWOIAF_DIR + BOOK_NAMES[0].replace(' ', '-') +"-characters.csv"));

            CSVWriter nodeWriter = new CSVWriter(new FileWriter("src/main/resources/data/logs/GoT1-node17-merged.csv"));

            nodeWriter.writeNext(new String[]{"Id", "Label", "Allegiance", "Royal House", "Culture"});


            HashMap<String, String[]> fullCharMap = new HashMap<String, String[]>();


            List<String[]> edgeLines = edgeReader.readAll();
            List<String[]> charLines = charReader.readAll();

            for (int i=1; i < charLines.size(); i++) {
                String[] charLine = charLines.get(i);
                fullCharMap.put(charLine[0], charLine);
            }

            Set<String> charSet = new TreeSet<String>();

            for (int j=1; j < edgeLines.size(); j++) {
                String[] edgeLine = edgeLines.get(j);
                charSet.add(edgeLine[0]);
                charSet.add(edgeLine[1]);
            }



            List<String> charList = new ArrayList<String>(charSet);
            String[] charLine = charReader.readNext();

            for (String character :charList) {

                System.out.println("Handling node " + character);

                String[] charData = fullCharMap.get(character);

                nodeWriter.writeNext(new String[]{charData[0], charData[1], charData[5].split(";")[0],
                        charData[6].split(";")[0], charData[7].split(";")[0]});
            }

            edgeReader.close();
            charReader.close();
            nodeWriter.close();


        } catch(Exception e){
            e.printStackTrace();
        }

    }


    /**
     * Compare the ids in the a file with the AWOIAF ids.
     */
    private static void checkMynotoarIds() {
        List<String> lines = FileUtils.readFile(ALL_CHAR_FILE_NAME);

        //List<String> lines = FileUtils.readFile("src/main/resources/data/characters/sos-list-mergedwiki-clean.csv");
        //List<String> lines = FileUtils.readFile("src/main/resources/data/characters/ffc-list-merge-ffcmyno4.csv");



        List<String> idList = new ArrayList<String>();

        // the authoritative list of ids
        for (String line : lines) {
            idList.add(line.split(",")[0].trim());
        }

        System.out.println("Populated ids" + idList.size());



         //List<String> catLines = FileUtils.readFile(MYNOTOAR_DIR + "StormOfSwordsCatalogShort.csv");

        //List<String> catLines = FileUtils.readFile(MYNOTOAR_DIR + "FeastForCrowsCatalogShort.csv");

//        List<String> catLines = FileUtils.readFile("src/main/resources/data/characters/sos-list-curated-hyphenated.csv");
        //List<String> catLines = FileUtils.readFile("src/main/resources/data/characters/ffc-list-curated-hyphenated.csv");

        List<String> catLines = FileUtils.readFile("src/main/resources/data/characters/ffc-list-merge-ffcmyno4.csv");



        for (String catLine : catLines) {
            String id = catLine.split(",")[0].trim();
            if (! idList.contains(id)) {
                //System.out.println("id not found: [" + id +  "]");
                System.out.println(id);
            } else {
                //System.out.println("\t found:" + id);
            }
        }

    }
}
