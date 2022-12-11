package com.labs;

import com.labs.solvers.LDFSSolver;
import com.labs.utils.GameUtils;
import com.labs.utils.QueenPosition;
import com.labs.utils.SearchResult;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
    public void ldfsSolverNegativeTestSolutionCutoff() {
        LDFSSolver ldfsSolver = new LDFSSolver(0);
        SearchResult searchResult = ldfsSolver.depthLimitedSearch();
        ldfsSolver.printReport(searchResult);
        assertThat(searchResult.isSuccess()).isFalse();
        assertThat(searchResult.getMessage()).isEqualTo("cutoff");
    }

    @Test
    public void ldfsSolverNegativeTestSolutionFailure() {
        LDFSSolver ldfsSolver = new LDFSSolver(4, new ArrayList<>());
        SearchResult searchResult = ldfsSolver.depthLimitedSearch();
        ldfsSolver.printReport(searchResult);
        assertThat(searchResult.isSuccess()).isFalse();
        assertThat(searchResult.getMessage()).isEqualTo("failure");
    }


    @Test
    public void enterLdfsDepthPositiveTest() {
        String expectedDepth = "10";
        String userInput = String.format(expectedDepth, System.lineSeparator(), System.lineSeparator());
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(bais);

        int actualDepth = GameUtils.enterDepth();

        assertThat(actualDepth).isPositive();
        assertThat(String.valueOf(actualDepth)).isEqualTo(expectedDepth);
    }

    @Test(expected = NoSuchElementException.class)
    public void enterLdfsDepthNegativeTest() {
        String expectedDepth = "-10";
        String userInput = String.format(expectedDepth, System.lineSeparator(), System.lineSeparator());
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(bais);

        int actualDepth = GameUtils.enterDepth();

        assertThat(actualDepth).isZero();
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
