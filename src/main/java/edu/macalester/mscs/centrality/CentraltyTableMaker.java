package edu.macalester.mscs.centrality;

import edu.macalester.mscs.utils.FileUtils;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by abeverid on 6/7/16.
 */
public class CentraltyTableMaker {



    public static final String TEMPLATE_NAME = "src/main/resources/centralities/centralities-template.tex";

    public static final String DEFAULT_DATA_FILE_NAME = "src/main/resources/data/network/GoT#NUM-06-07-node-data-rounded.csv";

    public static final String DEFAULT_OUTPUT_FILE_NAME = "src/main/resources/data/output/centrality2/GoT#NUM-06-07-centralities.tex";


    public static final String RANK_LINE_TEMPLATE = "\\node at (#XVAL,#YVAL) { \\tiny \\mycircled[10]{#RANK}  };";


    private Set<NetworkCharacter> charSet = null;

    private String dataFileName;

    private String outputFileName;

    private double minPageRank;


    public CentraltyTableMaker(String bookNum, double minimumPageRank) {
        dataFileName = DEFAULT_DATA_FILE_NAME.replace("#NUM", bookNum);
        outputFileName = DEFAULT_OUTPUT_FILE_NAME.replace("#NUM", bookNum);
        minPageRank = minimumPageRank;
    }

    public static void main(String[] args) {
        String[] bookNums = {"1", "2", "3", "4", "5", "-all"};
        double[] minPageRanks = {0.013,0.01, 0.009, 0.009,0.0089,0.006};

        for (int i=0; i < 1; i++) {
            CentraltyTableMaker maker = new CentraltyTableMaker(bookNums[i], minPageRanks[i]);

            maker.createCentralityTable();
        }
    }

    public static void main1(String[] args) {
        double minPageRank = 0.013;

        CentraltyTableMaker maker =
                new CentraltyTableMaker("1", minPageRank);

        maker.testRanking();

    }


    public void createCentralityTable() {
        // load in the characters with high PageRank
        Set<NetworkCharacter> charSet = getNetworkCharactersByPageRank(getMinPageRank());

        DecimalFormat decimalFormat = new DecimalFormat("#.###");



        // create the file from the template
        List<String> templateLines = FileUtils.readFile(TEMPLATE_NAME);
        List<String> newLines = new ArrayList<String>();

        for (String line : templateLines) {
            if (! line.startsWith("%%%")) {
                newLines.add(line);
            } else if (line.startsWith("%%% yCoords")) {
                newLines.add(getCoordinateLine(charSet));
            } else if (line.startsWith("%%% degreeData")) {
                for (NetworkCharacter character : charSet) {
                    double maxDegree = getMaxDegree();
                    double deg = character.getDegree() / maxDegree;
                    newLines.add(deg + "\t" + character.getShortName());
                }
            } else if (line.startsWith("%%% degreeRanking")) {
                newLines.addAll(getAttributeRankLines(NetworkCharacter.DEGREE, charSet));
            } else if (line.startsWith("%%% degreeTick")) {
                newLines.add("xticklabels={0,,,," + (int) getMaxDegree() + "},");
            } else if (line.startsWith("%%% weightedDegreeData")) {
                for (NetworkCharacter character : charSet) {
                    double maxWeightedDegree = getMaxWeightedDegree();
                    double weightDeg = character.getWeightedDegree() / maxWeightedDegree;
                    newLines.add(weightDeg + "\t" + character.getShortName());
                }
            } else if (line.startsWith("%%% weightedDegreeRanking")) {
                newLines.addAll(getAttributeRankLines(NetworkCharacter.WEIGHTED_DEGREE, charSet));
            } else if (line.startsWith("%%% weightedDegreeTick")) {
                newLines.add("xticklabels={0,,,," + (int) getMaxWeightedDegree() + "},");
            } else if (line.startsWith("%%% eigenvectorData")) {
                for (NetworkCharacter character : charSet) {
                    double maxEig = getMaxEigenvector();
                    double eig = character.getEigenvector() / maxEig;
                    newLines.add(eig + "\t" + character.getShortName());
                }
            } else if (line.startsWith("%%% eigenvectorRanking")) {
                newLines.addAll(getAttributeRankLines(NetworkCharacter.EIGENVECTOR_CENTRALITY, charSet));
            } else if (line.startsWith("%%% eigenvectorTick")) {
                newLines.add("xticklabels={0,,,," +  decimalFormat.format(getMaxEigenvector()) + "},");
            } else if (line.startsWith("%%% pageRankData")) {
                for (NetworkCharacter character : charSet) {
                    double max = getMaxPageRank();
                    double pr = character.getPageRank() / max;
                    newLines.add(pr + "\t" + character.getShortName());
                }
            } else if (line.startsWith("%%% pageRankRanking")) {
                newLines.addAll(getAttributeRankLines(NetworkCharacter.PAGE_RANK, charSet));
            } else if (line.startsWith("%%% pageRankTick")) {
                newLines.add("xticklabels={0,,,," +  decimalFormat.format(getMaxPageRank()) + "},");
            } else if (line.startsWith("%%% closenessData")) {
                for (NetworkCharacter character : charSet) {
                    double max = getMaxCloseness();
                    double pr = character.getCloseness() / max;
                    newLines.add(pr + "\t" + character.getShortName());
                }
            } else if (line.startsWith("%%% closenessRanking")) {
                newLines.addAll(getAttributeReverseRankLines(NetworkCharacter.CLOSENESS_CENTRALITY, charSet));
            } else if (line.startsWith("%%% closenessTick")) {
                newLines.add("xticklabels={0,,,," +  decimalFormat.format(getMaxCloseness()) + "},");
            } else if (line.startsWith("%%% betweennessData")) {
                for (NetworkCharacter character : charSet) {
                    double max = getMaxBetweenness();
                    double pr = character.getBetweenness() / max;
                    newLines.add(pr + "\t" + character.getShortName());
                }
            } else if (line.startsWith("%%% betweennessRanking")) {
                newLines.addAll(getAttributeRankLines(NetworkCharacter.BETWEENNESS_CENTRALITY, charSet));
            } else if (line.startsWith("%%% betweennessTick")) {
                newLines.add("xticklabels={0,,,," +  decimalFormat.format(getMaxBetweenness()) + "},");
            } else {
                System.out.println("Unknown category: " + line);
                newLines.add(line);
            }
        }

        FileUtils.writeFile(newLines, getOutputFileName());

    }


