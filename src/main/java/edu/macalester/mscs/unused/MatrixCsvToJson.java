package edu.macalester.mscs.unused;

import edu.macalester.mscs.utils.FileUtils;
import edu.macalester.mscs.utils.Logger;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Takes in the Matrix CSV file and creates a Matrix JSON file with columns ordered as desired.
 * This version works for GOT1.
 *
 * Created by abeverid on 4/24/16.
 */
public class MatrixCsvToJson {


    public static final String[] GOT1_ORDERED_CHAR = {
            "AemonT", "AlliserT", "BenjenS", "BowenM", "Chett", "Dareon", "DonalN", "Grenn", "Halder", "Hobb",
            "JaremyR", "JeorM", "JonS", "Pyp", "RandyllT", "SamwellT", "Yoren",

            "AerysT", "Alyn", "BarristanS", "BrandonS", "Cayn", "CerseiL", "Desmond", "EddardS", "Harwin",
            "Hullen", "JanosS", "JonA", "JoryC", "Lyanna", "Malleon", "PetyrB", "Pycelle", "RaymunD", "RenlyB",
            "RhaegarT", "RobertB", "StannisB", "TobhoM", "Tom", "TorrhenK", "Varys", "VayonP",

            "Aggo", "Cohollo", "DaenerysT", "Doreah", "Drogo", "Haggo", "IllyrioM", "Irri", "Jhiqui", "Jhogo",
            "JorahM", "MirriM", "Ogo", "Qotho", "Quaro", "Rakharo", "Rhaego", "ViserysT",

            "AddamM", "Bronn", "Chiggen", "Colemon", "Conn", "JaimeL", "Jyck", "KevanL", "Lefford", "LysaA",
            "Marillion", "Mord", "Morrec", "RobertA", "Shae", "Shagga", "Timett", "TyrionL", "TywinL", "VardisE",
            "WillisW",

            "BranS", "BryndenT", "CatelynS", "DonnelW", "EdmureT", "GalbartG", "GreatjonU", "HalM", "HallisM",
            "Hodor", "HosterT", "KarylV", "Luwin", "MaegeM", "MarqP", "MashaH",
            "Nan", "Osha", "RickardK", "RickonS", "RobbS", "RodrikC", "RooseB", "StevronF", "TheonG", "WalderF",

            "AryaS", "BericD", "BorosB", "GregorC", "IlynP", "JeyneP", "JoffreyB", "LorasT", "Maegor", "MerynT",
            "Mycah", "Myrcella", "SandorC", "SansaS", "SeptaM", "SyrioF", "Tommen"
    };


    private String[] orderedCharacters;
    private String csvFileName;
    private String jsonFileName;

    /**
     *
     * @param orderedCharacters
     * @param csvFileName
     * @param jsonFileName
     */
    public MatrixCsvToJson(String[] orderedCharacters, String csvFileName, String jsonFileName) {
        this.orderedCharacters = orderedCharacters;
        this.csvFileName = csvFileName;
        this.jsonFileName = jsonFileName;
    }

    public MatrixCsvToJson(String orderedCharFileName, String csvFileName, String jsonFileName) {

        this.csvFileName = csvFileName;
        this.jsonFileName = jsonFileName;

        List<String> orderedCharList = FileUtils.readFile(orderedCharFileName);

        this.orderedCharacters = orderedCharList.toArray(new String[0]);
    }





    public static void main(String args[]) {
        MatrixCsvToJson toJson = new MatrixCsvToJson("src/main/resources/data/characters/got-community50.txt",
                "src/main/resources/data/logs/GoT1-mat11-rebecca.csv",
                "src/main/resources/data/logs/GoT1-mat11-rebecca-out50.json");


        toJson.createJson();
    }



    public void createJson() {

        try {
            List<String> lines = FileUtils.readFile(csvFileName);
            String rawCharList = lines.get(0);
            int[][] matrix = new int[lines.size()-1][lines.size()-1];


//            System.out.println(rawCharList);

            if (orderedCharacters.length > matrix.length) {
                throw new RuntimeException("Character list length " + orderedCharacters.length +
                " matrix length " + matrix.length + "mismatch");
            }

            // read in the matrix, which has a header line
            for (int i = 1; i < lines.size(); i++) {
                StringTokenizer tokenizer = new StringTokenizer(lines.get(i),",");
                int j = 0;

                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    int weight = Integer.parseInt(token);
                    matrix[i-1][j] = weight;
                    matrix[j][i-1] = weight;
                    j++;
                }
            }


            // create a new matrix using the specified character order
            String[] characters = rawCharList.split(",");

            int[][] newMatrix = new int[orderedCharacters.length][orderedCharacters.length];
            String name1, name2;
            int name1index, name2index;

            for (int i=0; i < newMatrix.length; i++) {
                name1 = orderedCharacters[i];
                name1index = ArrayUtils.indexOf(characters,name1);
                for (int j=i+1; j < newMatrix.length; j++) {
                    name2 = orderedCharacters[j];
                    name2index = ArrayUtils.indexOf(characters,name2);

                    System.out.println("name1=" + name1 + " and name2=" + name2);

                    newMatrix[i][j] = matrix[name1index][name2index];
                    newMatrix[j][i] = matrix[name1index][name2index];

                }


                Logger logger = new Logger(true);



//              logger.log(Arrays.toString(ORDERED_CHAR));

                logger.log("[");

                for (int[] row : newMatrix) {
                    logger.log(Arrays.toString(row) + ",");
                }

                logger.log("]");

                logger.writeLog(jsonFileName);

            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
