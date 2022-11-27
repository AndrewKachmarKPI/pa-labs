package com.labs.service;

import com.labs.domain.Ant;

import java.util.*;
import java.util.stream.Collectors;

public class SalesmanProblemSolverService {
    private int A, B, L_MIN;
    private double R;

    private List<Ant> ants = new ArrayList<>();
    private List<Integer> cities = new ArrayList<>();
    private final int[][] distanceMatrix = new int[300][300];
    private final double[][] pheromoneMatrix = new double[300][300];

    public SalesmanProblemSolverService(int a, int b, double r, int lMin, int numberOfAnts) {
        this.A = a;
        this.B = b;
        this.R = r;
        this.L_MIN = lMin;

        generateDistanceMatrix();
        buildPheromoneMatrix();
        generateAnts(numberOfAnts);
    }

    private void findSolution() {
        double probability = calculateProbability(ants.get(0));
        System.out.println(probability);
    }

    private void generateAnts(int numberOfAnts) { //STEP 1
        int index = 0;
        for (int i = 0; i < numberOfAnts; i++) {
            ants.add(new Ant(distanceMatrix, index));
        }
    }

    private double calculateProbability(Ant ant) { //STEP 2
        List<Double> probabilities = new ArrayList<>();
        int currentLocation = ant.getCurrentLocation();
        for (int i = 0; i < distanceMatrix.length; i++) {
            if (currentLocation != i) {
                double up = Math.pow(getPheromoneLevel(currentLocation, i), A) * Math.pow(getDistance(currentLocation, i), B);
                double down = citiesProbability(ant);
                probabilities.add(up / down);
            }
        }
        probabilities.forEach(System.out::println);
        OptionalDouble probability = probabilities.stream()
                .mapToDouble(value -> value)
                .max();

        if (probability.isEmpty()) {
            throw new RuntimeException("Probability not found");
        }
        System.out.println("====================");
        return probability.getAsDouble();
    }

    private double citiesProbability(Ant ant) {
        List<Integer> availableCities = cities.stream().filter(city -> !ant.getVisitedCities().contains(city)).collect(Collectors.toList());
        double probability = 0;
        for (int i = 0; i < availableCities.size(); i++) {
            probability += Math.pow(getPheromoneLevel(ant.getCurrentLocation(), i), A) * Math.pow(getDistance(ant.getCurrentLocation(), i), B);
        }
        return probability;
    }

    private double getPheromoneLevel(int form, int to) {
        return pheromoneMatrix[form][to];
    }

    private double getDistance(int form, int to) {
        return distanceMatrix[form][to];
    }

    private void generateCities() {
        for (int i = 0; i < 300; i++) {
            cities.add(i);
        }
    }

    //ADDITIONAL
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
