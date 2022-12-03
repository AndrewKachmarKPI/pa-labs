package com.labs;

import com.labs.enums.AntPlacementType;
import com.labs.domain.PathSearchResult;
import com.labs.domain.SalesmanProblemDto;
import com.labs.service.SalesmanProblemSolverService;

/**
 * Задача комівояжера (симетрична мережа) + Мурашиний алгоритм
 */
public class ApplicationRunnerLab5 {

    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter alpha:");
//        int a = scanner.nextInt();
//        System.out.print("Enter betta:");
//        int b = scanner.nextInt();
//        System.out.print("Enter r:");
//        int r = scanner.nextInt();
//        System.out.print("Enter Lmin:");
//        int lMin = scanner.nextInt();

        SalesmanProblemDto salesmanProblemDto = SalesmanProblemDto.builder()
                .setA(2)
                .setB(3)
                .setR(0.2)
                .setL_MIN(34)
                .setNumberOfAnts(5)
                .setNumberOfEliteAnts(2)
                .setAntPlacementType(AntPlacementType.MANY_WITH_REPEAT).build();
        System.out.println(salesmanProblemDto);
        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(salesmanProblemDto);

        PathSearchResult pathSearchResult = solverService.findSolution();
        System.out.println(pathSearchResult);
    }
}
