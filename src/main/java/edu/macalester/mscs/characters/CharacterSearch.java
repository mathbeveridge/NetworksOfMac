package edu.macalester.mscs.characters;

import edu.macalester.mscs.utils.FileUtils;

import java.lang.reflect.Array;
import java.io.*;
import java.util.*;

/**
 * The mynotoar list only has first occurrence of a character. So we don't know whether a character in AGOT actually
 * appears in ACOK. The point of this code is to go through the previous characters lists (including appendices) and then
 * suggest which characters to include in the next book. This is not foolproof-- with both false positives and
 * false negatives. But it cuts out a lot of manual work.
 *
 * Created by abeverid on 5/27/16.
 */
public class CharacterSearch {

    public static final int SHORT_CAT_BOOK_INDEX = 0;
    public static final int SHORT_CAT_ID_INDEX = 1;
    public static final int SHORT_CAT_NAME_INDEX = 2;
    public static final int SHORT_CAT_TITLE_INDEX = 3;
    public static final int SHORT_CAT_FORENAME_INDEX = 4;
    public static final int SHORT_CAT_SURNAME_INDEX = 5;
    public static final int SHORT_CAT_FORMER_SURNAME_INDEX = 6;
    public static final int SHORT_CAT_ALIAS_INDEX = 7;


    /**
     * These were left over wiki names after reconciling the mynotoar names.
     */
    public static final String COK_WIKI_ONLY_NAMES =
            "Aegon-Frey-(son-of-Stevron),Aegon-Frey-(son-of-Aenys),Aemon-Estermont,Aemon-Rivers,Aladale-Wynch,Alan,Alannys-Harlaw,Albar-Royce,Albett,Alekyne-Florent,Alerie-Hightower,Alesander-Frey,Alester-Norcross,All-for-Joffrey,Alyn-Ambrose,Alyn-Estermont,Alyn-Frey,Alyn-Haigh,Alys-Frey,Alyssa-Blackwood,Alyx-Frey,Amarei-Crakehall,Ambrode,Amerei-Frey,Andar-Royce,Andrew-Estermont,Andrey-Charlton,Androw-Frey,Annara-Farring,Anya-Waynwood,Areo-Hotah,Arianne-Martell,Arwood-Frey,Arwyn-Frey,Baelor-Blacktyde,Bannen,Barth-(brewer),Barthogan-Stark,Bellena-Hawick,Benedict-Broom,Benfrey-Frey,Berena-Hornwood,Bethany-Rosby,Betharios-of-Braavos,Blane,Bowen-Marsh,Bradamar-Frey,Brandon-Stark,Brandon-Tallhart,Bryan-Frey,Butterbumps,Caleotte,Carolei-Waynwood,Cerenna-Lannister,Cersei-Frey,Cheyk,Clydas,Colin-Florent,Colmar-Frey,Cotter-Pyke,Cuger,Cynthea-Frey,Dacey-Mormont,Dafyn-Vance,Damon-Paege,Damon-Vypren,Danwell-Frey,Dareon,Darlessa-Marbrand,Deana-Hardyng,Della-Frey,Dickon-Frey,Donnel-Haigh,Donnel-Waynwood,Eddara-Tallhart,Edric-Dayne,Edwyn-Frey,Elron,Elyana-Vypren,Emberlei-Frey,Eon-Hunter,Erena-Glover,Eroeh,Erren-Florent,Esgred,Forley-Prester,Galt,Gariss,Garrett-Flowers,Garse-Flowers,Garse-Goodbrook,Garth-Tyrell,Gawen-Glover,Gawen-Westerling,Gelmarr,Gerion-Lannister,Gormon-Tyrell,Gueren,Halmon-Paege,Hobb,Hosman-Norcross,Hoster-Frey,Igon-Vyrwel,Jacks,Janei-Lannister,Janna-Tyrell,Jaqen-H'ghar,Jarmen-Buckwell,Jeren,Jeyne-Beesbury,Jeyne-Goodbrook,Jeyne-Lydden,Jeyne-Rivers,Joanna-Lannister,Jon-Brax,Jon-Wylde,Jonos-Frey,Joy-Hill,Joyeuse-Erenford,Jurne,Jyanna-Frey,Kedge-Whiteye,Kyra-Frey,Leslyn-Haigh,Lomas-Estermont,Lomys,Lothar-Frey,Luceon-Frey,Lucias-Vypren,Lyle-Crakehall,Lyman-Darry,Lyn-Corbray,Lyonel-(knight),Lythene-Frey,Maege-Mormont,Maegelle-Frey,Malwyn-Frey,Marianne-Vance,Marissa-Frey,Mariya-Darry,Marsella-Waynwood,Marwyn-Belmore,Masha-Heddle,Mathis-Frey,Matthar,Melara-Crane,Meldred-Merlyn,Melesa-Crakehall,Melessa-Florent,Mellara-Rivers,Melwys-Rivers,Merianne-Frey,Merrell-Florent,Mina-Tyrell,Morton-Waynwood,Morya-Frey,Moryn-Tyrell,Mullin,Murch-(Winterfell),Mya-Stone,Mychel-Redfort,Mylenda-Caron,Myranda-Royce,Myrielle-Lannister,Nestor-Royce,Norne-Goodbrother,Oberyn-Martell,Olenna-Redwyne,Omer-Florent,Orell,Oro-Tendyris,Osmund-Frey,Othell-Yarwyck,Pate-of-the-Blue-Fork,Patrek-Vance,Perra-Frey,Perriane-Frey,Petyr-Frey,Polliver,Pypar,Quent,Quentyn-Martell,Raymund-Frey,Red-Rolfe,Renly-Norcross,Rhaegar-Frey,Rhaego,Rhea-Florent,Rickard-Wylde,Robert-Brax,Robert-Brax-(son-of-Flement),Robert-Frey,Robert-Frey-(son-of-Raymund),Rodrik-Harlaw,Roslin-Frey,Ryam-Florent,Rycherd-Crane,Ryella-Frey,Ryella-Royce,Ryger-Rivers,Rylene-Florent,Sallei-Paege,Sandor-Frey,Sarra-Frey,Sarya-Whent,Satin,Serra-Frey,Shirei-Frey,Simon-Toyne,Stannis-Seaworth,Steffon-Frey,Steffon-Seaworth,Sybelle-Glover,Sylwa-Paege,Symond-Frey,Timett-(father),Todder,Tom;Too,Tyana-Wylde,Tysane-Frey,Tyta-Frey,Tytos-Frey,Tywin-Frey,Vickon-Greyjoy,Vortimer-Crane,Walda-Frey-(daughter-of-Edwyn),Walda-Frey-(daughter-of-Lothar),Walda-Frey-(daughter-of-Walton),Walda-Frey-(daughter-of-Rhaegar),Walda-Rivers,Walder-Brax,Walder-Frey-(son-of-Ryman),Walder-Frey-(son-of-Emmon),Walder-Goodbrook,Walder-Haigh,Walder-Vance,Waldon-Wynch,Walton-Frey,Wendel-Frey,Whalen-Frey,Will,Willamen-Frey,Willas-Tyrell,Willem-Frey,Wynafrei-Whent,Zachery-Frey,Zhoe-Blanetree,Zia-Frey";


