package com.labs;

import com.labs.solvers.LDFSSolver;
import com.labs.utils.SearchResult;

import java.util.Scanner;

import static com.labs.utils.GameUtils.enterDepth;

public class LDFSRunner {
    public static void main(String[] args) {
        int depth = enterDepth();
        LDFSSolver ldfsSolver = new LDFSSolver(depth);
        SearchResult result = ldfsSolver.depthLimitedSearch();
        ldfsSolver.printReport(result);
    }
}
