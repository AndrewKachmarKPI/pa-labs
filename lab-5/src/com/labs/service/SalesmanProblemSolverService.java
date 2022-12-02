package com.labs.service;

import com.labs.domain.Ant;
import com.labs.domain.CityNode;
import com.labs.domain.PathSearchResult;

import java.util.*;
import java.util.stream.Collectors;

import static com.labs.service.GeneralMethodsService.*;


public class SalesmanProblemSolverService {
    public static int G_SIZE = 5;
    private final int A;
    private final int B;
    private final int L_MIN;
    private final double R;
    private final int numberOfAnts;

    private List<Ant> ants = new ArrayList<>();
    private int[][] distanceMatrix = new int[G_SIZE][G_SIZE];
    private double[][] pheromoneMatrix = new double[G_SIZE][G_SIZE];

    private PathSearchResult bestPath;

    public SalesmanProblemSolverService(int a, int b, double r, int lMin, int numberOfAnts) {
        this.A = a;
        this.B = b;
        this.R = r;
        this.L_MIN = lMin;
        this.numberOfAnts = numberOfAnts;
    }

    public PathSearchResult findSolution() {
        distanceMatrix = buildDistanceMatrix();
        pheromoneMatrix = buildPheromoneMatrix();
        generateAnts(numberOfAnts);
        placeAnts(ants);

        for (int i = 0; i < 1; i++) {//COLONY LIFE
            List<PathSearchResult> paths = buildPathsForAllAnts();
            this.bestPath = paths.stream()
                    .min(Comparator.comparing(PathSearchResult::getPathCost))
                    .orElseThrow(() -> new RuntimeException("Path not found"));
            updatePheromoneLevel(paths);
        }
        return bestPath;
    }

    private void updatePheromoneLevel(List<PathSearchResult> pathSearchResults) {
        for (int i = 0; i < G_SIZE; i++) {
            for (int j = 0; j < G_SIZE; j++) {
                pheromoneMatrix[i][j] = getAntPheromoneLevel(pathSearchResults, i, j);
            }
        }
    }

    private double getAntPheromoneLevel(List<PathSearchResult> pathSearchResults, int from, int to) {
        double pheromoneSum = 0;
        for (PathSearchResult searchResult : pathSearchResults) {
            double pheromoneWeight = 0;
            String pathCheck = from + "-" + to;
            if (searchResult.getPath().contains(pathCheck)) {
                pheromoneWeight = L_MIN / searchResult.getPathCost();
            }
            pheromoneSum += (1 - R) * pheromoneAtPath(from, to) + pheromoneWeight;
        }
        return pheromoneSum;
    }


    private List<PathSearchResult> buildPathsForAllAnts() {
        List<PathSearchResult> paths = new ArrayList<>();
        for (Ant ant : ants) {
            PathSearchResult pathSearchResult = findAntPath(ant);
            pathSearchResult.countPathCost(distanceMatrix);
            paths.add(pathSearchResult);
        }
        return paths;
    }


    private PathSearchResult findAntPath(Ant ant) {
        int initialPosition = ant.getCurrentCityIndex();
        PathSearchResult pathSearchResult = new PathSearchResult(initialPosition, ant.getAntId());
        while (!ant.isFound()) {
            CityNode nextCityNode = nextCityMove(ant);
            if (nextCityNode.isNodeFound()) {
                ant.visitCity(nextCityNode.getIndex());
                pathSearchResult.addCityIndex(nextCityNode.getIndex(), false);
            } else {
                pathSearchResult.addCityIndex(initialPosition, true);
            }
        }
        return pathSearchResult;
    }

    private CityNode nextCityMove(Ant ant) {
        List<CityNode> availableCities = ant.availableCities();
        for (CityNode cityNode : availableCities) {
            double probability = countProbability(ant, ant.getCurrentCityIndex(), cityNode.getIndex());
            cityNode.setProbability(probability);
        }

        CityNode cityNode = new CityNode(null);
        if (!availableCities.isEmpty()) {
            cityNode = availableCities.stream().max(Comparator.comparing(CityNode::getProbability))
                    .orElseThrow(() -> new RuntimeException("City not found"));
        }
        return cityNode;
    }

    private double countProbability(Ant ant, int from, int to) {
        double pheromoneValue = Math.pow(pheromoneAtPath(from, to), A);
        double visionValue = Math.pow(ant.visionAtPath(from, to), B);
        double antsValue = sumAntCities(ant, from);
        return (pheromoneValue * visionValue) / (antsValue);
    }

    private double sumAntCities(Ant ant, int from) {
        List<Integer> availableCitiesIndexes = ant.availableCities().stream().map(CityNode::getIndex).collect(Collectors.toList());
        double sum = 0.0;
        for (Integer index : availableCitiesIndexes) {
            double pheromoneValue = Math.pow(pheromoneAtPath(from, index), A);
            double visionValue = Math.pow(ant.visionAtPath(from, index), B);
            sum += (pheromoneValue * visionValue);
        }
        return sum;
    }


    private void generateAnts(int numberOfAnts) {
        for (int i = 0; i < numberOfAnts; i++) {
            ants.add(new Ant(0, distanceMatrix));
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

    public static int randomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static double randomDouble(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public double distanceAtPath(int from, int to) {
        return distanceMatrix[from][to];
    }

    public double pheromoneAtPath(int from, int to) {
        return pheromoneMatrix[from][to];
    }
}
