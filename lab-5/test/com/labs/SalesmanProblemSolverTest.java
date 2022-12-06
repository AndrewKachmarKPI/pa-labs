package com.labs;

import com.labs.domain.PathSearchResult;
import com.labs.domain.SalesmanProblemDto;
import com.labs.enums.AntPlacementType;
import com.labs.service.SalesmanProblemSolverService;
import org.junit.Test;

public class SalesmanProblemSolverTest {

    @Test
    public void findSolutionPositiveTest() {
        SalesmanProblemDto salesmanProblemDto = SalesmanProblemDto.builder()
                .setA(16)
                .setB(24)
                .setR(0.8)
                .setL_MIN(64)
                .setNumberOfAnts(10)
                .setNumberOfEliteAnts(5)
                .setColonyLife(10)
                .setAntPlacementType(AntPlacementType.MANY_WITHOUT_REPEAT).build();
        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(salesmanProblemDto);
        PathSearchResult pathSearchResult = solverService.findSolution();
    }
}
