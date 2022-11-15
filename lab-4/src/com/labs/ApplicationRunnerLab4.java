package com.labs;

import com.labs.domain.PopulationNode;
import com.labs.services.KnapsackProblemService;

public class ApplicationRunnerLab4 {
    public static void main(String[] args) {
        KnapsackProblemService service = new KnapsackProblemService(150);
        PopulationNode populationNode = service.searchLoop(100000);
        System.out.println(populationNode);
    }
}
