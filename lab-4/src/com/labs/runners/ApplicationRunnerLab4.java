package com.labs.runners;

import com.labs.domain.PopulationNode;
import com.labs.services.KnapsackProblemService;

import java.util.Scanner;

import static com.labs.services.KnapsackProblemService.enterCapacity;

public class ApplicationRunnerLab4 {
    public static void main(String[] args) {
        int capacity = enterCapacity();
        KnapsackProblemService service = new KnapsackProblemService(capacity);
        PopulationNode solution = service.searchLoop(1000);
        System.out.println("Solution found");
        System.out.println(solution.toString());
    }
}
