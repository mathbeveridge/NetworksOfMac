package edu.macalester.mscs.network;

import edu.macalester.mscs.utils.WordUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author Ari Weiland
 */
public class Matrix {

    private String[] names;
    private final Map<String, Integer> nameIndices;
    private int[][] matrix;
    private final Map<Integer, Map<Integer, Encounter>> encounters;

    private boolean isModifiable = true;

    public Matrix(String[] names, Map<String, Integer> nameIndices) {
        this.names = names;
        this.nameIndices = nameIndices;
        this.matrix = new int[size()][size()];
        this.encounters = new HashMap<>();
    }

    public Matrix(String[] names, Map<String, Integer> nameIndices, String text, int reach) {
        this(names, nameIndices);
        build(text, reach);
    }

    public void build(String text, int reach) {
        StringBuilder search = new StringBuilder();
        Queue<String> nameQueue = new ArrayDeque<>(reach + 1);
        for (int i = 0; i < text.length() - 1; i++) {
            char c = text.charAt(i);
            String primary = "";
            boolean notLetter = !Character.isAlphabetic(c);
            if (notLetter) {
                for (String name : nameIndices.keySet()) {
                    if (WordUtils.endsWithWord(search.toString(), name)) {
                        primary = name;
                    }
                }
            }
            // tally neighbors
            if (!primary.isEmpty()) {
                for (String secondary : nameQueue) {
                    if (!secondary.isEmpty()) {
                        addEncounter(primary, secondary, i, search.toString());
                    }
                }
            }
            // update the search string
            search.append(c);
            // cut the string to size
            if (StringUtils.countMatches(search, ' ') > reach) {
                search = new StringBuilder(search.substring(search.indexOf(" ") + 1));
            }
            // update the queue if a word just finished
            if (i > 0 && notLetter && Character.isAlphabetic(text.charAt(i - 1))) {
                nameQueue.add(primary);
                if (nameQueue.size() > reach) { // limit its size
                    nameQueue.poll();
                }
            }
        }
    }

