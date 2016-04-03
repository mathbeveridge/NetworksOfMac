package edu.macalester.mscs.network;

import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;

import java.util.*;

/**
 * Modified version of the same class of JUNG for weighted-edge scoring.
 * @author Jie Shan
 */
public class EdgeBetweennessClusterer<V,E> {

    private final int numEdgesToRemove;
    private final Map<E, Pair<V>> edgesRemoved;

   /**
    * Constructs a new clusterer for the specified graph.
    * @param numEdgesToRemove the number of edges to be progressively removed from the graph
    */
    public EdgeBetweennessClusterer(int numEdgesToRemove) {
        if (numEdgesToRemove < 0) {
            throw new IllegalArgumentException("Invalid number of edges passed in: " + numEdgesToRemove);
        }

        this.numEdgesToRemove = numEdgesToRemove;
        this.edgesRemoved = new LinkedHashMap<>();
    }

    /**
    * Finds the set of clusters which have the strongest "community structure".
    * The more edges removed the smaller and more cohesive the clusters.
    * @param graph the graph
    */
    public Set<Set<V>> transform(Graph<V, E> graph, HashMap<E, Double> weights) {
                
        if (numEdgesToRemove > graph.getEdgeCount()) {
            throw new IllegalArgumentException("Graph does not contain enough edges to remove.");
        }

        edgesRemoved.clear();

        for (int k=0;k< numEdgesToRemove;k++) {
            BetweennessCentrality<V,E> bc = new BetweennessCentrality<>(graph);
            E toRemove = null;
            double score = 0;
            for (E e : graph.getEdges()){
            	double newScore = bc.getEdgeScore(e);
//                if (newScore*1.0/weights.get(e) > score) {
                if (newScore > score) {
                    toRemove = e;
                    score = newScore;
                }
            }
            edgesRemoved.put(toRemove, graph.getEndpoints(toRemove));
            graph.removeEdge(toRemove);
        }

        WeakComponentClusterer<V,E> wcSearch = new WeakComponentClusterer<>();
        Set<Set<V>> clusterSet = wcSearch.transform(graph);

        for (Map.Entry<E, Pair<V>> entry : edgesRemoved.entrySet())
        {
            Pair<V> endpoints = entry.getValue();
            graph.addEdge(entry.getKey(), endpoints.getFirst(), endpoints.getSecond());
        }
        return clusterSet;
    }

    /**
     * Retrieves the list of all edges that were removed 
     * (assuming extract(...) was previously called).  
     * The edges returned
     * are stored in order in which they were removed.
     * 
     * @return the edges in the original graph
     */
    public List<E> getEdgesRemoved() 
    {
        return new ArrayList<>(edgesRemoved.keySet());
    }

}

