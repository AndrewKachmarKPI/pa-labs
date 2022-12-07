package com.labs;

import com.labs.domain.PathSearchResult;
import com.labs.domain.SalesmanProblemDto;
import com.labs.enums.AntPlacementType;
import com.labs.service.SalesmanProblemSolverService;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SalesmanProblemSolverTest {

    @Test
    public void findSolutionPositiveTest() {
        SalesmanProblemDto salesmanProblemDto = SalesmanProblemDto.builder()
                .setA(1)
                .setB(3)
                .setR(0.4)
                .setL_MIN(2500)
                .setNumberOfOrdinaryAnts(200)
                .setNumberOfEliteAnts(20)
                .setNumberOfWildAnts(40)
                .setColonyLife(1)
                .setAntPlacementType(AntPlacementType.MANY_WITH_REPEAT).build();
        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(salesmanProblemDto, 100);
        PathSearchResult pathSearchResult = solverService.findSolution();

        String firstCity = pathSearchResult.getPath().split("-")[0];
        assertThat(pathSearchResult.getPath()).startsWith(firstCity).endsWith(firstCity);
        assertThat(pathSearchResult.getPathCost()).isNotNull();
        assertThat(pathSearchResult.getPathCost()).isNotZero();
        assertThat(pathSearchResult.getPathCost()).isPositive();
    }


    @Test(expected = RuntimeException.class)
    public void findSolutionNegativeTestInvalidAntsAmount() {
        SalesmanProblemDto salesmanProblemDto = SalesmanProblemDto.builder()
                .setA(1)
                .setB(3)
                .setR(0.4)
                .setL_MIN(2500)
                .setNumberOfOrdinaryAnts(100)
                .setNumberOfEliteAnts(20)
                .setNumberOfWildAnts(40)
                .setColonyLife(1)
                .setAntPlacementType(AntPlacementType.MANY_WITHOUT_REPEAT).build();
        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(salesmanProblemDto, 100);
        solverService.findSolution();
    }


    @Test(expected = RuntimeException.class)
    public void findSolutionNegativeTestInvalidEliteAnts() {
        SalesmanProblemDto salesmanProblemDto = SalesmanProblemDto.builder()
                .setNumberOfOrdinaryAnts(20)
                .setNumberOfEliteAnts(40)
                .setColonyLife(1)
                .setAntPlacementType(AntPlacementType.MANY_WITHOUT_REPEAT).build();
        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(salesmanProblemDto, 100);
        solverService.findSolution();
    }
}
