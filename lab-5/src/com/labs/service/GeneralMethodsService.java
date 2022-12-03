package com.labs.service;

import com.labs.domain.Ant;
import com.labs.domain.SalesmanProblemDto;
import com.labs.enums.AntPlacementType;
import com.labs.enums.AntType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.labs.service.SalesmanProblemSolverService.*;

public class GeneralMethodsService {
    private static Map<String, Integer> antsInitialPlacement = new HashMap<>();

    public static int[][] buildDistanceMatrix() {
        int[][] distanceMatrix = new int[G_SIZE][G_SIZE];
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                int distance = (i == j) ? Integer.MAX_VALUE : randomNumber(5, 150);
                distanceMatrix[i][j] = distance;
            }
        }
        return distanceMatrix;
    }

    public static double[][] buildVisionMatrix(int[][] distanceMatrix) {
        double[][] visionMatrix = new double[G_SIZE][G_SIZE];
        for (int i = 0; i < visionMatrix.length; i++) {
            for (int j = 0; j < visionMatrix.length; j++) {
                if (i == j) {
                    visionMatrix[i][j] = 0;
                } else {
                    visionMatrix[i][j] = round((double) 1 / distanceMatrix[i][j], 3);
                }
            }
        }
        return visionMatrix;
    }

    public static double[][] buildPheromoneMatrix() {
        double[][] pheromoneMatrix = new double[G_SIZE][G_SIZE];
        for (int i = 0; i < pheromoneMatrix.length; i++) {
            for (int j = 0; j < pheromoneMatrix.length; j++) {
                double pheromone = (i == j) ? 0 : (round(randomDouble(0.1, 0.2), 2));
                pheromoneMatrix[i][j] = pheromone;
            }
        }
        return pheromoneMatrix;
    }

    public static List<Integer> generateCities() {
        List<Integer> cities = new ArrayList<>();
        for (int i = 0; i < G_SIZE; i++) {
            cities.add(0);
        }
        return cities;
    }

    public static List<Ant> generateAnts(int[][] distanceMatrix, SalesmanProblemDto salesmanProblemDto) {
        List<Ant> ants = new ArrayList<>();
        if (salesmanProblemDto.getNumberOfEliteAnts() > salesmanProblemDto.getNumberOfAnts()) {
            throw new RuntimeException("Not enough ants for creation");
        }
        for (int i = 0; i < salesmanProblemDto.getNumberOfAnts() - salesmanProblemDto.getNumberOfEliteAnts(); i++) {
            ants.add(new Ant(0, distanceMatrix));
        }
        for (int i = 0; i < salesmanProblemDto.getNumberOfEliteAnts(); i++) {
            ants.add(new Ant(0, distanceMatrix, AntType.ELITE));
        }
        return ants;
    }


    public static void placeAnts(List<Ant> ants, AntPlacementType placementType) {
        for (Ant ant : ants) {
            int cityIndex = 0;

            if (placementType == AntPlacementType.MANY_WITH_REPEAT) {
                cityIndex = randomNumber(0, G_SIZE);
            } else {
                if (ants.size() > G_SIZE) {
                    throw new RuntimeException("Ant size should be greater that " + G_SIZE);
                }
                while (antsInitialPlacement.containsValue(cityIndex)) {
                    cityIndex = randomNumber(0, G_SIZE);
                }
            }
            ant.visitCity(cityIndex);
            antsInitialPlacement.put(ant.getAntId(), cityIndex);
        }
    }

    public static Map<String, Integer> getAntsInitialPlacement() {
        return antsInitialPlacement;
    }
}
