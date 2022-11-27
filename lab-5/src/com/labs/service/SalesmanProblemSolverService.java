package com.labs.service;

import com.labs.domain.Ant;
import com.labs.domain.CityNode;

import java.util.*;
import java.util.stream.Collectors;

public class SalesmanProblemSolverService {
    public static int G_SIZE = 5;
    private int A, B, L_MIN;
    private double R;
    private List<Ant> ants = new ArrayList<>();

    private List<String> cities = new ArrayList<>();

    private final int[][] distanceMatrix = new int[G_SIZE][G_SIZE];
    private final double[][] pheromoneMatrix = new double[G_SIZE][G_SIZE];

    public SalesmanProblemSolverService(int a, int b, double r, int lMin, int numberOfAnts) {
        this.A = a;
        this.B = b;
        this.R = r;
        this.L_MIN = lMin;

        generateDistanceMatrix();
        buildPheromoneMatrix();
        generateAnts(numberOfAnts);
        findSolution();
    }

    private void findSolution() {
        Ant ant = ants.get(0);
        String startCity = ant.getCurrentLocation();
        System.out.println("START ->" + startCity);

        for (int i = 0; i < 5; i++) {
            CityNode moveCityNode = getMoveCity(ant);
            System.out.println("MOVE TO ->"+moveCityNode);
            ant.moveToCity(moveCityNode.getCity());
//            System.out.println("==================");
//            System.out.println(ant.getVisitedCities());
//            System.out.println("==================");
        }

        System.out.println("FINISH ->" + ant.getCurrentLocation());
    }

    private void generateAnts(int numberOfAnts) { //STEP 1
        for (int i = 0; i < numberOfAnts; i++) {
            ants.add(new Ant(distanceMatrix, cities.get(0)));
        }
    }

    private CityNode getMoveCity(Ant ant) { //STEP 2  TODO FIX
        List<CityNode> cityNodes = new ArrayList<>();
        int currentLocationIndex = cities.indexOf(ant.getCurrentLocation());
        for (int i = 0; i < cities.size(); i++) {
            if (currentLocationIndex != i) {
                double up = Math.pow(getPheromoneLevel(currentLocationIndex, i), A) * Math.pow(getDistance(currentLocationIndex, i), B);
                double down = citiesProbability(ant);
                cityNodes.add(new CityNode(i, cities.get(i), up / down));
            }
        }
        Optional<CityNode> cityNode = cityNodes.stream().max(Comparator.comparing(CityNode::getProbability));
        if (cityNode.isEmpty()) {
            throw new RuntimeException("City not found");
        }
        return cityNode.get();
    }

    private double citiesProbability(Ant ant) { //STEP 2 TODO FIX RETREIVE ALL
        List<Integer> availableCities = new ArrayList<>();
        cities.forEach(city -> {
            if (!ant.getVisitedCities().contains(city)) {
                availableCities.add(cities.indexOf(city));
            }
        });
        System.out.println("SIZE->"+availableCities.size());

        double probability = 0;
        int currentLocationIndex = cities.indexOf(ant.getCurrentLocation());
        for (int i = 0; i < availableCities.size(); i++) {
            probability += Math.pow(getPheromoneLevel(currentLocationIndex, i), A) * Math.pow(getDistance(currentLocationIndex, i), B);
        }
        return probability;
    }

    private void updatePheromone() {

    }


    //ADDITIONAL
    private double getPheromoneLevel(int form, int to) {
        return pheromoneMatrix[form][to];
    }

    private double getDistance(int form, int to) {
        return distanceMatrix[form][to];
    }

    private void generateCities() {
        for (int i = 0; i < G_SIZE; i++) {
            cities.add(UUID.randomUUID().toString());
        }
    }

    private void generateDistanceMatrix() {
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                int distance = (i == j) ? 0 : randomNumber(5, 150);
                distanceMatrix[i][j] = distance;
            }
        }
        generateCities();
    }

    private void buildPheromoneMatrix() {
        for (int i = 0; i < pheromoneMatrix.length; i++) {
            for (int j = 0; j < pheromoneMatrix.length; j++) {
                pheromoneMatrix[i][j] = round(randomDouble(0.1, 0.2), 2);
            }
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

    public int randomNumber(int min, int max) {
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
}
