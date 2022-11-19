package com.labs.domain;

import java.util.List;
import java.util.UUID;

public class PopulationNode {
    private List<Integer> vector;
    private Integer totalPrice;
    private Integer totalWeight;
    private String nodeId;

    public PopulationNode(List<Integer> vector, Integer totalPrice, Integer totalWeight) {
        this.vector = vector;
        this.totalPrice = totalPrice;
        this.totalWeight = totalWeight;
        this.nodeId = UUID.randomUUID().toString();
    }

    public PopulationNode(List<Integer> vector) {
        this.vector = vector;
        this.totalPrice = 0;
        this.totalWeight = 0;
        this.nodeId = UUID.randomUUID().toString();
    }

    public List<Integer> getVector() {
        return vector;
    }

    public void setVector(List<Integer> vector) {
        this.vector = vector;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Integer totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getNodeId() {
        return nodeId;
    }

    @Override
    public String toString() {
        return "PopulationNode{" +
                "vector=" + vector +
                ", totalPrice=" + totalPrice +
                ", totalWeight=" + totalWeight +
                '}';
    }

    public void addGene(Integer index, Integer value) {
        this.vector.set(index, value);
    }

    public long selectedSize() {
        return vector.stream().filter(num -> num == 1).count();
    }

    public void countParameters(List<Item> items) {
        this.totalPrice = 0;
        this.totalWeight = 0;
        int counter = 0;
        for (Integer item : this.vector) {
            if (item == 1) {
                Item selectedItem = items.get(counter);
//                System.out.println("selectedItem " + selectedItem.getWeight() + "|" + selectedItem.getPrice());
                totalWeight += selectedItem.getWeight();
                totalPrice += selectedItem.getPrice();
            }
            counter++;
        }
//        System.out.println("totalWeight " + totalWeight + "| totalPrice" + totalPrice);
    }
}