    private Set<NetworkCharacter> getNetworkCharacters() {

        if (charSet == null) {

            System.out.println(getDataFileName());
            List<String> lines = FileUtils.readFile(getDataFileName());

            charSet = new TreeSet<NetworkCharacter>();

            String[] header = lines.get(0).split(",");

            for (int i = 1; i < lines.size(); i++) {
                String[] line = lines.get(i).split(",");
                System.out.println("adding " + line[0]);
                charSet.add(new NetworkCharacter(header, line));
                System.out.println("added " + line[0]);
            }
        }

        return charSet;
    }

    /**
     * Returns the list of characters whose PageRank is at least minPageRank.
     * @param minPageRank
     * @return
     */
    private Set<NetworkCharacter> getNetworkCharactersByPageRank(double minPageRank) {
        TreeSet<NetworkCharacter> list = new TreeSet<NetworkCharacter>();

        System.out.println("Using min pageRank=" + minPageRank + " and total characters=" + getNetworkCharacters().size());

        for (NetworkCharacter character : getNetworkCharacters()) {
            if (character.getPageRank() >= minPageRank) {
                list.add(character);
                System.out.println("\t added " + character.getId());
            } else {
                System.out.println(minPageRank + " is larger than " + character.getPageRank() + " for " + character.getId());
            }
        }

        return list;
    }


    private double getMax(String attributeName) {
        double max = 0.0;

        for (NetworkCharacter nc : getNetworkCharacters()) {
            if (nc.getAttributeDouble(attributeName) > max) {
                max = nc.getAttributeDouble(attributeName);
            }
        }

        return max;

    }


    private double getMaxDegree() {
        double degree = 0.0;

        for (NetworkCharacter nc : getNetworkCharacters()) {
            if (nc.getDegree() > degree) {
                degree = nc.getDegree();
            }
        }

        return degree;
    }

    private double getMaxWeightedDegree() {
        double wd = 0.0;

        for (NetworkCharacter nc : getNetworkCharacters()) {
            if (nc.getWeightedDegree() > wd) {
                wd = nc.getWeightedDegree();
            }
        }

        return wd;
    }

    private double getMaxEigenvector() {
        double eig = 0.0;

        for (NetworkCharacter nc : getNetworkCharacters()) {
            if (nc.getEigenvector() > eig) {
                eig = nc.getEigenvector();
            }
        }

        return eig;
    }

    private double getMaxPageRank() {
        double max = 0.0;

        for (NetworkCharacter nc : getNetworkCharacters()) {
            if (nc.getPageRank() > max) {
                max = nc.getPageRank();
            }
        }

        return max;
    }

