package com.labs.service;

import com.labs.domain.*;
import com.labs.enums.AntType;

import java.util.*;
import java.util.stream.Collectors;

import static com.labs.service.GeneralMethodsService.*;


public class SalesmanProblemSolverService {
    private final SalesmanProblemDto salesmanProblemDto;
    private List<Ant> ants = new ArrayList<>();

    public static int G_SIZE = 300;
    private int[][] distanceMatrix = new int[G_SIZE][G_SIZE];
    private double[][] pheromoneMatrix = new double[G_SIZE][G_SIZE];

    private PathSearchResult bestPath = new PathSearchResult();

    public SalesmanProblemSolverService(SalesmanProblemDto salesmanProblemDto, int numberOfCities) {
        G_SIZE = numberOfCities;
        this.salesmanProblemDto = salesmanProblemDto;
    }

    public PathSearchResult findSolution() {
        distanceMatrix = buildDistanceMatrix();
        pheromoneMatrix = buildPheromoneMatrix();
        ants = generateAnts(distanceMatrix, salesmanProblemDto);
        placeAnts(ants, salesmanProblemDto.getAntPlacementType());

        for (int i = 0; i < salesmanProblemDto.getColonyLife(); i++) {
            List<PathSearchResult> paths = buildPathsForAllAnts();
            PathSearchResult foundBestPath = paths.stream()
                    .min(Comparator.comparing(PathSearchResult::getPathCost))
                    .orElseThrow(() -> new RuntimeException("Path not found"));
            System.out.println("PATH->" + foundBestPath);
            if (foundBestPath.getPathCost() < this.bestPath.getPathCost()) {
                this.bestPath = foundBestPath;
            }
            updatePheromoneLevel(paths);
            clearAntMemory();
        }
        return bestPath;
    }

    public void clearAntMemory() {
        for (Ant ant : this.ants) {
            ant.clearMemory(getAntsInitialPlacement().get(ant.getAntId()));
        }
    }

    private void updatePheromoneLevel(List<PathSearchResult> pathSearchResults) {
        for (int i = 0; i < G_SIZE; i++) {
            for (int j = 0; j < G_SIZE; j++) {
                if (i != j) {
                    double pheromoneValue = (1 - salesmanProblemDto.getR()) * pheromoneMatrix[i][j] + getAntPheromoneLevel(pathSearchResults, i, j);
                    pheromoneMatrix[i][j] = round(pheromoneValue, 2);
                }
            }
        }
    }

    private double getAntPheromoneLevel(List<PathSearchResult> pathSearchResults, int from, int to) {
        double pheromoneSum = 0;
        for (PathSearchResult searchResult : pathSearchResults) {
            String pathCheck = from + "-" + to;
            if (searchResult.getPath().contains(pathCheck)) {
                Ant pathAnt = ants.stream()
                        .filter(ant -> ant.getAntId().equals(searchResult.getAntId()))
                        .findAny()
                        .orElseThrow(() -> new RuntimeException("Ant with id " + searchResult.getAntId() + " not found"));
                pheromoneSum += (double) salesmanProblemDto.getL_MIN() / searchResult.getPathCost();

                if (pathAnt.getAntType() == AntType.ELITE) {
                    pheromoneSum += (double) salesmanProblemDto.getL_MIN() / searchResult.getPathCost();
                }
            }
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
        while (Boolean.FALSE.equals(ant.isFound())) {
            CityNode nextCityNode = (ant.getAntType() == AntType.WILD) ? nextCityMoveForWildAnt(ant) : nextCityMove(ant);
            if (nextCityNode.isNodeFound()) {
                ant.visitCity(nextCityNode.getIndex());
                pathSearchResult.addCityIndex(nextCityNode.getIndex(), false);
            } else {
                pathSearchResult.addCityIndex(initialPosition, true);
            }
        }
        return pathSearchResult;
    }

    private CityNode nextCityMoveForWildAnt(Ant ant) {
        List<CityNode> availableCities = ant.availableCities();
        CityNode cityNode = new CityNode(null);
        if (!availableCities.isEmpty()) {
            cityNode = availableCities.get(randomNumber(0, availableCities.size()));
        }
        return cityNode;
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
        double pheromoneValue = Math.pow(pheromoneAtPath(from, to), salesmanProblemDto.getA());
        double visionValue = Math.pow(ant.visionAtPath(from, to), salesmanProblemDto.getB());
        double antsValue = sumAntCities(ant, from);
        double probability = 0;
        if (antsValue != 0) {
            probability = (pheromoneValue * visionValue) / (antsValue);
        }
        return probability;
    }

    private double sumAntCities(Ant ant, int from) {
        List<Integer> availableCitiesIndexes = ant.availableCities()
                .stream()
                .map(CityNode::getIndex)
                .collect(Collectors.toList());
        double sum = 0.0;
        for (Integer index : availableCitiesIndexes) {
            double pheromoneValue = Math.pow(pheromoneAtPath(from, index), salesmanProblemDto.getA());
            double visionValue = Math.pow(ant.visionAtPath(from, index), salesmanProblemDto.getB());
            sum += (pheromoneValue * visionValue);
        }
        return sum;
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

    public double pheromoneAtPath(int from, int to) {
        return pheromoneMatrix[from][to];
    }
}
