package com.labs;

import com.labs.domain.PathSearchResult;
import com.labs.domain.SalesmanProblemDto;
import com.labs.enums.AntPlacementType;
import com.labs.service.SalesmanProblemSolverService;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SalesmanProblemSolverTest {

    @Test
    public void findSolutionPositiveTest() {
        SalesmanProblemDto salesmanProblemDto = SalesmanProblemDto.builder()
                .setA(1)
                .setB(3)
                .setR(0.3)
                .setL_MIN(2200)
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
        assertThat(pathSearchResult.getPathCost()).isLessThan(Integer.MAX_VALUE);
    }

    @Test
    public void findSolutionPositiveTestUnOptimalSolution() {
        SalesmanProblemDto salesmanProblemDto = SalesmanProblemDto.builder()
                .setA(0)
                .setB(0)
                .setR(0)
                .setL_MIN(0)
                .setNumberOfOrdinaryAnts(100)
                .setNumberOfEliteAnts(0)
                .setNumberOfWildAnts(0)
                .setColonyLife(1)
                .setAntPlacementType(AntPlacementType.MANY_WITH_REPEAT).build();
        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(salesmanProblemDto, 100);
        PathSearchResult pathSearchResult = solverService.findSolution();

        String firstCity = pathSearchResult.getPath().split("-")[0];
        assertThat(pathSearchResult.getPath()).startsWith(firstCity).endsWith(firstCity);
        assertThat(pathSearchResult.getPathCost()).isNotNull();
        assertThat(pathSearchResult.getPathCost()).isNotZero();
        assertThat(pathSearchResult.getPathCost()).isPositive();
        assertThat(pathSearchResult.getPathCost()).isGreaterThan(2000);
    }


    @Test
    public void findSolutionNegativeTestInvalidPlacement() {
        SalesmanProblemDto salesmanProblemDto = SalesmanProblemDto.builder()
                .setNumberOfOrdinaryAnts(10)
                .setColonyLife(1)
                .setAntPlacementType(AntPlacementType.MANY_WITHOUT_REPEAT).build();
        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(salesmanProblemDto, 5);
        assertThatThrownBy(solverService::findSolution)
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ant size should be greater that 5");
    }

    @Test
    public void findSolutionNegativeTestOneCity() {
        SalesmanProblemDto salesmanProblemDto = SalesmanProblemDto.builder()
                .setA(1)
                .setB(1)
                .setR(1)
                .setL_MIN(1)
                .setNumberOfOrdinaryAnts(1)
                .setNumberOfEliteAnts(1)
                .setNumberOfWildAnts(1)
                .setColonyLife(1)
                .setAntPlacementType(AntPlacementType.MANY_WITH_REPEAT).build();
        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(salesmanProblemDto, 1);
        PathSearchResult pathSearchResult = solverService.findSolution();
        System.out.println(pathSearchResult);
        assertThat(pathSearchResult.getPath()).isEmpty();
        assertThat(pathSearchResult.getPathCost()).isNotNull();
        assertThat(pathSearchResult.getPathCost()).isEqualTo(Integer.MAX_VALUE);
    }


    @Test
    public void findSolutionNegativeTestPathsNotFound() {
        SalesmanProblemDto salesmanProblemDto = SalesmanProblemDto.builder()
                .setNumberOfOrdinaryAnts(0)
                .setColonyLife(1)
                .setAntPlacementType(AntPlacementType.MANY_WITH_REPEAT).build();
        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(salesmanProblemDto, 1);
        assertThatThrownBy(solverService::findSolution)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Path not found");
    }
}