    public static void main(String[] args) {

        /*
         Clash of Kings
         */

//        processFiles("src/main/resources/text/clashofkings.txt",
//                "src/main/resources/data/characters/mynotoar/GameOfThronesAppendixCatalog.csv",
//                "src/main/resources/data/characters/mynotoar/COK-GameOfThronesAppendix-Keep.csv",
//                "src/main/resources/data/characters/mynotoar/COK-GameOfThronesAppendix-Remove.csv"
//                );
//
//        processFiles("src/main/resources/text/clashofkings.txt",
//                "src/main/resources/data/characters/mynotoar/GameOfThronesCatalog2.csv",
//                "src/main/resources/data/characters/mynotoar/COK-GameOfThrones-Keep.csv",
//                "src/main/resources/data/characters/mynotoar/COK-GameOfThrones-Remove.csv"
//        );

//        processList("src/main/resources/text/clashofkings.txt", COK_WIKI_ONLY_NAMES);


        /*
        Storm of Swords

        processFiles("src/main/resources/text/stormofswords.txt",
                "src/main/resources/data/characters/mynotoar/GameOfThronesCatalog.csv",
                "src/main/resources/data/characters/mynotoar/SOS-GameOfThrones-Keep.csv",
                "src/main/resources/data/characters/mynotoar/SOS-GameOfThrones-Remove.csv"
        );

        processFiles("src/main/resources/text/stormofswords.txt",
                "src/main/resources/data/characters/mynotoar/GameOfThronesAppendixCatalog.csv",
                "src/main/resources/data/characters/mynotoar/SOS-GameOfThronesAppendix-Keep.csv",
                "src/main/resources/data/characters/mynotoar/SOS-GameOfThronesAppendix-Remove.csv"
                );

        processFiles("src/main/resources/text/stormofswords.txt",
                "src/main/resources/data/characters/mynotoar/ClashOfKingsCatalog.csv",
                "src/main/resources/data/characters/mynotoar/SOS-ClashOfKings-Keep.csv",
                "src/main/resources/data/characters/mynotoar/SOS-ClashOfKings-Remove.csv"
        );

        processFiles("src/main/resources/text/stormofswords.txt",
                "src/main/resources/data/characters/mynotoar/ClashOfKingsAppendixCatalog.csv",
                "src/main/resources/data/characters/mynotoar/SOS-ClashOfKingsAppendix-Keep.csv",
                "src/main/resources/data/characters/mynotoar/SOS-ClashOfKingsAppendix-Remove.csv"
        );

*/



        processFiles("src/main/resources/text/stormofswords.txt",
                "src/main/resources/data/characters/sos-list-curated-hyphenated.csv",
                "src/main/resources/data/characters/mynotoar/GameOfThronesCatalogShort.csv",
                "src/main/resources/data/characters/mynotoar/SOS-GameOfThrones-Keep.csv",
                "src/main/resources/data/characters/mynotoar/SOS-GameOfThrones-Remove.csv"
        );



        /// createAddFile();






    }

