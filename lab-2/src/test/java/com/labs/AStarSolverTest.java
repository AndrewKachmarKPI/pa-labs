package com.labs;

import com.labs.solvers.AStarSolver;
import com.labs.utils.QueenPosition;
import com.labs.utils.SearchResult;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class AStarSolverTest {

    @Test
    public void aStarSolverPositiveTest() {
        AStarSolver aStarSolver = new AStarSolver();
        SearchResult searchResult = aStarSolver.aStarSearch();
        aStarSolver.printReport(searchResult);
        assertThat(searchResult.isSuccess()).isTrue();
        assertThat(searchResult.getSolution().getPositions()).hasSize(8);
    }

    @Test
    public void aStarSolverPositiveTestDefaultSolution() {
        AStarSolver aStarSolver = new AStarSolver(solution());
        SearchResult searchResult = aStarSolver.aStarSearch();
        aStarSolver.printReport(searchResult);
        assertThat(searchResult.isSuccess()).isTrue();
        assertThat(searchResult.getSolution().getPositions()).hasSize(8);
    }

    @Test
    public void aStarSolverNegativeTestFailure() {
        AStarSolver aStarSolver = new AStarSolver(new ArrayList<>());
        SearchResult searchResult = aStarSolver.aStarSearch();
        assertThat(searchResult.isSuccess()).isFalse();
        assertThat(searchResult.getMessage()).isEqualTo("failure");
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
