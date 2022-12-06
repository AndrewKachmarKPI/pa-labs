package com.labs.runners;

import com.labs.enums.AntPlacementType;
import com.labs.domain.PathSearchResult;
import com.labs.domain.SalesmanProblemDto;
import com.labs.service.SalesmanProblemSolverService;

/**
 * Задача комівояжера (симетрична мережа) + Мурашиний алгоритм
 */
public class ApplicationRunnerLab5 {

    public static void main(String[] args) {
        SalesmanProblemDto salesmanProblemDto = SalesmanProblemDto.builder()
                .setA(16)
                .setB(24)
                .setR(0.8)
                .setL_MIN(64)
                .setNumberOfAnts(10)
                .setNumberOfEliteAnts(5)
                .setColonyLife(10)
                .setAntPlacementType(AntPlacementType.MANY_WITHOUT_REPEAT).build();
        System.out.println(salesmanProblemDto);
        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(salesmanProblemDto);
        PathSearchResult pathSearchResult = solverService.findSolution();

        System.out.println("Final solution");
        System.out.println(pathSearchResult);
    }
}
