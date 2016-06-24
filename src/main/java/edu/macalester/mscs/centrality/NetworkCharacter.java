package edu.macalester.mscs.centrality;

import java.util.HashMap;

/**
 * Created by abeverid on 6/7/16.
 */
public class NetworkCharacter implements Comparable<NetworkCharacter> {


    public static final String ID = "Id";
    public static final String LABEL = "Label";
    public static final String ALLEGIANCE = "Allegiance";
    public static final String ROYAL_HOUSE = "Royal House";
    public static final String CULTURE = "Culture";
    public static final String DEGREE = "Degree";
    public static final String WEIGHTED_DEGREE = "Weighted Degree";
    public static final String MODULARITY_CLASS = "Modularity Class";
    public static final String CLOSENESS_CENTRALITY = "Closeness Centrality";
    public static final String BETWEENNESS_CENTRALITY = "Betweenness Centrality";
    public static final String AUTHORITY = "Authority";
    public static final String PAGE_RANK = "PageRank";
    public static final String CLUSTERING_COEFFICIENT = "Clustering Coefficient";
    public static final String EIGENVECTOR_CENTRALITY = "Eigenvector Centrality";

    private HashMap<String, String> map = new HashMap<>();

    /*
    private String id;
    private String label;
    private String allegiance;
    private String royalHouse;
    private String culture;

    private double degree;
    private double weightedDegree;
    private int modularityClass;


    private double closeness;
    private double betweenness;
    private double authority;
    private double pageRank;
    private double clustering;
    private double eigenvector;
    */


    public NetworkCharacter(String[] header, String[] line) {
        for (int i = 0; i < header.length; i++) {
            map.put(header[i], line[i]);
        }

    }

    /*

            if (header[i].equals("Id")) {
                id = line[i];
            } else if (header[i].equals("Label")) {
                label = line[i];
            } else if (header[i].equals("Allegiance")) {
                allegiance = line[i];
            } else if (header[i].equals("Royal House")) {
                royalHouse = line[i];
            } else if (header[i].equals("Culture")) {
                culture = line[i];
            } else if (header[i].equals("Degree")) {
                degree = Double.parseDouble(line[i]);
            } else if (header[i].equals("Weighted Degree")) {
                weightedDegree = Double.parseDouble(line[i]);
            } else if (header[i].equals("Modularity Class")) {
                modularityClass = Integer.parseInt(line[i]);
            } else if (header[i].equals("Closeness Centrality")) {
                closeness = Double.parseDouble(line[i]);
            } else if (header[i].equals("Betweenness Centrality")) {
                betweenness = Double.parseDouble(line[i]);
            } else if (header[i].equals("Authority")) {
                authority = Double.parseDouble(line[i]);
            } else if (header[i].equals("PageRank")) {
                pageRank = Double.parseDouble(line[i]);
            } else if (header[i].equals("Clustering Coefficient")) {
                clustering = Double.parseDouble(line[i]);
            } else if (header[i].equals("Eigenvector Centrality")) {
                eigenvector = Double.parseDouble(line[i]);
            }
        }
     }
*/

    public String getAttribute(String attributeName) {
        return map.get(attributeName);
    }

    public double getAttributeDouble(String attributeName) {
        return Double.parseDouble(getAttribute(attributeName));
    }

    public int getAttributeInt(String attributeName) {
        return Integer.parseInt(getAttribute(attributeName));
    }


    public String getId() { return map.get(ID); }

    public String getLabel() {
        return map.get(LABEL);
    }

    public String getAllegiance() {
        return map.get(ALLEGIANCE);
    }

    public String getRoyalHouse() {
        return map.get(ROYAL_HOUSE);
    }

    public String getCulture() {
        return map.get(CULTURE);
    }

    public double getDegree() {
        return Double.parseDouble(map.get(DEGREE));
    }

    public double getWeightedDegree() {
        return Double.parseDouble(map.get(WEIGHTED_DEGREE));
    }

    public int getModularityClass() {
        return Integer.parseInt(map.get(MODULARITY_CLASS));
    }

    public double getCloseness() {
        return Double.parseDouble(map.get(CLOSENESS_CENTRALITY));
    }

    public double getBetweenness() {
        return Double.parseDouble(map.get(BETWEENNESS_CENTRALITY));
    }

    public double getAuthority() {
        return Double.parseDouble(map.get(AUTHORITY));
    }

    public double getPageRank() {
        return Double.parseDouble(map.get(PAGE_RANK));
    }

    public double getClustering() {
        return Double.parseDouble(map.get(CLUSTERING_COEFFICIENT));
    }

    public double getEigenvector() {
        return Double.parseDouble(map.get(EIGENVECTOR_CENTRALITY));
    }


    /**
     *     Order by name
     */

    public int compareTo(NetworkCharacter c) {

//        int modCompare = c.getModularityClass() - this.getModularityClass();
//
//        if (modCompare == 0) {
//            return this.getLabel().compareTo(c.getLabel());
//        } else {
//            return modCompare;
//        }


            return this.getId().compareTo(c.getId());


    }


}
