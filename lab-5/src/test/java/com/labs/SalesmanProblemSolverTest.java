package com.labs;

import com.labs.domain.AntAlgorithmParams;
import com.labs.domain.PathSearchResult;
import com.labs.enums.AntPlacementType;
import com.labs.service.SalesmanProblemSolverService;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SalesmanProblemSolverTest {
    @Test
    void Check_FindBestPath_Succeeds() {
        //Arrange
        int numberOfCities = 100;
        AntAlgorithmParams antAlgorithmParams = AntAlgorithmParams.builder()
                .A(1)
                .B(3)
                .R(0.3)
                .L_MIN(2200)
                .numberOfOrdinaryAnts(200)
                .numberOfEliteAnts(20)
                .numberOfWildAnts(40)
                .colonyLife(1)
                .antPlacementType(AntPlacementType.MANY_WITH_REPEAT).build();
        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(antAlgorithmParams, numberOfCities);
        //Act
        PathSearchResult pathSearchResult = solverService.findBestPath();
        //Assert
        String firstCity = pathSearchResult.getPath().toString().split("-")[0];

        assertThat(pathSearchResult.getPath().toString()).startsWith(firstCity).endsWith(firstCity);
        assertThat(pathSearchResult.getPathCost()).isNotNull();
        assertThat(pathSearchResult.getPathCost()).isNotZero();
        assertThat(pathSearchResult.getPathCost()).isPositive();
        assertThat(pathSearchResult.getPathCost()).isLessThan(Integer.MAX_VALUE);
    }

    @Test
    void Check_FindBestPath_Succeeds_When_UnOptimalInputParams() {
        //Arrange
        int numberOfCities = 100;
        AntAlgorithmParams antAlgorithmParams = AntAlgorithmParams.builder()
                .A(0)
                .B(0)
                .R(0)
                .L_MIN(0)
                .numberOfOrdinaryAnts(100)
                .numberOfEliteAnts(0)
                .numberOfWildAnts(0)
                .colonyLife(0)
                .antPlacementType(AntPlacementType.MANY_WITH_REPEAT).build();
        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(antAlgorithmParams, numberOfCities);
        //Act
        PathSearchResult pathSearchResult = solverService.findBestPath();
        //Assert
        String firstCity = pathSearchResult.getPath().toString().split("-")[0];
        assertThat(pathSearchResult.getPath().toString()).startsWith(firstCity).endsWith(firstCity);
        assertThat(pathSearchResult.getPathCost()).isNotNull();
        assertThat(pathSearchResult.getPathCost()).isNotZero();
        assertThat(pathSearchResult.getPathCost()).isPositive();
        assertThat(pathSearchResult.getPathCost()).isGreaterThan(2000);
    }

    @Test
    void Check_FindBestPath_Fails_When_InvalidPlacement() {
        //Arrange
        int numberOfCities = 5;
        AntAlgorithmParams antAlgorithmParams = AntAlgorithmParams.builder()
                .numberOfOrdinaryAnts(10)
                .colonyLife(1)
                .antPlacementType(AntPlacementType.MANY_WITHOUT_REPEAT).build();
        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(antAlgorithmParams, numberOfCities);
        //Act
        Exception exception = assertThrows(RuntimeException.class, solverService::findBestPath);
        //Assert
        assertThat(exception.getMessage()).isEqualTo("Ant size should be greater that 5");
    }

    @Test
    void Check_FindBestPath_Fails_When_OneCity() {
        //Arrange
        int numberOfCities = 1;
        AntAlgorithmParams antAlgorithmParams = AntAlgorithmParams.builder()
                .A(1)
                .B(1)
                .R(1)
                .L_MIN(1)
                .numberOfOrdinaryAnts(1)
                .numberOfEliteAnts(1)
                .numberOfWildAnts(1)
                .colonyLife(1)
                .antPlacementType(AntPlacementType.MANY_WITH_REPEAT).build();
        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(antAlgorithmParams, numberOfCities);
        //Act
        PathSearchResult pathSearchResult = solverService.findBestPath();
        //Assert
        assertThat(pathSearchResult.getPath().toString()).isEmpty();
        assertThat(pathSearchResult.getPathCost()).isNotNull();
        assertThat(pathSearchResult.getPathCost()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    void findBestPathFailsWhenPathsNotFound() {
        //Arrange
        int numberOfCities = 1;
        AntAlgorithmParams antAlgorithmParams = AntAlgorithmParams.builder()
                .numberOfOrdinaryAnts(0)
                .colonyLife(1)
                .antPlacementType(AntPlacementType.MANY_WITHOUT_REPEAT).build();
        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(antAlgorithmParams, numberOfCities);
        //Act
        Exception exception = assertThrows(RuntimeException.class, solverService::findBestPath);
        //Assert
        assertThat(exception.getMessage()).isEqualTo("Path not found");
    }
}
