package com.labs.runners;

import com.labs.solvers.AStarSolver;
import com.labs.utils.SearchResult;

public class AStarRunner {
    public static void main(String[] args) {
        AStarSolver solver = new AStarSolver();
        SearchResult searchResult = solver.aStarSearch();
        solver.printReport(searchResult);
    }
}
