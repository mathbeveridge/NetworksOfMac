package edu.macalester.mscs.characters;

import edu.macalester.mscs.utils.EntryComparator;
import edu.macalester.mscs.utils.FileUtils;
import edu.macalester.mscs.utils.WordUtils;

import java.util.*;

/**
 * @author Ari Weiland
 */
public class CharacterGroups {

    private final Map<String, Set<String>> groups;
    private final Map<String, String> redirects;
    private final Map<String, Integer> counter;

    public CharacterGroups() {
        this.groups = new HashMap<>();
        this.redirects = new HashMap<>();
        this.counter = new HashMap<>();
    }

    public CharacterGroups(Map<String, Integer> counter) {
        this(counter, new HashSet<String>());
    }

    public CharacterGroups(Map<String, Integer> counter, Set<String> nondescriptors) {
        this();
        for (String alias : counter.keySet()) {
            if (!alias.contains(" ") && !nondescriptors.contains(alias)) {
                addAlias(alias, counter.get(alias));
            }
        }
        for (String alias : counter.keySet()) {
            if (!nondescriptors.contains(alias)) {
                String[] split = alias.split(" ");
                if (split.length > 1) {
                    String group = null;
                    for (String s : split) {
                        if (isAlias(s)) {
                            if (group == null) {
                                group = getPrimaryAlias(s);
                                addAliasToGroup(group, alias, counter.get(alias));
                            } else {
                                combineGroups(group, s);
                            }
                        }
                    }
                    if (group == null) {
                        addAlias(alias, counter.get(alias));
                    }
                }
            }
        }
    }

    public void addAlias(String alias, int count) {
        if (!isAlias(alias)) {
            Set<String> aliases = new HashSet<>();
            aliases.add(alias);
            groups.put(alias, aliases);
        }
        counter.put(alias, count);
    }

    public void addAliasToGroup(String group, String alias, int count) {
        getGroup(group).add(alias);
        redirects.put(alias, getPrimaryAlias(group));
        counter.put(alias, count);
    }

    public void combineGroups(String... groups) {
        combineGroups(Arrays.asList(groups));
    }

    public void combineGroups(Collection<String> groups) {
        String first = null;
        for (String group : groups) {
            if (!isAlias(group)) {
                throw new IllegalArgumentException(group + " is not an alias in these groups.");
            }
            if (first == null) {
                first = group;
            } else {
                String group1 = getPrimaryAlias(first);
                String group2 = getPrimaryAlias(group);
                if (!group1.equals(group2)) {
                    getGroup(group1).addAll(this.groups.remove(group2));
                    redirects.put(group2, group1);
                }
            }
        }
    }

    public boolean isAlias(String alias) {
        return groups.containsKey(alias) || redirects.containsKey(alias);
    }

    public String getPrimaryAlias(String alias) {
        while (redirects.containsKey(alias)) {
            alias = redirects.get(alias);
        }
        return alias;
    }

    public Set<String> getGroup(String alias) {
        return groups.get(getPrimaryAlias(alias));
    }

    public Set<String> getPrimaryAliases() {
        return groups.keySet();
    }

    public Map<Set<String>, Integer> getGroups() {
        Map<Set<String>, Integer> map = new HashMap<>();
        for (String s : groups.keySet()) {
            map.put(getGroup(s), getAliasCount(s));
        }
        return map;
    }

    public int getAliasCount(Set<String> group) {
        int count = 0;
        for (String s : group) {
            count += counter.get(s);
        }
        return count;
    }

    public int getAliasCount(String group) {
        return getAliasCount(getGroup(group));
    }

    /**
     * Returns a list of strings, each of which corresponds to one of the CharacterGroups
     * Each line contains all the aliases in that group, and each line is one group
     * Each line corresponds (in iteration order) to the group associated with the name in names
     *
     * @return
     */
    public void writeNameList(String file) {
        writeNameList(false, file);
    }

    /**
     * Returns a list of strings, each of which corresponds to one of the CharacterGroups
     * Each line contains all the aliases in that group, and each line is one group
     * Each line corresponds (in iteration order) to the group associated with the name in names
     *
     * The removeRedundancy flag will remove names whose components are in the same group
     *
     * @return
     */
    public void writeNameList(boolean removeRedundancy, String file) {
        writeNameList(removeRedundancy, 0, file);
    }

