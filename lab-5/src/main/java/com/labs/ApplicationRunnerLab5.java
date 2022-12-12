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
                .A(1)
                .B(6)
                .R(0.5)
                .L_MIN(2200)
                .numberOfOrdinaryAnts(200)
                .numberOfEliteAnts(20)
                .numberOfWildAnts(40)
                .colonyLife(1)
                .antPlacementType(AntPlacementType.MANY_WITH_REPEAT).build();
        System.out.println("INPUT PARAMS ->" + antAlgorithmParams);

        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(antAlgorithmParams, 100);
        PathSearchResult pathSearchResult = solverService.findBestPath();

        System.out.println("RESULT -> " + pathSearchResult.getPathCost());
        System.out.println(pathSearchResult);
    }
}
