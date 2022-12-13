package com.labs;

import com.labs.solvers.AStarSolver;
import com.labs.utils.SearchPositionMetrics;
import com.labs.utils.SearchResult;
import static com.labs.utils.GameUtils.printSearchResultPositionAndMetrics;

public class AStarRunner {
    public static void main(String[] args) {
        AStarSolver solver = new AStarSolver();
        SearchResult searchResult = solver.aStarSearch();
        SearchPositionMetrics searchPositionMetrics = solver.getSearchPositionMetrics();
        printSearchResultPositionAndMetrics(searchResult, searchPositionMetrics);
    }
}
