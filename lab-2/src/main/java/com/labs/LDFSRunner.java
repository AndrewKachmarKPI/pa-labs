package com.labs;

import com.labs.solvers.LDFSSolver;
import com.labs.utils.SearchResult;

public class LDFSRunner {
    public static void main(String[] args) {
        LDFSSolver ldfsSolver = new LDFSSolver(8);
        SearchResult result = ldfsSolver.depthLimitedSearch();
        ldfsSolver.printReport(result);
    }
}
