package com.labs;

import com.labs.solvers.LDFSSolver;
import com.labs.utils.SearchPositionMetrics;
import com.labs.utils.SearchResult;

import static com.labs.utils.GameUtils.enterDepth;
import static com.labs.utils.GameUtils.printSearchResultPositionAndMetrics;

public class LDFSRunner {
    public static void main(String[] args) {
        int depth = enterDepth();
        LDFSSolver ldfsSolver = new LDFSSolver(depth);
        SearchResult searchResult = ldfsSolver.depthLimitedSearch();
        SearchPositionMetrics searchPositionMetrics = ldfsSolver.getSearchPositionMetrics();
        printSearchResultPositionAndMetrics(searchResult, searchPositionMetrics);
    }
}
