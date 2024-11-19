package graphs.shortestpaths;

import graphs.Edge;
import graphs.Graph;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Topological sorting implementation of the {@link ShortestPathSolver} interface for <b>directed acyclic graphs</b>.
 *
 * @param <V> the type of vertices.
 * @see ShortestPathSolver
 */
public class ToposortDAGSolver<V> implements ShortestPathSolver<V> {
    private final Map<V, Edge<V>> edgeTo;
    private final Map<V, Double> distTo;

    private int counter;
    /**
     * Constructs a new instance by executing the toposort-DAG-shortest-paths algorithm on the graph from the start.
     *
     * @param graph the input graph.
     * @param start the start vertex.
     */
    public ToposortDAGSolver(Graph<V> graph, V start) {
        edgeTo = new HashMap<>();
        distTo = new HashMap<>();

        edgeTo.put(start, null);
        distTo.put(start, 0.0);

        Set<V> visited = new HashSet<V>();
        List<V> result = new ArrayList<V>();

        dfsPostOrder(graph, start, visited, result);

        Collections.reverse(result);

        //relax edges
        for (V node : result) {
            for (Edge<V> edge : graph.neighbors(node)) {
                //System.out.println("DistTo: " + distTo.get(edge.to) + " ||||| Node: " + distTo.get(node) + " ||||| From: " + distTo.get(edge.from) + " ||||| Weight: " + edge.weight);
                double oldDist = distTo.getOrDefault(edge.to, Double.POSITIVE_INFINITY);
                double newDist = distTo.get(node) + edge.weight;
                if (newDist < oldDist) {
                    edgeTo.put(edge.to, edge);
                    distTo.put(edge.to, newDist);
                }
            }
        }
    }

    /**
     * Recursively adds nodes from the graph to the result in DFS postorder from the start vertex.
     *
     * @param graph   the input graph.
     * @param start   the start vertex.
     * @param visited the set of visited vertices.
     * @param result  the destination for adding nodes.
     */
    // Proud of adapting code from Djikstra solver to complete this section
    private void dfsPostOrder(Graph<V> graph, V start, Set<V> visited, List<V> result) {
        visited.add(start);

        for (Edge<V> edge : graph.neighbors(start)) {
            edgeTo.put(edge.to, edge);
            distTo.put(edge.to, edge.weight + distTo.get(edge.from));

            if (!visited.contains(edge.to)) {
                counter += 1;
                dfsPostOrder(graph, edge.to, visited, result);
            }
        }
        result.add(start);
    }


    @Override
    public List<V> solution(V goal) {
        List<V> path = new ArrayList<>();
        V curr = goal;
        path.add(curr);
        while (edgeTo.get(curr) != null) {
            curr = edgeTo.get(curr).from;
            path.add(curr);
        }
        Collections.reverse(path);
        return path;
    }
}
