package com.labs.services;

import com.labs.domain.Item;
import com.labs.domain.MutationResponse;
import com.labs.domain.PopulationNode;

import java.util.*;

/**
 * Population select criteria (best in population + randomly selected)
 */
public class KnapsackProblemService {
    private final Integer capacity;
    private static final Integer numberOfNodes = 100;

    private final List<Item> items;
    private final List<PopulationNode> currentPopulation = new ArrayList<>();
    private PopulationNode currentRecord; //F* rack with best price

    public KnapsackProblemService(Integer capacity) {
        this.capacity = capacity;
        this.items = generateItems(100);
        this.generateInitial();
    }

    public PopulationNode searchLoop(Integer iterations) {
        for (int i = 0; i < iterations; i++) {
            PopulationNode selectedNode = selection();//S0
            PopulationNode afterCross = cross(selectedNode, currentRecord); //S1
            MutationResponse mutationResponse = mutation(afterCross); //S2
            PopulationNode nodeForImprovement = mutationResponse.isSuccessful() ? mutationResponse.getPopulationNode() : afterCross;
            PopulationNode populationNode = localImprovement(nodeForImprovement);//S3
            if (populationNode.getTotalPrice() > currentRecord.getTotalPrice() && populationNode.getTotalWeight() <= capacity) {
                currentRecord = populationNode;
            }
            replaceWorstPopulationNode(populationNode);
        }
        return currentRecord;
    }


    //STEP 1
    public void generateInitial() {
        List<Integer> emptyPopulation = new ArrayList<>(Collections.nCopies(numberOfNodes, 0));
        for (int i = 0; i < numberOfNodes; i++) {
            List<Integer> population = new ArrayList<>(emptyPopulation);
            population.set(i, 1);
            Item currentItem = items.get(i);
            currentPopulation.add(new PopulationNode(population, currentItem.getPrice(), currentItem.getWeight()));
        }
        currentRecord = getBestNodeOfPopulation(currentPopulation);
    }
    //STEP 2
    public PopulationNode selection() {
        int nodeIndex = randomNumber(0, numberOfNodes);
        PopulationNode populationNode = currentPopulation.get(nodeIndex);
        if (populationNode.getNodeId().equals(currentRecord.getNodeId())) {
            selection();
        }
        return populationNode;
    }
    //STEP 3
    public PopulationNode cross(PopulationNode firstNode, PopulationNode secondNode) {
        List<Integer> selectedItems = new ArrayList<>();
        for (int i = 0; i < numberOfNodes; i++) {
            PopulationNode nodeForInsert = checkForCross() ? firstNode : secondNode;
            selectedItems.add(nodeForInsert.getVector().get(i));
        }
        PopulationNode newNode = new PopulationNode(selectedItems);
        newNode.countParameters(items);
        return newNode;
    }

    //STEP 4
    public MutationResponse mutation(PopulationNode populationNode) {
        int firstGeneIndex = randomNumber(0, numberOfNodes);
        int secondGeneIndex = randomNumber(0, numberOfNodes);
        while (secondGeneIndex == firstGeneIndex) {
            secondGeneIndex = randomNumber(0, numberOfNodes);
        }
        if (checkProbability(5) && isEnoughGenes(populationNode)) {
            Integer firstGeneValue = populationNode.getVector().get(firstGeneIndex);
            Integer secondGeneValue = populationNode.getVector().get(secondGeneIndex);

            populationNode.addGene(firstGeneIndex, secondGeneValue);
            populationNode.addGene(secondGeneIndex, firstGeneValue);
            populationNode.countParameters(items);
        }
        return new MutationResponse(populationNode, populationNode.getTotalWeight() < capacity);
    }

    //STEP 5
    public PopulationNode localImprovement(PopulationNode populationNode) {
        int dif = randomNumber(0, 5);
        int xRange = randomNumber(0, numberOfNodes);
        int randomIndex = randomNumber(xRange, countRange(dif, xRange));
        populationNode.addGene(randomIndex, 1);
        populationNode.countParameters(items);
        return populationNode;
    }

    public boolean checkForCross() {
        return randomDouble(0, 1) < 0.5;
    }

    public boolean checkProbability(int probability) {
        SplittableRandom random = new SplittableRandom();
        return random.nextInt(1, 101) <= probability;
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
        return populationNode.getVector().stream().filter(gene -> gene == 1).count() > 2;
    }

    public int countRange(int dif, int xRange) {
        if (xRange + dif > this.items.size()) {
            xRange = this.items.size() - xRange;
        }
        return xRange;
    }

    public List<Item> generateItems(int count) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            items.add(new Item(randomNumber(2, 11), randomNumber(1, 6)));
        }
        return items;
    }

    public int randomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public double randomDouble(int min, int max) {
        return ((Math.random() * (max - min)) + min);
    }

    public static Integer enterCapacity() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter backpack capacity:");
        int capacity = scanner.nextInt();
        while (capacity < 0) {
            System.out.print("Invalid capacity enter again:");
            capacity = scanner.nextInt();
        }
        return capacity;
    }
}