    private double getMaxCloseness() {
        double max = 0.0;

        for (NetworkCharacter nc : getNetworkCharacters()) {
            if (nc.getCloseness() > max) {
                max = nc.getCloseness();
            }
        }

        return max;
    }

    private double getMaxBetweenness() {
        double max = 0.0;

        for (NetworkCharacter nc : getNetworkCharacters()) {
            if (nc.getBetweenness() > max) {
                max = nc.getBetweenness();
            }
        }

        return max;
    }



    public String getDataFileName() {
        return dataFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public double getMinPageRank() {
        return minPageRank;
    }

    public String getCoordinateLine(Set<NetworkCharacter> tableChars) {
//        return "  symbolic y coords={ Robert, Stannis, Z, Cersei, Jaime, Joffrey, Tyrion, Tywin, Y, Arya, Bran, Catelyn, Jon, Robb, Sansa, X, Daenerys},";
        StringBuffer sb = new StringBuffer();

        sb.append("  symbolic y coords={ " );

        for (NetworkCharacter nc : tableChars) {
            sb.append(nc.getShortName() + ", ");
        }

        sb.deleteCharAt(sb.length()-1);
        sb.append("},");


        return sb.toString();

    }


    private Map<NetworkCharacter, Integer> getRankByAttribute(String attributeName) {
        return getRankByAttribute(attributeName, false);
    }

    private Map<NetworkCharacter, Integer> getRankByAttribute(String attributeName, boolean reverse) {
        List<NetworkCharacter> charList = new ArrayList(charSet);
        Collections.sort(charList, new NetworkCharacterComparator(attributeName));

        if (reverse) {
            Collections.reverse(charList);
        }

        HashMap<NetworkCharacter, Integer> rankMap = new HashMap<>();

        int currentRank = 0;
        double currentValue = 0.0;

        for (int i=0; i < charList.size(); i++) {
            NetworkCharacter c = charList.get(i);

            if (currentRank == 0) {
                currentRank = 1;
                currentValue = c.getAttributeDouble(attributeName);
            } else if (reverse) {
                if (c.getAttributeDouble(attributeName) > currentValue) {
                    currentRank = i+1;
                    currentValue = c.getAttributeDouble(attributeName);
                }
            } else {
                if (c.getAttributeDouble(attributeName) < currentValue) {
                    currentRank = i+1;
                    currentValue = c.getAttributeDouble(attributeName);
                }
            }

            rankMap.put(c, new Integer(currentRank));

        }


        return rankMap;
    }




    public void testRanking() {
        // load in the characters with high PageRank
        Set<NetworkCharacter> charSet = getNetworkCharactersByPageRank(getMinPageRank());


        Map<NetworkCharacter,Integer> map = getRankByAttribute(NetworkCharacter.AUTHORITY);

        for (NetworkCharacter c : charSet) {
            System.out.println("char=" + c.getId() + " ranking=" + map.get(c));
        }
    }

    /**
     * Creates the lines that adds the ranks (circled) for the attributes.
     * @param attributeName
     * @param charSet
     * @return
     */
    private List<String> getAttributeRankLines(String attributeName, Set<NetworkCharacter> charSet) {
        ArrayList<String> lines = new ArrayList<>();
        double maxAttribute = getMax(attributeName);
        double yval = charSet.size() * 0.5 - 0.05;
        Map<NetworkCharacter, Integer> map = getRankByAttribute(attributeName);
        for (NetworkCharacter character : charSet) {
            double xval = character.getAttributeDouble(attributeName)/maxAttribute * 1.3 + .22;
            lines.add(RANK_LINE_TEMPLATE.replace("#XVAL", Double.toString(xval)).
                    replace("#YVAL", Double.toString(yval)).
                    replace("#RANK", map.get(character).toString()));
            yval = yval - 0.5;
        }

        return lines;
    }


    private List<String> getAttributeReverseRankLines(String attributeName, Set<NetworkCharacter> charSet) {
        ArrayList<String> lines = new ArrayList<>();
        double maxAttribute = getMax(attributeName);
        double yval = charSet.size() * 0.5 - 0.05;
        Map<NetworkCharacter, Integer> map = getRankByAttribute(attributeName, true);
        for (NetworkCharacter character : charSet) {
            double xval = character.getAttributeDouble(attributeName)/maxAttribute * 1.3 + .22;
            lines.add(RANK_LINE_TEMPLATE.replace("#XVAL", Double.toString(xval)).
                    replace("#YVAL", Double.toString(yval)).
                    replace("#RANK", map.get(character).toString()));
            yval = yval - 0.5;
        }

        return lines;
    }
}
