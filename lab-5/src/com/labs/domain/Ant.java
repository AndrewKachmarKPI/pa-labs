package com.labs.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Ant {
    private final String antId;
    private final List<Integer> visitedCities = new ArrayList<>();
    private final int currentLocation;
    private final double[][] visionMatrix = new double[300][300];


    public Ant(int[][] distanceMatrix, int currentLocation) {
        this.antId = UUID.randomUUID().toString();
        this.currentLocation = currentLocation;
        this.visitedCities.add(currentLocation);
        buildVisionMatrix(distanceMatrix);
    }

    private void buildVisionMatrix(int[][] distanceMatrix) {
        for (int i = 0; i < visionMatrix.length; i++) {
            for (int j = 0; j < visionMatrix.length; j++) {
                if (distanceMatrix[i][j] != 0) {
                    visionMatrix[i][j] = (double) 1 / distanceMatrix[i][j];
                } else {
                    visionMatrix[i][j] = 0;
                }
            }
        }
    }

    public String getAntId() {
        return antId;
    }

    public List<Integer> getVisitedCities() {
        return visitedCities;
    }

    public int getCurrentLocation() {
        return currentLocation;
    }

    public double[][] getVisionMatrix() {
        return visionMatrix;
    }

    public int getPathCost(int[][] distances){
        int cost = 0;
        return cost;
    }
}
