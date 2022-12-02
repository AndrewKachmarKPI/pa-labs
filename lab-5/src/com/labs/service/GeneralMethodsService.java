package com.labs.service;

import com.labs.domain.Ant;

import java.util.ArrayList;
import java.util.List;

import static com.labs.service.SalesmanProblemSolverService.*;

public class GeneralMethodsService {
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

    public static void placeAnts(List<Ant> ants) {
        for (Ant ant : ants) {
            ant.visitCity(randomNumber(0, G_SIZE));
        }
    }

}
