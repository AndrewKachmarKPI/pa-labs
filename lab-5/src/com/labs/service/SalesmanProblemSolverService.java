package com.labs.service;

import com.labs.domain.*;
import com.labs.enums.AntPlacementType;
import com.labs.enums.AntType;

import java.util.*;
import java.util.stream.Collectors;

import static com.labs.service.GeneralMethodsService.*;


public class SalesmanProblemSolverService {
    public static int G_SIZE = 300;
    private SalesmanProblemDto salesmanProblemDto;

    private List<Ant> ants = new ArrayList<>();
    private int[][] distanceMatrix = new int[G_SIZE][G_SIZE];
    private double[][] pheromoneMatrix = new double[G_SIZE][G_SIZE];

    private PathSearchResult bestPath = new PathSearchResult();

    public SalesmanProblemSolverService(SalesmanProblemDto salesmanProblemDto) {
        this.salesmanProblemDto = salesmanProblemDto;
    }

    public PathSearchResult findSolution() {//MAIN METHOD
        distanceMatrix = buildDistanceMatrix();
        System.out.println("GEN distanceMatrix");
        pheromoneMatrix = buildPheromoneMatrix();
        System.out.println("GEN pheromoneMatrix");
        ants = generateAnts(distanceMatrix, salesmanProblemDto);
        System.out.println("GEN ants-" + ants.size());
        placeAnts(ants, salesmanProblemDto.getAntPlacementType());
        System.out.println("PLACE ants-" + ants.size());

        for (int i = 0; i < salesmanProblemDto.getColonyLife(); i++) {//COLONY LIFE
            List<PathSearchResult> paths = buildPathsForAllAnts(); //FIND CYCLES FOR ALL ANTS
            PathSearchResult foundBestPath = paths.stream()  //FIND BEST PATH BY COST
                    .min(Comparator.comparing(PathSearchResult::getPathCost))
                    .orElseThrow(() -> new RuntimeException("Path not found"));
            if (foundBestPath.getPathCost() < this.bestPath.getPathCost()) { //CHECK BEST SOLUTION
                this.bestPath = foundBestPath;
            }
            updatePheromoneLevel(paths); //UPDATE PHEROMONE
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
            System.out.println("PATH SIZES -" + paths.size());
//            System.out.println("FOUND PATH FOR ant " + ant.getAntId() + " -" + pathSearchResult);
        }
        return paths;
    }


    private PathSearchResult findAntPath(Ant ant) {
        int initialPosition = ant.getCurrentCityIndex();
        PathSearchResult pathSearchResult = new PathSearchResult(initialPosition, ant.getAntId());
        while (Boolean.FALSE.equals(ant.isFound())) {
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
        double pheromoneValue = Math.pow(pheromoneAtPath(from, to), salesmanProblemDto.getA());
        double visionValue = Math.pow(ant.visionAtPath(from, to), salesmanProblemDto.getB());
        double antsValue = sumAntCities(ant, from);
        return (pheromoneValue * visionValue) / (antsValue);
    }

    private double sumAntCities(Ant ant, int from) {
        List<Integer> availableCitiesIndexes = ant.availableCities().stream().map(CityNode::getIndex).collect(Collectors.toList());
        double sum = 0.0;
        for (Integer index : availableCitiesIndexes) {
            double pheromoneValue = Math.pow(pheromoneAtPath(from, index), salesmanProblemDto.getA());
            double visionValue = Math.pow(ant.visionAtPath(from, index), salesmanProblemDto.getB());
            sum += (pheromoneValue * visionValue);
        }
        return sum;
    }

    private void printMatrix(int[][] matrix) {
        for (int[] ints : matrix) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }

    private void printMatrix(double[][] matrix) {
        for (double[] ints : matrix) {
            for (double anInt : ints) {
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
