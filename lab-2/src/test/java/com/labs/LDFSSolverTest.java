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

import static com.labs.utils.SolverTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LDFSSolverTest {
    @Test
    public void Check_FindResult_Succeeds_When_CorrectPlacement() {
        //Arrange
        int depth = 4;
        List<QueenPosition> placement = getCorrectPlacement();
        LDFSSolver ldfsSolver = new LDFSSolver(depth, placement);
        //Action
        SearchResult searchResult = ldfsSolver.depthLimitedSearch();
        //Assert
        assertThat(searchResult.isSuccess()).isTrue();
        assertThat(searchResult.getSolution().getDepth()).isLessThan(depth);
        assertThat(searchResult.getSolution().getPositions()).hasSize(placement.size());
    }

    @Test
    public void Check_FindResult_Fails_When_SmallDepth() {
        //Arrange
        int depth = 1;
        List<QueenPosition> placement = getRandomPlacement();
        LDFSSolver ldfsSolver = new LDFSSolver(depth, placement);
        //Action
        SearchResult searchResult = ldfsSolver.depthLimitedSearch();
        //Assert
        assertThat(searchResult.isSuccess()).isFalse();
        assertThat(searchResult.getMessage()).isEqualTo("cutoff");
    }

    @Test
    public void Check_FindResult_Fails_When_EmptyPlacement() {
        //Arrange
        int depth = 4;
        List<QueenPosition> placement = new ArrayList<>();
        LDFSSolver ldfsSolver = new LDFSSolver(depth, placement);
        //Action
        SearchResult searchResult = ldfsSolver.depthLimitedSearch();
        //Assert
        assertThat(searchResult.isSuccess()).isFalse();
        assertThat(searchResult.getMessage()).isEqualTo("failure");
    }


    @Test
    public void Check_EnterSearchDepth_Succeeds_When_PositiveNumber() {
        //Arrange
        String expectedDepth = "10";
        String userInput = String.format(expectedDepth, System.lineSeparator(), System.lineSeparator());
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(bais);
        //Action
        int actualDepth = GameUtils.getEnterDepth();
        //Assert
        assertThat(actualDepth).isPositive();
        assertThat(String.valueOf(actualDepth)).isEqualTo(expectedDepth);
    }

    @Test
    public void Check_EnterSearchDepth_Fails_When_IncorrectDepthNumber() {
        //Arrange
        String expectedDepth = "-10";
        String userInput = String.format(expectedDepth, System.lineSeparator(), System.lineSeparator());
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(bais);
        //Action / Assert
        assertThatThrownBy(GameUtils::getEnterDepth).isInstanceOf(NoSuchElementException.class);
    }
}
