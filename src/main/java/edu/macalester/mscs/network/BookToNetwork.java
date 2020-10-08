package edu.macalester.mscs.network;


public class BookToNetwork {

    /**
     * Turn a book into a network. If no arguments are passed, then it tries to process Pride and Prejudice
     * Otherwise, there are 7 required arguments
     * args[0] = file name of the book
     * args[1] = file name of the character csv
     * args[2] = output directory name
     * args[3] = prefix id for the output files
     * args[4] = radius of words to use
     * args[5] = noise to ignore: edges with weights below the noise are discarded
     *
     * @param args
     */
    public static void main(String[] args) {

        if (args.length == 0) {
            oldMain(args);
        } else {
            // The data sources
            String textFileName = args[0];
            String characterFileName = args[1];


            // the output file information
            String outDir = args[2];
            String bookId = args[3];
            String fileDesc = "";

            int radius = Integer.parseInt(args[4]);
            int noise = Integer.parseInt(args[5]);

            MatrixConstructor mc = new MatrixConstructor(bookId, textFileName, characterFileName, radius, noise);

            mc.constructMatrix(fileDesc, outDir);
            mc.writeFiles(fileDesc, outDir, false);
        }
    }


    /**
     * The default option is to process Pride and Prejudice
     * @param args
     */
    public static void oldMain(String[] args) {

        // The data sources
        String textFileName = "src/main/resources/text/pandp-hyphen.txt";
        String characterFileName = "src/main/resources/data/characters/pp-char-hyphen.csv";


        // the output file information
        String outDir = "src/main/resources/data/output";
        String bookId = "PP-1";
        String fileDesc = "test";

        int radius = 12;
        int noise = 1;

        System.out.println("Running BookToNetwork with default configuration");

        MatrixConstructor mc = new MatrixConstructor(bookId, textFileName, characterFileName, radius, noise);

        mc.constructMatrix(fileDesc, outDir);
        mc.writeFiles(fileDesc, outDir, false);
    }
}
