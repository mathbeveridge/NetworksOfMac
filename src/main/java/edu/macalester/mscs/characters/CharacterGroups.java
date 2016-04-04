package edu.macalester.mscs.characters;

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
//        this.counter.putAll(counter);
        for (String alias : counter.keySet()) {
            if (!alias.contains(" ") && !nondescriptors.contains(alias)) {
                addAlias(alias, counter.get(alias));
            }
        }
        for (String alias : counter.keySet()) {
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

    public void combineGroups(String group1, String group2) {
        group1 = getPrimaryAlias(group1);
        group2 = getPrimaryAlias(group2);
        if (!group1.equals(group2)) {
            getGroup(group1).addAll(groups.remove(group2));
            redirects.put(group2, group1);
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
}
