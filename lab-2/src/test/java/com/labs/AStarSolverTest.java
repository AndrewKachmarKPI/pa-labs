package com.labs;

import com.labs.solvers.AStarSolver;
import com.labs.utils.QueenPosition;
import com.labs.utils.SearchResult;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.labs.utils.SolverTestUtils.getCorrectPlacement;
import static org.assertj.core.api.Assertions.*;

public class AStarSolverTest {
    @Test
    public void findResultSucceedsWhenRandomInitialPlacement() {
//      arrange
        AStarSolver aStarSolver = new AStarSolver();
//      act
        SearchResult searchResult = aStarSolver.aStarSearch();
//      assert
        assertThat(searchResult.isSuccess()).isTrue();
        assertThat(searchResult.getSolution().getPositions()).hasSize(8);
    }

    @Test
    public void findResultSucceedsWhenCorrectPlacement() {
//      arrange
        List<QueenPosition> correctPlacement = getCorrectPlacement();
        AStarSolver aStarSolver = new AStarSolver(correctPlacement);
//      act
        SearchResult searchResult = aStarSolver.aStarSearch();
//      assert
        assertThat(searchResult.isSuccess()).isTrue();
        assertThat(searchResult.getSolution().getPositions()).hasSize(8);
    }


    @Test
    public void findResultFailsWhenEmptyPlacement() {
//      arrange
        List<QueenPosition> placement = new ArrayList<>();
        AStarSolver aStarSolver = new AStarSolver(placement);
//      act
        SearchResult searchResult = aStarSolver.aStarSearch();
//      assert
        assertThat(searchResult.isSuccess()).isFalse();
        assertThat(searchResult.getMessage()).isEqualTo("failure");
    }
}
