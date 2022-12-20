package com.labs.service;

import com.labs.domain.*;
import com.labs.enums.AntPlacementType;
import com.labs.enums.AntType;

import java.util.*;
import java.util.stream.Collectors;

public class SalesmanProblemSolverService {
    public static int G_SIZE;
    private final AntAlgorithmParams antAlgorithmParams;

    private List<Ant> ants = new ArrayList<>();
    private int[][] distanceMatrix = new int[G_SIZE][G_SIZE];
    private double[][] pheromoneMatrix = new double[G_SIZE][G_SIZE];

    private final Map<String, Integer> antsInitialPlacement = new HashMap<>();
    private PathSearchResult bestPath = new PathSearchResult();

    public SalesmanProblemSolverService(AntAlgorithmParams antAlgorithmParams) {
        antAlgorithmParams.checkParamsValidity();
        this.antAlgorithmParams = antAlgorithmParams;
    }

    public PathSearchResult getBestPath(int numberOfCities) {
        if (numberOfCities <= 0) {
            throw new RuntimeException("Invalid city size");
        }
        G_SIZE = numberOfCities;
        distanceMatrix = getDistanceMatrix();
        pheromoneMatrix = getPheromoneMatrix();
        ants = getAnts(antAlgorithmParams);
        placeAnts(ants, antAlgorithmParams.getAntPlacementType());

        for (int i = 0; i < antAlgorithmParams.getColonyLife(); i++) {
            System.out.println("Colony life ->" + i + 1);
            List<PathSearchResult> paths = getPathsForAllAnts();
            PathSearchResult pathSearchResult = paths.stream()
                    .min(Comparator.comparing(PathSearchResult::getPathCost))
                    .orElseThrow(() -> new RuntimeException("Path not found"));
            updateBestPath(pathSearchResult);
            updatePheromoneLevel(paths);
            clearAntMemory();
        }
        return bestPath;
    }

    private void updateBestPath(PathSearchResult pathSearchResult) {
        if (pathSearchResult.getPathCost() < this.bestPath.getPathCost()) {
            this.bestPath = pathSearchResult;
        }
    }

    private void clearAntMemory() {
        this.ants.forEach(ant -> ant.clearMemory(antsInitialPlacement.get(ant.getAntId())));
    }

    private void updatePheromoneLevel(List<PathSearchResult> pathSearchResults) {
        for (int i = 0; i < G_SIZE; i++) {
            for (int j = 0; j < G_SIZE; j++) {
                if (i != j) {
                    double pheromoneValue = (1 - antAlgorithmParams.getR()) * pheromoneMatrix[i][j] + getAntPheromoneLevel(pathSearchResults, i, j);
                    pheromoneMatrix[i][j] = getRoundedNumber(pheromoneValue, 2);
                }
            }
        }
    }

    private double getAntPheromoneLevel(List<PathSearchResult> pathSearchResults, int from, int to) {
        double pheromoneLevel = 0;
        for (PathSearchResult searchResult : pathSearchResults) {
            String pathCheck = from + "-" + to;
            if (searchResult.getPath().toString().contains(pathCheck)) {
                Ant pathAnt = ants.stream()
                        .filter(ant -> ant.getAntId().equals(searchResult.getAntId()))
                        .findAny()
                        .orElseThrow(() -> new RuntimeException("Ant with id " + searchResult.getAntId() + " not found"));
                pheromoneLevel += (double) antAlgorithmParams.getL_MIN() / searchResult.getPathCost();

                if (pathAnt.getAntType() == AntType.ELITE) {
                    pheromoneLevel += (double) antAlgorithmParams.getL_MIN() / searchResult.getPathCost();
                }
            }
        }
        return pheromoneLevel;
    }

    private List<PathSearchResult> getPathsForAllAnts() {
        List<PathSearchResult> paths = new ArrayList<>();
        for (Ant ant : ants) {
            System.out.println("Find path for ant->" + ant.getAntId());
            PathSearchResult pathSearchResult = getAntPath(ant);
            pathSearchResult.countPathCost(distanceMatrix);
            paths.add(pathSearchResult);
        }
        return paths;
    }

    private PathSearchResult getAntPath(Ant ant) {
        int initialPosition = ant.getCurrentCityIndex();
        PathSearchResult antPath = new PathSearchResult(initialPosition, ant.getAntId());
        while (!ant.getIsFound()) {
            CityNode nextCityNode = (ant.getAntType() == AntType.WILD) ? getNextCityMoveForWildAnt(ant) : getNextCityMove(ant);
            if (nextCityNode.isNodeFound()) {
                ant.visitCity(nextCityNode.getIndex());
                antPath.addCityIndex(nextCityNode.getIndex(), false);
            } else {
                antPath.addCityIndex(initialPosition, true);
            }
        }
        return antPath;
    }

