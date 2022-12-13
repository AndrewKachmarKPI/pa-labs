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
    public void Check_FindResult_Succeeds_When_RandomInitialPlacement() {
        //Arrange
        AStarSolver aStarSolver = new AStarSolver();
        //Action
        SearchResult searchResult = aStarSolver.aStarSearch();
        //Assert
        assertThat(searchResult.isSuccess()).isTrue();
        assertThat(searchResult.getSolution().getPositions()).hasSize(8);
    }

    @Test
    public void Check_FindResult_Succeeds_When_CorrectPlacement() {
        //Arrange
        List<QueenPosition> correctPlacement = getCorrectPlacement();
        AStarSolver aStarSolver = new AStarSolver(correctPlacement);
        //Action
        SearchResult searchResult = aStarSolver.aStarSearch();
        //Assert
        assertThat(searchResult.isSuccess()).isTrue();
        assertThat(searchResult.getSolution().getPositions()).hasSize(8);
    }


    @Test
    public void Check_FindResult_Fails_When_EmptyPlacement() {
        //Arrange
        List<QueenPosition> placement = new ArrayList<>();
        AStarSolver aStarSolver = new AStarSolver(placement);
        //Action
        SearchResult searchResult = aStarSolver.aStarSearch();
        //Assert
        assertThat(searchResult.isSuccess()).isFalse();
        assertThat(searchResult.getMessage()).isEqualTo("failure");
    }
}