    /**
     * Returns a list of strings, each of which corresponds to one of the CharacterGroups
     * Each line contains all the aliases in that group, and each line is one group
     * Each line corresponds (in iteration order) to the group associated with the name in names
     *
     * Only names that occur more than minimumOccurrence times will be included
     *
     * @return
     */
    public void writeNameList(int minimumOccurrence, String file) {
        writeNameList(false, minimumOccurrence, file);
    }

    /**
     * Returns a list of strings, each of which corresponds to one of the CharacterGroups
     * Each line contains all the aliases in that group, and each line is one group
     * Each line corresponds (in iteration order) to the group associated with the name in names
     *
     * The removeRedundancy flag will remove names whose components are in the same group
     * Only names that occur more than minimumOccurrence times will be included
     *
     * @return
     */
    public void writeNameList(boolean removeRedundancy, int minimumOccurrence, String file) {
        writeNameList(getPrimaryAliases(), removeRedundancy, minimumOccurrence, file);
    }

    /**
     * Returns a list of strings, each of which corresponds to one of the CharacterGroups
     * Each line contains all the aliases in that group, and each line is one group
     * Each line corresponds (in iteration order) to the group associated with the name in names
     *
     * @return
     */
    public void writeNameList(Collection<String> names, String file) {
        writeNameList(names, false, file);
    }

    /**
     * Returns a list of strings, each of which corresponds to one of the CharacterGroups
     * Each line contains all the aliases in that group, and each line is one group
     * Each line corresponds (in iteration order) to the group associated with the name in names
     *
     * The removeRedundancy flag will remove names whose components are in the same group
     *
     * @return
     */
    public void writeNameList(Collection<String> names, boolean removeRedundancy, String file) {
        writeNameList(names, removeRedundancy, 0, file);
    }

    /**
     * Returns a list of strings, each of which corresponds to one of the CharacterGroups
     * Each line contains all the aliases in that group, and each line is one group
     * Each line corresponds (in iteration order) to the group associated with the name in names
     *
     * Only names that occur more than minimumOccurrence times will be included
     *
     * @return
     */
    public void writeNameList(Collection<String> names, int minimumOccurrence, String file) {
        writeNameList(names, false, minimumOccurrence, file);
    }

    /**
     * Returns a list of strings, each of which corresponds to one of the CharacterGroups
     * Each line contains all the aliases in that group, and each line is one group
     * Each line corresponds (in iteration order) to the group associated with the name in names
     *
     * The removeRedundancy flag will remove names whose components are in the same group
     * Only names that occur more than minimumOccurrence times will be included
     *
     * @return
     */
    public void writeNameList(Collection<String> names, boolean removeRedundancy, int minimumOccurrence, String file) {
        FileUtils.writeFile(getNameList(names, removeRedundancy, minimumOccurrence), file);
    }

    private List<String> getNameList(Collection<String> names, boolean removeRedundancy, int minimumOccurrence) {
        Map<List<String>, Integer> groupMap = new HashMap<>();
        for (String name : names) {
            if (isAlias(name)) {
                int aliasCount = getAliasCount(name);
                if (aliasCount >= minimumOccurrence) {
                    List<String> list = new ArrayList<>();
                    Set<String> group = getGroup(name);
                    for (String alias : group) {
                        String[] split = alias.split(" ");
                        boolean isUnique = true;
                        if (removeRedundancy && split.length > 1) {
                            for (String s : split) {
                                if (group.contains(s)) {
                                    isUnique = false;
                                }
                            }
                        }
                        if (isUnique) {
                            list.add(alias);
                        }
                    }

                    Collections.sort(list, WordUtils.DESCENDING_LENGTH_COMPARATOR);
                    list.add(0, name);
                    groupMap.put(list, aliasCount);
                }
            }
        }

        List<Map.Entry<List<String>, Integer>> groups = new ArrayList<>(groupMap.entrySet());
        groups.sort(EntryComparator.DESCENDING);
        List<String> lines = new ArrayList<>();
        for (Map.Entry<List<String>, Integer> group : groups) {
            System.out.println(group.getValue() + "\t" + group.getKey());
            StringBuilder line = new StringBuilder();
            boolean first = true;
            for (String name : group.getKey()) {
                if (first) {
                    first = false;
                } else {
                    line.append(",");
                }
                line.append(name);
            }
            lines.add(line.toString());
        }
        System.out.println();
        System.out.println(groups.size());
        System.out.println();
        return lines;
    }
}
