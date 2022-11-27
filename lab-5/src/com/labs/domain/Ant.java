package com.labs.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.labs.service.SalesmanProblemSolverService.G_SIZE;


public class Ant {
    private final String antId;
    private final List<String> visitedCities = new ArrayList<>();
    private String currentLocation;
    private final double[][] visionMatrix = new double[G_SIZE][G_SIZE];


    public Ant(int[][] distanceMatrix, String currentLocation) {
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

    public List<String> getVisitedCities() {
        return visitedCities;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public double[][] getVisionMatrix() {
        return visionMatrix;
    }

    public void moveToCity(String cityIndex) {
        visitedCities.add(cityIndex);
        this.currentLocation = cityIndex;
    }

    public int getPathCost(int[][] distances) {
        int cost = 0;
        return cost;
    }
}
