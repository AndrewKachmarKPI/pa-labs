package com.labs;

import com.labs.service.SalesmanProblemSolverService;

import java.util.Scanner;

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
        SalesmanProblemSolverService solverService = new SalesmanProblemSolverService(2, 3, 0.2, 34, 1);
    }
}
