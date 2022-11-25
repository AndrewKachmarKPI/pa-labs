package com.labs.domain;

import java.util.List;
import java.util.UUID;

public class Ant {
    private List<String> visitedCities;
    private int[][] visionMatrix = new int[300][300];
    private int[][] pheromoneMatrix = new int[300][300];
    private String antId;


    public Ant(int[][] distanceMatrix) {
        this.antId = UUID.randomUUID().toString();
        buildVisionMatrix(distanceMatrix);
    }

    private void buildVisionMatrix(int[][] distanceMatrix) {
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix.length; j++) {
                visionMatrix[i][j] = 1 / distanceMatrix[i][j];
            }
        }
    }

    private void buildPheromoneMatrix() {

    }

}