    public String[] getNames() {
        return names;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    /**
     * Returns a sorted list of every encounter included in this Matrix
     * @return
     */
    public List<Encounter> getEncounterList() {
        List<Encounter> encounterList = new ArrayList<>();
        for (Map<Integer, Encounter> map : encounters.values()) {
            encounterList.addAll(map.values());
        }
        Collections.sort(encounterList);
        return encounterList;
    }

    /**
     * Returns a sorted list of every encounter included in this Matrix
     * involving the specified character
     * @param name
     * @return
     */
    public List<Encounter> getEncounterList(String name) {
        List<Encounter> encounterList = new ArrayList<>();
        for (Encounter encounter : getEncounterList()) {
            if (name.equals(encounter.character1) || name.equals(encounter.character2)) {
                encounterList.add(encounter);
            }
        }
        return encounterList;
    }

    public int size() {
        return names.length;
    }

    public void addEncounter(String name1, String name2, int position) {
        addEncounter(name1, name2, position, "");
    }

    public void addEncounter(String name1, String name2, int position, String context) {
        if (!isModifiable) {
            throw new IllegalStateException("This matrix has been cleaned and can no longer be modified.");
        }
        int index1 = nameIndices.get(name1);
        int index2 = nameIndices.get(name2);
        if (!encounters.containsKey(position)) {
            encounters.put(position, new HashMap<Integer, Encounter>());
        }
        // avoid duplicate encounters at the same position
        if (index1 != index2 && !encounters.get(position).containsKey(index2)) {
            matrix[index1][index2]++;
            matrix[index2][index1]++;
            encounters.get(position).put(index2, new Encounter(name1, name2, position, context));
        }
    }

    /**
     * Removes any connections whose strength is below the specified noise threshold
     * Also removes any characters who have no connections
     * @param noise
     * @return
     */
    public List<String> cleanNoise(int noise) {
        List<String> lines = new ArrayList<>();
        lines.add("Removing noisy connections:");
        // clean noise
        for (int i=0; i<size(); i++) {
            for (int j=0; j<size(); j++) {
                if (matrix[i][j] < noise && matrix[i][j] > 0) {
                    // remove really weak connections
                    lines.add(names[i] +  ", " + names[j] + ", " + matrix[i][j]);
                    matrix[i][j] = 0;
                }
            }
            // make sure the diagonal is 0
            matrix[i][i] = 0;
        }
        // get people without connections
        Set<Integer> loners = new HashSet<>();
        for (int i=0; i<size(); i++) {
            boolean loner = true;
            for (int j=0; j<size() && loner; j++) {
                if (matrix[i][j] > 0) {
                    loner = false;
                }
            }
            if (loner) {
                loners.add(i);
            }
        }
        lines.add("Removing loners: " + removeRows(loners));
        return lines;
    }

    /**
     * Removes any characters not connected to the network containing
     * the character whose index is 0
     * @return
     */
    public List<String> cleanFloaters() {
        return cleanFloaters(0);
    }

    /**
     * Removes any characters not connected to the network containing
     * the character whose index is entryPoint
     * @param entryPoint
     * @return
     */
    public List<String> cleanFloaters(int entryPoint) {
        // get people without connections
        Set<Integer> floaters = new HashSet<>();
        for (int i=0; i<names.length; i++) {
            floaters.add(i);
        }
        Queue<Integer> bfs = new ArrayDeque<>();
        bfs.add(entryPoint);
        floaters.remove(entryPoint);
        while (!bfs.isEmpty()) {
            int index = bfs.poll();
            for (int i=0; i<size(); i++) {
                if (matrix[index][i] > 0 && floaters.contains(i)) {
                    bfs.add(i);
                    floaters.remove(i);
                }
            }
        }
        List<String> lines = new ArrayList<>();
        lines.add("Removing floating characters: " + removeRows(floaters));
        return lines;
    }

    /**
     * Iteratively removes characters with only one connection until the map stabilizes.
     * @return
     */
    public List<String> cleanSingletons() {
        List<String> lines = new ArrayList<>();
        // get people with only one connection
        Set<Integer> singletons = new HashSet<>();
        do {
            singletons.clear();
            for (int i = 0; i < size(); i++) {
                int neighbors = 0;
                for (int j = 0; j < size(); j++) {
                    if (matrix[i][j] > 0) {
                        neighbors++;
                    }
                }
                if (neighbors < 2) {
                    singletons.add(i);
                }
            }
            lines.add("Removing singletons: " + removeRows(singletons));
        } while (!singletons.isEmpty());
        return lines;
    }

    /**
     * Iteratively removes characters with only one connection for a fixed number of iterations.
     * @return
     */
    public List<String> cleanSingletons(int iterations) {
        List<String> lines = new ArrayList<>();
        // get people with only one connection
        for (int n=0; n<iterations; n++) {
            Set<Integer> singletons = new HashSet<>();
            for (int i=0; i<size(); i++) {
                int neighbors = 0;
                for (int j=0; j<size(); j++) {
                    if (matrix[i][j] > 0) {
                        neighbors++;
                    }
                }
                if (neighbors < 2) {
                    singletons.add(i);
                }
            }
            lines.add("Removing singletons: " + removeRows(singletons));
        }
        return lines;
    }

    /**
     * Converts the Matrix to CSV lines of a matrix.
     * The first line is the name headers.
     * @return
     */
    public List<String> toMatrixCsvLines() {
        List<String> lines = new ArrayList<>();
        lines.add(cleanArrayString(Arrays.toString(names)));
        for (int[] row : matrix) {
            lines.add(cleanArrayString(Arrays.toString(row)));
        }
        return lines;
    }

    /**
     * Converts the matrix to CSV lines of a list of edges, for import into Gephi.
     * The header is "Source,Target,Weight,Type", where the type is always undirected.
     * This is equivalent to calling
     * toEdgeListCsvLines("Source,Target,Weight,Type", "#C1,#C2,#W,undirected")
     * @return
     */
    public List<String> toEdgeListCsvLines() {
        return toEdgeListCsvLines("Source,Target,Weight,Type", "#C1,#C2,#W,undirected");
    }

    /**
     * Converts the matrix to CSV lines of a list of edges, with customizable parameters.
     * The defaultValues parameter can contain default string values, or data references.
     * Use '#C1' to reference the first character in an edge.
     * Use '#C2' to reference the second character in an edge.
     * Use '#W' to reference the weight of an edge.
     * Make sure to include quotes as necessary, though they must not be included around
     * the three special references.
     * @param header
     * @param defaultValue
     * @return
     */
    public List<String> toEdgeListCsvLines(String header, String defaultValue) {
        List<String> lines = new ArrayList<>();
        lines.add(header);
        for (int i=0; i<size(); i++) {
            for (int j=i+1; j<size(); j++) {
                if (matrix[i][j] > 0) {
                    String line = defaultValue
                            .replace("#C1", "\"" + names[i] + "\"")
                            .replace("#C2", "\"" + names[j] + "\"")
                            .replace("#W", Double.toString(matrix[i][j]));
                    lines.add(line);
                }
            }
        }
        return lines;
    }

    private static String cleanArrayString(String arrayString) {
        return StringUtils.strip(arrayString, "[]").replaceAll(" ", "");
    }

    private String removeRows(Set<Integer> removed) {
        isModifiable = false;
        StringBuilder sb = new StringBuilder();
        if (!removed.isEmpty()) {
            int newLength = size() - removed.size();
            String[] cleanNames = new String[newLength];
            int[][] cleanMatrix = new int[newLength][newLength];
            int row=0;
            for(int i=0; i < size(); i++){
                if (!removed.contains(i)) {
                    cleanNames[row] = names[i]; // clean names
                    int col = 0;
                    for (int j=0; j<size(); j++) { // clean matrix
                        if (!removed.contains(j)) {
                            cleanMatrix[row][col] = matrix[i][j];
                            col++;
                        }
                    }
                    row++;
                } else {
                    sb.append(names[i]).append(" ");
                }
            }
            names = cleanNames;
            matrix = cleanMatrix;
        }
        return sb.toString();
    }

    private static boolean endsWithName(String input, String name) {
        return input.matches("(.*\\W)?" + name);
    }
}
