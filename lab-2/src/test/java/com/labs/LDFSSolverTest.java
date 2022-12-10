package com.labs;

import com.labs.solvers.LDFSSolver;
import com.labs.utils.QueenPosition;
import com.labs.utils.SearchResult;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LDFSSolverTest {

    @Test
    public void ldfsSolverPositiveTest() {
        LDFSSolver ldfsSolver = new LDFSSolver(4, solution());
        SearchResult searchResult = ldfsSolver.depthLimitedSearch();
        ldfsSolver.printReport(searchResult);
        assertThat(searchResult.isSuccess()).isTrue();
    }

    @Test
    public void ldfsSolverNegativeTest() {
        LDFSSolver ldfsSolver = new LDFSSolver(0);
        SearchResult searchResult = ldfsSolver.depthLimitedSearch();
        ldfsSolver.printReport(searchResult);
        assertThat(searchResult.isSuccess()).isFalse();
        assertThat(searchResult.getMessage()).isEqualTo("cutoff");
    }

    private List<QueenPosition> solution() {
        List<QueenPosition> queenPositions = new ArrayList<>();
        queenPositions.add(new QueenPosition(2, 0));
        queenPositions.add(new QueenPosition(5, 1));
        queenPositions.add(new QueenPosition(7, 2));
        queenPositions.add(new QueenPosition(0, 3));
        queenPositions.add(new QueenPosition(3, 4));
        queenPositions.add(new QueenPosition(6, 5));
        queenPositions.add(new QueenPosition(4, 6));
        queenPositions.add(new QueenPosition(1, 7));
        return queenPositions;
    }
}
