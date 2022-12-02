package com.labs.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.labs.service.GeneralMethodsService.*;
import static com.labs.service.SalesmanProblemSolverService.*;


public class Ant {
    private final String antId;
    private List<Integer> visitedCities;
    private Integer currentCityIndex;
    private Boolean isFound = false;

    private double[][] pheromoneMatrix;
    private double[][] visionMatrix;

    public Ant(Integer currentCityIndex, int[][] distances) {
        this.antId = UUID.randomUUID().toString();
        this.currentCityIndex = currentCityIndex;
        this.visitedCities = generateCities();

        this.visionMatrix = buildVisionMatrix(distances);
        this.pheromoneMatrix = buildPheromoneMatrix();
    }

    public void visitCity(Integer cityIndex) {
        visitedCities.set(cityIndex, 1);
        this.currentCityIndex = cityIndex;
    }

    public List<CityNode> availableCities() {
        List<CityNode> cityNodes = new ArrayList<>();
        for (int i = 0; i < G_SIZE; i++) {
            if (visitedCities.get(i) != 1) {
                cityNodes.add(new CityNode(i));
            }
        }
        if (cityNodes.isEmpty()) {
            this.isFound = true;
        }
        return cityNodes;
    }

    public void updatePheromone() {

    }

    public Integer getCurrentCityIndex() {
        return currentCityIndex;
    }

    public Boolean isFound() {
        return isFound;
    }

    public double pheromoneAtPath(int from, int to) {
        return pheromoneMatrix[from][to];
    }

    public double visionAtPath(int from, int to) {
        return visionMatrix[from][to];
    }
}
