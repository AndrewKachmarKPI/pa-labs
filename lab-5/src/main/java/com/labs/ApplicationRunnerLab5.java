package com.labs;

import com.labs.enums.AntPlacementType;
import com.labs.domain.PathSearchResult;
import com.labs.domain.AntAlgorithmParams;
import com.labs.service.SalesmanProblemSolverService;

/**
 * Задача комівояжера (симетрична мережа) + Мурашиний алгоритм
 */
public class ApplicationRunnerLab5 {
    public static void main(String[] args) {
        AntAlgorithmParams antAlgorithmParams = AntAlgorithmParams.builder()
                .alpha(1.0)
                .betta(6.0)
                .evaporation(0.5)
                .orderOfPrice(2200)
                .numberOfOrdinaryAnts(200)
                .numberOfEliteAnts(20)
                .numberOfWildAnts(40)
                .colonyLife(1)
                .antPlacementType(AntPlacementType.MANY_WITH_REPEAT).build();
        int numberOfCities = 300;
        System.out.println("Input params ->" + antAlgorithmParams + " Cities ->" + numberOfCities);
        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(antAlgorithmParams);
        PathSearchResult bestPathSearchResult = solverService.getBestPath(numberOfCities);
        System.out.println("Best path ->" + bestPathSearchResult);
    }
}
