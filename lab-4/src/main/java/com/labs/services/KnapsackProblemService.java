package com.labs.services;

import com.labs.domain.Item;
import com.labs.domain.MutationResponse;
import com.labs.domain.PopulationNode;

import java.util.*;

public class KnapsackProblemService {
    private final Integer capacity;
    private static final Integer numberOfNodes = 100;
    private final List<Item> items;
    private final List<PopulationNode> currentPopulation = new ArrayList<>();
    private PopulationNode currentRecordNode; //F* rack with best price

    public KnapsackProblemService(int capacity) {
        this.capacity = capacity;
        this.items = getRandomItems(100);
        generateInitialPopulation();
    }

    public PopulationNode getPackedKnapsack(Integer iterations) {
        if (capacity <= 0) {
            throw new RuntimeException("Capacity should not be 0 or less");
        }
        for (int i = 0; i < iterations; i++) {
            PopulationNode selectedNode = getPopulationSelection();
            PopulationNode afterCross = getPopulationCross(selectedNode, currentRecordNode);

            MutationResponse mutationResponse = getPopulationMutation(afterCross);
            PopulationNode nodeForImprovement = mutationResponse.isSuccessful() ? mutationResponse.getPopulationNode() : afterCross;
            PopulationNode populationNode = getLocalImprovement(nodeForImprovement);

            updateCurrentRecord(populationNode);
            replaceWorstPopulationNode(populationNode);
        }
        return currentRecordNode;
    }

    public void updateCurrentRecord(PopulationNode populationNode) {
        if (populationNode.getTotalPrice() > currentRecordNode.getTotalPrice() && populationNode.getTotalWeight() <= capacity) {
            currentRecordNode = populationNode;
        }
    }

    public void generateInitialPopulation() {
        List<Integer> emptyPopulation = new ArrayList<>(Collections.nCopies(numberOfNodes, 0));
        for (int i = 0; i < numberOfNodes; i++) {
            List<Integer> population = new ArrayList<>(emptyPopulation);
            population.set(i, 1);
            Item currentItem = items.get(i);
            currentPopulation.add(new PopulationNode(population, currentItem.getPrice(), currentItem.getWeight()));
        }
        currentRecordNode = getBestNodeOfPopulation(currentPopulation);
    }

    public PopulationNode getPopulationSelection() {
        int nodeIndex = getRandomNumber(0, numberOfNodes);
        PopulationNode populationNode = currentPopulation.get(nodeIndex);
        if (populationNode.getNodeId().equals(currentRecordNode.getNodeId())) {
            getPopulationSelection();
        }
        return populationNode;
    }

    public PopulationNode getPopulationCross(PopulationNode firstNode, PopulationNode secondNode) {
        List<Integer> selectedItems = new ArrayList<>();
        for (int i = 0; i < numberOfNodes; i++) {
            PopulationNode nodeForInsert = isPopulationCross() ? firstNode : secondNode;
            selectedItems.add(nodeForInsert.getItemsSelection().get(i));
        }
        PopulationNode newNode = new PopulationNode(selectedItems);
        newNode.countParameters(items);
        return newNode;
    }

    public MutationResponse getPopulationMutation(PopulationNode populationNode) {
        int firstNodeIndex = getRandomNumber(0, numberOfNodes);
        int secondNodeIndex = getRandomNumber(0, numberOfNodes);
        while (secondNodeIndex == firstNodeIndex) {
            secondNodeIndex = getRandomNumber(0, numberOfNodes);
        }
        if (isPerformMutation(5) && isEnoughGenes(populationNode)) {
            Integer firstGeneValue = populationNode.getItemsSelection().get(firstNodeIndex);
            Integer secondGeneValue = populationNode.getItemsSelection().get(secondNodeIndex);
            populationNode.selectItem(firstNodeIndex, secondGeneValue);
            populationNode.selectItem(secondNodeIndex, firstGeneValue);
            populationNode.countParameters(items);
        }
        return new MutationResponse(populationNode, populationNode.getTotalWeight() < capacity);
    }

    public PopulationNode getLocalImprovement(PopulationNode populationNode) {
        int dif = getRandomNumber(0, 5);
        int xRange = getRandomNumber(0, numberOfNodes);
        int randomIndex = getRandomNumber(xRange, getRange(dif, xRange));
        populationNode.selectItem(randomIndex, 1);
        populationNode.countParameters(items);
        return populationNode;
    }

    public boolean isPopulationCross() {
        return getRandomDouble(0, 1) < 0.5;
    }

    public boolean isPerformMutation(int mutationProbability) {
        SplittableRandom random = new SplittableRandom();
        return random.nextInt(1, 101) <= mutationProbability;
    }

    public PopulationNode getBestNodeOfPopulation(List<PopulationNode> currentPopulation) {
        Integer maxTotalPrice = currentPopulation.stream().max(Comparator.comparing(PopulationNode::getTotalPrice)).orElseThrow(() -> new RuntimeException("Best node not found")).getTotalPrice();
        return currentPopulation.stream().filter(node -> node.getTotalPrice().equals(maxTotalPrice)).min(Comparator.comparing(PopulationNode::getTotalWeight)).orElseThrow(() -> new RuntimeException("Best node not found"));
    }

    public void replaceWorstPopulationNode(PopulationNode populationNode) {
        Integer minTotalPrice = currentPopulation.stream().min(Comparator.comparing(PopulationNode::getTotalPrice)).orElseThrow(() -> new RuntimeException("Min node not found")).getTotalPrice();
        PopulationNode worstNode = currentPopulation.stream().filter(node -> node.getTotalPrice().equals(minTotalPrice)).max(Comparator.comparing(PopulationNode::getTotalWeight)).orElseThrow(() -> new RuntimeException("Min node not found"));
        currentPopulation.set(currentPopulation.indexOf(worstNode), populationNode);
    }

    public boolean isEnoughGenes(PopulationNode populationNode) {
        return populationNode.getItemsSelection().stream().filter(gene -> gene == 1).count() > 2;
    }

    public int getRange(int dif, int xRange) {
        if (xRange + dif > this.items.size()) {
            xRange = this.items.size() - xRange;
        }
        return xRange;
    }

    public List<Item> getRandomItems(int count) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            items.add(new Item(getRandomNumber(2, 11), getRandomNumber(1, 6)));
        }
        return items;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public double getRandomDouble(int min, int max) {
        return ((Math.random() * (max - min)) + min);
    }

    public static Integer getCapacity() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter backpack capacity:");
        int capacity = scanner.nextInt();
        if (capacity < 0) {
            System.out.println("Invalid capacity");
            return getCapacity();
        }
        return capacity;
    }
}
