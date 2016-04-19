package edu.macalester.mscs.utils;

import java.util.Comparator;
import java.util.Map;

/**
 * This is a simple comparator for sorting map entries by their value, which must be an integer.
 *
 * @author Ari Weiland
 */
public class EntryComparator implements Comparator<Map.Entry<?, Integer>> {

    public static final Comparator<Map.Entry<?, Integer>> ASCENDING = new EntryComparator();
    public static final Comparator<Map.Entry<?, Integer>> DESCENDING = ASCENDING.reversed();

    private EntryComparator() {}

    @Override
    public int compare(Map.Entry<?, Integer> o1, Map.Entry<?, Integer> o2) {
        return o1.getValue() - o2.getValue();
    }
}
