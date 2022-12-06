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
                .setA(1)
                .setB(3)
                .setR(0.4)
                .setL_MIN(2500)
                .setNumberOfOrdinaryAnts(200)
                .setNumberOfEliteAnts(20)
                .setNumberOfWildAnts(40)
                .setColonyLife(10)
                .setAntPlacementType(AntPlacementType.MANY_WITH_REPEAT).build();
        System.out.println(salesmanProblemDto);
        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(salesmanProblemDto, 300);
        PathSearchResult pathSearchResult = solverService.findSolution();

        System.out.println("Final solution");
        System.out.println(pathSearchResult);
    }
}
