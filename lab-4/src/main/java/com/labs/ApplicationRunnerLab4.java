package com.labs;

import com.labs.domain.PopulationNode;
import com.labs.services.KnapsackProblemService;

import static com.labs.services.KnapsackProblemService.getCapacity;

public class ApplicationRunnerLab4 {
    public static void main(String[] args) {
        int capacity = getCapacity();
        KnapsackProblemService service = new KnapsackProblemService(capacity);
        PopulationNode knapsack = service.getPackedKnapsack(1000);
        System.out.println("Solution found ->" + knapsack.toString());
    }
}
