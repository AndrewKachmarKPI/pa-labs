package com.labs;

import com.labs.domain.PathSearchResult;
import com.labs.domain.SalesmanProblemDto;
import com.labs.enums.AntPlacementType;
import com.labs.service.SalesmanProblemSolverService;

import static com.labs.service.GeneralMethodsService.*;

/**
 * Задача комівояжера (симетрична мережа) + Мурашиний алгоритм
 */
public class ApplicationRunnerManualLab5 {

    public static void main(String[] args) {
        int numberOfCities = enterParam(2, "number of cities");
        double a = enterParamDouble(0.1, "a");
        double b = enterParamDouble(0.1, "b");
        double r = enterParamDouble(0.1, "r");
        int l_min = enterParam(1, "l_min");
        int ordinaryAnts = enterParam(1, "ordinary ants");
        int eliteAnts = enterParam(1, "elite ants");
        int wildAnts = enterParam(1, "wild ants");
        int colonyLife = enterParam(1, "colony life");
        AntPlacementType placementType = selectAntPlacement();

        SalesmanProblemDto salesmanProblemDto = SalesmanProblemDto.builder()
                .setA(a)
                .setB(b)
                .setR(r)
                .setL_MIN(l_min)
                .setNumberOfOrdinaryAnts(ordinaryAnts)
                .setNumberOfEliteAnts(eliteAnts)
                .setNumberOfWildAnts(wildAnts)
                .setColonyLife(colonyLife)
                .setAntPlacementType(placementType).build();
        System.out.println("Input PARAMS ->" + salesmanProblemDto);


        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(salesmanProblemDto, numberOfCities);
        PathSearchResult pathSearchResult = solverService.findSolution();

        System.out.println("BEST path cost -> " + pathSearchResult.getPathCost());
        System.out.println(pathSearchResult);
    }
}
