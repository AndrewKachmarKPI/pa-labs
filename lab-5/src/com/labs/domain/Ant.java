package com.labs.domain;

import com.labs.enums.AntType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.labs.service.GeneralMethodsService.*;
import static com.labs.service.SalesmanProblemSolverService.*;


public class Ant {
    private final String antId;
    private List<Integer> visitedCities;
    private Integer currentCityIndex;
    private AntType antType = AntType.ORDINARY;
    private Boolean isFound = false;

    private double[][] visionMatrix;

    public Ant(Integer currentCityIndex, int[][] distances) {
        this.antId = UUID.randomUUID().toString();
        this.currentCityIndex = currentCityIndex;
        this.visitedCities = generateCities();
        this.visionMatrix = buildVisionMatrix(distances);
    }

    public Ant(Integer currentCityIndex, int[][] distances, AntType antType) {
        this.antId = UUID.randomUUID().toString();
        this.currentCityIndex = currentCityIndex;
        this.visitedCities = generateCities();
        this.visionMatrix = buildVisionMatrix(distances);
        this.antType = antType;
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

    public void clearMemory(Integer cityIndex) {
        this.visitedCities = generateCities();
        visitCity(cityIndex);
        this.isFound = false;
    }

    public Integer getCurrentCityIndex() {
        return currentCityIndex;
    }

    public String getAntId() {
        return antId;
    }

    public AntType getAntType() {
        return antType;
    }

    public Boolean isFound() {
        return isFound;
    }

    public double visionAtPath(int from, int to) {
        return visionMatrix[from][to];
    }
}