    private CityNode getNextCityMoveForWildAnt(Ant ant) {
        List<CityNode> availableCities = ant.getAvailableCities();
        CityNode nextCity = new CityNode();
        if (!availableCities.isEmpty()) {
            nextCity = availableCities.get(getRandomNumber(0, availableCities.size()));
        }
        return nextCity;
    }

    private CityNode getNextCityMove(Ant ant) {
        List<CityNode> availableCities = ant.getAvailableCities();
        for (CityNode cityNode : availableCities) {
            double probability = getMoveProbability(ant, ant.getCurrentCityIndex(), cityNode.getIndex());
            cityNode.setProbability(probability);
        }
        CityNode nextCity = new CityNode();
        if (!availableCities.isEmpty()) {
            nextCity = availableCities.stream().max(Comparator.comparing(CityNode::getProbability))
                    .orElseThrow(() -> new RuntimeException("City not found"));
        }
        return nextCity;
    }

    private double getMoveProbability(Ant ant, int from, int to) {
        double pheromoneValue = Math.pow(getPheromoneAtPath(from, to), antAlgorithmParams.getA());
        double visionValue = Math.pow(ant.getVisionAtPath(from, to), antAlgorithmParams.getB());
        double antsValue = getAntCitiesSum(ant, from);
        double moveProbability = 0;
        if (antsValue != 0) {
            moveProbability = (pheromoneValue * visionValue) / (antsValue);
        }
        return moveProbability;
    }

    private double getAntCitiesSum(Ant ant, int from) {
        List<Integer> availableCitiesIndexes = ant.getAvailableCities()
                .stream()
                .map(CityNode::getIndex)
                .collect(Collectors.toList());
        double antCitiesSum = 0.0;
        for (Integer index : availableCitiesIndexes) {
            double pheromoneValue = Math.pow(getPheromoneAtPath(from, index), antAlgorithmParams.getA());
            double visionValue = Math.pow(ant.getVisionAtPath(from, index), antAlgorithmParams.getB());
            antCitiesSum += (pheromoneValue * visionValue);
        }
        return antCitiesSum;
    }

    private int[][] getDistanceMatrix() {
        int[][] distanceMatrix = new int[G_SIZE][G_SIZE];
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                int distance = (i == j) ? Integer.MAX_VALUE : getRandomNumber(5, 150);
                distanceMatrix[i][j] = distance;
            }
        }
        return distanceMatrix;
    }

    private double[][] getPheromoneMatrix() {
        double[][] pheromoneMatrix = new double[G_SIZE][G_SIZE];
        for (int i = 0; i < pheromoneMatrix.length; i++) {
            for (int j = 0; j < pheromoneMatrix.length; j++) {
                double pheromone = (i == j) ? 0 : (getRoundedNumber(getRandomDouble(0.1, 0.2), 2));
                pheromoneMatrix[i][j] = pheromone;
            }
        }
        return pheromoneMatrix;
    }

    private double[][] getVisionMatrix(int[][] distanceMatrix) {
        double[][] visionMatrix = new double[G_SIZE][G_SIZE];
        for (int i = 0; i < visionMatrix.length; i++) {
            for (int j = 0; j < visionMatrix.length; j++) {
                if (i == j) {
                    visionMatrix[i][j] = 0;
                } else {
                    visionMatrix[i][j] = getRoundedNumber((double) 1 / distanceMatrix[i][j], 3);
                }
            }
        }
        return visionMatrix;
    }

    private List<Ant> getAnts(AntAlgorithmParams antAlgorithmParams) {
        List<Ant> ants = new ArrayList<>();
        double[][] visionMatrix = getVisionMatrix(distanceMatrix);
        for (int i = 0; i < antAlgorithmParams.getNumberOfOrdinaryAnts(); i++) {
            ants.add(new Ant(0, visionMatrix, AntType.ORDINARY));
        }
        for (int i = 0; i < antAlgorithmParams.getNumberOfEliteAnts(); i++) {
            ants.add(new Ant(0, visionMatrix, AntType.ELITE));
        }
        for (int i = 0; i < antAlgorithmParams.getNumberOfWildAnts(); i++) {
            ants.add(new Ant(0, visionMatrix, AntType.WILD));
        }
        return ants;
    }

    private void placeAnts(List<Ant> ants, AntPlacementType placementType) {
        for (Ant ant : ants) {
            int cityIndex = 0;
            if (placementType == AntPlacementType.MANY_WITH_REPEAT) {
                cityIndex = getRandomNumber(0, G_SIZE);
            } else {
                if (ants.size() > G_SIZE) {
                    throw new RuntimeException("Ant size should be greater that " + G_SIZE);
                }
                while (antsInitialPlacement.containsValue(cityIndex)) {
                    cityIndex = getRandomNumber(0, G_SIZE);
                }
            }
            ant.visitCity(cityIndex);
            antsInitialPlacement.put(ant.getAntId(), cityIndex);
        }
    }

    private double getPheromoneAtPath(int from, int to) {
        return pheromoneMatrix[from][to];
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public double getRandomDouble(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    public double getRoundedNumber(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
