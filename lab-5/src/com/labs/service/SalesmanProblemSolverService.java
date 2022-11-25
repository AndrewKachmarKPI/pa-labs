package com.labs.service;

import com.labs.domain.Ant;

import java.util.ArrayList;
import java.util.List;

public class SalesmanProblemSolverService {
    private int a;
    private int b;
    private int r;
    private int lMin;

    private List<Ant> ants = new ArrayList<>();
    private int[][] distanceMatrix = new int[300][300];

    public SalesmanProblemSolverService(int a, int b, int r, int lMin) {
        this.a = a;
        this.b = b;
        this.r = r;
        this.lMin = lMin;

        generateDistanceMatrix();
        printMatrix(distanceMatrix);
    }

    public SalesmanProblemSolverService(int numberOfAnts) {
        generateDistanceMatrix();
        generateAnts(numberOfAnts);
        printMatrix(distanceMatrix);
    }


    private void generateAnts(int numberOfAnts) {
        for (int i = 0; i < numberOfAnts; i++) {
            ants.add(new Ant(distanceMatrix));
        }
    }

    private void generateDistanceMatrix() {
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                int distance = (i == j) ? 0 : randomNumber(5, 150);
                distanceMatrix[i][j] = distance;
            }
        }
    }

    private void printMatrix(int[][] matrix) {
        for (int[] ints : matrix) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }

    public int randomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public double randomDouble(int min, int max) {
        return ((Math.random() * (max - min)) + min);
    }
}
