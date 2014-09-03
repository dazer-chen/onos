package org.onlab.graph;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.onlab.graph.DepthFirstSearch.EdgeType;

/**
 * Test of the DFS algorithm.
 */
public class DepthFirstSearchTest extends AbstractGraphPathSearchTest {

    @Override
    protected DepthFirstSearch<TestVertex, TestEdge> graphSearch() {
        return new DepthFirstSearch<>();
    }

    @Test
    public void defaultGraphTest() {
        executeDefaultTest(3, 6, 5.0, 12.0);
        executeBroadSearch();
    }

    @Test
    public void defaultHopCountWeight() {
        weight = null;
        executeDefaultTest(3, 6, 3.0, 6.0);
        executeBroadSearch();
    }

    protected void executeDefaultTest(int minLength, int maxLength,
                                      double minCost, double maxCost) {
        g = new AdjacencyListsGraph<>(vertices(), edges());
        DepthFirstSearch<TestVertex, TestEdge> search = graphSearch();

        DepthFirstSearch<TestVertex, TestEdge>.SpanningTreeResult result =
                search.search(g, A, H, weight);
        Set<Path<TestVertex, TestEdge>> paths = result.paths();
        assertEquals("incorrect path count", 1, paths.size());

        Path path = paths.iterator().next();
        System.out.println(path);
        assertEquals("incorrect src", A, path.src());
        assertEquals("incorrect dst", H, path.dst());

        int l = path.edges().size();
        assertTrue("incorrect path length " + l,
                   minLength <= l && l <= maxLength);
        assertTrue("incorrect path cost " + path.cost(),
                   minCost <= path.cost() && path.cost() <= maxCost);

        System.out.println(result.edges());
        printPaths(paths);
    }

    public void executeBroadSearch() {
        g = new AdjacencyListsGraph<>(vertices(), edges());
        DepthFirstSearch<TestVertex, TestEdge> search = graphSearch();

        // Perform narrow path search to a specific destination.
        DepthFirstSearch<TestVertex, TestEdge>.SpanningTreeResult result =
                search.search(g, A, null, weight);
        assertEquals("incorrect paths count", 7, result.paths().size());

        int[] types = new int[]{0, 0, 0, 0};
        for (EdgeType t : result.edges().values()) {
            types[t.ordinal()] += 1;
        }
        assertEquals("incorrect tree-edge count", 7,
                     types[EdgeType.TREE_EDGE.ordinal()]);
        assertEquals("incorrect back-edge count", 1,
                     types[EdgeType.BACK_EDGE.ordinal()]);
        assertEquals("incorrect cross-edge & forward-edge count", 4,
                     types[EdgeType.FORWARD_EDGE.ordinal()] +
                             types[EdgeType.CROSS_EDGE.ordinal()]);
    }

}