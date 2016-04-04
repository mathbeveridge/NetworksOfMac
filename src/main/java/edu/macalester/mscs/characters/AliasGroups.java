package edu.macalester.mscs.characters;

import java.util.*;

/**
 * @author Ari Weiland
 */
public class AliasGroups {

    private final Map<String, Set<String>> groups;
    private final Map<String, String> redirects;

    public AliasGroups() {
        this.groups = new HashMap<>();
        this.redirects = new HashMap<>();
    }

    public void addAlias(String alias) {
        if (!isAlias(alias)) {
            Set<String> aliases = new HashSet<>();
            aliases.add(alias);
            groups.put(alias, aliases);
        }
    }

    public void addAliasToGroup(String group, String alias) {
        getGroup(group).add(alias);
        redirects.put(alias, getPrimaryAlias(group));
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

    public Collection<Set<String>> getGroups() {
        return groups.values();
    }
}
