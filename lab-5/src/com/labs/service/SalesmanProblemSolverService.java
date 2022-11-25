package com.labs.service;

public class SalesmanProblemSolverService {
    private final int a;
    private final int b;
    private final int r;
    private final int lMin;

    private int[][] distanceMatrix = new int[300][300];
    private int[][] visionMatrix;
    private int[][] pheromoneMatrix;

    public SalesmanProblemSolverService(int a, int b, int r, int lMin) {
        this.a = a;
        this.b = b;
        this.r = r;
        this.lMin = lMin;

        buildVisionMatrix();
        buildPheromoneMatrix();
    }

    private void generateDistanceMatrix() {
//        for (int i = 0; i < distanceMatrix.length; i++) {
//            for (int j = 0; j < distanceMatrix[i].length; j++) {
//                distanceMatrix[i][j] = ge
//            }
//        }
    }

    private void buildVisionMatrix() {

    }

    private void buildPheromoneMatrix() {

    }
}
