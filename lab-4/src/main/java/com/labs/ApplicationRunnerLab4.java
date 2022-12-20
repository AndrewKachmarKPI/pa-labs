package com.labs;

import com.labs.domain.PopulationNode;
import com.labs.services.KnapsackProblemService;

import static com.labs.services.KnapsackProblemService.getCapacity;

public class ApplicationRunnerLab4 {
    public static void main(String[] args) {
        int capacity = getCapacity();
        int iterations = 1000;
        KnapsackProblemService service = new KnapsackProblemService(capacity);
        System.out.println("Genetic algorithm");
        System.out.println("Input params -> iterations ->" + iterations + " capacity ->" + capacity);
        PopulationNode resultPopulationNode = service.getPackedKnapsack(iterations);
        System.out.println("Knapsack packed ->" + resultPopulationNode.toString());
    }
}
