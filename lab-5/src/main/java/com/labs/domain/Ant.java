package com.labs.domain;

import com.labs.enums.AntType;
import lombok.Getter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.labs.service.SalesmanProblemSolverService.G_SIZE;

@Getter
public class Ant {
    private final String antId;
    private List<Integer> visitedCities;
    private Integer currentCityIndex;
    private AntType antType;
    private Boolean isFound = false;
    private double[][] visionMatrix;

    public Ant(Integer currentCityIndex, double[][] visionMatrix, AntType antType) {
        if (currentCityIndex < 0) {
            throw new RuntimeException("Invalid city index");
        }
        this.antId = UUID.randomUUID().toString();
        this.currentCityIndex = currentCityIndex;
        this.visitedCities = getCities();
        this.visionMatrix = visionMatrix;
        this.antType = antType;
    }

    public void visitCity(Integer cityIndex) {
        visitedCities.set(cityIndex, 1);
        this.currentCityIndex = cityIndex;
    }

    public List<CityNode> getAvailableCities() {
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
        this.visitedCities = getCities();
        visitCity(cityIndex);
        this.isFound = false;
    }

    public double getVisionAtPath(int from, int to) {
        return visionMatrix[from][to];
    }

    private List<Integer> getCities() {
        return new ArrayList<>(Collections.nCopies(G_SIZE, 0));
    }
}