    /**
     * Goes through the original text and then makes a recommendation for each character in the given catelog: keep or remove.
     * @param textName
     * @param catalogName
     * @param keepCatalogName
     * @param removeCatalogName
     */
    private static void processFiles(String textName, String currentListName,
                                     String catalogName, String keepCatalogName, String removeCatalogName) {
        List<String> charLines = FileUtils.readFile(catalogName);
        HashMap<String, String[]> charMap = new HashMap<String, String[]>();
        HashMap<String, String> charLineMap = new HashMap<String, String>();

        System.out.println("Processing book:" + textName + " and catalog:" + catalogName);


        for (String line : charLines) {
            String[] tokens = line.split(",");
            String key = tokens[1].replace("\"","");
            charMap.put(key, tokens);
            charLineMap.put(key,line);
        }

        List<String> textLines = FileUtils.readFile(textName);

        for (String textLine : textLines) {


            List<String> removalList = new ArrayList<String>();

            for (String key : charMap.keySet()) {
                String[] tokens = charMap.get(key);

                if (textLine.contains(tokens[SHORT_CAT_NAME_INDEX])) {
                    // name
                    System.out.println("matched: " + key + " name token:" + tokens[SHORT_CAT_NAME_INDEX]);
                    removalList.add(key);
                } else if ( tokens.length > SHORT_CAT_ALIAS_INDEX &&
                        tokens[SHORT_CAT_ALIAS_INDEX].length() > 0 && textLine.contains(tokens[SHORT_CAT_ALIAS_INDEX])) {
                    System.out.println("matched: " + key + " alias token:" + tokens[SHORT_CAT_ALIAS_INDEX]);
                    removalList.add(key);
                } else if ( tokens.length > SHORT_CAT_SURNAME_INDEX &&
                        tokens[SHORT_CAT_TITLE_INDEX].length() > 0) {
                    if (tokens[SHORT_CAT_SURNAME_INDEX].length() >0) {
                        if (textLine.contains(tokens[SHORT_CAT_TITLE_INDEX] + ' ' + tokens[SHORT_CAT_SURNAME_INDEX])) {
                            System.out.println("matched: " + key + " title/last name token:" + tokens[SHORT_CAT_TITLE_INDEX] + ' ' + tokens[SHORT_CAT_SURNAME_INDEX]);
                            removalList.add(key);
                        }
                    }

                    if (tokens[SHORT_CAT_SURNAME_INDEX].length() >0) {
                        if (textLine.contains(tokens[SHORT_CAT_FORENAME_INDEX] + ' ' + tokens[SHORT_CAT_SURNAME_INDEX])) {
                            System.out.println("matched: " + key + " first/last name token:" + tokens[SHORT_CAT_FORENAME_INDEX] + ' ' + tokens[SHORT_CAT_SURNAME_INDEX]);
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

        List<String> currentCharList = FileUtils.readFile(currentListName);
        TreeMap<String, String> currentMap = new TreeMap<String, String>();

        for (String currentLine : currentCharList) {
            String currentKey = currentLine.split(",")[0];
            currentMap.put(currentKey, "old," + currentLine);
        }

        List<String> keepLines = new ArrayList<String>();
        List<String> removeLines = new ArrayList<String>();

        for (String line : charLines) {
            String charKey = line.split(",")[SHORT_CAT_ID_INDEX];
            System.out.println("\t===charKey=[" + charKey + "]");

            if (charMap.containsKey(charKey)) {
                removeLines.add(line);
            } else {
                keepLines.add(line);

                if (! currentMap.containsKey(charKey)) {
                    System.out.println("Adding for [" + charKey + "]");
                    currentMap.put(charKey, charLineMap.get(charKey));
                }
            }
        }

        FileUtils.writeFile(keepLines,keepCatalogName);
        FileUtils.writeFile(removeLines,removeCatalogName);

        List<String> updatedList = new ArrayList<String>();

        for (String k : currentMap.keySet()) {
            updatedList.add(currentMap.get(k));
        }

        System.out.println("updatedList=" + updatedList.size());



        for (String temp : updatedList) {
            //System.out.println(temp);

            if (temp == null) {
                System.out.println("\t\t========" + temp);
            }
         }

       FileUtils.writeFile(updatedList, currentListName + "2");



    }


    /**
     * Creates a character list for the next book, marking some as "carryover" (from previous books)
     * and "keep" (first appearing in this book)
     */
    private static void createAddFile() {
        String[] previousListFiles = { "src/main/resources/data/characters/got-list-merged.csv",
                "src/main/resources/data/characters/cok-list-merged2.csv" };

        String[] keepFiles = {
                "src/main/resources/data/characters/mynotoar/SOS-GameOfThrones-Keep.csv",
                "src/main/resources/data/characters/mynotoar/SOS-GameOfThronesAppendix-Keep.csv",
                "src/main/resources/data/characters/mynotoar/SOS-ClashOfKings-Keep.csv",
                "src/main/resources/data/characters/mynotoar/SOS-ClashOfKingsAppendix-Keep.csv",
        };

        Map<String, String> map = new HashMap<String, String>();


        for (int i=0; i < previousListFiles.length; i++) {
            List<String> lineList = FileUtils.readFile(previousListFiles[0]);

            for (String line : lineList) {
                map.put(line.split(",")[0], line);
            }
        }

        List<String> newLines = new ArrayList<String>();

        for (int j=0; j < keepFiles.length; j++) {
            List<String> keepLines = FileUtils.readFile(keepFiles[j]);

            for (String keep : keepLines) {
                String key = keep.split(",")[0];
                keep = (map.containsKey(key)) ? "carryover," + map.get(key) : "keep," +  keep;

                newLines.add(keep);
            }

        }

        FileUtils.writeFile(newLines,"src/main/resources/data/characters/mynotoar/SOS-keep.csv");

    }

    /**
     * Similar to processFiles, but this one takes a comma delimited string of names as an input.
     * This is useful at the end of the character list process, when there is a disparity between the potential lists.
     * This function makes a recommendation, but is not fool proof! It searches the text for the full character name
     * and the character first name.
     * @param textName
     * @param names
     */
    private static void processList(String textName, String names) {


        List<String> textLines = FileUtils.readFile(textName);

        List<String> nameList = new ArrayList<String>();

        String[] nameArray = names.split(",");

        for (String w : nameArray) {
            nameList.add(w.split("-\\(")[0]);
        }

        for (String textLine : textLines) {

            List<String> removalList = new ArrayList<String>();

            for (String id : nameList) {


                String  name = id.replace('-',' ');
                String firstName = name.split(" ")[0];

                if (textLine.contains(id)) {
                    System.out.println("\tmatched: " + id + " token:" + id);
                    removalList.add(id);
                } else if ( textLine.contains(name)) {
                    System.out.println("\tmatched: " + id + " token:" + name);
                    removalList.add(id);
                } else if (textLine.contains(firstName)) {
                    System.out.println("\tmatched: " + id + " token:" + firstName);
                    removalList.add(id);
                }


            }

            if (removalList.size() > 0) {
                for (String k : removalList) {
                    nameList.remove(k);
                }
            }
        }


        System.out.println("===== didn't match " + nameList.size());

        for (String key : nameList) {
            System.out.println("\t suggest removing: " + key );
        }


//        List<String> keepLines = new ArrayList<String>();
//        List<String> removeLines = new ArrayList<String>();
//
//        for (String line : charLines) {
//            String charKey = line.split(",")[0];
//
//            if (charMap.containsKey(charKey)) {
//                removeLines.add(line);
//            } else {
//                keepLines.add(line);
//            }
//        }
//
//        FileUtils.writeFile(keepLines,keepCatalogName);
//        FileUtils.writeFile(removeLines,removeCatalogName);

    }


}
