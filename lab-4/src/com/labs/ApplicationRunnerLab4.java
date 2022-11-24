package com.labs;

import com.labs.domain.PopulationNode;
import com.labs.services.KnapsackProblemService;

import java.util.Scanner;

public class ApplicationRunnerLab4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter backpack capacity:");
        int capacity = scanner.nextInt();
        KnapsackProblemService service = new KnapsackProblemService(capacity);
        PopulationNode solution = service.searchLoop(1000);
        System.out.println("Solution found");
        System.out.println(solution.toString());
    }
}
