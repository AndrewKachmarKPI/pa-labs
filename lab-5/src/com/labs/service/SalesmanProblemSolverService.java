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
    private int L_STAR = Integer.MAX_VALUE;


    private List<Ant> ants = new ArrayList<>();
    private List<String> cities = new ArrayList<>();

    private int[][] distanceMatrix = new int[G_SIZE][G_SIZE];


    public SalesmanProblemSolverService(int a, int b, double r, int lMin, int numberOfAnts) {
        this.A = a;
        this.B = b;
        this.R = r;
        this.L_MIN = lMin;
        this.numberOfAnts = numberOfAnts;


        findSolution();
    }

    private void findSolution() {
        distanceMatrix = buildDistanceMatrix();

        generateAnts(numberOfAnts);
        placeAnts(ants);

        for (Ant ant : ants) {
            PathSearchResult pathSearchResult = findAntPath(ant);
            pathSearchResult.printPath();
        }
        System.out.println();
    }


    public PathSearchResult findAntPath(Ant ant) {
        int initialPosition = ant.getCurrentCityIndex();
        PathSearchResult pathSearchResult = new PathSearchResult(initialPosition);
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

    public CityNode nextCityMove(Ant ant) {
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

    public double countProbability(Ant ant, int from, int to) {
        double pheromoneValue = Math.pow(ant.pheromoneAtPath(from, to), A);
        double visionValue = Math.pow(ant.visionAtPath(from, to), B);
        double antsValue = sumAntCities(ant, from);
        return (pheromoneValue * visionValue) / (antsValue);
    }

    public double sumAntCities(Ant ant, int from) {
        List<Integer> availableCitiesIndexes = ant.availableCities().stream().map(CityNode::getIndex).collect(Collectors.toList());
        double sum = 0.0;
        for (Integer index : availableCitiesIndexes) {
            double pheromoneValue = Math.pow(ant.pheromoneAtPath(from, index), A);
            double visionValue = Math.pow(ant.visionAtPath(from, index), B);
            sum += (pheromoneValue * visionValue);
        }
        return sum;
    }


    public void generateAnts(int numberOfAnts) {
        for (int i = 0; i < numberOfAnts; i++) {
            ants.add(new Ant(0, distanceMatrix));
        }
    }

//    private CityNode getMoveCity(Ant ant) { //STEP 2  TODO FIX
//        List<CityNode> cityNodes = new ArrayList<>();
//        int currentLocationIndex = cities.indexOf(ant.getCurrentLocation());
//        for (int i = 0; i < cities.size(); i++) {
//            if (currentLocationIndex != i) {
//                double up = Math.pow(getPheromoneLevel(currentLocationIndex, i), A) * Math.pow(getDistance(currentLocationIndex, i), B);
//                double down = citiesProbability(ant);
//                cityNodes.add(new CityNode(i, cities.get(i), up / down));
//            }
//        }
//        Optional<CityNode> cityNode = cityNodes.stream().max(Comparator.comparing(CityNode::getProbability));
//        if (cityNode.isEmpty()) {
//            throw new RuntimeException("City not found");
//        }
//        return cityNode.get();
//    }
//
//    private double citiesProbability(Ant ant) { //STEP 2 TODO FIX RETREIVE ALL
//        List<Integer> availableCities = new ArrayList<>();
//        cities.forEach(city -> {
//            if (!ant.getVisitedCities().contains(city)) {
//                availableCities.add(cities.indexOf(city));
//            }
//        });
//        System.out.println("SIZE->" + availableCities.size());
//
//        double probability = 0;
//        int currentLocationIndex = cities.indexOf(ant.getCurrentLocation());
//        for (int i = 0; i < availableCities.size(); i++) {
//            probability += Math.pow(getPheromoneLevel(currentLocationIndex, i), A) * Math.pow(getDistance(currentLocationIndex, i), B);
//        }
//        return probability;
//    }


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
}
