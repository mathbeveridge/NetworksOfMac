package edu.macalester.mscs.centrality;

import java.util.Comparator;

/**
 * Created by abeverid on 6/24/16.
 */
public class NetworkCharacterComparator implements Comparator<NetworkCharacter> {

    String attribute;

    public NetworkCharacterComparator(String attributeName) {
        this.attribute = attributeName;
    }

    @Override
    public int compare(NetworkCharacter o1, NetworkCharacter o2) {
        double diff =  o2.getAttributeDouble(attribute) - o1.getAttributeDouble(attribute);

        if (diff < 0) {
            return -1;
        } else if (diff > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
